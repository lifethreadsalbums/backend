package com.poweredbypace.pace.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.layout.ProoferSettings.ProofStatus;
import com.poweredbypace.pace.domain.layout.ProoferStats;
import com.poweredbypace.pace.domain.layout.ProoferTrackingEvent;
import com.poweredbypace.pace.domain.layout.ProoferTrackingEvent.ProoferTrackingEventType;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.domain.layout.SpreadComment;
import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.domain.user.User.UserStatus;
import com.poweredbypace.pace.event.ProoferAlbumApprovedEvent;
import com.poweredbypace.pace.event.ProoferAlbumPublishedEvent;
import com.poweredbypace.pace.event.ProoferAlbumUnapprovedEvent;
import com.poweredbypace.pace.event.ProoferBrideUnreadRepliesEvent;
import com.poweredbypace.pace.event.ProoferEditsCompletedEvent;
import com.poweredbypace.pace.event.ProoferEditsCompletedReminderEvent;
import com.poweredbypace.pace.event.ProoferEditsPendingEvent;
import com.poweredbypace.pace.event.ProoferPhotographerUnreadRepliesEvent;
import com.poweredbypace.pace.event.ProoferUnreadRepliesEvent;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.LayoutRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.repository.ProoferEventRepository;
import com.poweredbypace.pace.repository.ProoferSettingsRepository;
import com.poweredbypace.pace.repository.RoleRepository;
import com.poweredbypace.pace.repository.SpreadCommentRepository;
import com.poweredbypace.pace.repository.UserRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.ProoferService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Service
public class ProoferServiceImpl implements ProoferService {

	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SpreadCommentRepository commentRepo;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private LayoutRepository layoutRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProoferSettingsRepository prooferSettingsRepository;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired(required=false)
	private ProoferEventRepository prooferEventRepository;
	
	
	@Override
	public ProoferSettings saveProoferSettings(ProoferSettings settings) throws EmailAlreadyExistsException {
		User user = userRepo.findByEmail(settings.getEmail());
		if (user!=null && !user.hasRole(Role.ROLE_PROOFER_USER)) {
			throw new EmailAlreadyExistsException("Email " + settings.getEmail() + " has been already registered.");
		}
		if (user==null) {
			user = new User();
			user.setCreateDate(new Date());
			user.setStatus(UserStatus.Enabled);
			user.getRoles().add( roleRepository.findByName(Role.ROLE_PROOFER_USER) );
			user.setPassword(settings.getPassword());
		}
		user.setEmail(settings.getEmail());
		user.setFirstName(settings.getFirstName());
		user.setLastName(settings.getLastName());
		user = userRepo.save(user);
		settings.setUser(user);
		
		settings = prooferSettingsRepository.save(settings);
		return settings;
	}
	
	
	@Override
	public ProoferSettings publish(ProoferSettings settings) throws EmailAlreadyExistsException {
		settings.setPublished(true);
		settings = saveProoferSettings(settings);
		
		eventService.sendEvent(new ProoferAlbumPublishedEvent(settings.getProduct()));
		return settings;
	}
	
	@Override
	public ProoferSettings approve(ProoferSettings settings) throws EmailAlreadyExistsException {
		settings.setApproved(true);
		settings = saveProoferSettings(settings);
		
		notificationBroadcaster.broadcast(Notification.create(NotificationType.LayoutApproved, settings));
		eventService.sendEvent(new ProoferAlbumApprovedEvent(settings.getProduct()));
		return settings;
	}
	
	@Override
	public ProoferSettings unapprove(ProoferSettings settings) throws EmailAlreadyExistsException {
		settings.setApproved(false);
		settings = saveProoferSettings(settings);
		
		notificationBroadcaster.broadcast(Notification.create(NotificationType.LayoutUnapproved, settings));
		eventService.sendEvent(new ProoferAlbumUnapprovedEvent(settings.getProduct()));
		return settings;
	}
	
	@Override
	public ProoferSettings getProoferSettings(Long productId) {
		logger.info("getProoferSettings for product id " + productId);
		return prooferSettingsRepository.findByProductId(productId);
	}

	@Override
	public SpreadComment saveComment(SpreadComment c) {
		if (c.getDateCreated()==null) {
			c.setDateCreated(new Date());
		}
		if (c.getDateCompleted()==null && BooleanUtils.isTrue(c.getCompleted())) {
			c.setDateCompleted(new Date());
		}
		c = commentRepo.save(fixRefs(c));
		notificationBroadcaster.broadcast(Notification.create(NotificationType.EntityChange, c));
		return c;
	}
	
	private SpreadComment fixRefs(SpreadComment c) {
		c.setLayout(layoutRepository.findOne(c.getLayout().getId()));
		c.setUser(userRepository.findOne(c.getUser().getId()));
		
		if (c.getParent()!=null) {
			c.setParent(commentRepo.findOne(c.getParent().getId()));
		}
		for(SpreadComment reply:c.getReplies()) {
			fixRefs(reply);
		}
		return c;
	}

	@Override
	public List<SpreadComment> getComments(Long layoutId) {
		return commentRepo.findByLayoutId(layoutId);
	}

	@Override
	public void deleteComment(long id) {
		SpreadComment c = commentRepo.findOne(id);
		commentRepo.delete(c);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.EntityDelete, c));
	}
	
	@Override
	public ProoferStats getProoferStats(Product p) {
		List<SpreadComment> comments = getValidComments(p);
		int numCompleted = 0;
		int numPending = 0;
		int numArchived = 0;
		for(SpreadComment comment:comments) {
			if (BooleanUtils.isTrue(comment.getIsArchived())) {
				numArchived++;
			} else {
				if (BooleanUtils.isTrue(comment.getCompleted())) {
					numCompleted++;
				} else {
					numPending++;
				}
			}
		}
		ProoferStats stats = new ProoferStats();
		stats.setNumCompleted(numCompleted);
		stats.setNumPending(numPending);
		stats.setNumArchived(numArchived);
		return stats;
	}
	
	@Override
	public ProofStatus getProofStatus(Product p) {
		ProoferSettings ps = getProoferSettings(p.getId());
		if (ps==null) return ProofStatus.NoComments;
		if (BooleanUtils.isTrue(ps.getApproved())) return ProofStatus.Approved;
		
		List<SpreadComment> comments = getValidComments(p);
		if (comments.isEmpty()) return ProofStatus.NoComments;
		int numCompleted = 0;
		int numComments = comments.size();
		for(SpreadComment comment:comments) {
			if (BooleanUtils.isTrue(comment.getCompleted())) {
				numCompleted++;
			} 
		}
		if (numCompleted==numComments) 
			return ProofStatus.WaitingOnClient;
		
		return ProofStatus.WaitingOnDesigner;
	}
	
	private long getDateDiffInDays(Date d1, Date d2) {
		long diffInMillies = Math.abs(d1.getTime() - d2.getTime());
	    return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	private long getDateDiffInHours(Date d1, Date d2) {
		long diffInMillies = Math.abs(d1.getTime() - d2.getTime());
	    return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}
	
	private void saveTrackingEvent(ProoferTrackingEventType type, Product p, long checksum) {
		ProoferTrackingEvent event = new ProoferTrackingEvent();
		event.setDate(new Date());
		event.setProduct(p);
		event.setType(type);
		event.setChecksum(checksum);
		prooferEventRepository.save(event);
	}
	
	@Override
	public void trackComments(Product p) {
		ProofStatus proofStatus = getProofStatus(p);
		ProoferStats stats = getProoferStats(p);
		
		if (proofStatus==ProofStatus.NoComments || proofStatus==ProofStatus.Approved) return;
		long checksum = getCommentsChecksum(p);
		Date now = new Date();
		
		if (proofStatus==ProofStatus.WaitingOnClient) {
			//all edits have been completed by photographer
			List<ProoferTrackingEvent> events = prooferEventRepository.findByProductAndTypeOrderByDateDesc(p, 
				ProoferTrackingEventType.EditsCompleted);
			if (events.size()==0 || events.get(0).getChecksum().longValue()!=checksum) {
				saveTrackingEvent(ProoferTrackingEventType.EditsCompleted, p, checksum);
				eventService.sendEvent(new ProoferEditsCompletedEvent(p));
			}
			if (events.size()>0 && events.get(0).getChecksum().longValue()==checksum &&
				stats.getNumArchived() < stats.getNumCompleted()) {
				//Photographer has marked all the edits as completed edits but the bride has not yet logged in to see them. 
				ProoferTrackingEvent lastEvent = events.get(0);
				
				long diffInDays = getDateDiffInDays(now, lastEvent.getDate());
			    if (diffInDays>=3) {
			    	//check if reminder has been sent
			    	List<ProoferTrackingEvent> reminderEvents = prooferEventRepository.findByProductAndTypeOrderByDateDesc(p, 
						ProoferTrackingEventType.EditsCompletedReminder);
			    	if (reminderEvents.size()==0) {
						saveTrackingEvent(ProoferTrackingEventType.EditsCompletedReminder, p, checksum);
						eventService.sendEvent(new ProoferEditsCompletedReminderEvent(p));
			    	} else {
			    		diffInDays = getDateDiffInDays(now, reminderEvents.get(0).getDate());
			    		if (diffInDays>=7) {
			    			saveTrackingEvent(ProoferTrackingEventType.EditsCompletedReminder, p, checksum);
			    			eventService.sendEvent(new ProoferEditsCompletedReminderEvent(p));
			    		}
			    	}
			    }
			}
		} else if (proofStatus==ProofStatus.WaitingOnDesigner) {
			List<ProoferTrackingEvent> events = prooferEventRepository.findByProductAndTypeOrderByDateDesc(p, 
				ProoferTrackingEventType.EditsPending);
			if (events.size()==0 || events.get(0).getChecksum().longValue()!=checksum) {
				ProoferTrackingEvent event = new ProoferTrackingEvent();
				event.setDate(new Date());
				event.setProduct(p);
				event.setType(ProoferTrackingEventType.EditsPending);
				event.setChecksum(checksum);
				prooferEventRepository.save(event);
				eventService.sendEvent(new ProoferEditsPendingEvent(p));
			} 
		}
	}
	
	@Override
	public void trackReplies(Product p) {
		ProofStatus proofStatus = getProofStatus(p);
		if (proofStatus==ProofStatus.NoComments || proofStatus==ProofStatus.Approved) return;
		
		Date now = new Date();
		//track unread replies
		List<SpreadComment> comments = getValidComments(p);
		Map<User, List<SpreadComment>> repliesByUser = new HashMap<User, List<SpreadComment>>();
		for(SpreadComment c:comments) {
			for(SpreadComment reply:c.getReplies()) {
				long diffInHours = getDateDiffInHours(now, reply.getDateCreated());
				if (BooleanUtils.isNotTrue(reply.getIsRead()) && 
					reply.getUser() != c.getUser() &&
					diffInHours >= 6) {
					
					if (!repliesByUser.containsKey(c.getUser())) {
						repliesByUser.put(c.getUser(), new ArrayList<SpreadComment>());
					}
					repliesByUser.get(c.getUser()).add(reply);
				}
			}
		}
		for(List<SpreadComment> replies:repliesByUser.values()) {
			if (replies.size()>0) {
				ProoferUnreadRepliesEvent event = null;
				if (replies.get(0).getUser()==p.getUser()) {
					event = new ProoferBrideUnreadRepliesEvent(p, replies);
				} else {
					event = new ProoferPhotographerUnreadRepliesEvent(p, replies);
				}
				eventService.sendEvent(event);
			}
		}
	}
	
	@Override
	public void trackComments() {
		List<Product> products = productRepository.findByStateAndParentIsNull(ProductState.New);
		for(Product p: products) {
			trackComments(p);
		}
	}
	
	@Override
	public void trackReplies() {
		List<Product> products = productRepository.findByStateAndParentIsNull(ProductState.New);
		for(Product p: products) {
			trackReplies(p);
		}
	}
	
	private long getCommentsChecksum(Product p) {
		StringBuilder builder = new StringBuilder();
		List<SpreadComment> comments = getValidComments(p);
		for(SpreadComment c:comments) {
			builder.append(c.getSpreadId());
			builder.append(c.getElementId());
			builder.append(c.getCompleted());
		}
		byte bytes[] = builder.toString().getBytes();
		Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);
		return checksum.getValue();
	}
	
	private List<SpreadComment> getValidComments(Product p) {
		Layout layout = p.getLayout();
		if (layout==null) return new ArrayList<SpreadComment>();
		
		List<SpreadComment> comments = commentRepo.findByLayoutId(layout.getId());
		List<String> elementIds = new ArrayList<String>();
		for(Spread s:layout.getSpreads()) {
			for(Element el:s.getElements()) {
				elementIds.add(el.getInternalId());
			}
		}
		List<SpreadComment> result = new ArrayList<SpreadComment>();
		for(SpreadComment comment:comments) {
			boolean isValid = true;
			if (comment.getElementId()!=null && !elementIds.contains(comment.getElementId()))
				isValid = false;
			if (!isValid) continue;
			result.add(comment);
		}
		return result;
	}
	
}

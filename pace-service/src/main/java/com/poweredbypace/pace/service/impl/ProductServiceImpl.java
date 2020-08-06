package com.poweredbypace.pace.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionDate;
import com.poweredbypace.pace.domain.ProductOptionElement;
import com.poweredbypace.pace.domain.ProductOptionFile;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TProductOptionValue;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.FilmStrip;
import com.poweredbypace.pace.domain.layout.FilmStripImageItem;
import com.poweredbypace.pace.domain.layout.FilmStripItem;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.event.BulkProductStateChangedEvent;
import com.poweredbypace.pace.event.ProductStateChangedEvent;
import com.poweredbypace.pace.manager.ProductManager;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.AttachmentRepository;
import com.poweredbypace.pace.repository.BatchRepository;
import com.poweredbypace.pace.repository.ImageStampElementRepository;
import com.poweredbypace.pace.repository.LayoutRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.repository.ProoferEventRepository;
import com.poweredbypace.pace.repository.ProoferSettingsRepository;
import com.poweredbypace.pace.repository.PrototypeProductOptionRepository;
import com.poweredbypace.pace.repository.PrototypeProductOptionValueRepository;
import com.poweredbypace.pace.repository.TProductOptionValueRepository;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.ProductPrototypeService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.JsonUtil;


@Service
public class ProductServiceImpl implements ProductService, ProductManager {

	private final Log log = LogFactory.getLog(ProductServiceImpl.class);
	
	
	@Autowired
	private ProductPrototypeService prototypeProductService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private BatchRepository batchRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private ImageStampElementRepository imageStampElementRepo;
	
	@Autowired(required=false)
	private Env env;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	@PersistenceContext(unitName="paceUnit")
	private EntityManager entityManager;
	
	@Autowired
	private LayoutRepository layoutRepo;
	
	@Autowired
	private AttachmentRepository attachmentRepo;
	
	@Autowired
	private ProoferSettingsRepository prooferSettingsRepository;
	
	@Autowired
	private ProoferEventRepository prooferEventRepository;
	
	@Autowired
	private EventService eventService;
	
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Attachment getAttachment(Product product, AttachmentType attachmentType) {
		List<Attachment> list = attachmentRepo.findByProductAndType(product, attachmentType);
		return list.size()>0 ? list.get(0) : null;
	}
	
	@Transactional(value=TxType.REQUIRES_NEW)
	public Attachment saveAttachment(Product product, String url, AttachmentType attachmentType, User user) {
		
		//save attachment
		List<Attachment> attachments = attachmentRepo.findByProductAndType(product, attachmentType);
		int version = 1;
		if (attachments.size()>0) {
			version = attachments.get(attachments.size()-1).getDocumentVersion() + 1;
			attachmentRepo.delete(attachments);
		}
		Attachment a = new Attachment();
		a.setUser(user);
		a.setProduct(product);
		a.setType(attachmentType);
		a.setDocumentVersion(version);
		a.setUrl(url);
		a.setDate(new Date());
		a.setChecksum( this.getProductChecksum(product) );
		attachmentRepo.save(a);
		
		return a;
	}
	
	@Transient
	public long getProductChecksum(Product p) {
		StringBuilder builder = new StringBuilder();
		builder.append(JsonUtil.serialize(p.getProductOptions()));
		Layout layout = layoutService.getEffectiveLayout(p);
		if (layout!=null) {
			for(Spread s:layout.getSpreads()) {
				for(Element el:s.getElements()) {
					builder.append(JsonUtil.serialize(el));
				}
			}
		}
		byte bytes[] = builder.toString().getBytes();
		Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);
		return checksum.getValue();
	}
	
	
	public List<Product> getByUser(User user) {
		return productRepo.findByUserAndParentIsNull(user);
	}
	
	public List<Product> getByState(ProductState state) {
		return productRepo.findByStateAndParentIsNull(state);
	}
	
	public List<Product> getByBatchId(long batchId) {
		Batch batch = batchRepo.findOne(batchId);
		return productRepo.findByBatchAndParentIsNull(batch);
	}
	
	public List<Product> getFavourite(User user) {
		return productRepo.findByUserAndIsFavouriteTrueAndParentIsNull(user);
	}
	
	public List<Product> getByUserAndState(User user, ProductState state) {
		return productRepo.findByUserAndStateAndParentIsNull(user, state);
	}
	
	@Override
	public List<Product> getByUserAndName(User user, String name) {
		List<Product> result = new ArrayList<Product>();
		List<Product> products = this.getByUser(user);
		for(Product p:products) {
			if (StringUtils.equalsIgnoreCase(name, p.getName()))
				result.add(p);
		}
		return result;
	}
	
	@Override
	public boolean checkUniqueName(Product product) {
		if (product.getName()==null) return true;
		List<Product> products = productRepo.findByUserAndStateAndParentIsNull(product.getUser(), ProductState.New);
		for(Product p:products) {
			if (!p.getId().equals(product.getId()) && 
				StringUtils.equalsIgnoreCase(p.getName(), product.getName()))
				return false;
		}
		return true;
	}

	@Override
	@Transactional(value=TxType.REQUIRED)
	public Product save(Product product) {
		boolean isNewDuplicate = (product.getId()==null && product.getParent()!=null);
	
		if (product.getUser()==null) {
			if (product.getParent()!=null) {
				product.setUser(product.getParent().getUser());
			} else if (product.getOriginal()!=null) {
				product.setUser(product.getOriginal().getUser());
			} else {
				product.setUser(userService.getCurrentUser());
			}
		}
		
		for(Product child:product.getChildren())
			child.setUser(product.getUser());
		
		if (product.getDateCreated()==null)
			product.setDateCreated(new Date());
		
		//if (!checkUniqueName(product))
		//	throw new ProductNameExistsException();
		
		//calculate prices
		boolean skipPrice = (BooleanUtils.isTrue(product.getIsCoverBuilderWizardEnabled()) && product.getId()==null);
		if (!skipPrice) {
			if (product.getParent()!=null) {
				pricingService.executePricing(product.getParent());
				productRepo.save(product.getParent());
			} else {
				pricingService.executePricing(product);
			}
		}
		
		product.setModified(new Date());
		Product saved = productRepo.save(product);
		
		if (isNewDuplicate && product.getParent().getOrderItem()!=null) {
			product.getParent().getChildren().add(saved);
			Order o = product.getParent().getOrderItem().getOrder();
			orderService.assignOrderNumber(o);
			orderService.save(o);
		}
		
		//notificationBroadcaster.broadcast(Notification.create(NotificationType.EntityChange, product) );
		return saved;
	}
	
	@Override
	@Transactional
	public List<Product> save(List<Product> entities) {
		for(Product p:entities)
			save(p);
		return entities;
	}
	
	@Override
	public void updatePageCount(Product p) {
		//update page counts
		if (p.getLayout()==null) return;
		
		Layout layout = p.getLayout();
		int numSpreads = layout.getSpreads().size();
		
		ProductPageType pageType = p.getPrototypeProduct().getProductPageType();
		int numPages = pageType==ProductPageType.PageBased ?
				(numSpreads - 1) * 2 : numSpreads;
		Integer oldPageCount = p.getPageCount();
		if (oldPageCount==null || (oldPageCount!=null && oldPageCount.intValue()!=numPages)) {
			p.setPageCount(numPages);
			log.debug("Update page count for layout ID=" + layout.getId() + 
					", oldPageCount=" + oldPageCount + ", newPageCount=" + numPages);
		}
		
		for(Product child:p.getChildren()) {
			if (BooleanUtils.isTrue(child.getLinkLayout())) {
				child.setPageCount(numPages);
			}
		}
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public Product reorder(Product product) {
		Product newProduct = copy(product);
		if (product.getLayout()!=null) {
			Layout newLayout = layoutService.copy(product.getLayout());
			
			//copy filmstrip
			FilmStrip filmstrip = new FilmStrip();
			FilmStrip parentFilmstrip = product.getLayout().getFilmStrip();
			
			for(FilmStripItem item:parentFilmstrip.getItems()) {
				if (item instanceof FilmStripImageItem) {
					FilmStripImageItem imageItem = (FilmStripImageItem) item;
					FilmStripImageItem newImageItem = new FilmStripImageItem();
					newImageItem.setImage(imageItem.getImage());
					newImageItem.setFilmStrip(filmstrip);
					filmstrip.getItems().add(newImageItem);
				}
			}
			newLayout.setFilmStrip(filmstrip);
			newLayout = layoutRepo.save(newLayout);
			newProduct.setLayout(newLayout);
		}
		newProduct.setState(ProductState.New);
		newProduct.setDateCreated(new Date());
		newProduct.setLinkLayout(false);
		newProduct.setProductNumber(null);
		newProduct.setOnHold(false);
		newProduct.setIsReprint(product.getIsReprint());
		newProduct.setUser(product.getUser());
		
		for(Product child:product.getChildren()) {
			Product newChild = copy(child);
			newChild.setState(ProductState.New);
			newChild.setDateCreated(new Date());
			newChild.setLinkLayout(child.getLinkLayout());
			newChild.setProductNumber(null);
			newChild.setOnHold(false);
			newChild.setIsReprint(child.getIsReprint());
			newChild.setUser(child.getUser());
			
			if (child.getLayout()!=null && child.getLayout()!=product.getLayout()) {
				Layout newChildLayout = layoutService.copy(child.getLayout());
				newChildLayout = layoutRepo.save(newChildLayout);
				newChild.setLayout(newChildLayout);
			}
			newChild.setParent(newProduct);
			newProduct.getChildren().add(newChild);
		}
		
		List<Product> products = this.getByUser(product.getUser());
		int numProducts = 2;
		boolean uniqueName = true;
		String name = newProduct.getName() + "-" + numProducts;
		do {
			uniqueName = true;
			for(Product p:products) {
				if (!p.getId().equals(product.getId()) && 
					StringUtils.equalsIgnoreCase(p.getName(), name)) {
					numProducts++;
					name = newProduct.getName() + "-" + numProducts;
					uniqueName = false;
				}
			}
		} while(!uniqueName);
		newProduct.setName(name);
		
		newProduct = productRepo.save(newProduct);
		pricingService.executePricing(newProduct);
		
		return newProduct;
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public Product createReprint(Product p)
	{
		if (BooleanUtils.isTrue(p.getIsReprint())) {
			p = p.getOriginal();
		}
		Order order = null;
		if (p.getOrderItem()!=null) {
			order = p.getOrderItem().getOrder();
		} else if (p.getParent()!=null && p.getParent().getOrderItem()!=null) {
			order = p.getParent().getOrderItem().getOrder();
		}
		
		Product reprint = copy(p);
		
		int version = productRepo.countByOriginalAndIsReprintTrue(p) + 1;
		String productNumber = p.getProductNumber();
		if (productNumber==null) productNumber = p.getName();
		
		reprint.setIsReprint(true);
		reprint.setProductNumber(productNumber + "R");
		reprint.setName(productNumber + "-REPRINT_v" + Integer.toString(version));
		reprint.setState(ProductState.Preflight);
		reprint.setDateCreated(new Date());
		
		reprint.setUser(p.getUser());
		reprint.setOriginal(p);
		pricingService.executePricing(reprint);
		reprint = productRepo.save(reprint);
		
		if (order!=null) {
			order = orderService.get(order.getId());
			
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderItemNumber(reprint.getProductNumber());
			orderItem.setProduct(reprint);
			orderItem.setOrder(order);
			
			order.getOrderItems().size();
			order.getOrderItems().add(orderItem);
			orderService.save(order);
			
			entityManager.refresh(reprint);
		}
		
		return reprint;
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public List<Product> changeState(List<Product> products, ProductState state) {
		for(Product product:products) {
			ProductState prevState = product.getState();
			for(Product p:product.getProductAndChildren()) {
				
				if (state!=ProductState.OnHold) {
					p.setState(state);
					p.setOnHold(false);
				} else {
					p.setOnHold(true);
				}
				
				if (p.getBatch()==null && (state==ProductState.Printing || state==ProductState.Printed)) {
					p.setBatch(batchService.getPendingBatch());
				}
				if (state==ProductState.Preflight) {
					p.setBatch(null);
				}
				productRepo.save(p);
			}
			eventService.sendEvent(new ProductStateChangedEvent(product, prevState));
		}
		
		//group products by order ID
		Map<Long, List<Product>> productsByOrder = new HashMap<Long, List<Product>>();
		for(Product p:products) {
			OrderItem orderItem = p.getParent()!=null ? p.getParent().getOrderItem() : p.getOrderItem();
			
			if (orderItem!=null) {
				Long id = orderItem.getOrder().getId();
				if (!productsByOrder.containsKey(id)) {
					productsByOrder.put(id, new ArrayList<Product>());
				}
				productsByOrder.get(id).add(p);
			}
		}
		for(List<Product> group:productsByOrder.values()) {
			
			Product firstProduct = group.get(0);
			OrderItem orderItem = firstProduct.getParent()!=null ? 
				firstProduct.getParent().getOrderItem() : firstProduct.getOrderItem();
			int numOrderItems = 0;
			for(OrderItem oi: orderItem.getOrder().getOrderItems()) {
				numOrderItems += oi.getProduct().getProductAndChildren().size();
			}
			
			if (group.size()==numOrderItems) {
				//send one email, all order items have been changed
				eventService.sendEvent(new BulkProductStateChangedEvent(products, state));
			} else {
				for(Product p:group) {
					List<Product> l = new ArrayList<Product>();
					l.add(p);
					eventService.sendEvent(new BulkProductStateChangedEvent(l, state));
				}
			}
		}
		
		notificationBroadcaster.broadcast(Notification.create(NotificationType.ProductStateChanged, products));
		return products;
	}
	
	@Override
	public Product findOne(long id) {
		return productRepo.findOne(id);
	}
	
	@Override
	public Product getProduct(Long id) {
		return productRepo.findOne(id);
	}

	public Product createProductFromPrototype(PrototypeProduct prototypeProduct) {
		Product product = new Product();
		product.setPrototypeProduct(prototypeProduct);
		product.setStore(env.getStore());
		
		for(PrototypeProductOption prototypeProductOption : prototypeProduct.getPrototypeProductOptions()) {
			try {
				ProductOption<?> productOption = prototypeProductOption.getProductOptionType().getProductOptionClass().newInstance();
				if (prototypeProductOption.getSystemAttribute()==SystemAttribute.DateCreated &&
					productOption.getClass().isAssignableFrom(ProductOptionDate.class)) {
					ProductOptionDate poDate = (ProductOptionDate) productOption;
					poDate.setValue(new Date());
				}
				productOption.setProduct(product);
				productOption.setPrototypeProductOption(prototypeProductOption);
				product.getProductOptions().add(productOption);
			} catch (Exception e) {
				log.error("", e);
			} 
		}
		
		return product;
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Product copy(Product product) {
		Product newProduct = createProductFromPrototype(product.getPrototypeProduct());
		
		for(ProductOption<?> productOption:product.getProductOptions()) {
			String optionCode = productOption.getPrototypeProductOption().getEffectiveCode();
			ProductOption newProductOption = newProduct.getProductOptionByCode(optionCode);
			
			if (productOption.getClass().isAssignableFrom(ProductOptionElement.class)) {
				ProductOptionElement poElement = (ProductOptionElement) productOption;
				ProductOptionElement newPoElement = (ProductOptionElement) newProductOption;
				if (poElement.getElement()!=null) {
					
					Element el = poElement.getElement();
					
					Hibernate.initialize(el);
					if (el instanceof HibernateProxy) { 
						el = (Element) ((HibernateProxy) el).getHibernateLazyInitializer().getImplementation();
					}
					
					Element newElement = null;
					try {
						newElement = el.getClass().newInstance();
						el.copy(newElement);						
					} catch (InstantiationException e) {
					} catch (IllegalAccessException e) {
					}
					
					newPoElement.setValue(newElement);
				} 
				
				
			} else if (productOption.getClass().isAssignableFrom(ProductOptionFile.class)) {
				//ProductOptionFile poFile = (ProductOptionFile) productOption;
				//poFile.setValue( mapper.readValue(jsonValue.traverse(), File.class) );
			} else {
				newProductOption.setValue(productOption.getValue());
			}
			
		}
		newProduct.setProductNumber(product.getProductNumber());
		newProduct.setState(product.getState());
		newProduct.setUser(product.getUser());
		
		return newProduct;
	}
	
	public Product createProductFromPrototype(long prototypeId) {
		PrototypeProduct prototypeProduct = prototypeProductService.getById(prototypeId);
		Product p = createProductFromPrototype(prototypeProduct);
		return p;
	}

	@Override
	public List<Product> findAll() {
		return productRepo.findAll();
	}

	@Override
	@Transactional
	public void delete(Product product) {
		if (product.getParent()!=null && product.getParent().getOrderItem()!=null) {
			//duplicate being removed;
			product.getParent().getChildren().remove(product);
			productRepo.save(product.getParent());
			Order o = product.getParent().getOrderItem().getOrder();
			orderService.assignOrderNumber(o);
			orderService.save(o);
			return;
		}
		if (product.getOrderItem()!=null) {
			orderService.deleteOrderItem(product.getOrderItem());
			product.setOrderItem(null);
		}
		attachmentRepo.delete(attachmentRepo.findByProduct(product));
		ProoferSettings ps = prooferSettingsRepository.findByProduct(product);
		if (ps!=null) prooferSettingsRepository.delete(ps);
		prooferEventRepository.delete(prooferEventRepository.findByProduct(product));
		
		productRepo.delete(product);
	}

	@Override
	@Transactional
	public void delete(List<Product> entities) {
		for(Product p:entities) {
			if (p.isReprint()) {
				delete(p);
			}
		}
		for(Product p:entities) {
			if (!p.isReprint()) {
				delete(p);
			}
		}
	}

	@Override
	public List<Product> findAll(List<Long> ids) {
		return productRepo.findAll(ids);
	}

	@Override
	@Transactional(value=TxType.REQUIRED)
	public void delete(long id) {
		Product p = productRepo.findOne(id);
		delete(p);
	}

	@Override
	public int countImageStampElements(ImageFile imageFile) {
		return imageStampElementRepo.countByImageFile(imageFile);
	}

	@Override
	public List<BigInteger> findByImageFileId(long id) {
		//return new ArrayList<Product>();
		return productRepo.findByImageFileId(id);
	}
	
	@Async
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateProductThumb(Layout layout) {
		List<Product> products = productRepo.findByLayoutAndParentIsNull(layout);
		
		for(Product p:products) {
		
			if (p!=null && p.getLayout()!=null && p.getLayout().getFilmStrip()!=null && 
				p.getLayout().getFilmStrip().getItems().size()>0) {
				log.debug("Generating thumb for project ID=" + p.getId());
				FilmStripItem item = p.getLayout().getFilmStrip().getItems().get(0);
				if (item instanceof FilmStripImageItem) {
					FilmStripImageItem imageItem = (FilmStripImageItem)item;
					try {
						storageService.copyFile(ApplicationConstants.THUMB_IMAGE_PATH + imageItem.getImage().getUrl(), 
								"images/product-thumb/" + p.getId() + ".jpg");
					} catch(Exception e) {
						
					}
				}
			}
		
		}
	}
	
	public List<Product> search(String query) {
		return null;
	}

	@Override
	public PrototypeProduct getPrototype(long id) {
		return prototypeProductService.getById(id);
	}
	
	@Autowired
	private PrototypeProductOptionValueRepository prototypeProductOptionValueRepository;
	
	@Autowired
	private PrototypeProductOptionRepository prototypeProductOptionRepository;
	
	@Autowired
	private TProductOptionValueRepository tProductOptionValueRepository;
	
	
	@Override
	@Cacheable(value="prototypeProductOptionValue", key="#id")
	public PrototypeProductOptionValue getPrototypeProductOptionValue(long id) {
		PrototypeProductOptionValue value = 
			prototypeProductOptionValueRepository.findOne(id);
		value.getProductOptionValue().getCode();
		return value;
	}
	
	@Override
	@Cacheable(value="prototypeProductOptionValue", key="#code")
	public TProductOptionValue getProductOptionValueByCode(String code) {
		List<TProductOptionValue> val = tProductOptionValueRepository.findByCode(code);
		if (val!=null && val.size()>0) {
			return val.get(0);
		}
		return null;
	}
	
	@Override
	@Cacheable(value="prototypeProductOption", key="#id")
	public PrototypeProductOption getPrototypeProductOption(long id) {
		PrototypeProductOption value = 
			prototypeProductOptionRepository.findOne(id);
		value.getEffectiveCode();
		return value;
	}

	@Override
	public List<Product> findByQueryAndProductStates(String query, ProductState[] states, Pageable pageRequest) {
		return productRepo.findByQueryAndProductStates(query, states, pageRequest);
	}

	@Override
	public List<Product> findByProductStates(ProductState[] states, Pageable pageRequest) {
		return productRepo.findByProductStates(states, pageRequest);
	}
	
	@Override
	public Layout getLayout(Long id) {
		return layoutRepo.getOne(id);
	}
}

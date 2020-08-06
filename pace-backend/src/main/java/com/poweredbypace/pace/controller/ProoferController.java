package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.layout.ProoferSettings.ProofStatus;
import com.poweredbypace.pace.domain.layout.SpreadComment;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.ProoferService;

@Controller
@RequestMapping(value = "/api/proofer")
public class ProoferController {

	@Autowired
	private ProoferService prooferService;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	private ProductRepository productRepository;
	
	
	@RequestMapping(value="/comment", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public SpreadComment saveComment(@RequestBody SpreadComment comment) {
		return prooferService.saveComment(comment);
	}
	
	@RequestMapping(value="/typing", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void sendTypingEvent(@RequestBody SpreadComment comment) {
		notificationBroadcaster.broadcast(Notification.create(NotificationType.CommentTyping, comment));
	}
	
	@RequestMapping(value="/approve", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ProoferSettings approve(@RequestBody ProoferSettings settings) throws EmailAlreadyExistsException {
		return prooferService.approve(settings);
	}
	
	@RequestMapping(value="/unapprove", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public ProoferSettings unapprove(@RequestBody ProoferSettings settings) throws EmailAlreadyExistsException {
		return prooferService.unapprove(settings);
	}
	
	@RequestMapping(value = "/comments/{layoutId}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<SpreadComment> getComments(@PathVariable("layoutId") long layoutId) {
		return prooferService.getComments(layoutId);
	}
	
	@RequestMapping(value = "/comments/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void delete(@PathVariable long id) {
		prooferService.deleteComment(id);
	}
	
	@RequestMapping(value="/settings", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public ProoferSettings saveSettings(@RequestBody ProoferSettings settings) throws EmailAlreadyExistsException {
		return prooferService.saveProoferSettings(settings);
	}
	
	@RequestMapping(value="/publish", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public ProoferSettings publish(@RequestBody ProoferSettings settings) throws EmailAlreadyExistsException {
		return prooferService.publish(settings);
	}
	
	@RequestMapping(value = "/settings/{productId}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProoferSettings getSettings(@PathVariable("productId") long productId) {
		ProoferSettings result = prooferService.getProoferSettings(productId);
		return result;
	}
	
	@RequestMapping(value = "/status/{productId}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProofStatusDto getProofStatus(@PathVariable("productId") long productId) {
		Product p = productRepository.findOne(productId);
		return new ProofStatusDto(prooferService.getProofStatus(p));
	}
	
	@RequestMapping(value = "/preview/{productId}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProofPreviewData getPreviewData(@PathVariable("productId") long productId) {
		Product p = productRepository.findOne(productId);
		ProofPreviewData result = new ProofPreviewData();
		result.layout = p.getLayout();
		result.coverLayout = p.getCoverLayout();
		result.productPrototype = p.getPrototypeProduct();
		return result;
	}
	
	public static class ProofStatusDto {
		public ProofStatus status;

		public ProofStatusDto(ProofStatus status) { this.status = status; }
		public ProofStatusDto() { }
	}
	
	public static class ProofPreviewData {
		public Layout layout;
		public Layout coverLayout;
		public PrototypeProduct productPrototype;
	}
}

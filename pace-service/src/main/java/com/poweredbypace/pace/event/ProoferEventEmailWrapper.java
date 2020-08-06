package com.poweredbypace.pace.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.layout.ProoferStats;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.domain.layout.SpreadComment;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.repository.ProoferSettingsRepository;
import com.poweredbypace.pace.repository.SpreadCommentRepository;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.ProoferService;
import com.poweredbypace.pace.service.StoreService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProoferEventEmailWrapper extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProoferService prooferService;
	
	@Autowired
	private ProoferSettingsRepository prooferSettingsRepository;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private SpreadCommentRepository spreadCommentRepository;
	
	private ApplicationEvent event;

	public ProoferEventEmailWrapper(ApplicationEvent e) {
		this.event = e;
	}
	
	@PostConstruct
	private void postConstruct() {
		
		this.put("event", event);
		ProoferEvent e = (ProoferEvent) event;
		Product p = productService.findOne(e.getProductId());
		ProoferSettings ps = prooferSettingsRepository.findByProductId(e.getProductId());
		ProoferStats stats = prooferService.getProoferStats(p);
		Store store = storeService.getUserStore(p.getUser());
		
		this.put("product", p);
		this.put("prooferSettings", ps);
		this.put("prooferStats", stats);
		this.put("store", store);
		this.put("prooferUrl", store.getConfig().getProoferUrl() + "#/proof/" + p.getId());
		this.put("publicUrl", store.getConfig().getProoferUrl() + "#/preview/" + p.getId());
		
		if (event instanceof ProoferUnreadRepliesEvent) {
			ProoferUnreadRepliesEvent re = (ProoferUnreadRepliesEvent) event;
			@SuppressWarnings("unchecked")
			List<Long> ids = (List<Long>) CollectionUtils.collect(re.getReplies(), TransformerUtils.invokerTransformer("getId"));

			List<SpreadComment> replies = spreadCommentRepository.findAll(ids);
			List<SpreadCommentDto> dtos = new ArrayList<ProoferEventEmailWrapper.SpreadCommentDto>();
			this.put("replies", dtos);
			
			Layout l = p.getLayout();
			final LayoutSize ls = l.getLayoutSize();
			for(SpreadComment reply:replies) {
				final String spreadId = reply.getParent().getSpreadId();
				final String elementId = reply.getParent().getElementId();
				Spread spread = (Spread) CollectionUtils.find(l.getSpreads(), new Predicate() {
					@Override
					public boolean evaluate(Object spread) {
						return StringUtils.equals(((Spread) spread).getInternalId(), spreadId);
					}
				});
				if (spread==null) continue;
				Element el = (Element) CollectionUtils.find(spread.getElements(), new Predicate() {
					@Override
					public boolean evaluate(Object el) {
						return StringUtils.equals(((Element) el).getInternalId(), elementId);
					}
				});
				if (el!=null) {
					List<Element> els = Arrays.asList(spread.getElements().toArray(new Element[0]));
					Collections.sort(els, new Comparator<Element>() {
						@Override
						public int compare(Element el1, Element el2) {
							return getElementOrder(el1, ls) - getElementOrder(el2, ls);
						}
					});
					
					SpreadCommentDto c = new SpreadCommentDto();
					c.setSpreadBased(p.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased);
					c.setPageNumber(c.isSpreadBased() ? (spread.getPageNumber() / 2) + 1 : spread.getPageNumber());
					c.setText(reply.getParent().getText());
					c.setImageNumber(els.indexOf(el) + 1);
					if (el instanceof ImageElement) {
						ImageElement imEl = (ImageElement)el;
						c.setImageUrl(store.getConfig().getUrlPrefix() + imEl.getImageFile().getThumbImageUrl());
						c.setImageName(imEl.getImageFile().getFilename());
					}
					dtos.add(c);
				}
			}
		}
		
	}
	
	private final static int getElementOrder(Element el, LayoutSize ls) {
		float d = 20;
		int x = Math.round((el.getX() + el.getWidth()/2f) / d);
		int y = Math.round((el.getY() + el.getHeight()/2f) / d);
	    int  page = el.getX() + (el.getWidth()/2f) < ls.getWidth() ? 1 : 2;
	    return x + (y * 1000) + (page * 1000000);
	}
	
	public static class SpreadCommentDto {
		private String text;
		private String imageUrl;
		private int imageNumber;
		private int pageNumber;
		private boolean isSpreadBased;
		private String imageName;
		
		public String getText() { return text; }
		public void setText(String text) { this.text = text; }
		
		public String getImageUrl() { return imageUrl; }
		public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
		
		public int getImageNumber() { return imageNumber; }
		public void setImageNumber(int imageNumber) { this.imageNumber = imageNumber; }
		
		public int getPageNumber() { return pageNumber; }
		public void setPageNumber(int pageNumber) { this.pageNumber = pageNumber; }
		
		public boolean isSpreadBased() { return isSpreadBased; }
		public void setSpreadBased(boolean isSpreadBased) {	this.isSpreadBased = isSpreadBased;	}
		
		public String getImageName() { return imageName; }
		public void setImageName(String imageName) { this.imageName = imageName; }
	}
	
}

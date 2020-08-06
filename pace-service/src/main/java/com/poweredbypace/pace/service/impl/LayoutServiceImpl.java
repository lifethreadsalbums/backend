package com.poweredbypace.pace.service.impl;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.ImageFile.ImageFileStatus;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.ProductOptionInteger;
import com.poweredbypace.pace.domain.PrototypeProduct.FirstPageType;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.CoverTemplate;
import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.FilmStrip;
import com.poweredbypace.pace.domain.layout.FilmStripImageItem;
import com.poweredbypace.pace.domain.layout.FilmStripItem;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.LayoutSize.PageOrientation;
import com.poweredbypace.pace.domain.layout.LayoutSnapshot;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.domain.layout.TextElement;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.exception.LowResImagesException;
import com.poweredbypace.pace.exception.ProductNotDesignedException;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.CoverTemplateRepository;
import com.poweredbypace.pace.repository.CoverTypeRepository;
import com.poweredbypace.pace.repository.FilmStripRepository;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.repository.LayoutRepository;
import com.poweredbypace.pace.repository.LayoutSizeRepository;
import com.poweredbypace.pace.repository.LayoutSnapshotRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.GeomUtils;
import com.poweredbypace.pace.util.JsonUtil;
import com.poweredbypace.pace.util.Numbers;
import com.poweredbypace.pace.util.PaceFileUtils;
import com.poweredbypace.pace.util.UrlUtil;

/**
 * Service for working with layouts
 *
 */
@Service
public class LayoutServiceImpl implements LayoutService {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private LayoutRepository layoutRepo;
	
	@Autowired
	private FilmStripRepository filmstripRepo;
	
	@Autowired 
	private LayoutSizeRepository layoutSizeRepository;
	
	@Autowired
	private CoverTypeRepository coverTypeRepository;
	
	@Autowired
	private CoverTemplateRepository coverTemplateRepository;

	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ImageFileRepository imageFileRepo;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private LayoutSnapshotRepository layoutSnapshotRepo;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	
	
	/***
	 * Creates a deep copy of the given {@link Layout} 
	 * @param layout the layout to be copied
	 * @return the copy of the layout
	 */
	@Override
	public Layout copy(Layout layout) {
		Layout newLayout = new Layout();
		newLayout.setLayoutSize(layout.getLayoutSize());
		newLayout.setFirstPageType(layout.getFirstPageType());
		newLayout.setIsLayFlat(layout.getIsLayFlat());
		for(Spread s:layout.getSpreads()) {
			Spread newSpread = new Spread();
			newSpread.setLayout(newLayout);
			newSpread.setNumPages(s.getNumPages());
			newSpread.setPageNumber(s.getPageNumber());
			newSpread.setInternalId(s.getInternalId());
			newLayout.getSpreads().add(newSpread);
			for(Element el:s.getElements()) {
				Element newElement = null;
				try {
					newElement = el.getClass().newInstance();
					el.copy(newElement);
				} catch (InstantiationException e) {
					log.error("Error while copying elements");
				} catch (IllegalAccessException e) {
					log.error("Error while copying elements");
				}
				newElement.setSpread(newSpread);
				newSpread.getElements().add(newElement);
			}
		}
		return newLayout;
	}
	
	public static final float LF_CENTER_OFFSET = 0.125f * 72f;
	
	/***
	 * Creates a new resized copy of the given layout
	 * @param p			{@link Product} instance associated with the layout
	 * @param layout 	the layout to be copied
	 * @param newSize	the new size
	 * @return the new, resized layout 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Layout resize(Product p, Layout layout, LayoutSize newSize) {
		Layout newLayout = new Layout();
		newLayout.setLayoutSize(newSize);
		newLayout.setFirstPageType(layout.getFirstPageType());
		newLayout.setIsLayFlat(layout.getIsLayFlat());
		boolean isHorizontal = layout.getLayoutSize().getPageOrientation()==PageOrientation.Horizontal;
		
		float scale = newSize.getHeight() / layout.getLayoutSize().getHeight();
		
		float oldWidth = layout.getLayoutSize().getWidth();
		float oldHeight = layout.getLayoutSize().getHeight();
		
		float newWidth = newLayout.getLayoutSize().getWidth();
		float newHeight = newLayout.getLayoutSize().getHeight();
		float centerOffset = 0;
		
		if (p.getPrototypeProduct().getProductPageType()==ProductPageType.PageBased)
			centerOffset = LF_CENTER_OFFSET;
			
		if (BooleanUtils.isTrue(layout.getIsLayFlat())) {
			oldWidth -= ApplicationConstants.LF_HIDDEN_AREA;
			newWidth -= ApplicationConstants.LF_HIDDEN_AREA;
			centerOffset = LF_CENTER_OFFSET;
		}
		
		for(Spread s:layout.getSpreads()) {
			Spread newSpread = new Spread();
			newSpread.setLayout(newLayout);
			newSpread.setNumPages(s.getNumPages());
			newSpread.setPageNumber(s.getPageNumber());
			newLayout.getSpreads().add(newSpread);
			
			boolean frameCrossesSpine = false;
			for (Element el:s.getElements()) {
				if (Numbers.valueOf(el.getX()) + Numbers.valueOf(el.getWidth()) > oldWidth) {
					frameCrossesSpine = true;
					break;
				}
			}
			
			for(Element el:s.getElements()) {
				
				float oldCenterX = oldWidth * (isHorizontal ? s.getNumPages() : 1) / 2.0f;
				float oldCenterY = oldHeight * (!isHorizontal ? s.getNumPages() : 1) / 2.0f;
				
				float newCenterX = newWidth * (isHorizontal ? s.getNumPages() : 1) / 2.0f;
				float newCenterY = newHeight * (!isHorizontal ? s.getNumPages() : 1) / 2.0f;
				
				if (frameCrossesSpine)
				{
					oldCenterX = oldWidth;
					newCenterX = newWidth;
				} else if (Numbers.valueOf(el.getWidth()) < oldWidth ) {
					if (Numbers.valueOf(el.getX()) < oldWidth)
					{
						oldCenterX = (oldWidth / 2.0f) - (centerOffset);
						newCenterX = (newWidth / 2.0f) - (centerOffset * scale);
					} else {
						oldCenterX = (oldWidth * 1.5f) + (centerOffset);
						newCenterX = (newWidth * 1.5f) + (centerOffset * scale);
					}
				}
				
				Element newElement = null;
				try {
					newElement = el.getClass().newInstance();
					el.copy(newElement);
					float x = (( Numbers.valueOf(newElement.getX()) - oldCenterX) * scale) + newCenterX;
					float y = (( Numbers.valueOf(newElement.getY()) - oldCenterY) * scale) + newCenterY;
					newElement.setX(x);
					newElement.setY(y);
					newElement.setWidth( Numbers.valueOf(newElement.getWidth()) * scale);
					newElement.setHeight( Numbers.valueOf(newElement.getHeight()) * scale);
					if (newElement.getClass().isAssignableFrom(ImageElement.class)) {
						ImageElement imgEl = (ImageElement)newElement;
						imgEl.setImageWidth( Numbers.valueOf(imgEl.getImageWidth()) * scale);
						imgEl.setImageHeight( Numbers.valueOf(imgEl.getImageHeight()) * scale);
						imgEl.setImageX( Numbers.valueOf(imgEl.getImageX()) * scale);
						imgEl.setImageY( Numbers.valueOf(imgEl.getImageY()) * scale);
					} else if (newElement.getClass().isAssignableFrom(TextElement.class)) {
						TextElement txtEl = (TextElement)newElement;
						
						if (txtEl.getFontSize()!=null) {
							float fontSize = txtEl.getFontSize();
							txtEl.setFontSize( ((float)fontSize) * scale );
						}
						Map<String,Object> styles = txtEl.getStyles();
						if (styles!=null) {
							for(String lineNumber:styles.keySet()) {
								Map<String,Object> lineStyles = (Map<String, Object>) styles.get(lineNumber);
								for(String charNumber:lineStyles.keySet()) {
									Map<String,Object> charStyles = (Map<String, Object>) lineStyles.get(charNumber);
									if (charStyles.containsKey("fontSize")) {
										int fontSize = (Integer)(charStyles.get("fontSize"));
										charStyles.put("fontSize", ((float)fontSize) * scale);
									}
								}
							}
							txtEl.setStyles(styles);
						}
					}
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
				newSpread.getElements().add(newElement);
			}
		}
		return newLayout;
	}
	
	/***
	 * Creates a new resized copy of the given cover layout
	 * @param layout 	the cover layout to be copied
	 * @param newSize	the new size
	 * @return the new, resized cover layout 
	 */
	private Layout resizeCoverLayout(Layout layout, LayoutSize newSize) {
		Layout newLayout = new Layout();
		newLayout.setLayoutSize(newSize);
		boolean isHorizontal = layout.getLayoutSize().getPageOrientation()==PageOrientation.Horizontal;
		
		float scaleY = newSize.getHeight() / layout.getLayoutSize().getHeight();
		float scaleX = newSize.getWidth() / layout.getLayoutSize().getWidth();
		for(Spread s:layout.getSpreads()) {
			Spread newSpread = new Spread();
			newSpread.setLayout(newLayout);
			newSpread.setNumPages(s.getNumPages());
			newSpread.setPageNumber(s.getPageNumber());
			newLayout.getSpreads().add(newSpread);
			
			float oldCenterX = layout.getLayoutSize().getWidth() * (isHorizontal ? s.getNumPages() : 1) / 2.0f;
			float oldCenterY = layout.getLayoutSize().getHeight() * (!isHorizontal ? s.getNumPages() : 1) / 2.0f;
			
			float newCenterX = newLayout.getLayoutSize().getWidth() * (isHorizontal ? s.getNumPages() : 1) / 2.0f;
			float newCenterY = newLayout.getLayoutSize().getHeight() * (!isHorizontal ? s.getNumPages() : 1) / 2.0f;
			for(Element el:s.getElements()) {
				Element newElement = null;
				try {
					newElement = el.getClass().newInstance();
					el.copy(newElement);
					float x = (( Numbers.valueOf(newElement.getX()) - oldCenterX) * scaleX) + newCenterX;
					float y = (( Numbers.valueOf(newElement.getY()) - oldCenterY) * scaleY) + newCenterY;
					newElement.setX(x);
					newElement.setY(y);
					newElement.setWidth( Numbers.valueOf(newElement.getWidth()) * scaleX);
					newElement.setHeight( Numbers.valueOf(newElement.getHeight()) * scaleY);
					
				} catch (InstantiationException e) {
				} catch (IllegalAccessException e) {
				}
				newSpread.getElements().add(newElement);
			}
			
		}
		
		return newLayout;
	}
	
	/***
	 * Saves the layout
	 * @param layout 	the layout to be saved
	 * @return saved layout 
	 */
	@Override
	@Transactional
	public Layout save(Layout layout) {
		//update page counts
		log.debug("Saving layout, ID=" + layout.getId());
		if (layout.getId()!=null) {
			int numSpreads = layout.getSpreads().size();
			List<Product> products = productRepo.findByLayout(layout);
			
			boolean pageCountUpdated = false;
			for(Product p:products) {
				if (p.getPrototypeProduct().getProductType()==ProductType.DesignableProduct) {
					ProductPageType pageType = p.getPrototypeProduct().getProductPageType();
					int numPages = pageType==ProductPageType.PageBased ?
							(numSpreads - 1) * 2 : numSpreads;
					Integer oldPageCount = p.getPageCount();
					if (oldPageCount==null || (oldPageCount!=null && oldPageCount.intValue()!=numPages)) {
						p.setPageCount(numPages);
						pageCountUpdated = true;
						log.debug("Update page count for layout ID=" + layout.getId() + 
								", oldPageCount=" + oldPageCount + ", newPageCount=" + numPages);
						pricingService.executePricing(p);
					}
				}
			}
			if (pageCountUpdated) {
				productRepo.save(products);
			}
		}
		
		//update element's z-order
		for(Spread s:layout.getSpreads()) {
			int zorder = 0;
			for(Element el:s.getElements()) {
				el.setZorder(zorder++);
			}
		}
		
		Layout savedLayout = layoutRepo.save(layout);
		log.debug("Layout saved, ID=" + layout.getId());
		
		notificationBroadcaster.broadcast(Notification.create(NotificationType.EntityChange, savedLayout) );
		return savedLayout;
	}
	
	/***
	 * Creates a new cover layout for the given product
	 * @param p			{@link Product} instance 
	 * @return the new cover layout 
	 */
	private void createCoverLayout(Product p) {
		Layout coverLayout = null;
		CoverType coverType = p.getCoverType();
		
		if (coverType!=null) {
			LayoutSize layoutSize = p.getLayoutSize();
			LayoutSize coverLayoutSize = layoutSizeRepository.findByCodeAndCoverType(
					layoutSize.getCode(), coverType);
			
			if (coverLayoutSize!=null) {
				coverLayout = p.getCoverLayout()!=null ? p.getCoverLayout() : new Layout();
				
				if (p.getParent()!=null && BooleanUtils.isTrue(p.getLinkLayout())) {
					LayoutSize parentLayoutSize = p.getParent().getLayoutSize();
					CoverType parentCoverType = p.getParent().getCoverType();
					if (coverType.equals(parentCoverType) && layoutSize.equals(parentLayoutSize)) {
						//link cover layout
						p.setCoverLayout(p.getParent().getCoverLayout());
						return;
					}
				}
				
				if (p.getParent()!=null && BooleanUtils.isFalse(p.getLinkLayout()) &&
					p.getParent().getCoverLayout()!=null && p.getCoverLayout()!=null &&
					ObjectUtils.equals(p.getParent().getCoverLayout().getId(), p.getCoverLayout().getId())) { 
					//unlink cover layout
					coverLayout = copy(p.getParent().getCoverLayout());
				}
					
				Spread spread = coverLayout.getSpreads().size()>0 ? 
						coverLayout.getSpreads().get(0) : new Spread();
				spread.setPageNumber(1);
				spread.setNumPages(2);
				spread.setAutoLayout(false);
				spread.setLayout(coverLayout);
				
				if (spread.getId()==null)
					coverLayout.getSpreads().add(spread);
				
				coverLayout.setLayoutSize(coverLayoutSize);
				coverLayout.setFirstPageType(FirstPageType.LeftPageStart);
				coverLayout = layoutRepo.save(coverLayout);
				
				p.setCoverLayout(coverLayout);
			} 
		} 
		p.setCoverLayout(coverLayout);
	}
	
	
	/***
	 * Creates a new layout for the given product
	 * @param p			{@link Product} instance 
	 * @return the new layout 
	 */
	@Override
	@Transactional
	public void createLayout(Product product) {
		
		if (product.getLayout()!=null && 
			ObjectUtils.equals(product.getVersion(), product.getLayout().getProductVersion())) {
			return;
		}
		
		if (product.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct) {
			Layout layout = product.getLayout();
			if (layout == null) {
				layout = new Layout();
				layout.setProductVersion(product.getVersion());
				FilmStrip filmstrip = new FilmStrip();
				filmstrip.setLayout(layout);
				layout.setFilmStrip(filmstrip);
				layout = layoutRepo.save(layout);
				
				product.setLayout(layout);
				return;
			}
			return;
		}
		
		LayoutSize layoutSize = product.getLayoutSize();
		if (layoutSize==null) 
			return;
		
		Integer pageCount = product.getPageCount();
		if (pageCount!=null && pageCount.intValue()==0)
			return;
		
		if (product.getParent()!=null) {
			if (BooleanUtils.isTrue(product.getLinkLayout())) {
				//link layout to parent
				if (product.getParent().getLayout()==null) {
					createLayout(product.getParent());
				}
				
				product.setLayout(product.getParent().getLayout());
				//sync covers
				createCoverLayout(product);
				return;
			} else {
				//unlink
				if (product.getLayout()!=null &&
					product.getParent().getLayout()!=null &&
					ObjectUtils.equals(product.getLayout().getId(), product.getParent().getLayout().getId())) {
					product.setLayout(null);
				}
				
				if (product.getLayout()==null) {
					Layout parentLayout = product.getParent().getLayout();
					if (parentLayout!=null) {
						layoutSize = parentLayout.getLayoutSize();
					}
					Layout layout = copy(parentLayout);
					layout = layoutRepo.save(layout);
					product.setLayout(layout);
				} else {
					layoutSize = product.getLayout().getLayoutSize();
				}
			}
		}
		
		Layout layout = product.getLayout();
		boolean isNewLayout = false;
		if (layout==null) {
			layout = new Layout();
			layout.setAutoFillEnabled(false);
			isNewLayout = true;
		}
		ProductPageType pageType = product.getPrototypeProduct().getProductPageType();
		int numSpreads = pageType==ProductPageType.PageBased ?
				(pageCount / 2 ) + 1 : pageCount;

		if (numSpreads!=layout.getSpreads().size()) {
			log.debug("Number of spreads changed from "+ layout.getSpreads().size() +
					" to " + numSpreads + ", productID="+product.getId() + 
					", layoutID="+layout.getId());
			saveSnapshot(layout);
		}
		GenericRule layFlatRule = genericRuleService.findRule(product, GenericRule.LAY_FLAT_LAYOUT);
		layout.setIsLayFlat(layFlatRule!=null);
		int pageNumber = 1;
		for(int i=0;i<numSpreads;i++) {
			
			Spread spread = i<layout.getSpreads().size() ? layout.getSpreads().get(i) : new Spread();
			spread.setLayout(layout);
			if (pageType==ProductPageType.PageBased && (i==0 || i==numSpreads-1)) 
				spread.setNumPages(1);
			else
				spread.setNumPages(2);
			spread.setPageNumber(pageNumber);
			pageNumber += spread.getNumPages();
			if (spread.getId()==null) {
				spread.setLocked(false);
				layout.getSpreads().add(spread);
			}
		}
		while(layout.getSpreads().size()>numSpreads) {
			layout.getSpreads().remove(layout.getSpreads().size() - 1);
		}
		
		if (layout.getFilmStrip()==null) {
			FilmStrip filmstrip = new FilmStrip();
			filmstrip.setLayout(layout);
			layout.setFilmStrip(filmstrip);
		}
		layout.setFirstPageType(product.getPrototypeProduct().getFirstPageType());
		layout.setLayoutSize(layoutSize);
		layout.setProductVersion(product.getVersion());
		layout = layoutRepo.save(layout);
		
		product.setLayout(layout);
		if (isNewLayout && product.getChildren()!=null && product.getChildren().size()>0) {
			for(Product child:product.getChildren()) {
				if (BooleanUtils.isTrue(child.getLinkLayout())) {
					child.setLayout(layout);
				}
			}
		}
		
		//create cover layout
		for(Product p:product.getProductAndChildren()) {
			createCoverLayout(p);
		}
		
		boolean hasCoverZone = false;
		for(Product p:product.getProductAndChildren()) {
			if (p.getCoverLayout()!=null) {
				hasCoverZone = true;
				break;
			}
		}
		layout.getFilmStrip().setHasCoverZone(hasCoverZone);
		
	}
	
	@Transactional
	@Override
	public Layout publishLayout(Layout l) {
		List<Layout> revisions =  layoutRepo.findByMainLayout(l);
		int rev = 0;
		if (revisions!=null && revisions.size()>0) {
			for(Layout r:revisions) {
				if (r.getRevision()>rev) {
					rev = r.getRevision();
				}
			}
		}
		rev++;
		Layout revision = copy(l);
		revision.setDateCreated(new Date());
		revision.setRevision(rev);
		revision.setMainLayout(l);
		layoutRepo.save(revision);
		
		return revision;
	}
	
	/***
	 * Synchronizes the filmstrip with the parent layout
	 * @param layout 	the layout 
	 * @param product	{@link Product} instance associated with the layout
	 */
	@Override
	public void syncWithParentLayout(Layout layout, Product product) {
		boolean modified = false;
		if (product.getParent()!=null && BooleanUtils.isFalse(product.getLinkLayout())) {
			Layout parentLayout = product.getParent().getLayout();
			FilmStrip filmstrip = layout.getFilmStrip();
			FilmStrip parentFilmstrip = parentLayout.getFilmStrip();
			Map<Long, ImageFile> images = new HashMap<Long, ImageFile>();
			for(FilmStripItem item:filmstrip.getItems()) {
				if (item instanceof FilmStripImageItem) {
					FilmStripImageItem imageItem = (FilmStripImageItem) item;
					images.put(imageItem.getImage().getId(), imageItem.getImage());
				}
			}
			for(FilmStripItem item:parentFilmstrip.getItems()) {
				if (item instanceof FilmStripImageItem) {
					FilmStripImageItem imageItem = (FilmStripImageItem) item;
					if (!images.containsKey(imageItem.getImage().getId())) {
						FilmStripImageItem newImageItem = new FilmStripImageItem();
						newImageItem.setImage(imageItem.getImage());
						newImageItem.setFilmStrip(filmstrip);
						filmstrip.getItems().add(newImageItem);
						modified = true;
					}
				}
			}
		}
		if (modified) {
			layoutRepo.save(layout);
		}
	}
	
	/***
	 * Finds the cover template for the given product
	 * @param product	{@link Product} instance associated with the layout
	 * @return the cover template as ({@link Layout} instance
	 */
	@Override
	public Layout getCoverLayout(Product product) {
		LayoutSize layoutSize = product.getLayoutSize();
		if (layoutSize==null)
			return null;
		
		CoverType coverType = product.getCoverType();
		
		CoverTemplate matchedTemplate = coverTemplateRepository
				.findByPrototypeProductAndCoverTypeAndLayoutSize(
						product.getPrototypeProduct(), 
						coverType, 
						layoutSize);
		
    	if (matchedTemplate==null) {
    		//find any template for this product prototype and cover type
    		List<CoverTemplate> templates = coverTemplateRepository
    				.findByPrototypeProductAndCoverType(
    						product.getPrototypeProduct(), 
    						coverType);
    		
    		if (templates.size()>0) {
    			matchedTemplate = templates.get(0);
    			float w = layoutSize.getWidth().floatValue();
    			float h = layoutSize.getHeight().floatValue();
    			float maxDiff = Float.MAX_VALUE;
    			
    			for(CoverTemplate ct:templates) {
    				float tw = ct.getLayoutSize().getWidth().floatValue();
    				float th = ct.getLayoutSize().getHeight().floatValue();
    				
    				float diff = (Math.abs(tw - w) + Math.abs(th - h)) / 2f;
    				if (diff<maxDiff) {
    					maxDiff = diff;
    					matchedTemplate = ct;
    				}
    			}
    		} else {
    			matchedTemplate = coverTemplateRepository.findOne(1l);
    		}
    	}
    	
    	CoverType ficCoverType = coverTypeRepository.findByCode("fic_cover");
		
		LayoutSize ficLayoutSize = layoutSizeRepository.findByCodeAndCoverType(
				layoutSize.getCode(), 
				ficCoverType);
		if (ficLayoutSize!=null) {
			layoutSize = new LayoutSize(ficLayoutSize);
			layoutSize.setCoverType(null);
		}
    	//get the layout size
    	Layout layout = resizeCoverLayout(matchedTemplate.getLayout(), layoutSize);
    	return layout;
	}
	
	/***
	 * Returns an effective layout for the given product
	 * @param product	{@link Product} instance associated with the layout
	 * @return the layout instance 
	 */
	@Override
	public Layout getEffectiveLayout(Product product) {
		Layout layout = product.getLayout();
		if (product.getParent()!=null && layout==null) {
			layout = product.getParent().getLayout();
		} else if (product.isReprint()) {
			layout = product.getOriginal().getLayout();
		}
		if (layout!=null) {
			LayoutSize layoutSize = product.getLayoutSize();
			if (!layoutSize.equals(layout.getLayoutSize())) {
				Layout resizedLayout = resize(product, layout, layoutSize);
				return resizedLayout;
			}
		}
		return layout;
	}
	
	/***
	 * Returns an effective cover layout for the given product
	 * @param product	{@link Product} instance associated with the layout
	 * @return the layout instance 
	 */
	@Override
	public Layout getEffectiveCoverLayout(Product product) {
		Layout layout = product.getCoverLayout();
		if (layout==null && product.getParent()!=null) {
			Layout parentLayout = product.getParent().getCoverLayout();
			if (parentLayout!=null) {
				CoverType coverType = product.getCoverType();
				LayoutSize layoutSize = product.getLayoutSize();
				LayoutSize coverLayoutSize = layoutSizeRepository.findByCodeAndCoverType(
						layoutSize.getCode(), coverType);
				if (!layoutSize.equals(parentLayout.getLayoutSize())) {
					Layout resizedLayout = resize(product, parentLayout, coverLayoutSize);
					return resizedLayout;
				}
			}
			return parentLayout;
		}
		return layout;
	}
	
	public LayoutSize getEffectiveLayoutSize(Product product, Layout layout) {
		LayoutSize layoutSize = layout.getLayoutSize();
		if (layoutSize.getCoverType()!=null) {
			LayoutSize newLayoutSize = new LayoutSize(layoutSize);
			
			//TODO: make things consistent - spine, hinges are in cm right now
			float pointsPerUnit = ApplicationConstants.POINTS_PER_CM;
			
			float spineWidthBuffer = newLayoutSize.getSpineBuffer();
			
			if (BooleanUtils.isTrue(layoutSize.getDynamicSpineWidth())) {
				float spineWidth = getSpineWidth(product);
				float hingeGap = getHingeGap(product);
				log.debug("spine width = "+spineWidth + ", hingeGap=" + hingeGap);
				newLayoutSize.setSpineWidth((spineWidth * pointsPerUnit) + spineWidthBuffer);
				newLayoutSize.setHingeGap(hingeGap * pointsPerUnit);
			}
			
			float boardWidthBuffer = newLayoutSize.getBoardWidthBuffer();
			float boardHeightBuffer = newLayoutSize.getBoardHeightBuffer();
			
			log.debug("spine width buffer = "+newLayoutSize.getSpineBuffer() + 
					", width buffer=" + newLayoutSize.getBoardWidthBuffer() + 
					", height buffer=" + newLayoutSize.getBoardHeightBuffer());
			
			if (newLayoutSize.getPageOrientation().equals(PageOrientation.Horizontal)) {
				float width = layoutSize.getWidth() + 
					boardWidthBuffer + 
					newLayoutSize.getHingeGap() + 
					(newLayoutSize.getSpineWidth() / 2.0f);

				float height = layoutSize.getHeight() + boardHeightBuffer;
				newLayoutSize.setWidth(width);
				newLayoutSize.setHeight(height);
			} else {
				float width = layoutSize.getWidth() + boardWidthBuffer;
				float height = layoutSize.getHeight() + 
					boardHeightBuffer +
					newLayoutSize.getHingeGap() + 
					((spineWidthBuffer + newLayoutSize.getSpineWidth())	/ 2.0f);
				newLayoutSize.setWidth(width);
				newLayoutSize.setHeight(height);
			}
			
			return newLayoutSize;
		}
		
		return layoutSize;
	}
	
	/***
	 * Returns the spine width for the given product
	 * @param product	{@link Product} instance 
	 * @return the spine width 
	 */
	@Override
	public float getSpineWidth(Product product) {
		return getPageRangeValue(product, GenericRule.SPINE_WIDTH);
	}
	
	/***
	 * Returns the hinge gap width for the given product
	 * @param product	{@link Product} instance associated with the layout
	 * @return the hinge gap 
	 */
	@Override
	public float getHingeGap(Product product) {
		return getPageRangeValue(product, GenericRule.HINGE_GAP);
	}
	
	@Override
	public ImageFile duplicateAndConvert(Layout layout, ImageElement backgroundFrame, Element emptyFrame) {
		
		log.info("Duplicate & Convert");
		float imageX = Numbers.valueOf(backgroundFrame.getX()) + Numbers.valueOf(backgroundFrame.getImageX());
		float imageY = Numbers.valueOf(backgroundFrame.getY()) + Numbers.valueOf(backgroundFrame.getImageY());
		
		float cropX = Numbers.valueOf(emptyFrame.getX()) - imageX;
		float cropY = Numbers.valueOf(emptyFrame.getY()) - imageY;
		float cropW = Numbers.valueOf(emptyFrame.getWidth());
		float cropH = Numbers.valueOf(emptyFrame.getHeight());
		
		float scale = backgroundFrame.getImageFile().getWidth().floatValue() / backgroundFrame.getImageWidth().floatValue();
		
		log.debug("Downloading file "+backgroundFrame.getImageFile().getOriginalImageUrl());
		File imageFile = storageService.getFile(backgroundFrame.getImageFile().getOriginalImageUrl());
		log.debug("Cropping file");
		File croppedFile = imageService.crop(imageFile, (int)(cropW * scale), (int)(cropH*scale), (int)(cropX*scale), (int)(cropY*scale));
		
		FilmStrip filmstrip = layout.getFilmStrip();
		int idx=1;
		boolean uniqueFilename = false;
		String filename = null;
		User user = null;
		while(!uniqueFilename) {
			
			uniqueFilename = true;
			filename = PaceFileUtils.appendStringBeforeExtension(backgroundFrame.getImageFile().getFilename(), "-" + idx);
			for(FilmStripItem item:filmstrip.getItems()) {
				if (item instanceof FilmStripImageItem) {
					ImageFile img = ((FilmStripImageItem)item).getImage();
					user = img.getUser();
					if (StringUtils.equals(filename, img.getFilename())) {
						uniqueFilename = false;
						idx++;
						break;
					}
				}
			}
			
		}
		
		log.debug("Saving image");
		ImageFile image = backgroundFrame.getImageFile().copy();
		
		image.setId(null);
		image.setVersion(null);
		image.setFilename(filename);
		image.setSize(croppedFile.length());
		image.setWidth((int)(cropW*scale));
		image.setHeight((int)(cropH*scale));
		image.setTargetIccProfile(null);
		image.setUser(user);
		
		saveAndUploadImage(image, croppedFile);
		
		log.info("Duplicate & Convert done.");
		return image;
	}
	
	/***
	 * Splits images in half
	 * @param images	List of images to split
	 * @return list of split images
	 */
	@Override
	public List<ImageFile> splitImages(List<ImageFile> images) {
		
		log.info("Splitting images.");
		List<ImageFile> result = new ArrayList<ImageFile>();
		List<ImageFile> deleted = new ArrayList<ImageFile>();
		for(ImageFile image:images) {
			//download image
			log.debug("Downloading image "+image.getOriginalImageUrl());
			image = imageFileRepo.getOne(image.getId());
			File imageFile = storageService.getFile(image.getOriginalImageUrl());

			//crop
			log.debug("Cropping image");
			int leftWidth = image.getWidth() / 2;
			int rightWidth = image.getWidth() - leftWidth;

			File left = imageService.crop(imageFile, leftWidth, image.getHeight(), 0, 0);
			File right = imageService.crop(imageFile, rightWidth, image.getHeight(), leftWidth, 0);

			ImageFile oldImage = image.copy();
			deleted.add(oldImage);

			ImageFile imageLeft = image;
			ImageFile imageRight = image.copy();
			
			imageLeft.setWidth(leftWidth);
			imageLeft.setFilename(PaceFileUtils.appendStringBeforeExtension(imageLeft.getFilename(), "-left"));

			imageRight.setId(null);
			imageRight.setVersion(null);
			imageRight.setWidth(rightWidth);
			imageRight.setFilename(PaceFileUtils.appendStringBeforeExtension(imageRight.getFilename(), "-right"));
			
			saveAndUploadImage(imageLeft, left);
			saveAndUploadImage(imageRight, right);
			
			result.add(imageLeft);
			result.add(imageRight);
			
			left.delete();
			right.delete();
			imageFile.delete();
		}
		
		//delete original image from s3
		log.debug("Deleting original images");
		for(ImageFile image:deleted) {
			String filename = FilenameUtils.getName(image.getUrl());
			storageService.moveFile(image.getOriginalImageUrl(),  "images/archive/" + filename);
			storageService.deleteFile(image.getThumbImageUrl());
			storageService.deleteFile(image.getLowResImageUrl());
		}
		
		log.info("Done.");
		return result;
	}
	
	/***
	 * Checks layout for errors
	 * @param product	{@link Product} instance 
	 */
	@Override
	public void checkLayout(Product product) {
		//check cover layout
		checkCoverLayout(product);
		
		//check main layout;
		checkMainLayout(product);
	}
	
	private void checkMainLayout(Product product) {
		Layout layout = product.getLayout();
		ProductOptionInteger option = (ProductOptionInteger) product.getSystemAttribute(SystemAttribute.PageCount);
		if (layout!=null && option.getValue()!=null) {
			checkLayout(layout, product);
		}
	}
	
	private void checkCoverLayout(Product product) {
		Layout coverLayout = product.getCoverLayout();
		if (coverLayout!=null) {
			boolean coverDesigned = true;
			try {
				checkLayout(coverLayout, product);
			} catch(ProductNotDesignedException ex) {
				coverDesigned = false;
			} 
			if (!coverDesigned) {
				String coverName = "";
				if (coverLayout.getLayoutSize().getCoverType().getLabel()!=null) {
					coverName = coverLayout.getLayoutSize()
						.getCoverType().getLabel().getTranslatedValue().toLowerCase();
				}
				String msg = "You cannot leave your " + coverName +
					" cover blank. Please design your cover or change this project to another material in order to send it in.";
				
				throw new ProductNotDesignedException(msg, product);
			}
		}
	}
	
	private void checkLayout(Layout layout, Product product) {
		
		int numNonBlank = 0;
		int minSpreads = 10;
		int numBrokenImages = 0;
		int numRejectedImages = 0;
		int numEmptyTextBoxes = 0;
		
		boolean isSinglePrint = product.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct;
		boolean isSpreadBased = product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased;
		
		
		if (product.getPrototypeProduct().getMinSpreads()!=null) {
			minSpreads = product.getPrototypeProduct().getMinSpreads().intValue();
		}
		log.debug("Layout ID="+layout.getId() + ", version="+layout.getVersion());
		if (layout.getLayoutSize()!=null && layout.getLayoutSize().getCoverType()!=null) {
			LayoutSize layoutSize = this.getEffectiveLayoutSize(product, layout);
			minSpreads = 1;
			boolean hasElementOnFrontPage = false;
			float width = layoutSize.getWidth() + layoutSize.getGapBetweenPages();
			for(Spread s:layout.getSpreads()) {
				for (Element element : s.getElements()) {
					Rectangle2D.Float bbox = GeomUtils.getBoundingBox(element);
					float right = bbox.x + bbox.width;
					if (right>=width) {
						hasElementOnFrontPage = true;
					}
				}
			}
			if (!hasElementOnFrontPage) 
				throw new ProductNotDesignedException();
			return;
		}
		
		List<Spread> errors = new ArrayList<Spread>();
		List<Spread> lowResErrors = new ArrayList<Spread>();
		for(Spread s:layout.getSpreads()) {
			if (s.getElements().size()>0)
				numNonBlank++;
			if (BooleanUtils.isTrue(s.getHasErrorsLeft()) || BooleanUtils.isTrue(s.getHasErrorsRight())) {
				
				if (s.getErrorTop()!=null && (s.getErrorTop()==2 || s.getErrorTop()==12) &&
					s.getErrorBottom()!=null && (s.getErrorBottom()==2 || s.getErrorBottom()==12)) 
					continue;
				
				errors.add(s);
			}
			if ((s.getNumLowResErrorsLeft()!=null && s.getNumLowResErrorsLeft()>0) ||
				(s.getNumLowResErrorsRight()!=null && s.getNumLowResErrorsRight()>0)) {
				lowResErrors.add(s);
			}
			for(Element el:s.getElements()) {
				if (el instanceof ImageElement) {
					ImageElement imageElement = (ImageElement) el;
					ImageFile imageFile = imageElement.getImageFile();
					if (imageFile!=null) {
						if (imageFile.getStatus()==ImageFileStatus.Rejected && 
							!ImageFile.GENERIC_UPLOAD_ERROR_MESSAGE.equals(imageFile.getErrorMessage()))
							numRejectedImages++;
						else if (imageFile.getStatus()!=ImageFileStatus.Uploaded) {
							numBrokenImages++;
							log.warn(s.getPageNumber() + ",image=" + imageFile.getFilename());
						}
					}
				} else if (el instanceof TextElement) {
					TextElement textEl = (TextElement) el;
					if (StringUtils.equalsIgnoreCase(textEl.getText(), textEl.getPlaceholder())) {
						numEmptyTextBoxes++;
					}
				}
			}
		}
		
		if (isSinglePrint && numEmptyTextBoxes>0) {
			String msg = "Please type in your full name in order to be able to check out.";
			throw new ProductNotDesignedException(msg, product.getParent());
		}
		
		if (numBrokenImages>0 || numRejectedImages>0) {
			/*
			 Message for files still uploading:
			You cannot send this project into production until all files have finished uploading.    OK
			
			Message for files with errors:
			You cannot send this project into production until all files with errors have been correctly uploaded.      OK 
			
			Combined errors
			You cannot send this project into production until all files have finished uploading and all files with errors have been correctly uploaded.
			 */
			String msg = "You cannot send this project into production until all files have finished uploading.";
			if (numRejectedImages>0 && numBrokenImages==0)
				msg = "You cannot send this project into production until all files with errors have been correctly uploaded.";
			if (numRejectedImages>0 && numBrokenImages>0)
				msg = "You cannot send this project into production until all files have finished uploading and all files with errors have been correctly uploaded.";
			
			if (isSinglePrint) {
				throw new ProductNotDesignedException(msg, product.getParent());
			}
			throw new ProductNotDesignedException(msg, product);
		}
		
		if (isSinglePrint && numNonBlank==0 && product.getParent()!=null) {
			String msg = "You cannot add an empty size container to your shopping cart. Please delete it and attempt check out again.";
			throw new ProductNotDesignedException(msg, product.getParent());
		}
		
		if (!BooleanUtils.isTrue(product.getSkipLowResCheck()) && lowResErrors.size()>0) {
			
			/*
			Multiple occurences version
			You have low image quality warnings on pages/spreads N, N, N. 
			Please replace these images with higher resolution versions. 
			If you choose to continue now {company name} IRISbook cannot be held responsible. Do you wish to continue? YES NO

			Single occurrence version
			You have a low image quality warning on page/spread N. 
			Please replace this image with a higher resolution version. 
			If you choose to continue now {company name} IRISbook cannot be held responsible. Do you wish to continue? YES NO
			*/
			
			List<Object> pages = new ArrayList<Object>();
			for(Spread s:lowResErrors) {
				
				int numErrorsLeft = s.getNumLowResErrorsLeft()!=null ? s.getNumLowResErrorsLeft().intValue() : 0;
				int numErrorsRight = s.getNumLowResErrorsRight()!=null ? s.getNumLowResErrorsRight().intValue() : 0;
				
				if (isSpreadBased) {
					int spreadNumber = (s.getPageNumber() + 1) / 2;
					if (numErrorsLeft>0)
						pages.add(spreadNumber + "L");
					if (numErrorsRight>0)
						pages.add(spreadNumber + "R");
				} else {
					if (s.getNumPages()==1 || (s.getNumPages()==2 && numErrorsLeft>0))
						pages.add(s.getPageNumber());
					
					if (s.getNumPages()==2 && numErrorsRight>0)
						pages.add(s.getPageNumber() + 1);
				}
			}
			boolean multiple = pages.size()>1;
			
			String msg = "You have low image quality warning on " + (isSpreadBased ? "spreads: " : "pages: ") +
					StringUtils.join(pages, ", ") +
					". Please replace this image with higher resolution version.";
			if (multiple) {
				msg = "You have low image quality warnings on " + (isSpreadBased ? "spread " : "page ") +
					StringUtils.join(pages, ", ") +
					". Please replace these images with higher resolution versions.";
			}
			
			if (isSinglePrint) {
				msg = "You have low image quality warnings. Please replace these images with higher resolution versions.";
				throw new LowResImagesException(msg, product.getParent());
			}
			
			throw new LowResImagesException(msg, product);
		}
		
		if (errors.size()>0) {
			String msg = "You cannot send this project into production until you fix the layout issues on " + 
				(isSpreadBased ? "spread" : "page");
			if (errors.size()>1) 
				msg += "s";
			
			List<Object> pages = new ArrayList<Object>();
			for(Spread s:errors) {
				if (isSpreadBased) {
					int spreadNumber = (s.getPageNumber() + 1) / 2;
					if (s.getHasErrorsLeft())
						pages.add(spreadNumber + "L");
					if (s.getHasErrorsRight())
						pages.add(spreadNumber + "R");
				} else {
					if (s.getNumPages()==1 || (s.getNumPages()==2 && s.getHasErrorsLeft()))
						pages.add(s.getPageNumber());
					
					if (s.getNumPages()==2 && s.getHasErrorsRight())
						pages.add(s.getPageNumber() + 1);
				}
			}
			msg += " " + StringUtils.join(pages, ", ") + ".";
			throw new ProductNotDesignedException(msg, product);
		}
		
		if (!isSinglePrint && numNonBlank<minSpreads) {
			String msg = null;
			if (product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased) {
				msg = "In order to purchase " + product.getName() + " you must design at least " + minSpreads + " spreads.";
			} else {
				msg = "In order to purchase " + product.getName() + " you must design at least " + (minSpreads * 2) + " pages.";
			}
			throw new ProductNotDesignedException(msg, product);
		}
	}
	
	private void saveAndUploadImage(ImageFile image, File imageFile) {
		
		try {
			log.info("Create lowres and thumbnail images");
			if (image.getId()==null)
				image = imageFileRepo.save(image);
			
			File thumbnail = File.createTempFile("irisbook-image-", "");
			File lowResImage = File.createTempFile("irisbook-image-", "");
	
			imageService.resize(imageFile, lowResImage, 1000, 1000, thumbnail, 240, 240);
	
			String url = UrlUtil.slug(image.getId() + "-" + image.getFilename());
			image.setUrl(url);
	
			//save original image
			log.info("Uploading original image " + url);
			storageService.putFile(imageFile, ApplicationConstants.ORIGINAL_IMAGE_PATH + url);
			
			//save thumbnail
			log.info("Uploading thumbnail image " + url);
			storageService.putFile(thumbnail, ApplicationConstants.THUMB_IMAGE_PATH + url);
			
			//save low-res image
			log.info("Uploading low-res image " + url);
			storageService.putFile(lowResImage, ApplicationConstants.LOW_RES_IMAGE_PATH + url);
			
			thumbnail.delete();
			lowResImage.delete();
			
			imageFileRepo.save(image);
		} catch(Exception e) {
			throw new ImageProcessingException(e);
		}
		
	}
	
	private float getPageRangeValue(Product product, String code) {
		List<PageRangeValue> values = genericRuleService.getRuleCollectionValue(product, code, PageRangeValue.class);
		PageRangeValueCollection valCol = new PageRangeValueCollection(values);
		return valCol.getValue(product.getPageCount());
	}
	
	private void saveSnapshot(Layout layout) {
		try {
			LayoutSnapshot snapshot = new LayoutSnapshot();
			snapshot.setDate(new Date());
			snapshot.setLayoutId(layout.getId());
			snapshot.setLayoutJson(JsonUtil.serialize(layout));
			
			layoutSnapshotRepo.save(snapshot);
		} catch(Exception ex) {
			log.error("Cannot save layout snapshot.", ex);
		}
	}

	/***
	 * Deletes unused images 
	 * @param filmstrip	the filmstrip to remove images from
	 * @param layouts	list of layout to remove images from
	 */
	@Override
	public void deleteUnusedImages(FilmStrip filmstrip, List<Layout> layouts) {
		if (filmstrip==null) return;
		
		List<Long> usedImages = new ArrayList<Long>();
		for(Layout l:layouts) {
			for(Spread s:l.getSpreads()) {
				for(Element el:s.getElements()) {
					if (el instanceof ImageElement) {
						ImageElement imageEl = (ImageElement)el;
						if (imageEl.getImageFile()!=null) {
							usedImages.add(imageEl.getImageFile().getId());
						}
					}
				}
			}
		}
				
		Object[] items = filmstrip.getItems().toArray();
		int numRemoved = 0;
		for(Object item:items) {
			if (item instanceof FilmStripImageItem) {
				ImageFile imageFile = ((FilmStripImageItem) item).getImage();
				if (!usedImages.contains(imageFile.getId())) {
					log.info("Deleting image "+imageFile.getFilename());
					//delete file item
					filmstrip.getItems().remove(item);
					//TODO: delete off S3
					numRemoved++;
				}
			}
		}
		if (numRemoved>0) {
			filmstripRepo.save(filmstrip);
		}
		
	}

	/***
	 * Deletes unused images 
	 * @param layout	the layout to remove images from
	 */
	@Override
	public void deleteUnusedImages(Layout layout) {
		List<Layout> layouts = new ArrayList<Layout>();
		layouts.add(layout);
		deleteUnusedImages(layout.getFilmStrip(), layouts);
	}
}

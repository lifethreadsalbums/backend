package com.poweredbypace.pace.legacy;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.ImageFile.ImageFileStatus;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionBoolean;
import com.poweredbypace.pace.domain.ProductOptionDate;
import com.poweredbypace.pace.domain.ProductOptionElement;
import com.poweredbypace.pace.domain.ProductOptionInteger;
import com.poweredbypace.pace.domain.ProductOptionString;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.layout.FilmStripImageItem;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.legacy.domain.Book;
import com.poweredbypace.pace.legacy.domain.BookDetails;
import com.poweredbypace.pace.legacy.domain.BookDuplicate;
import com.poweredbypace.pace.legacy.domain.Image;
import com.poweredbypace.pace.legacy.domain.IrisbookPaceMap;
import com.poweredbypace.pace.legacy.domain.Order;
import com.poweredbypace.pace.legacy.domain.Page;
import com.poweredbypace.pace.legacy.domain.PlacedElement;
import com.poweredbypace.pace.legacy.domain.StampLine;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.repository.LayoutRepository;
import com.poweredbypace.pace.repository.UserRepository;
import com.poweredbypace.pace.service.InvoiceService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.ProductPrototypeService;
import com.poweredbypace.pace.service.ProductService;



public class IrisbookImportService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@PersistenceContext(unitName="irisbookUnit")
	private EntityManager em;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private ProductPrototypeService prototypeService;
	
	@Autowired
	private ImageFileRepository imageFileRepo;
	
	@Autowired
	private LayoutRepository layoutRepo;
	
	@Autowired
	private OrderService orderService;
	
	@SuppressWarnings("unused")
	@Autowired
	private InvoiceService invoiceService;
	
	
	@Value("${aws.accessKey}")
	private String accessKey;
	
	@Value("${aws.secretKey}")
	private String secretKey;
	
	@Value("${s3.bucket}")
	private String bucketName = "irisstudio";
	
	public void importOrders() {
		String sql = "from Order where orderId in "+
				"(9939,10083,10105,10470,10473,10524,10616,10618,10726,10732,11047,11358,11388,11398,9763,9830,9855,9876,9885,9886,9887,9935,9936,9954,9976,10015,10016,10031,10068,10087,10092,10104,10189,10257,10522,10648,10649,10650,10651,10658,10660,10680,10737,10938,10939,11119,11120,11121,11197,11210,11215,11216,11217,11268,11333,11374,11379,11384,11393,11400,11408,11409,11410,11412,11413,11414,11415)";
				
		List<Order> orders = em.createQuery(sql, Order.class).getResultList();
		
		int i = 1;
		for(Order o:orders) {
			log.info("Importing job "+ i + " of " +orders.size() + 
					", " + o.getJobId() + ", " + o.getJobName() + ", " + 
					o.getBookDetails().getType());
			try {
				importOrder(o.getOrderId());
			} catch (Exception e) {
				log.error("Cannot import job "+o.getJobId() + ", " + o.getJobName(), e);
			}
			i++;
		}
	}
	
	public void fixOrders() {
		String sql = "from Order where orderId in "+
				//"(9571,9051,8618,5200,5530,6775,6931,7056,7150,7154,7642,7889,8069,8348,8349,8359,8385,8418,8524,8546,8556,8557,8559,8750,8793,8798,8801,8822,9000,9129,9162,9205,9254,9257,9265,9270,9289,9295,9317,9334,9361,9422,9524,8625,5859,7201,7204,7206,7228,7952,8201,8486,8487,9520,5546,5839,6726,9508,8240,8389,8338,9279,9559,9347,9043,9391,6338,6539,7924,8103,8424,8271,8025,8558,8754,8053,8194,8308,8371,8290,8478,8733,8976,9500,8412,9510,8719,8458,8125,8126,8129,8037,8549,9343,1836,4616,8540,8261,9542,9452,9456,8651,8942,8864,8111,9389,8561,6668,5020,5129,7334,7335,7575,7623,8327,9552,8303,8038,9543,9056,7643,9458,8039,5186,5885,7250,8347,9421,9538,8232,9097,6181,9568,8650,7483,7980,8986,8987,8996,9181,9368,8943,9348,7977,9511,9021,9203,3517,3819,3825,4417,5577,9514,8808,8833,8612,9226,9285,9315,9336,7979,8169,8501,8502,8930,9357,9556,9523,9461,9206,9207,9455,9555,8381,9411,8805,9547,7956,8275,8289,9515,9109,7687,7947,8176,8302,8305,8351,8352,8353,8386,8472,8550,9209,9383,9384,9385,9386,9413,9485,9495,9535,9537,9558,9562,4760,8787,3949,5342,8293,8829,8903,8904,8905,8920,9208,9370,9377,8040,9048,8698,9221,7973,7325,7800,8219,8800,9516,9268,6414,6426,9387,9388,5120,8504,8505,6993,9545,7243,8122,5375,6562,6936,7616,8581,9263,9301,9302,9231,9521,9426,8497,7971,4300,6792,7124,8151,8998,2003,8334,8453,8784,6719,9338,8220,8637,8014,8098,9449,9469,9470,9564,9566,8216,8026,9423,8925,7312,8277,7751,2407,2629,2631,3446,4384,5070,5135,5136,5917,6034,6049,6093,6094,7046,7048,7336,7455,7461,7466,7550,7693,8299,8300,8450,9399,6977,8535,8885,9018,1422,3372,7478,8768,6110,6111,6904,7082,7083,7086,7384,8451,9170,9528,4398,6482,9446,9539,9540,9560,9321,9496,6485,8429,9450,8631,9262,9475,8211,8664)";
				"(9279)";
				//"(8069,8348,8349,8359,8385,8418,8524,8546,8556,8557,8559,8750,8793,8798,8801,8822,9000,9129,9162,9205,9254,9257,9265,9270,9289,9295,9317,9334,9361,9422,9524,8625,5859,7201,7204,7206,7228,7952,8201,8486,8487,9520,5546,5839,6726,9508,8240,8389,8338,9279,9559,9347,9043,9391,6338,6539,7924,8103,8424,8271,8025,8558,8754,8053,8194,8308,8371,8290,8478,8733,8976,9500,8412,9510,8719,8458,8125,8126,8129,8037,8549,9343,1836,4616,8540,8261,9542,9452,9456,8651,8942,8864,8111,9389,8561,6668,5020,5129,7334,7335,7575,7623,8327,9552,8303,8038,9543,9056,7643,9458,8039,5186,5885,7250,8347,9421,9538,8232,9097,6181,9568,8650,7483,7980,8986,8987,8996,9181,9368,8943,9348,7977,9511,9021,9203,3517,3819,3825,4417,5577,9514,8808,8833,8612,9226,9285,9315,9336,7979,8169,8501,8502,8930,9357,9556,9523,9461,9206,9207,9455,9555,8381,9411,8805,9547,7956,8275,8289,9515,9109,7687,7947,8176,8302,8305,8351,8352,8353,8386,8472,8550,9209,9383,9384,9385,9386,9413,9485,9495,9535,9537,9558,9562,4760,8787,3949,5342,8293,8829,8903,8904,8905,8920,9208,9370,9377,8040,9048,8698,9221,7973,7325,7800,8219,8800,9516,9268,6414,6426,9387,9388,5120,8504,8505,6993,9545,7243,8122,5375,6562,6936,7616,8581,9263,9301,9302,9231,9521,9426,8497,7971,4300,6792,7124,8151,8998,2003,8334,8453,8784,6719,9338,8220,8637,8014,8098,9449,9469,9470,9564,9566,8216,8026,9423,8925,7312,8277,7751,2407,2629,2631,3446,4384,5070,5135,5136,5917,6034,6049,6093,6094,7046,7048,7336,7455,7461,7466,7550,7693,8299,8300,8450,9399,6977,8535,8885,9018,1422,3372,7478,8768,6110,6111,6904,7082,7083,7086,7384,8451,9170,9528,4398,6482,9446,9539,9540,9560,9321,9496,6485,8429,9450,8631,9262,9475,8211,8664)";
		List<Order> orders = em.createQuery(sql, Order.class).getResultList();
		
		int i = 1;
		for(Order o:orders) {
			
			log.info("Processing job "+ i + " of " +orders.size() + 
					", " + o.getJobName() + ", " + o.getBookDetails().getType());
			
				
			try {
				//if (o.getBookDetails().getBookMaterial().equals("FIC") || 
				//	o.getBookDetails().getBookMaterial().equals("1/4 IC")) {
					
					log.info("Importing layout");
					fixLayout(o);
				//}
				//fix end papers
				//fixEndPapers(o);
				
			} catch (Exception e) {
				log.error("Cannot import job "+o.getJobId() + ", " + o.getJobName(), e);
			}
			
			i++;
		}
		log.info("Done.");
	}
	
	public void fixOrder(long id) {
		
		Order o = em.find(Order.class, id);
			
		try {
			log.info("Importing layout");
			fixLayout(o);
		} catch (Exception e) {
			log.error("Cannot import job "+o.getJobId() + ", " + o.getJobName(), e);
		}
		
		log.info("Done.");
	}
	
	public void importOrder(long id) {
		
		Order order = em.find(Order.class, id);
		
		User user = userRepo.findByEmail(order.getUsername());
		if (user==null) {
			log.info("Cannot find user account " + order.getUsername());
			return;
		}
		
		Product p = createProduct(order, order.getBookDetails());
		p.setUser(user);
		
		for(BookDuplicate dup:order.getBookDuplicates()) {
			Product pDup = createProduct(order, dup.getBookDetails());
			pDup.setUser(user);
			pDup.setParent(p);
			p.getChildren().add(pDup);
		}
		
		boolean completed = (order.getJobId()!=null && order.getJobId().length()>0);
		if (completed) {
			p.setProductNumber(order.getJobId());
			p.setState(ProductState.Completed);
		}
		productService.save(p);
		
		Layout l = createLayout(order, p);
		layoutService.save(l);
		
		if (p.getCoverLayout()!=null)
			layoutService.save(p.getCoverLayout());
		
		if (completed) {
			com.poweredbypace.pace.domain.order.Order o = orderService.createOrder(user);
			OrderItem oi = new OrderItem();
			oi.setProduct(p);
			oi.setOrder(o);
			o.getOrderItems().add(oi);
			o.setOrderNumber(p.getProductNumber());
			o.setState(OrderState.Completed);
			o.setCreated(order.getOrderDate());
			orderService.save(o);
		}
		
		//invoiceService.create(o);
		
		log.info("job imported id=" + order.getOrderId() + ", name=" + order.getJobName());
	}
	
	private void fixLayout(Order order) {
		User user = userRepo.findByEmail(order.getUsername());
		
		List<Product> products = productService.getByUserAndName(user, order.getJobName());
		Product p = products.get(0);
		
		Layout l = p.getLayout();
		if (l!=null) {
			p.setLayout(null);
			layoutRepo.delete(l);
		}
		
		l = createLayout(order, p);
		layoutService.save(l);
		
		if (p.getCoverLayout()!=null) {
			layoutService.save(p.getCoverLayout());
		}
	}
	
	@SuppressWarnings("unused")
	private void fixEndPapers(Order order) {
		User user = userRepo.findByEmail(order.getUsername());
		
		List<Product> products = productService.getByUserAndName(user, order.getJobName());
		Product p = products.get(0);
		
		fillOption(p, "endPapersType", order.getBookDetails().getBookEndPapers(), IrisbookPaceMap.END_PAPERS_TYPES);
		fillOption(p, "endPapersColour", order.getBookDetails().getBookEndPapers(), IrisbookPaceMap.END_PAPERS);
		
		int i=0;
		for(BookDuplicate dup:order.getBookDuplicates()) {
			Product child = p.getChildren().get(i++);
			fillOption(child, "endPapersType", dup.getBookDetails().getBookEndPapers(), IrisbookPaceMap.END_PAPERS_TYPES);
			fillOption(child, "endPapersColour", dup.getBookDetails().getBookEndPapers(), IrisbookPaceMap.END_PAPERS);
		}
		productService.save(p);
	}
	
	private Product createProduct(Order o, BookDetails bd) {
		String type = IrisbookPaceMap.TYPES.get(bd.getType());
		PrototypeProduct prototype = prototypeService.getByCode(type);
		Product p = productService.createProductFromPrototype(prototype);
		p.setIsReprint(false);
		
		fillOption(p, "_productPrototype", bd.getType(), IrisbookPaceMap.TYPES);
		fillOption(p, "productType", bd.getType(), IrisbookPaceMap.PRODUCT_TYPES);
		fillOption(p, "_name", bd.getJobName());
		fillOption(p, "_quantity", bd.getQuantity());
		fillOption(p, "_dateCreated", o.getOrderDate());
		fillOption(p, "_studioSample", bd.getStudioSample());
		fillOption(p, "_rush", bd.getPriority());
		fillOption(p, "shape", bd.getShape(), IrisbookPaceMap.SHAPES);
		fillOption(p, "size", bd.getShape());
		fillOption(p, "bookMaterial", bd.getBookMaterial(), IrisbookPaceMap.MATERIALS);
		fillOption(p, "bookColour", bd.getBookColour(), IrisbookPaceMap.COLORS);
		
		fillOption(p, "endPapersType", bd.getBookEndPapers(), IrisbookPaceMap.END_PAPERS_TYPES);
		fillOption(p, "endPapersColour", bd.getBookEndPapers(), IrisbookPaceMap.END_PAPERS);
		fillOption(p, "paperType", bd.getPaperTypeId().toString(), IrisbookPaceMap.PAPER_TYPES);
		fillOption(p, "_pageCount", o.getPageCount());
		fillOption(p, "_notes", bd.getJobComments());
		
		fillOption(p, "boxType", bd.getBoxStyle(), IrisbookPaceMap.BOXES);
		fillOption(p, "boxMaterial", bd.getBoxMaterial(), IrisbookPaceMap.MATERIALS);
		fillOption(p, "boxColour", bd.getBoxColour(), IrisbookPaceMap.COLORS);
		fillOption(p, "boxRibbon", bd.getBoxRibbon(), IrisbookPaceMap.RIBBONS);
		
		fillOption(p, "boxLinersWallsMaterial", bd.getField3(), IrisbookPaceMap.MATERIALS);
		fillOption(p, "boxLinersWallsColour", bd.getField4(), IrisbookPaceMap.COLORS);
		
		fillOption(p, "printType", bd.getField2(), IrisbookPaceMap.PRINT_TYPES);
		fillOption(p, "pageStyle", bd.getField1(), IrisbookPaceMap.PAGE_STYLES);
		
		fillOption(p, "spineStyle", bd.getSpineStyle(), IrisbookPaceMap.SPINE_STYLES);
		fillOption(p, "spineMaterial", bd.getSpineMaterial(), IrisbookPaceMap.MATERIALS);
		fillOption(p, "spineColour", bd.getSpineColour(), IrisbookPaceMap.COLORS);
		
		fillStamp(p, "bookStampText", bd);
		
		return p;
	}
	
	private void fillOption(Product p, String code, Object value) {
		fillOption(p, code, value, null);
	}
	
	private void fillOption(Product p, String optionCode, Object value, Map<String,String> valMap)  {
		
		ProductOption<?>po = p.getProductOptionByCode(optionCode);
		
		if (po==null) {
			log.warn("Cannot fill option "+optionCode + ", value=" + value);
			return;
		}
		
		if (po instanceof ProductOptionValue) {
			String irisVal = (String) value;
			String paceCode = valMap!=null ? valMap.get(irisVal) : irisVal;
			
			PrototypeProductOption option = p.getPrototypeProduct().getOptionByCode(optionCode);
			ProductOptionValue poValue = (ProductOptionValue) po;
			for(PrototypeProductOptionValue val:option.getPrototypeProductOptionValues()) {
				if (val.getCode().equals(paceCode)) {
					poValue.setValue(val);
					break;
				}
			}
		} else if (po instanceof ProductOptionInteger) {
			ProductOptionInteger poInt = (ProductOptionInteger) po;
			poInt.setValue((Integer)value);
		} else if (po instanceof ProductOptionBoolean) {
			ProductOptionBoolean poBool = (ProductOptionBoolean) po;
			poBool.setValue((Boolean)value);
		} else if (po instanceof ProductOptionString) {
			ProductOptionString poString = (ProductOptionString) po;
			poString.setValue((String)value);
		} else if (po instanceof ProductOptionDate) {
			ProductOptionDate poDate = (ProductOptionDate) po;
			poDate.setValue((Date)value);
		}
	
	}
	
	private void fillStamp(Product p, String optionCode, BookDetails bd) {
		
		ProductOption<?>po = p.getProductOptionByCode(optionCode);
		
		if (po==null) {
			log.warn("Cannot fill option "+optionCode);
			return;
		}
		
		if (po instanceof ProductOptionElement && bd.getStampLines().size()>0) {
			ProductOptionElement poEl = (ProductOptionElement)po;
			
			StampLine sl = bd.getStampLines().get(0);
			String text = "";
			for(StampLine sl2:bd.getStampLines()) {
				if (text.length()>0) 
					text += "\r";
				text += sl2.getStampText();
			}
			TextStampElement el = new TextStampElement();
			el.setText(text);
			
			if (sl.getStampFont()==null) {
				sl.setStampFont("Twentieth Century Medium 60pt");
			}
			
			String font = sl.getStampFont().replaceAll("\\s\\d\\dpt$", "");
			int fontSize = Integer.parseInt(sl.getStampFont().replaceAll("[^\\d]", ""));
			el.setFontFamily(IrisbookPaceMap.FONTS.get(font));
			el.setFontSize((float)fontSize);
			el.setOpacity(1f);
			el.setFill("#00000000");
			
			PrototypeProductOption foilOption = p.getPrototypeProduct().getOptionByCode("bookStampFoil");
			for(PrototypeProductOptionValue val:foilOption.getPrototypeProductOptionValues()) {
				if (val.getCode().equals(IrisbookPaceMap.FOILS.get(sl.getStampFoil()))) {
					
					//ProductOptionValue poFoil = (ProductOptionValue) p.getProductOptionByCode("bookStampFoil");
					//poFoil.setValue(val);
					el.setFoilCode(val.getCode());
					break;
				}
			}
			
			PrototypeProductOption posOption = p.getPrototypeProduct().getOptionByCode("bookStampPosition");
			for(PrototypeProductOptionValue val:posOption.getPrototypeProductOptionValues()) {
				if (val.getCode().equals("middle_center")) {
					
					//ProductOptionValue poFoil = (ProductOptionValue) p.getProductOptionByCode("bookStampPosition");
					//poFoil.setValue(val);
					el.setPositionCode(val.getCode());
					break;
				}
			}
			
			poEl.setValue(el);
			
		}
	}
	
	private Layout createLayout(Order order, Product p) {
		Book book = em.find(Book.class, order.getBookId());
		
		if (p.getLayout()!=null) {
			p.getLayout().getSpreads().clear();
		}
		
		layoutService.createLayout(p);
		Layout l = p.getLayout();
		Layout coverLayout = p.getCoverLayout();
		
		if ((coverLayout==null && l.getSpreads().size()!=book.getPages().size()) ||
			(coverLayout!=null && l.getSpreads().size()!=book.getPages().size() - 1)) {
			log.warn("Spread count mismatch, material=" + 
					order.getBookDetails().getBookMaterial() + 
					", pace spreads =" + l.getSpreads().size() + 
					", irisbook spreads =" + book.getPages().size() +
					", page count=" + order.getPageCount());
			//throw new IllegalStateException("Spread count mismatch - cannot import layout");
		}
		
		//TransferManager tx = new TransferManager(new BasicAWSCredentials(accessKey, secretKey));
		
		Map<Long, ImageFile> imageFiles = new HashMap<Long, ImageFile>();
		for(Image image:book.getImages()) {
			if ("PENDING_UPLOAD".equals(image.getStatus()))
				continue;
			ImageFile imageFile = new ImageFile();
			imageFile.setColorSpace(image.getColorSpace());
			imageFile.setCreationDate(image.getCreationDate());
			imageFile.setDpiX(image.getDpiX());
			imageFile.setDpiY(image.getDpiY());
			imageFile.setErrorMessage(image.getErrorString());
			imageFile.setFilename(image.getFilename());
			imageFile.setHeight(image.getHeight());
			imageFile.setIccProfile(image.getIccProfile());
			imageFile.setSize(image.getSize());
			imageFile.setStatus(ImageFileStatus.Uploaded);
			imageFile.setWidth(image.getWidth());
			imageFile.setUser(p.getUser());
			imageFile.setUrl(image.getUrlOriginal().replaceAll("images/original/", ""));
			
			//imageFile = imageFileRepo.save(imageFile);
			
			//String url = UrlUtil.slug(imageFile.getId() + "-" + imageFile.getFilename());
			
			/*
			log.debug("Copying file " + image.getFilename());
			
			try {
				tx.getAmazonS3Client().copyObject("irisbook", image.getUrlOriginal(), 
						bucketName, ApplicationConstants.ORIGINAL_IMAGE_PATH + imageFile.getUrl());
			} catch (Exception e) {
				log.error("Cannot copy file " + image.getImageId() + ", " + image.getUrlOriginal() + ". " + e.getMessage());
			} 
			try {
				tx.getAmazonS3Client().copyObject("irisbook", image.getUrlLowRes(), 
						bucketName, ApplicationConstants.LOW_RES_IMAGE_PATH + imageFile.getUrl());
			} catch (Exception e) {
				log.error("Cannot copy file " + image.getImageId() + ", " + image.getUrlLowRes() + ". " + e.getMessage());
			} 
			try {
				tx.getAmazonS3Client().copyObject("irisbook", image.getUrlThumbnail(), 
						bucketName, ApplicationConstants.THUMB_IMAGE_PATH + imageFile.getUrl());
			} catch (Exception e) {
				log.error("Cannot copy file " + image.getImageId() + ", " + image.getUrlThumbnail() + ". " + e.getMessage());
			}
			*/
			
			imageFiles.put(image.getImageId(), imageFile);
			FilmStripImageItem imageItem = new FilmStripImageItem();
			imageItem.setCurrentOrder(image.getListOrder());
			imageItem.setImage(imageFile);
			imageItem.setFilmStrip(l.getFilmStrip());
			l.getFilmStrip().getItems().add(imageItem);
		}
		
		imageFileRepo.save( imageFiles.values() );
		
		int spreadIdx = 0;
		if (coverLayout!=null) 
			spreadIdx = -1;
		
		for(Page page:book.getPages()) {
			
			Spread spread = spreadIdx>=0 && spreadIdx<l.getSpreads().size() ?
					l.getSpreads().get(spreadIdx) : new Spread();
			
			if (coverLayout!=null && spreadIdx==-1) {
				spread = coverLayout.getSpreads().get(0);
			}
			
			spread.getElements().clear();
			
			if (spread.getLayout()==null) {
				spread.setPageNumber(page.getPageNumber());
				spread.setNumPages(page.getNumPages());
				spread.setLayout(l);
				l.getSpreads().add(spread);
			}
			
			float offsetX = 0;
			if (spread.getNumPages()==1) {
				float width = l.getLayoutSize().getWidth();
				if (BooleanUtils.isTrue( l.getIsLayFlat() ))
						width -= ApplicationConstants.LF_HIDDEN_AREA;
				offsetX = (width/2.0f);
			}
			
			for(PlacedElement pe:page.getPlacedImages()) {
				
				if (pe.getImage()!=null) {
					ImageElement el = new ImageElement();
					el.setImageFile(imageFiles.get(pe.getImage().getImageId()));
					el.setX(pe.getPictureBoxX().floatValue() + offsetX);
					el.setY(pe.getPictureBoxY().floatValue());
					el.setWidth(pe.getPictureBoxWidth().floatValue());
					el.setHeight(pe.getPictureBoxHeight().floatValue());
					el.setImageX(pe.getImageBoxX().floatValue());
					el.setImageY(pe.getImageBoxY().floatValue());
					el.setImageWidth(pe.getImageBoxWidth().floatValue());
					el.setImageHeight(pe.getImageBoxHeight().floatValue());
					el.setOpacity(pe.getOpacity().floatValue()/100f);
					el.setRotation(pe.getRotation().floatValue());
					el.setImageRotation(0f);
					el.setStrokeWidth(pe.getStroke().floatValue());
					if (pe.getStrokeColor()!=null) {
						el.setStrokeColor("#"+Long.toHexString(pe.getStrokeColor()));
					}
					el.setLocked(false);
					el.setSpread(spread);
					spread.getElements().add(el);
				}
				
			}
			spreadIdx++;
		}
		
		return l;
	}

}

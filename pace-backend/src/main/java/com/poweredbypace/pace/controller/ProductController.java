package com.poweredbypace.pace.controller;

import java.text.ParseException;
import java.util.*;

import com.poweredbypace.pace.util.DateUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.EcmaError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.FilmStripImageItem;
import com.poweredbypace.pace.domain.layout.FilmStripItem;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.repository.UserRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.util.SpringContextUtil;

@Controller
@RequestMapping(value = "/api/product")
public class ProductController extends CrudController<Product> {
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PricingService pricingService;
	
	@Override
	protected CrudService<Product> getService() { return productService; }
	
	@Override
	protected JpaRepository<Product, Long> getRepository() { return null; }
	
	@Autowired
	private UserRepository userRepo;
	
	
	@ResponseStatus(value=HttpStatus.OK)
	@RequestMapping(value="/addToCart", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public void addToCart(@RequestBody Product product) {
		orderService.addToCart(product);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Product save(@RequestBody Product product) {
		return productService.save(product);
	}
	
	@RequestMapping(value = "/{id}/reprint", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Product createReprint(@PathVariable("id")long id) {
		Product p = productRepo.findOne(id);
		return productService.createReprint(p);
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Iterable<Product> saveMultiple(@RequestBody List<Product> products) {
		return IteratorUtils.toList(productService.save( products ).iterator());
	}
	
	@RequestMapping(value = "/state", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public List<Product> changeState(@RequestBody ChangeProductStateAction action) {
		List<Product> products = new ArrayList<Product>();
		for(long id:action.productIds) {
			Product product = productRepo.findOne(id);
			if (product!=null) products.add(product);
		}
		return productService.changeState(products, action.state);
	}
	
	@RequestMapping(value="/price", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Product calculatePrice(@RequestBody Product product, @AuthenticationPrincipal User currentUser) {
		
		try {
			product.setUser(currentUser);
			for(Product child:product.getChildren())
				child.setUser(currentUser);
			
			pricingService.executePricing(product);
		} catch(EcmaError e) {
			log.warn("Cannot calculate price. " + e.getMessage());
			ProductPrice price = new ProductPrice();
			price.setSubtotal(new Money(0.0f));
			price.setSubtotalIncludingAdjustements(new Money(0.0f));
			product.setSubtotal(new Money(0.0f));
			product.getProductPrices().clear();
			product.getProductPrices().add(price);
		} catch (Exception e) {
			log.error("Cannot calculate price. ", e);
			throw new RuntimeException(e);
		}
		return product;
	}
	
	@RequestMapping(value = "/{id}/thumbUrl", method = RequestMethod.GET)
	@ResponseBody
	public List<ThumbDto> getThumbUrl(@PathVariable long id) {
		
		List<ThumbDto> result = new ArrayList<ThumbDto>();
		Product p = productService.findOne(id);
		if (p!=null) {
			int numItems = p.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct ? 4 : 1; 
			String defaultUrl = p.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct ? 
					"images/dashboard-photo-default.png" : "images/dashboard-project-default.png";
			Layout layout = p.getLayout();
			if (layout==null && BooleanUtils.isTrue(p.getLinkLayout()) && p.getParent()!=null) {
				layout = p.getParent().getLayout();
			}
			if (layout!=null && layout.getFilmStrip()!=null && 
				layout.getFilmStrip().getItems().size()>0) {
				
				for(int i=0;i<numItems && i<layout.getFilmStrip().getItems().size();i++) {
					FilmStripItem item = layout.getFilmStrip().getItems().get(i);
					if (item instanceof FilmStripImageItem) {
						FilmStripImageItem imageItem = (FilmStripImageItem)item;
						String url = SpringContextUtil.getEnv().getStore().getStorageUrl() + 
							imageItem.getImage().getThumbImageUrl();
						result.add(new ThumbDto(url));
					}
				}
			}
			int n = numItems - result.size();
			for(int i=0;i<n;i++) {
				result.add(new ThumbDto(defaultUrl));
			}
		}
		
		return result;
	} 
	
	@RequestMapping(value = "/{id}/reorder", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Product reorder(@PathVariable long id) {
		Product p = productService.findOne(id);
		Product newProduct = productService.reorder(p);
		return newProduct;
	}
	
	@RequestMapping(value = "", params={"state","pageIndex","pageSize"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getByState(
		@RequestParam(value="state", required=true) ProductState state,
		@RequestParam(value="q", required=false) String q,
		@RequestParam(value="pageIndex", required=true) Integer pageIndex,
		@RequestParam(value="pageSize", required=true) Integer pageSize) {
		
		if (q!=null) {
			ProductState[] states = { state };
			return productRepo.findByQueryAndProductStates(q, states, 
				new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "productNumber")));
		} else {
			return productRepo.findByStateAndParentIsNull(state, 
				new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "productNumber")));
		}
	}
	
	@RequestMapping(value = "", params={"states", "q", "pageIndex","pageSize"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getByQueryAndStatse(
		@RequestParam(value="states", required=true) ProductState[] states,
		@RequestParam(value="q", required=true) String q,
		@RequestParam(value="pageIndex", required=true) Integer pageIndex,
		@RequestParam(value="pageSize", required=true) Integer pageSize) {
		
		return productRepo.findByQueryAndProductStates(q, states, 
			new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "productNumber")));
	}

    @RequestMapping(value = "/searchByDate", params = {"fromDate", "toDate", "pageIndex", "pageSize"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Product> getByDateAndState(
            @RequestParam(value = "states", required = true) ProductState[] states,
            @RequestParam(value = "fromDate", required = true) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate,
            @RequestParam(value = "pageIndex", required = true) Integer pageIndex,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) throws ParseException {
        Date dateFrom = DateUtils.getFormattedDate(fromDate, "yyyy-MM-dd");
        Date dateTo = DateUtils.getFormattedDate(toDate, "yyyy-MM-dd");
        return productRepo.findProductsByFromAndToDate(dateFrom, dateTo, states,
                new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "productNumber")));
    }

	@RequestMapping(value = "", params= {"userId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getByUserId(@RequestParam(required=true) Long userId) {
		User user = userRepo.findOne(userId);
		return productRepo.findByUserAndParentIsNull(user);
	}
	
	@RequestMapping(value = "/count", params="state", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> countByState(@RequestParam(required=true) ProductState state) {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("count", productRepo.countByState(state));
		return result;
	}
	
	@RequestMapping(value = "/count", params="states", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> countByState(@RequestParam(required=true) ProductState[] states) {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("count", productRepo.countByStates(states));
		return result;
	}
	
	@RequestMapping(value = "", params="batchId", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getByBatch(@RequestParam(required=true) long batchId) {
		return productService.getByBatchId(batchId);		
	}
	
	public static class ChangeProductStateAction {
		public long[] productIds;
		public ProductState state;
	}
	
	public static class AddToBatchAction {
		private Long[] productIds;
		private Long batchId;
		
		public Long[] getProductIds() {
			return productIds;
		}
		public void setProductIds(Long[] productIds) {
			this.productIds = productIds;
		}
		public Long getBatchId() {
			return batchId;
		}
		public void setBatchId(Long batchId) {
			this.batchId = batchId;
		}

		public AddToBatchAction() { }	
	}
	
	public static class ThumbDto {
		public String url;

		public ThumbDto(String url) { this.url = url; }
		public ThumbDto() { }
	}
}

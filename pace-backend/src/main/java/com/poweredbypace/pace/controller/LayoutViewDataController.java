package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplate;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.UserService;

@RestController
@RequestMapping(value = "/api/layoutViewData")
public class LayoutViewDataController {
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private UserService userService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public LayoutViewData getByProductId(@PathVariable Long id) {
		LayoutViewData data = new LayoutViewData();
		
		Product p = productRepo.findOne(id);
		if (p!=null && p.isReprint()) {
			p = p.getOriginal();
		}
		
		if (p.getPrototypeProduct().getProductType()==ProductType.DesignableProduct ||
			p.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct) {
			
			//create layout and filmstrip for designable products
			layoutService.createLayout(p);
			
		} else {
			throw new IllegalArgumentException("Product is not designable.");
		}
		
		Layout layout = p.getLayout();
		ProductContext ctx = new ProductContext(p);
		if (p.getParent()!=null && BooleanUtils.isFalse(p.getLinkLayout())) {
			//sync images with parent layout
			layoutService.syncWithParentLayout(layout, p);
		}
		List<Layout> coverLayouts = new ArrayList<Layout>();
		List<Long> coverSizes = new ArrayList<Long>();
				
		for(Product pp:p.getProductAndChildren()) {
			if (pp.getCoverLayout()!=null) {
				Layout coverLayout = pp.getCoverLayout();
				Long sizeId = coverLayout.getLayoutSize().getId();
				
				if (BooleanUtils.isNotTrue(pp.getLinkLayout()) || !coverSizes.contains(sizeId)) {
					coverLayouts.add(coverLayout);
					coverSizes.add(sizeId);
				} 
			}
		}
		data.coverLayouts = coverLayouts.size()>0 ? coverLayouts : null;
		data.layout = layout;
		data.product = p;
		if (coverLayouts.size()>0) {
			data.hinges = getPageRangeValues(ctx, GenericRule.HINGE_GAP);
			data.spines = getPageRangeValues(ctx, GenericRule.SPINE_WIDTH);
		}
		data.centerOffset = genericRuleService.getRuleValue(ctx, GenericRule.CENTER_OFFSET, Double.class);
		if (data.centerOffset==null) data.centerOffset = 0d;
		
		data.savedLayoutTemplates = userService.getCurrentUser().getSavedLayoutTemplates();
		
		//LayoutSize object to override layout.layoutSize values if necessary
		data.layoutSettings = genericRuleService.getRuleValue(ctx, GenericRule.LAYOUT_SIZE, Map.class);
		
		return data;
	}
	
	private PageRangeValueCollection getPageRangeValues(ExpressionContext ctx, String code) {
		return new PageRangeValueCollection(
				genericRuleService.getRuleCollectionValue(ctx, code, PageRangeValue.class));
	}
	
	public static class LayoutViewData {
		public Layout layout;
		public Product product;
		public PageRangeValueCollection spines;
		public PageRangeValueCollection hinges;
		public Double centerOffset;
		public List<LayoutTemplate> savedLayoutTemplates;
		public List<Layout> coverLayouts;
		public Map<String,Object> layoutSettings;
	}

}

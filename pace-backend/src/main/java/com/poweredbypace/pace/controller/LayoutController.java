package com.poweredbypace.pace.controller;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.repository.LayoutRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.LayoutService;


@Controller
@RequestMapping(value = "/api/layout")
public class LayoutController extends CrudController<Layout> {

	@Override
	protected CrudService<Layout> getService() { return null; }
	
	@Override
	protected JpaRepository<Layout, Long> getRepository() { return repo; }
	
	@Autowired
	private LayoutRepository repo;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Layout save(@RequestBody Layout data) {
		Layout layout = layoutService.save(data);
		return layout;
	}
	
	@RequestMapping(value="/getCoverLayout", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Layout getCoverLayout(@RequestBody Product product) {
		Layout layout = layoutService.getCoverLayout(product);
		return layout!=null ? layout : new Layout();
	}

	@RequestMapping(value = "", params={"productId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Layout getByProductId(@RequestParam Long productId) {
		Product p = productRepo.findOne(productId);
		
		if (p.getPrototypeProduct().getProductType()==ProductType.DesignableProduct) {
			//create layout and filmstrip for designable products
			layoutService.createLayout(p);
		} else {
			throw new IllegalArgumentException("Product is not designable.");
		}
		
		Layout layout = p.getLayout();
		if (p.getParent()!=null && BooleanUtils.isFalse(p.getLinkLayout())) {
			//sync images with parent layout
			layoutService.syncWithParentLayout(layout, p);
		}
		return layout;
	}
	
	@RequestMapping(value = "", params={"productId","rev"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Layout getByProductIdAndRevision(@RequestParam long productId, @RequestParam int rev) {
		Product p = productRepo.findOne(productId);
		Layout layout = p.getLayout();
		Layout result = repo.findByMainLayoutAndRevision(layout, rev);
		return result;
	}
	
	@RequestMapping(value = "/{id}/publish", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public Layout publish(@PathVariable("id") long id) {
		Layout layout = repo.findOne(id);
		Layout result = layoutService.publishLayout(layout);
		return result;
	}
	
	@RequestMapping(value="/spines", params={"productId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public PageRangeValueCollection getSpines(@RequestParam Long productId) {
		return getPageRangeValues(productId, GenericRule.SPINE_WIDTH);
	}

	@RequestMapping(value="/hinges", params={"productId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public PageRangeValueCollection getHinges(@RequestParam Long productId) {
		return getPageRangeValues(productId, GenericRule.HINGE_GAP);
	}
	
	@RequestMapping(value="/duplicateAndConvert", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ImageFile duplicateAndConvert(@RequestBody DuplicateAndConvertParams params) {
		Layout layout = repo.getOne(params.layoutId);
		return layoutService.duplicateAndConvert(layout, params.backgroundFrame, params.emptyFrame);
	}

	@RequestMapping(value="/splitImages", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	public ImageFile[] splitImages(@RequestBody List<ImageFile> images) {
		List<ImageFile> list = layoutService.splitImages(images);
		
		ImageFile[] result = new ImageFile[list.size()];
		list.toArray(result);
		return result;
	}
	
	private PageRangeValueCollection getPageRangeValues(Long productId, String code) {
		Product product = productRepo.findOne(productId);
		List<PageRangeValue> values = genericRuleService.getRuleCollectionValue(product, code, PageRangeValue.class);
		return new PageRangeValueCollection(values);
	}
	
	public static class DuplicateAndConvertParams {
		public Long layoutId;
		public ImageElement backgroundFrame;
		public ImageElement emptyFrame;
	}	

}

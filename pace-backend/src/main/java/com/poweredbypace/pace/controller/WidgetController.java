package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.ProductOptionGroup;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.widget.BuildSectionWidget;
import com.poweredbypace.pace.domain.widget.Widget;
import com.poweredbypace.pace.service.ProductPrototypeService;


@Controller
public class WidgetController {
	
	@Autowired
	private ProductPrototypeService prototypeProductService;
	
	
	private void makeGroups(PrototypeProduct prototype, 
			List<ProductOptionGroup> groups, Map<Long, List<PrototypeProductOption>> groupOptions) {
		for(PrototypeProductOption o:prototype.getPrototypeProductOptions()) {
			
			ProductOptionGroup group = o.getEffectiveGroup();
			if (group!=null) {
				if (!groups.contains(group))
					groups.add(group);
				
				if (!groupOptions.containsKey(group.getId())) 
					groupOptions.put(group.getId(), new ArrayList<PrototypeProductOption>());
				
				groupOptions.get(group.getId()).add(o);
			}
		
		}
		Collections.sort(groups, new Comparator<ProductOptionGroup>() {
			@Override
			public int compare(ProductOptionGroup g1,
					ProductOptionGroup g2) {
				Integer o1 = g1.getOrder()!=null ? g1.getOrder() : g1.getId().intValue();
				Integer o2 = g2.getOrder()!=null ? g2.getOrder() : g2.getId().intValue();
				return o1.compareTo(o2);
			}
		});
	}
	
	private void sortOptions(List<PrototypeProductOption> options) {
		 Collections.sort(options, new Comparator<PrototypeProductOption>() {
			public int compare(PrototypeProductOption o1,
					PrototypeProductOption o2) {
				
				Integer order1 = o1.getSortOrder()!=null ? o1.getSortOrder() : o1.getId().intValue();
				Integer order2 = o2.getSortOrder()!=null ? o2.getSortOrder() : o2.getId().intValue();
				return order1.compareTo(order2);
			}
		});
	}
	
	@RequestMapping(value = "/api/widget/build/{productPrototypeId}", method = RequestMethod.GET)
	@ResponseBody
	public Widget getBuildWidget(
			@PathVariable("productPrototypeId") long productPrototypeId) throws InstantiationException, IllegalAccessException {
	
		PrototypeProduct prototype = prototypeProductService.getById(productPrototypeId);
		Map<Long, List<PrototypeProductOption>> groupOptions = new HashMap<Long, List<PrototypeProductOption>>();
		List<ProductOptionGroup> groups = new ArrayList<ProductOptionGroup>();
		makeGroups(prototype, groups, groupOptions);
		
		Widget root = new Widget();
		for(ProductOptionGroup group:groups) {
			BuildSectionWidget section = new BuildSectionWidget();
			section.setLabel(group.getLabel());
			section.setPrompt(group.getPrompt());
			section.setSortType(group.getSortType());
			
			List<PrototypeProductOption> options = groupOptions.get(group.getId());
			sortOptions(options);
			
			int numIncluded = 0;
			for(PrototypeProductOption o:options) {
				if (BooleanUtils.isFalse(o.getEffectiveIncludeInBuild()))
					continue;
				Class<Widget> widgetClass = o.getEffectiveBuildWidgetClass();
				if (widgetClass!=null) {
					Widget widget = widgetClass.newInstance();
					widget.setPrototypeProductOption(o);
					widget.setLabel(o.getEffectiveLabel());
					widget.setPrompt(o.getEffectivePrompt());
					section.getChildren().add(widget);
					numIncluded++;
				}
			}
			if (numIncluded>0)
				root.getChildren().add(section);
			
		}
		return root;
	}
	
}

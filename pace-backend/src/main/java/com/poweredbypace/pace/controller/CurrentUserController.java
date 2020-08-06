package com.poweredbypace.pace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.DieFile;
import com.poweredbypace.pace.domain.ProoferLogoFile;
import com.poweredbypace.pace.domain.LogoFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.repository.DieFileRepository;
import com.poweredbypace.pace.repository.ProoferLogoFileRepository;
import com.poweredbypace.pace.repository.LogoFileRepository;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;

@Controller
@RequestMapping("/api/currentuser")
public class CurrentUserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private LogoFileRepository logoFileRepo;
	
	@Autowired
	private ProoferLogoFileRepository prooferLogoFileRepo;
	
	@Autowired
	private DieFileRepository dieFileRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	
	@RequestMapping(value = "/orders", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Order> getOrders(@RequestParam(required=false) OrderState state, @RequestParam(required=false) Integer count, @RequestParam(required=false) Integer startIndex) {
		User user = userService.getCurrentUser();
		OrderState oState = state!=null ? state : OrderState.PaymentComplete;
		return orderRepo.findByUserAndState(user, oState);
	}
	
	@RequestMapping(value = "/orders/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getOrderCount(@RequestParam(required=false) OrderState state) {
		User user = userService.getCurrentUser();
		OrderState oState = state!=null ? state : OrderState.PaymentComplete;
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		List<Order> orders = orderRepo.findByUserAndState(user, oState);
		Integer itemCount = 0;
		for(Order order : orders) {
			itemCount += order.getOrderItems().size();
		}
		result.put("itemCount", itemCount);
		result.put("count", orders.size());
		return result;
	}
	
	@RequestMapping(value = "/products/current", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getCurrentProducts(@AuthenticationPrincipal User currentUser,
			@RequestParam(value="q", required=false) String q,
			@RequestParam(required=false) Integer pageIndex,
			@RequestParam(required=false) Integer pageSize) {
		
		ProductState[] states = { ProductState.New };
		return findProducts(currentUser, states, q, pageIndex, pageSize, false);
	}
	
	@RequestMapping(value = "/products/current/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProductCount countCurrentProducts(@AuthenticationPrincipal User currentUser) {
		ProductState[] states = { ProductState.New };
		return new ProductCount(productRepo.countByUserAndStates(currentUser, states));
	}
	
	@RequestMapping(value = "/products/production", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getProductionProducts(
			@AuthenticationPrincipal User currentUser,
			@RequestParam(value="q", required=false) String q,
			@RequestParam(required=false) Integer pageIndex,
			@RequestParam(required=false) Integer pageSize) {
		
		ProductState[] states = { ProductState.Preflight, ProductState.Bindery,
			ProductState.Printed, ProductState.Printing, ProductState.ReadyToShip, ProductState.Shipped };
		return findProducts(currentUser, states, q, pageIndex, pageSize, true);
	}
	
	@RequestMapping(value = "/products/production/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProductCount countProductionProducts(@AuthenticationPrincipal User currentUser) {
		ProductState[] states = { ProductState.Preflight, ProductState.Bindery,
				ProductState.Printed, ProductState.Printing, ProductState.ReadyToShip };
		return new ProductCount(productRepo.countByUserAndStates(currentUser, states));
	}
	
	@RequestMapping(value = "/products/shipped", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getShippedProducts(@AuthenticationPrincipal User currentUser,
			@RequestParam(value="q", required=false) String q,
			@RequestParam(required=false) Integer pageIndex,
			@RequestParam(required=false) Integer pageSize) {
		
		ProductState[] states = { ProductState.Completed, ProductState.Shipped };
		return findProducts(currentUser, states, q, pageIndex, pageSize, true);
	}
	
	@RequestMapping(value = "/products/shipped/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProductCount countShippedProducts(@AuthenticationPrincipal User currentUser) {
		ProductState[] states = { ProductState.Completed, ProductState.Shipped };
		return new ProductCount(productRepo.countByUserAndStates(currentUser, states));
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getProducts(@RequestParam(required=false) Boolean favourite,
			@RequestParam(required=false) ProductState state,
			@RequestParam(required=false) Integer pageIndex,
			@RequestParam(required=false) Integer pageSize) {
		
		ProductState pState = state != null ? state : ProductState.New;
		User user = userService.getCurrentUser();
		if (BooleanUtils.isTrue(favourite))
			return productService.getFavourite(user);
		else if (state!=null) {
			if (pageIndex!=null && pageSize!=null) {
				PageRequest pageReq = new PageRequest(pageIndex, pageSize); 
				return productRepo.findByUserAndStateAndParentIsNull(user, pState, pageReq);
			}
			return productService.getByUserAndState(user, pState);
		} else
			return productService.getByUser(user);
	}
	
	@RequestMapping(value = "/logos", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public LogoFile[] getLogos() {
		List<LogoFile> list = logoFileRepo.findByUser(userService.getCurrentUser());
		return list.toArray(new LogoFile[0]);
	}
	
	@RequestMapping(value = "/prooferLogos", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ProoferLogoFile[] getProoferLogos() {
		List<ProoferLogoFile> list = prooferLogoFileRepo.findByUser(userService.getCurrentUser());
		return list.toArray(new ProoferLogoFile[0]);
	}
	
	@RequestMapping(value = "/dies", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public DieFile[] getDies() {
		List<DieFile> list = dieFileRepo.findByUser(userService.getCurrentUser());
		return list.toArray(new DieFile[0]);
	}
	
	@RequestMapping(value = "/products", params="name", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getProductsByName(@RequestParam(required=true) String name) {
		User user = userService.getCurrentUser();
		return productService.getByUserAndName(user, name);
	}
	
	private List<Product> findProducts(User currentUser, ProductState[] states, 
			String q, Integer pageIndex, Integer pageSize, boolean sortByProductNumber) {
				
		PageRequest pageReq = sortByProductNumber ? 
			new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "productNumber")) :
			new PageRequest(pageIndex, pageSize);	
		
		if (q!=null) {
			return productRepo.findByQueryAndUserAndProductStates(q, currentUser, states, pageReq);
		}
		return productRepo.findByUserAndStates(currentUser, states, pageReq);
	}
	
	public static class ProductCount {
		public Integer count;

		public ProductCount() { }
		public ProductCount(Integer count) {
			super();
			this.count = count;
		}
	}
	
}

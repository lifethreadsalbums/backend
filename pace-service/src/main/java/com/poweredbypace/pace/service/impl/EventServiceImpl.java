package com.poweredbypace.pace.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.EventHook;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.event.ApplicationEvent;
import com.poweredbypace.pace.event.EventHandler;
import com.poweredbypace.pace.event.OrderEvent;
import com.poweredbypace.pace.event.BulkProductStateChangedEvent;
import com.poweredbypace.pace.event.ProductEvent;
import com.poweredbypace.pace.event.ProoferEvent;
import com.poweredbypace.pace.event.ProoferEventContext;
import com.poweredbypace.pace.event.UserEvent;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.OrderContext;
import com.poweredbypace.pace.expression.impl.BuldProductStateContext;
import com.poweredbypace.pace.expression.impl.ProductContextExt;
import com.poweredbypace.pace.expression.impl.UserContext;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.job.task.ApplicationEventTask;
import com.poweredbypace.pace.repository.EventHookRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.JsonUtil;
import com.poweredbypace.pace.util.SpringContextUtil;

@Service
public class EventServiceImpl implements EventService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private EventHookRepository repo;
	
	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Transactional(value=TxType.REQUIRED)
	public void processEvent(ApplicationEvent e) {
		List<EventHook> hooks = repo.findByEventClass(e.getClass());
		for(EventHook hook:hooks) {
			try {
				
				if (hook.getCondition()!=null) {
					ExpressionContext ctx = getContextFromEvent(e);
					if (ctx!=null) {
						Boolean condition = expressionEvaluator.evaluate(ctx, 
							hook.getCondition(), Boolean.class);
			  			
			  			if (!BooleanUtils.isTrue(condition)) continue;
					}
				}
				
				EventHandler handler = SpringContextUtil.getApplicationContext()
					.getBean(hook.getHandlerClass(), hook.getParamsJson() );
				handler.handleEvent(e);
				
			} catch (Throwable t) {
				log.error("Error while handling event "+e.getClass().getName(), t);
			}
		}
	}
	
	@Transactional(value=TxType.REQUIRED)
	public void sendEvent(ApplicationEvent e) {
		Job job = new Job();
		job.setType(ApplicationEventTask.class);
		job.setParams(e);
		job.setUser(userService.getPrincipal());
		//TODO: serialize here to avoid lazy loading issues - better solution needed!
		String json = JsonUtil.serialize(job);
		jobScheduler.scheduleJob(json);
	}
	
	private ExpressionContext getContextFromEvent(ApplicationEvent e) {
		
		if (e instanceof ProoferEvent) {
			Product p = productService.findOne( ((ProoferEvent)e).getProductId() );
			return new ProoferEventContext(p);
		} else if (e instanceof BulkProductStateChangedEvent) {
			BulkProductStateChangedEvent event = (BulkProductStateChangedEvent) e;
			List<Product> products = new ArrayList<Product>();
			for(Long id:event.getProductIds()) {
				Product p = productService.findOne(id);
				if (p!=null) products.add(p);
			}
			return new BuldProductStateContext(products, event.getState());
		} else if (e instanceof OrderEvent) {
			Order o = orderService.get( ((OrderEvent)e).getOrderId() );
			return new OrderContext(o);
		} else if (e instanceof ProductEvent) {
			Product p = productService.findOne( ((ProductEvent)e).getProductId() );
			return new ProductContextExt(p);
		} else if (e instanceof UserEvent) {
			User u = userService.get( ((UserEvent)e).getUser().getId() );
			return new UserContext(u);
		}
		
		return null;
	}
	
}

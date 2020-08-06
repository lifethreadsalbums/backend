package com.poweredbypace.pace.event;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.util.JsonUtil;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScheduleTaskEventHander implements EventHandler {

	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BatchService batchService;

	
	private String paramsJson;
	
	public ScheduleTaskEventHander(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	@Override
	public void handleEvent(ApplicationEvent e) throws IOException, ClassNotFoundException {
		
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(this.paramsJson);
		String json = exp.getValue(getContext(e), String.class);
		
		Job job = JsonUtil.deserialize(json, Job.class);
		jobScheduler.scheduleJob(job);
		
	}
	
	private StandardEvaluationContext getContext(ApplicationEvent e) {
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("event", e);
		
		if (e instanceof BulkProductStateChangedEvent) {
			BulkProductStateChangedEvent event = (BulkProductStateChangedEvent) e;
			List<Product> products = new ArrayList<Product>();
			for(Long id:event.getProductIds()) {
				Product p = productService.findOne(id);
				if (p!=null) products.add(p);
			}
			context.setVariable("products", products);
		}
			
		if (e instanceof OrderEvent) {
			Order o = orderService.get( ((OrderEvent)e).getOrderId() );
			context.setVariable("order", o);
		}
			
		if (e instanceof ProductEvent) {
			Product p = productService.findOne( ((ProductEvent)e).getProductId() );
			context.setVariable("product", p);
		}
		
		if (e instanceof BatchSentEvent) {
			Batch batch = batchService.findOne(((BatchSentEvent)e).getBatchId());
			context.setVariable("batch", batch);
		}
		
		return context;
	}

}

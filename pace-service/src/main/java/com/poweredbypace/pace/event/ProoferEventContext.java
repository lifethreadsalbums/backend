package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.ProoferStats;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.service.ProoferService;
import com.poweredbypace.pace.util.SpringContextUtil;

public class ProoferEventContext extends ProductContext {
	
	private static final long serialVersionUID = -4762841627687874100L;

	public ProoferEventContext(Product product)
	{
		super(product);
		
		ProoferService ps = SpringContextUtil.getApplicationContext().getBean(ProoferService.class);
		ProoferStats stats = ps.getProoferStats(product);
		this.put("numArchived", stats.getNumArchived());
		this.put("numCompleted", stats.getNumCompleted());
		this.put("numPending", stats.getNumPending());
	}
	
}

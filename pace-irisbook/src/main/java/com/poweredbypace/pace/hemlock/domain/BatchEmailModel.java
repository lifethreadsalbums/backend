package com.poweredbypace.pace.hemlock.domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.JavaScriptExpressionEvaluator;
import com.poweredbypace.pace.service.GenericRuleService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BatchEmailModel {

	private Log log = LogFactory.getLog(getClass());
	private ExpressionEvaluator evaluator = new JavaScriptExpressionEvaluator();
	private Batch batch;
	private List<BatchItem> rows;
	
	@Autowired
	private GenericRuleService ruleService;
	
	public BatchEmailModel() {}
	
	public BatchEmailModel(Batch batch) {
		log.info("BatchModel " + batch.getName());
		this.batch = batch;
	}

	public Batch getBatch() {
		return batch;
	}

	public List<BatchItem> getRows() {
		makeRows();
		return rows;
	}
	
	public int getNumSpreadBased() {
		makeRows();
		int num = 0;
		for(BatchItem row:rows) {
			if (row.getProduct().getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased)
				num++;
		}
		return num;
	}
	
	public int getTotalNumSets(List<BatchItem> items) {
		int result = 0;
		for(BatchItem row:items) {
			result += row.getNumSets();
		}
		return result;
	}
	
	public int getTotalSheetCount(List<BatchItem> items) {
		int result = 0;
		for(BatchItem row:items) {
			result += row.getSheets();
		}
		return result;
	}
	
	public String getSubtotal(List<BatchItem> items) {
		float result = 0;
		for(BatchItem row:items) {
			result += row.getTotal();
		}
		DecimalFormat format = new DecimalFormat("$#0.00");
		return format.format(result);
	}
	
	public List<BatchItem> getRowsByGroup(String group) {
		List<BatchItem> result = new ArrayList<BatchItem>();
		makeRows();
		for(BatchItem bi:rows) {
			if (group.equals(bi.getGroup())) {
				result.add(bi);
			}
		}
		Collections.sort(result, new Comparator<BatchItem>() {

			@Override
			public int compare(BatchItem o1, BatchItem o2) {
				String paper1 = o1.getPaper();
				if (paper1==null) paper1 = "";
				
				String grain1 = o1.getGrain();
				if (grain1==null) grain1 = "";
				
				String paper2 = o2.getPaper();
				if (paper2==null) paper1 = "";
				
				String grain2 = o2.getGrain();
				if (grain2==null) grain2 = "";
				
				paper1 += grain1;
				paper2 += grain2;
				
				return paper1.compareTo(paper2);
			}
		
		});
		return result;
	}

	private void makeRows() {
		if (rows==null) {
			rows = new ArrayList<BatchItem>();
			for(Product product:batch.getProducts()) {
				for(Product p:product.getProductAndChildren()) {
					rows.add(new BatchItem(p, evaluator, ruleService));
				}
			}
		}
	}

}

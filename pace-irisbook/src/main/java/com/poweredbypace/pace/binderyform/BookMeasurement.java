package com.poweredbypace.pace.binderyform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.poweredbypace.pace.binderyform.BoxMeasurement.BookDimensions;
import com.poweredbypace.pace.binderyform.BoxMeasurement.BoxFormula;
import com.poweredbypace.pace.binderyform.BoxMeasurement.BoxFormulas;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.exception.FormulaParseException;
import com.poweredbypace.pace.service.GenericRuleService;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BookMeasurement {

	public BookMeasurement() { }
	
	private StandardEvaluationContext context;
	
	@Autowired
	private GenericRuleService ruleService;
	
	public void init(Product product) {
		BookDimensions bookMeasurements = ruleService.getRuleValue(product, "BOOK_MEASUREMENTS", BookDimensions.class);
		assert(bookMeasurements!=null);
		
		BoxFormulas clothFormulas = ruleService.getRuleValue(product, "BOOK_CLOTH_FORMULAS", BoxFormulas.class);
		assert(clothFormulas!=null);
		
		List<PageRangeValue> spineWidths = ruleService.getRuleCollectionValue(product, "SPINE_WIDTH", PageRangeValue.class);
		assert(spineWidths!=null);
		PageRangeValueCollection spines = new PageRangeValueCollection(spineWidths);
		
		List<PageRangeValue> hingeGaps = ruleService.getRuleCollectionValue(product, "HINGE_GAP", PageRangeValue.class);
		assert(hingeGaps!=null);
		PageRangeValueCollection hinges = new PageRangeValueCollection(hingeGaps);
		
		int numPages = product.getPageCount();
		
		initContext(bookMeasurements.getBookWidth(), 
			bookMeasurements.getBookHeight(), 
			spines.getValue(numPages),
			hinges.getValue(numPages));
		eval(clothFormulas);
	}
	
	
	public float getValue(String var)
	{
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("#"+var);
		double value = exp.getValue(context, Double.class);
		return (float)value;
	}
	
	private void initContext(float bookWidth, float bookHeight, double spineWidth, double hingeGap)
	{
		context = new StandardEvaluationContext(new BoxMeasurement.BoxMeasurementFunctions());
		context.setVariable("BookWidth", bookWidth);
		context.setVariable("BookHeight", bookHeight);
		context.setVariable("BoardWidth", bookWidth);
		context.setVariable("BoardHeight", bookHeight);
		context.setVariable("BookSpine", spineWidth);
		context.setVariable("Hinge", hingeGap);
	}
	
	private void eval(BoxFormulas formulas) throws FormulaParseException
	{
		ExpressionParser parser = new SpelExpressionParser();
		for(BoxFormula f:formulas.getFormulas())
		{
			try {
				Expression exp = parser.parseExpression(f.getExpr());
				double value = exp.getValue(context, Double.class);
				context.setVariable(f.getVar(), value);
			} catch (Throwable e) {
				String msg = String.format("Error evaluating expression '%s'.", f.getExpr());
				throw new FormulaParseException(msg);
			} 
		}
	}

}

package com.poweredbypace.pace.binderyform;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.exception.FormulaParseException;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.service.GenericRuleService;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BoxMeasurement {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private StandardEvaluationContext context;
	
	@Autowired
	private GenericRuleService ruleService;
	
	public void init(Product product) {
		BookDimensions bookMeasurements = ruleService.getRuleValue(product, "BOOK_MEASUREMENTS", BookDimensions.class);
		assert(bookMeasurements!=null);
		
		List<PageRangeValue> bookThicknessValues = ruleService.getRuleCollectionValue(product, "BOOK_THICKNESS", PageRangeValue.class);
		assert(bookThicknessValues!=null);
		PageRangeValueCollection bookThickness = new PageRangeValueCollection(bookThicknessValues);
		
		BoxFormulas boxFormulas = ruleService.getRuleValue(product, "BOX_FORMULAS", BoxFormulas.class);
		assert(boxFormulas!=null);
		
		if (bookMeasurements!=null && bookThickness!=null && boxFormulas!=null)
		{
			int numPages = product.getPageCount();
			
//			if (product.getConfiguration().getPageType().equals(PageType.SPREAD))
//				numPages /= 2;
			
			float thickness = bookThickness.getValue(numPages);
			
			/*
			 Whenever the book material is a natural linen OR baby blue grey or baby pink you need to buffer add 0.1cm to 
			 the book thickness which will make the slip case wall height 0.1cm larger. 
			 This applies to all box types. What matters here is the book material.
			 */
			IrisProduct p = new IrisProduct(product);
			try {
				String material = p.getBookMaterialCode();
				String boxType = p.getBoxTypeCode();
				
				if ("slip_case".equals(boxType) && ("natural_linen".equals(material) || "silk".equals(material))) {
					String colour = p.getBookColourCode();
				 	if ("baby_pink".equals(colour) || "baby_blue".equals(colour) || "natural_linen".equals(material)) {
				 		thickness += 0.1;
						log.info("Adding 0.1 to book thickness");
				 	}
				}
				
			} catch(Exception ex) {}
			
			initContext(bookMeasurements.getBookWidth(), bookMeasurements.getBookHeight(), thickness);
			eval(boxFormulas);
		}
	}
	
	
	public BoxMeasurement() {
		
	}
	
	public float getValue(String var)
	{
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression("#"+var);
		double value = exp.getValue(context, Double.class);
		return (float)value;
	}
	
	private void initContext(float bookWidth, float bookHeight, float bookThickness)
	{
		context = new StandardEvaluationContext(new BoxMeasurementFunctions());
		context.setVariable("BookWidth", bookWidth);
		context.setVariable("BookHeight", bookHeight);
		context.setVariable("BookThickness", bookThickness);
	}
	
	private void eval(BoxFormulas formulas) 
	{
		ExpressionParser parser = new SpelExpressionParser();
		for(BoxFormula f:formulas.getFormulas())
		{
			try {
				Expression exp = parser.parseExpression(f.getExpr());
				double value = exp.getValue(context, Double.class);
				context.setVariable(f.getVar(), value);
			} catch (Exception e) {
				String msg = String.format("Error evaluating expression '%s'.", f.getExpr());
				throw new FormulaParseException(msg);
			} 
		}
	}
	
	public static class BoxMeasurementFunctions {
		
		public double round(double v)
		{
			return Math.round(v);
		}
		
		public double roundCloth(double v)
		{
			return (Math.floor(v + 1.5d));
		}
	}
	
	public static class BookDimensions {
		private float bookWidth;
		private float bookHeight;
		
		public float getBookWidth() {
			return bookWidth;
		}
		public void setBookWidth(float bookWidth) {
			this.bookWidth = bookWidth;
		}
		public float getBookHeight() {
			return bookHeight;
		}
		public void setBookHeight(float bookHeight) {
			this.bookHeight = bookHeight;
		}
		public BookDimensions() {}
	}
	
	public static class BoxFormula {
		private String var;
		private String expr;
		public String getVar() {
			return var;
		}
		public void setVar(String var) {
			this.var = var;
		}
		public String getExpr() {
			return expr;
		}
		public void setExpr(String expr) {
			this.expr = expr;
		}
		public BoxFormula() {}
	}
	
	public static class BoxFormulas {
		private BoxFormula[] formulas;

		public BoxFormula[] getFormulas() {
			return formulas;
		}

		public void setFormulas(BoxFormula[] formulas) {
			this.formulas = formulas;
		}
		
		public BoxFormulas() {}
	}

}

package com.poweredbypace.pace.hemlock.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.Publisher;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOptionValue;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.hemlock.config.HemlockConfig;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.util.PACEUtils;
import com.poweredbypace.pace.util.UrlUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class HemlockTicketProducer {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private ExpressionEvaluator evaluator;
	
	public String getTicketContents(Product p) throws IOException, TemplateException {
		Map<String,Object> model = new HashMap<String,Object>();
		
		Layout layout = layoutService.getEffectiveLayout(p);
		if (p.isReprint()) {
			layout = layoutService.getEffectiveLayout(p.getOriginal());
		}
		
		LayoutSize layoutSize = p.getLayoutSize();
		Float width = layoutSize.getWidth();
		Float height = layoutSize.getHeight();
		if (p.getPrototypeProduct().getProductPageType().equals(ProductPageType.SpreadBased))
			width *= 2.0f;
		
		String tw = String.format("%10.4f", width / 72.0d).trim();
		String th = String.format("%10.4f", height / 72.0d).trim();
		String batchNumber = p.getBatch().getName();
		String jobId = String.format("%s_%08d", 
				p.getProductNumber(),
				p.getId()); 
		int numPages = p.getPageCount();
		
		if (p.isReprint()) {
			List<Integer> pageNumbers = PACEUtils.getReprintPages(p);
			if (pageNumbers!=null) {
				numPages = pageNumbers.size();
			}
		}
		
		Map<String,String> sizes = new HashMap<String, String>();
		Map<String,String> shapes = new HashMap<String, String>();
		
		boolean isPortfolio = false;
		if (isPortfolio)
		{
			sizes.put("XL", "EXTRA LARGE");
			sizes.put("M", "MEDIUM");
			sizes.put("L", "LARGE");
			
			shapes.put("S", "PORTFOLIO SQUARE");
			shapes.put("P", "PORTFOLIO PORTRAIT");
			shapes.put("L", "PORTFOLIO LANDSCAPE");
		} else {
			sizes.put("S", "SMALL");
			sizes.put("M", "MEDIUM");
			sizes.put("L", "LARGE");
			sizes.put("T", "PRESS SHEET");
			
			shapes.put("S", "SQUARE");
			shapes.put("P", "PORTRAIT");
			shapes.put("L", "LANDSCAPE");
		}
		
		String shapeAndSize = layoutSize.getCode()
			.replaceAll("PRF BK", "")
			.replaceAll("P-", "")
			.trim();
		String size = shapeAndSize.substring(0, shapeAndSize.length() - 1);
		String shape = shapeAndSize.substring(shapeAndSize.length() - 1);
		
		ProductOptionValue paperType = (ProductOptionValue) p.getProductOptionByCode("paperType");
		
		boolean isLfPaper = BooleanUtils.isTrue(layout.getIsLayFlat());
		String impoTemplate = layoutSize.getCode() + (isLfPaper ? "-LF" : "-S");
		
		ProductOptionValue productType = (ProductOptionValue) p.getProductOptionByCode("productType");
		if (productType.getValue().getCode().equals("flushmounts"))
			impoTemplate = layoutSize.getCode() + "-FM";
		
		String paperWeight = genericRuleService.getRuleValue(p, "IRIS_PAPER_WEIGHT", String.class);
		
		GenericRule rule = genericRuleService.findRule(p, "IRIS_GRAIN");
		String grain = evaluator.evaluate(new ProductContext(p), rule.getJsonData(), String.class);
			
		if (grain==null) grain = "LONG";
		//String url = p.getHighResPdfUrl();
		
		String s3Filename = UrlUtil.slug(String.format("%d-%s_%s.%s",
			p.getId(),
			p.getProductNumber()!=null ? p.getProductNumber() : p.getId().toString(),
			p.getName(),
			"pdf"));
		
		String url = p.getStore().getStorageUrl() +
			ApplicationConstants.ALBUM_PATH + "pdf/" + 			
			s3Filename;
				
		double scale = 1;
//		if (p.getParent()!=null && !isLfPaper) {
//			LayoutSize parentLayoutSize = p.getParent().getLayoutSize();
//			scale = layoutSize.getWidth().doubleValue() / parentLayoutSize.getWidth().doubleValue();
//		}
		DecimalFormat df = new DecimalFormat("#.#%");
		
		model.put("url", url);
		model.put("batchNumber", batchNumber);
		model.put("jobTicket", p.getProductNumber());
		model.put("jobId", jobId);
		model.put("numCopies", p.getQuantity());
		model.put("paperName", paperType.getDisplayValue());
		model.put("paperWeight", paperWeight);
		model.put("numPages", numPages);
		model.put("heavyCoverage", "No");
		model.put("scale", df.format(scale));
		model.put("width", tw);
		model.put("height", th);
		
		model.put("size",sizes.get(size));
		model.put("shape",shapes.get(shape));
		
		model.put("grain", grain);
		model.put("impoTemplate", impoTemplate);
		
		Configuration freemarkerConfiguration = new Configuration();
		freemarkerConfiguration.setClassForTemplateLoading(HemlockConfig.class, "/com/poweredbypace/pace/hemlock/template/");
		
		Template template = freemarkerConfiguration.getTemplate("hemlock-ticket.xml");
		String body = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		
		return body;
	}
	
	
	@Publisher(channel = "hemlockFtp")
	@Transactional
	public File sendTicket(Product p) {
		String path = String.format("%s/order-%08d.%s", 
				getTempDir(), 
				p.getId(),
				"xml");
		FileUtils.deleteQuietly(new File(path));
		File file = new File(path);
		try {
			String body = getTicketContents(p);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(body.getBytes());
			fos.close();
			logger.info("File written."+file.getAbsolutePath());
		} catch (Exception e) {
			logger.error("Error while generating XML ticket", e);
			new RuntimeException(e);
		}
		
		return file;
	}
	
	private String getTempDir() {
		return System.getProperty("java.io.tmpdir");
	}

}

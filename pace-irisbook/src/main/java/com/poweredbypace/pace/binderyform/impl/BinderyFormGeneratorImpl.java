package com.poweredbypace.pace.binderyform.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.PdfGenerationException;
import com.poweredbypace.pace.print.BinderyFormGenerator;
import com.poweredbypace.pace.service.LayoutService;

public class BinderyFormGeneratorImpl implements BinderyFormGenerator {
	
private static final float MARGIN = 0.3125f  * 72f;
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	@Qualifier("binderyFormRenderer")
	private BinderyFormRenderer binderyFormRenderer;
	
	@Autowired
	private LayoutService layoutService;
	
	@Override
	public File generate(List<Product> products, JobProgressInfo job) {
		
		try {
			File file = File.createTempFile("bindery-sheet-", ".pdf");
			Document document = new Document(PageSize.LETTER);
			document.setMargins(MARGIN, MARGIN, MARGIN, MARGIN);
	
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(file));
	
			writer.setPdfVersion(PdfWriter.VERSION_1_6);
			writer.setCompressionLevel(9);
			document.open();
	
			int progress = 0;
			int progressStep = 100 / products.size();
	
			for (Product product : products) {
				
				if (product.isReprint()) continue;
				if (product.getPrototypeProduct().getProductType()==ProductType.NondesignableProduct) continue;
				
				log.info(String.format("Generating BF for product ID=%d", product.getId()));
				Layout layout = layoutService.getEffectiveLayout(product);
				
				Spread firstPage = layout.getSpreads().get(0);
				Spread lastPage = layout.getSpreads().get(layout.getSpreads().size() - 1);
						
				Layout coverLayout = layoutService.getEffectiveCoverLayout(product); 
				LayoutSize coverLayoutSize = null;
				Spread coverPage = null;
	
				if (coverLayout != null) 
				{
					coverPage = coverLayout.getSpreads().get(0);
					coverLayoutSize = coverLayout.getLayoutSize();
				}
				document.newPage();
				
				binderyFormRenderer.render(document, writer,
						product, layout.getLayoutSize(), coverLayoutSize,
						firstPage, lastPage, coverPage, job,
						progress, progress + progressStep);
	
				progress += progressStep;
				log.info(String.format("BF for product ID=%d done.", product.getId()));
			}
	
			document.close();
			return file;

		} catch(Exception e) {
			log.error("Error while generating bindery form", e);
			throw new PdfGenerationException(e);
		}
	
	}

	@Override
	public File generate(Product product, JobProgressInfo job) {
		if (product.isReprint()) return null;
		if (product.getPrototypeProduct().getProductType()==ProductType.NondesignableProduct) return null;
		List<Product> allProducts = new ArrayList<Product>();
		allProducts.add(product);
		return generate(allProducts, job);
	}

}

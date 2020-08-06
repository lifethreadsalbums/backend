package com.poweredbypace.pace.print.pdf;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.LayoutSize.PageOrientation;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.util.PACEUtils;

@Service
public class PdfGeneratorImpl implements LayoutPrintGenerator {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired(required=false)
	private HiResPdfRenderer hiResPdfRenderer;
	
	@Autowired(required=false)
	private HiResSpreadPdfRenderer hiResSpreadPdfRenderer;
	
	@Autowired(required=false)
	private CameoPdfRenderer cameoPdfRenderer;
	
	@Autowired(required=false)
	private DiePdfRenderer diePdfRenderer;
	
	@Autowired
	private LowResPdfRenderer lowResPdfRenderer;
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;

	@Autowired
	private LayoutService layoutService;
	
	private PrintingMarksRenderer defaultBookPrintingMarksRenderer;
	private PrintingMarksRenderer defaultCameoPrintingMarksRenderer;
	private PrintingMarksRenderer defaultFICPriningMarksRenderer;
	private PrintingMarksRenderer defaultQBICPriningMarksRenderer;
	
	
	public void setDefaultBookPrintingMarksRenderer(
			PrintingMarksRenderer defaultBookPrintingMarksRenderer) {
		this.defaultBookPrintingMarksRenderer = defaultBookPrintingMarksRenderer;
	}

	public void setDefaultCameoPrintingMarksRenderer(
			PrintingMarksRenderer defaultCameoPriningMarksRenderer) {
		this.defaultCameoPrintingMarksRenderer = defaultCameoPriningMarksRenderer;
	}

	public void setDefaultFICPriningMarksRenderer(PrintingMarksRenderer defaultFICPriningMarksRenderer) {
		this.defaultFICPriningMarksRenderer = defaultFICPriningMarksRenderer;
	}

	public void setDefaultQBICPriningMarksRenderer(PrintingMarksRenderer defaultQBICPriningMarksRenderer) {
		this.defaultQBICPriningMarksRenderer = defaultQBICPriningMarksRenderer;
	}

	@Override
	public File generateDie(Product product, TextStampElement element, ProgressListener progressListener) 
			throws InterruptedException, PrintGenerationException {
		try {
			File pdfFile = diePdfRenderer.generate(product, element, progressListener);
			return pdfFile;
		} catch (Exception e) {
			throw new PrintGenerationException("PDF generation error", e);
		} 
	}
	
	@Override
	public File generateCameos(Product product, ProgressListener progressListener) 
			throws InterruptedException, PrintGenerationException {
		
		PrintingMarksRenderer printingMarksRenderer = defaultCameoPrintingMarksRenderer;
		try {
			File pdfFile = cameoPdfRenderer.generate(product, printingMarksRenderer, progressListener);
			return pdfFile;
		} catch (Exception e) {
			throw new PrintGenerationException("PDF generation error", e);
		} 
	}
	
	@Override
	public File generateAlbum(Product product, ProgressListener progressListener) 
			throws InterruptedException, PrintGenerationException {
		
		PrintingMarksRenderer printingMarksRenderer = null;
		try {
			String printingMarkClass = genericRuleService.getRuleValue(product, "PDF_PRINTING_MARK_CLASS", String.class);
			if (printingMarkClass!=null) {
				log.debug("Printing mark class="+printingMarkClass);
				@SuppressWarnings("unchecked")
				Class<? extends PrintingMarksRenderer> clazz = (Class<? extends PrintingMarksRenderer>) Class.forName(printingMarkClass);
				printingMarksRenderer = clazz.newInstance();
			}
		} catch (Exception e) {
			log.error("", e);
			//printingMarksRenderer = defaultBookPrintingMarksRenderer;// new BookPrintingMarksRenderer();
		}
		if (printingMarksRenderer==null) {
			try {
				ProductContext context = new ProductContext(product);
				GenericRule rule =  genericRuleService.findRule(context, "PDF_PRINTING_MARK_INSTANCE");
				if (rule!=null) {
					printingMarksRenderer = expressionEvaluator.evaluate(context, rule.getJsonData(), PrintingMarksRenderer.class);
				}
			} catch (Exception e) {
				log.error("", e);
			}
		}
		if (printingMarksRenderer==null) {
			log.warn("Cannot find printing mark renderer for "+product.getName()+".");
			printingMarksRenderer = defaultBookPrintingMarksRenderer;
		}
		
		try {
			Layout layout = layoutService.getEffectiveLayout(product);
			List<Integer> pageNumbers = null;
			if (product.isReprint()) {
				pageNumbers = PACEUtils.getReprintPages(product);
				layout = layoutService.getEffectiveLayout(product.getOriginal());
			}
			if (product.getPrototypeProduct().getProductPageType()==ProductPageType.PageBased) {
				File pdfFile = hiResPdfRenderer.generate(product, layout, pageNumbers, 
						printingMarksRenderer, progressListener);
				return pdfFile;
			} else {
				File pdfFile = hiResSpreadPdfRenderer.generate(product, layout, pageNumbers, 
						printingMarksRenderer, progressListener);
				return pdfFile;
			}
		} catch (Exception e) {
			log.error("",e);
			throw new PrintGenerationException("PDF generation error", e);
		} 
	}

	@Override
	public File generateAlbumPreview(Product product, ProgressListener progressListener) 
			throws InterruptedException, PrintGenerationException {
		File pdfFile;
		try {
			pdfFile = lowResPdfRenderer.generate(product, progressListener);
		} catch (Exception e) {
			throw new PrintGenerationException("PDF generation error", e);
		}
		return pdfFile;
	}

	@Override
	public File generateCover(Product product, ProgressListener progressListener)
			throws InterruptedException, PrintGenerationException {
		
		PrintingMarksRenderer printingMarksRenderer = null;
		Layout coverLayout = layoutService.getEffectiveCoverLayout(product);
		LayoutSize layoutSize = layoutService.getEffectiveLayoutSize(product, coverLayout);
		coverLayout = layoutService.copy(coverLayout);
		coverLayout.setLayoutSize(layoutSize);
		
		if (BooleanUtils.isTrue(coverLayout.getLayoutSize().getDynamicSpineWidth())) {
			//Tweak cover layout for FIC cover
			coverLayout.getSpreads().get(0).setNumPages(1);
			if (layoutSize.getPageOrientation().equals(PageOrientation.Horizontal))
				layoutSize.setWidth( layoutSize.getWidth().floatValue() * 2.0f );
			else
				layoutSize.setHeight( layoutSize.getHeight().floatValue() * 2.0f );
			printingMarksRenderer = defaultFICPriningMarksRenderer;
		} else {
			printingMarksRenderer = defaultQBICPriningMarksRenderer;
		}
		try {
			File pdfFile = hiResPdfRenderer.generate(product, coverLayout, null, 
					printingMarksRenderer, progressListener);
			return pdfFile;
		} catch (Exception e) {
			throw new PrintGenerationException("PDF generation error", e);
		} 
	
	}

}

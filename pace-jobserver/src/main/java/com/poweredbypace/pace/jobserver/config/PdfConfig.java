package com.poweredbypace.pace.jobserver.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.binderyform.BookMeasurement;
import com.poweredbypace.pace.binderyform.BoxMeasurement;
import com.poweredbypace.pace.binderyform.impl.BfLogosNotesRenderer;
import com.poweredbypace.pace.binderyform.impl.BfMainContentRenderer;
import com.poweredbypace.pace.binderyform.impl.BfStampsRenderer;
import com.poweredbypace.pace.binderyform.impl.BfThumbRenderer;
import com.poweredbypace.pace.binderyform.impl.BinderyFormGeneratorImpl;
import com.poweredbypace.pace.binderyform.impl.BinderyFormServiceImpl;
import com.poweredbypace.pace.binderyform.impl.ClamShellBoxComponentsRenderer;
import com.poweredbypace.pace.binderyform.impl.MainBinderyFormRenderer;
import com.poweredbypace.pace.binderyform.impl.PresentationBoxBoxComponentsRenderer;
import com.poweredbypace.pace.binderyform.impl.SlipCaseBoxComponentsRenderer;
import com.poweredbypace.pace.print.BinderyFormGenerator;
import com.poweredbypace.pace.print.ColorConverter;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.print.jpeg.GhostscriptPdfToJpegConverter;
import com.poweredbypace.pace.print.jpeg.GsJpegSpreadGenerator;
import com.poweredbypace.pace.print.pdf.CameoPdfRenderer;
import com.poweredbypace.pace.print.pdf.DiePdfRenderer;
import com.poweredbypace.pace.print.pdf.HiResPdfRenderer;
import com.poweredbypace.pace.print.pdf.HiResPrintsPdfRenderer;
import com.poweredbypace.pace.print.pdf.HiResSpreadPdfRenderer;
import com.poweredbypace.pace.print.pdf.LowResPdfRenderer;
import com.poweredbypace.pace.print.pdf.PdfGeneratorImpl;
import com.poweredbypace.pace.print.pdf.marks.BookPrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.marks.CameoPrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.marks.FicPrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.marks.QbicPrintingMarksRenderer;
import com.poweredbypace.pace.service.BinderyFormService;
import com.poweredbypace.pace.tlfrenderer.FabricToTlfConverter;
import com.poweredbypace.pace.tlfrenderer.FontRegistry;
import com.poweredbypace.pace.tlfrenderer.JpegTextFlowRenderer;
import com.poweredbypace.pace.tlfrenderer.PdfTextFlowRenderer;

@Configuration
public class PdfConfig {

	@Value("${fonts.path}") String fontPath;
	@Value("${studio.logo.url}") String studioLogoUrl;
	
	@Value("${pdf.cropImages}") boolean pdfCropImages;
	@Value("${pdf.useTiffImages}") boolean pdfUseTiffImages;

	
	@Bean
	public CameoPdfRenderer cameoPdfRenderer() {
		return new CameoPdfRenderer();
	}
	
	@Bean
	public DiePdfRenderer diePdfRenderer() {
		return new DiePdfRenderer();
	}
	
	@Bean
	public HiResPdfRenderer hiResPdfRenderer() {
		HiResPdfRenderer r = new HiResPdfRenderer();
		r.setCropImages(pdfCropImages);
		r.setUseTiffImages(pdfUseTiffImages);
		return r;
	}
	
	@Bean
	public HiResSpreadPdfRenderer hiResSpreadPdfRenderer() {
		HiResSpreadPdfRenderer r = new HiResSpreadPdfRenderer();
		r.setCropImages(pdfCropImages);
		r.setUseTiffImages(pdfUseTiffImages);
		return r;
	}
	
	@Bean 
	public HiResPrintsPdfRenderer hiResPrintsPdfRenderer() {
		HiResPrintsPdfRenderer r = new HiResPrintsPdfRenderer();
		r.setCropImages(pdfCropImages);
		r.setUseTiffImages(pdfUseTiffImages);
		return r;
	}
	
	@Bean
	public GhostscriptPdfToJpegConverter pdfToJpegConverter() {
		return new GhostscriptPdfToJpegConverter();
	}
	
	@Bean
	public LowResPdfRenderer lowResPdfRenderer() {
		return new LowResPdfRenderer();
	}
	
	@Bean
	@Qualifier("pdfGenerator")
	public LayoutPrintGenerator pdfGenerator() {
		PdfGeneratorImpl gen = new PdfGeneratorImpl();
		gen.setDefaultBookPrintingMarksRenderer(new BookPrintingMarksRenderer());
		gen.setDefaultCameoPrintingMarksRenderer(new CameoPrintingMarksRenderer());
		gen.setDefaultFICPriningMarksRenderer(new FicPrintingMarksRenderer());
		gen.setDefaultQBICPriningMarksRenderer(new QbicPrintingMarksRenderer());
		return gen;
	}
	
	@Bean
	public BinderyFormGenerator bfGen() {
		return new BinderyFormGeneratorImpl();
	}
	
	@Bean
	public BinderyFormService bfSvc() {
		return new BinderyFormServiceImpl();
	}
	
	@Bean
	@Qualifier("binderyFormRenderer")
	public BinderyFormRenderer bfr() {
		return new MainBinderyFormRenderer();
	}
	
	@Bean
	@Qualifier("mainContentRenderer")
	public BinderyFormRenderer bfmcr() {
		return new BfMainContentRenderer();
	}
	
	@Bean
	@Qualifier("thumbRenderer")
	public BinderyFormRenderer bftr() {
		return new BfThumbRenderer();
	}
	
	@Bean
	@Qualifier("logosNotesRenderer")
	public BinderyFormRenderer bflr() {
		BfLogosNotesRenderer logosRenderer = new BfLogosNotesRenderer();
		logosRenderer.setStudioLogoUrl(studioLogoUrl);
		return logosRenderer;
	}
	
	@Bean
	@Qualifier("stampsRenderer")
	public BinderyFormRenderer bfsr() {
		return new BfStampsRenderer();
	}
	
	@Bean
	@Qualifier("clamShellRenderer")
	public BinderyFormRenderer bfcsr() {
		return new ClamShellBoxComponentsRenderer();
	}

	@Bean
	@Qualifier("presentationBoxRenderer")
	public BinderyFormRenderer bfpbr() {
		return new PresentationBoxBoxComponentsRenderer();
	}
	
	@Bean
	@Qualifier("slipCaseRenderer")
	public BinderyFormRenderer bfscr() {
		return new SlipCaseBoxComponentsRenderer();
	}
	
	@Bean
	public BoxMeasurement boxMeasurement() {
		return new BoxMeasurement();
	}
	
	@Bean
	public BookMeasurement bookMeasurement() {
		return new BookMeasurement();
	}

	@Bean
	public FontRegistry fontReg() {
		FontRegistry fr = new FontRegistry();
		fr.setFontDirectory(fontPath);
		return fr;
	}
	
	@Bean
	public FabricToTlfConverter fabric2Tlf() {
		return new FabricToTlfConverter();
	}
	
	@Bean
	public PdfTextFlowRenderer pdfTlf() {
		return new PdfTextFlowRenderer();
	}
	
	@Bean
	public JpegTextFlowRenderer jpegTlf() {
		return new JpegTextFlowRenderer();
	}
	
	@Bean
	public ColorConverter colorConverter() {
		ColorConverter conv = new ColorConverter();
		conv.setIccProfile("/pace/profiles/HPIndigoInk250Photo.icc");
		return conv;
	}
	
	@Bean
	@Qualifier("jpegGenerator")
	public LayoutPrintGenerator jpegGen() {
		GsJpegSpreadGenerator gen = new GsJpegSpreadGenerator();
		gen.setOutputType(OutputType.Jpeg);
		return gen;
	}
	
	@Bean
	@Qualifier("jpegFolderGenerator")
	public LayoutPrintGenerator jpegFolderGen() {
		GsJpegSpreadGenerator gen = new GsJpegSpreadGenerator();
		gen.setZipSpreads(false);
		gen.setOutputType(OutputType.Jpeg);
		return gen;
	}
	
	@Bean
	@Qualifier("tiffGenerator")
	public LayoutPrintGenerator tiffGen() {
		GsJpegSpreadGenerator gen = new GsJpegSpreadGenerator();
		gen.setOutputType(OutputType.Tiff);
		return gen;
	}
	
	
}

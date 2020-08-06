package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;

//@Component
//@Qualifier("binderyFormRenderer")
public class MainBinderyFormRenderer implements BinderyFormRenderer {
	
	protected final Log logger = LogFactory.getLog(getClass());
		
	@Autowired
	@Qualifier("thumbRenderer")
	private BinderyFormRenderer thumbRenderer;
	
	@Autowired
	@Qualifier("slipCaseRenderer")
	private BinderyFormRenderer slipCaseRenderer;
	
	@Autowired
	@Qualifier("presentationBoxRenderer")
	private BinderyFormRenderer presentationBoxRenderer;
	
	@Autowired
	@Qualifier("clamShellRenderer")
	private BinderyFormRenderer clamShellRenderer;
	
	
	@Autowired
	@Qualifier("stampsRenderer")
	private BinderyFormRenderer stampsRenderer;
	
	@Autowired
	@Qualifier("mainContentRenderer")
	private BinderyFormRenderer mainContentRenderer;
	
	@Autowired
	@Qualifier("logosNotesRenderer")
	private BinderyFormRenderer logosNotesRenderer;
	
	
	public MainBinderyFormRenderer() {
		super();
	}

	@Override
	public void render(Document document, PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress)
			throws IOException, DocumentException, ImageProcessingException {
		
		
		BinderyFormRenderer[] renderers = new BinderyFormRenderer[] {
				mainContentRenderer,
				slipCaseRenderer,
				presentationBoxRenderer,
				clamShellRenderer,
				thumbRenderer,
				stampsRenderer,
				logosNotesRenderer
		};
		
		for(BinderyFormRenderer r:renderers)
		{
			r.render(document, writer, product, 
					bookTemplate, coverTemplate, firstPage, 
					lastPage, coverPage, job, 
					minProgress, maxProgress);
		}
		
	}
	
	
}

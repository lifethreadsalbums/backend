package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.binderyform.BoxMeasurement;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.irisbook.IrisProduct;

public class AbstractBoxComponentsRenderer implements BinderyFormRenderer {

	protected float[] totalWidths = new float[] {68f, 40f, 12f, 40f, 20f, 20f};
	protected float maxWw = 0f;
	private float maxHw = 0f;
	protected PdfWriter writer;
	
	protected String getBoxStyle() { return null; }
	
	@Autowired
	protected BoxMeasurement boxMeasurements;

	public AbstractBoxComponentsRenderer() {
		super();
	}
	
	@Override
	public void render(Document document, PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress)
			throws IOException, DocumentException, ImageProcessingException {
				
		IrisProduct irisProduct = new IrisProduct(product);
		if (irisProduct.hasBox() && irisProduct.getBoxTypeCode()!=null && 
				irisProduct.getBoxTypeCode().equals(getBoxStyle()))
		{
			boxMeasurements.init(product);
			this.writer = writer;
			
			PdfPTable t = getBoxComponentsTable(irisProduct, bookTemplate);
			PdfContentByte canvas = writer.getDirectContent();
			canvas.saveState();
			t.writeSelectedRows(0, t.getRows().size(),  
					document.right() - t.getTotalWidth() + 10f, 
					document.top() - 39, canvas);
			canvas.restoreState();
		}
		
	}

	private PdfPTable getBoxComponentsTable(IrisProduct irisProduct, LayoutSize bookTemplate) 
	{
		PdfPTable table = new PdfPTable(6);
		try {
			table.setLockedWidth(true);
			
			PdfPCell cell = new PdfPCell();
			cell.setBorder(0);
			Paragraph header = new Paragraph("BOX COMPONENTS", BinderyFormHelper.getFont(18, true));
			header.setSpacingAfter(4f);
			cell.setNoWrap(true);
			cell.addElement(header);
			cell.setColspan(6);
			
			table.addCell(cell);
			
			PdfPCell c = new PdfPCell();
			c.setBorder(0);
			c.setFixedHeight(19);
			table.addCell(c);
			
			c = new PdfPCell(new Phrase("W", BinderyFormHelper.getFont(14, true)));
			c.setBorder(0);
			c.setPaddingRight(5f);
			c.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c);
			
			c = new PdfPCell();
			c.setBorder(0);
			c.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(c);
			
			c = new PdfPCell(new Phrase("H", BinderyFormHelper.getFont(14, true)));
			c.setBorder(0);
			c.setPaddingRight(6.5f);
			c.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(c);
			
			for(int i=0;i<2;i++)
			{
				c = new PdfPCell();
				c.setBorder(0);
				table.addCell(c);
			}
			
			addValueRows(table, irisProduct, bookTemplate);
			
			String[] whiteBoardsColors = { 
				"ivory",
				"light_grey",
				"oat",
				"wheat",
				
				"baby_blue",
				"baby_pink",
				"sunflower",
				"cream",
				
				"pearl",
				"nat_lin_canvas",
	        };
			
			boolean useWhiteBoards = false;
			for(String color:whiteBoardsColors)
			{
				if (StringUtils.equals(irisProduct.getBoxColourCode(), color))
					useWhiteBoards = true;
			}
			
			if (useWhiteBoards)
			{
				c = new PdfPCell(new Phrase("USE WHITE BOARDS", BinderyFormHelper.getFont(14, true)));
				c.setColspan(6);
				c.setBorder(0);
				table.addCell(c);
			}
			
			totalWidths[1] = maxWw + 4f;
			totalWidths[3] = maxHw + 4f;
			table.setTotalWidth(totalWidths);
			
		} catch (DocumentException e) {
		} catch (IOException e) {
		}
		return table;
	}

	protected void addValueRows(PdfPTable table,
			IrisProduct product,
			LayoutSize bookTemplate) throws BadElementException, IOException
	{
		
	}
	
	protected void addEmptyRow(PdfPTable table)
	{
		PdfPCell c = new PdfPCell();
		c.setBorder(0);
		c.setFixedHeight(19);
		c.setColspan(7);
		table.addCell(c);
	}
		
	protected void addValueRow(PdfPTable table, String label,
			float w, float h, int numBoxes) throws BadElementException,
			IOException 
	{
		DecimalFormat format = new DecimalFormat("#0.0");
		
		Phrase p = new Phrase();
		if (label.equals("F-TCB:") || label.equals("B-TCB:"))
		{
			p.add(new Chunk(label.substring(0, 1), BinderyFormHelper.getFont(14, true)));
			p.add(new Chunk(label.substring(1), BinderyFormHelper.getFont(14)));
		} else 
			p.add(new Chunk(label, BinderyFormHelper.getFont(14)));
		PdfPCell c = new PdfPCell(p);
		c.setBorder(0);
		c.setFixedHeight(19);
		table.addCell(c);
		
		String width = format.format(w);
		BaseFont bf = BinderyFormHelper.getFont(14, true).getCalculatedBaseFont(false);
		float widthPoint = bf.getWidthPoint(width, 14);
		if (widthPoint>maxWw)
			maxWw = widthPoint;
		
		String height = format.format(h);
		widthPoint = bf.getWidthPoint(height, 14);
		if (widthPoint>maxHw)
			maxHw = widthPoint;
		
		c = new PdfPCell(new Phrase(width, BinderyFormHelper.getFont(14, true)));
		c.setBorder(0);
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c.setFixedHeight(19);
		c.setPaddingLeft(0f);
		table.addCell(c);
		
		c = new PdfPCell(new Phrase("x", BinderyFormHelper.getFont(14, true)));
		c.setBorder(0);
		c.setHorizontalAlignment(Element.ALIGN_CENTER);
		c.setFixedHeight(19);
		table.addCell(c);
		
		c = new PdfPCell(new Phrase(height, BinderyFormHelper.getFont(14, true)));
		c.setBorder(0);
		c.setHorizontalAlignment(Element.ALIGN_RIGHT);
		c.setFixedHeight(19);
		c.setPaddingLeft(0f);
		table.addCell(c);
		
		for(int i=0;i<2;i++)
		{
			if (i<numBoxes)
				c = new PdfPCell(BinderyFormHelper.renderCheckBox(writer), false);
			else
				c = new PdfPCell();
			c.setBorder(0);
			c.setPaddingBottom(0);
			c.setPaddingTop(4f);
			c.setHorizontalAlignment(i==0 ? Element.ALIGN_CENTER : Element.ALIGN_LEFT);
			c.setFixedHeight(19);
			c.setPaddingRight(0f);
			
			table.addCell(c);
		}
	}

}
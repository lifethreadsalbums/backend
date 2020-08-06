package com.poweredbypace.pace.print.productionsheet;


import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;

public abstract class AbstractProductionSheetRenderer {
	
	public static class Margins {
		private float left;
		private float right;
		private float top;
		private float bottom;
		public float getLeft() {
			return left;
		}
		public void setLeft(float left) {
			this.left = left;
		}
		public float getRight() {
			return right;
		}
		public void setRight(float right) {
			this.right = right;
		}
		public float getTop() {
			return top;
		}
		public void setTop(float top) {
			this.top = top;
		}
		public float getBottom() {
			return bottom;
		}
		public void setBottom(float bottom) {
			this.bottom = bottom;
		}
		public Margins(float left, float right, float top, float bottom) {
			super();
			this.left = left;
			this.right = right;
			this.top = top;
			this.bottom = bottom;
		}
		
	}
	
	public static class CellFormat {
		private BaseColor backgroundColor = BaseColor.WHITE;
		private int horizontalAlignment = Element.ALIGN_LEFT;
		private int verticalAlignment = Element.ALIGN_MIDDLE;
		private Font font;
		
		public CellFormat() {
			super();
			try {
				BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, true);
				font = new Font(bf, 10);
			} catch (DocumentException e) {
			} catch (IOException e) {
			}
		}
		public BaseColor getBackgroundColor() {
			return backgroundColor;
		}
		public void setBackgroundColor(BaseColor backgroundColor) {
			this.backgroundColor = backgroundColor;
		}
		
		public int getHorizontalAlignment() {
			return horizontalAlignment;
		}
		public void setHorizontalAlignment(int horizontalAlignment) {
			this.horizontalAlignment = horizontalAlignment;
		}
		
		public int getVerticalAlignment() {
			return verticalAlignment;
		}
		public void setVerticalAlignment(int verticalAlignment) {
			this.verticalAlignment = verticalAlignment;
		}
		
		public Font getFont() {
			return font;
		}
		public void setFont(Font font) {
			this.font = font;
		}
		
	}
	
	private static float ROW_HEIGHT = 0.3125f * 72;
	
	protected float tableWidth;
	
	public Margins getMargins() { return new Margins(18.0f, 18.0f, 36.0f, 36.0f); }
	
	protected int getRowCount() { return 0; }
	
	protected int getColumnCount() { return 0; }
	
	protected int getColSpan(int row, int col) { return 1;}
	
	protected String getCellValue(int row, int col) { return null; }
	
	public boolean isEmpty() { return false; }
	
	
	
	protected CellFormat getCellFormat(int row, int col) 
	{ 
		CellFormat format = new CellFormat();
		return format;
	}
	
	protected float[] getColumnWidths() 
	{ 
		float[] w = new float[getColumnCount()];
		for(int i=0;i<getColumnCount();i++)
		{
			float maxw = 0f;
			for(int j=1;j<getRowCount();j++)
			{
				CellFormat format = getCellFormat(j, i);
				
				float width = format.getFont().getBaseFont().getWidthPoint(getCellValue(j, i), 
						format.getFont().getSize()) + 6f;
				if (width>maxw)
					maxw = width;
			}
			
			w[i] = maxw;
		}
		return w;
	}
	
	protected int getWidthPercentage() { return 91; }
	
	protected float getSpacingBefore() { return 0; }
	
	protected float getSpacingAfter() { return 0; }
	
	protected int getHeaderRows() { return 2; }
	
	public Rectangle getPageSize() { return PageSize.LEGAL.rotate(); }
	
	public void render(Document doc, PdfWriter writer, String batchNumbers) throws DocumentException, IOException
	{
		renderTable(doc, writer, batchNumbers);
	}
	
	protected void renderTable(final Document doc, final PdfWriter writer, final String batchNumbers) throws DocumentException
	{
		int numRows = getRowCount();
		int numCols = getColumnCount();
		
		PdfPTable table = new PdfPTable(numCols);	
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setSpacingBefore(getSpacingBefore());
		table.setSpacingAfter(getSpacingAfter());
		table.setHeaderRows(getHeaderRows());
		
		float[] colWidths = getColumnWidths();
		if (colWidths!=null)
		{
			table.setLockedWidth(true);
			table.setTotalWidth(colWidths);
			tableWidth = table.getTotalWidth();
		} else
			table.setWidthPercentage(getWidthPercentage());
		
		for(int row=0;row<numRows;row++)
			for (int col=0;col<numCols;)
			{
				CellFormat format = getCellFormat(row, col);
				
				String cellValue = getCellValue(row, col);
				
				Phrase phrase = new Phrase(cellValue, format.getFont());
					
				PdfPCell cell = new PdfPCell(phrase);
				
				cell.setBackgroundColor(format.getBackgroundColor());
				cell.setHorizontalAlignment(format.getHorizontalAlignment());
				cell.setVerticalAlignment(format.getVerticalAlignment());
				cell.setFixedHeight( ROW_HEIGHT );
				
				int colSpan = getColSpan(row,col);
				cell.setColspan(colSpan);
				table.addCell(cell);
				col += colSpan;
			}
		
		writer.setPageEvent(null);
		writer.setPageEvent(new PdfPageEvent() {
			
			@Override
			public void onStartPage(PdfWriter writer, Document document) {
				
			}
			
			@Override
			public void onSectionEnd(PdfWriter writer, Document document,
					float paragraphPosition) { }
			
			@Override
			public void onSection(PdfWriter writer, Document document,
					float paragraphPosition, int depth, Paragraph title) { }
			
			@Override
			public void onParagraphEnd(PdfWriter writer, Document document,
					float paragraphPosition) { }
			
			@Override
			public void onParagraph(PdfWriter writer, Document document,
					float paragraphPosition) { }
			
			@Override
			public void onOpenDocument(PdfWriter writer, Document document) { }
			
			@Override
			public void onGenericTag(PdfWriter writer, Document document,
					Rectangle rect, String text) { }
			
			@Override
			public void onEndPage(PdfWriter writer, Document document) { 
				renderBatchNumbers(doc, writer, batchNumbers);
			}
			
			@Override
			public void onCloseDocument(PdfWriter writer, Document document) { }
			
			@Override
			public void onChapterEnd(PdfWriter writer, Document document,
					float paragraphPosition) { }
			
			@Override
			public void onChapter(PdfWriter writer, Document document,
					float paragraphPosition, Paragraph title) { }
		});
		
		doc.add(table);
	}
	
	protected void renderBatchNumbers(Document doc, PdfWriter writer, String batchNumbers)
	{
		String[] batches = batchNumbers.split(",");
		float fontSize = 48;
		PdfContentByte over = writer.getDirectContent();
		Font font = FontFactory.getFont(BaseFont.HELVETICA, fontSize, Font.BOLD);
		boolean fontOk=false;
		float docWidth = (doc.right() - doc.left()) - 18f;
		while(!fontOk && fontSize>16)
		{
			float maxw = 0;
			for(String batch:batches)
			{
				float w = font.getBaseFont().getWidthPoint(batch, fontSize);
				if (w>maxw)
					maxw = w;
			}
			
			float freeSpace = docWidth - tableWidth;
			if (maxw<freeSpace)
				fontOk = true;
			else {
				fontSize -= 1;
				font = FontFactory.getFont(BaseFont.HELVETICA, fontSize, Font.BOLD);
			}
		}
		float top = getBatchNumberPosition(doc, font, batches);
		over.saveState();
		for(String batch:batches)
		{
			over.beginText();	
			over.setColorFill(BaseColor.RED);
			over.setFontAndSize(font.getBaseFont(), fontSize);
			over.showTextAligned(Element.ALIGN_RIGHT, batch, 
					doc.right(), 
					top, 0);
			top -= fontSize + 9f;
			over.endText();	
		}
		over.restoreState();
	}
	
	protected float getBatchNumberPosition(Document doc, Font font, String[] batchNumbers)
	{
		float ascent = batchNumbers.length>0 ? 
				font.getBaseFont().getAscentPoint(batchNumbers[0], font.getSize()) : font.getSize();
		float top = doc.top() - ascent;
		return top;
	}

}

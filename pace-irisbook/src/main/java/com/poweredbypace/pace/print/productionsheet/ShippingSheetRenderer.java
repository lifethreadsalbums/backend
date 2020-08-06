package com.poweredbypace.pace.print.productionsheet;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.hemlock.domain.BatchItem;
import com.poweredbypace.pace.irisbook.IrisConstants;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisConstants.Material;
import com.poweredbypace.pace.irisbook.IrisConstants.ProductType;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.util.DateUtils;

public class ShippingSheetRenderer extends AbstractProductionSheetRenderer {
	
	private List<BatchItem> rows;
	private SimpleDateFormat dateFormatter;

	private String[] headers = {
			"!RUSH!",
			"Batch",
			"Job ID",
			"Type",
			"",
			"Shape",
			"Material",
			"Colour",
			"Production Date",
			"1.5 weeks",
			"3 weeks",
			"4 weeks",
			"5 weeks",
			"6 weeks",
			"Shipped",
			"Notes"};
	
	private int numPortfolio;
	
	private Map<String, Integer> counts = new HashMap<String, Integer>();
	
	public ShippingSheetRenderer(List<BatchItem> batchRows)
	{
		
		DecimalFormat format = new DecimalFormat("#0.#");
		format.setDecimalSeparatorAlwaysShown(false);
		
		rows = batchRows;
		dateFormatter = new SimpleDateFormat("MMMM d");
		
		for(String mat:IrisConstants.Material.ALL)
			counts.put(mat, 0);
		
		for(String mat:IrisConstants.BoxStyle.ALL)
			counts.put(mat, 0);
		
		for(BatchItem row:batchRows)
		{
			IrisProduct bd = new IrisProduct(row.getProduct());
			
			if (bd.isPortfolio())
				numPortfolio++;
			
			String bookMaterial = bd.getBookMaterialCode();
			if (!StringUtils.isEmpty(bookMaterial))
			{
				int val = counts.containsKey(bookMaterial) ? counts.get(bookMaterial) : 0;
				counts.put(bookMaterial, val + 1);
			}
			
			String boxStyle = bd.getBoxTypeCode();
			if ( !StringUtils.isEmpty(boxStyle) )
			{
				int val = counts.containsKey(boxStyle) ? counts.get(boxStyle) : 0;
				counts.put(boxStyle, val + 1);
			}
		}
	}
	
	@Override
	public Margins getMargins() { 
		return new Margins(18.0f, 18.0f, 42f, 36.0f); 
	}
	
	@Override
	public boolean isEmpty() 
	{ 
		return rows.size()==0; 
	}
	
	@Override
	public Rectangle getPageSize()
	{
		return PageSize.LEGAL.rotate();
	}
	
	@Override
	protected int getRowCount() 
	{
		return rows.size() + 2;
	}
	
	@Override
	protected int getColumnCount() 
	{
		return headers.length;
	}
	
	@Override
	protected float getSpacingBefore()
	{
		return 9f;
	}
	
	@Override
	protected int getColSpan(int row, int col)
	{
		if (row==0 && col==0)
			return getColumnCount();
		else
			return 1;
	}
	
	@Override
	protected float[] getColumnWidths() 
	{ 
		boolean showPriorities = false;
		for(BatchItem row:rows)
		{
			if (BooleanUtils.isTrue(row.getProduct().getRush()))
			{
				showPriorities = true;
				break;
			}
		}
		float[] w = super.getColumnWidths();
		
		if (!showPriorities)
			w[0] = 0f;
		
		float totalWidth = 0f;
		for(int i=0;i<w.length-1;i++)
			totalWidth += w[i];
		
		w[w.length-1] = getPageSize().getWidth() - 36f - totalWidth;
		
		return w;
	}
	
	@Override
	protected String getCellValue(int row, int col)
	{
		if (row==0)
			return getHeader();
		else if (row==1)
			return headers[col];
		else {
			BatchItem br = rows.get(row - 2);
			IrisProduct p = new IrisProduct(br.getProduct());
			
			Date productionDate = p.getProductionDate();
			if (br.getInvoice()!=null) {
				productionDate = br.getInvoice().getDateCreated();
			}
			
			switch(col) {
			case 0: //RUSH
				return p.getPriority() ? "!RUSH!" : "";
			case 1: //Batch
				return br.getBatchNumber();
			case 2: //Job ID
				return p.getJobId();
			case 3: //Type
				return p.getProductLineLabel();
			case 4: //FM
				return ProductType.FLUSHMOUNT.equals(p.getProductType()) ? "FM" : "";
			case 5: //Shape
				return br.getShape();
			case 6: //Material
				return p.getBookMaterialLabel();
			case 7: //Colour
				return p.getBookColourLabel()!=null ? p.getBookColourLabel() : "N/A";
			case 8: //production date
				return DateUtils.customFormat(productionDate, dateFormatter);
			case 9: //min lead time
			case 10: //max lead time
			case 11:
			case 12:
			case 13:
				if (p.getPriority())
				{
					String dueDate = DateUtils.customFormat(p.getDueDate(), 
							dateFormatter);
					return dueDate;
				}
				
				float[] times = {1.5f,3,4,5,6};
				int days = Math.round(times[col - 9] * 7f);
				
				return DateUtils.customFormat( DateUtils.getNextBusinessDay(
						DateUtils.addDays(productionDate, days)),
						dateFormatter);
			
			case 15://comments
				return p.getAdminNotes();
			default:
				return "";
			}
		}
	}
	
	@Override
	protected CellFormat getCellFormat(int row, int col) 
	{ 
		CellFormat format = super.getCellFormat(row, col);
		
		format.setHorizontalAlignment( (row==0 && col==0) ? Element.ALIGN_LEFT : Element.ALIGN_CENTER );
		
		if (row<2 || col==0)
		{
			Font font = FontFactory.getFont(BaseFont.HELVETICA, 10, 
					col==0 && row==1 ? Font.BOLDITALIC : Font.BOLD);
			if (col==0 && row==1)
				font.setColor(BaseColor.RED);
			format.setFont(font);
		} 
		
		if (row>1)
		{
			format.setBackgroundColor(row%2!=0 ? new BaseColor(0xcbcbcb) : BaseColor.WHITE);
			BatchItem br = rows.get(row - 2);
			IrisProduct p = new IrisProduct(br.getProduct());
			
			if ((col==0 || col==1) && p.getPriority())
			{
				format.setBackgroundColor(BaseColor.RED);
				if (col==0)
				{
					Font font = FontFactory.getFont(BaseFont.HELVETICA, 10, Font.BOLDITALIC);
					font.setColor(BaseColor.WHITE);
					format.setFont(font);
				}
			}
			
			if ((col>=8 && col<=12) && p.getPriority())
			{
				Font font = FontFactory.getFont(BaseFont.HELVETICA, 10, Font.BOLD);
				font.setColor(BaseColor.RED);
				format.setFont(font);
			}
			
			String boxStyle = p.getBoxTypeCode();
			
			if (col==2 && p.hasBox() && boxStyle!=null)
			{
				if (boxStyle.equals(BoxStyle.PRESENTATION_BOX))
					format.setBackgroundColor(new BaseColor(0x14bf18)); //green
				else if (boxStyle.equals(BoxStyle.CLAM_SHELL))
					format.setBackgroundColor(new BaseColor(0x00d5ff)); //blue;
				if (boxStyle.equals(BoxStyle.SLIP_CASE))
					format.setBackgroundColor(new BaseColor(0xFCF151)); //yellow;		
			}
			
			String material = p.getBookMaterialCode();
			if (col==5 && material!=null)
			{
				if (material.equals(Material.LEATHER) || material.equals(Material.VINTAGE_LEATHER))
					format.setBackgroundColor(new BaseColor(0xFFA839)); //orange
				else if (material.equals(Material.FIC) || material.equals(Material.QBIC))
					format.setBackgroundColor(new BaseColor(0xD7AFF9)); //purple
			}
			
			if (col==3 && p.isPortfolio())
				format.setBackgroundColor(new BaseColor(0xffc3d4)); 
		}
			
		return format;
	}
	
	@Override
	protected void renderBatchNumbers(Document doc, PdfWriter writer, String batchNumbers)
	{
		float fontSize = 24;
		PdfContentByte over = writer.getDirectContent();
		Font font = FontFactory.getFont(BaseFont.HELVETICA, fontSize, Font.BOLD);
		
		over.saveState();
		over.setColorFill(BaseColor.RED);
		over.setFontAndSize(font.getBaseFont(), fontSize);
		
		float top = doc.top() + 5f;
		
		over.beginText();	
		over.showTextAligned(Element.ALIGN_RIGHT, batchNumbers, 
				doc.right(), 
				top, 0);
		over.endText();
		
		over.beginText();	
		over.showTextAligned(Element.ALIGN_LEFT, batchNumbers, 
				doc.left(), 
				top, 0);
		over.endText();	
		over.restoreState();
	}
	
	private String getHeader()
	{
		return String.format("Shipping Schedule: " +
				"Books: %d     Portfolios: %d,      " +
				"(Silk:%d Leather:%d IC:%d Satin:%d, Chromo:%d Carbon:%d) " +
				"      Slip Cases:%d       Pres.Boxes:%d       Clam Shells: %d",
				rows.size() - numPortfolio, 
				numPortfolio, 
				counts.get(Material.SILK),
				counts.get(Material.LEATHER),
				counts.get(Material.FIC) + counts.get(Material.QBIC),
				counts.get(Material.SATIN),
				counts.get(Material.CHROMO),
				counts.get(Material.CARBON),
				counts.get(BoxStyle.SLIP_CASE),
				counts.get(BoxStyle.PRESENTATION_BOX),
				counts.get(BoxStyle.CLAM_SHELL));
	}

}

package com.poweredbypace.pace.print.productionsheet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.poweredbypace.pace.hemlock.domain.BatchItem;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisConstants.Material;
import com.poweredbypace.pace.irisbook.IrisConstants.ProductType;
import com.poweredbypace.pace.irisbook.IrisProduct;


public class SewingSheetRenderer extends AbstractProductionSheetRenderer {
	
	private List<BatchItem> rows;
	@SuppressWarnings("serial")
	final Map<String,String> papers = new HashMap<String, String>()
	{
	    {
	    	put("futura_matte_lg", "M");
			put("futura_matte_sg", "M");
			put("luster", "L");       
			put("mccoy_matte", "M");
			put("opus_matte_100", "M");
			put("opus_matte_65", "M");
			put("mohawk_felt", "M");
			put("prophoto_heavy", "L");	
			put("classic_crest", "M");
			
	        put("enhanced_velvet", "GV");
	        put("lasal_luster", "GL");
	        put("lassal_matte", "GM");
	        put("fuji_luster", "FL");
	        put("fuji_matte", "FDM");
	    }
	};

	private String[] headers = {
			"No",
			"!RUSH!",
			"Batch",
			"Job ID",
			"Type",
			"",
			"Shape",
			"Material",
			"Colour",
			"Paper",
			"                         "};
	
	public SewingSheetRenderer(List<BatchItem> batchRows)
	{
		rows = batchRows;
	}
	
	@Override
	public boolean isEmpty() 
	{ 
		return rows.size()==0; 
	}
	
	@Override
	public Rectangle getPageSize()
	{
		return PageSize.LETTER;
	}
	
	@Override
	protected int getWidthPercentage() { return 80; }
	
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
			IrisProduct p = new IrisProduct(row.getProduct());
			if (p.getPriority())
			{
				showPriorities = true;
				break;
			}
		}
		float[] w = super.getColumnWidths();
		if (!showPriorities)
			w[1] = 0f;
		
		return w;
	}
	
	@Override
	protected String getCellValue(int row, int col)
	{
		if (row==0)
			return "Sewing Sheet";
		else if (row==1)
			return headers[col];
		else {
			BatchItem br = rows.get(row - 2);
			IrisProduct p = new IrisProduct(br.getProduct());
			switch(col) {
			case 0: //No
				return String.format("%d", row - 1);
			case 1: //RUSH
				return p.getPriority() ? "!RUSH!" : "";
			case 2: //Batch
				return br.getBatchNumber();
			case 3: //Job ID
				return p.getJobId();
			case 4: //Type
				return p.getProductLineLabel();
			case 5: //FM
				return ProductType.FLUSHMOUNT.equals(p.getProductType()) ? "FM" : "";
			case 6: //Shape
				return br.getShape();
			case 7: //Material;
				return p.getBookMaterialLabel();
			case 8: //Colour
				return p.getBookColourLabel()!=null ? p.getBookColourLabel() : "N/A";
			case 9: //Paper
				return papers.get(p.getPaperTypeCode());
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
					col==1 && row==1 ? Font.BOLDITALIC : Font.BOLD);
			if (col==1 && row==1)
				font.setColor(BaseColor.RED);
			format.setFont(font);
		} 
		
		if (row>1)
		{
			format.setBackgroundColor(row%2!=0 ? new BaseColor(0xcbcbcb) : BaseColor.WHITE);
			
			BatchItem br = rows.get(row - 2);
			IrisProduct p = new IrisProduct(br.getProduct());
			if ((col==1 || col==2) && p.getPriority())
			{
				format.setBackgroundColor(BaseColor.RED);
				if (col==1)
				{
					Font font = FontFactory.getFont(BaseFont.HELVETICA, 10, Font.BOLDITALIC);
					font.setColor(BaseColor.WHITE);
					format.setFont(font);
				}
			}
			
			String boxStyle = p.getBoxTypeCode();
			if (col==3 && p.hasBox() && boxStyle!=null && 
					!br.getProduct().isReprint())
			{
				if (boxStyle.equals(BoxStyle.PRESENTATION_BOX))
					format.setBackgroundColor(new BaseColor(0x14bf18)); //green
				else if (boxStyle.equals(BoxStyle.CLAM_SHELL))
					format.setBackgroundColor(new BaseColor(0x00d5ff)); //blue;
				if (boxStyle.equals(BoxStyle.SLIP_CASE))
					format.setBackgroundColor(new BaseColor(0xFCF151)); //yellow;		
			}
			
			String material = p.getBookMaterialCode();
			if (col==6 && material!=null)
			{
				if (material.equals(Material.LEATHER) || material.equals(Material.VINTAGE_LEATHER))
					format.setBackgroundColor(new BaseColor(0xFFA839)); //orange
				else if (material.equals(Material.FIC) || material.equals(Material.QBIC))
					format.setBackgroundColor(new BaseColor(0xD7AFF9)); //purple
			}
			
			if (col==4 && p.isPortfolio())
				format.setBackgroundColor(new BaseColor(0xffc3d4)); 
		}
			
		return format;
	}
	

}

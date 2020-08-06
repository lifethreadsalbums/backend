package com.poweredbypace.pace.binderyform.impl;


import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisConstants.Material;
import com.poweredbypace.pace.irisbook.IrisProduct;

//@Component
//@Qualifier("slipCaseRenderer")
public class SlipCaseBoxComponentsRenderer extends AbstractBoxComponentsRenderer {
	
	@Override
	protected String getBoxStyle() { return BoxStyle.SLIP_CASE; }
	
	@Override
	protected void addValueRows(PdfPTable table,
			IrisProduct product, LayoutSize layoutSize) throws BadElementException, IOException
	{
		
		addValueRow(table, "Boards:", 
				boxMeasurements.getValue("BoardWidth"), boxMeasurements.getValue("BoardHeight"), 2);
		addValueRow(table, "Walls:", 
				boxMeasurements.getValue("WallWidth"), boxMeasurements.getValue("WallHeight"), 2);
		addValueRow(table, "Spine:", 
				boxMeasurements.getValue("SpineWidth"), boxMeasurements.getValue("SpineHeight"), 1);
		addValueRow(table, "Cloth:", 
				boxMeasurements.getValue("ClothWidth"), boxMeasurements.getValue("ClothHeight"), 1);
			
		String material = product.getBookMaterialCode();
		if (StringUtils.equals(material, Material.FIC) ||
			StringUtils.equals(material, Material.QBIC))
		{
			PdfPCell c = new PdfPCell(new Phrase("LINE WITH FLOCK", BinderyFormHelper.getFont(14, true)));
			c.setColspan(6);
			c.setBorder(0);
			table.addCell(c);
		}
		

	}
}

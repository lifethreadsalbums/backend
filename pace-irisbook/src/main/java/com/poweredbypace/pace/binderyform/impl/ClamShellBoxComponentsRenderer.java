package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfPTable;
import com.poweredbypace.pace.binderyform.BoxMeasurement;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisProduct;

//@Component
//@Qualifier("clamShellRenderer")
public class ClamShellBoxComponentsRenderer extends AbstractBoxComponentsRenderer {
	
	@Override
	protected String getBoxStyle() { return BoxStyle.CLAM_SHELL; }
	
	@Override
	protected void addValueRows(PdfPTable table,
			IrisProduct product, LayoutSize layoutSize) throws BadElementException, IOException
	{
		BoxMeasurement box = boxMeasurements;
			
		addValueRow(table, "Boards:", 
				box.getValue("BoardWidth"), box.getValue("BoardHeight"), 1);
		addValueRow(table, "Spine:", 
				box.getValue("SpineWidth"), box.getValue("SpineHeight"), 1);
		addValueRow(table, "Cloth:", 
				box.getValue("ClothWidth"), box.getValue("ClothHeight"), 1);
		
		
		addEmptyRow(table);
		
		addValueRow(table, "F-TCB:", 
				box.getValue("FTCBWidth"), box.getValue("FTCBHeight"), 1);
		addValueRow(table, "Walls:", 
				box.getValue("FTCBWallWidth"), box.getValue("FTCBWallHeight"), 2);
		addValueRow(table, "Spine:", 
				box.getValue("FTCBSpineWidth"), box.getValue("FTCBSpineHeight"), 1);
		addValueRow(table, "Cloth:", 
				box.getValue("FTCBClothWidth"), box.getValue("FTCBClothHeight"), 1);
		
		addEmptyRow(table);
		
		addValueRow(table, "B-TCB:", 
				box.getValue("BTCBWidth"), box.getValue("BTCBHeight"), 1);
		addValueRow(table, "Walls:", 
				box.getValue("BTCBWallWidth"), box.getValue("BTCBWallHeight"), 2);
		addValueRow(table, "Spine:", 
				box.getValue("BTCBSpineWidth"), box.getValue("BTCBSpineHeight"), 1);
		addValueRow(table, "Cloth:", 
				box.getValue("BTCBClothWidth"), box.getValue("BTCBClothHeight"), 1);
		
		addEmptyRow(table);
		
		addValueRow(table, "Sp. Cloth:", 
				box.getValue("SpineClothWidth"), box.getValue("SpineClothHeight"), 1);
		
		addEmptyRow(table);
		
		addValueRow(table, "Liners:", 
				box.getValue("LinersWidth"), box.getValue("LinersHeight"), 2);
		addValueRow(table, "Cloth:", 
				box.getValue("LinersClothWidth"), box.getValue("LinersClothHeight"), 1);
		
	}
	
}

package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.pdf.PdfPTable;
import com.poweredbypace.pace.binderyform.BoxMeasurement;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisProduct;

//@Component
//@Qualifier("presentationBoxRenderer")
public class PresentationBoxBoxComponentsRenderer extends AbstractBoxComponentsRenderer {
	
	@Override
	protected String getBoxStyle() { return BoxStyle.PRESENTATION_BOX; }
	
	@Override
	protected void addValueRows(PdfPTable table,
			IrisProduct product, LayoutSize layoutSize) throws BadElementException, IOException
	{
		BoxMeasurement box = boxMeasurements;
		
		addValueRow(table, "Fr. Case:", 
				box.getValue("FrCaseWidth"), box.getValue("FrCaseHeight"), 1);
		addValueRow(table, "Bk. Case:", 
				box.getValue("BkCaseWidth"), box.getValue("BkCaseHeight"), 1);
		addValueRow(table, "Spine:", 
				box.getValue("SpineWidth"), box.getValue("SpineHeight"), 1);
		addValueRow(table, "Cloth:", 
				box.getValue("ClothWidth"), box.getValue("ClothHeight"), 1);
		
		addEmptyRow(table);
		
		addValueRow(table, "TCB:", 
				box.getValue("TCBWidth"), box.getValue("TCBHeight"), 1);
		addValueRow(table, "Walls:", 
				box.getValue("TCBWallWidth"), box.getValue("TCBWallHeight"), 2);
		addValueRow(table, "Spines:", 
				box.getValue("TCBSpineWidth"), box.getValue("TCBSpineHeight"), 2);
		addValueRow(table, "Cloth:", 
				box.getValue("TCBClothWidth"), box.getValue("TCBClothHeight"), 1);
		
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

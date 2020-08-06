package com.poweredbypace.pace.print.pdf;

import com.itextpdf.text.Rectangle;
import com.poweredbypace.pace.domain.layout.LayoutSize;

public class SpreadInfo extends PdfPageInfo {
	
	private LayoutSize layoutSize;

	private int numPages;
	
	public SpreadInfo(LayoutSize layoutSize, int numPages) {
		super(layoutSize);
		this.layoutSize = layoutSize;
		this.numPages = numPages;
	}
	
	public Rectangle getTrimRect() {
		return new Rectangle(
				layoutSize.getSlugOutside().floatValue() +
					layoutSize.getBleedOutside().floatValue(),
					
				layoutSize.getSlugBottom().floatValue() +
					layoutSize.getBleedBottom().floatValue(), 
				
				layoutSize.getSlugOutside().floatValue() +
					layoutSize.getBleedOutside().floatValue() +
					(layoutSize.getWidth().floatValue() * (float)numPages),
					
				layoutSize.getSlugBottom().floatValue() +
					layoutSize.getBleedBottom().floatValue() + 
					layoutSize.getHeight().floatValue());
	}

	public Rectangle getBleedRect() {
		return new Rectangle(
				layoutSize.getSlugOutside().floatValue(), 
				
				layoutSize.getSlugBottom().floatValue(), 
				
				layoutSize.getSlugOutside().floatValue() + 
					(layoutSize.getWidth().floatValue() + layoutSize.getBleedOutside().floatValue()) * (float)numPages,
					
				layoutSize.getSlugBottom().floatValue() + 
					layoutSize.getHeight().floatValue() + 
					layoutSize.getBleedTop().floatValue() +
					layoutSize.getBleedBottom().floatValue());
	}

	public Rectangle getSlugRect() {
		return new Rectangle(
				0, 
				0, 
				((layoutSize.getWidth().floatValue() + layoutSize.getBleedOutside().floatValue()) * (float)numPages) + 
					layoutSize.getSlugOutside().floatValue() * 2.0f,
					
				layoutSize.getHeight().floatValue() + 
					layoutSize.getBleedTop().floatValue() +
					layoutSize.getBleedBottom().floatValue() + 
					layoutSize.getSlugTop().floatValue() + 
					layoutSize.getSlugBottom().floatValue());
	}

}

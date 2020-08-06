package com.poweredbypace.pace.print.pdf;

import com.itextpdf.text.Rectangle;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;

public class SpoPageInfo extends PdfPageInfo {
	
	public SpoPageInfo(LayoutSize layoutSize, Spread spread) {
		super(layoutSize);
	}
	
	public Rectangle getTrimRect() {
		return new Rectangle(
			layoutSize.getSlugOutside().floatValue() +
				layoutSize.getBleedOutside().floatValue(),
				
			layoutSize.getSlugBottom().floatValue() +
				layoutSize.getBleedBottom().floatValue(), 
			
			layoutSize.getSlugOutside().floatValue() +
				(layoutSize.getBleedOutside().floatValue() * 2.0f) +
				layoutSize.getWidth().floatValue(),
				
			layoutSize.getSlugBottom().floatValue() +
				layoutSize.getBleedBottom().floatValue() + 
				layoutSize.getHeight().floatValue());
	}

	public Rectangle getBleedRect() {
		return new Rectangle(
			layoutSize.getSlugOutside().floatValue(), 
			
			layoutSize.getSlugBottom().floatValue(), 
			
			layoutSize.getSlugOutside().floatValue() + 
				(layoutSize.getBleedOutside().floatValue() * 2.0f) +
				layoutSize.getWidth().floatValue(),
				
			layoutSize.getSlugBottom().floatValue() + 
				layoutSize.getHeight().floatValue() + 
				layoutSize.getBleedTop().floatValue() +
				layoutSize.getBleedBottom().floatValue());
	}

	public Rectangle getSlugRect() {
		return new Rectangle(
			0, 
			0, 
			layoutSize.getWidth().floatValue() + 
				(layoutSize.getBleedOutside().floatValue() * 2.0f) + 
				layoutSize.getSlugOutside().floatValue() * 2.0f,
				
			layoutSize.getHeight().floatValue() + 
				layoutSize.getBleedTop().floatValue() +
				layoutSize.getBleedBottom().floatValue() + 
				layoutSize.getSlugTop().floatValue() + 
				layoutSize.getSlugBottom().floatValue());
	}

}

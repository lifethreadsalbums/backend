package com.poweredbypace.pace.print.pdf;

import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.LayoutSize.PageOrientation;


public class PdfPageInfo {
	
	public static final float kPrinterMarkOffset = 9.0f; // 0.125"
    public static final float kCropMarkLength = 15.0f;
    public static final float kCropMarkOffset = kCropMarkLength + kPrinterMarkOffset;
    
    
	protected LayoutSize layoutSize;
	
	public PdfPageInfo(final LayoutSize bookTemplate) {
		this.layoutSize = bookTemplate;
	}
	
	public LayoutSize getLayoutSize()
	{
		return layoutSize;
	}
	
	public boolean isVertical()
	{
		return layoutSize.getPageOrientation().equals(PageOrientation.Vertical);
	}
	
	public float getPageWidth()
	{
		float w = layoutSize.getWidth().floatValue();
		
		if (layoutSize.getCoverType()!=null)
		{
			if (isVertical())
			{
				w += layoutSize.getSlugTop().floatValue() + 
					 layoutSize.getSlugBottom().floatValue() +
					 layoutSize.getBleedTop().floatValue() +
					 layoutSize.getBleedBottom().floatValue();
			} else {
				w += layoutSize.getSlugInside().floatValue() + 
					 layoutSize.getSlugOutside().floatValue() +
					 layoutSize.getBleedInside().floatValue() +
					 layoutSize.getBleedOutside().floatValue();
			}
		} else { 
			w += (kCropMarkOffset * 2.0f);
		}
		
		return w;
	}
	
	public float getPageHeight()
	{
		float h = layoutSize.getHeight().floatValue();
		
		if (layoutSize.getCoverType()!=null)
		{
			if (isVertical())
			{
				h += layoutSize.getSlugInside().floatValue() + 
					 layoutSize.getSlugOutside().floatValue() +
					 layoutSize.getBleedInside().floatValue() +
					 layoutSize.getBleedOutside().floatValue();
			} else {
				h += layoutSize.getSlugTop().floatValue() + 
					 layoutSize.getSlugBottom().floatValue() +
					 layoutSize.getBleedTop().floatValue() +
					 layoutSize.getBleedBottom().floatValue();
			}
			
		} else { 
			h += (kCropMarkOffset * 2.0f);
		}
		
		return h;
	}
	
	public float getPageLeftOffset(int pageIndex)
	{
		if (layoutSize.getCoverType()!=null)
		{
			if (isVertical())
			{
				return layoutSize.getSlugBottom().floatValue();
			} else {
				float slug = pageIndex % 2==0 ? layoutSize.getSlugOutside().floatValue() : 
					layoutSize.getSlugInside().floatValue();
				
				return slug;
			}
		} else {
			float bleed = pageIndex % 2==0 ? layoutSize.getBleedOutside().floatValue() : 
				layoutSize.getBleedInside().floatValue();
			
			return kCropMarkOffset - bleed;
		}
	}
	
	public float getPageBottomOffset(int pageIndex)
	{
		if (layoutSize.getCoverType()!=null)
		{
			if (isVertical())
			{
				float slug = pageIndex % 2!=0 ? layoutSize.getSlugOutside().floatValue() : 
					layoutSize.getSlugInside().floatValue();
				return slug;
			} else
				return layoutSize.getSlugBottom().floatValue();
		} else 
			return kCropMarkOffset - layoutSize.getBleedBottom().floatValue();
	}
	
	public float getBleedTop() 
	{
		return layoutSize.getBleedTop().floatValue();
	}

	public float getBleedBottom() 
	{
		return layoutSize.getBleedBottom().floatValue();
	}
	
	public float getBleedInside() 
	{
		return layoutSize.getBleedInside().floatValue();
	}

	public float getBleedOutside() 
	{
		return layoutSize.getBleedOutside().floatValue();
	}

	public float getMarginTop() 
	{
		return layoutSize.getMarginTop().floatValue();
	}

	public float getMarginBottom() 
	{
		return layoutSize.getMarginBottom().floatValue();
	}

	public float getMarginInside() 
	{
		return layoutSize.getMarginInside().floatValue();
	}

	public float getMarginOutside() 
	{
		return layoutSize.getMarginOutside().floatValue();
	}

}

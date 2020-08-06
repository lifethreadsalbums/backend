package com.poweredbypace.pace.print.pdf;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.layout.LayoutSize;

public class PdfLfPageInfo extends PdfPageInfo {

	public PdfLfPageInfo(LayoutSize bookTemplate) {
		super(bookTemplate);
	}
	
	@Override
	public float getPageWidth()
	{
		return super.getPageWidth() - ApplicationConstants.LF_HIDDEN_AREA;
	}

}

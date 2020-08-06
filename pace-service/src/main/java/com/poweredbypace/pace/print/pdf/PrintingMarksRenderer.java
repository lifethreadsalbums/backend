package com.poweredbypace.pace.print.pdf;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;

public interface PrintingMarksRenderer {
	
	void draw(PdfContentByte cb, 
			PdfPageInfo pageInfo, 
			Product product,
			IccProfile iccProfile,
			int pageIndex, 
			int numPages) throws DocumentException, IOException;

}

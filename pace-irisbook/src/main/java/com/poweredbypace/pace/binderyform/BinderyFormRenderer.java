package com.poweredbypace.pace.binderyform;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;

public interface BinderyFormRenderer {

	public abstract void render(Document document,
			PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress) throws IOException,
			DocumentException, ImageProcessingException;

}
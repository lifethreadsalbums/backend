package com.poweredbypace.pace.binderyform.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.ProductOptionElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;

public class BinderyFormHelper {
	
	public static Font getFont(float size, boolean bold, BaseColor color)
	{
		Font font = getFont(size, bold);
		font.setColor(color);
		return font;
	}
	
	public static Font getFont(float size, boolean bold, boolean italic, BaseColor color)
	{
		Font font = getFont(size, bold, italic);
		font.setColor(color);
		return font;
	}
	
	public static Font getFont(float size, boolean bold)
	{
		return FontFactory.getFont(bold ? "HelveticaBold" : "HelveticaRegular", 
				BaseFont.CP1252, true, size);
	}
	
	public static Font getFont(float size, boolean bold, boolean italic)
	{
		String fontName = "Helvetica";
		if (bold)
			fontName += "Bold";
		if (italic)
			fontName += "Oblique";
		if (!italic && !bold)
			fontName += "Regular";
		return FontFactory.getFont(fontName, BaseFont.CP1252, true, size);
	}
	
	public static Font getFont(float size)
	{
		return getFont(size, false);
	}
	
	public static Font fitFont(String text, float maxWidth, Font font, float maxFontSize)
	{
		BaseFont bf = font.getCalculatedBaseFont(false);
		boolean textFits=false;
		float fontSize = Math.min(maxFontSize, font.getSize());
		while(!textFits) {
			float w = bf.getWidthPoint(text, fontSize);
			if (w>maxWidth)
				fontSize -= 1;
			else 
				textFits = true;
		}
		Font result = new Font(font);
		result.setSize(fontSize);
		return result;
	}
	
	public static void drawThumbLabels(PdfTemplate thumbTemplate, 
			float w, float h, String label, boolean isBlank)
	{
		if (isBlank)
		{
			Font helvetica = new Font(FontFamily.HELVETICA, 12);
			BaseFont bf_helv = helvetica.getCalculatedBaseFont(false);
			thumbTemplate.beginText(); 
			thumbTemplate.setFontAndSize(bf_helv, 12);
			thumbTemplate.showTextAligned(Element.ALIGN_CENTER, "BLANK", w/2, h/2, 0);	
			thumbTemplate.endText();
		}

		thumbTemplate.saveState();
		thumbTemplate.beginText(); 
		thumbTemplate.setFontAndSize(
				getFont(14, true).getCalculatedBaseFont(false), 14);
		thumbTemplate.setColorFill(BaseColor.RED);
		thumbTemplate.showTextAligned(Element.ALIGN_CENTER, label, w/2, h + 4f, 0);	
		thumbTemplate.endText();
		thumbTemplate.restoreState();
	}
	
	public static void drawThumbBorder(PdfTemplate thumbTemplate, float x, float y, float w, float h)
	{
		//draw border
		thumbTemplate.saveState();
		thumbTemplate.setColorStroke(BaseColor.BLACK); 
		thumbTemplate.setLineWidth(0.25f);
		thumbTemplate.rectangle(x, y, w, h);
		thumbTemplate.stroke();
		thumbTemplate.restoreState();
	}
	
	public static void drawThumbBorderAndLabels(PdfTemplate thumbTemplate, 
			float w, float h, String label, boolean isBlank)
	{
		drawThumbLabels(thumbTemplate, w, h, label, isBlank);
		drawThumbBorder(thumbTemplate, 0, 0, w, h);
	}
	
	public static Image renderCheckBox(PdfWriter writer) throws BadElementException, IOException
	{
		float size = 0.1804f * 72f;
		PdfTemplate t = writer.getDirectContent()
			.createTemplate(size, size);
		
		t.saveState();
		t.setColorStroke(BaseColor.BLACK); 
		t.setLineWidth(0.75f);
		t.rectangle(0f, 0f, size, size);
		t.stroke();
		t.restoreState();
		
		Image res = Image.getInstance(t);
		writer.releaseTemplate(t);
		return res;
	}

	public static Paragraph createTitleValueParagraph(String title, String value)
	{
		return createTitleValueParagraph(title, value, null);
	}
	
	public static Paragraph createTitleValueParagraph(String title, String value, BaseColor backgroundColor)
	{
		Paragraph p = new Paragraph();
		
		if (title!=null && value!=null)
		{
			Chunk titleChunk = new Chunk(title+": ", BinderyFormHelper.getFont(14));
			Chunk valueChunk = new Chunk(value, BinderyFormHelper.getFont(14, true));
			if (backgroundColor!=null)
			{
				titleChunk.setBackground(backgroundColor);
				valueChunk.setBackground(backgroundColor);
			}
			p.add(titleChunk);
			p.add(valueChunk);
			p.setSpacingAfter(3f);
		}
		return p;
	}
	
	public static String getFontName(String font) {
		Map<String,String> fonts = new HashMap<String, String>();
		fonts.put("NewsGothicRegular", "News Gothic");
		fonts.put("NewsGothicItalic", "News Gothic Italic");
		fonts.put("NewsGothicBold", "News Gothic Bold");
		
		fonts.put("TWCItalic", "TCM Italic");
		fonts.put("TWCRegular", "TCM Regular");
		if (fonts.containsKey(font)) return fonts.get(font);
		
		return StringUtils.join(
			StringUtils.splitByCharacterTypeCamelCase(font), ' ');
	}
	
	private static String getTextCase(String text) {
		if (text==null) return "";
		String textCase = "Upper & Lower Case";
		if (text.toLowerCase().equals(text))
			textCase = "Lower Case";
		else if (text.toUpperCase().equals(text))
			textCase = "CAPS";
		return textCase;
	}
	
	@SuppressWarnings("unchecked")
	public static List<StampInfo> getStamps(Product product)
	{
		List<StampInfo> result = new ArrayList<StampInfo>();
		for(ProductOption<?> po:product.getProductOptions()) {
			if (po instanceof ProductOptionElement) {
				ProductOptionElement poEl = (ProductOptionElement) po;
				
				com.poweredbypace.pace.domain.layout.Element element = poEl.getElement();
				
				Hibernate.initialize(element);
				if (element instanceof HibernateProxy) { 
					element = (com.poweredbypace.pace.domain.layout.Element) 
							((HibernateProxy) element).getHibernateLazyInitializer().getImplementation();
				}
				
				if (element instanceof TextStampElement) {
					TextStampElement el = (TextStampElement) element;
					String text = el.getText();
					String foil = StringUtils.capitalize(el.getFoilCode());
					
					String[] textLines = {};
					if (text!=null)
						textLines = text.split("\\n");
					
					boolean boxStamp = false;
					if ("fm_luxe".equals(product.getPrototypeProduct().getCode()) ||
						"luxe".equals(product.getPrototypeProduct().getCode())) {
							boxStamp = true;
						}
					
					for(int i=0;i<textLines.length;i++) {
						String fontFamily2 = el.getFontFamily();
						float fontSize2 = el.getFontSize()!=null ? el.getFontSize() : 36;
						
						Map<String,Object> styles = el.getStyles();
						String lineIdx = Integer.toString(i);
						if (styles!=null && styles.containsKey(lineIdx)) {
							try {
								Map<String,Object> lineStyles = (Map<String, Object>) styles.get(lineIdx);
								Map<String,Object> charStyles = (Map<String, Object>) lineStyles.get("0");
								if (charStyles.containsKey("fontFamily"))
									fontFamily2 = (String) charStyles.get("fontFamily");
								if (charStyles.containsKey("fontSize"))
									fontSize2 = (Integer) charStyles.get("fontSize");
							} catch(Exception ex) {}
						}
						
						String fontName2 = getFontName(fontFamily2) + " " + Math.round(fontSize2) + "pt";
						String stampCase2 = getTextCase(textLines[i]);
						
						StampInfo si2 = new StampInfo();
						si2.setFont(fontFamily2);
						si2.setFontSize((int)fontSize2);
						si2.setText(textLines[i]);
						si2.setFoil(foil);
						si2.setFontName(String.format("%s - %s", fontName2, stampCase2));
						si2.setBookStamp(true);
						si2.setBoxStamp(boxStamp);
						result.add(si2);
					}
				} 
			}
		}

		return result;
	}
	
}

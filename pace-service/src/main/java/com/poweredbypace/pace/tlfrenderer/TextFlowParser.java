package com.poweredbypace.pace.tlfrenderer;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class TextFlowParser {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private static float DEFAULT_FONT_SIZE = 14.0f;
	private static String DEFAULT_FONT_FAMILY = "HelveticaRegular";
	
	private FontRegistry fontRegistry;
	
	public TextFlowParser(FontRegistry fontRegistry) {
		super();
		this.fontRegistry = fontRegistry;
	}

	public TextFlow parse(String textFlowString) throws TextFlowException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new org.xml.sax.InputSource(new StringReader(textFlowString)));
			
			TextFlow textFlow = importTextFlow(doc);
			
			return textFlow;
		} catch(Exception pce) {
			throw new TextFlowException("Cannot import text flow.", pce);
		}
	}
	
	private TextFlow importTextFlow(Document doc) {
		TextFlow textFlow = new TextFlow();
		
		Element root = doc.getDocumentElement();
		parseTextFlowElement(root, textFlow);
		
		NodeList nl = root.getChildNodes();
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {
				processElement((Element)nl.item(i), textFlow.getTextBlocks());	
			}
		}
		
		return textFlow;
	}
	
	private ContentElement processElement(Element element, List<TextBlock> textBlocks) {
		if (element.getNodeName().equals("p"))
		{
			NodeList nl = element.getChildNodes();
			
			//create TextBlock from paragraph
			List<ContentElement> elements = new ArrayList<ContentElement>();
			TextBlock textBlock = new TextBlock(new GroupElement(elements)); 
			String textAlign = element.getAttribute("textAlign");
			if (textAlign!=null && textAlign.length()>0)
				textBlock.setTextAlign(textAlign);
			textBlocks.add(textBlock);
			
			if(nl != null && nl.getLength() > 0) 
			{
				for(int i = 0 ; i < nl.getLength();i++) 
				{
					ContentElement ce = processElement((Element)nl.item(i), textBlocks);
					elements.add(ce);
				}
			}
			
		} else if (element.getNodeName().equals("span")) {
			//create TextElement from span
			return createTextElementFromSpan(element);
		}
		return null;
	}
	
	private TextElement createTextElementFromSpan(Element span)
	{
		String text = span.getTextContent() ;
		ElementFormat format = new ElementFormat();
		
		String fontFamily = span.getAttribute("fontFamily");
		String fontSize = span.getAttribute("fontSize");
		String fontStyle = span.getAttribute("fontStyle");
		String fontWeight = span.getAttribute("fontWeight");
		String textDecoration = span.getAttribute("textDecoration");
		String lineHeight = span.getAttribute("lineHeight");
		String baselineShift = span.getAttribute("baselineShift");
		String color = span.getAttribute("color");
		String trackingRight = span.getAttribute("trackingRight");
		String trackingLeft = span.getAttribute("trackingLeft");
		
		if (!StringUtils.isEmpty(color))
		{
			int intColor = Integer.parseInt(color.replaceAll("#",""), 16);
			format.setColor(intColor);
		}
		
		float fFonfSize = DEFAULT_FONT_SIZE; //default font size
		if (fontSize!=null && fontSize.length()>0)
		{
			fFonfSize = Float.parseFloat(fontSize);
			format.setFontSize(fFonfSize);
		}
		
		float fLineHeight = 0f;
		if (lineHeight!=null && lineHeight.length()>0)
		{
			if (lineHeight.indexOf('%')>=0)
			{
				float percent = Float.parseFloat(lineHeight.replace("%",""));
				fLineHeight = fFonfSize * percent / 100.0f;
			} else
				fLineHeight = Float.parseFloat(lineHeight);
		} else 
			fLineHeight = fFonfSize * 1.2f ;//default line height = 120%;
		format.setLineHeight(fLineHeight);
		
		float fBaselineShift = 0f;
		if (baselineShift!=null && baselineShift.length()>0)
		{
			fBaselineShift = Float.parseFloat(baselineShift);
		}
		format.setBaselineShift(fBaselineShift);
		
		float fTrackingLeft = 0f;
		if (trackingLeft!=null && trackingLeft.length()>0)
		{
			fTrackingLeft = Float.parseFloat(trackingLeft);
		}
		format.setTrackingLeft(fTrackingLeft);
		
		float fTrackingRight = 0f;
		if (trackingRight!=null && trackingRight.length()>0)
		{
			fTrackingRight = Float.parseFloat(trackingRight);
		}
		format.setTrackingRight(fTrackingRight);
		
		if (StringUtils.isEmpty(fontFamily))
			fontFamily = DEFAULT_FONT_FAMILY;
		
		format.setFont(getFont(fontFamily, fFonfSize, fontStyle, fontWeight, textDecoration));
		format.setFontPath(fontRegistry.getFontPath(fontFamily));
		
		return new TextElement(text, format);
	}
	
	private Font getFont(String fontFamily, float fontSize, String fontStyle, String fontWeight, String textDecoration) 
	{
		Font font=null;
		
		int style=Font.NORMAL;
		
		if (fontStyle!=null && fontStyle.equals("italic"))
			style+=Font.ITALIC;
		if (fontWeight!=null && fontWeight.equals("bold"))
			style+=Font.BOLD;
		if (textDecoration!=null && textDecoration.equals("underline"))
			style+=Font.UNDERLINE;
		
		BaseFont bf;
		try {
			String fontPath = fontRegistry.getFontPath(fontFamily);
			//bf = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, true);
			bf = BaseFont.createFont(fontPath, BaseFont.CP1252, true);
			bf.setSubset(false);
			font = new Font(bf, fontSize, style);
		} catch (DocumentException e) {
			logger.error(String.format("Cannot create font %s", fontFamily), e);
		} catch (IOException e) {
			logger.error(String.format("Cannot create font %s", fontFamily), e);
		}
		
		return font;
	}
	
	private void parseTextFlowElement(Element element, TextFlow textFlow)
	{
		String paddingLeft = element.getAttribute("paddingLeft");
		String paddingTop = element.getAttribute("paddingTop");
		String paddingRight = element.getAttribute("paddingRight");
		String paddingBottom = element.getAttribute("paddingBottom");
		if (paddingLeft!=null && paddingLeft.matches("\\d+"))
			textFlow.setPaddingLeft(Float.parseFloat(paddingLeft));
		
		if (paddingTop!=null && paddingTop.matches("\\d+"))
			textFlow.setPaddingTop(Float.parseFloat(paddingTop));
		
		if (paddingRight!=null && paddingRight.matches("\\d+"))
			textFlow.setPaddingRight(Float.parseFloat(paddingRight));
		
		if (paddingBottom!=null && paddingBottom.matches("\\d+"))
			textFlow.setPaddingBottom(Float.parseFloat(paddingBottom));
		
		String textRotation = element.getAttribute("textRotation");
		if (textRotation!=null && textRotation.length()>0)
			textFlow.setTextRotation(textRotation);
		
		String verticalAlign = element.getAttribute("verticalAlign");
		if (verticalAlign!=null && verticalAlign.length()>0)
			textFlow.setVerticalAlign(verticalAlign);
		
	}
}

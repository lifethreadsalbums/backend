package com.poweredbypace.pace.tlfrenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.domain.layout.TextElement;
import com.poweredbypace.pace.exception.JsonException;
import com.poweredbypace.pace.util.ColorUtils;

public class FabricToTlfConverter {

	public String convert(TextElement el) {
		
		StringBuilder b = new StringBuilder();
		b.append("<TextFlow xmlns=\"http://ns.adobe.com/textLayout/2008\" fontLookup=\"embeddedCFF\" "); 
		b.append("paddingBottom=\"2\" paddingLeft=\"2\" paddingRight=\"2\" paddingTop=\"2\" renderingMode=\"cff\" ");
		b.append("textIndent=\"0\" textRotation=\"rotate0\" whiteSpaceCollapse=\"preserve\" version=\"2.0.0\" ");
		
		attr(b, "fontFamily", el.getFontFamily());
		attr(b, "fontSize", el.getFontSize());
		
		b.append(">");
		
		ObjectMapper mapper = new ObjectMapper();
		TextStyles styles = null;
		try {
			String json = el.getStylesAsString();
			styles = mapper.readValue(json, TextStyles.class);
		} catch (Exception e) {
			throw new JsonException(e);
		}
		
		FontStyle defaultFontStyle = new FontStyle();
		defaultFontStyle.fontFamily = el.getFontFamily();
		defaultFontStyle.fontSize = el.getFontSize();
		defaultFontStyle.fill = el.getFill()!=null ? hexColor(el.getFill()) : "#000000";
		
		for(TextLine line:getLines(el, styles)) {
			b.append("<p textAlign=\"");
			b.append(el.getTextAlign());
			b.append("\">");
			for(TextChunk chunk:line.chunks) {
				b.append("<span ");
				
				FontStyle fs = chunk.fontStyle!=null ? chunk.fontStyle : defaultFontStyle;
				attr(b, "fontFamily", defaultVal(fs.fontFamily, defaultFontStyle.fontFamily));
				attr(b, "fontSize", defaultVal(fs.fontSize, defaultFontStyle.fontSize));
				attr(b, "color", hexColor(defaultVal(fs.fill, defaultFontStyle.fill)));
				
				b.append(">");
				
				String text = StringEscapeUtils.escapeXml(chunk.text);
				
				b.append(text);
				b.append("</span>");
			}
			b.append("</p>");
		}
		b.append("</TextFlow>");
		return b.toString();
	}
	
	private <T> T defaultVal(T val, T defaultVal) {
		return (val!=null) ? val : defaultVal;
	}
	
	private void attr(StringBuilder b, String attr, Object value) {
		if (value!=null && StringUtils.isNotEmpty(value.toString())) {
			b.append(attr + "=\"" + value + "\" ");
		}
	}
	
	private String hexColor(String color) {
		if ("black".equals(color)) return "#000000";
		return ColorUtils.toHex( ColorUtils.parseColor(color) );
	}
	
	private List<TextLine> getLines(TextElement el, TextStyles styles) {
		
		String text = el.getText();
		String [] lines = text.split("\n");
		List<TextLine> result = new ArrayList<FabricToTlfConverter.TextLine>();
		int idx = 0;
		for(String line:lines) {
			TextLine textLine = new TextLine();
			textLine.text = line;
			textLine.index = idx++;
			textLine.chunks = getChunks(el, styles, textLine);
			result.add(textLine);
		}
		
		return result;
	}
	
	private List<TextChunk> getChunks(TextElement el, TextStyles styles, TextLine line) {
		
		List<TextChunk> result = new ArrayList<FabricToTlfConverter.TextChunk>();
		
		LineStyle lineStyle = styles.get(line.index);
		if (lineStyle==null) {
			TextChunk chunk = new TextChunk();
			chunk.text = line.text;
			result.add(chunk);
			return result;
		}
		
		String chunkText = "";
		FontStyle prevStyle = null;
		for(int i=0;i<line.text.length();i++) {
			
			FontStyle style = styles.get(line.index).get(i);
			
			if (prevStyle!=null && !prevStyle.equals(style)) {
				TextChunk chunk = new TextChunk();
				chunk.text = chunkText;
				chunk.fontStyle = prevStyle;
				result.add(chunk);
				chunkText = "";
			}
			chunkText += line.text.charAt(i);
			prevStyle = style;
		}
		if (chunkText.length()>0) {
			TextChunk chunk = new TextChunk();
			chunk.text = chunkText;
			chunk.fontStyle = prevStyle;
			result.add(chunk);
		}
		
		return result;
	}
	
	private static class TextLine {
		public String text;
		public int index;
		public List<TextChunk> chunks;
	}
	
	private static class TextChunk {
		public String text;
		public FontStyle fontStyle;
	}
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	public static class FontStyle {
		public String fontFamily;
		public String fill;
		public Float fontSize;
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((fill == null) ? 0 : fill.hashCode());
			result = prime * result
					+ ((fontFamily == null) ? 0 : fontFamily.hashCode());
			result = prime * result
					+ ((fontSize == null) ? 0 : fontSize.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FontStyle other = (FontStyle) obj;
			if (fill == null) {
				if (other.fill != null)
					return false;
			} else if (!fill.equals(other.fill))
				return false;
			if (fontFamily == null) {
				if (other.fontFamily != null)
					return false;
			} else if (!fontFamily.equals(other.fontFamily))
				return false;
			if (fontSize == null) {
				if (other.fontSize != null)
					return false;
			} else if (!fontSize.equals(other.fontSize))
				return false;
			return true;
		}
		
		public FontStyle() { }
	}
	
	public static class LineStyle extends HashMap<Integer, FontStyle> {
		private static final long serialVersionUID = 9165817340542468187L;
		public LineStyle() { }
	}
	
	public static class TextStyles extends HashMap<Integer, LineStyle> {
		private static final long serialVersionUID = 1743708663183080494L;
		public TextStyles() { }
	}
	
}

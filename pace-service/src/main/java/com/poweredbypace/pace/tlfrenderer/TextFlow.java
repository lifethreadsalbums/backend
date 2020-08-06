package com.poweredbypace.pace.tlfrenderer;

import java.util.ArrayList;
import java.util.List;

public class TextFlow {
	
	public static String ROTATE_0 = "rotate0";
	public static String ROTATE_180 = "rotate180";
	public static String ROTATE_270 = "rotate270";
	public static String ROTATE_90 = "rotate90";
	
	private float paddingLeft = 0f;
	private float paddingRight = 0f;
	private float paddingTop = 0f;
	private float paddingBottom = 0f;
	private String textRotation = ROTATE_0;
	private String verticalAlign = "top";
	
	private List<TextBlock> textBlocks;
	
	public String getTextRotation() {
		return textRotation;
	}
	public void setTextRotation(String textRotation) {
		this.textRotation = textRotation;
	}
	public float getPaddingLeft() {
		return paddingLeft;
	}
	public void setPaddingLeft(float paddingLeft) {
		this.paddingLeft = paddingLeft;
	}
	public float getPaddingRight() {
		return paddingRight;
	}
	public void setPaddingRight(float paddingRight) {
		this.paddingRight = paddingRight;
	}
	public float getPaddingTop() {
		return paddingTop;
	}
	public void setPaddingTop(float paddingTop) {
		this.paddingTop = paddingTop;
	}
	public float getPaddingBottom() {
		return paddingBottom;
	}
	public void setPaddingBottom(float paddingBottom) {
		this.paddingBottom = paddingBottom;
	}
	
	
	public String getVerticalAlign() {
		return verticalAlign;
	}
	public void setVerticalAlign(String verticalAlign) {
		this.verticalAlign = verticalAlign;
	}
	public List<TextBlock> getTextBlocks() {
		return textBlocks;
	}
	public void setTextBlocks(List<TextBlock> textBlocks) {
		this.textBlocks = textBlocks;
	}
	
	public TextFlow() {
		super();
		textBlocks = new ArrayList<TextBlock>();
	}
	
	public float calculateHeight(float width)
	{
		float h = 0;
		int lineIndex=0;
		List<TextBlock> blocks = getTextBlocks();
		for(TextBlock textBlock:blocks)
		{
			List<TextLine> lines = textBlock.createTextLines(width);
			if (lines.size()==1 && blocks.size()==1)
			{
				h += lines.get(0).getAscent() + lines.get(0).getDescent();
			} else {
				for(TextLine tl:lines)
				{
					if (lineIndex==0)
						h += tl.getAscent()+tl.getDescent();
					else
						h += tl.getLineHeight();
				}
			}
			lineIndex++;
		}
		return h;
	}

}

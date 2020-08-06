package com.poweredbypace.pace.print;

import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.CMYKColor;

public class ColorConverter   {

	protected final Log logger = LogFactory.getLog(getClass());
	
	private ColorSpace colorSpace;
	private String iccProfile;
	
	

	public void setIccProfile(String iccProfile) {
		this.iccProfile = iccProfile;
	}
	

	@PostConstruct
	public void postConstruct()
	{
		try {
			colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(iccProfile));
		} catch (Throwable t) {
			logger.error(t);
		}
	}
	
	public CMYKColor toCMYKColor(BaseColor color) {
		ColorSpace cs = colorSpace;
		
		float r = color.getRed() / 255f;
		float g = color.getGreen() / 255f;
		float b = color.getBlue() / 255f;
		
		float[] cmyk = cs.fromRGB(new float[] {r, g, b});
		return new CMYKColor(cmyk[0], cmyk[1], cmyk[2], cmyk[3]);
	}

}

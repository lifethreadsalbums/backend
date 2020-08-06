package com.poweredbypace.pace.irisbook;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.poweredbypace.pace.irisbook.IrisConstants.ProductType;

public class IrisUtils {

	public static String formatJobId(String batchNumber, String jobId) {
		return String.format("%s%s", StringUtils.isEmpty(batchNumber) ? ""
				: batchNumber + "-", jobId);
	}

	public static String getReducedShapeCode(String originalShape, String scale) {
		String reducedShape;
		char shapeCode = originalShape.charAt(originalShape.length() - 1);
		if (scale.equals("100%"))
			reducedShape = originalShape;
		else if (scale.equals("71.5%"))
			reducedShape = "M" + shapeCode;
		else if (scale.equals("52.4%") || scale.equals("73.4%"))
			reducedShape = "S" + shapeCode;
		else
			reducedShape = originalShape;

		// exception for portfolio shapes
		if (!reducedShape.equals(originalShape)
				&& originalShape.indexOf("P-") == 0)
			reducedShape = "P-" + reducedShape;

		return reducedShape;
	}
	
	public static String getShapePaperCode(String shape, String productType, String paperType) {
		if (ProductType.FLUSHMOUNT.equals(productType))
			shape +=  "-FM";
		Map<String,String> bfCodes = new HashMap<String, String>();
		
		bfCodes.put("futura_matte_lg", "LF-M");
		bfCodes.put("futura_matte_sg", "LF-M");
		bfCodes.put("luster", "LF-L");       
		bfCodes.put("mccoy_matte", "S-M");
		bfCodes.put("opus_matte_100", "S-M");
		bfCodes.put("opus_matte_65", "S-M");
		bfCodes.put("mohawk_felt", "M");
		bfCodes.put("prophoto_heavy", "L");	
		bfCodes.put("classic_crest", "M");
		bfCodes.put("lasal_luster", "L");
		bfCodes.put("lassal_matte", "M");
		bfCodes.put("enhanced_velvet", "M");
		bfCodes.put("fuji_luster", "L");    
		bfCodes.put("fuji_matte", "M");
		
		if (bfCodes.containsKey(paperType))
			shape += "-" + bfCodes.get(paperType);
		
		return shape;
	}
	
	public static int getPDFVersion(String url)	{
		int version = 0;
		if (url!=null)
		{
			Pattern pattern = Pattern.compile("v(\\d+).pdf$");
	        Matcher matcher = pattern.matcher(url);
	        if (matcher.find())
	        {
	        	String grp = matcher.group(1);
	        	version = Integer.parseInt(grp);
	        }
		}
		return version;
	}
	
	
}

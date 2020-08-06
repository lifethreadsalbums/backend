package com.poweredbypace.pace.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;

public class PACEUtils {
	
	public static List<Integer> getReprintPages(Product product) {
		if (product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased)
			return parseSpreadNumbers(product);
		else
			return parsePageNumbers(product);
	}
	
	public static List<Integer> parseSpreadNumbers(Product product)	{
		String reprintPages = product.getReprintPages();
		if (StringUtils.isEmpty(reprintPages))
			return new ArrayList<Integer>();
		if (StringUtils.equals(reprintPages.toLowerCase(), "all"))
			return null;
		//int lps = product.getPrototypeProduct().getFirstPageType()==FirstPageType.LeftPageStart ? 1 : 0;
		List<Integer> result = new ArrayList<Integer>();
		Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*\\-[0-9]*\\.?[0-9]+)|([0-9]+\\.?[0-9]*)");
        Matcher matcher = pattern.matcher(reprintPages);
        while (matcher.find()) {
        	
        	String grp = matcher.group();
        	if (grp.indexOf("-")>0)
        	{
        		String[]range = grp.split("-");
        		float from = Float.parseFloat(range[0]);
        		float to = (int)Math.ceil( Float.parseFloat(range[1]) );
        		
        		if (from<1)
        			from = 1;
        		else
        			from = (int)Math.ceil(from);
        		
        		if (to==from) {
        			result.add((int)to);
        		} else if (to<from)
        			continue;
        		
        		for(int i=(int)from;i<=to;i++) {
        			result.add(i);
        		}
        		
        	} else {
        		float num = Float.parseFloat(grp);
        		if (num<1)
        			result.add(0);
        		else
        			result.add((int)Math.ceil(num));
        	}
        	//logger.debug(matcher.group()+","+matcher.group(0)+","+matcher.group(1));
        }
        
        //for(int n:result)
        //	logger.debug(n);
        return result;
        
	}
	
	public static List<Integer> parsePageNumbers(Product product) {
		String reprintPages = product.getReprintPages();
		if (StringUtils.isEmpty(reprintPages))
			return null;
		if (StringUtils.equals(reprintPages.toLowerCase(), "all"))
			return null;
		List<Integer> result = new ArrayList<Integer>();
		Pattern pattern = Pattern.compile("(\\d+\\-\\d+)|(\\d+)");
        Matcher matcher = pattern.matcher(reprintPages);
        while (matcher.find()) {
        	
        	String grp = matcher.group();
        	if (grp.indexOf("-")>0)
        	{
        		String[]range = grp.split("-");
        		int from = Integer.parseInt(range[0]);
        		int to = Integer.parseInt(range[1]);
        		if (to==from)
        		{
        			if (to%2==0)
        				to--;
        			result.add(to);
        			result.add(to+1);
        		} else if (to<from)
        			continue;
        		if (from%2==0)
        			from--;
        		
        		for(int i=from;i<=to;i+=2)
        		{
        			result.add(i);
        			result.add(i+1);
        		}
        		
        	} else {
        		int num = Integer.parseInt(grp);
        		if (num%2==0)
        			num--;
        		result.add(num);
        		result.add(num+1);
        	}
        	//logger.debug(matcher.group()+","+matcher.group(0)+","+matcher.group(1));
        }
        
        //for(int n:result)
        //	logger.debug(n);
        return result;
        
	}
	
	public static String getPrintFilename(Product product, String ext) {
		String filename = UrlUtil.slug( String.format("%s%s_%s.%s",
				product.getBatch()!=null ? product.getBatch().getName() + "-" : "",
				product.getProductNumber()!=null ? product.getProductNumber() : product.getId(),
				product.getName(),
				ext));
		return filename;
	}

}

package com.poweredbypace.pace.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.apache.commons.lang.StringUtils;


public class URLUtils {
	
	public static URL makeURL(String urlStr) throws URISyntaxException, MalformedURLException
	{
		URL url = new URL(urlStr);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();
		return url;
	}
	
	public static String slug(String input) {
        if (StringUtils.isEmpty(input)) {
            return "";
        }
        String out = normalize(input);
        out = removeDuplicateWhiteSpaces(out);
        out = out.toLowerCase();
        out = out.replace(" ", "-");
 
        return out;
    }
 
    private static String normalize(String input) {
        String result = Normalizer.normalize(input, Form.NFD).replaceAll("[^\\p{ASCII}]", ""); // 1
        result = result.replaceAll("[^a-zA-Z0-9\\s\\.]", " "); // 2
 
        return result;
    }
 
    private static String removeDuplicateWhiteSpaces(String input) {
        return input.replaceAll("\\s+", " ");
    }
}

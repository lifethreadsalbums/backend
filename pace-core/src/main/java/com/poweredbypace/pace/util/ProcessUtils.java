package com.poweredbypace.pace.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.io.CharStreams;

public class ProcessUtils {

	private static final Log logger = LogFactory.getLog(ProcessUtils.class);
	private static final String execPath = "/usr/bin:/usr/local/bin:/opt/local/bin";
	
	public static String findCommandPath(String command)
	{
		String result = null;
		try {
			ProcessBuilder pb = new ProcessBuilder("which", command); 
			
			String oldPath = pb.environment().get("PATH");
			String newPath = execPath + ":" + oldPath;
			pb.environment().put("PATH", newPath);
			
			pb.redirectErrorStream(true);
			final Process process = pb.start();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(process
					.getInputStream()));
			String currentLine = null;
			while ((currentLine = in.readLine()) != null)
			{
				if (currentLine.length()>0)
				{
					result = currentLine;
					break;
				}
			}
			
			process.waitFor();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.getErrorStream().close();
	
		} catch (Exception ex) {
			result = null;
		} 
		return result;
		
	}
	
	public static int exec(String... args) throws IOException, InterruptedException  
	{
		List<String> argList = new ArrayList<String>();
		argList.addAll( Arrays.asList(args) );
		
		ProcessBuilder pb = new ProcessBuilder(argList); 
		
		String oldPath = pb.environment().get("PATH");
		String newPath = execPath + ":" + oldPath;
		pb.environment().put("PATH", newPath);
		
		pb.redirectErrorStream(true);
		final Process process = pb.start();
		
		String output = CharStreams.toString(new InputStreamReader(process.getInputStream(), "UTF-8"));
		if (logger.isDebugEnabled() && StringUtils.isNotBlank(output))
			logger.debug(output);
		
		int ret = process.waitFor();
		
		if (ret!=0) {
			String errorString = CharStreams.toString(new InputStreamReader(process.getErrorStream(), "UTF-8"));
			throw new RuntimeException(errorString);
		}
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
			
		return ret;
	}
	
	public static int exec(File dir, String... args) throws IOException, InterruptedException  
	{
		List<String> argList = new ArrayList<String>();
		argList.addAll( Arrays.asList(args) );
		
		ProcessBuilder pb = new ProcessBuilder(argList); 
		pb.directory(dir);
		
		String oldPath = pb.environment().get("PATH");
		String newPath = execPath + ":" + oldPath;
		pb.environment().put("PATH", newPath);
		
		pb.redirectErrorStream(true);
		final Process process = pb.start();
		
		String output = CharStreams.toString(new InputStreamReader(process.getInputStream(), "UTF-8"));
		if (logger.isDebugEnabled() && StringUtils.isNotBlank(output))
			logger.debug(output);
		
		int ret = process.waitFor();
		
		if (ret!=0) {
			String errorString = CharStreams.toString(new InputStreamReader(process.getErrorStream(), "UTF-8"));
			throw new RuntimeException(errorString);
		}
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
			
		return ret;
	}
	
}

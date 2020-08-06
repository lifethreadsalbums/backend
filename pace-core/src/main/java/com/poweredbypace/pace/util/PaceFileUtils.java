package com.poweredbypace.pace.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;

public class PaceFileUtils {

	public static void downloadFile(URL url, String filename)
			throws IOException {
		BufferedInputStream in = new BufferedInputStream(url.openStream());
		FileOutputStream fos = new FileOutputStream(filename);
		BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
		byte[] data = new byte[1024];
		int x = 0;
		while ((x = in.read(data, 0, 1024)) >= 0) {
			bout.write(data, 0, x);
		}
		bout.close();
		in.close();
	}
	
	public static File zip(File... files) throws IOException, InterruptedException {
		File zip = File.createTempFile("pace-zip-", ".zip");
		zip.delete();
		
		String[] args = new String[files.length + 4]; 
		args[0] = "zip";
		args[1] = "-0";
		args[2] = "-j";
		args[3] = zip.getAbsolutePath();
		for(int i=0;i<files.length;i++) {
			args[i+4] = files[i].getAbsolutePath();
		}
		ProcessUtils.exec(args);
		return zip;
	}

	public static int zipFile(String zipCommand, String inputFilename,
			String outputFilename) throws IOException, InterruptedException {
		Runtime rt = Runtime.getRuntime();
		String zip = String.format(zipCommand, outputFilename, inputFilename);

		Process pr = rt.exec(zip);

		int exitVal = pr.waitFor();
		return exitVal;
	}

	public static String appendStringBeforeExtension(String filename,
			String textToAppend) {
		String res = filename;
		int idx = res.lastIndexOf(".");
		if (idx >= 0)
			res = res.substring(0, idx) + textToAppend + res.substring(idx);
		else
			res += textToAppend;
		return res;
	}

	public static String formatSize(long longSize, int decimalPos) {
		NumberFormat fmt = NumberFormat.getNumberInstance();
		if (decimalPos >= 0) {
			fmt.setMaximumFractionDigits(decimalPos);
		}
		final double size = longSize;
		double val = size / (1024 * 1024);
		if (val > 1) {
			return fmt.format(val).concat(" MB");
		}
		val = size / 1024;
		if (val > 10) {
			return fmt.format(val).concat(" KB");
		}
		return fmt.format(val).concat(" bytes");
	}
	
	public static String sanitizeFilename(String filename)
	{
		String result = filename.replaceAll("/", "_");
		result = result.replaceAll("\\\\", "_");
		return result;
	}
	
	private static final int TEMP_DIR_ATTEMPTS = 10000;
	
	public static File createTempDir() {
		File baseDir = new File(System.getProperty("java.io.tmpdir"));
		String baseName = System.currentTimeMillis() + "-";

		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (tempDir.mkdir()) {
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within "
			+ TEMP_DIR_ATTEMPTS + " attempts (tried "
			+ baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}
}
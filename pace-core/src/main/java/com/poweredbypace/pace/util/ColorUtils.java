package com.poweredbypace.pace.util;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class ColorUtils {

	/**
	 * Regular expression for "RGB( 255, 0, 0 )", integer range 0 - 255
	 */

	private static final String COLOR_CSS_PATTERN = "[rR][gG][bB][(]" + //$NON-NLS-1$
			"[\\s]*[\\d]+[\\s]*[,]" + //$NON-NLS-1$
			"[\\s]*[\\d]+[\\s]*[,]" + //$NON-NLS-1$
			"[\\s]*[\\d]+[\\s]*" + //$NON-NLS-1$
			"[)]"; //$NON-NLS-1$

	/**
	 * Regular expression for "RGB( 255.0%, 0.0%, 0.0% )", float range 0.0% -
	 * 100.0%
	 */

	private static final String COLOR_CSS_PERCENT_PATTERN = "[rR][gG][bB][(]" + //$NON-NLS-1$
			"[\\s]*[\\d]+(.\\d+)?[\\s]*[%][\\s]*[,]" + //$NON-NLS-1$
			"[\\s]*[\\d]+(.\\d+)?[\\s]*[%][\\s]*[,]" + //$NON-NLS-1$
			"[\\s]*[\\d]+(.\\d+)?[\\s]*[%][\\s]*" + //$NON-NLS-1$
			"[)]"; //$NON-NLS-1$

	/**
	 * Compiled pattern for CSS absolute pattern: "RGB( 255, 0, 0 )"
	 */

	private static Pattern cssAbsolutePattern = Pattern
			.compile(COLOR_CSS_PATTERN);

	/**
	 * Compiled pattern for CSS relative pattern: "RGB( 255%, 0%, 0% )"
	 */
	private static Pattern cssRelativePattern = Pattern
			.compile(COLOR_CSS_PERCENT_PATTERN);

	/**
	 * Indicates whether the color value is of valid css absolute format:
	 * "RGB(r,g,b)". The RGB prefix is case insensitive and r, g, b should be
	 * integer value. Whitespace characters are allowed around the numerical
	 * values. The followings are some cases of valid css absolute colors:
	 * <p>
	 * <ul>
	 * <li>RGB(255,0,0)</li>
	 * <li>Rgb( 255, 0, 0)</li>
	 * <li>rgb(300,300,300)</li>
	 * </ul>
	 * 
	 * @param value
	 *            a string color value
	 * @return <code>true</code> if the color value is in a valid css absolute
	 *         color representation.
	 */

	public static boolean isCssAbsolute(String value) {
		return cssAbsolutePattern.matcher(value).matches();
	}

	/**
	 * Indicates whether the color value is of a valid css relative format:
	 * "RGB( r%, g%, b%)". The RGB prefix is case insensitive and r, g, b should
	 * be a float value. Whitespace characters are allowed around the numerical
	 * values. The followings are some cases of valid css relative colors:
	 * 
	 * <p>
	 * <ul>
	 * <li>RGB(100%,0%,0%)</li>
	 * <li>Rgb( 100% , 0% , 0% )</li>
	 * <li>rgb(200%,200%,200%)</li>
	 * </ul>
	 * 
	 * @param value
	 *            a string color value
	 * @return <code>true</code> if the color value is in a valid css relative.
	 *         color representation.
	 */

	public static boolean isCssRelative(String value) {
		return cssRelativePattern.matcher(value).matches();
	}

	/**
	 * Parse a valid css color expressed in absolute or relative format as
	 * "RGB(r%,g%,b%)" or "RGB(r,g,b)" and return the integer value of the
	 * color.
	 * 
	 * @param rgbColor
	 *            input color value of a valid css relative or absolute format.
	 * @return an integer value of the color representation. Return
	 *         <code>-1</code> if the string is not in a valid absolute or
	 *         relative representation.
	 * 
	 * @see #isCssAbsolute(String)
	 * @see #isCssRelative(String)
	 */

	private static int parseRGBColor(String rgbColor) {
		// not valid, return -1.

		if (!isCssAbsolute(rgbColor) && !isCssRelative(rgbColor))
			return -1;

		boolean hasPercentage = false;

		int start = rgbColor.indexOf('(');
		int end = rgbColor.indexOf(')');

		// cut from "rgb(255,0,0)" to "255, 0, 0", rgb(100%,100%,100%) to
		// "100%,100%,100%"

		String subStr1 = rgbColor.substring(start + 1, end).trim();

		if (subStr1.indexOf('%') != -1) {
			// get rid of '%'
			subStr1 = subStr1.replace('%', ' ');
			hasPercentage = true;
		}

		// split into 3 strings, we got {255,0,0}

		String[] numbers = subStr1.split(","); //$NON-NLS-1$

		// #FF0000
		String colorValue = "#"; //$NON-NLS-1$

		// parse String( base 10 ) into String( base 16 )

		for (int i = 0; i < 3; i++) {
			int intValue = 0;

			String number = numbers[i].trim();

			if (hasPercentage) {
				float value = Float.parseFloat(number);

				if (value > 100.0f)
					value = 100.0f;

				// 100.0% => 255, 0.0% => 0

				intValue = (int) (value * 255.0f / 100.0f + 0.5f);
			} else {
				intValue = Integer.parseInt(number);

				// e,g. clip "300" to "255"
				if (intValue > 255)
					intValue = 255;
			}

			// Fill "f" to "0f"

			String strValue = "0" + Integer.toHexString(intValue); //$NON-NLS-1$
			strValue = strValue.substring(strValue.length() - 2);

			colorValue = colorValue + strValue;
		}

		return Integer.decode(colorValue).intValue();

	}

	/**
	 * Parses the string color value as a color keyword or a numerical RGB
	 * notation, return its corresponding rgb integer value. The string value
	 * can be one of the followings:
	 * 
	 * <ul>
	 * <li>A decimal number: "16711680". The number will be clipped into
	 * 0~#FFFFFF</li>
	 * <li>A hexadecimal number in HTML format. '#' immediately followed by
	 * either three or six hexadecimal characters. The three-digit RGB notation
	 * (#rgb) is converted into six-digit form (#rrggbb) by replicating digits,
	 * not by adding zeros. For example, #fb0 expands to #ffbb00. Values outside
	 * "#FFFFFF" will be clipped.</li>
	 * <li>A hexadecimal number in Java format: "0xRRGGBB"</li>
	 * <li>A css predefined color name: "red", "green", "yellow" etc.</li>
	 * <li>A css absolute or relative notation: "rgb(r,g,b)" or "rgb(r%,g%,b%)".
	 * 'rgb(' followed by a comma-separated list of three numerical values
	 * (either three integer values in the range of 0-255, or three percentage
	 * values in the range of 0.0% to 100.0%) followed by '), Values outside the
	 * numerical ranges will be clipped. Whitespace characters are allowed
	 * around the numerical values.</li>
	 * </ul>
	 * <p>
	 * These examples given a allowed color value that can be parsed into an
	 * integer.
	 * <ul>
	 * <li>"16711680"</li>
	 * <li>"#FFFF00"</li>
	 * <li>"#FFFF00F", will be clipped into "#FFFFFF"</li>
	 * <li>"0xFF00FF"</li>
	 * <li>"red" or "green"</li>
	 * <li>rgb(255,0,0)</li>
	 * <li>rgb(100.0%,0%,0%)</li>
	 * </ul>
	 * 
	 * @param value
	 *            a given string containing one of the allowed notation.
	 * @return the integer value of the color, return <code>-1</code> if the
	 *         value is not in one of the allowed format. If the value is in a
	 *         valid integer format, return value will be clipped to 0 ~
	 *         0xFFFFFF
	 * 
	 * 
	 */

	public static int parseColor(String value) {
		if (StringUtils.isBlank(value))
			return -1;

		// 1. Is the value a hexadecimal number or decimal number. It can be
		// six-digit form (#rrggbb) or three-digit form (#rgb) or java style
		// as (0xRRGGBB )

		int first = value.charAt(0);
		if (first == '#' || (first >= '0' && first <= '9')) {
			if (first == '#' && value.length() == 4) {
				// #RGB
				char[] rgb_chars = value.toCharArray();
				char[] rrggbb_chars = { '#', rgb_chars[1], rgb_chars[1],
						rgb_chars[2], rgb_chars[2], rgb_chars[3], rgb_chars[3] };

				value = String.valueOf(rrggbb_chars);
			}

			try {
				int retValue = Integer.decode(value).intValue();

				if (retValue > 0xFFFFFF)
					return 0xFFFFFF;

				return retValue;
			} catch (NumberFormatException e) {
				return -1;
			}

		}

		// 3. CSS absolute or relative format: {rgb(r,g,b)} or {rgb(r%,g%,b%)}

		if (isCssAbsolute(value) || isCssRelative(value))
			return parseRGBColor(value);

		return -1;
	}

	/**
	 * Returns the Red, Blue, Green value for a integer RGB color value. The
	 * given RGB value should be in the scope of (0~0xFFFFFF), otherwise return
	 * <code>null</code>.
	 * 
	 * @param rgbValue
	 *            a given integer RGB color value.
	 * @return an array containing Red, Blue, Green separately. Return
	 *         <code>null</code> if the value is not in (0~0xFFFFFF).
	 */

	public static int[] getRGBs(int rgbValue) {
		if (rgbValue > 0xFFFFFF || rgbValue < 0)
			return null;

		int rgb = rgbValue;

		int r = rgb >> 16;
		int g = (rgb >> 8) & 0xFF;
		int b = rgb & 0xFF;

		return new int[] { r, g, b };
	}

	/**
	 * Returns the Red, Blue, Green value for a color value. The given string
	 * containing one of the allowed notations.
	 * 
	 * @param colorValue
	 *            a given string color value in one of the allowed notations.
	 * @return an array containing Red, Blue, Green separately. Return
	 *         <code>null</code> if the given color value is not parsable.
	 */

	public static int[] getRGBs(String colorValue) {
		int rgb = parseColor(colorValue);
		return rgb == -1 ? null : getRGBs(rgb);
	}

	/**
	 * Calculates the integer color value given its red, blue, green values. If
	 * any color factor is over 0xFF, it will be clipped to 0xFF; if any color
	 * factor is below 0, it will be increased to 0.
	 * 
	 * 
	 * @param r
	 *            red value.
	 * @param g
	 *            green value.
	 * @param b
	 *            blue value.
	 * @return the integer color value of the given color factors.
	 */

	public static int formRGB(int r, int g, int b) {
		// clip to 0 ~ 0xFF

		r = r > 0xFF ? 0xFF : r < 0 ? 0 : r;
		g = g > 0xFF ? 0xFF : g < 0 ? 0 : g;
		b = b > 0xFF ? 0xFF : b < 0 ? 0 : b;

		return (r << 16) + (g << 8) + b;
	}

	public static String toHex(int color) {
		int[] rgb = getRGBs(color);
		String hex = String.format("#%02x%02x%02x", rgb[0], rgb[1], rgb[2]);
		return hex;
	}

}
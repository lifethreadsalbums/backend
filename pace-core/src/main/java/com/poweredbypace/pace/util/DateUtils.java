package com.poweredbypace.pace.util;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date addDays(Date date, int numDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, numDays);

		return calendar.getTime();
	}

	public static Date getNextBusinessDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

		if (dayOfWeek == Calendar.FRIDAY) {
			calendar.add(Calendar.DATE, 3);
		} else if (dayOfWeek == Calendar.SATURDAY) {
			calendar.add(Calendar.DATE, 2);
		} else {
			calendar.add(Calendar.DATE, 1);
		}

		return calendar.getTime();
	}

	public static String customFormat(Date date, SimpleDateFormat sdf) {
		StringBuffer strBuf = new StringBuffer();
		FieldPosition fp = new FieldPosition(DateFormat.DATE_FIELD);
		sdf.format(date, strBuf, fp);
		String dateStr = strBuf.toString();
		int begIdx = fp.getBeginIndex();
		int endIdx = fp.getEndIndex();
		String dayNbrStr = dateStr.substring(begIdx, endIdx);
		int dayNbr = Integer.valueOf(dayNbrStr);
		dayNbrStr += getOrdinalFor(dayNbr);
		return dateStr.substring(0, begIdx) + dayNbrStr
				+ dateStr.substring(endIdx);
	}

	public static String getOrdinalFor(int value) {
		int hundredRemainder = value % 100;
		if ((hundredRemainder >= 10) && (hundredRemainder <= 20))
			return "th";
		int tenRemainder = value % 10;
		switch (tenRemainder) {
		case 1:
			return "st";
		case 2:
			return "nd";
		case 3:
			return "rd";
		default:
			return "th";
		}
	}

	public static Date getFormattedDate(String date, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		Date dateFormatted = formatter.parse(date);
		return dateFormatted;
	}

}

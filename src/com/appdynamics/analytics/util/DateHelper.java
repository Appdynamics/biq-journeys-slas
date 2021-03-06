package com.appdynamics.analytics.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateHelper {

	public static Range getTimeRangeForNow(long minutes){
		ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.systemDefault());
		long end = zdtNow.toInstant().toEpochMilli();
		zdtNow = zdtNow.minusMinutes(minutes);
		long start = zdtNow.toInstant().toEpochMilli();
		return new Range(start,end);
	}

	public static Range getBaseLineTimeRange(long days){
		ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.systemDefault());
		zdtNow = zdtNow.minusDays(1);
		zdtNow = zdtNow.with(LocalTime.MAX);
		long end = zdtNow.toInstant().toEpochMilli();
		zdtNow = zdtNow.minusDays(days);
		zdtNow = zdtNow.with(LocalTime.MIN);
		long start = zdtNow.toInstant().toEpochMilli();
		return new Range(start,end);
	}
	
	
	public static Date atStartOfDay(Date date) {
	    LocalDateTime localDateTime = dateToLocalDateTime(date);
	    LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
	    return localDateTimeToDate(startOfDay);
	}

	public static Date atEndOfDay(Date date) {
	    LocalDateTime localDateTime = dateToLocalDateTime(date);
	    LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
	    return localDateTimeToDate(endOfDay);
	}

	private static LocalDateTime dateToLocalDateTime(Date date) {
	    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
	    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static long parseTime(String format, String value) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		return df.parse(value).getTime();
	}

	public static long diffTimeFromStrings(String format, String value1, String value2) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		long val1 = df.parse(value1).getTime();
		long val2 = df.parse(value2).getTime();
		
		if(val1 >= val2) {
			return val1 - val2;
		}else {
			return val2 - val1;
		}
	}
	
	public static long diffTimeFromNow(String format, String time) throws ParseException {
		DateFormat df = new SimpleDateFormat(format);
		Date date1 = df.parse(time);
		long val1 = date1.getTime();
		Date date = new Date();
		long val2 = date.getTime();
		return val2 - val1;
	}

	public static String parseDate(long millis) {
		Date d = new Date(millis);
		DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
		return df.format(d);
	}
}

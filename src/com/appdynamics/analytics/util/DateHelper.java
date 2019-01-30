package com.appdynamics.analytics.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateHelper {

	public static Range getTimeRangeForNow(){
		ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.systemDefault());
		long end = zdtNow.toInstant().toEpochMilli();
		zdtNow = zdtNow.minusDays(1);
		long start = zdtNow.toInstant().toEpochMilli();
		return new Range(start,end);
	}

	public static Range getLastTwoWeeksSinceYesterday(){
		ZonedDateTime zdtNow = ZonedDateTime.now(ZoneId.systemDefault());
		zdtNow = zdtNow.minusDays(1);
		zdtNow = zdtNow.with(LocalTime.MAX);
		long end = zdtNow.toInstant().toEpochMilli();
		zdtNow = zdtNow.minusDays(14);
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

	public static long parseTime(String value) {
		ZonedDateTime zdt = ZonedDateTime.parse(value);
		return zdt.toInstant().toEpochMilli();
	}

	public static long diffTime(String value1, String value2) {
		ZonedDateTime zdt = ZonedDateTime.parse(value1);
		long val1 = zdt.toInstant().toEpochMilli();
		zdt = ZonedDateTime.parse(value2);
		long val2 = zdt.toInstant().toEpochMilli();
		
		if(val1 >= val2) {
			return val1 - val2;
		}else {
			return val2 - val1;
		}
	}

	public static String parseDate(long millis) {
		Date d = new Date(millis);
		DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");
		return df.format(d);
	}
}

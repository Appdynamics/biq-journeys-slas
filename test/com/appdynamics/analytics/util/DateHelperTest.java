package com.appdynamics.analytics.util;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

class DateHelperTest {

	@Test
	void testParsing() throws ParseException {
		long milliseconds = DateHelper.parseTime("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'","2019-01-14T15:50:32.000Z");
		assertEquals(1547502632000l, milliseconds);
		milliseconds = DateHelper.parseTime("yyyy-MM-dd'T'hh:mm:ss.SSSZ","2019-01-31T10:35:13.786+0000");
	}

	
	@Test
	void testDifference() throws Exception {
		long diff = DateHelper.diffTimeFromStrings("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'","2019-01-14T15:50:32.000Z","2019-01-14T16:50:32.000Z");
		assertEquals(3600000, diff);
		
		//swap the values
		diff = DateHelper.diffTimeFromStrings("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'","2019-01-14T16:50:32.000Z","2019-01-14T15:50:32.000Z");
		assertEquals(3600000, diff);
		
		//crazy timestamp
		diff = DateHelper.diffTimeFromNow("yyyy-MM-dd'T'hh:mm:ss.SSSZ","2019-01-31T10:35:13.786+0000");			
	}
	
	@Test
	void testDifferenceFromNow() throws Exception {
		long diff = DateHelper.diffTimeFromNow("yyyy-MM-dd'T'hh:mm:ss.SSSZ","2019-01-31T10:35:13.786+0000");
		assertNotNull(diff);
	}
	
	@Test
	void testRange() {
		Range range = DateHelper.getTimeRangeForNow(14);
		assertNotNull(DateHelper.parseDate(range.getStart()));
		assertNotNull(DateHelper.parseDate(range.getEnd()));
	}

	
}

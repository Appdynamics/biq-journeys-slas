package com.appdynamics.analytics.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DateHelperTest {

	@Test
	void testParsing() {
		long milliseconds = DateHelper.parseTime("2019-01-14T15:50:32.000Z");
		assertEquals(1547481032000l, milliseconds);
	}

	
	@Test
	void testDifference() {
		long diff = DateHelper.diffTime("2019-01-14T15:50:32.000Z","2019-01-14T16:50:32.000Z");
		assertEquals(3600000, diff);
		
		//swap the values
		diff = DateHelper.diffTime("2019-01-14T16:50:32.000Z","2019-01-14T15:50:32.000Z");
		assertEquals(3600000, diff);
	}

	
}

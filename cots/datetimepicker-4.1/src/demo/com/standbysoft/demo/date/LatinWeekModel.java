/*
 * @(#)LatinWeekModel.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.util.Calendar;

import com.standbysoft.component.date.swing.DefaultWeekModel;
import com.standbysoft.component.date.swing.event.WeekModelEvent;

/**
 * A week model with week days names in Latin.
 */
public class LatinWeekModel extends DefaultWeekModel {
	/**
	 * The short week days names in Latin.
	 */
	private static String[] LATIN_NAMES = new String[] { "sol", "lun", "mar", "mer", "iov", "ven", "sat" };

	/**
	 * The Latin week days codes.
	 */
	private static int[] WEEK_DAYS = new int[] { Calendar.SUNDAY, Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY };

	/**
	 * The Latin week days names ordered depending on the first day of week.
	 */
	private String[] names = new String[7];

	/**
	 * The Latin week days codes ordered depending on the first day of week.
	 */
	private int[] dows = new int[7];

	public LatinWeekModel() {
		setDowFirst(Calendar.SUNDAY);
	}

	public String[] getDowNames() {
		return names;
	}

	public int[] getDows() {
		return dows;
	}

	public void setDowFirst(int first) {
		this.dowFirst = first;

		computeDows();
		fireDowFirstChanged(new WeekModelEvent(this));
	}

	private void computeDows() {
		int index = 0;

		while (WEEK_DAYS[index] != dowFirst) {
			index++;
		}

		for (int i = 0; i < 7; i++) {
			names[i] = LATIN_NAMES[(index + i) % 7];
			dows[i] = WEEK_DAYS[(index + i) % 7];
		}

		fireDowNamesChanged(new WeekModelEvent(this));
	}
}
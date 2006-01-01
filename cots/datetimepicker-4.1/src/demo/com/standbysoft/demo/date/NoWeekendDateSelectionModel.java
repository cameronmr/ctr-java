/*
 * @(#)NoWeekendDateSelectionModel.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.util.Calendar;
import java.util.Date;

import com.standbysoft.component.date.DefaultDateSelectionModel;

/**
 * A selection model that disables all weekend days. 
 */
public class NoWeekendDateSelectionModel extends DefaultDateSelectionModel {
	private static Calendar cal = Calendar.getInstance();

	public boolean isDisabled(Date date) {
		//respect the contract from the parent class
		if (super.isDisabled(date)) {
			return true;
		}
		
		cal.setTime(date);
		return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	}
}
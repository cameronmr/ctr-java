/*
 * @(#)ToggleWeekModel.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import com.standbysoft.component.date.swing.DefaultWeekModel;
import com.standbysoft.component.date.swing.event.WeekModelEvent;

/**
 * A custom week model that toggles the selection of a day of week or week if it 
 * is reselected. If a user clicks on a day of week and then clicks again, 
 * that day of week is unselected.
 */
public class ToggleWeekModel extends DefaultWeekModel {
	/**
	 * Toggles the selection of a day of week if it is reselected.
	 * 
	 * @param dow new day of week that is selected. Accepted values are: 
	 * WeekModel.NO_SELECTED_DOW, Calendar.MONDAY, Calendar.TUESDAY, etc. 
	 */
	public void setSelectedDow(int dow) {
		if (this.dow == dow) {
			this.dow = NO_SELECTED_DOW;
			fireDowChanged(new WeekModelEvent(this));
		} else {
			super.setSelectedDow(dow);
		}
	}
	
	/**
	 * Toggles the selection of a week if it is reselected.
	 * 
	 * @param year year to which the selected week belongs 
	 * @param week selected week
	 */
	public void setSelectedWeek(int year, int week) {
		if (this.week == week && this.year == year) {
			this.week = NO_SELECTED_WEEK;
			this.year = NO_SELECTED_YEAR;
			fireWeekChanged(new WeekModelEvent(this));
		} else {
			super.setSelectedWeek(year, week);
		}
	}
}
/*
 * @(#)MonthROTitleUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.BasicMonthUI;

/**
 * <p>A UI delegate for <code>JMonth</code> that customizes the month title
 * to contain just a single label with the name of the current month and year.</p>
 */
public class MonthROTitleUI extends BasicMonthUI {
	/**
	 * The label used to represent the month name and year.
	 */
	protected JLabel monthYearTitle;
	
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthROTitleUI();
	}

	/**
	 * Overriden to create a new title month component.
	 * 
	 * @return a label that will show the current month name and year.
	 */
	protected JComponent createTitleMonth() {
		monthYearTitle = new JLabel("", SwingConstants.CENTER);
		
		return monthYearTitle;
	}
	
	/**
	 * Updates the text from the title month label to reflect the state of the
	 * <code>JMonth</code> component.
	 */
	protected void updateTitle() {
		int m = month.getMonth();
		int y = month.getYear();
		
		monthYearTitle.setText(month.getMonthModel().getMonthNames()[m] + " " + y);
	}
}
/*
 * @(#)DatePickerCustomCalendarUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.JMonthView;
import com.standbysoft.component.date.swing.plaf.DateUI;
import com.standbysoft.component.date.swing.plaf.basic.BasicDatePickerUI;

/**
 * <p>A UI delegate for <code>JDatePicker</code> that overrides the method that
 * creates its drop down calendar to change its appearance.</p>
 */
public class DatePickerCustomCalendarUI extends BasicDatePickerUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new DatePickerCustomCalendarUI();
	}
	
	protected JMonthView createMonthView() {
		JMonthView mv = super.createMonthView();
		
		//change the appearance of a month from the calendar (JMonthView)
		mv.putClientProperty("JMonthView.monthUI", MonthROTitleUI.class.getName());
		//change the appearance of the calendar (JMonthView)
		mv.setUI((DateUI)MonthViewYearButtonsUI.createUI(monthView));
		mv.invalidate();
		
		return mv;
	}
}
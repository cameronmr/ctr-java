/*
 * @(#)TrailingDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders trailing dates.
 */
public class TrailingDateRenderer extends RegularDateRenderer {
	private static Calendar calendar = Calendar.getInstance();
	
	/**
	 * Returns a component that renders a trailing date or <code>null</code> if the
	 * date is not a trailing date.
	 */
	public Component getDateRendererComponent(JMonth month, Date date) {
		calendar.setTime(date);

		if ((calendar.get(Calendar.YEAR) != month.getYear()) || (calendar.get(Calendar.MONTH) != month.getMonth())) {
			setValue(date);
			setFont(month.getFont());
			setForeground(month.getTrailingForeground());
			
			return this;
		}
		
		return null;
	}
}
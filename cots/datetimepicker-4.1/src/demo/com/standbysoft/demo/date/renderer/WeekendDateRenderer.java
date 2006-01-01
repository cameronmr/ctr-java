/*
 * @(#)WeekendDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Calendar;
import java.util.Date;

import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders weekend dates using a red foreground.
 */
public class WeekendDateRenderer extends RegularDateRenderer {
	private Calendar calendar = Calendar.getInstance();
	
	public Component getDateRendererComponent(JMonth month, Date date) {
		calendar.setTime(date);
		
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			setForeground(Color.red);
			setFont(month.getFont());
			setValue(date);
			
			return this;
		}
		
		return null;
	}
}
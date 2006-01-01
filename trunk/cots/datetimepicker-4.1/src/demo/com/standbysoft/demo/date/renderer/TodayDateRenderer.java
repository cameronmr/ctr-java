/*
 * @(#)TodayDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders the today date.
 */
public class TodayDateRenderer extends RegularDateRenderer {
	private static Calendar todayCalendar = Calendar.getInstance();
	private static Calendar calendar = Calendar.getInstance();
	
	private Color color;

	public TodayDateRenderer() {
		this(Color.red);
	}
	
	public TodayDateRenderer(Color color) {
		this.color = color;
	}
	
	/**
	 * Returns a component that renders the today date or <code>null</code> if the
	 * date not the today date.
	 */
	public Component getDateRendererComponent(JMonth month, Date date) {
		calendar.setTime(date);

		if (month.getMonthModel().isToday(date)) {
			setValue(date);
			return this;
		}
		
		return null;
	}
	
	/**
	 * Customizes the painting for the today date.
	 */
	public void paint(Graphics g) {
		g.setColor(color);
		g.drawOval(1, 1, getWidth() - 2, getHeight() - 2);
	}
}
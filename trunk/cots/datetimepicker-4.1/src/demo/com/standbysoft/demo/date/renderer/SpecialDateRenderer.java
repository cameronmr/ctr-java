/*
 * @(#)SpecialDateRenderer.java
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
 * Renders all dates that end in one (1, 11, 21, 31) as special dates.
 */
public class SpecialDateRenderer extends RegularDateRenderer {
	private Calendar calendar = Calendar.getInstance();
	
	public Component getDateRendererComponent(JMonth month, Date date) {
		calendar.setTime(date);

		if (calendar.get(Calendar.DATE) % 10 == 1) {
			setForeground(Color.red);
			setFont(month.getFont());
			setValue(date);
			
			return this;
		}
		
		return null;
	}
	
	/**
	 * Customizes the painting for the special date.
	 */
	public void paint(Graphics g) {
		g.setColor(Color.orange);
		g.fillOval(1, 1, getWidth() - 1, getHeight() - 1);
		g.setColor(Color.red);
		g.drawString("*", getWidth() - 7, getHeight());
		super.paint(g);
	}
}
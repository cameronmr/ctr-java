/*
 * @(#)RegularDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

import com.standbysoft.component.date.swing.DateRenderer;
import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders all dates in a standard way. Paints their value using some default
 * colors.
 */
public class RegularDateRenderer extends JLabel implements DateRenderer {
	public RegularDateRenderer() {
		//use this font to paint the dates. it will be used to override the
		//default font. see paint method.
//		setFont(new Font("Arial", Font.PLAIN, 12));
	}
	
	public void setValue(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		setText(cal.get(Calendar.DATE) + "");
	}
	
	public Component getDateRendererComponent(JMonth month, Date date) {
		setForeground(month.getForeground());
		setBackground(month.getMonthBackground());
		setFont(month.getFont());
		setValue(date);
		
		return this;
	}
	
	public void paint(Graphics g) {
		Color oldColor = g.getColor();

		//use current font to override the font of the graphics context
		g.setColor(getForeground());
		
		FontMetrics fm = g.getFontMetrics(g.getFont());
		int sw = fm.stringWidth(getText());
		g.drawString(getText(), (getWidth() - sw) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());

		g.setColor(oldColor);
	}
}
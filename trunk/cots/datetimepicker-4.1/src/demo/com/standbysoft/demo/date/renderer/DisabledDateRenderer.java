/*
 * @(#)DisabledDateRenderer.java
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
import java.util.Date;

import javax.swing.JLabel;

import com.standbysoft.component.date.swing.DateRenderer;
import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders disabled dates.
 */
public class DisabledDateRenderer extends JLabel implements DateRenderer {
	private Color color;
	
	public DisabledDateRenderer() {
		this(Color.red);
	}
	
	public DisabledDateRenderer(Color color) {
		this.color = color;
	}
	
	/**
	 * Returns a component that renders a disabled date or <code>null</code> if the
	 * date is not a disabled date.
	 */
	public Component getDateRendererComponent(JMonth month, Date date) {
		if (!month.getDateSelectionModel().isDateSelectable(date)) {
			return this;
		}
		
		return null;
	}
	
	/**
	 * Customizes the painting for a disabled date.
	 */
	public void paint(Graphics g) {
		g.setColor(color);
		g.drawLine(3, 3, getWidth() - 3, getHeight() - 3);
		g.drawLine(getWidth() - 3, 3, 3, getHeight() - 3);
	}
}
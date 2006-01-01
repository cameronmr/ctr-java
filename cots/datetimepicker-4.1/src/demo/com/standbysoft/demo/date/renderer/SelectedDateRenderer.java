/*
 * @(#)SelectedDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Component;
import java.awt.Graphics;
import java.util.Date;

import com.standbysoft.component.date.swing.JMonth;

/**
 * Renders selected dates.
 */
public class SelectedDateRenderer extends RegularDateRenderer {
	/**
	 * Indicates whether the selected date is painted as a oval or a circle. 
	 * When true, it is painted as a square. Otherwise, it is painted as an oval.
	 */
	private boolean isSquare;
	
	public SelectedDateRenderer() {
		this(false);
	}
	
	public SelectedDateRenderer(boolean isSquare) {
		this.isSquare = isSquare;
	}
	
	/**
	 * Returns a component that renders a selected date or <code>null</code> if the
	 * date is not a selected date.
	 */
	public Component getDateRendererComponent(JMonth month, Date date) {
		if (month.getDateSelectionModel().isDateSelected(date)) {
			setValue(date);
			setBackground(month.getTitleBackground());
			setForeground(month.getTitleForeground());
			
			return this;
		}
		
		return null;
	}
	
	/**
	 * Customizes the painting for the selected dates.
	 */
	public void paint(Graphics g) {
		g.setColor(getBackground());
		if (isSquare) {
			g.fillRect(1, 1, getWidth() - 1, getHeight() - 1);
		} else {
			g.fillOval(1, 1, getWidth() - 1, getHeight() - 1);
		}
		// need to paint the actual text over the oval background
		super.paint(g); 
	}
}
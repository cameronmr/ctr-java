/*
 * @(#)CompositeDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.renderer;

import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;

import com.standbysoft.component.date.swing.DateRenderer;
import com.standbysoft.component.date.swing.JMonth;

/**
 * <p>A composite date renderer uses several other renderers to display a date. 
 * A certain date could represent several aspects like: disabled and trailing 
 * and today date. </p>
 * 
 * <p>This renderer uses a list of renderers that deal only with a specific date 
 * aspect. For instance the <code>TodayDateRenderer</code> draws a red circle 
 * around the today date.</p>
 */
public class CompositeDateRenderer extends JLabel implements DateRenderer {
	protected List renderers;
	private Date date;
	private JMonth month;

	/**
	 * Creates a composite date renderer with no registered renderer. You need
	 * to register your own date renderers, in order for this object to be 
	 * functional.
	 */
	public CompositeDateRenderer() {
		setOpaque(false);
		renderers = new ArrayList();
	}
		
	/**
	 * Returns this component as the component that renders the specified date. The
	 * {@link #paint(Graphics)} method is used to paint the date appropriately.
	 */
	public Component getDateRendererComponent(JMonth month, Date date) {
		//keep the state of this renderer consistent for the paint method
		this.date = date;
		this.month = month;

		if (month.getMonthModel().isTrailingNext(date)) {
			if (month.isTrailingNextEnabled()) {
				setForeground(month.getTrailingForeground());
			} else {
				setForeground(month.getMonthBackground());
			}
		} else if (month.getMonthModel().isTrailingPrevious(date)) {
			if (month.isTrailingPreviousEnabled()) {
				setForeground(month.getTrailingForeground());
			} else {
				setForeground(month.getMonthBackground());
			}
		} else {
			setForeground(month.getForeground());
		}
		
		return this;
	}

	/**
	 * The painting of the date is handled here. Every registered renderer is used
	 * to render a certain aspect of the current date.
	 */	
	public void paint(Graphics g) {
		for (Iterator i = renderers.iterator(); i.hasNext();) {
			DateRenderer r = (DateRenderer)i.next();
			Component c = r.getDateRendererComponent(month, date);
			
			// if the renderer can render for the specified context (month, date)
			// then use the same graphic context to draw whatever is needed
			if ((month.getMonthModel().isTrailingNext(date) && month.isTrailingNextEnabled()) ||
					(month.getMonthModel().isTrailingPrevious(date) && month.isTrailingPreviousEnabled()) ||
					(!month.getMonthModel().isTrailingPrevious(date) && !month.getMonthModel().isTrailingNext(date))) {
				if (c != null) {
					c.setSize(getSize());
					c.paint(g);
				}
			}
		}
	}
}
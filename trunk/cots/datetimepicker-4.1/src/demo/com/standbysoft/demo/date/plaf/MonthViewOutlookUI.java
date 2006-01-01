/*
 * @(#)MonthViewOutlookUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.FontUIResource;

import com.standbysoft.component.date.swing.plaf.basic.AbstractMonthViewUI;
import com.standbysoft.component.date.swing.plaf.basic.DateRendererUIResource;
import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthViewUI;
import com.standbysoft.demo.date.CustomDateRenderer;

/**
 * <p>A UI delegate for <code>JMonthView</code> that simulates the Outlook
 * style.</p>
 * 
 * <p>This is an advanced UI delegate because it creates its own today button 
 * and layout. The layout is used to change how the today button is positioned.</p>
 */
public class MonthViewOutlookUI extends DefaultMonthViewUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthViewOutlookUI();
	}
	
	protected Object[] createDefaults() {
		Object[] defaults =
		      new Object[] {
		        "MonthView.font",
		        new FontUIResource("Arial", Font.PLAIN, 12),
		        "MonthView.gridColor",
		        new ColorUIResource(Color.WHITE),
		        "MonthView.titleBackground",
		        new ColorUIResource(184, 219, 195),
		        "MonthView.titleForeground",
		        new ColorUIResource(Color.BLACK),
		        "MonthView.foreground",
		        new ColorUIResource(Color.BLACK),
		        "MonthView.trailingForeground",
		        new ColorUIResource(Color.GRAY),
		        "MonthView.monthBackground",
		        new ColorUIResource(Color.WHITE),
		        "MonthView.dateRenderer",
		        new DateRendererUIResource(new CustomDateRenderer())
		    };
		
		return defaults;
	}
	
	/**
	 * Overridden to specify that this UI delegate does not have a None button.
	 * @return <code>null</code>
	 */
	protected JComponent createNoneComponent() {
		return null;
	}
	
	protected JComponent createTodayComponent() {
		return new JButton("Today") {
			public boolean isFocusTraversable() {
				return false;
			}
		};
	}

	/**
	 * Overridden to configure the color for the today button.
	 */
	protected void updateTitleBackground() {
		super.updateTitleBackground();
		
		if (todayComponent != null) {
			todayComponent.setBackground(monthView.getTitleBackground());
			todayComponent.setBorder(BorderFactory.createLineBorder(monthView.getTitleBackground().darker()));
		}
	}
	
	public Dimension getPreferredSize(JComponent c) {
		return new Dimension(165, 200);
	}
	
	protected LayoutManager createLayout() {
		return new MyCalendarLayout();
	}
	
	private static class MyCalendarLayout extends AbstractMonthViewUI.CalendarLayout {
		protected void layoutNoneAndTodayComponentsInContainer(Container parent) {
			Insets insets = parent.getInsets();
			
			Dimension parentSize = new Dimension(parent.getSize().width - insets.left - insets.right, parent.getSize().height - insets.bottom - insets.top);
			Dimension todaySize = new Dimension(70, (int)(parentSize.getHeight() * 0.11));
			
			if (todayLabel != null) {
				todayLabel.setBounds((int)(parentSize.getWidth() - todaySize.getWidth())/2, parentSize.height - todaySize.height, todaySize.width, todaySize.height);
			}
		}
	}
}
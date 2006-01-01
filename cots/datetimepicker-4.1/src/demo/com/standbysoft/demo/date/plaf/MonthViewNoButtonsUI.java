/*
 * @(#)MonthViewNoButtonsUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthViewUI;

/**
 * <p>A UI delegate for <code>JMonthView</code> that eliminates the scroll buttons.</p>
 */
public class MonthViewNoButtonsUI extends DefaultMonthViewUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthViewNoButtonsUI();
	}

	/**
	 * Returns <code>null</code> to indicate that no button is used.
	 * 
	 * @return <code>null</code> to indicate that no button is used.
	 */
	protected JButton createPreviousMonthButton() {
		return null;
	}

	/**
	 * Returns <code>null</code> to indicate that no button is used.
	 * 
	 * @return <code>null</code> to indicate that no button is used.
	 */
	protected JButton createNextMonthButton() {
		return null;
	}
}
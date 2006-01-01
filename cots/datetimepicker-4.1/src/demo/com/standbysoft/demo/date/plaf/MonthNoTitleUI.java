/*
 * @(#)MonthNoTitleUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthUI;

/**
 * <p>A UI delegate for <code>JMonth</code> that has no title.</p>
 */
public class MonthNoTitleUI extends DefaultMonthUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthNoTitleUI();
	}
	
	/**
	 * Returns <code>null</code> to indicate that no title is used.
	 * 
	 * @return <code>null</code> to indicate that no title is used.
	 */
	protected JComponent createTitle() {
		return null;
	}
}
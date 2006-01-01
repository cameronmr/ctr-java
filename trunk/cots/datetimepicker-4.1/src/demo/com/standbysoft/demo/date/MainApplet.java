/*
 * @(#)MainApplet.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.applet.Applet;

import javax.swing.UIManager;

import com.standbysoft.component.date.swing.plaf.basic.BasicDateFieldUI;
import com.standbysoft.component.date.swing.plaf.basic.BasicDatePickerUI;
import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthUI;
import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthViewUI;

/**
 * The entry point for the Java Date Picker applet demo application.
 */
public class MainApplet extends Applet {
	public void init() {
		UIManager.put("MonthUI", DefaultMonthUI.class.getName());
		UIManager.put("MonthViewUI", DefaultMonthViewUI.class.getName());
		UIManager.put("DatePickerUI", BasicDatePickerUI.class.getName());
		UIManager.put("DateFieldUI", BasicDateFieldUI.class.getName());
		
		DemoApp.start(this, false);
	}
}
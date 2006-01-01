/*
 * @(#)MonthNewLayoutUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthUI;

/**
 * <p>A UI delegate for <code>JMonth</code> that uses a different title layout
 * in order to minimize the space it occupies.</p>
 */
public class MonthNewLayoutUI extends DefaultMonthUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthNewLayoutUI();
	}

	protected LayoutManager createLayout() {
		return new TightLayoutManager();
	}

	protected LayoutManager createTitleLayout() {
		return new TightTitleLayoutManager();
	}
}

class TightLayoutManager extends GridBagLayout {
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints.equals("Title")) {
			super.addLayoutComponent(comp,
					new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.HORIZONTAL,
						new Insets(0, 0, 0, 0),0,0));
		}
		if (constraints.equals("Month")) {
			super.addLayoutComponent(comp,
					new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0),0,0));
		}
	}
};

class TightTitleLayoutManager extends GridBagLayout {
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints.equals("TitleMonth")) {
			super.addLayoutComponent(comp, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(2, 5, 2, 1), 0, 0));
		} else if (constraints.equals("TitleYear")) {
			super.addLayoutComponent(comp, new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(2, 1, 2, 5), 0, 0));
		} else if (constraints.equals("TitleLeft")) {
			super.addLayoutComponent(comp, new GridBagConstraints(0, 0, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));
		} else if (constraints.equals("TitleRight")) {
			super.addLayoutComponent(comp, new GridBagConstraints(3, 0, 1, 1, 2.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 0, 2, 0), 0, 0));
		}
	}
};
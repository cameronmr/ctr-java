/*
 * @(#)MonthViewYearButtonsUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthViewUI;

/**
 * <p>A UI delegate for <code>JMonthView</code> with image month and year 
 * scrolling buttons.</p>
 */
public class MonthViewYearButtonsUI extends DefaultMonthViewUI {
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthViewYearButtonsUI();
	}
	
	protected JButton createPreviousMonthButton() {
		JButton button = new ScrollButton(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepBack16.gif")));
		return button; 
	}

	protected JButton createNextMonthButton() {
		JButton button = new ScrollButton(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepForward16.gif")));
		return button;
	}
	
	protected JButton createPreviousYearButton() {
		JButton button = new ScrollButton(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Rewind16.gif")));
		return button;
	}
	
	protected JButton createNextYearButton() {
		JButton button = new ScrollButton(new ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/FastForward16.gif")));
		return button;
	}
}

/**
 * A custom button with icon.
 */
class ScrollButton extends JButton {
	public ScrollButton(Icon icon) {
		super(icon);
		setContentAreaFilled(false);
		setBorder(new EmptyBorder(new Insets(0, 0, 0, 0)));
	}
	
	/**
	 * Overridden in order to disallow this button to get focus.
	 * @return false to restrict focus
	 */ 
	public boolean isFocusTraversable() {
		return false;
	}
}
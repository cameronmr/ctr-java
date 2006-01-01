/*
 * @(#)MonthWindowsDateTimeUI.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.BasicMonthUI;

/**
 * <p>A UI delegate for <code>JMonth</code> that uses a combo box and a spinner 
 * in the title to select the month and year.</p>
 */
public class MonthWindowsDateTimeUI extends BasicMonthUI {
	protected JComboBox monthTitleComboBox;
	
	protected JSpinner yearTitleSpinner;
	
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthWindowsDateTimeUI();
	}

	protected LayoutManager createTitleLayout() {
		return new TitleLayoutManager();
	}
	
	protected LayoutManager createLayout() {
		return new DefaultLayoutManager();
	}
	
	/**
	 * Overridden to create a new title month component.
	 * 
	 * @return a combo box with all the months of the year.
	 */
	protected JComponent createTitleMonth() {
		monthTitleComboBox = new JComboBox(new Integer[]{
				new Integer(Calendar.JANUARY), new Integer(Calendar.FEBRUARY), new Integer(Calendar.MARCH), 
				new Integer(Calendar.APRIL), new Integer(Calendar.MAY), new Integer(Calendar.JUNE), 
				new Integer(Calendar.JULY), new Integer(Calendar.AUGUST), new Integer(Calendar.SEPTEMBER), 
				new Integer(Calendar.OCTOBER), new Integer(Calendar.NOVEMBER), new Integer(Calendar.DECEMBER)
				}) {
			
			public void setFont(Font font) {
				super.setFont(font);
				super.setFont(getFont().deriveFont(Font.PLAIN));
			}
		};
		
		monthTitleComboBox.setMaximumRowCount(12);

		//make sure the combo displays the name of the month correctly
		monthTitleComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				int m = ((Integer)value).intValue();
				label.setText(month.getMonthModel().getMonthNames()[m]);
				
				return label;
			}
		});
		
	    //the combo controls the displayed month
		monthTitleComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				month.setMonth(((Integer)monthTitleComboBox.getSelectedItem()).intValue());
			}
		});
		
		return monthTitleComboBox;
	}
	
	protected JComponent createBody() {
		JComponent body = super.createBody();
		body.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		return body;
	}

	/**
	 * Overridden to create a new title year component.
	 * 
	 * @return spinner title year component.
	 */
	protected JComponent createTitleYear() {
		yearTitleSpinner = new JSpinner(new SpinnerNumberModel(month.getYear(), 0, 9999, 1));
		yearTitleSpinner.setEditor(new JSpinner.NumberEditor(yearTitleSpinner, "#"));

	    //the spinner controls the displayed year
		yearTitleSpinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				Integer value = (Integer)yearTitleSpinner.getValue();
				month.setYear(value.intValue());
			}
		});
		
		return yearTitleSpinner;
	}
	
	/**
	 * Updates the text from the title month label to reflect the state of the
	 * <code>JMonth</code> component.
	 */
	protected void updateTitle() {
		int m = month.getMonth();
		int y = month.getYear();

		monthTitleComboBox.setSelectedItem(new Integer(m));
		yearTitleSpinner.setValue(new Integer(y));
	}
	
	protected void updateTitleForeground() {
		Color cm = monthTitleComboBox.getForeground();
		Color cy = yearTitleSpinner.getForeground();
		super.updateTitleForeground();
		monthTitleComboBox.setForeground(cm);
		yearTitleSpinner.setForeground(cy);
	}

	/**
	 * Empty implementation because the background must remain untouched to 
	 * resemble to Windows Date/Time component.
	 */
	protected void updateTitleBackground() {
	}
}

class TitleLayoutManager extends GridBagLayout {
	public void addLayoutComponent(Component comp, Object constraints) {
		if (constraints.equals("TitleMonth")) {
			super.addLayoutComponent(comp, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 5), 0, 0));
		} else if (constraints.equals("TitleYear")) {
			super.addLayoutComponent(comp, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 10, 0), 0, 0));
		}
	}
};

class DefaultLayoutManager extends GridBagLayout {
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
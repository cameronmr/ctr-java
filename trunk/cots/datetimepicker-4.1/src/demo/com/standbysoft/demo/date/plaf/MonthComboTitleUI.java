/*
 * @(#)ComboBoxTitleMonthUII.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.plaf.ComponentUI;

import com.standbysoft.component.date.swing.plaf.basic.BasicMonthUI;

/**
 * <p>A UI delegate for <code>JMonth</code> that uses combo boxes in the title
 * to select the month and year.</p>
 */
public class MonthComboTitleUI extends BasicMonthUI {
	protected JComboBox monthTitleComboBox;
	
	protected JComboBox yearTitleComboBox;
	
	/**
	 * Factory method that creates the actual UI delegate.
	 */
	public static ComponentUI createUI(JComponent c) {
		return new MonthComboTitleUI();
	}

	/**
	 * Overriden to create a new title month component.
	 * 
	 * @return a combo box with all the months of the year.
	 */
	protected JComponent createTitleMonth() {
		monthTitleComboBox = new JComboBox(new Integer[]{
				new Integer(Calendar.JANUARY), new Integer(Calendar.FEBRUARY), new Integer(Calendar.MARCH), 
				new Integer(Calendar.APRIL), new Integer(Calendar.MAY), new Integer(Calendar.JUNE), 
				new Integer(Calendar.JULY), new Integer(Calendar.AUGUST), new Integer(Calendar.SEPTEMBER), 
				new Integer(Calendar.OCTOBER), new Integer(Calendar.NOVEMBER), new Integer(Calendar.DECEMBER)
				});
		
		monthTitleComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				
				int m = ((Integer)value).intValue();
				label.setText(month.getMonthModel().getMonthNames()[m]);
				
				return label;
			}
		});
		
		monthTitleComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				month.setMonth(((Integer)monthTitleComboBox.getSelectedItem()).intValue());
			}
		});
		
		monthTitleComboBox.setSelectedItem(new Integer(month.getMonth()));
		monthTitleComboBox.setMaximumRowCount(12);
		
		return monthTitleComboBox;
	}

	/**
	 * Overridden to create a new title year component.
	 * 
	 * @return 
	 */
	protected JComponent createTitleYear() {
		yearTitleComboBox = new JComboBox();
		yearTitleComboBox.setEditable(true);
		yearTitleComboBox.setPreferredSize(new Dimension(50, yearTitleComboBox.getPreferredSize().height));
		yearTitleComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				Integer value = (Integer)yearTitleComboBox.getSelectedItem();
				month.setYear(value.intValue());
			}
		});

		yearTitleComboBox.setModel(new YearComboBoxModel(10));
		yearTitleComboBox.setMaximumRowCount(10);
		yearTitleComboBox.setSelectedItem(new Integer(month.getYear()));
		((JTextField)yearTitleComboBox.getEditor().getEditorComponent()).setColumns(5);
		
		return yearTitleComboBox;
	}
	
	/**
	 * Updates the text from the title month label to reflect the state of the
	 * <code>JMonth</code> component.
	 */
	protected void updateTitle() {
		int m = month.getMonth();
		int y = month.getYear();

		monthTitleComboBox.setSelectedItem(new Integer(m));
		yearTitleComboBox.setSelectedItem(new Integer(y));
	}
	
	protected void updateTitleForeground() {
		Color cm = monthTitleComboBox.getForeground();
		Color cy = yearTitleComboBox.getForeground();
		super.updateTitleForeground();
		monthTitleComboBox.setForeground(cm);
		yearTitleComboBox.setForeground(cy);
	}

	/**
	 * The combo box model used to keep the years.
	 */
	private class YearComboBoxModel extends AbstractListModel implements ComboBoxModel {
		/**
		 * Current selected year.
		 */
		private Integer currentYear;
		
		/**
		 * All the years represented here.
		 */
		private List years;
		
		/**
		 * Number of years represented here.
		 */
		private int size;
		
		
		public YearComboBoxModel(int size) {
			years = new ArrayList();
			this.size = size;
			
			currentYear = new Integer(Calendar.getInstance().get(Calendar.YEAR));
			update();
		}
		
		private void update() {
			years.clear();
			for (int i = -(size / 2); i < (size / 2); i++) {
				years.add(new Integer(currentYear.intValue() + i));
			}
		}

		public Object getSelectedItem() {
			return currentYear;
		}

		public void setSelectedItem(Object anItem) {
			if (anItem == null || !(anItem instanceof Integer)) {
				return;
			}
			
			currentYear = (Integer)anItem;
			update();
		    fireContentsChanged(this, -1, -1);
		}

		public int getSize() {
			return years.size();
		}

		public Object getElementAt(int index) {
			return years.get(index);
		}
	}
}
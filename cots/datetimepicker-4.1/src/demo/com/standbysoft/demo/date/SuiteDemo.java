/*
 * @(#)SuiteDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.standbysoft.component.date.DefaultDateSelectionModel;
import com.standbysoft.component.date.swing.JDateField;
import com.standbysoft.component.date.swing.JDatePicker;
import com.standbysoft.component.date.swing.JMonthView;

/**
 * Shows all the components of the suite in an intelligible manner.
 */
public class SuiteDemo extends JPanel {
	private JMonthView calendar;
	private JLabel fromLabel;
	private JDatePicker fromDatePicker;
	private JLabel toLabel;
	private JDatePicker toDatePicker;
	private JLabel executeLabel;
	private JDateField executeDateField;
	
	public SuiteDemo() {		
		calendar = new JMonthView();
		
		DefaultDateSelectionModel dsm = new DefaultDateSelectionModel();
		dsm.addDisabled(new GregorianCalendar(2006, Calendar.NOVEMBER, 7).getTime());
		dsm.addDisabled(new GregorianCalendar(2006, Calendar.NOVEMBER, 10).getTime());
		dsm.addDisabled(new GregorianCalendar(2006, Calendar.NOVEMBER, 22).getTime());
		dsm.addDisabled(new GregorianCalendar(2006, Calendar.DECEMBER, 6).getTime());
		dsm.addDisabled(new GregorianCalendar(2006, Calendar.DECEMBER, 23).getTime());
		dsm.addDisabled(new GregorianCalendar(2007, Calendar.JANUARY, 4).getTime());
		
		calendar.setDateRenderer(new CustomWeekendDateRenderer());		
		calendar.setDateSelectionModel(dsm);
		calendar.setMonth(Calendar.OCTOBER);
		calendar.setYear(2006);
		
		calendar.setStatusVisible(false);
		calendar.setFont(new Font("Default", Font.PLAIN, 11));
		calendar.setDowFirst(Calendar.MONDAY);
		calendar.setColumns(2);
		calendar.setRows(2);
		calendar.setForeground(new Color(50, 50, 50));
		calendar.setTitleBackground(new Color(41, 71, 172));
		calendar.setSize(300, 60);
		
		fromLabel = new JLabel("Valid from:");
		fromDatePicker = new JDatePicker();
		
		toLabel = new JLabel("Until:");
		toDatePicker = new JDatePicker();
		toDatePicker.setEmptySelectionText("Always");
		toDatePicker.setCalendarTitleBackground(new Color(255, 170, 0));
		toDatePicker.setCalendarBackground(new Color(255, 231, 186));
		
		executeLabel = new JLabel("Launch at:");
		executeDateField = new JDateField();
		executeDateField.setCustomDateFormat("h:mm a");
		executeDateField.setHorizontalAlignment(JDateField.TRAILING);
		
		setLayout(new GridBagLayout());
		add(calendar, new GridBagConstraints(2, 0, 1, 4, 200.0, 40.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(executeLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 2, 0, 2), 0, 0));
		add(executeDateField, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(fromLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 2, 0, 2), 0, 0));
		add(fromDatePicker, new GridBagConstraints(1, 0, 1, 1, 300.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(toLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 2, 0, 2), 0, 0));
		add(toDatePicker, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(10, 5, 0, 5), 0, 0));
		add(new JLabel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Suite Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		SuiteDemo newContentPane = new SuiteDemo();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
		}
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
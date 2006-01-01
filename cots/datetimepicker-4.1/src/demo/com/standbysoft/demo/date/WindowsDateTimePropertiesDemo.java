/*
 * @(#)WindowsDateTimePropertiesDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.standbysoft.component.date.swing.JDateField;
import com.standbysoft.component.date.swing.JMonthView;
import com.standbysoft.demo.date.plaf.MonthViewNoButtonsUI;
import com.standbysoft.demo.date.plaf.MonthWindowsDateTimeUI;

/**
 * Reproduces in part the Date/Time Properties dialog from Windows. The purpose
 * of this demo is to show that the calendar (JMonthView) can be customized to 
 * take on any appearance by changing its UI delegate.
 */
public class WindowsDateTimePropertiesDemo extends JTabbedPane {
	public WindowsDateTimePropertiesDemo() {
		add("Date & Time", createDateTimePane());
		add("Time Zone", createTimeZonePane());
	}

	private JPanel createDateTimePane() {
		JPanel panel = new JPanel();

		JPanel datePanel = new JPanel();
		datePanel.setBorder(BorderFactory.createTitledBorder("Date"));

		UIManager.put("MonthViewUI", MonthViewNoButtonsUI.class.getName());
		UIManager.put("MonthUI", MonthWindowsDateTimeUI.class.getName());
		
		JMonthView monthView = new JMonthView();
		monthView.setFont(monthView.getFont().deriveFont(11.0f));
		monthView.setDateRenderer(new CustomDateRenderer());
		monthView.setWeekNumbersVisible(false);
		monthView.setStatusVisible(false);
		monthView.setBorder(BorderFactory.createEmptyBorder());
		monthView.setTrailingNextEnabled(false);
		monthView.setTrailingPreviousEnabled(false);
		
		datePanel.setLayout(new GridBagLayout());
		datePanel.add(monthView, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		JPanel timePanel = new JPanel();
		JDateField dateField = new JDateField();
		dateField.setCustomDateFormat("hh:mm:ss a");
		dateField.setHorizontalAlignment(SwingConstants.RIGHT);
		
		timePanel.setBorder(BorderFactory.createTitledBorder("Time"));
		timePanel.setLayout(new GridBagLayout());
		timePanel.add(new JLabel("Windows Clock"), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		timePanel.add(dateField, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		
		panel.setLayout(new GridBagLayout());
		panel.add(datePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		panel.add(timePanel, new GridBagConstraints(1, 0, 1, 1, 2.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		panel.add(new JLabel("Current time zone: Greenwich Standard"), new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 9, 5, 5), 0, 0));
		
		return panel;
	}
	
	private JPanel createTimeZonePane() {	
		JPanel timezonePanel = new JPanel();
		timezonePanel.add(new JLabel());
		
		return timezonePanel;
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("Date/Time Properties Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		WindowsDateTimePropertiesDemo newContentPane = new WindowsDateTimePropertiesDemo();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);	
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
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
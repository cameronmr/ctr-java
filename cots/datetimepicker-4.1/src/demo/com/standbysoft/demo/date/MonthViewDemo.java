/*
 * @(#)MonthViewDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.standbysoft.component.date.swing.JMonthView;
import com.standbysoft.component.date.swing.plaf.DateUI;
import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthUI;
import com.standbysoft.component.date.swing.plaf.basic.DefaultMonthViewUI;
import com.standbysoft.demo.date.plaf.MonthViewNoButtonsUI;
import com.standbysoft.demo.date.plaf.MonthROTitleUI;
import com.standbysoft.demo.date.plaf.MonthViewYearButtonsUI;
import com.standbysoft.demo.date.plaf.MonthWindowsDateTimeUI;

/**
 * Shows the operations that can be performed on a <code>JMonthView</code> component.
 */
public class MonthViewDemo extends JPanel {
	/**
	 * JMonthView component used to show the features.
	 */
	private JMonthView monthView;

	public MonthViewDemo() {
		monthView = createMonthView();

		setLayout(new GridBagLayout());
		add(createGeneralPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(8, 5, 0, 5), 0, 0));
		add(createGridPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(8, 5, 0, 5), 0, 0));
		add(monthView, new GridBagConstraints(0, 1, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(10, 5, 0, 5), 0, 0));
		add(new JLabel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Creates the month view component used by this demo.
	 * 
	 * @return month view component used by this demo.
	 */
	private JMonthView createMonthView() {
		JMonthView monthView = new JMonthView() {
			/**
			 * Resets the UI delegate for the internal JMonth components of the 
			 * JMonthView. This method override is particular for this demo; you
			 * don't have to do the same for a usual application.
			 */
			public void updateUI() {
				putClientProperty("JMonthView.monthUI", null);
				super.updateUI();
			}
		};
		monthView.setColumns(2);
		monthView.setStatusVisible(false);
		
		return monthView;
	}
	
	/**
	 * Creates a panel that controls JMonthView specific features.
	 * 
	 * @return actual panel
	 */
	private JComponent createGeneralPanel() {
		JPanel generalPanel = new JPanel();
		generalPanel.setBorder(new TitledBorder("General"));
		generalPanel.setLayout(new GridBagLayout());
		
		JLabel localeLabel = new JLabel("Locale:");
		localeLabel.setDisplayedMnemonic('l');
		
		final JComboBox localeComboBox = new JComboBox(Locale.getAvailableLocales());
		localeLabel.setLabelFor(localeComboBox);

		localeComboBox.setSelectedItem(monthView.getLocale());
		localeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Locale locale = (Locale) localeComboBox.getSelectedItem();
					monthView.setLocale(locale);
				}
			}
		});
		localeComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				Locale locale = (Locale) value;
				l.setText(locale.getDisplayName());

				return l;
			}
		});
		
		final JCheckBox displayTodayCheckBox = new JCheckBox("Display Today Button");
		displayTodayCheckBox.setMnemonic('t');
		displayTodayCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				monthView.setDisplayToday(displayTodayCheckBox.isSelected());
			}
		});
		displayTodayCheckBox.setSelected(monthView.isDisplayToday());
		
		final JCheckBox displayNoneCheckBox = new JCheckBox("Display None Button");
		displayNoneCheckBox.setMnemonic('n');
		displayNoneCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				monthView.setNoneButtonVisible(displayNoneCheckBox.isSelected());
			}
		});
		displayNoneCheckBox.setSelected(monthView.isNoneButtonVisible());
		
		final JCheckBox showStatusBar = new JCheckBox("Show Status Bar");
		showStatusBar.setMnemonic('s');
		showStatusBar.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				monthView.setStatusVisible(showStatusBar.isSelected());
				displayTodayCheckBox.setEnabled(showStatusBar.isSelected());
				displayNoneCheckBox.setEnabled(showStatusBar.isSelected());
			}
		});
		showStatusBar.setSelected(monthView.isStatusVisible());
		displayTodayCheckBox.setEnabled(showStatusBar.isSelected());
		displayNoneCheckBox.setEnabled(showStatusBar.isSelected());
		
		final JCheckBox emptySelectionCheckBox = new JCheckBox("Allow Empty Selection");
		emptySelectionCheckBox.setMnemonic('e');
		emptySelectionCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				try {
					monthView.setEmptySelectionAllowed(emptySelectionCheckBox.isSelected());
				} catch (Exception e) {
					emptySelectionCheckBox.setSelected(monthView.isEmptySelectionAllowed());
					JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(MonthViewDemo.this), "You can switch off empty selection only if at least one date is selected.", "Empty Selection Error", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Dismiss" }, null);
				}
			}
		});
		emptySelectionCheckBox.setSelected(monthView.isEmptySelectionAllowed());
		
		JLabel monthViewUIDelegateLabel = new JLabel("Custom UI:");
		final JComboBox monthViewUIDelegateComboBox = new JComboBox(new String[] {"Default", "Windows Date/Time", "Read Only Title", "Image Scroll Buttons"}) {
			/**
			 * We need to override this to reselect the default UI for 
			 * the JMonthView when the Look and Feel is changed.
			 */
			public void updateUI() {
				super.updateUI();
				setSelectedIndex(0);
			}
		};
		monthViewUIDelegateLabel.setFont(monthViewUIDelegateLabel.getFont().deriveFont(Font.BOLD));
		monthViewUIDelegateLabel.setDisplayedMnemonic('u');
		monthViewUIDelegateLabel.setLabelFor(monthViewUIDelegateComboBox);

		monthViewUIDelegateComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (monthViewUIDelegateComboBox.getSelectedIndex() == 0) {
					monthView.putClientProperty("JMonthView.monthUI", DefaultMonthUI.class.getName());
					monthView.setUI((DateUI)DefaultMonthViewUI.createUI(monthView));
					monthView.invalidate();
				} else if (monthViewUIDelegateComboBox.getSelectedIndex() == 1) {
					monthView.putClientProperty("JMonthView.monthUI", MonthWindowsDateTimeUI.class.getName());
					monthView.setUI((DateUI)MonthViewNoButtonsUI.createUI(monthView));
					monthView.invalidate();
				} else if (monthViewUIDelegateComboBox.getSelectedIndex() == 2) {
					monthView.putClientProperty("JMonthView.monthUI", MonthROTitleUI.class.getName());
					monthView.setUI((DateUI)DefaultMonthViewUI.createUI(monthView));
					monthView.invalidate();
				} else if (monthViewUIDelegateComboBox.getSelectedIndex() == 3) {
					monthView.putClientProperty("JMonthView.monthUI", DefaultMonthUI.class.getName());
					monthView.setUI((DateUI)MonthViewYearButtonsUI.createUI(monthView));
					monthView.invalidate();
				}
			}
		});

		generalPanel.add(monthViewUIDelegateLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(monthViewUIDelegateComboBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(localeLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(localeComboBox, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(showStatusBar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(displayTodayCheckBox, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(displayNoneCheckBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(emptySelectionCheckBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(new JLabel(), new GridBagConstraints(0, 4, 3, 1, 2.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		return generalPanel;
	}

	private JComponent createGridPanel() {
		JPanel gridPanel = new JPanel();
		gridPanel.setBorder(new TitledBorder("Grid"));
		gridPanel.setLayout(new GridBagLayout());
				
		JLabel rowsLabel = new JLabel("Rows:");
		rowsLabel.setDisplayedMnemonic('r');
		
		final JComboBox rowsComboBox = new JComboBox(new Integer[] {new Integer(1), new Integer(2), new Integer(3), new Integer(4)});
		rowsLabel.setLabelFor(rowsComboBox);

		rowsComboBox.setSelectedItem(new Integer(monthView.getRows()));
		rowsComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer rows = (Integer) rowsComboBox.getSelectedItem();
					monthView.setRows(rows.intValue());
				}
			}
		});
		
		JLabel columnsLabel = new JLabel("Columns:");
		columnsLabel.setDisplayedMnemonic('c');
		
		final JComboBox columnsComboBox = new JComboBox(new Integer[] {new Integer(1), new Integer(2), new Integer(3), new Integer(4)});
		columnsLabel.setLabelFor(columnsComboBox);

		columnsComboBox.setSelectedItem(new Integer(monthView.getColumns()));
		columnsComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer columns = (Integer) columnsComboBox.getSelectedItem();
					monthView.setColumns(columns.intValue());
				}
			}
		});
		
		JLabel scrollingDeltaLabel = new JLabel("Scrolling Delta:");
		scrollingDeltaLabel.setDisplayedMnemonic('s');
		
		final JComboBox scrollingDeltaComboBox = new JComboBox(new Integer[] {new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9), new Integer(10), new Integer(11), new Integer(12)});
		scrollingDeltaComboBox.setMaximumRowCount(12);
		scrollingDeltaLabel.setLabelFor(scrollingDeltaComboBox);

		scrollingDeltaComboBox.setSelectedItem(new Integer(monthView.getRows()));
		scrollingDeltaComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer delta = (Integer) scrollingDeltaComboBox.getSelectedItem();
					monthView.setScrollingDelta(delta.intValue());
				}
			}
		});
		
		gridPanel.add(rowsLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(rowsComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(columnsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(columnsComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(scrollingDeltaLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(scrollingDeltaComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.SOUTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(new JLabel(), new GridBagConstraints(0, 4, 3, 1, 2.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		return gridPanel;
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JMonthView Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		MonthViewDemo newContentPane = new MonthViewDemo();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);

		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
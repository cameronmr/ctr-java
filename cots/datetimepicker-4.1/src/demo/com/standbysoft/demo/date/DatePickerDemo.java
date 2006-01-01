/*
 * @(#)DatePickerDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.*;

import com.standbysoft.component.date.DateSelectionModel.SelectionMode;
import com.standbysoft.component.date.swing.JDatePicker;

/**
 * Shows specific <code>JDatePicker</code> operations.
 */
public class DatePickerDemo extends JPanel {
	/**
	 * Actual date picker used to show its features.
	 */
	private JDatePicker datePicker;

	public DatePickerDemo() {
		datePicker = createDatePicker();
		
		setLayout(new GridBagLayout());
		
		add(datePicker, new GridBagConstraints(0, 0, 1, 1, 4.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 5, 5), 0, 0));
		add(createGeneralPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateFormattingPanel(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createColorsPanel(), new GridBagConstraints(2, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));		
		add(createTablePanel(), new GridBagConstraints(1, 2, 2, 1, 3.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Creates the date picker component that is used by this demo.
	 * 
	 * @return date picker component that is used by this demo.
	 */
	private JDatePicker createDatePicker() {
		return new JDatePicker(true);
	}
	
	/**
	 * Creates a panel that controls specific JDatePicker features.
	 * 
	 * @return actual panel
	 */
	private JComponent createGeneralPanel() {
		JPanel generalPanel = new JPanel();
		generalPanel.setBorder(new TitledBorder("General"));
		generalPanel.setLayout(new GridBagLayout());

//		datePicker.putClientProperty("JDatePicker.backgroundOnEditable", Boolean.TRUE);
		
		final JCheckBox editableCheckBox = new JCheckBox("Editable", datePicker.isEditable());
		editableCheckBox.setFont(editableCheckBox.getFont().deriveFont(Font.BOLD));
		editableCheckBox.setMnemonic('d');
		editableCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datePicker.setEditable(editableCheckBox.isSelected());
				DatePickerDemo.this.invalidate();
			}
		});
		
		final JCheckBox enabledCheckBox = new JCheckBox("Enabled", datePicker.isEnabled());
		enabledCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				datePicker.setEnabled(enabledCheckBox.isSelected());
			}
		});
		
		final JCheckBox multipleCheckBox = new JCheckBox("Multiple Selection", (datePicker.getDateSelectionModel().getSelectionMode() == SelectionMode.MULTIPLE_INTERVAL));
		multipleCheckBox.setToolTipText("CTRL click or drag the mouse to select multiple dates in the drop-down calendar");
		multipleCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (multipleCheckBox.isSelected()) {
					datePicker.getDateSelectionModel().setSelectionMode(SelectionMode.MULTIPLE_INTERVAL);
				} else {
					datePicker.getDateSelectionModel().setSelectionMode(SelectionMode.SINGLE);
				}
			}
		});
		
		Color color = null;
		
		JLabel foregroundLabel = new JLabel("Foreground:");
		foregroundLabel.setDisplayedMnemonic('o');
		final JButton foregroundButton = new JButton(" ");
		foregroundLabel.setLabelFor(foregroundButton);
		foregroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Foreground", datePicker.getCalendarForeground());
				if (color != null) {
					datePicker.setForeground(color);
					foregroundButton.setBackground(datePicker.getForeground());
				}
			}
		});
		
		color = datePicker.getForeground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		foregroundButton.setBackground(color);
		
		JLabel backgroundLabel = new JLabel("Background:");
		final JButton backgroundButton = new JButton(" ");
		backgroundLabel.setDisplayedMnemonic('b');
		backgroundLabel.setLabelFor(backgroundButton);
		backgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Background", datePicker.getCalendarTitleBackground());
				if (color != null) {
					datePicker.setBackground(color);
					backgroundButton.setBackground(datePicker.getBackground());
				}
			}
		});
		
		color = datePicker.getBackground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		backgroundButton.setBackground(color);

		generalPanel.add(editableCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(enabledCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(multipleCheckBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		generalPanel.add(foregroundLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		generalPanel.add(foregroundButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		generalPanel.add(backgroundLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		generalPanel.add(backgroundButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(new JLabel(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));
		
		return generalPanel;
	}
	/**
	 * Creates a panel that controls JDatePicker date formatting.
	 * 
	 * @return actual panel
	 */
	private JPanel createDateFormattingPanel() {
		JPanel formattingPanel = new JPanel();
		formattingPanel.setBorder(new TitledBorder("Date Formatting"));
		formattingPanel.setLayout(new GridBagLayout());		
		
		JLabel localeLabel = new JLabel("Locale:");
		localeLabel.setDisplayedMnemonic('l');
		final JComboBox localeComboBox = new JComboBox(Locale.getAvailableLocales());
		localeLabel.setLabelFor(localeComboBox);

		localeComboBox.setSelectedItem(datePicker.getLocale());
		localeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Locale locale = (Locale) localeComboBox.getSelectedItem();
					datePicker.setLocale(locale);
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
		
		
		final JLabel formatLabel = new JLabel("Date Format:");
		formatLabel.setDisplayedMnemonic('d');
		
		final JComboBox formatComboBox = new JComboBox(new Integer[] {new Integer(DateFormat.SHORT), new Integer(DateFormat.MEDIUM), new Integer(DateFormat.LONG), new Integer(DateFormat.FULL)});
		formatLabel.setLabelFor(formatComboBox);
		formatComboBox.setSelectedItem(new Integer(datePicker.getDateFormat()));
		formatComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer integer = (Integer) formatComboBox.getSelectedItem();
					datePicker.setDateFormat(integer.intValue());
				}
			}
		});
		
		formatComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				switch (((Integer)value).intValue()) {
					case DateFormat.SHORT: l.setText("Short"); break;
					case DateFormat.MEDIUM: l.setText("Medium"); break;
					case DateFormat.LONG: l.setText("Long"); break;
					case DateFormat.FULL: l.setText("Full"); break;
					default: l.setText("Unavailable");
				}

				return l;
			}
		});

		String[] patternExamples = {
				 "MMM d, yyyy hh:mm a",
				 "dd MMMMM yyyy",
				 "dd.MM.yy",
				 "MM/dd/yy",
				 "yyyy.MM.dd G 'at' hh:mm:ss z",
				 "EEE, MMM d, ''yy",
				 "h:mm a",
				 "H:mm:ss:SSS",
				 "K:mm a,z",
				 "yyyy.MMMMM.dd GGG hh:mm aaa"
		};
		
		final JComboBox customDateFormatComboBox = new JComboBox(patternExamples);
		customDateFormatComboBox.setEditable(true);
		customDateFormatComboBox.addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				datePicker.setCustomDateFormat((String)customDateFormatComboBox.getSelectedItem());
			}
		});

		final JCheckBox customDateFormatCheckBox = new JCheckBox("Custom Format:");
		customDateFormatCheckBox.setMnemonic('u');
		customDateFormatCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				JCheckBox cb = (JCheckBox)evt.getSource();
				
				if (cb.isSelected()) {
					datePicker.setCustomDateFormat((String)customDateFormatComboBox.getSelectedItem());
					formatComboBox.setEnabled(false);
					formatLabel.setEnabled(false);
					customDateFormatComboBox.setEnabled(true);
				} else {
					datePicker.setCustomDateFormat(null);
					formatComboBox.setEnabled(true);
					formatLabel.setEnabled(true);
					customDateFormatComboBox.setEnabled(false);
				}
			}
		});
		customDateFormatCheckBox.setSelected(!customDateFormatCheckBox.isSelected());
		customDateFormatCheckBox.setSelected(!customDateFormatCheckBox.isSelected());

		formattingPanel.add(customDateFormatCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(customDateFormatComboBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(formatLabel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(formatComboBox, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(localeLabel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(localeComboBox, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(new JLabel(), new GridBagConstraints(3, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));

		return formattingPanel;
	}
	
	/**
	 * Creates a panel that controls the colors of the JDatePicker drop down calendar.
	 * 
	 * @return actual panel
	 */
	private JComponent createColorsPanel() {
		JPanel colorsPanel = new JPanel();
		colorsPanel.setBorder(new TitledBorder("Calendar Colors"));
		colorsPanel.setLayout(new GridBagLayout());
		
		Color color = null;
		
		JLabel foregroundLabel = new JLabel("Foreground:");
		foregroundLabel.setDisplayedMnemonic('o');
		final JButton foregroundButton = new JButton(" ");
		foregroundLabel.setLabelFor(foregroundButton);
		foregroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Foreground", datePicker.getCalendarForeground());
				if (color != null) {
					datePicker.setCalendarForeground(color);
					foregroundButton.setBackground(datePicker.getCalendarForeground());
				}
			}
		});
		
		color = datePicker.getCalendarForeground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		foregroundButton.setBackground(color);
		
		JLabel titleBackgroundLabel = new JLabel("Title Background:");
		final JButton titleBackgroundButton = new JButton(" ");
		titleBackgroundLabel.setDisplayedMnemonic('b');
		titleBackgroundLabel.setLabelFor(titleBackgroundButton);
		titleBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Title Background", datePicker.getCalendarTitleBackground());
				if (color != null) {
					datePicker.setCalendarTitleBackground(color);
					titleBackgroundButton.setBackground(datePicker.getCalendarTitleBackground());
				}
			}
		});
		
		color = datePicker.getCalendarTitleBackground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		titleBackgroundButton.setBackground(color);
		
		JLabel titleForegroundLabel = new JLabel("Title Foreground:");
		final JButton titleForegroundButton = new JButton(" ");
		titleForegroundLabel.setDisplayedMnemonic('r');
		titleForegroundLabel.setLabelFor(titleForegroundButton);
		titleForegroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Title Foreground", datePicker.getCalendarTitleForeground());
				if (color != null) {
					datePicker.setCalendarTitleForeground(color);
					titleForegroundButton.setBackground(datePicker.getCalendarTitleForeground());
				}
			}
		});
		
		color = datePicker.getCalendarTitleForeground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		titleForegroundButton.setBackground(color);
		
		JLabel trailingForegroundLabel = new JLabel("Trailing Foreground:");
		final JButton trailingForegroundButton = new JButton(" ");
		trailingForegroundLabel.setDisplayedMnemonic('t');
		trailingForegroundLabel.setLabelFor(trailingForegroundButton);
		trailingForegroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Trailing Foreground", datePicker.getCalendarTrailingForeground());
				if (color != null) {
					datePicker.setCalendarTrailingForeground(color);
					trailingForegroundButton.setBackground(datePicker.getCalendarTrailingForeground());
				}
			}
		});
		
		color = datePicker.getCalendarTrailingForeground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		trailingForegroundButton.setBackground(color);
		
		JLabel monthBackgroundLabel = new JLabel("Month Background:");
		monthBackgroundLabel.setDisplayedMnemonic('m');
		final JButton monthBackgroundButton = new JButton(" ");
		monthBackgroundLabel.setLabelFor(monthBackgroundButton);
		monthBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(DatePickerDemo.this, "Choose Month Background", datePicker.getCalendarBackground());
				if (color != null) {
					datePicker.setCalendarBackground(color);
					monthBackgroundButton.setBackground(datePicker.getCalendarBackground());
				}
			}
		});
		
		color = datePicker.getCalendarBackground();
		if (color != null && color instanceof ColorUIResource) {
			color = new Color(color.getRGB());
		}
		monthBackgroundButton.setBackground(color);
		
		colorsPanel.add(foregroundLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(foregroundButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(titleBackgroundLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(titleBackgroundButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(titleForegroundLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(titleForegroundButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(trailingForegroundLabel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(trailingForegroundButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(monthBackgroundLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(monthBackgroundButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		colorsPanel.add(new JLabel(), new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		return colorsPanel;
	}
	
	/**
	 * Creates a panel that shows how JDatePicker can be used as a JTable cell editor.
	 * 
	 * @return actual panel
	 */
	private JComponent createTablePanel() {
		JPanel tablePanel = new JPanel(new BorderLayout());
		tablePanel.setBorder(new TitledBorder("JTable Support"));
		final JTable table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(400, 100));
//		table.setSurrendersFocusOnKeystroke(true);
		
		List projects = new Vector();
		projects.add(new Task("Release X", null, TaskPriority.HIGH, TaskStatus.STARTED));
		projects.add(new Task("Release v4.0", new GregorianCalendar(2005, Calendar.APRIL, 7).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v3.3", new GregorianCalendar(2004, Calendar.DECEMBER, 17).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v3.2", new GregorianCalendar(2004, Calendar.OCTOBER, 12).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v3.1", new GregorianCalendar(2004, Calendar.AUGUST, 30).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v3.0", new GregorianCalendar(2004, Calendar.JUNE, 18).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.4", new GregorianCalendar(2004, Calendar.MAY, 3).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.3.1", new GregorianCalendar(2004, Calendar.APRIL, 15).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.3", new GregorianCalendar(2004, Calendar.MARCH, 25).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.2", new GregorianCalendar(2004, Calendar.FEBRUARY, 23).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.1", new GregorianCalendar(2004, Calendar.FEBRUARY, 2).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.0.1", new GregorianCalendar(2004, Calendar.JANUARY, 21).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		projects.add(new Task("Release v2.0", new GregorianCalendar(2003, Calendar.DECEMBER, 15).getTime(), TaskPriority.HIGH, TaskStatus.FINISHED));
		TableModel model = new TasksTableModel(projects);
		table.setModel(model);
		
		final JDatePicker tableDatePicker = new JDatePicker();
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy dd MMMM");
		tableDatePicker.setEmptySelectionText("Not Available");
		tableDatePicker.setCustomDateFormat("yyyy dd MMMM");
		table.setDefaultEditor(Date.class, JDatePicker.createTableCellEditor(tableDatePicker));
		table.setDefaultRenderer(Date.class, new DefaultTableCellRenderer() {
			protected void setValue(Object value) {
				setText(value == null ? "Not Available" : sdf.format((Date) value));
			}
		});
		
		final JCheckBox editableCheckBox = new JCheckBox("Editable Date Cell Editor", tableDatePicker.isEditable());
		editableCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableDatePicker.setEditable(editableCheckBox.isSelected());
			}
		});
		
		Boolean clear = (Boolean)tableDatePicker.getClientProperty("JDateEditComponent.clearDateOnEdit");
		final JCheckBox clearDateOnEditCheckBox = new JCheckBox("Clear Date On Edit", clear != null && clear.booleanValue());
		clearDateOnEditCheckBox.setToolTipText("Delete date when editing is started after a keyboard focus (not mouse)");
		clearDateOnEditCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableDatePicker.putClientProperty("JDateEditComponent.clearDateOnEdit", Boolean.valueOf(clearDateOnEditCheckBox.isSelected()));
			}
		});
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.getColumn(0).setMinWidth(25);
		tcm.getColumn(0).setMaxWidth(25);
		tcm.getColumn(2).setMinWidth(150);
		table.setRowHeight(25);

		TableColumn priorityColumn = table.getColumnModel().getColumn(3);
		JComboBox priorityComboBox = new JComboBox();
		priorityComboBox.addItem(TaskPriority.LOW);
		priorityComboBox.addItem(TaskPriority.NORMAL);
		priorityComboBox.addItem(TaskPriority.HIGH);
		priorityColumn.setCellEditor(new DefaultCellEditor(priorityComboBox));

		TableColumn statusColumn = table.getColumnModel().getColumn(4);
		JComboBox statusComboBox = new JComboBox();
		statusComboBox.addItem(TaskStatus.STARTED);
		statusComboBox.addItem(TaskStatus.FINISHED);
		statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

		JPanel rightPanel = new JPanel(new GridBagLayout());		
		JPanel buttonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
		JButton addButton = new JButton("Add");
		addButton.setMnemonic('A');
		addButton.setToolTipText("Adds a new task to the list");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TasksTableModel model = (TasksTableModel)table.getModel();
				model.add(new Task("TODO", new Date(), TaskPriority.NORMAL, TaskStatus.STARTED));
			}
		});
		JButton removeButton = new JButton("Remove");
		removeButton.setMnemonic('R');
		removeButton.setToolTipText("Removes the selected task");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TasksTableModel model = (TasksTableModel)table.getModel();
				int row = table.getSelectedRow();
				if (row != -1) {
					int nextRow = row;
					if (row == model.getRowCount() - 1) {
						nextRow = row - 1;
					}
					model.removeAt(row);
					table.getSelectionModel().setSelectionInterval(nextRow, nextRow);
				}
			}
		});
		buttonsPanel.add(addButton);
		buttonsPanel.add(removeButton);
		rightPanel.add(buttonsPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		rightPanel.add(new JLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.VERTICAL, new Insets(0, 5, 5, 5), 0, 0));

		JPanel tableControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		tableControlPanel.add(editableCheckBox);
//		tableControlPanel.add(clearDateOnEditCheckBox);
		tablePanel.add(tableControlPanel, BorderLayout.NORTH);
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
		tablePanel.add(rightPanel, BorderLayout.EAST);
		
		table.getSelectionModel().setSelectionInterval(0, 0);
		
		return tablePanel;
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JDatePicker Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		DatePickerDemo newContentPane = new DatePickerDemo();
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

class Task {
	private String name;
	private Date start;
	private TaskPriority priority;
	private TaskStatus status;
	
	public Task(String name, Date start, TaskPriority priority, TaskStatus status) {
		this.name = name;
		this.start = start;
		this.priority = priority;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public TaskPriority getPriority() {
		return priority;
	}

	public Date getStart() {
		return start;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setName(String string) {
		name = string;
	}

	public void setPriority(TaskPriority i) {
		priority = i;
	}

	public void setStart(Date date) {
		start = date;
	}

	public void setStatus(TaskStatus i) {
		status = i;
	}
}

class TaskPriority {
	public static final TaskPriority LOW = new TaskPriority("Low");
	public static final TaskPriority NORMAL = new TaskPriority("Normal");
	public static final TaskPriority HIGH = new TaskPriority("High");
	private String name;
	
	private TaskPriority(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}

class TaskStatus {
	public static final TaskStatus STARTED = new TaskStatus("Started");
	public static final TaskStatus FINISHED = new TaskStatus("Finished");
	private String name;
	
	private TaskStatus(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}
}

/**
 * The model used by the table that displays project information. 
 */
class TasksTableModel extends AbstractTableModel {
	private static final String COLUMN_NAMES[] = {"Crt", "Task Name", "Start", "Priority", "Status"};
	private static final Class COLUMN_CLASS[] = {Integer.class, String.class, Date.class, TaskPriority.class, TaskStatus.class};
	private List projects;
	
	public TasksTableModel(List projects) {
		this.projects = projects;
	}

	public void add(Task task) {
		if (projects.add(task)) {
			fireTableRowsInserted(projects.size() - 1, projects.size() - 1);
		}
	}
	
	public void removeAt(int index) {
		try {
			projects.remove(index);
			fireTableRowsDeleted(index, index);
		} catch (Exception e) {
		}
	}

	public int getRowCount() {
		return projects.size();
	}

	public int getColumnCount() {
		return COLUMN_NAMES.length;
	}
	
	public Class getColumnClass(int columnIndex) {
		return COLUMN_CLASS[columnIndex];
	}
	
	public String getColumnName(int columnIndex) {
		return COLUMN_NAMES[columnIndex];
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Task info = (Task)projects.get(rowIndex);
		
		if (columnIndex == 0) {
			return new Integer(rowIndex + 1);
		} else if (columnIndex == 1) {
			return info.getName();
		} else if (columnIndex == 2) {
			return info.getStart();
		} else if (columnIndex == 3) {
			return info.getPriority();
		} else if (columnIndex == 4) {
			return info.getStatus();
		}

		return null;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex != 0;
	}

	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		Task info = (Task)projects.get(rowIndex);
		
		if (columnIndex == 1) {
			info.setName((String)value);
		} else if (columnIndex == 2) {
			info.setStart((Date)value);
		} else if (columnIndex == 3) {
			info.setPriority((TaskPriority)value);
		} else if (columnIndex == 4) {
			info.setStatus((TaskStatus)value);
		}
	}
}
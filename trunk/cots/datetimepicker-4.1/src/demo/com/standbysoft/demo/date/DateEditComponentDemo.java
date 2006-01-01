/*
 * @(#)DateEditComponentDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.standbysoft.component.date.DateParserException;
import com.standbysoft.component.date.DateSelectionModel;
import com.standbysoft.component.date.DefaultDateParser;
import com.standbysoft.component.date.DefaultDateSelectionModel;
import com.standbysoft.component.date.event.DateEvent;
import com.standbysoft.component.date.event.DateListener;
import com.standbysoft.component.date.event.DateSelectionEvent;
import com.standbysoft.component.date.event.DateSelectionListener;
import com.standbysoft.component.date.swing.JDateEditComponent;
import com.standbysoft.component.date.swing.JDateField;

/**
 * Shows the basic operations that can be performed on a <code>JDateEditComponent</code>.
 */
public class DateEditComponentDemo extends JPanel {
	/**
	 * Date edit component for which this demo was created. It could be 
	 * JDatePicker or JDateField.
	 */
	private JDateEditComponent editComponent;
	
	public DateEditComponentDemo() {
		editComponent = new JDateField(); //we have decided to use JDateField.
		editComponent.setEmptySelectionText(" / / ");
		editComponent.setDateFormat(DateFormat.SHORT);

		setLayout(new GridBagLayout());
		
		add(editComponent, new GridBagConstraints(0, 0, 1, 1, 4.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 5, 5), 0, 0));
		add(createDateValidationPanel(), new GridBagConstraints(1, 0, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateEditingPanel(), new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateSelectionPanel(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateFormattingPanel(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createMessagesPanel(), new GridBagConstraints(1, 3, 2, 1, 3.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Creates a panel that controls validation behavior for the date edit component.
	 * 
	 * @return actual panel
	 */
	private JPanel createDateValidationPanel() {
		editComponent.setDateParser(new MyDateParser());
		
		JPanel validationPanel = new JPanel();
		validationPanel.setBorder(new TitledBorder("Date Validation"));
		validationPanel.setLayout(new GridBagLayout());
		
		final JCheckBox autoValidateCheckBox = new JCheckBox("Auto Validate", editComponent.isAutoValidate());
		autoValidateCheckBox.setToolTipText("Dates are validated when the component loses focus");
		autoValidateCheckBox.setMnemonic('v');
		autoValidateCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setAutoValidate(autoValidateCheckBox.isSelected());
			}
		});
		
		final JCheckBox autoRestoreCheckBox = new JCheckBox("Auto Restore", editComponent.isAutoRestore());
		autoRestoreCheckBox.setToolTipText("Invalid dates are automatically restored to the last valid date");
		autoRestoreCheckBox.setMnemonic('r');
		autoRestoreCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setAutoRestore(autoRestoreCheckBox.isSelected());
			}
		});
		
		final JCheckBox autoCenturyCheckBox = new JCheckBox("Auto Century", editComponent.isAutoCentury());
		autoCenturyCheckBox.setToolTipText("Century is automatically added to the year field if its value is less than 100");
		autoCenturyCheckBox.setMnemonic('c');
		autoCenturyCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setAutoCentury(autoCenturyCheckBox.isSelected());
			}
		});
		
		final JCheckBox beepOnErrorCheckBox = new JCheckBox("Beep On Error", editComponent.isBeepOnError());
		beepOnErrorCheckBox.setToolTipText("Beep if the typed date is not valid");
		beepOnErrorCheckBox.setMnemonic('b');
		beepOnErrorCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setBeepOnError(beepOnErrorCheckBox.isSelected());
			}
		});
		
		validationPanel.add(autoValidateCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		validationPanel.add(autoRestoreCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		validationPanel.add(autoCenturyCheckBox, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		validationPanel.add(beepOnErrorCheckBox, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		validationPanel.add(new JLabel(), new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		
		return validationPanel;
	}

	/**
	 * Creates a panel that controls editing features for the date edit component.
	 * 
	 * @return actual panel
	 */
	private JPanel createDateEditingPanel() {
		JPanel editingPanel = new JPanel();
		editingPanel.setBorder(new TitledBorder("Date Editing"));
		editingPanel.setLayout(new GridBagLayout());
		
		Boolean clear = (Boolean)editComponent.getClientProperty("JDateEditComponent.clearDateOnEdit");
		final JCheckBox clearDateOnEditCheckBox = new JCheckBox("Clear Date On Edit", clear != null && clear.booleanValue());
		clearDateOnEditCheckBox.setToolTipText("Delete date when editing is started after a keyboard focus (not mouse)");
		clearDateOnEditCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.putClientProperty("JDateEditComponent.clearDateOnEdit", Boolean.valueOf(clearDateOnEditCheckBox.isSelected()));
			}
		});
		
		final JCheckBox customParserAllowedCheckBox = new JCheckBox("Free Text Editing (Hint: type \"today\" to select the today date)", editComponent.isFreeText());
		customParserAllowedCheckBox.setToolTipText("Any text can be a date as long as it is recognized by the custom date parser");
		customParserAllowedCheckBox.setMnemonic('f');
		customParserAllowedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setFreeText(customParserAllowedCheckBox.isSelected());
			}
		});
		
		editingPanel.add(clearDateOnEditCheckBox, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		editingPanel.add(customParserAllowedCheckBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		editingPanel.add(new JLabel(), new GridBagConstraints(0, 0, 5, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		
		return editingPanel;
	}
	
	/**
	 * Creates a panel that controls date formatting for the date edit component.
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

		localeComboBox.setSelectedItem(editComponent.getLocale());
		localeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Locale locale = (Locale) localeComboBox.getSelectedItem();
					editComponent.setLocale(locale);
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
		
		
		final JLabel alignLabel = new JLabel("Alignment:");
		
		final JComboBox alignComboBox = new JComboBox(new Integer[] {new Integer(JDateEditComponent.LEFT), new Integer(JDateEditComponent.RIGHT), new Integer(JDateEditComponent.CENTER), new Integer(JDateEditComponent.TRAILING), new Integer(JDateEditComponent.LEADING)});
		alignLabel.setLabelFor(alignComboBox);
		alignComboBox.setSelectedItem(new Integer(editComponent.getHorizontalAlignment()));
		alignComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer integer = (Integer) alignComboBox.getSelectedItem();
					editComponent.setHorizontalAlignment(integer.intValue());
				}
			}
		});
		
		alignComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				switch (((Integer)value).intValue()) {
					case JDateEditComponent.LEADING: l.setText("Leading"); break;
					case JDateEditComponent.TRAILING: l.setText("Trailing"); break;
					case JDateEditComponent.LEFT: l.setText("Left"); break;
					case JDateEditComponent.CENTER: l.setText("Center"); break;
					case JDateEditComponent.RIGHT: l.setText("Right"); break;
					default: l.setText("Unavailable");
				}

				return l;
			}
		});
		
		final JLabel formatLabel = new JLabel("Date Format:");
		formatLabel.setDisplayedMnemonic('d');
		
		final JComboBox formatComboBox = new JComboBox(new Integer[] {new Integer(DateFormat.SHORT), new Integer(DateFormat.MEDIUM), new Integer(DateFormat.LONG), new Integer(DateFormat.FULL)});
		formatLabel.setLabelFor(formatComboBox);
		formatComboBox.setSelectedItem(new Integer(editComponent.getDateFormat()));
		formatComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer integer = (Integer) formatComboBox.getSelectedItem();
					editComponent.setDateFormat(integer.intValue());
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
				editComponent.setCustomDateFormat((String)customDateFormatComboBox.getSelectedItem());
			}
		});

		final JCheckBox customDateFormatCheckBox = new JCheckBox("Custom Format:");
		customDateFormatCheckBox.setMnemonic('u');
		customDateFormatCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				JCheckBox cb = (JCheckBox)evt.getSource();
				
				if (cb.isSelected()) {
					editComponent.setCustomDateFormat((String)customDateFormatComboBox.getSelectedItem());
					formatComboBox.setEnabled(false);
					formatLabel.setEnabled(false);
					customDateFormatComboBox.setEnabled(true);
				} else {
					editComponent.setCustomDateFormat(null);
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
		formattingPanel.add(alignLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(alignComboBox, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		formattingPanel.add(new JLabel(), new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));

		return formattingPanel;
	}

	/**
	 * Creates a panel that controls date selection for the date edit component.
	 * 
	 * @return actual panel
	 */
	private JPanel createDateSelectionPanel() {
		JPanel dateSelectionPanel = new JPanel();
		dateSelectionPanel.setBorder(new TitledBorder("Date Selection"));
		dateSelectionPanel.setLayout(new GridBagLayout());
		
		JLabel nullDateLabel = new JLabel("Null Date Text: ");
		final JCheckBox nullDateCheckBox = new JCheckBox("Allow Null Date");
		nullDateCheckBox.setMnemonic('n');
		final JTextField nullDateTextField = new JTextField(editComponent.getEmptySelectionText(), 10);
		final JButton nullDateTextButton = new JButton("Apply");
		nullDateTextButton.setMargin(new Insets(2, 2, 2, 2));
		nullDateTextButton.setToolTipText("Set the text that represents a null date for the date picker");
		nullDateTextButton.setMnemonic('t');

		nullDateTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editComponent.setEmptySelectionText(nullDateTextField.getText());
			}
		});
		
		nullDateCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				try {
					editComponent.setEmptySelectionAllowed(nullDateCheckBox.isSelected());
					nullDateTextField.setEnabled(nullDateCheckBox.isSelected());
					nullDateTextButton.setEnabled(nullDateCheckBox.isSelected());
				} catch (Exception e) {
					nullDateCheckBox.setSelected(editComponent.isEmptySelectionAllowed());
					JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(DateEditComponentDemo.this), "The following error occurred:\n" + e.getMessage(), "Null Date Error", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Dismiss" }, null);
				}
			}
		});
		nullDateCheckBox.setSelected(editComponent.isEmptySelectionAllowed());
		nullDateTextField.setEnabled(nullDateCheckBox.isSelected());
		nullDateTextButton.setEnabled(nullDateCheckBox.isSelected());

		JLabel dateSelectionModelLabel = new JLabel("Selection Model:");
		final JComboBox dateSelectionModelComboBox = new JComboBox(new Object[] {new DefaultDateSelectionModel(), new NoWeekendDateSelectionModel()});
		dateSelectionModelComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					DateSelectionModel dsm = (DateSelectionModel)dateSelectionModelComboBox.getSelectedItem();
					editComponent.setDateSelectionModel(dsm);
				}
			}
		});
		dateSelectionModelComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				String className = value.getClass().getName();
				
				if (className.indexOf("Default") != -1) {
					l.setText("Default");
				} else if (className.indexOf("NoWeekend") != -1) {
					l.setText("No Weekends");
				} else {
					l.setText("Custom");
				}

				return l;
			}
		});

		dateSelectionPanel.add(nullDateCheckBox, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(nullDateLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(nullDateTextField, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(nullDateTextButton, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(dateSelectionModelLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(dateSelectionModelComboBox, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(new JLabel(), new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		return dateSelectionPanel;
	}

	/**
	 * Creates a panel that displays date selection messages.
	 * 
	 * @return actual panel
	 */
	private JPanel createMessagesPanel() {
		JPanel messagesPanel = new JPanel();
		messagesPanel.setBorder(new TitledBorder("Messages"));
		messagesPanel.setLayout(new GridBagLayout());

		final JTextArea messageArea = new JTextArea();
		messageArea.setColumns(15);
		messageArea.setRows(10);		
		
		editComponent.addDateSelectionListener(new MyDateSelectionListener(messageArea));
		editComponent.addDateListener(new MyDateListener(messageArea));

		JButton clearSelectionButton = new JButton("Clear");
		clearSelectionButton.setMnemonic('e');
		clearSelectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageArea.setText("");
			}
		});
		
		messagesPanel.add(new JScrollPane(messageArea), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		messagesPanel.add(clearSelectionButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));

		return messagesPanel;
	}
	
	class MyDateSelectionListener implements DateSelectionListener {
		private JTextArea messageArea;
		
		public MyDateSelectionListener(JTextArea messageArea) {
			this.messageArea = messageArea;
		}
		
		public void dateSelectionChanged(DateSelectionEvent evt) {
			messageArea.append(evt + "\n");
		}

		public void disabledDatesChanged(DateSelectionEvent evt) {}

		public void selectionModeChanged(DateSelectionEvent evt) {}

		public void emptySelectionAllowedChanged(DateSelectionEvent evt) {}

		public void disabledDateSelectionAttempted(DateSelectionEvent evt) {}
	}
	
	class MyDateListener implements DateListener {
		private JTextArea messageArea;
		
		public MyDateListener(JTextArea messageArea) {
			this.messageArea = messageArea;
		}
		
		public void dateFieldChanged(DateEvent evt) {
			messageArea.append(evt + "\n");
		}

		public void dateChanged(DateEvent evt) {
			messageArea.append(evt + "\n");
		}

		public void dateFieldCleared(DateEvent evt) {
			messageArea.append(evt + "\n");
		}
	}
	
	class MyDateParser extends DefaultDateParser {
		private Calendar cal = Calendar.getInstance();
		
		public MyDateParser() {
			cal.clear(Calendar.HOUR);
			cal.clear(Calendar.HOUR_OF_DAY);
			cal.clear(Calendar.MINUTE);
			cal.clear(Calendar.SECOND);
			cal.clear(Calendar.MILLISECOND);
		}
		
		public Date parse(String text, Date reference, DateFormat format) throws DateParserException {
			if (text.toLowerCase().equals("today")) {
				return cal.getTime();
			} else {
				return super.parse(text, reference, format);
			}
		}
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JDateEditComponent Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		DateEditComponentDemo newContentPane = new DateEditComponentDemo();
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
/*
 * @(#)DateComponentDemo.java
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
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ActionMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import com.standbysoft.component.date.DefaultDateSelectionModel;
import com.standbysoft.component.date.event.DateSelectionAdapter;
import com.standbysoft.component.date.event.DateSelectionEvent;
import com.standbysoft.component.date.event.DateSelectionListener;
import com.standbysoft.component.date.swing.JDateComponent;
import com.standbysoft.component.date.swing.JDatePicker;
import com.standbysoft.component.date.swing.JMonthView;

/**
 * Shows the basic operations that can be performed on a <code>JDateComponent</code>.
 */
public class DateComponentDemo extends JPanel {
	/**
	 * Date component for which this demo was created. It could be any date 
	 * component: JDatePicker, JDateField, JMonth or JMonthView.
	 */
	private JDateComponent component;

	public DateComponentDemo() {
		component = new JMonthView(); //we have decided to use JMonthView.
		
		installCustomKeyboardAction();
		
		setLayout(new GridBagLayout());
		
		add(component, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(createGeneralPanel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateSelectionPanel(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		add(createMessagesPanel(), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Associates the select-next-day action with the SPACE key for the date component.
	 */
	private void installCustomKeyboardAction() {
		InputMap im = component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap am = component.getActionMap();

		KeyStroke keyForward = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0);
		im.put(keyForward, JDateComponent.selectNextDayAction);
		am.put(JDateComponent.selectNextDayAction, new JDateComponent.RollDateAction(Calendar.DATE, true));
	}
	
	/**
	 * Creates a panel that controls general features of a date component.
	 * 
	 * @return actual panel
	 */
	private JPanel createGeneralPanel() {
		JPanel generalPanel = new JPanel();
		
		generalPanel.setBorder(new TitledBorder("General"));
		generalPanel.setLayout(new GridBagLayout());
		
		final JCheckBox enabledCheckBox = new JCheckBox("Enabled", component.isEnabled());
		enabledCheckBox.setMnemonic('e');
		enabledCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				component.setEnabled(enabledCheckBox.isSelected());
			}
		});
		
		JLabel localeLabel = new JLabel("Locale:");
		localeLabel.setDisplayedMnemonic('l');
		final JComboBox localeComboBox = new JComboBox(Locale.getAvailableLocales());
		localeLabel.setLabelFor(localeComboBox);

		localeComboBox.setSelectedItem(component.getLocale());
		localeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Locale locale = (Locale) localeComboBox.getSelectedItem();
					component.setLocale(locale);
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
		
		generalPanel.add(enabledCheckBox, new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(localeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(localeComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		generalPanel.add(new JLabel(), new GridBagConstraints(2, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		return generalPanel;
	}
	
	/**
	 * Creates a panel that controls date selection features of a date component.
	 * 
	 * @return actual panel
	 */
	private JPanel createDateSelectionPanel() {
		final DefaultDateSelectionModel defaultSelectionModel = new DefaultDateSelectionModel();
		final DefaultDateSelectionModel setSelectionModel = new DefaultDateSelectionModel();
		final DefaultDateSelectionModel intervalSelectionModel = new DefaultDateSelectionModel();
		final NoWeekendDateSelectionModel noWeekendSelectionModel = new NoWeekendDateSelectionModel();


		JPanel dateSelectionPanel = new JPanel();
		dateSelectionPanel.setBorder(new TitledBorder("Date Selection"));
		dateSelectionPanel.setLayout(new GridBagLayout());
		
		final JCheckBox emptySelectionCheckBox = new JCheckBox("Allow Empty Selection");
		emptySelectionCheckBox.setMnemonic('e');
		emptySelectionCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				try {
					component.setEmptySelectionAllowed(emptySelectionCheckBox.isSelected());
				} catch (Exception e) {
					emptySelectionCheckBox.setSelected(component.isEmptySelectionAllowed());
					JOptionPane.showOptionDialog(JOptionPane.getFrameForComponent(DateComponentDemo.this), "You can switch off empty selection only if at least one date is selected.", "Empty Selection Error", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Dismiss" }, null);
				}
			}
		});
		emptySelectionCheckBox.setSelected(component.isEmptySelectionAllowed());
		
		final JToggleButton toggleButton = new JToggleButton("Toggle disabled dates");
		toggleButton.setToolTipText("Click and turn on/off the disabled state for calendar dates");

		component.addDateSelectionListener(new DateSelectionAdapter() {
			public void dateSelectionChanged(DateSelectionEvent evt) {
				if (toggleButton.isSelected()) {
					Date[] dates = setSelectionModel.getSelectedDates();
					for (int i = 0; i < dates.length; i++) {
						setSelectionModel.addDisabled(dates[i]);
					}
				}
			}
			
			public void disabledDateSelectionAttempted(DateSelectionEvent evt) {
				if (toggleButton.isSelected()) {
					setSelectionModel.removeDisabled(evt.getFirstDate());
				}
			}
		});
		
		final JLabel minDateLabel = new JLabel("Valid from");
		minDateLabel.setDisplayedMnemonic('v');
		final JDatePicker minDateDatePicker = new JDatePicker();
		minDateDatePicker.setEmptySelectionText("Always");
		minDateDatePicker.setEditable(true);
		minDateLabel.setLabelFor(minDateDatePicker);
		minDateDatePicker.addDateSelectionListener(new DateSelectionAdapter() {
			public void dateSelectionChanged(DateSelectionEvent evt) {
				intervalSelectionModel.setMinimumAllowed(minDateDatePicker.getSelectedDate());
			}
		});
		minDateDatePicker.setSelectedDate(null);

		final JLabel maxDateLabel = new JLabel("until");
		maxDateLabel.setDisplayedMnemonic('u');
		final JDatePicker maxDateDatePicker = new JDatePicker();
		maxDateDatePicker.setEmptySelectionText("Always");
		maxDateDatePicker.setEditable(true);
		maxDateLabel.setLabelFor(maxDateDatePicker);
		maxDateDatePicker.addDateSelectionListener(new DateSelectionAdapter() {
			public void dateSelectionChanged(DateSelectionEvent evt) {
				intervalSelectionModel.setMaximumAllowed(maxDateDatePicker.getSelectedDate());
			}
		});
		maxDateDatePicker.setSelectedDate(null);

		final JComboBox dateSelectionModelComboBox = new JComboBox(new String[] {"Default", "Disable weekends", "Disable at your choice", "Keep specified interval" });
		dateSelectionModelComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				int index = dateSelectionModelComboBox.getSelectedIndex();

				if (evt.getStateChange() == ItemEvent.SELECTED) {
					toggleButton.setVisible(false);
					minDateLabel.setVisible(false);
					minDateDatePicker.setVisible(false);
					maxDateLabel.setVisible(false);
					maxDateDatePicker.setVisible(false);
					if (index == 0) {
						component.setDateSelectionModel(defaultSelectionModel);
					} else if (index == 1) {
						component.setDateSelectionModel(noWeekendSelectionModel);
					} else if (index == 2) {
						component.setDateSelectionModel(setSelectionModel);
						toggleButton.setVisible(true);
					} else if (index == 3) {
						component.setDateSelectionModel(intervalSelectionModel);
						minDateLabel.setVisible(true);
						minDateDatePicker.setVisible(true);
						maxDateLabel.setVisible(true);
						maxDateDatePicker.setVisible(true);
					}
				}
			}
		});
		dateSelectionModelComboBox.setSelectedIndex(1);
		JLabel dateSelectionModelLabel = new JLabel("Selection Model:");
		
		dateSelectionPanel.add(emptySelectionCheckBox, new GridBagConstraints(0, 0, 6, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(dateSelectionModelLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(dateSelectionModelComboBox, new GridBagConstraints(1, 1, 5, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(toggleButton, new GridBagConstraints(2, 2, 3, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(minDateLabel, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(minDateDatePicker, new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(maxDateLabel, new GridBagConstraints(4, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(maxDateDatePicker, new GridBagConstraints(5, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(new JLabel(), new GridBagConstraints(6, 3, 1, 1, 2.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		return dateSelectionPanel;
	}
	
	/**
	 * Creates a panel that displays date selection messages.
	 * 
	 * @return actual panel
	 */
	private JPanel createMessagesPanel() {
		final JTextArea messageArea = new JTextArea();
		
		messageArea.setColumns(40);
		messageArea.setRows(15);
		messageArea.setEditable(false);
		
		JButton clearSelectionButton = new JButton("Clear");
		clearSelectionButton.setMnemonic('c');
		clearSelectionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageArea.setText("");
			}
		});
		
		JPanel messagesPanel = new JPanel();
		messagesPanel.setBorder(new TitledBorder("Messages"));
		messagesPanel.setLayout(new GridBagLayout());
		messagesPanel.add(new JScrollPane(messageArea), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		messagesPanel.add(clearSelectionButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));

		//Registers a date selection listener to track selection changes		
		component.addDateSelectionListener(new MyDateSelectionListener(messageArea));
		
		//Registers an event listener to track action events		
		component.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageArea.append(e + "\n");
			}
		});
		
		return messagesPanel;
	}
	
	class MyDateSelectionListener implements DateSelectionListener {
		private JTextArea messageArea;
		
		public MyDateSelectionListener(JTextArea messageArea) {
			this.messageArea = messageArea;
		}
		
		public void dateSelectionChanged(DateSelectionEvent evt) {
			messageArea.append("[" + evt.getFirstDate() + ", " + evt.getLastDate() + "]\n");
		}

		public void disabledDatesChanged(DateSelectionEvent evt) {}

		public void selectionModeChanged(DateSelectionEvent evt) {}

		public void emptySelectionAllowedChanged(DateSelectionEvent evt) {}

		public void disabledDateSelectionAttempted(DateSelectionEvent evt) {}
	}
	
	class MyDateSelectionModel extends DefaultDateSelectionModel {
	  private Calendar cal = Calendar.getInstance();

	  public boolean isDisabled(Date date) {
		cal.setTime(date);
		return (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
	  }
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JDateComponent Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		DateComponentDemo newContentPane = new DateComponentDemo();
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

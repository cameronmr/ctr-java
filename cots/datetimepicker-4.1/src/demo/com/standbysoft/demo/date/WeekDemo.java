/*
 * @(#)WeekDemo.java
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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import com.standbysoft.component.date.swing.DefaultWeekModel;
import com.standbysoft.component.date.swing.JMonth;
import com.standbysoft.component.date.swing.JMonthView;
import com.standbysoft.component.date.swing.event.WeekModelAdapter;
import com.standbysoft.component.date.swing.event.WeekModelEvent;

/**
 * This demo shows specific WeekModel operations. The WeekModel is used to
 * control week settings for a JMonth component.
 */
public class WeekDemo extends JPanel {
	/**
	 * JMonth component for which this demo was created. It could be JMonth or JMonthView.
	 */
	private JMonth month;

	public WeekDemo() {
		month = createMonth();
		
		setLayout(new GridBagLayout());
		
		add(month, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(createWeekSelectionPanel(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(createWeekNumbersPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(createMessagesPanel(), new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Create the <code>JMonth</code> component used by this demo.
	 * 
	 * @return <code>JMonth</code> component used by this demo.
	 */
	private JMonth createMonth() {
		JMonthView mv = new JMonthView();
		mv.setColumns(1);
		mv.getWeekModel().setSelectedDow(Calendar.WEDNESDAY);
		
//		return mv;
		return new JMonth();
	}

	/**
	 * Creates a panel that control the week numbers.
	 * 
	 * @return actual panel
	 */
	private JPanel createWeekNumbersPanel() {
		JPanel weekNumbersPanel = new JPanel();
		weekNumbersPanel.setBorder(new TitledBorder("General"));
		weekNumbersPanel.setLayout(new GridBagLayout());

		DefaultWeekModel model = (DefaultWeekModel)month.getWeekModel();
		
		final JCheckBox isoWeekNumbersEnabled = new JCheckBox("ISO Week Numbers Enabled", model.isWeekNumberISOFormatEnabled());
		isoWeekNumbersEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//note that we KNOW here that the model is of the type DefaultWeekModel 
				DefaultWeekModel model = (DefaultWeekModel)month.getWeekModel();
				
				model.setWeekNumberISOFormatEnabled(isoWeekNumbersEnabled.isSelected());
			}
		});
		isoWeekNumbersEnabled.setToolTipText("Weeks are numbered according to the ISO 8601 standard (53 weeks)");

		final JLabel weekNamesLabel = new JLabel("Week Days Names:");
		final JComboBox weekNamesComboBox = new JComboBox(new String[] {"Letter", "Short", "Long"});
		weekNamesLabel.setLabelFor(weekNamesComboBox);

		weekNamesComboBox.setSelectedIndex(1);
		
		weekNamesComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					//note that we KNOW here that the model is of the type DefaultWeekModel
					DefaultWeekModel model = (DefaultWeekModel)month.getWeekModel();
					
					if (weekNamesComboBox.getSelectedIndex() == 0) {
						model.setDowNamesFormat(DefaultWeekModel.DOW_NAMES_LETTER);
					} else if (weekNamesComboBox.getSelectedIndex() == 1) {
						model.setDowNamesFormat(DefaultWeekModel.DOW_NAMES_SHORT);
					} else if (weekNamesComboBox.getSelectedIndex() == 2) {
						model.setDowNamesFormat(DefaultWeekModel.DOW_NAMES_LONG);
					}
				}
			}
		});
		
		final JLabel dowFirstLabel = new JLabel("Week Starts On:");
		dowFirstLabel.setDisplayedMnemonic('s');

		final JComboBox dowFirstComboBox = new JComboBox();
		dowFirstLabel.setLabelFor(dowFirstComboBox);
		dowFirstComboBox.addItem(new Integer(Calendar.SUNDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.MONDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.TUESDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.WEDNESDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.THURSDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.FRIDAY));
		dowFirstComboBox.addItem(new Integer(Calendar.SATURDAY));
		dowFirstComboBox.setSelectedItem(new Integer(month.getDowFirst()));
		dowFirstComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Integer df = (Integer) dowFirstComboBox.getSelectedItem();
					month.setDowFirst(df.intValue());
				}
			}
		});
		dowFirstComboBox.setRenderer(new DefaultListCellRenderer() {
			private String DAY_NAMES[] = new DateFormatSymbols().getWeekdays();

			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				Integer day = (Integer) value;
				l.setText(DAY_NAMES[day.intValue()]);

				return l;
			}
		});
		
		month.addPropertyChangeListener("weekModel", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				DefaultWeekModel model = (DefaultWeekModel)month.getWeekModel();
				isoWeekNumbersEnabled.setSelected(model.isWeekNumberISOFormatEnabled());
			}
		});
		
		weekNumbersPanel.add(isoWeekNumbersEnabled, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		weekNumbersPanel.add(weekNamesLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		weekNumbersPanel.add(weekNamesComboBox, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		weekNumbersPanel.add(dowFirstLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		weekNumbersPanel.add(dowFirstComboBox, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
	
		return weekNumbersPanel;
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
		
		month.addWeekModelListener(new MyWeekModelListener(messageArea, month));
	
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
	
	/**
	 * Creates a panel that controls specific week selection features.
	 * 
	 * @return actual panel
	 */
	private JPanel createWeekSelectionPanel() {
		JPanel monthPanel = new JPanel();
		monthPanel.setLayout(new GridBagLayout());
		monthPanel.setBorder(new TitledBorder("Selection"));

		final DefaultWeekModel defaultWeekModel = new DefaultWeekModel();
		final DefaultWeekModel toggleWeekModel = new ToggleWeekModel();
		
		month.setWeekModel(toggleWeekModel);		
		
		month.setWeekSelectionAllowed(true);
		final JCheckBox weekSelectionAllowed = new JCheckBox("Week Selection Allowed", month.isWeekSelectionAllowed());
		weekSelectionAllowed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setWeekSelectionAllowed(weekSelectionAllowed.isSelected());
			}
		});
		weekSelectionAllowed.setToolTipText("Weeks can be selected if their numbers are clicked");
		
		month.setDowSelectionAllowed(true);
		final JCheckBox dowSelectionAllowed = new JCheckBox("Day of Week Selection Allowed", month.isDowSelectionAllowed());
		dowSelectionAllowed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setDowSelectionAllowed(dowSelectionAllowed.isSelected());
			}
		});
		dowSelectionAllowed.setToolTipText("Days of week can be selected if their labels are clicked");

		month.putClientProperty("JMonth.isWeekLabelsHighlighted", Boolean.TRUE);
		final JCheckBox highlightLabels = new JCheckBox("Highlight Selected Labels", ((Boolean)month.getClientProperty("JMonth.isWeekLabelsHighlighted")).booleanValue());
		highlightLabels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.putClientProperty("JMonth.isWeekLabelsHighlighted", Boolean.valueOf(highlightLabels.isSelected()));
			}
		});		
		highlightLabels.setToolTipText("Highlights the weeks or days of week labels when they are selected");

		final JLabel weekMonthModelLabel = new JLabel("Week Model:");
		final JComboBox weekMonthModelComboBox = new JComboBox(new String[] {"Default", "Toggle Dow/Week Selection"});
		weekMonthModelLabel.setLabelFor(weekMonthModelComboBox);

		weekMonthModelComboBox.setSelectedIndex(1);
		
		weekMonthModelComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					if (weekMonthModelComboBox.getSelectedIndex() == 0) {
						month.setWeekModel(defaultWeekModel);
					} else if (weekMonthModelComboBox.getSelectedIndex() == 1) {
						month.setWeekModel(toggleWeekModel);
					}
				}
			}
		});
		
		month.addPropertyChangeListener("weekModel", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				weekSelectionAllowed.setSelected(month.isWeekSelectionAllowed());
				dowSelectionAllowed.setSelected(month.isDowSelectionAllowed());
			}
		});
		
		monthPanel.add(weekSelectionAllowed, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(dowSelectionAllowed, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(highlightLabels, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(weekMonthModelLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(weekMonthModelComboBox, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(new JLabel(), new GridBagConstraints(0, 10, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		return monthPanel;
	}
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("WeekModel Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		WeekDemo newContentPane = new WeekDemo();
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

/**
 * A listener that displays day of week and week selection messages in a specified
 * text area.
 */
class MyWeekModelListener extends WeekModelAdapter {
	private JTextArea messageArea;
	
	private JMonth month;
	
	public MyWeekModelListener(JTextArea messageArea, JMonth month) {
		this.messageArea = messageArea;
		this.month = month;
	}
	
	public void selectedDowChanged(WeekModelEvent evt) {
		messageArea.append("New selected dow: " + month.getWeekModel().getSelectedDow() + "\n");
	}
	
	public void selectedWeekChanged(WeekModelEvent evt) {
		messageArea.append("New selected week: " + month.getWeekModel().getSelectedWeek() + "/" + month.getWeekModel().getSelectedYear() + "\n");
	}
}
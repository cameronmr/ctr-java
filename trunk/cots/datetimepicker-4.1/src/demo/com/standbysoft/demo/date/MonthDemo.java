/*
 * @(#)MonthDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Vector;

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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.standbysoft.component.date.DateSelectionModel;
import com.standbysoft.component.date.DefaultDateSelectionModel;
import com.standbysoft.component.date.DateSelectionModel.SelectionMode;
import com.standbysoft.component.date.swing.DateRenderer;
import com.standbysoft.component.date.swing.DefaultDateRenderer;
import com.standbysoft.component.date.swing.DefaultMonthModel;
import com.standbysoft.component.date.swing.DefaultWeekModel;
import com.standbysoft.component.date.swing.JMonth;
import com.standbysoft.component.date.swing.MonthModel;
import com.standbysoft.component.date.swing.WeekModel;
import com.standbysoft.component.date.swing.event.WeekModelEvent;

/**
 * Shows the operations that can be performed on a <code>JMonth</code> or 
 * <code>JMonthView</code> component.
 */
public class MonthDemo extends JPanel {
	/**
	 * JMonth component for which this demo was created. It could be JMonth or JMonthView.
	 */
	private JMonth month;

	public MonthDemo() {
		month = createMonth();
		
		setLayout(new GridBagLayout());
		
		add(month, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		add(createFontsPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
		add(createColorsPanel(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 0, 5), 0, 0));
		add(createMonthPanel(), new GridBagConstraints(2, 1, 1, 2, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(createGridPanel(), new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(createDateSelectionPanel(), new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		add(new JLabel(), new GridBagConstraints(0, 3, 4, 1, 0.0, 2.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
	}

	/**
	 * Create the <code>JMonth</code> component used by this demo.
	 * 
	 * @return <code>JMonth</code> component used by this demo.
	 */
	private JMonth createMonth() {
		return new JMonth();
	}
	
	/**
	 * Creates a panel that controls the font for the component.
	 * 
	 * @return actual panel
	 */
	private JComponent createFontsPanel() {
		JScrollPane fontsPane = new JScrollPane();
		fontsPane.setBorder(new TitledBorder("Font"));

		Vector fonts = new Vector();
		fonts.add(new Font("Serif", Font.PLAIN, 10));
		fonts.add(new Font("Arial", Font.PLAIN, 10));
		fonts.add(new Font("Serif", Font.PLAIN, 12));
		fonts.add(new Font("Arial", Font.ITALIC, 12));
		fonts.add(new Font("Courier", Font.PLAIN, 13));
		fonts.add(new Font("Serif", Font.PLAIN, 13));
		fonts.add(new Font("Arial", Font.PLAIN, 14));
		fonts.add(month.getFont());
		Collections.sort(fonts, new Comparator() {
			public int compare(Object o1, Object o2) {
				Font f1 = (Font) o1;
				Font f2 = (Font) o2;

				return f1.getSize() - f2.getSize();
			}
		});
		
		final JList fontsList = new JList(fonts);
		month.setFont((Font)fonts.get(fonts.size() - 1));
		fontsList.setSelectedValue(month.getFont(), true);
		
		fontsList.setVisibleRowCount(5);
		fontsList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent evt) {
					Font font = (Font) fontsList.getSelectedValue();
					month.setFont(font);
			}
		});
		fontsList.setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				Font font = (Font) value;
				l.setFont(font);
				l.setText(font.getName() + " " + font.getSize());

				return l;
			}
		});
		
		fontsPane.setViewportView(fontsList);
		
		return fontsPane;
	}

	/**
	 * Creates a panel that controls the colors for the component.
	 * 
	 * @return actual panel
	 */
	private JPanel createColorsPanel() {
		JPanel colorsPanel = new JPanel();
		colorsPanel.setBorder(new TitledBorder("Colors"));
		colorsPanel.setLayout(new GridBagLayout());

		month.setForeground(Color.black);
		month.setTitleBackground(new Color(0, 114, 255));
		month.setTitleForeground(Color.white);
		month.setTrailingForeground(Color.gray);
		month.setMonthBackground(Color.white);
		
		JLabel foregroundLabel = new JLabel("Foreground:");
		foregroundLabel.setDisplayedMnemonic('o');
		final JButton foregroundButton = new JButton(" ");
		foregroundLabel.setLabelFor(foregroundButton);
		foregroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Foreground", month.getForeground());
				if (color != null) {
					month.setForeground(color);
					foregroundButton.setBackground(month.getForeground());
				}
			}
		});
		foregroundButton.setBackground(month.getForeground());
		
		JLabel titleBackgroundLabel = new JLabel("Title Background:");
		final JButton titleBackgroundButton = new JButton(" ");
		titleBackgroundLabel.setDisplayedMnemonic('b');
		titleBackgroundLabel.setLabelFor(titleBackgroundButton);
		titleBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Title Background", month.getTitleBackground());
				if (color != null) {
					month.setTitleBackground(color);
					titleBackgroundButton.setBackground(month.getTitleBackground());
				}
			}
		});
		titleBackgroundButton.setBackground(month.getTitleBackground());
		
		JLabel titleForegroundLabel = new JLabel("Title Foreground:");
		final JButton titleForegroundButton = new JButton(" ");
		titleForegroundLabel.setDisplayedMnemonic('r');
		titleForegroundLabel.setLabelFor(titleForegroundButton);
		titleForegroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Title Foreground", month.getTitleForeground());
				if (color != null) {
					month.setTitleForeground(color);
					titleForegroundButton.setBackground(month.getTitleForeground());
				}
			}
		});
		titleForegroundButton.setBackground(month.getTitleForeground());
		
		JLabel trailingForegroundLabel = new JLabel("Trailing Foreground:");
		final JButton trailingForegroundButton = new JButton(" ");
		trailingForegroundLabel.setDisplayedMnemonic('t');
		trailingForegroundLabel.setLabelFor(trailingForegroundButton);
		trailingForegroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Trailing Foreground", month.getTrailingForeground());
				if (color != null) {
					month.setTrailingForeground(color);
					trailingForegroundButton.setBackground(month.getTrailingForeground());
				}
			}
		});
		trailingForegroundButton.setBackground(month.getTrailingForeground());
		
		JLabel monthBackgroundLabel = new JLabel("Month Background:");
		final JButton monthBackgroundButton = new JButton(" ");
		monthBackgroundLabel.setDisplayedMnemonic('m');
		monthBackgroundLabel.setLabelFor(monthBackgroundButton);
		monthBackgroundButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Month Background", month.getMonthBackground());
				if (color != null) {
					month.setMonthBackground(color);
					monthBackgroundButton.setBackground(month.getMonthBackground());
				}
			}
		});
		monthBackgroundButton.setBackground(month.getMonthBackground());

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
	 * Creates a panel that controls specific month features.
	 * 
	 * @return actual panel
	 */
	private JPanel createMonthPanel() {
		final DateRenderer defaultDateRenderer = new DefaultDateRenderer();
		final DateRenderer weekendDateRenderer = new CustomWeekendDateRenderer();
		final DateRenderer specialDateRenderer = new CustomSpecialDateRenderer();
		final DateRenderer squareDateRenderer = new CustomDateRenderer();
		
		JPanel monthPanel = new JPanel();
		monthPanel.setLayout(new GridBagLayout());
		monthPanel.setBorder(new TitledBorder("Month"));
				
		JLabel rendererLabel = new JLabel("Date Renderer:");
		rendererLabel.setFont(rendererLabel.getFont().deriveFont(Font.BOLD));
		final JComboBox rendererComboBox = new JComboBox(new String[]{"Default", "Highlight Weekend Days", "Highlight Special Dates", "Square Selected Dates"});
		rendererComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				int index = rendererComboBox.getSelectedIndex();
				
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					if (index == 0) {
						month.setDateRenderer(defaultDateRenderer);
					} else if (index == 1) {
						month.setDateRenderer(weekendDateRenderer);
					} else if (index == 2) {
						month.setDateRenderer(specialDateRenderer);
					} else if (index == 3) {
						month.setDateRenderer(squareDateRenderer);
					}
				}
			}
		});
		rendererComboBox.setSelectedIndex(1);

		final JCheckBox trailingPreviousEnabled = new JCheckBox("Enable Previous Month Dates", month.isTrailingPreviousEnabled());
		trailingPreviousEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setTrailingPreviousEnabled(trailingPreviousEnabled.isSelected());
			}
		});
		
		final JCheckBox trailingNextEnabled = new JCheckBox("Enable Next Month Dates", month.isTrailingNextEnabled());
		trailingNextEnabled.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setTrailingNextEnabled(trailingNextEnabled.isSelected());
			}
		});

		final JCheckBox weekNumbersVisible = new JCheckBox("Show Week Numbers", month.isWeekNumbersVisible());
		weekNumbersVisible.setMnemonic('n');
		weekNumbersVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setWeekNumbersVisible(weekNumbersVisible.isSelected());
			}
		});
		
		final JCheckBox weekNamesVisible = new JCheckBox("Show Day Names", month.isWeekNamesVisible());
		weekNamesVisible.setMnemonic('n');
		weekNamesVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setWeekNamesVisible(weekNamesVisible.isSelected());
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
		
		final JLabel localeLabel = new JLabel("Locale:");
		localeLabel.setDisplayedMnemonic('l');
		final JComboBox localeComboBox = new JComboBox(Locale.getAvailableLocales());
		localeLabel.setLabelFor(localeComboBox);

		localeComboBox.setSelectedItem(month.getLocale());
		localeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					Locale locale = (Locale) localeComboBox.getSelectedItem();
					dowFirstComboBox.setSelectedItem(new Integer(Calendar.getInstance(locale).getFirstDayOfWeek()));
					month.setLocale(locale);
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

		final MonthModel defaultMonthModel = new DefaultMonthModel();
		final MonthModel latinMonthModel = new LatinMonthModel();
		
		final WeekModel defaultWeekModel = new DefaultWeekModel();
		final WeekModel latinWeekModel = new LatinWeekModel();
		
		final JLabel weekMonthModelLabel = new JLabel("Week & Month Models:");
		final JComboBox weekMonthModelComboBox = new JComboBox(new String[] {"Default", "Latin Names"});
		weekMonthModelLabel.setLabelFor(weekMonthModelComboBox);

		month.setMonthModel(defaultMonthModel);
		month.setWeekModel(defaultWeekModel);
		weekMonthModelComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					localeLabel.setEnabled(true);
					localeComboBox.setEnabled(true);
					
					if (weekMonthModelComboBox.getSelectedIndex() == 0) {
						month.setMonthModel(defaultMonthModel);
						month.setWeekModel(defaultWeekModel);
					} else if (weekMonthModelComboBox.getSelectedIndex() == 1) {
						localeLabel.setEnabled(false);
						localeComboBox.setEnabled(false);
						month.setMonthModel(latinMonthModel);
						month.setWeekModel(latinWeekModel);
					}
				}
			}
		});
		
		monthPanel.add(weekNamesVisible, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(weekNumbersVisible, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(trailingNextEnabled, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(trailingPreviousEnabled, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		monthPanel.add(weekMonthModelLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		monthPanel.add(weekMonthModelComboBox, new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(rendererLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(rendererComboBox, new GridBagConstraints(1, 8, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		monthPanel.add(dowFirstLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
//		monthPanel.add(dowFirstComboBox, new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(localeLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(localeComboBox, new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		monthPanel.add(new JLabel(), new GridBagConstraints(0, 9, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		return monthPanel;
	}
	
	/**
	 * Creates a panel that controls grid features.
	 * 
	 * @return actual panel
	 */
	private JComponent createGridPanel() {
		JPanel gridPanel = new JPanel();
		gridPanel.setLayout(new GridBagLayout());
		gridPanel.setBorder(new TitledBorder("Grid"));
		
		JLabel gridColorLabel = new JLabel("Color:");
		
		final JButton gridColorButton = new JButton(" ");
		gridColorLabel.setLabelFor(gridColorButton);
		gridColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color color = JColorChooser.showDialog(MonthDemo.this, "Choose Grid Color", month.getGridColor());
				if (color != null) {
					month.setGridColor(color);
					gridColorButton.setBackground(month.getGridColor());
				}
			}
		});
		gridColorButton.setBackground(new Color(month.getGridColor().getRGB()));
		
		final JCheckBox horizontalLinesVisible = new JCheckBox("Show Horizontal Lines", month.isHorizontalLinesVisible());
		horizontalLinesVisible.setMnemonic('h');
		horizontalLinesVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setHorizontalLinesVisible(horizontalLinesVisible.isSelected());
			}
		});
		
		final JCheckBox verticalLinesVisible = new JCheckBox("Show Vertical Lines", month.isVerticalLinesVisible());
		verticalLinesVisible.setMnemonic('v');
		verticalLinesVisible.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setVerticalLinesVisible(verticalLinesVisible.isSelected());
			}
		});
		
		gridPanel.add(verticalLinesVisible, new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(horizontalLinesVisible, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(gridColorLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(gridColorButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(0, 5, 5, 5), 0, 0));
		gridPanel.add(new JLabel(), new GridBagConstraints(0, 2, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));

		return gridPanel;
	}
	
	/**
	 * Creates a panel that controls date selection for the component.
	 * 
	 * @return actual panel
	 */
	private JComponent createDateSelectionPanel() {
		JPanel dateSelectionPanel = new JPanel();
		dateSelectionPanel.setLayout(new GridBagLayout());
		dateSelectionPanel.setBorder(new TitledBorder("Date Selection"));
		
		JLabel dateSelectionModelLabel = new JLabel("Selection Model:");
		dateSelectionModelLabel.setFont(dateSelectionModelLabel.getFont().deriveFont(Font.BOLD));
		final JComboBox dateSelectionModelComboBox = new JComboBox(new Object[] {new DefaultDateSelectionModel(), new NoWeekendDateSelectionModel()});
		dateSelectionModelComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					DateSelectionModel dsm = (DateSelectionModel)dateSelectionModelComboBox.getSelectedItem();
					month.setDateSelectionModel(dsm);
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

		final JCheckBox toggleDateSelectionEnabledCheckBox = new JCheckBox("Toggled Date Selection", month.isToggleDateSelectionEnabled());
		toggleDateSelectionEnabledCheckBox.setToolTipText("When checked, you can select multiple dates without using the CTRL key");
		toggleDateSelectionEnabledCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				month.setToggleDateSelectionEnabled(toggleDateSelectionEnabledCheckBox.isSelected());
			}
		});

		JLabel selectionTypeLabel = new JLabel("Selection Type:");		
		selectionTypeLabel.setFont(selectionTypeLabel.getFont().deriveFont(Font.BOLD));
		JComboBox selectionTypeComboBox = new JComboBox(SelectionMode.VALUES);
		selectionTypeComboBox.setToolTipText("Select date selection mode for the JMonthView component");
		selectionTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				SelectionMode item = (SelectionMode)evt.getItem();
				
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					toggleDateSelectionEnabledCheckBox.setEnabled(false);
					if (item.equals(SelectionMode.SINGLE)) {
						month.setSelectionMode(SelectionMode.SINGLE);
					} else if (item.equals(SelectionMode.SINGLE_INTERVAL)) {
						month.setSelectionMode(SelectionMode.SINGLE_INTERVAL);
					} else if (item.equals(SelectionMode.MULTIPLE_INTERVAL)) {
						toggleDateSelectionEnabledCheckBox.setEnabled(true);
						month.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL);
					}
				}
			}
		});
		selectionTypeComboBox.setSelectedItem(SelectionMode.MULTIPLE_INTERVAL);
		
		dateSelectionPanel.add(dateSelectionModelLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(dateSelectionModelComboBox, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(selectionTypeLabel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(selectionTypeComboBox, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		dateSelectionPanel.add(toggleDateSelectionEnabledCheckBox, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(0, 5, 5, 5), 0, 0));
		
		return dateSelectionPanel;
	}

	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JMonth Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		MonthDemo newContentPane = new MonthDemo();
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
/*
 * @(#)DemoApp.java
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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.standbysoft.component.date.swing.plaf.basic.BasicDatePickerUI;
import com.standbysoft.demo.date.binding.DataBindingDemo;
import com.standbysoft.demo.date.plaf.ext.PlasticComboBoxUIExt;

import edu.stanford.ejalbert.BrowserLauncher;

/**
 * This class factors the code used to start the demo application both as
 * standalone and applet.
 */
public class DemoApp {
	public static Color BLUE = new Color(41, 71, 156);
	
	public static Color ORANGE = new Color(255, 165, 0);
	
	public static void start(Container container, boolean enableSplash) {
		Splash splashScreen = null;
		
		if (enableSplash) {
			splashScreen = new Splash("com/standbysoft/demo/date/logo.jpg", 12);
			splashScreen.showSplash();
		}

		Frame frame = JOptionPane.getFrameForComponent(container);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			SwingUtilities.updateComponentTreeUI(frame);
			splashIncrease(splashScreen, 1);
		} catch (Exception ex) {
		}
		
		BasicDatePickerUI.registerDateComboBoxUI("com.jgoodies.looks.plastic.Plastic3DLookAndFeel", PlasticComboBoxUIExt.class);
		UIManager.installLookAndFeel("Plastic", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		
		splashIncrease(splashScreen, 1);
				
		DemoList demoList = new DemoList(new JTabbedPane());
		
		container.setLayout(new BorderLayout());
		container.add(createControlPanel(splashScreen, demoList), BorderLayout.NORTH);
		container.add(createDemosPanel(splashScreen, demoList), BorderLayout.CENTER);

		splashIncrease(splashScreen, 1);
		frame.pack();
		
		if (splashScreen != null) {
			splashScreen.hideSplash();
		}
		reportFocusStatus();
	}
	
	/**
	 * Creates a panel that controls system wide properties like: look and feel 
	 * and <code>UIManager</code> settings.
	 * 
	 * @param splashScreen splash screen object used to show progress when the 
	 * application is started
	 * 
	 * @return control panel for the application
	 */
	private static JComponent createControlPanel(Splash splashScreen, final DemoList demoList) {
		UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

		final JComboBox lfComboBox = new JComboBox(info);
		JLabel lfLabel = new JLabel("Look and Feel:");
		lfLabel.setDisplayedMnemonic('f');
		lfLabel.setLabelFor(lfComboBox);		
		
		for (int i = 0; i < info.length; i++) {
			if (info[i].getName() == UIManager.getLookAndFeel().getName()) {
				lfComboBox.setSelectedItem(info[i]);
				break;
			}
		}

		lfComboBox.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value != null && value instanceof UIManager.LookAndFeelInfo) {
					UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo) value;
					setText(info.getName());
				}

				return c;
			}
		});
		
		final JToggleButton toggle = new JToggleButton("Switch to Expert Mode +");
		toggle.setToolTipText("More demos are visible in expert mode");
		toggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				demoList.setExpertVisible(toggle.isSelected());
			}
		});
		lfComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Runnable changelf = new Runnable() {
					public void run() {
						Frame frame = JOptionPane.getFrameForComponent(lfComboBox);
						
						try {
							UIManager.LookAndFeelInfo info = (UIManager.LookAndFeelInfo) lfComboBox.getSelectedItem();
							if (info != null) {
								UIManager.setLookAndFeel(info.getClassName());
								SwingUtilities.updateComponentTreeUI(frame);
								
								if (!toggle.isSelected()) {
									demoList.updateExpertTabsUI();
								}
								
								frame.pack();
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							JOptionPane.showOptionDialog(frame, "The following error occurred while changing the look and feel:\n" + ex.getMessage(), "Look And Feel Error", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[] { "Dismiss" }, null);
						}
					}
				};
				SwingUtilities.invokeLater(changelf);
			}
		});
		
		JPanel lfPanel = new JPanel(new GridBagLayout());
		
		lfPanel.add(lfLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		lfPanel.add(lfComboBox, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		lfPanel.add(new JLabel(), new GridBagConstraints(2, 0, 1, 1, 1.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		lfPanel.add(toggle, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 0, 0));
		
		lfPanel.setBorder(new TitledBorder("Application Settings"));

		splashIncrease(splashScreen, 1);

		return lfPanel;
	}

	/**
	 * Creates a panel that assembles all the demo applications.
	 * 
	 * @param splashScreen splash screen object used to show progress when the 
	 * application is started
	 * 
	 * @return panel that assembles all the demo applications.
	 */
	private static JComponent createDemosPanel(Splash splashScreen, DemoList demoList) {
		demoList.add(new DemoTab("Welcome", new HtmlPagePanel("welcome.html"), "An introduction to what this application is about", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Date Picker", new DatePickerDemo(), "JDatePicker specific features", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Date Time Editing", new DateEditComponentDemo(), "JDateField and JDatePicker common features", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Date Restriction", new DateComponentDemo(), "Basic features common to all date components", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Month Customization", new MonthDemo(), "JMonth and JMonthView common features", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Calendar Customization", new MonthViewDemo(), "JMonthView specific features", false));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("Week Selection +", new WeekDemo(), "Week selection for JMonth components", true));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("JGoodies Binding +", new DataBindingDemo(), "Integration with JGoodies data binding framework", true));
		splashIncrease(splashScreen, 1);
		demoList.add(new DemoTab("About", new HtmlPagePanel("about.html"), "What is Java Date Picker", false));
		
		return demoList.getComp();
	}
	
	private static void splashIncrease(Splash splash, int step) {
		if (splash != null) {
			splash.increaseStep(step);
		}
	}
	
	private static void reportFocusStatus() {
//		java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager()
//	        .addPropertyChangeListener(new PropertyChangeListener() {
//	        public void propertyChange(PropertyChangeEvent evt) {
//	            Component oldComp = (Component)evt.getOldValue();
//	            Component newComp = (Component)evt.getNewValue();
//	    
//	            if ("focusOwner".equals(evt.getPropertyName())) {
//	                if (oldComp == null) {
//	                	System.out.println("f g " + newComp);
//	                    // the newComp component gained the focus
//	                } else {
//	                	System.out.println("f l " + oldComp);
//	                    // the oldComp component lost the focus
//	                }
//	            } else if ("focusedWindow".equals(evt.getPropertyName())) {
//	                if (oldComp == null) {
//	                	System.out.println("fw g " + newComp);
//	                    // the newComp window gained the focus
//	                } else {
//	                	System.out.println("fw l " + oldComp);
//	                    // the oldComp window lost the focus
//	                }
//	            }
//	        }
//	    });
	}
}

/**
 * Displays information from a specified HTML file.
 */
class HtmlPagePanel extends JPanel {
	public HtmlPagePanel(String page) {
		JTextPane message = new JTextPane();
		message.setContentType("text/html");
		try {
			message.setPage(getClass().getResource(page));
		} catch (Exception e) {
			message.setText("error");
		}
		
		message.setEditable(false);
		message.setAutoscrolls(true);
		
		message.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent evt) {
				if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try {
						BrowserLauncher.openURL(evt.getURL().toExternalForm());
					} catch (IOException e) {
					}
				}
			}
		});

		setLayout(new BorderLayout());
		JScrollPane pane = new JScrollPane(message);
		pane.getViewport().setPreferredSize(new Dimension(100, 100));
		add(pane, BorderLayout.CENTER);
	}
}

/**
 * A convenient representation for a demo tab.
 */
class DemoTab {
	/**
	 * Name of the tab.
	 */
	private String name;
	
	/**
	 * Component that represents the tab.
	 */
	private JComponent component;
	
	/**
	 * Tab description.
	 */
	private String description;
	
	/**
	 * Whether the tab is a basic or expert one.
	 */
	private boolean expert;
	
	public DemoTab(String name, JComponent comp, String description, boolean expert) {
		this.component = comp;
		this.name = name;
		this.description = description;
		this.expert = expert;
	}
	
	public JComponent getComponent() {
		return component;
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean isExpert() {
		return expert;
	}
	
	public String getName() {
		return name;
	}
}

/**
 * A convenient wrapper that manages the tabs of the demo.
 */
class DemoList {
	private List tabs;
	
	private JTabbedPane pane;
	
	private boolean visible;
	
	public DemoList(JTabbedPane pane) {
		this.pane = pane;
		tabs = new ArrayList();
		visible = false;
	}
	
	public JComponent getComp() {
		return pane;
	}
	
	public void add(DemoTab tab) {
		tabs.add(tab);
		if (tab.isExpert()) {
			if (visible) {
				pane.addTab(tab.getName(), null, tab.getComponent(), tab.getDescription());
			}
		} else {
			pane.addTab(tab.getName(), null, tab.getComponent(), tab.getDescription());
		}
	}
	
	public void updateExpertTabsUI () throws Exception {
		for (int i = 0; i < tabs.size(); i++) {
			DemoTab tab = (DemoTab)tabs.get(i);
				
			if (tab.isExpert()) {
				SwingUtilities.updateComponentTreeUI(tab.getComponent());
			}
		}
	}
	
	public void setExpertVisible(boolean visible) {
		if (this.visible == visible) {
			return;
		} else {
			this.visible = visible;
		}
		
		for (int i = 0; i < tabs.size(); i++) {
			DemoTab tab = (DemoTab)tabs.get(i);
			
			if (tab.isExpert()) {
				if (visible) {
					int index = pane.getComponentCount() - 1;
					pane.insertTab(tab.getName(), null, tab.getComponent(), tab.getDescription(), index);
					tab.getComponent().repaint();
				} else {
					pane.remove(tab.getComponent());
				}
			}
		}
	}
}
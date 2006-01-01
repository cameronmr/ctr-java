/*
 * @(#)DataBindingDemo.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.binding;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.standbysoft.component.date.swing.JDateField;
import com.standbysoft.component.date.swing.JDatePicker;
import com.standbysoft.component.date.swing.binding.DateEditComponentAdapter;

/**
 * Shows how the date edit components can be used with JGoodies Data Bindings framework.
 */
public class DataBindingDemo extends JPanel {
    /**
     * Holds the edited task and vends ValueModels that adapt book properties.
     */
    private final PresentationModel presentationModel;
    
    private JTextArea messageTextArea;
	
	public DataBindingDemo() {
		Task task = Task.TASK1;
        this.presentationModel = new PresentationModel(task);
        
        // Observe changes in all bound task properties.
        task.addPropertyChangeListener(new BookPropertyChangeListener());
		
		setLayout(new BorderLayout());
		add(createControlPanel(), BorderLayout.CENTER);
	}
	
	private JPanel createControlPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		JLabel nameLabel = new JLabel("Task:", JLabel.RIGHT);
		JTextField nameTextField = BasicComponentFactory.createTextField(
                presentationModel.getModel(Task.PROPERTYNAME_NAME));
		
		JLabel startLabel = new JLabel("Start Date:", JLabel.RIGHT);
		JDateField startDateField = new JDateField();
        DateEditComponentAdapter.bind(startDateField, presentationModel.getModel(Task.PROPERTYNAME_START_DATE));
		
		JLabel endLabel = new JLabel("End Date:", JLabel.RIGHT);
		JDatePicker endDatePicker = new JDatePicker();
		endDatePicker.setEditable(true);
        DateEditComponentAdapter.bind(endDatePicker, presentationModel.getModel(Task.PROPERTYNAME_END_DATE));
		
		JButton resetButton = new JButton("Reset Dates");
		resetButton.setToolTipText("Change the dates by acting directly on the model");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				presentationModel.getModel(Task.PROPERTYNAME_START_DATE).setValue(new GregorianCalendar(2000, 2, 2).getTime());
				presentationModel.getModel(Task.PROPERTYNAME_END_DATE).setValue(new GregorianCalendar(2000, 2, 2).getTime());
			}
		});
		
		messageTextArea = new JTextArea();
		messageTextArea.setColumns(40);
		messageTextArea.setRows(15);
		messageTextArea.setEditable(false);		
		
		JButton clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				messageTextArea.setText("");
			}
		});
		
		panel.add(nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		panel.add(nameTextField, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		
		panel.add(startLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		panel.add(startDateField, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		
		panel.add(endLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		panel.add(endDatePicker, new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		
		panel.add(resetButton, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 0, 5), 0, 0));
		
		panel.add(new JScrollPane(messageTextArea), new GridBagConstraints(1, 4, 2, 1, 1.0, 1.0, GridBagConstraints.NORTH, GridBagConstraints.BOTH, new Insets(8, 5, 0, 5), 0, 0));
		panel.add(clearButton, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 10, 5), 0, 0));		
		panel.add(new JLabel(), new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL, new Insets(8, 5, 10, 5), 0, 0));

		return panel;
	}
    
    // Listens to all book changes and write the book string to the console.
    private class BookPropertyChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
        	messageTextArea.append(
                    "Changed '" + evt.getPropertyName() + 
                    "' from '" + evt.getOldValue() + 
                    "' to '" + evt.getNewValue() + "'.\n");
        	messageTextArea.append(evt.getSource() + "\n\n");
        }
    }
	
	private static void createAndShowGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("JGoodies Data Binding Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		DataBindingDemo newContentPane = new DataBindingDemo();
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
/*
 * @(#)Main.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * The entry point for the Java Date Picker standalone demo application.
 */
public class Main {
	/**
	 * Name of the product.
	 */
	public static final String NAME = "Java Date Picker";

	/**
	 * Product tag line.
	 */
	public static final String MESSAGE = "Professional date components for Swing";

	/**
	 * Centers a given frame on the screen.
	 * 
	 * @param frame frame to be centered
	 */
	private static void center(JFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension size = frame.getSize();
		screenSize.height = screenSize.height / 2;
		screenSize.width = screenSize.width / 2;
		size.height = size.height / 2;
		size.width = size.width / 2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		frame.setLocation(x, y);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame(NAME + " - " + MESSAGE);

		DemoApp.start(frame.getContentPane(), true);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		center(frame);
		frame.setVisible(true);
	}
}
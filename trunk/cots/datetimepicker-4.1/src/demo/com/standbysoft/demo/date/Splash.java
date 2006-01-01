/*
 * @(#)Splash.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.SwingUtilities;

/**
 * Splash screen to be used when an application is launched.
 */
public final class Splash {
	private Frame parent;
	
	private MediaTracker mediaTracker;

	private Image image;
	
	private SplashWindow window ;
	
	private int steps;

	private int step;

	/**
	 * Creates a splash screen from an image URL.
	 * 
	 * @param path path of the image displayed in the splash screen
	 */
	public Splash(String path, int steps) {
		this.steps = steps;
		parent = new Frame();
		mediaTracker = new MediaTracker(parent);
		image = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource(path));
	}

	/**
	 * Show the actual splash screen.
	 */
	public void showSplash() {
		parent.setSize(image.getWidth(null), image.getHeight(null));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle frame = parent.getBounds();
		parent.setLocation((screen.width - frame.width) / 2, (screen.height - frame.height) / 2);

		mediaTracker.addImage(image, 0);
		try {
			mediaTracker.waitForID(0);
		} catch (InterruptedException ie) {
		}

		window = new SplashWindow(parent, image);
	}
	
	/**
	 * Hide the splash screen.
	 */
	public void hideSplash() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                parent.dispose();
            }
        });
	}
	
	public void increaseStep() {
		step++;
		window.setRatio(((float)step)/steps);
		window.repaint();
	}
	
	public void increaseStep(int delta) {
		step += delta;
		window.setRatio(((float)step)/steps);
		window.repaint();
	}

	private class SplashWindow extends Window {
		private Image image;
		private Color bgcolor = new Color(42, 72, 156);
		private Color color = new Color(255, 165, 0);
		private int thick = 3;
		private int extraHeight = 20;
		private int indent = 8;
		private float ratio;
		
		SplashWindow(Frame parent, Image aImage) {
			super(parent);
			ratio = 0;
			this.image = aImage;
			setSize(image.getWidth(null), image.getHeight(null) + extraHeight);
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle window = getBounds();
			setLocation((screen.width - window.width) / 2, (screen.height - window.height) / 2);
			setVisible(true);
		}
		
		public void setRatio(float ratio) {
			this.ratio = ratio;
		}

		public void repaint() {
			super.repaint(indent, getHeight()- extraHeight / 2 - thick, indent + (int)((getWidth() - 2 * indent)* ratio), getHeight() - extraHeight / 2 - thick);
		}
		
		public void paint(Graphics graphics) {
			if (image != null) {
			    Graphics2D g2d = (Graphics2D)graphics;
				g2d.drawImage(image, 0, 0, this);
				
				g2d.setColor(bgcolor);
				g2d.fillRect(0, getHeight() - extraHeight, getWidth(), extraHeight);

				g2d.setStroke(new BasicStroke(thick));				

				g2d.setColor(Color.WHITE);
				g2d.drawLine(indent, getHeight()- extraHeight / 2, indent + (int)(getWidth() - 2 * indent), getHeight() - extraHeight / 2);
				
				g2d.setColor(color);
				g2d.drawLine(indent, getHeight()- extraHeight / 2, indent + (int)((getWidth() - 2 * indent) * ratio), getHeight() - extraHeight / 2);
			}
		}
	}
}
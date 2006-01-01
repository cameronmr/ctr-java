/*
 * @(#)MetalIconComboBoxUIExt.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.plaf.ext;

import java.awt.Insets;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.plaf.metal.MetalComboBoxButton;

import com.standbysoft.component.util.swing.MetalComboBoxUIExt;

/**
 * An extension for the ComboBoxUI that if registered on the JDatePicker will
 * replace its combo box arrow with an icon.
 */
public class MetalIconComboBoxUIExt extends MetalComboBoxUIExt {
	protected JButton createArrowButton() {
        URL image = ClassLoader.getSystemClassLoader().getResource("com/standbysoft/demo/date/plaf/bean.gif");
        JButton button = new MetalComboBoxButton(comboBox, new ImageIcon(image), false, this.currentValuePane, this.listBox);
        button.setMargin( new Insets( 1, 1, 1, 1 ) );
        
        return button;
     }
}
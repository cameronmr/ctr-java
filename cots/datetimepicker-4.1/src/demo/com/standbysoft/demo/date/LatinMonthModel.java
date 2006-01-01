/*
 * @(#)LatinMonthModel.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import com.standbysoft.component.date.swing.DefaultMonthModel;

/**
 * A month model with month names in Latin.
 */
public class LatinMonthModel extends DefaultMonthModel {
	/**
	 * The month names in Latin.
	 */
	private String[] names = {"Ianuarius", "Februarius", "Martius", "Aprilis", "Maius", "Iunius", "Iulius", "Augustus", "September", "October", "November", "December"};
	
	public String[] getMonthNames() {
		return names;
	}
}
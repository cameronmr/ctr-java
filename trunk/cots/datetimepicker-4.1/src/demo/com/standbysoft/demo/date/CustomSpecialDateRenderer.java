/*
 * @(#)CustomSpecialDateRenderer.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date;

import com.standbysoft.demo.date.renderer.CompositeDateRenderer;
import com.standbysoft.demo.date.renderer.DisabledDateRenderer;
import com.standbysoft.demo.date.renderer.RegularDateRenderer;
import com.standbysoft.demo.date.renderer.SelectedDateRenderer;
import com.standbysoft.demo.date.renderer.SpecialDateRenderer;
import com.standbysoft.demo.date.renderer.TodayDateRenderer;
import com.standbysoft.demo.date.renderer.TrailingDateRenderer;

/**
 * Date renderer that highlights some dates (1, 11, 21, 31) as company dates.
 */
public class CustomSpecialDateRenderer extends CompositeDateRenderer {
	public CustomSpecialDateRenderer() {
		renderers.add(new RegularDateRenderer());
		renderers.add(new TrailingDateRenderer());
		renderers.add(new SpecialDateRenderer());
		renderers.add(new DisabledDateRenderer());
		renderers.add(new SelectedDateRenderer());
		renderers.add(new TodayDateRenderer());
	}
}
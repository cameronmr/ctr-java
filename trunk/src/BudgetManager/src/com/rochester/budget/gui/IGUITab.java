/*
 * IGUITab.java
 *
 * Created on 20 December 2005, 19:46
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import javax.swing.event.ChangeListener;

/**
 *
 * @author Cam
 */
public interface IGUITab extends ChangeListener
{
    String getTabTitle();
}

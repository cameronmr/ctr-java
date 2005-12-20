/*
 * CategoryStatsPanel.java
 *
 * Created on 20 December 2005, 20:39
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author Cam
 */
public class CategoryStatsPanel implements IGUIComponent
{
    
    /** Creates a new instance of CategoryStatsPanel */
    public CategoryStatsPanel()
    {
    }
        
    public Component getComponent()
    {
        return m_statsPanel;
    }
    
    private JPanel m_statsPanel = new JPanel();    
}

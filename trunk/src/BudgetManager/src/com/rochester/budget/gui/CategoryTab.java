/*
 * CategoryTab.java
 *
 * Created on 20 December 2005, 19:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Cam
 */
public class CategoryTab implements IGUIComponent, IGUITab
{
    
    /** Creates a new instance of CategoryTab */
    public CategoryTab()
    {
        m_theTreePanel = new CategoryTreePanel();
        CategoryDetailsPanel detailsPanel = new CategoryDetailsPanel();
        CategoryStatsPanel statsPanel = new CategoryStatsPanel();
        JSplitPane catVPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane catHPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        catHPane.setLeftComponent( m_theTreePanel.getComponent() );
        catHPane.setRightComponent( detailsPanel.getComponent() );
        catVPane.setTopComponent( catHPane );
        catVPane.setBottomComponent( statsPanel.getComponent() );
        catHPane.setDividerLocation( 0.30 );
        catVPane.setDividerLocation( 0.5 );
        
        m_theComponent = catVPane;
    }
    
    public String getTabTitle()
    {
        return "Categories";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // Selection has changed
        JTabbedPane pane = (JTabbedPane)evt.getSource();
        
        if ( pane.getSelectedComponent() == m_theComponent )
        {
            m_theTreePanel.stateChanged( evt );
        }
    }
    
    private Component m_theComponent = null;
    private CategoryTreePanel m_theTreePanel = null;
}

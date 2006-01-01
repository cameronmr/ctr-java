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

import com.rochester.budget.core.CategoryDetailsModel;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.gui.CategoryTreePanel.CategoryNode;
import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 *
 * @author Cam
 */
public class CategoryTab implements IGUIComponent, IGUITab, TreeSelectionListener
{
    
    /** Creates a new instance of CategoryTab */
    public CategoryTab()
    {
        m_theTreePanel = new CategoryTreePanel();
        m_theTreePanel.addTreeSelectionListener( this );
        m_detailsPanel = new DetailsPanel();
        CategoryStatsPanel statsPanel = new CategoryStatsPanel();
        JSplitPane catVPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        JSplitPane catHPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        catHPane.setLeftComponent( m_theTreePanel.getComponent() );
        catHPane.setRightComponent( m_detailsPanel.getComponent() );
        catVPane.setTopComponent( catHPane );
        catVPane.setBottomComponent( statsPanel.getComponent() );
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
            m_theTreePanel.setVisible( );
        }
    }
    
    public void valueChanged( TreeSelectionEvent e ) 
    {
        // Convert the event into a category to update the Details View
        JTree tree = (JTree)e.getSource();
        
        Object select = tree.getLastSelectedPathComponent();
        if ( null != select )
        {
            CategoryNode parentNode = (CategoryNode)(tree.getLastSelectedPathComponent());

            CategoryDetailsModel model = new CategoryDetailsModel( parentNode.getCategory() );
            m_detailsPanel.updateView( model );
        }
    }
    
    private Component m_theComponent = null;
    private CategoryTreePanel m_theTreePanel = null;
    private DetailsPanel m_detailsPanel = null;
}

/*
 * StatementTab.java
 *
 * Created on 20 December 2005, 20:13
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IStatement;
import java.awt.Component;
import javax.swing.JList;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class StatementTab implements IGUITab, IGUIComponent, ListSelectionListener
{
    
    /** Creates a new instance of StatementTab */
    public StatementTab()
    {
        m_statementList = new StatementListPanel();
        m_statementList.addListSelectionListener( this );
        m_summaryPanel = new StatementSummaryPanel();
        
        JSplitPane split = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        
        split.setLeftComponent( m_statementList.getComponent() );
        split.setRightComponent( m_summaryPanel.getComponent() );
        
        m_theComponent = split;
    }
    
    public String getTabTitle()
    {
        return "Statements";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // Tab selection has changed
    }
    
    public void valueChanged( ListSelectionEvent e ) 
    {
        // Statement List selection has changed
        if ( e.getValueIsAdjusting() ) return;
        
        // Convert the value changed into an update on the ListDetailPanel
        JList source = (JList)e.getSource();
        
        // Single selection only. Returns null if none selected
        m_summaryPanel.updateView( (IStatement)source.getSelectedValue() );
    }    
    
    private Component m_theComponent;
    private StatementSummaryPanel m_summaryPanel;
    private StatementListPanel m_statementList;
}

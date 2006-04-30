/*
 * ReconciliationTab.java
 *
 * Created on 20 December 2005, 19:53
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IGUIComponent;
import java.awt.Component;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author Cam
 */
public class ReconciliationTab implements IGUIComponent, IGUITab
{
    
    /** Creates a new instance of ReconciliationTab */
    public ReconciliationTab()
    { 
        ReconciliationPanel reconciliationPanel = new ReconciliationPanel();  
        m_transactionPanel = new TransactionPanel();         
        
        /* The ReconciliationPanel listens for changes to transaction selection */   
        m_transactionPanel.addActionListener( reconciliationPanel );
        
        /* The TransactionPanel listens for changes to reconciliation state */
        reconciliationPanel.addActionListener( m_transactionPanel );
        
        JSplitPane reconPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT );
        reconPane.setTopComponent( reconciliationPanel );
        reconPane.setBottomComponent( m_transactionPanel.getComponent() );                
        
        m_theComponent = reconPane;      
    }
    
    public String getTabTitle()
    {
        return "Transactions";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        JTabbedPane pane = (JTabbedPane)evt.getSource();
        
        if ( pane.getSelectedComponent() == m_theComponent )
        {
            // Add the reconciliation
            m_transactionPanel.viewGained();
        }
        else
        {
            // Remove the reconciliation
            m_transactionPanel.viewLost();
        }
    }
    
    private JSplitPane m_theComponent = null;
    private TransactionPanel m_transactionPanel = null;
}

/*
 * AccountTab.java
 *
 * Created on 20 December 2005, 20:17
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.IAccount;
import com.rochester.budget.core.IGUIComponent;
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
public class AccountTab implements IGUITab, IGUIComponent, ListSelectionListener
{
    
    /** Creates a new instance of AccountTab */
    public AccountTab()
    {
        AccountListPanel accountList = new AccountListPanel();
        accountList.addListSelectionListener( this );
        m_details = new AccountDetailsPanel();
        
        JSplitPane split1 = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT );
        split1.setLeftComponent( accountList.getComponent() );
        split1.setRightComponent( m_details.getComponent() );
        split1.setDividerLocation( 0.30 );
        
        m_theComponent = split1;
    }
    
    public String getTabTitle()
    {
        return "Accounts";
    }
    
    public Component getComponent()
    {
        return m_theComponent;
    }
    
    public void stateChanged( ChangeEvent evt ) 
    {
        // Selection has changed
    }
        
    public void valueChanged( ListSelectionEvent e ) 
    {
        // Convert the value changed into an update on the ListDetailPanel
        JList source = (JList)e.getSource();
        
        // Single selection only. Returns null if none selected
        m_details.displayAccountDetails( (IAccount) source.getSelectedValue() );
    }
    
    private Component m_theComponent = null;
    private AccountDetailsPanel m_details = null;
}

/*
 * AccountListPanel.java
 *
 * Created on 20 December 2005, 20:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.Comparators;
import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IAccount;
import com.rochester.budget.core.IGUIComponent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class AccountListPanel implements IGUIComponent
{
    /** Creates a new instance of AccountListPanel */
    public AccountListPanel()
    {
        // Load the account list
        m_accountModel = new GenericListModel<IAccount>( DataObjectFactory.loadAccounts() );
        m_accountModel.setComparator( Comparators.ALPHABETICAL_ORDER );
        
        m_accountList = new JList( m_accountModel );
        m_accountList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );        
        
        JScrollPane scrollPane = new JScrollPane( m_accountList );
        scrollPane.setColumnHeaderView( new JLabel( "Accounts", JLabel.CENTER ) );
        m_accountPanel.add( scrollPane, BorderLayout.CENTER );
        m_accountPanel.setPreferredSize( new Dimension( 180, -1 ) );
        
        // New Account Button
        JButton newAccount = new JButton( "New Account" );
        newAccount.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                m_accountList.clearSelection();
                // do something
                IAccount account = DataObjectFactory.newAccount();
                m_accountModel.addItem( account );
                
                m_accountList.setSelectedValue( account, true );
            }
        });
        
        m_accountPanel.add( newAccount, BorderLayout.SOUTH );
        
        m_accountPanel.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
    }    
    
    public Component getComponent()
    {
        return m_accountPanel;
    }
    
    public void addListSelectionListener( ListSelectionListener listener )
    {
        m_accountList.addListSelectionListener( listener );
    }
            
    private JPanel m_accountPanel = new JPanel( new BorderLayout( 5,5 ) );
    private JList m_accountList;
    private GenericListModel<IAccount> m_accountModel;
}

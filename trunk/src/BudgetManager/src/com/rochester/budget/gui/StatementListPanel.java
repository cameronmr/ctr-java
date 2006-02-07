/*
 * StatementListPanel.java
 *
 * Created on 20 December 2005, 20:24
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.Comparators;
import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericComboBoxModel;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IAccount;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IStatement;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class StatementListPanel implements IGUIComponent
{
    
    /** Creates a new instance of StatementListPanel */
    public StatementListPanel()
    {
        // Load the statement list
        m_statementModel = new GenericListModel<IStatement>( );
        m_statementModel.setComparator( Comparators.STATEMENT_DATE_ORDER_ASC );
        
        m_statementList = new JList( m_statementModel );
        m_statementList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
        GenericComboBoxModel< IAccount > accountModel =
                new GenericComboBoxModel<IAccount>( DataObjectFactory.loadAccounts() );
        
        // Sort the model before adding it
        accountModel.setComparator( Comparators.ALPHABETICAL_ORDER );
        m_accountCombo = new JComboBox( accountModel );        
        m_accountCombo.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                IAccount account = (IAccount)m_accountCombo.getSelectedItem();
                
                // Update the statement list
                m_statementModel.setItems( DataObjectFactory.loadStatementsForAccount( account ) );
            }
        });
        
        m_accountCombo.setSelectedIndex( 0 );
        
        m_statementPanel.add( m_accountCombo, BorderLayout.NORTH );
        JScrollPane scrollPane = new JScrollPane( m_statementList );
        scrollPane.setColumnHeaderView( new JLabel( "Statements", JLabel.CENTER ) );
        m_statementPanel.add( scrollPane, BorderLayout.CENTER );
        m_statementPanel.setPreferredSize( new Dimension( 180, -1 ) );
        
        // New Account Button
        JButton newAccount = new JButton( "New Statement" );
        newAccount.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                m_statementList.clearSelection();
                
                // do something
                IStatement statement = DataObjectFactory.newStatement( (IAccount)m_accountCombo.getSelectedItem() );
                m_statementModel.addItem( statement );
                
                m_statementList.setSelectedValue( statement, true );
            }
        });
        
        m_statementPanel.add( newAccount, BorderLayout.SOUTH );
        
        m_statementPanel.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
    }
    
    public Component getComponent()
    {
        return m_statementPanel;
    }
        
    public void addListSelectionListener( ListSelectionListener listener )
    {
        m_statementList.addListSelectionListener( listener );
    }
    
    private JPanel m_statementPanel = new JPanel( new BorderLayout( 5,5 ) );
    private JList m_statementList;
    private JComboBox m_accountCombo;
    private GenericListModel<IStatement> m_statementModel;
}

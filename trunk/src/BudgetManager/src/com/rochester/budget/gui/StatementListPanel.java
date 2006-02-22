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
import com.rochester.budget.core.IStatement;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 *
 * @author Cam
 */
public class StatementListPanel extends AbstractListPanel<IStatement>
{
    
    /** Creates a new instance of StatementListPanel */
    public StatementListPanel()
    {
        super( new GenericListModel<IStatement>( ), "Statements" );
        
        // Create the account combo box
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
                setItems( DataObjectFactory.loadStatementsForAccount( account ) );
            }
        });
        
        if ( accountModel.getSize() != 0 )
        {
            m_accountCombo.setSelectedIndex( 0 );
        }
        
        m_thePanel.add( m_accountCombo, BorderLayout.NORTH );
        m_thePanel.add( super.getComponent(), BorderLayout.CENTER );
    }
    
    public IStatement onNew( )
    {
        return DataObjectFactory.newStatement( (IAccount)m_accountCombo.getSelectedItem() );
    }
    
    public Component getComponent()
    {
        // Override the default implementation so we can get the layout we want
        return m_thePanel;
    }
        
    private JComboBox m_accountCombo;
    private JPanel m_thePanel = new JPanel( new BorderLayout() );
}

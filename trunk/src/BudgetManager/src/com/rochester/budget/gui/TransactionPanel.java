/*
 * TransactionPanel.java
 *
 * Created on 3 July 2005, 20:01
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.TransactionTableModel;
import java.awt.Component;
import com.rochester.budget.core.IGUIComponent;
import com.rochester.budget.core.IRule;
import com.rochester.budget.core.ITransaction;
import com.rochester.budget.core.SelectTransactionTableModel;
import java.awt.AWTEventMulticaster;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class TransactionPanel implements IGUIComponent, ActionListener, IBudgetActionPublisher
{
    
    /** Creates a new instance of TransactionPanel */
    public TransactionPanel( )
    {        
        initComponents();        
    }
    
    private void initComponents()
    { 
        m_transactionModel = new TransactionTableModel( DataObjectFactory.loadTransactions( ) );
        m_transactionTable = new TransactionTable( m_transactionModel );
        m_scrollPane = new JScrollPane( m_transactionTable );
        
        ListSelectionModel rowSM = m_transactionTable.getSelectionModel();
        rowSM.addListSelectionListener( new ListSelectionListener()
        {
            public void valueChanged( ListSelectionEvent e )
            {
                //Ignore extra messages.
                if ( e.getValueIsAdjusting() ) return;
                
                ListSelectionModel lsm =
                    (ListSelectionModel)e.getSource();
                
                if ( lsm.isSelectionEmpty() )
                {
                    // Remove the selected transaction
                    fireActionEvent( TRANS_SELECTION_CHANGED, null );
                }
                else 
                {
                    // Set the selected transaction
                    try
                    {
                        fireActionEvent( TRANS_SELECTION_CHANGED, m_transactionModel.getTransactionAt( lsm.getMinSelectionIndex() ) );
                        m_transactionModel.fireTableRowsUpdated( m_currentRow, m_currentRow );
                        m_currentRow = lsm.getMinSelectionIndex();
                    }
                    catch ( Exception ex )
                    {
                        JOptionPane.showMessageDialog( null, ex.toString() );
                        
                        lsm.setValueIsAdjusting( true );
                        
                        // The item is in a valid state so go back to the previous selection
                        m_transactionTable.setRowSelectionInterval( m_currentRow, m_currentRow );
                    }
                }
            }
        });
        m_thePanel.add( m_scrollPane, BorderLayout.CENTER );
                
         // Apply Rule Button
        m_applyRule = new JButton( "Apply Rule" );
        m_applyRule.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent evt )
            {
                // Build the list of Actions
                Collection<IRule> rules = DataObjectFactory.loadRules();
                
                // Add the actions to the popupMenu
                JPopupMenu popupMenu = new JPopupMenu();
                for ( final IRule rule : rules )
                {
                    Action anAction = new AbstractAction( rule.getName() )
                    {
                        public void actionPerformed( ActionEvent e )
                        {
                            // Popup menu
                            ruleSelected( m_rule );
                        }
                        
                        private IRule m_rule = rule;
                    };
                    popupMenu.add( anAction );
                }
                
                // Popup the rule selection list
                popupMenu.show( m_applyRule, m_applyRule.getWidth(), m_applyRule.getHeight() );
            }
        });
        
        JPanel topButtonPanel = new JPanel( );
        
        JPanel bottomButtonPanel = new JPanel( );
        bottomButtonPanel.setLayout( new BoxLayout( bottomButtonPanel, BoxLayout.LINE_AXIS ) );        
        topButtonPanel.setLayout( new BoxLayout( topButtonPanel, BoxLayout.LINE_AXIS ) );
        // Make the panel stick to the right side of the window
        bottomButtonPanel.add( Box.createHorizontalGlue() );
        bottomButtonPanel.add( m_applyRule );

        m_thePanel.add( topButtonPanel, BorderLayout.NORTH );
        m_thePanel.add( bottomButtonPanel, BorderLayout.SOUTH );
        m_thePanel.setBorder( BorderFactory.createTitledBorder( "" ) );
    }
    
    private void ruleSelected( IRule rule )
    {        
        // Transactions are only the unreconciled transactions
        Collection<ITransaction> transactions = m_transactionModel.getUnreconciledTransactions();
        Collection<ITransaction> matchedTransactions = new ArrayList<ITransaction>();
        
        for ( ITransaction transaction : transactions )
        {
            if ( rule.matchTransaction(transaction) )
            {
                matchedTransactions.add( transaction );
            }
        }        
        
        String str = new String( "<HTML>The following " + 
                Integer.toString( matchedTransactions.size() ) + 
                " transactions matched your selected filter (" +
                rule.toString() + 
                ").<BR> Please verify the selected transactions and press OK to apply the rule.</HTML>" );
        
        JPanel panel = new JPanel( new BorderLayout( 5, 5 ) );
        panel.add( new JLabel( str ) , BorderLayout.NORTH );
        SelectTransactionTableModel model = new SelectTransactionTableModel( matchedTransactions );
        TransactionTable table = new TransactionTable( model );
        JScrollPane pane = new JScrollPane( table );
        panel.add( pane, BorderLayout.SOUTH );
        
        // Popup the rule dialog with the list of matching transactions
        if ( JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog( 
                null, panel, "Matching Transactions", JOptionPane.OK_CANCEL_OPTION ) ) 
        {
            // Apply the rule
            try
            {
                model.applyRule( rule );
            }
            catch ( Exception e )
            {
                JOptionPane.showMessageDialog( null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE );
            }
            
            m_transactionModel.filterAndSort();
            m_thePanel.validate();
        }
    }
    
    public void actionPerformed( ActionEvent e )
    {
        // Constants defined in IBudgetActionPublisher
        switch ( e.getID() )
        {
            case NEXT_TRANSACTION:
                // Select the next row
                m_transactionTable.changeSelection( m_transactionTable.getSelectedRow() + 1 , 0, false, false );
                break;
            case PREV_TRANSACTION:
                // Select the previous row
                m_transactionTable.changeSelection( m_transactionTable.getSelectedRow() - 1 , 0, false, false );
                break;
        }
    }
    
    public Component getComponent()
    {    
        return m_thePanel;
    }
    
    public synchronized void addActionListener(ActionListener l)
    {   
        m_subscribers = AWTEventMulticaster.add(m_subscribers, l);
    }
    public synchronized void removeActionListener(ActionListener l)
    {   
        m_subscribers = AWTEventMulticaster.remove(m_subscribers, l);
    }
    private void fireActionEvent( int id, Object object )
    {   
        if (m_subscribers != null)
        {
            m_subscribers.actionPerformed(new ActionEvent(object, id, "") );
        }
    }
    
    private TransactionTable m_transactionTable;
    private TransactionTableModel m_transactionModel;
    private JPanel m_thePanel = new JPanel( new BorderLayout( 2, 2 ) );
    private JButton m_applyRule;
    private JScrollPane m_scrollPane;
    private int m_currentRow;
    private ActionListener m_subscribers = null;    
}

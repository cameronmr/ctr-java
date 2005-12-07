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

import com.rochester.budget.core.TransactionTableModel;
import java.awt.Component;
import com.rochester.budget.core.IGUIComponent;
import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
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
        m_transactionModel = new TransactionTableModel();
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
                    fireActionEvent( 3, null );
                }
                else 
                {
                    // Set the selected transaction
                    try
                    {
                        fireActionEvent( 3, m_transactionModel.getTransactionAt( lsm.getMinSelectionIndex() ) );
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
        return m_scrollPane;
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
    private JScrollPane m_scrollPane;
    private int m_currentRow;
    private ActionListener m_subscribers = null;    
}

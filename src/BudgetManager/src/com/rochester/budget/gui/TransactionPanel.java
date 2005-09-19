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
import java.util.Observable;
import java.util.Observer;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cam
 */
public class TransactionPanel extends Observable implements IGUIComponent
{
    
    /** Creates a new instance of TransactionPanel */
    public TransactionPanel( Observer observer )
    {        
        addObserver( observer );
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
                
                setChanged();

                ListSelectionModel lsm =
                    (ListSelectionModel)e.getSource();
                
                if ( lsm.isSelectionEmpty() )
                {
                    // Remove the selected transaction
                    notifyObservers( null );
                }
                else 
                {
                    // Set the selected transaction
                    notifyObservers( m_transactionModel.getTransactionAt( lsm.getMinSelectionIndex() ) );
                }
            }
        });
        
        // Select the first row at startup
        m_transactionTable.setRowSelectionInterval( 0, 0 );
    }
    
    public Component getComponent()
    {    
        return m_scrollPane;
    }
    
    TransactionTable m_transactionTable;
    TransactionTableModel m_transactionModel;
    JScrollPane m_scrollPane;
}

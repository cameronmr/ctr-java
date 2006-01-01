/*
 * TransactionTableModel.java
 *
 * Created on 29 June 2005, 20:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cam
 */
public class TransactionTableModel extends AbstractTableModel implements IDataChangeObserver
{
    public TransactionTableModel( )
    {
        
    }
    
    public TransactionTableModel( Collection<ITransaction> transactions )
    {
        setTransactions(transactions);
    }
    
    public void setTransactions( Collection<ITransaction> transactions )
    {
        // remove any observerss to the transactions
        for ( ITransaction trans : m_transactions )
        {
            trans.deleteObserver( this );
        }
        
        m_transactions.clear();
        
        m_transactions.addAll( transactions );
        
        Collections.sort( m_transactions, Comparators.TRANSACTION_DATE_ORDER );        
        
        // Observe any changes to the transactions
        for ( ITransaction trans : m_transactions )
        {
            trans.addObserver( this );
        }
    }
    
    public int getColumnCount() 
    {
        return m_columns.length;
    }

    public int getRowCount() 
    {
        return m_transactions.size();
    }

    public String getColumnName(int col) 
    {
        return m_columns[col];
    }

    public Object getValueAt( int row, int col )
    {
        ITransaction trans = m_transactions.get( row );
        
        switch ( col )
        {
            case 0:
                return trans.getReconciliationState();
            case 1:
                return trans.getDate();
            case 2:
                return trans.getAccount();
            case 3:
                return trans.getNarrative();
            case 4:
                return trans.getMonetaryValue();
        }
        return null;
    }

    public Class getColumnClass(int c)
    {
        switch ( c )
        {
            case 0:
                return ITransaction.ReconciliationState.class;
            case 1:
                return Date.class;
            case 2:
                return IAccount.class;
            case 3:
                return String.class;
            case 4:
                return MonetaryValue.class;
        }
        return null;
    }
    
    public ITransaction getTransactionAt( int row )
    {
        return m_transactions.get( row );
    }

    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object )
    {
        // Find the item in the collection and update the table
        if ( ChangeType.UPDATE == change )
        {
            // Update the reconciliation status
            int row = m_transactions.indexOf( object );
            if ( row >= 0 )
            {
                fireTableCellUpdated( row, 0 );
            }
        }
    }
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    /*public boolean isCellEditable(int row, int col) 
    {
        return true;
    }*/

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    /*public void setValueAt(Object value, int row, int col)
    {
        m_trans[row][col] = value;
        fireTableCellUpdated(row, col);
    }*/
    
    private ArrayList<ITransaction> m_transactions = new ArrayList<ITransaction>();
    private static final String[] m_columns = { "Reconciled", "Date", "Account", "Description", "Value" };
}

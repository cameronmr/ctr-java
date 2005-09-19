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

import java.util.Collections;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cam
 */
public class TransactionTableModel extends AbstractTableModel
{
    public TransactionTableModel( )
    {
        m_columns = Transaction.getColumns();
        m_transactions = new Vector<ITransaction>( DataObjectFactory.getTransactions( ) );
        Collections.sort( m_transactions, ITransaction.TRANSACTION_DATE_ORDER );
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
        return m_transactions.get( row ).getValue( col );
    }

    public Class getColumnClass(int c)
    {
        Object o = getValueAt( 0, c );
        if ( null != o )
        {
            return o.getClass();
        }
        
        return String.class;
    }
    
    public ITransaction getTransactionAt( int row )
    {
        return m_transactions.get( row );
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
    
    private Vector<ITransaction> m_transactions;
    private String[] m_columns;
}

/*
 * SelectTransactionTableModel.java
 *
 * Created on 8 February 2006, 19:56
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cam
 */
public class SelectTransactionTableModel extends AbstractTableModel
{
    
    /** Creates a new instance of SelectTransactionTableModel */
    public SelectTransactionTableModel( Collection<ITransaction> transactions )
    {
        setTransactions( transactions );
    }
    
     public void setTransactions( Collection<ITransaction> transactions )
    {        
        m_transactions.clear();
        m_selections.clear();
        
        m_transactions.addAll( transactions );
        
        Collections.sort( m_transactions, Comparators.TRANSACTION_DATE_ORDER );        
        
        // Observe any changes to the transactions
        for ( ITransaction trans : m_transactions )
        {            
            // By default
            m_selections.add( true );
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
                return m_selections.get( row );
            case 1:
                return trans.getDate();
            case 2:
                return trans.getMonetaryValue();
            case 3:
                return trans.getNarrative();
        }
        return null;
    }

    public Class getColumnClass(int c)
    {
        switch ( c )
        {
            case 0:
                return Boolean.class;
            case 1:
                return Date.class;
            case 2:
                return MonetaryValue.class;
            case 3:
                return String.class;
        }
        return null;
    }    
    
    public boolean isCellEditable(int row, int col) 
    {
        if ( col == 0 )
        {
            return true;
        }
        
        return false;
    }
    
    public void setValueAt(Object value, int row, int col)
    {
        if ( col == 0 )
        {
            m_selections.set( row, (Boolean)value );
        }
        fireTableCellUpdated(row, col);
    }
    
    public void applyRule( IRule rule ) throws Exception
    {
        for( int i = 0; i < m_selections.size(); i++ )
        {
            if ( m_selections.get(i) )
            {
                rule.applyRule( m_transactions.get( i ) );
            }
        }
    }
    
    private ArrayList<Boolean> m_selections = new ArrayList<Boolean>();
    private ArrayList<ITransaction> m_transactions = new ArrayList<ITransaction>();
    private static final String[] m_columns = { " ", "Date", "Value", "Description" };
}

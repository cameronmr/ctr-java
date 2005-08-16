/*
 * ReconciliationTableModel.java
 *
 * Created on 29 June 2005, 20:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cam
 */
public class ReconciliationTableModel extends AbstractTableModel
{
    
    /** Creates a new instance of ReconciliationTableModel */
    public ReconciliationTableModel(  ) 
    {
    }
    
    public void setTransaction( ITransaction transaction )
    {        
        m_transaction = transaction;
        
        // reload the table
        fireTableDataChanged();
    }
    
    public int getColumnCount() 
    {
        return m_columns.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_columns[col];
    }
    
    public int getRowCount() 
    {
        if ( null != m_transaction )
        {
            return m_transaction.getReconciliations().size();
        }
        
        return 4;
    }
    
    public Object getValueAt( int row, int column )
    {
        if ( null == m_transaction )
        {
            return "";
        }
        
        IReconciliation recon = m_transaction.getReconciliations().get( row );
        switch ( column )
        {
            case 0:
                return recon.getCatecory();
            case 1:
                return recon.getNote();
            case 2:
                return recon.getValue();
        }
        
        return null;
    }
    
    public Class getColumnClass(int c)
    {        
        switch ( c )
        {
            case 0:
                return ICategory.class;
            case 1:
                return String.class;
            case 2:
                return MonetaryValue.class;
        }
        
        return null;
    }
    
    private String[] m_columns = { "Category", "Reconciliation Note", "Amount" };
    private ITransaction m_transaction;
}

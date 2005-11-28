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

import java.util.ArrayList;
import javax.swing.JOptionPane;
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
    
    public void setTransaction( ITransaction transaction ) throws Exception
    {                
        // When the transaction is applied ask the existing transaction to apply any changes that are necessary
        if ( null != m_newReconciliation )
        {
            if ( m_newReconciliation.isNew() &&
                    !m_newReconciliation.isModified() )
            {
                // If it hasn't been modified just delete it
                m_newReconciliation.delete();
            }
            else
            {
                // This will throw an exception if not valid
                m_newReconciliation.commit();

                m_transaction.addReconciliation( m_newReconciliation );
            }
        }

        m_transaction = transaction;

        m_reconciliations = new ArrayList<IReconciliation>(transaction.getReconciliations());

        // If the transaction is not fully reconciled then create a reconciliation with the remaining amount
        if ( m_transaction.getReconciliationState() != ITransaction.ReconciliationState.FULL )
        {
            m_newReconciliation = DataObjectFactory.newReconciliationForTransaction( m_transaction );
            m_reconciliations.add( m_newReconciliation );
        }

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
        return m_reconciliations.size();
    }
    
    public Object getValueAt( int row, int column )
    {
        if ( m_reconciliations.isEmpty() )
        {
            return "";
        }
        
        IReconciliation recon = m_reconciliations.get( row );
        switch ( column )
        {
            case 0:
                return recon.getCatecory();
            case 1:
                return recon.getNote();
            case 2:
                return recon.getValue();
            default:
                return null;
        }
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
    
    /*
     * Don't need to implement this method unless your table's
     * editable.
     */
    public boolean isCellEditable(int row, int col) 
    {
        return true;
    }

    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col)
    {
        if ( m_reconciliations.isEmpty() )
        {
            return;
        }
        
        IReconciliation recon = m_reconciliations.get( row );
        switch ( col )
        {
            case 0:
                recon.setCategory( (ICategory)value );
                break;
            case 1:
                recon.setNote( (String)value );
                break;
            case 2:
                recon.getValue();
                break;
        }
        
        // Commit the changes to the database
        try
        {
            recon.commit();
        }
        catch ( Exception e )
        {
            // TODO:
        }
        
        fireTableCellUpdated(row, col);
    }
    
    private String[] m_columns = { "Category", "Reconciliation Note", "Amount" };
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();
    private ITransaction m_transaction;
    private IReconciliation m_newReconciliation;
}

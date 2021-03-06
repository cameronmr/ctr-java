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

import com.rochester.budget.core.IDataChangeObserver.ChangeType;
import com.rochester.budget.core.exceptions.UnsavedReconciliationException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cam
 */
public class ReconciliationTableModel extends AbstractTableModel implements IDataChangeObserver
{
    
    /** Creates a new instance of ReconciliationTableModel */
    public ReconciliationTableModel(  ) 
    {
    }
    
    public void removeTransaction()
    {
        m_reconciliations.clear();
        
        try
        {
            setTransaction( null, true );
        }
        catch( Exception e )
        {}
    }
    
    public void setTransaction( ITransaction transaction, boolean discardIncomplete ) throws UnsavedReconciliationException
    {                        
        // When the transaction is applied ask the existing transaction to apply any changes that are necessary
        // If the applied transaction is the same as the existing transaction then skip this bit
        if ( m_transaction != null )
        {
            ArrayList<IReconciliation> recons = new ArrayList<IReconciliation>( m_transaction.getReconciliations() );
            for ( IReconciliation recon : recons )
            {
                if ( recon.isNew() )
                {
                    if( recon.isModified() && !discardIncomplete )
                    {
                        throw new UnsavedReconciliationException("");
                    }
                    else
                    {
                        // If it hasn't been modified, or we are discarding incomplete, just delete it                
                        recon.delete();
                    }
                }
            }
        }

        if ( transaction != null )
        {
            // Store the transaction 
            m_transaction = transaction;
            m_reconciliations = new ArrayList<IReconciliation>( transaction.getReconciliations() );

            // If the transaction is not fully reconciled then create a reconciliation with the remaining amount
            // If a reconciliation still exists then we don't want to add anymore
            if ( transaction.getReconciliationState() != ITransaction.ReconciliationState.FULL  )
            {
                IReconciliation newReconciliation = DataObjectFactory.newReconciliationForTransaction( transaction );
                m_reconciliations.add( newReconciliation );

                try
                {
                    // The transaction can also monitor changes to the reconciliation, to update value remaining, etc
                    transaction.addReconciliation( newReconciliation );
                }
                catch ( Exception e )
                {
                    // a new reconciliation always has a value of zero and should not cause an exception to be thrown 
                }

                // We want to observe the reconciliation to delete it if necessary
                newReconciliation.addObserver( this );
            }
        }

        // reload the table
        fireTableDataChanged();
    }
    
    public void deleteReconciliation( int item )
    {
        try
        {
            IReconciliation recon = m_reconciliations.get( item );

            if ( null != recon )
            {
                // Delete the currently selected reconciliation, this should automatically update the transaction
                // and it will automatically update us as well (we are observing the reconciliation)
                recon.delete();           
            }

            // Don't need to do this as the change to the transaction will result in the table being redrawn
            // fireTableDataChanged();
        }
        catch ( IndexOutOfBoundsException e )
        {
            // do nothing
        }
    }
    
    public void notifyDatabaseChange( ChangeType change, IDatabaseObject object )
    {        
        switch ( change )
        {
            case DELETE:
                // remove from the list of reconciliations
                m_reconciliations.remove( object );
                break;
            case UPDATE:
                // Do nothing, the transaction is already listening to this!
                break;
        }        
    }
    
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
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
    
    public boolean isCellEditable(int row, int col) 
    {
        return true;
    }

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
                recon.setValue( new MonetaryValue( value ) );
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
    
    private static final String[] m_labels = { "Category", "Reconciliation Note", "Amount" };
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();
    private ITransaction m_transaction = null;
}

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
public class ViewReconTableModel extends AbstractTableModel
{
    
    /** Creates a new instance of SelectTransactionTableModel */
    public ViewReconTableModel( final StatementSummary summary )
    {
        m_summary = summary;
        m_reconciliations = new ArrayList<IReconciliation>( summary.getReconciliations() );
    }
         
    public int getColumnCount() 
    {
        return m_columns.length;
    }

    public int getRowCount() 
    {
        return m_reconciliations.size();
    }

    public String getColumnName(int col) 
    {
        return m_columns[col];
    }

    public Object getValueAt( int row, int col )
    {
        IReconciliation recon = m_reconciliations.get( row );
        
        //"Date", "Narrative", "Note", "Value"
        switch ( col )
        {
            case 0:
                return recon.getTransaction().getDate();
            case 1:
                return recon.getTransaction().getNarrative();
            case 2:
                return recon.getNote();
            case 3:
                return recon.getValue();
        }
        return null;
    }

    public Class getColumnClass(int c)
    {
        //"Date", "Narrative", "Note", "Value"
        switch ( c )
        {
            case 0:
                return Date.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return MonetaryValue.class;
        }
        return null;
    }    
    
    public boolean isCellEditable(int row, int col) 
    {        
        return false;
    }
        
    private StatementSummary m_summary;
    private ArrayList<IReconciliation> m_reconciliations;
    private static final String[] m_columns = { "Date", "Narrative", "Note", "Value" };
}

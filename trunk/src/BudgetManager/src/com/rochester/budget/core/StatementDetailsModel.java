/*
 * StatementDetailsModel.java
 *
 * Created on 27 December 2005, 16:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.sql.Date;

/**
 *
 * @author Cam
 */
public class StatementDetailsModel extends AbstractDetailsModel<IStatement>
{ 
    /**
     * Creates a new instance of AccountDetailsModel 
     */
    public StatementDetailsModel( IStatement theStatement )
    {
        super( theStatement );
    }
        
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
    }
    
    public boolean isCellEditable( int row, int col ) 
    {
        // "Statement", "Reconciled Account", "Start Date", "End Date"
        return true;
    }
        
    public Object getValueAt( int row, int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        // "Statement", "Reconciled Account", "Start Date", "End Date"
        IStatement statement = get( row );
        switch ( column )
        {
            case 0:
                return statement.getTransactionAccount();
            case 1:
                return statement.getStatementStart();
            case 2:
                return statement.getStatementEnd();      
        }
        
        return null;
    }
    
    public Class getColumnClass( int c )
    {           
        // "Statement", "Reconciled Account", "Start Date", "End Date"
        switch ( c )
        {
            case 0:
                return IAccount.class;
            case 1:
            case 2:
                return Date.class;
        }
        
        return null;
    }    
    
    public void setValueAt(Object value, int row, int col)
    {        
        IStatement statement = get( row );
        
        // "Reconciled Account", "Start Date", "End Date"
        switch ( col )
        {
            case 0:
                statement.setTransactionAccount( (IAccount) value );
                break;
            case 1:
                statement.setStatementStart( (Date) value );
                break;
            case 2:
                statement.setStatementEnd( (Date) value );
                break;
        }
                
        fireTableCellUpdated(row, col);
    }
    
    public String getTitle()
    {
        return "Statement Details";
    }
        
    private static final String[] m_labels = { "For Account", "Start Date", "End Date" };
    
}

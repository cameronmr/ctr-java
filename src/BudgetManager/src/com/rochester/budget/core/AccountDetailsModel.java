/*
 * AccountDetailsModel.java
 *
 * Created on 26 December 2005, 12:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.ArrayList;
import javax.swing.JOptionPane;


/**
 *
 * @author Cam
 */
public class AccountDetailsModel extends AbstractDetailsModel<IAccount>
{    
    /**
     * Creates a new instance of AccountDetailsModel 
     */
    public AccountDetailsModel( IAccount theAccount )
    {
        super( theAccount );
    }
        
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
    }
        
    public boolean isCellEditable(int row, int col) 
    {
        return true;
    }
    
    public Object getValueAt( int row, int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        IAccount account = get( row );
        switch ( column )
        {
            case 0:
                return account.getName();
            case 1:
                return account.getNumber();
            case 2:
                return account.getDescription();
        }
        
        return null;
    }
    
    public Class getColumnClass(int c)
    {                
        return String.class;
    }    
    
    public void setValueAt(Object value, int row, int col)
    {        
        IAccount account = get( row );
        switch ( col )
        {
            case 0:
                account.setName( (String) value );
                break;
            case 1:
                account.setNumber( (String) value );
                break;
            case 2:
                account.setDescription( (String) value );
                break;
        }
                
        fireTableCellUpdated(row, col);
    }
    
    public String getTitle()
    {
        return "Account Details";
    }
        
    private static final String[] m_labels = { "Name", "Number", "Description" };
}

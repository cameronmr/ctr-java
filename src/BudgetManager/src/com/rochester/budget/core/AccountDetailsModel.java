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
        super( theAccount, m_labels );
    }
        
    public boolean isEditable(int col) 
    {
        return true;
    }
    
    public Object getValueAt( int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        IAccount account = get( );
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
    
    public void setValueAt(Object value, int col)
    {        
        IAccount account = get( );
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
    }
    
    public String getTitle()
    {
        return "Account Details";
    }
        
    private static final String[] m_labels = { "Name", "Number", "Description" };
}

/*
 * CategoryDetailsModel.java
 *
 * Created on 26 December 2005, 22:52
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
public class CategoryDetailsModel extends AbstractDetailsModel<ICategory>
{
    
    /** Creates a new instance of CategoryDetailsModel */
    public CategoryDetailsModel( ICategory category )
    {
        super( category );
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
        switch ( col )
        {
            case 0:
            case 1:
            case 2:
            case 3:
                return true;
            case 4:
                return false;
        }
        
        return false;
    }
    
    public Object getValueAt( int row, int column )
    {
        if ( isEmpty() )
        {
            return "";
        }
        
        //"Name", "Description", "Parent Category", "Active", "Associated Account"
        ICategory category = get( row );
        switch ( column )
        {
            case 0:
                return category.getName();
            case 1:
                return category.getDescription();
            case 2:
                return category.getParent();
            case 3:
                return category.isActive();
            case 4:
                return category.getAccount();                
        }
        
        return null;
    }
    
    public Class getColumnClass(int c)
    {         
        switch ( c )
        {
            case 0:                
            case 1:
                return String.class;
            case 2:
                return ICategory.class;
            case 3:
                return Boolean.class;
            case 4:
                return IAccount.class;
        }
        
        return null;
    }    

    public void setValueAt(Object value, int row, int col)
    {        
        ICategory category = get( row );
        
        //"Name", "Description", "Parent Category", "Active", "Associated Account"
        switch ( col )
        {
            case 0:
                category.setName( (String) value );
                break;
            case 1:
                category.setDescription( (String) value );
                break;
            case 2:
                category.setParent( (ICategory) value );
                break;
            case 3:
                category.setActive( (Boolean) value );
                break;
            case 4:
                category.setAccount( (IAccount) value );
                break;
        }
                
        fireTableCellUpdated(row, col);
    }
    
    public String getTitle()
    {
        return "Category Details";
    }
    
    
    private static final String[] m_labels = { "Name", "Description", "Parent Category", "Active", "Associated Account" };
}

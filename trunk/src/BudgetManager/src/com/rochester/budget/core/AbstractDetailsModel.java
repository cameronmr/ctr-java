/*
 * AbstractDetailsModel.java
 *
 * Created on 27 December 2005, 16:23
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
public abstract class AbstractDetailsModel< E extends IDatabaseObject > extends AbstractTableModel implements IDetailsPanelModel
{
    
    /** Creates a new instance of AbstractDetailsModel */
    public AbstractDetailsModel( E theObject )
    {
        if ( null != theObject )
        {
            m_items.add( theObject );
        }
    }
            
    public int getRowCount() 
    {
        return m_items.size();
    }
         
    public boolean isCellEditable(int row, int col) 
    {
        return false;
    }
    
    public boolean isEditable( int index )
    {
        return isCellEditable( 0, index );
    }
        
    public Object getValueAt( int index )
    {
        return getValueAt( 0, index );
    }
    
    public void setValueAt( Object value, int index )
    {
        setValueAt( value, 0, index );
    }
    
    public boolean isValid()
    {
        return m_items.get( 0 ).isValid();
    }
    
    public boolean isModified()
    {
        return m_items.get( 0 ).isModified();
    }
    
    public void applyChanges() throws Exception
    {
        m_items.get( 0 ).commit();
    }
    
    public void cancelChanges() throws Exception
    {
        if ( m_items.get( 0 ).isNew() )
        {
            if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, 
                    "This object has not yet been saved. Cancelling now will delete the object. Are you sure you wish to delete the object?", 
                    "Delete Object",  
                    JOptionPane.YES_NO_OPTION ) )
            {
                m_items.get( 0 ).delete();
                m_items.remove( 0 );
            }
        }
        else
        {
            m_items.get( 0 ).rollbackToLastGoodState();
        }
    }
    
    public E get( int row )
    {
        return m_items.get( row );
    }
    
    public boolean isEmpty()
    {
        return m_items.isEmpty();
    }
    
    private ArrayList<E> m_items = new ArrayList<E>();
}

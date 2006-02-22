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
public abstract class AbstractDetailsModel< E extends IDatabaseObject > implements IDetailsPanelModel
{    
    /** Creates a new instance of AbstractDetailsModel */
    public AbstractDetailsModel( E theObject, final String[] labels )
    {
        m_theItem = theObject;
        m_labels = labels;
    }
                 
    public int getColumnCount() 
    {
        return m_labels.length;
    }
    
    public String getColumnName(int col) 
    {
        return m_labels[col];
    }
    
    public boolean isValid()
    {
        if ( null == m_theItem )
        {
            return false;
        }
        
        return m_theItem.isValid();
    }
    
    public boolean isModified()
    {
        return m_theItem.isModified();
    }
    
    public void applyChanges() throws Exception
    {
        m_theItem.commit();
    }
    
    public void cancelChanges() throws Exception
    {
        if ( m_theItem.isNew() )
        {
            if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog( null, 
                    "This object has not yet been saved. Cancelling now will delete the object. Are you sure you wish to delete the object?", 
                    "Delete Object",  
                    JOptionPane.YES_NO_OPTION ) )
            {
                m_theItem.delete();
            }
        }
        else
        {
            m_theItem.rollbackToLastGoodState();
        }
    }
    
    public E get( )
    {        
        return m_theItem;
    }
    
    public boolean isEmpty()
    {
        return m_theItem == null;
    }
    
    private E m_theItem = null;
    private String[] m_labels = null;
}

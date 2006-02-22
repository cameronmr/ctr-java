/*
 * DateEditor.java
 *
 * Created on 27 December 2005, 21:05
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeListener;
import java.util.Date;
import javax.swing.AbstractCellEditor;

import com.toedter.calendar.JDateChooser;

/**
 *
 * @author Cam
 */
public class DateEditor extends AbstractCellEditor implements DetailsPanelEditor
{
    public DateEditor( )
    {
    }

    public DateEditor( int index, boolean editable, Object value )
    {
        m_index = index;
        m_dateChooser = new JDateChooser( );
        m_dateChooser.setEnabled( editable );        
        m_dateChooser.setDate( value != null ? (Date)value : new Date() );
        m_originalValue = value;

        m_dateChooser.addPropertyChangeListener( new PropertyChangeListener ()
        {
            public void propertyChange( java.beans.PropertyChangeEvent evt )
            {
                // We only care about changes to the date property
                if ( evt.getPropertyName().equals( "date") )
                {
                    editingStopped();
                }
            }
        });

        m_dateChooser.addFocusListener( new FocusAdapter()
        {
            public void focusLost( FocusEvent e )
            {
                editingStopped( );
            }
        });
    }

    public DateEditor getDetailPanelEditorComponent( int index, boolean editable, Object value )
    {            
        return new DateEditor( index, editable, value );
    }

    public Component getComponent()
    {
        return m_dateChooser;
    }

    public Object getCellEditorValue()
    {            
        return new java.sql.Date( m_dateChooser.getDate().getTime() );
    }

    public int getIndex( )
    {
        return m_index;
    }

    private void editingStopped()
    {
        // If there is no original value, and the text field hasn't been modified don't edit the cell
        if ( m_originalValue == null )
        {
            if ( null != m_dateChooser.getDate() )
            {
                stopCellEditing();
            }

            return;
        }

        if ( ! m_originalValue.equals( m_dateChooser.getDate() ) )
        {
            stopCellEditing();
        }
    }
        
    private int m_index;
    private JDateChooser m_dateChooser;
    private Object m_originalValue;    
}

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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Date;
import javax.swing.AbstractCellEditor;

import com.standbysoft.component.date.swing.JDatePicker;
import com.standbysoft.component.date.event.DateSelectionAdapter;
import com.standbysoft.component.date.event.DateSelectionEvent;
import com.standbysoft.component.date.event.DateSelectionEvent;

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
        m_datePicker = new JDatePicker( );
        m_datePicker.setEnabled( editable );        
        m_datePicker.setSelectedDate( value != null ? (Date)value : new Date() );
        m_originalValue = value;

        m_datePicker.addDateSelectionListener( new DateSelectionAdapter()
        {
            public void dateSelectionChanged(DateSelectionEvent evt) 
            {
                editingStopped( );
            }
        }); 

        m_datePicker.addFocusListener( new FocusListener()
        {
            public void focusGained( FocusEvent e )
            {
                // Do nothing
            }

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
        return m_datePicker;
    }

    public Object getCellEditorValue()
    {            
        return new java.sql.Date( m_datePicker.getSelectedDate().getTime() );
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
            if ( null != m_datePicker.getSelectedDate() )
            {
                stopCellEditing();
            }

            return;
        }

        if ( ! m_originalValue.equals( m_datePicker.getSelectedDate() ) )
        {
            stopCellEditing();
        }
    }
        
    private int m_index;
    private JDatePicker m_datePicker;
    private Object m_originalValue;    
}

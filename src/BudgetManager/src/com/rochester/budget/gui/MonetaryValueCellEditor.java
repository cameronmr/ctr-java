/*
 * MonetaryValueEditor.java
 *
 * Created on 29 November 2005, 20:38
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.MonetaryValue;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.AbstractCellEditor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Cam
 */
public class MonetaryValueCellEditor extends AbstractCellEditor implements TableCellEditor
{
    /** Creates a new instance of MonetaryValueEditor */
    public MonetaryValueCellEditor()
    { 
        m_field.setFocusTraversalKeysEnabled( false );
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        m_field.setValue( ((MonetaryValue)value).getValue() );
        m_field.setText( "$" );
        m_field.setCaretPosition( 1 );
        return m_field;
    }
        
    public Object getCellEditorValue()
    {        
        try
        {
            m_field.commitEdit();
        }
        catch ( Exception e )
        {}
        return m_field.getValue();
    }
    
    private JFormattedTextField m_field = new JFormattedTextField( NumberFormat.getCurrencyInstance() );
    
}

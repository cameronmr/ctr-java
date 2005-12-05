/*
 * CategoryCellEditor.java
 *
 * Created on 11 September 2005, 11:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.ICategory;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

/**
 *
 * @author Cam
 */
public class CategoryCellEditor extends DefaultCellEditor// implements TableCellEditor
{
    
    /** Creates a new instance of CategoryCellEditor */
    public CategoryCellEditor( JTable table )
    {
        super( new CategoryComboBox() );
        m_categoryCombo = (CategoryComboBox)getComponent();
        m_table = table;
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        m_categoryCombo.populateCategorys( );
        
        return super.getTableCellEditorComponent(table, value, isSelected, row, column );
    }
            
    public boolean stopCellEditing()
    {
        if ( super.stopCellEditing() )
        {
            // For some reason focus was returning to the main transaction table..?
            m_table.requestFocusInWindow();            
            return true;
        }
        
        return false;
    }
    
    private CategoryComboBox m_categoryCombo = null;
    private JTable m_table = null;
}
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
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Cam
 */
public class CategoryCellEditor extends AbstractCellEditor implements TableCellEditor
{
    
    /** Creates a new instance of CategoryCellEditor */
    public CategoryCellEditor()
    {
        m_panel.setLayout( new BorderLayout() );
        
        m_categoryCombo = new CategoryComboBox( this );
        m_panel.add( m_categoryCombo, BorderLayout.CENTER );
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        // Update the label & the button
        ICategory category = (ICategory)value;
        m_categoryCombo.selectCategory( category );
        
        m_selectedCol = column;
        m_selectedRow = row;
        m_table = table;
        return m_panel;
    }
        
    public Object getCellEditorValue()
    {        
        return m_categoryCombo.getSelectedItem();
    }
    
    public void cancelCellEditing()
    {
        super.cancelCellEditing();
    }
    
    public boolean stopCellEditing()
    {
        if ( super.stopCellEditing() )
        {
            m_table.setColumnSelectionInterval( m_selectedCol + 1, m_selectedCol + 1 );
            return true;
        }
        return false;
    }
    
    private JPanel m_panel = new JPanel();
    private CategoryComboBox m_categoryCombo = null;
    private int m_selectedCol, m_selectedRow;
    private JTable m_table = null;
}
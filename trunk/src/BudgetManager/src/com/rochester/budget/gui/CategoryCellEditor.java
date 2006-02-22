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
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Cam
 */
public class CategoryCellEditor extends AbstractCellEditor implements TableCellEditor
{
    
    /** Creates a new instance of CategoryCellEditor */
    public CategoryCellEditor( JTable table )
    {
        m_categoryCombo = new CategoryComboBox();
        m_categoryCombo.addKeyListener(new KeyAdapter()
        {
            public void keyPressed(KeyEvent evt) 
            {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER)
                {
                    fireEditingStopped();
                }
            }
        });
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) 
    {
        m_categoryCombo.populateCategorys( );
        m_categoryCombo.setSelectedItem( value );
                        
        return m_categoryCombo;
    }
    
    public Object getCellEditorValue()
    {        
        return m_categoryCombo.getSelectedItem();
    }
                    
    private CategoryComboBox m_categoryCombo = null;
}
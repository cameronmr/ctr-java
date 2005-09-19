/*
 * CategoryCellRenderer.java
 *
 * Created on 7 September 2005, 19:15
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.ICategory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Cam
 */
public class CategoryCellRenderer extends JPanel implements TableCellRenderer
{
    
    /** Creates a new instance of CategoryCellRenderer */
    public CategoryCellRenderer()
    {
        setLayout( new BorderLayout() );
        add( m_categoryLabel, BorderLayout.CENTER );
        add( m_categoryButton, BorderLayout.EAST );
    }
    
    public void setForeground(Color c)
    {
        super.setForeground( c );
        if ( null != m_categoryLabel )
        {
            m_categoryLabel.setForeground( c );
        }
    }
    
    public void setBackground(Color c) 
    {
        super.setBackground( c );
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        if (isSelected) 
        {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        }
        else 
        {
            setForeground( table.getForeground() );
            setBackground( table.getBackground() );
        }
        
        // Update the label & the button
        ICategory category = (ICategory)value;
        m_categoryLabel.setText( category.toString() );
        m_categoryButton.setText( "..." );
        
        return this;
    }  
    
    private JLabel m_categoryLabel = new JLabel();
    private JButton m_categoryButton = new JButton();    
}

/*
 * CategoryComboBox.java
 *
 * Created on 11 September 2005, 18:48
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.ICategory;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Cam
 */
public class CategoryComboBox extends JComboBox implements ActionListener
{   
    /** Creates a new instance of CategoryComboBox */
    public CategoryComboBox( AbstractCellEditor editor )
    {
        addActionListener( this );
        m_editor = editor;
    }
    
    public void selectCategory( final ICategory category )
    {        
        // Empty the combo box, disable it before emptying it to stop unnecessary messages
        m_editing = false;
        this.removeAllItems();
        
        try
        {
            List<ICategory> categories = DataObjectFactory.loadRootCategory().getDescendants();

            // Sort the categories into name order before inserting into the combo box
            Collections.sort( categories, ICategory.CATEGORY_NAME_ORDER );

            for ( ICategory c : categories )
            {
                this.addItem( c );
            }
            
            // Add the new category placeholder
            this.addItem( m_newCategoryString );
            
            setSelectedItem( category );
            m_selectedIndex = getSelectedIndex();
            
            // Combo box has been repopulated, so enable it again
            m_editing = true;
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, e.toString() );
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if ( m_editing )
        {
            JComboBox cb = (JComboBox)e.getSource();
            Object newSelection = cb.getSelectedItem();
            if ( null != newSelection )
            {
                if( newSelection.equals( m_newCategoryString ) )
                {            
                    if ( JOptionPane.showConfirmDialog( this, "create new category..." ) == JOptionPane.OK_OPTION )
                    {
                        // Select the new               
                        m_editor.stopCellEditing();
                    }
                    else
                    {
                        // select the original
                        setSelectedIndex( m_selectedIndex );
                    }
                }
                else
                {
                    m_selectedIndex = getSelectedIndex();
                    m_editor.stopCellEditing();
                }
            }
        }
    }
    
    private final String m_newCategoryString = new String( "New Category..." );
    private int m_selectedIndex;
    private AbstractCellEditor m_editor = null;
    private boolean m_editing = false;
}
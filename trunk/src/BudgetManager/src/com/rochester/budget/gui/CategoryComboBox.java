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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Cam
 */
public class CategoryComboBox extends JComboBox implements ActionListener
{   
    /** Creates a new instance of CategoryComboBox */
    public CategoryComboBox( )
    {
        addActionListener( this );
    }
    
    public void selectCategory( final ICategory category )
    {
        m_selectedCategory = category;
        
        // Empty the combo box
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
            
            setSelectedItem( m_selectedCategory );
        }
        catch( Exception e )
        {
            JOptionPane.showMessageDialog( this, e.toString() );
        }
    }
    
    public void actionPerformed(ActionEvent e)
    {
        JComboBox cb = (JComboBox)e.getSource();
        Object selected = cb.getSelectedItem();
        if ( null != selected && 
                selected.equals( m_newCategoryString ) )
        {
            if ( JOptionPane.showConfirmDialog( this, "woot" ) == JOptionPane.OK_OPTION )
            {
                // Select the new                
            }
            else
            {
                // select the original
                setSelectedItem( m_selectedCategory );
            }
        }
    }
    
    private final String m_newCategoryString = new String( "New Category..." );
    private ICategory m_selectedCategory;
}

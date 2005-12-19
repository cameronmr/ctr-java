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
        populateCategorys();
        addActionListener( this );
    }
    
    
    public void populateCategorys( )
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
            if( isNewCategorySelected() )
            {   
                CategoryCreatePanel panel = new CategoryCreatePanel( null );
                if ( JOptionPane.showConfirmDialog( null, panel, "Create New Category...", JOptionPane.OK_CANCEL_OPTION ) == JOptionPane.OK_OPTION )
                {
                    try
                    {
                        ICategory newCat = panel.getCategory(); 
                        
                        // Select the new      
                        populateCategorys();
                        setSelectedItem( newCat );
                    }
                    catch ( Exception ex )
                    {
                        JOptionPane.showMessageDialog( null, ex, "Error", JOptionPane.ERROR_MESSAGE );
                        // select the original
                        setSelectedIndex( m_selectedIndex );                        
                    }
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
            }
            
            //super.actionPerformed( e );
        }
    }
    
    public boolean isNewCategorySelected( )
    {
        Object newSelection = this.getSelectedItem();
        if ( newSelection != null &&
            newSelection.equals( m_newCategoryString ) )
        {
            return true;
        }
        
        return false;
    }
    
    private final String m_newCategoryString = new String( "New Category..." );
    private int m_selectedIndex;
    private boolean m_editing = false;
}

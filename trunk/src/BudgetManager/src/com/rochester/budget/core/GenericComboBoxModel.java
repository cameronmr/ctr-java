/*
 * GenericComboBoxModel.java
 *
 * Created on 27 December 2005, 22:16
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.Collection;
import javax.swing.ComboBoxModel;

/**
 *
 * @author Cam
 */
public class GenericComboBoxModel< E extends IDatabaseObject > extends GenericListModel< E > implements ComboBoxModel
{
    
    /** Creates a new instance of GenericComboBoxModel */
    public GenericComboBoxModel()
    {
    }
    
    public GenericComboBoxModel( Collection<E> contents )
    {
        super( contents );
    }
    
    public Object getSelectedItem()
    {
        return m_selectedItem;
    }
    
    public E getSelected()
    {
        return m_selectedItem;
    }
    
    public void setSelectedItem( Object anItem ) 
    {
        m_selectedItem = (E)anItem;
    }
    
    private E m_selectedItem = null;
}

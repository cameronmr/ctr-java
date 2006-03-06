/*
 * ReconciliationTable.java
 *
 * Created on 29 June 2005, 21:32
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.ICategory;
import com.rochester.budget.core.MonetaryValue;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

/**
 *
 * @author Cam
 */
public class ReconciliationTable extends JTable
{
    
    /** Creates a new instance of ReconciliationTable */
    public ReconciliationTable( TableModel model )
    {
        super( model );
        
        setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        setSurrendersFocusOnKeystroke( true );
        
        // TODO: Get the classes from the table model
        setDefaultRenderer( ICategory.class, new DefaultTableCellRenderer() );
        setDefaultRenderer( MonetaryValue.class, new DefaultTableCellRenderer() );
        
        setDefaultEditor( ICategory.class, new CategoryCellEditor( this ) );
        setDefaultEditor( MonetaryValue.class, new MonetaryValueCellEditor() );
    }
    
}

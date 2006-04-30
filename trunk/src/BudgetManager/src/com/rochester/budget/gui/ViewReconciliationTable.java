/*
 * ViewReconciliationTable.java
 *
 * Created on 25 April 2006, 11:24
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Cam
 */
public class ViewReconciliationTable extends JTable
{
    
    /** Creates a new instance of ViewReconciliationTable */
    public ViewReconciliationTable( TableModel model )
    {
        super( model );
        this.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
                
        initColumnSizes( );        
    }
    
    private void initColumnSizes( ) 
    {       
        // TODO: Store column widths in database!
        TableModel model = getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
            
        column = getColumnModel().getColumn(0);
        column.setMaxWidth( 60 );        
        column = getColumnModel().getColumn(3);
        column.setMaxWidth( 55 );        
    }
    
}

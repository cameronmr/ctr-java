/*
 * TransactionTable.java
 *
 * Created on 14 February 2005, 20:45
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.IAccount;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author Cameron
 */
public class TransactionTable extends JTable
{
    
    /** Creates a new instance of TransactionTable */
    public TransactionTable( TableModel model )
    {
        super( model );
        this.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        
        setDefaultRenderer( IAccount.class, new DefaultTableCellRenderer() );
        
        initColumnSizes( );        
    }    
    
     /*
     * This method picks good column sizes.
     * If all column heads are wider than the column's cells'
     * contents, then you can just use column.sizeWidthToFit().
     */
    private void initColumnSizes( ) 
    {       
        // TODO: Store column widths in database!
        TableModel model = getModel();
        TableColumn column = null;
        Component comp = null;
        int headerWidth = 0;
        TableCellRenderer headerRenderer =
            getTableHeader().getDefaultRenderer();

        for (int i = 0; i < model.getColumnCount(); i++) 
        {
            if ( i == 3 )
                continue;
            
            column = getColumnModel().getColumn(i);

            comp = headerRenderer.getTableCellRendererComponent(
                                 null, column.getHeaderValue(),
                                 false, false, 0, 0);
            
            headerWidth = comp.getPreferredSize().width * 3;

            column.setMaxWidth( headerWidth );
        }
    }
}


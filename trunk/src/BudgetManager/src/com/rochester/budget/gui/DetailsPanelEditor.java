/*
 * DetailsPanelEditor.java
 *
 * Created on 26 December 2005, 13:55
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;
import com.rochester.budget.core.IGUIComponent;
import javax.swing.CellEditor;

/**
 *
 * @author Cam
 */
public interface DetailsPanelEditor extends CellEditor, IGUIComponent
{
    DetailsPanelEditor getDetailPanelEditorComponent( int index, boolean editable, Object value );
    
    int getIndex( );    
}

/*
 * IDetailsPanelModel.java
 *
 * Created on 26 December 2005, 17:12
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import javax.swing.table.TableModel;

/**
 *
 * @author Cam
 */
public interface IDetailsPanelModel extends TableModel
{
    String getTitle( );
    
    boolean isEditable( int index );
    
    Object getValueAt( int index );
    
    void setValueAt( Object value, int index );
    
    boolean isValid();    
    
    boolean isModified();
    
    void applyChanges() throws Exception;
    
    void cancelChanges() throws Exception;
}

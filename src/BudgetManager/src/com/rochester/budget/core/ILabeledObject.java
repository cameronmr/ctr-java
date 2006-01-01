/*
 * ILabeledObject.java
 *
 * Created on 26 December 2005, 11:22
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

/**
 * This class represents an object that can be displayed in a list,
 * or tree, or other tabular style GUI element.
 * @author Cam
 */
public interface ILabeledObject
{       
    /**
     * Get the object value at the given index.
     * @param index The index of the value to retrieve
     * @return The value at the given index
     */
    Object getValue( int index );
    
    /**
     * Set the value of the object at the given index.
     * @param index The index of the value to set.
     * @param value The new value of the object
     */
    void setValue( int index, Object value );
}

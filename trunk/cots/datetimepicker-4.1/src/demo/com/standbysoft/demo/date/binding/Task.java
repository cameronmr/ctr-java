/*
 * @(#)Task.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.demo.date.binding;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.jgoodies.binding.beans.Model;

/**
 * Describes a task and provides bound bean properties.
 */
public final class Task extends Model {
    
    // Examples ***************************************************************
    
    /** An example Task. */
    public static final Task TASK1 = createTask();

    // Names of the Bound Bean Properties *************************************

    public static final String PROPERTYNAME_NAME      = "name";
    public static final String PROPERTYNAME_START_DATE    = "startDate";
    public static final String PROPERTYNAME_END_DATE        = "endDate";
    
    
    // Instance Fields ********************************************************
    
    private String  name;
    private long  startDate;
    private long    endDate;


    // Instance Creation ******************************************************
    
    /**
     * Constructs an empty book with an initial blank review text.
     */
    public Task() {
        name = ""; 
    }
    
    
    private Task(String name, String startDate, String endDate) {
        setName(name);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ENGLISH);
        try {
            setStartDate(dateFormat.parse(startDate));
        } catch (Exception e) {
            System.out.println("Can't parse '" + startDate + "'.");
        }
        try {
            setEndDate(dateFormat.parse(endDate));
        } catch (Exception e) {
            System.out.println("Can't parse '" + endDate + "'.");
        }
    }
    
    
    // Creating Example Instances *********************************************
    
    private static Task createTask() {
        return new Task("Take a break", "12/20/1994", "12/31/1994");
    }

    // Accessors **************************************************************
    
    public String getName() {
        return name;
    }
    
    public Date getStartDate() {
        return new Date(startDate);
    }
    
    public Date getEndDate() {
        return new Date(endDate);
    }

    public void setName(String name) {
        String oldValue = getName();
        this.name = name;
        firePropertyChange(PROPERTYNAME_NAME, oldValue, name);
    }
    
    public void setStartDate(Date newStartDate) {
        Date oldValue = getStartDate();
        startDate = newStartDate.getTime();
        firePropertyChange(PROPERTYNAME_START_DATE, oldValue, newStartDate);
    }
    
    public void setEndDate(Date newEndDate) {
        Date oldValue = getEndDate();
        endDate = newEndDate.getTime();
        firePropertyChange(PROPERTYNAME_END_DATE, oldValue, newEndDate);
    }
    
    // Misc *******************************************************************
    
    /**
     * Returns a string representation of this task
     * that contains the property values in a single text line.
     * 
     * @return a string representation of this task
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer("Task");
        buffer.append(" [name=");
        buffer.append(getName());
        buffer.append("; start date=");
        buffer.append(getStartDate());
        buffer.append("; end date=");
        buffer.append(getEndDate());
        return buffer.toString();
    }

    /**
     * Returns a string representation of this task
     * that contains the property values.
     * 
     * @return a string representation of this task
     */
    public String toWrappedString() {
        StringBuffer buffer = new StringBuffer("Task");
        buffer.append("[\nname=");
        buffer.append(getName());
        buffer.append(";\nstart date=");
        buffer.append(getStartDate());
        buffer.append(";\nend date=");
        buffer.append(getEndDate());
        return buffer.toString();
    }

}

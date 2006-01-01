/*
 * @(#)DateEditComponentAdapter.java
 *
 * Copyright (c) 2003-2004 Stand By Soft, Ltd. All rights reserved.
 *
 * This software is the proprietary information of Stand By Soft, Ltd.  
 * Use is subject to license terms.
 */
package com.standbysoft.component.date.swing.binding;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;

import com.jgoodies.binding.value.ValueModel;
import com.standbysoft.component.date.DefaultDateModel;
import com.standbysoft.component.date.swing.JDateEditComponent;

/**
 * Converts ValueModels to the <code>DateModel</code> interface. 
 * Useful to bind {@link com.standbysoft.component.date.swing.JDateEditComponent}s
 * to a ValueModel.<p>
 * 
 * <strong>Example:</strong><pre>
 * ValueModel model = new PropertyAdapter(book, "published", date);
 * JDateField publishedDateField = new JDateField();
 * publishedDateField.setDateModel(new DateEditComponentAdapter(model));
 * publishedDateField.commitEdit();
 * </pre>
 */
public class DateEditComponentAdapter extends DefaultDateModel {
    /**
     * Refers to the underlying ValueModel that stores the state.
     */
    private final ValueModel subject;

    /**
	 * Constructs a <code>DateEditComponentAdapter</code> on the given subject for
	 * the specified choice.
	 * 
	 * @param subject the subject that holds the value
     * 
	 * @throws NullPointerException if the subject is <code>null</code>
	 */
    public DateEditComponentAdapter(ValueModel subject) {
        if (subject == null)
            throw new NullPointerException("The subject must not be null.");
        this.subject = subject;
        subject.addValueChangeListener(new SubjectValueChangeHandler());
        setDate((Date)subject.getValue());
    }
    
	public void setDate(Date date) {
		super.setDate(date);
		if (subject != null) {
			subject.setValue(date);
		}
	}
	
	public Date getDate() {
		return super.getDate();
	}
    
    // Handles changes in the subject's value.
    private class SubjectValueChangeHandler implements PropertyChangeListener {
        /**
		 * Invokes the protected code to synchronize the adapter's value with
		 * the subject value.
		 * 
		 * @param evt the property change event fired by the subject
		 */
        public void propertyChange(PropertyChangeEvent evt) {
        	setDate((Date)subject.getValue());
        }
    }
    
    /**
     * Binds a date edit component to a specified value model.
     * 
     * @param dateEditComponent component that is binded to a value model
     * @param model value model where the component is binded
     */
    public static void bind(JDateEditComponent dateEditComponent, ValueModel model) {
        dateEditComponent.setDateModel(new DateEditComponentAdapter(model));
        dateEditComponent.commitEdit();
    }
}
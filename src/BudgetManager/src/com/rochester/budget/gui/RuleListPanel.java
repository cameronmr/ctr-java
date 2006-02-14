/*
 * RuleListPanel.java
 *
 * Created on 28 December 2005, 17:10
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IRule;

/**
 *
 * @author Cam
 */
public class RuleListPanel extends AbstractListPanel<IRule>
{
    
    /** Creates a new instance of RuleListPanel */
    public RuleListPanel()
    {    
        super( new GenericListModel<IRule>( DataObjectFactory.loadRules() ), "Rules" );        
    }    
    
    public IRule onNew()
    {
        // create a new rule
        return DataObjectFactory.newRule();
    }            
}

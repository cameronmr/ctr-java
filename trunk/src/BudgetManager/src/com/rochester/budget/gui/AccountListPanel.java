/*
 * AccountListPanel.java
 *
 * Created on 20 December 2005, 20:33
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.gui;

import com.rochester.budget.core.DataObjectFactory;
import com.rochester.budget.core.GenericListModel;
import com.rochester.budget.core.IAccount;

/**
 *
 * @author Cam
 */
public class AccountListPanel extends AbstractListPanel<IAccount>
{
    /** Creates a new instance of AccountListPanel */
    public AccountListPanel()
    {
        super( new GenericListModel<IAccount>( DataObjectFactory.loadAccounts() ), "Accounts" );
    }    
    
    public IAccount onNew()
    {
        // create a new rule
        return DataObjectFactory.newAccount();
    }     
}

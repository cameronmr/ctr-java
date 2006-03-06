/*
 * IStatement.java
 *
 * Created on 27 December 2005, 15:36
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.sql.Date;


/**
 *
 * @author Cam
 */
public interface IStatement extends IDatabaseObject
{          
    IAccount getTransactionAccount();
    void setTransactionAccount( IAccount account );
    
    Date getStatementStart();
    void setStatementStart( final Date startDate );
    
    Date getStatementEnd();
    void setStatementEnd( final Date endDate );
    
    StatementSummary getSummary( final ICategory category, boolean flat );
    StatementSummary getSummary( );
}

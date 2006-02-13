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
import java.util.Collection;

/**
 *
 * @author Cam
 */
public interface IStatement extends IDatabaseObject
{        
    public class StatementSummary
    {
        public StatementSummary( )
        {
            m_value = new MonetaryValue( 0 );
        }
                
        public void addReconciliation( final IReconciliation recon )
        {
            m_reconciliations++;
            
            m_value.addValue( recon.getValue() );
        }
        
        public MonetaryValue m_value;
        public int m_reconciliations;
    };
    
    IAccount getAccount();
    void setAccount( IAccount account );
    
    Date getStatementStart();
    void setStatementStart( final Date startDate );
    
    Date getStatementEnd();
    void setStatementEnd( final Date endDate );
    
    Collection<IAccount> getAccounts( final ICategory category, boolean flat );
    StatementSummary getSummary( final IAccount account, boolean flat );
    StatementSummary getSummary( );
}

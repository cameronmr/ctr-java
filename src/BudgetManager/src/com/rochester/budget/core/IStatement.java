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
import java.util.HashMap;

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
            // Increment the overall summary
            addValue( recon.getValue() );
            
            // Increment the per reconciled account summary
            getSummaryForAccount( recon.getCatecory().getAccount() ).addValue( recon.getValue() );            
        }
        
        public void addValue( final MonetaryValue value )
        {
            m_additions++;
            
            m_value.addValue( value );
        }
        
        private StatementSummary getSummaryForAccount( final IAccount account )
        {
            if ( !m_accounts.containsKey( account ) )
            {
                m_accounts.put( account, new StatementSummary() );
            }
            
            return m_accounts.get( account );
        }
        
        public MonetaryValue m_value;
        public int m_additions;
        public HashMap<IAccount, StatementSummary> m_accounts = new HashMap<IAccount, StatementSummary>();
    };
    
    IAccount getTransactionAccount();
    void setTransactionAccount( IAccount account );
    
    Date getStatementStart();
    void setStatementStart( final Date startDate );
    
    Date getStatementEnd();
    void setStatementEnd( final Date endDate );
    
    StatementSummary getTransactionsSummary( );
    StatementSummary getSummary( final ICategory category, boolean flat );
    StatementSummary getSummary( );
}

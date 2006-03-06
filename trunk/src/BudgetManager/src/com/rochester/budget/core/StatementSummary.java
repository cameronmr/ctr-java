/*
 * StatementSummary.java
 *
 * Created on 6 March 2006, 12:31
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 *
 * @author Cam
 */
public class StatementSummary
{    
    public StatementSummary( final String label )
    {
        m_label = label;
    }

    public void addReconciliation( final IReconciliation recon )
    {
        // Add this to the list of reconciliations
        m_reconciliations.add( recon );
        
        // Increment the per reconciled account summary
        addReconciliationForAccount( recon.getCatecory().getAccount(), recon );            
    }
    
    public MonetaryValue getReconciliationValue( )
    {
        MonetaryValue value = new MonetaryValue();
        for ( IReconciliation recon : m_reconciliations )
        {
            value.addValue( recon.getValue() );
        }
        
        return value;
    }
    
    public int getReconciliationCount( )
    {
        return m_reconciliations.size();
    }
    
    public boolean hasReconciliations()
    {
        return !m_reconciliations.isEmpty();
    }
    
    public void addTransaction( final ITransaction trans )
    {
        m_transactions.add( trans );       
    }
    
    public MonetaryValue getTransactionValue( )
    {
        MonetaryValue value = new MonetaryValue( );
        for ( ITransaction trans : m_transactions )
        {
            value.addValue( trans.getValue() );
        }
        
        return value;
    }
    
    public int getTransactionCount( )
    {
        return m_transactions.size();
    }
    
    public boolean hasTransactions()
    {
        return !m_transactions.isEmpty();
    }
    
    public MonetaryValue getReconciliationValueForAccount( final IAccount account )
    {
        return m_accounts.get( account ).getReconciliationValue();
    }
    
    public Collection<IAccount> getReconciliationAccounts()
    {
        return m_accounts.keySet();
    }
    
    public int getReconciliationCountForAccount( final IAccount account )
    {
        return m_accounts.get( account ).getReconciliationCount();
    }
    
    public String toString()
    {
        return "Statement Summary: " + m_label;
    }
    
    private void addReconciliationForAccount( final IAccount account, final IReconciliation recon )
    {
        if ( !m_accounts.containsKey( account ) )
        {
            m_accounts.put( account, new StatementSummary("") );
        }

        StatementSummary summary = m_accounts.get( account );
        summary.m_reconciliations.add( recon );
    }
    

    private HashMap<IAccount, StatementSummary> m_accounts = new HashMap<IAccount, StatementSummary>();
    private ArrayList<ITransaction> m_transactions = new ArrayList<ITransaction>();
    private ArrayList<IReconciliation> m_reconciliations = new ArrayList<IReconciliation>();
    private String m_label;
}

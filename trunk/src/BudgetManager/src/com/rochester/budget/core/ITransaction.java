/*
 * ITransaction.java
 *
 * Created on 9 February 2005, 20:44
 */

package com.rochester.budget.core;
import com.rochester.budget.core.exceptions.BudgetManagerException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author Cameron
 */
public interface ITransaction extends IDatabaseObject
{    
    enum ReconciliationState 
    {
        NONE,
        PARTIAL,
        FULL
    };
    
    String getKey();
    
    String getNarrative();
    
    String getNote();
    
    Date getDate();
                
    MonetaryValue getValue();
    
    IAccount getAccount();
        
    Object getValue( int col );
    
    String toString();
    
    ReconciliationState getReconciliationState();
    
    List<IReconciliation> getReconciliations();
    
    MonetaryValue getValueRemaining();
    
    void addReconciliation( IReconciliation reconciliation ) throws BudgetManagerException;
    
    void removeReconciliation( IReconciliation reconciliation );
}

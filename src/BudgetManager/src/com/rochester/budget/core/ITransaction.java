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
        
    String getNarrative();
    void setNarrative( final String narrative );
    
    String getNote();
    void setNote( final String note );
    
    Date getDate();
    void setDate( final Date date );
                
    MonetaryValue getMonetaryValue();
    void setMonetaryValue( final MonetaryValue value );
    
    IAccount getAccount();
    void setAccount( final IAccount account );
        
    Object getValue( int col );
    
    String toString();
    
    ReconciliationState getReconciliationState();
    
    List<IReconciliation> getReconciliations();
    
    MonetaryValue getValueRemaining();
    
    void addReconciliation( IReconciliation reconciliation ) throws BudgetManagerException;
    
    void removeReconciliation( IReconciliation reconciliation );
}

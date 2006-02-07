/*
 * IRuleResult.java
 *
 * Created on 29 December 2005, 12:44
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

/**
 *
 * @author Cam
 */
public interface IRuleResult extends IDatabaseObject
{    
    public enum RESULT_TYPE
    {
        FULL { 
            public String toString()
            {
                return "Fully Reconcile";
            }
        },
        PARTIAL { 
            public String toString()
            {
                return "Partially Reconcile";
            }
        }
    };
      
    void applyResult( ITransaction transaction ) throws Exception;
    
    Enum[] getResultTypes();
    RESULT_TYPE getResultType();
    void setResultType( RESULT_TYPE type );
    
    void setReconciliationValue( final MonetaryValue value );
    MonetaryValue getReconciliationValue( );
    
    ICategory getReconciliationCategory( );
    void setReconciliationCategory( final ICategory category );
}

/*
 * Comparators.java
 *
 * Created on 27 December 2005, 22:06
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.rochester.budget.core;

import java.util.Comparator;

/**
 *
 * @author Cam
 */
public class Comparators
{    
    public static final Comparator<IDatabaseObject> ALPHABETICAL_ORDER = 
                                 new Comparator<IDatabaseObject>()
    {
        public int compare( IDatabaseObject e1, IDatabaseObject e2 ) 
        {
            return e1.toString().compareTo( e2.toString() );
        }
    };
    
    public static final Comparator<ITransaction> TRANSACTION_DATE_ORDER = 
                                 new Comparator<ITransaction>()
    {
        public int compare( ITransaction t1, ITransaction t2 ) 
        {
            return t1.getDate().compareTo( t2.getDate() );
        }
    };
    
    public static final Comparator<IStatement> STATEMENT_DATE_ORDER_ASC = 
                                 new Comparator<IStatement>()
    {
        public int compare( IStatement c1, IStatement c2 ) 
        {
            if ( c1.getStatementStart() == null || 
                 c2.getStatementStart() == null )
            {
                return 0;
            }
            
            return c1.getStatementStart().compareTo( c2.getStatementStart() );
        }
    };
    
}

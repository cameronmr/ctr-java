/*
 * MonetaryValue.java
 *
 * Created on 14 February 2005, 21:58
 */

package com.rochester.budget.core;

import com.rochester.budget.core.exceptions.BudgetManagerException;

/**
 *
 * @author Cameron
 */
public class MonetaryValue
{
    /**
     * Creates a new instance of MonetaryValue 
     */
    public MonetaryValue( int cents )
    {
        m_cents = cents;
    }
    
    public MonetaryValue( final MonetaryValue value )
    {
        m_cents = value.m_cents;
    }
    
    public MonetaryValue( final String value )
    {
        int pos = value.indexOf('.');
        m_cents = ( Integer.parseInt( value.substring( 0, pos ) ) * 100 ) + Integer.parseInt( value.substring( pos + 1) );
    }
    
    public String getCents()
    {
        return Integer.toString( m_cents );
    }
    
    public void subtractValue( MonetaryValue value ) throws BudgetManagerException
    {
        // TODO: throw specific exception!
        if ( m_cents - value.m_cents < 0 )
        {
            throw new BudgetManagerException( "Value to subtract larger than available value" );
        }
        
        m_cents -= value.m_cents;
    }
    
    public void addValue( MonetaryValue value )
    {
        m_cents += value.m_cents;
    }
    
    public boolean isZero()
    {
        return m_cents == 0;
    }
        
    public String toString()
    {
        int remainingCents = m_cents % 100;        
        StringBuffer amount = new StringBuffer();
        amount.append( "$");
        amount.append( m_cents / 100 );
        amount.append( remainingCents<10?".0":"." );
        amount.append( remainingCents );
        return amount.toString();
    }
    
    private int m_cents;
}

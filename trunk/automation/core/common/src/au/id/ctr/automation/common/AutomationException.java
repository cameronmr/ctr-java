/*
 * AutomationException.java
 *
 * Created on 4 December 2007, 20:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.common;

/**
 *
 * @author Cameron
 */
public class AutomationException extends Exception
{
    
    /** Creates a new instance of AutomationException */
    public AutomationException(final String msg)
    {
        super(msg, null);
    } 
    
    public AutomationException( final Throwable cause)
    {
        super(null, cause);
    }
    
    public AutomationException(final String msg, final Throwable cause)
    {
        super(msg, cause);
    }
}

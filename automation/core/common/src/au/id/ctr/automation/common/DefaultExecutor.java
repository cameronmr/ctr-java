/*
 * DefaultExecutor.java
 *
 * Use this executor throughout for sending notifications
 */

package au.id.ctr.automation.common;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author Cameron
 */
public class DefaultExecutor
{
    private static Executor executor = Executors.newCachedThreadPool();
    
    public static Executor getExecutor()
    {
        return executor;
    }
}

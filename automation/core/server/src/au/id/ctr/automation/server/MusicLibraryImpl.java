/*
 * MusicLibraryImpl.java
 *
 * Created on 12 December 2007, 20:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.server;

import au.id.ctr.automation.common.AbstractLibrary;
import au.id.ctr.automation.mbeans.MusicLibraryMBean;

/**
 *
 * @author Cameron
 */
public class MusicLibraryImpl extends AbstractLibrary implements MusicLibraryMBean
{
    public MusicLibraryImpl()
    {
        super(MusicLibraryMBean.class);
    }
    
    public String getFile()
    {
        return "woot";
    }
}

/*
 * MusicLibraryMBean.java
 *
 * Created on 12 December 2007, 20:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.mbeans;

import au.id.ctr.automation.common.interfaces.Library;

/**
 *
 * @author Cameron
 */
public interface MusicLibraryMBean extends Library
{
    String getFile();
}

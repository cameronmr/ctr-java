/*
 * WinampRendererImpl.java
 *
 * Created on 12 December 2007, 20:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.ctr.automation.server;

import au.id.ctr.automation.common.AbstractRenderer;
import au.id.ctr.automation.mbeans.WinampRendererMBean;
import java.net.MalformedURLException;
import java.util.logging.Logger;

/**
 *
 * @author Cameron
 */
public class WinampRendererImpl extends AbstractRenderer implements WinampRendererMBean
{
    private static final int port = 8011;
    private static final String host = "192.168.1.20";
    private static final Logger logger = Logger.getLogger(WinampRendererImpl.class.getName());
    
    private boolean isPlaying = false;
    
//    private XmlRpcClient client;
    /** Creates a new instance of WinampRendererImpl */
    public WinampRendererImpl(final String zone) throws MalformedURLException
    {
        super(WinampRendererMBean.class, zone);        
//        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
//        config.setServerURL(new URL("http://127.0.0.1:8011/"));
//        
//        client = new XmlRpcClient( );
//        client.setConfig(config);     
        
        
        
    }

    public void playFile(final String file)
    {
        logger.info("Zone [" + getZone() + "] Received play command: " + file);
//        try
//        {
//            
//            if (isPlaying)
//            {
//                // stop
//                client.execute("stop", Collections.emptyList());
//                isPlaying = false;
//            }
//            else
//            {
//                // play
//                client.execute("play", Collections.emptyList());
//                isPlaying = true;
//            }
//        }
//        catch (XmlRpcException ex)
//        {
//            logger.log(Level.WARNING, "Error executing xml-rpc", ex);
//        }
    }
}

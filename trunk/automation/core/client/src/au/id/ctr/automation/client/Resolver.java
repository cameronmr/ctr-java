package au.id.ctr.automation.client;

/*
 * Resolver.java
 *
 * Resolve manager reference. Creates NodeClients for each
 * Node reference.
 */

import au.id.ctr.automation.common.AutomationException;
import au.id.ctr.automation.common.ManagerClient;
import au.id.ctr.automation.common.NodeRef;
import au.id.ctr.automation.mbeans.LibraryMXBean;
import au.id.ctr.automation.mbeans.NodeMXBean;
import au.id.ctr.automation.mbeans.ZoneMXBean;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.management.remote.JMXServiceURL;

/**
 *
 * @author Cameron
 */
public class Resolver
{
    private static final Logger logger = Logger.getLogger(Resolver.class.getName());
    private static Resolver instance = null;
    private Map<String, NodeMXBean> nodes = new HashMap<String,NodeMXBean>();
    private ManagerClient theManager;
    
    private Resolver() throws AutomationException
    {
        // Load up all the nodes!
        // Create the Server connections to the manager
        theManager = new ManagerClient("service:jmx:rmi:///jndi/rmi://cameron:1506/jmxrmi");
        theManager.connect();
        
        for (NodeRef ref : theManager.getNodes())
        {
            logger.info("Found node: " + ref.getName());
            try
            {
                logger.info(ref.getName() + " url " + ref.getURL().toString());
                nodes.put(ref.getName(), 
                        new NodeWrapper(ref.getName(), new JMXServiceURL(ref.getURL())));
            } 
            catch (MalformedURLException ex)
            {
                throw new AutomationException(ex);
            } 
        }
    }
    
    public static Resolver getInstance() throws AutomationException
    {
        if (instance == null)
        {
            instance = new Resolver();
        }
        return instance;
    }
    
    public static List<ZoneMXBean> getZones(final String node) throws AutomationException
    {
        return getInstance().nodes.get(node).getZones();
    }
    
    public static ZoneMXBean getZone(final String zone) throws AutomationException
    {
        for (NodeMXBean node : getInstance().nodes.values())
        {
            ZoneMXBean z = node.getZone(zone);
            if (z != null)
            {
                return z;
            }
        }
        return null;
    }
    
    public static List<LibraryMXBean> getLibraries() throws AutomationException
    {
        List<LibraryMXBean> libs = new ArrayList<LibraryMXBean>();
        for (NodeMXBean node : getInstance().nodes.values())
        {
            libs.addAll(node.getLibraries());
        }
        return libs;
    }
    
    public static <T extends LibraryMXBean> T getLibrary(Class<T> clss) throws AutomationException
    {
        for (NodeMXBean node : getInstance().nodes.values())
        {
            T lib = (T)node.getLibrary(clss.getName());
            if (lib != null)
            {
                return lib;
            }
        }
        return null;
    }
    
}

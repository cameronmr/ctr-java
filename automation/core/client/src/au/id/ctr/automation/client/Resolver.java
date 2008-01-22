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
import au.id.ctr.automation.common.interfaces.Zone;
import au.id.ctr.automation.common.interfaces.Library;
import au.id.ctr.automation.common.interfaces.Node;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Cameron
 */
public class Resolver
{
    private static final Logger logger = Logger.getLogger(Resolver.class.getName());
    private static Resolver instance = null;
    private Map<String,NodeClient> nodes = new HashMap<String,NodeClient>();
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
            logger.info(ref.getName() + " url " + ref.getURL().toString());
            nodes.put(ref.getName(), new NodeClient(ref.getName(), ref.getURL()));
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
    
    public static List<Zone> getZones(final String node) throws AutomationException
    {
        return getInstance().nodes.get(node).getZones();
    }
    
    public static Zone getZone(final String zone) throws AutomationException
    {
        for (Node node : getInstance().nodes.values())
        {
            Zone z = node.getZone(zone);
            if (z != null)
            {
                return z;
            }
        }
        return null;
    }
    
    public static List<Library> getLibraries() throws AutomationException
    {
        List<Library> libs = new ArrayList<Library>();
        for (Node node : getInstance().nodes.values())
        {
            libs.addAll(node.getLibraries());
        }
        return libs;
    }
    
    public static <T extends Library> T getLibrary(Class<T> clss) throws AutomationException
    {
        for (Node node : getInstance().nodes.values())
        {
            T lib = node.getLibrary(clss);
            if (lib != null)
            {
                return lib;
            }
        }
        return null;
    }
    
}

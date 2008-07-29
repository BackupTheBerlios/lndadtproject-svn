/*
 * Created on Sep 3, 2004
 *
 */

package DADT;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.logging.Level;

import space.Host;
/**
 * This singleton provides access to the DADT Manager of the current site. 
 * @author migliava
 */
public abstract class DADTMgr {

    /**
     * This is the static field containing the DADT Manager of the current Site.
     */
	
	//!! @author galiya TODO: here we have an explicit use of IPMDADTMgr(); 
    public static DADTMgr mgr; //!!= new IPMDADTMgr(); 
    
	protected BindingRegistry registry = new BindingRegistry();
	protected Identificator adtIdentificator;


	static public Logger l = Logger.getLogger("InfoLogger");
	

    /**
     * Metod used to initialize the system 
     */
	public static void Initialize(DADTMgr mgr) {
		DADTMgr.mgr = mgr;
        
        l.setLevel(Level.ALL);
    }

	
	/**
	 * @param name
	 */
	protected void bind(Object adtInstance, String DADTClassName) {
		registry.register(adtInstance, DADTClassName);
//		//probably it is not needed (on the first identification 
//		//it generates the identifier
//		adtIdentificator.identify(adtInstance);
	}
	
    /**
     * @param adtInstance
     * @param class1
     */
    protected void unbind(Object adtInstance, String DADTClassName) {
        registry.unregister(adtInstance,DADTClassName);
    }

    /**
     * @param dv
     * @return
     */
    public Collection getInstances(String DADTClassName) {
        return registry.getLocalInstances(DADTClassName);
    }
    
    //TODO The problem here is that if we allow more than one space DADT than we have to define
    //the semantic of "selecting operator according to the 
    //it is a hack: we should have some place method at least in the api.
    //plus should we be able to use the same bind method for both spaceADTs and dataADTs
	static Host host;
	static Class spaceDADTClass;
	
    /**
     * This will bound the passed host set the current adt under which bound 
     * adt are automatically "placed" (as for now explicit place is not possible)
     * @param host
     * @param spaceDADTClass
     */
	/*
	public static void setSpaceADT(Host host, Class spaceDADTClass) {
	    if (DADTMgr.host != null)
            DADTMgr.mgr.unbind(DADTMgr.host,DADTMgr.spaceDADTClass.getName());
        DADTMgr.host = host;
	    DADTMgr.spaceDADTClass = spaceDADTClass;
        DADTMgr.mgr.bind(DADTMgr.host,DADTMgr.spaceDADTClass.getName());
	}
	*/
    
    public static Host getSpaceADT() { return host; };
	
    public Identificator getAdtIdentificator() {
        return adtIdentificator;
    }
}

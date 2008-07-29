/*
 * Created on Aug 31, 2004
 * 
 * There is an old issue which survived up to the final version.
 * The question is here if (un)bind can/should be controlled by the DADT Class.
 * 
 * 
 * In the article we didn't addressed the issue. However in discussions
 * before the submission GP agreed that could be a feature worth implementing.
 * For now DADTMgr has internal API presented in the paper, which offer the advantage
 * that DADT class could be absent on the site that export the ADT. 
 */

package DADT;

import java.lang.reflect.Modifier;

import DADT.LNSupport.NodeMgr;

/**
 * This class provides access to the bind/unbind operations.
 * @author migliava
 */
public abstract class DADT {

    /**
     * This method can be used to bind ADT instances to the current class and can be used
     * directly within the DADTClass, providing encapsulation of binds.
     * @param instance
     */
	protected void bindADT(Object instance) {
		DADTMgr.mgr.bind(instance,this.getClass().getName());
		// PHASE 0:DISCOVERY
		// TODO publish(subscribe?) my presence on the net. (this.getClass())
		// FIXME what about destruction?
	}

	protected void unbindADT(Object instance) {
	    DADTMgr.mgr.unbind(instance, this.getClass().getName());
	}
	
    /**
     * This static method allows binding ADT instances to a generic DADTClass.
     * @param instance
     * @param DADTClassName 
     */
    public static void bind(Object instance, final String DADTClassName) {
 		DADTMgr.mgr.bind(instance,DADTClassName);
    }
    /**
     * This static method allows unbinding ADT instances to a generic DADTClass.
     * @param instance
     * @param DADTClassName 
     */
    public static void unbind(Object instance, final String DADTClassName) {
        DADTMgr.mgr.unbind(instance,DADTClassName);
    }

    /**
     * @author khasanova
     * Very bad programming here! I have to specify DADT.mgr with an instance of sub-class (!horrible!), but there is no other solution 
     * unless refactor code of DADT and DADTMgr, which is not my main goal at this moment
     * @param mgr
     */
    public static void setMgr(String mgr) {
    	//TODO: refactor DADT and DADTMgr code (no use of instance of sub-class in an abstract class).

    	// 25-06
    	//if (mgr == "LNView") {
    	//	DADTMgr.mgr = new LNADTMgr();
    	//}
    	
    	//else 
    	//if (mgr == "IPMulticastView"){
    	//	DADTMgr.mgr = new IPMDADTMgr();
    	//}
    	
    	
    }
}

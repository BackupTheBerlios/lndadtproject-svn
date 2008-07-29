/*
 * Created on Sep 20, 2005
 *
 */
package DADT.IPMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.logging.Logger;

import DADT.DADTMgr;
import DADT.Functions;
import DADT.InvocationData;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IPMDADTMgr extends DADTMgr {
    
    //if the two are the same we get address already in use exception.
    //Seems that multicast can reusesockets between themeselves but not with
    //unicast. See if it is the case and which is the semantics of port sharing (multicast)  
    static final int IPM_DADT_MGR_MULTICASTPORT = 5000;
    static final int IPM_DADT_MGR_UNICASTPORT = 5001;
    TreeMap ipData = new TreeMap();
	//!! OperatorInvoker opInvoker; //!! commented out on 19-06
    static Logger l = Logger.getLogger("InfoLogger");
	
    DatagramSocket alwaysOnSocket;
//	static {
//		//out = new MulticastSocket(5001);
//	}
	
	/**
     * 
     */
    public IPMDADTMgr() {
        try {

            //TODO probably there is a race condition between this two
            //starts check if we could start the opinvoker from the listener
            //Is this exactly the same as a multi threaded server?
            
        	//!! opInvoker = new OperatorInvoker(); //!! commented out on 19-06
            //!!opInvoker.setDaemon(true); //!! commented out on 19-06
            //!!opInvoker.start(); //!! commented out on 19-06

            adtIdentificator = new IPIdentificator(
                    new InetSocketAddress(InetAddress.getLocalHost(),IPM_DADT_MGR_UNICASTPORT));
            //alwaysOnSocket = new DatagramSocket(IPM_DADT_MGR_UNICASTPORT);
            //new OperatorListener("AlwaysOnSocket",alwaysOnSocket).start();
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
//        } catch (SocketException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
        }
    }
    
	protected void bind(Object adtInstance, String DADTClassName) {
	    int oldsize = registry.getLocalInstances(DADTClassName).size();
        //System.out.println("size:"+oldsize);
        super.bind(adtInstance, DADTClassName);
		
		if (oldsize == 0 && registry.getLocalInstances(DADTClassName).size()==1) {
		    //startListeningThreadFor(DADTClassName); //!! commented out 18-06 
		}

	}
	
	
    /**
     * @param DADTClassName
     */
    private void startListeningThreadFor(String DADTClassName) {
        String IPgroupName = DADTClassName;
        
        
        InetSocketAddress groupAddress = new InetSocketAddress(
                Algorithms.hash(IPgroupName), IPM_DADT_MGR_MULTICASTPORT);
        
        DADTMgr.l.info("InetSocketAddress is: " + groupAddress.toString());
        
        MulticastSocket m;
        
        try {
            m = new MulticastSocket(groupAddress);
            InetAddress address = Algorithms.hash(IPgroupName);
            m.joinGroup(address);
            
            //!! commented out on 19-06, problems between jist and thread
            //!! OperatorListener w = new OperatorListener(IPgroupName,m);
            //!! w.setDaemon(true);
            //!! w.start();
            
            //TODO should we encapsulate the thread into the IPinfo
            //which is more clean from an ecapsulation perspective?
            //ipData.put(IPgroupName,new IPMinfo(m,w));
            DADTMgr.l.info("Listening for "+ IPgroupName + "(groupname " + IPgroupName + ") on Address " + groupAddress);
        } catch (IOException e) {
            System.err.println("Error: is multicast enabled?");
            e.printStackTrace();
        }
        
    }

    protected void unbind(Object instance, String DADTClassName) {
        assert registry.getLocalInstances(DADTClassName).size()>0;
        assert ipData.containsKey(DADTClassName);
		super.unbind(instance, DADTClassName);
		
        if (registry.getLocalInstances(DADTClassName).size()==0) {
            IPMinfo info = (IPMinfo)ipData.get(DADTClassName);
            info.getWaiter().halt();
            info.getM_socket().close();
            ipData.remove(DADTClassName);
        }
		//IPMinfo info = ((IPMinfo) ipData.get(groupName));
//        Collection c = info.getDADTinstances();
//		Iterator iter = c.iterator();
//		while (iter.hasNext()) {
//            WeakReference weakRef = (WeakReference) iter.next();
//            if (weakRef.get()==instance) iter.remove();
//        }
//		//This will cause problems with some test... investigate when we have time.
//		if (c.isEmpty())
//		    info.waiter.halt();
//			registry.remove(groupName);
//			//notify NO_MORE_ADTS
    }
}


    /**
    	 * @author migliava
    	 * This class define the information that the runtime should keep 
    	 * regarding a given DADT including instances and the socket for listening to calls
    	 * for membership evaluation.
    	 */
    	class IPMinfo {

    		MulticastSocket m_socket;
    		DADT.IPMulticast.OperatorListener waiter; //!! IPMDADTMgr.OperatorListener waiter;

    		/**
             * @param m
             * @param w
             */
            public IPMinfo(MulticastSocket m, //!!IPMDADTMgr.OperatorListener w
            		DADT.IPMulticast.OperatorListener w) {
    			super();
    			this.m_socket = m;
    			this.waiter = w;
            }

    		MulticastSocket getM_socket() {
    			return m_socket;
    		}

    		
    		DADT.IPMulticast.OperatorListener getWaiter() //!!IPMDADTMgr.OperatorListener getWaiter() 
    		{
    			return waiter;
    		}

    	}

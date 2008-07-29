/*
 * Created on Sep 30, 2005
 *
 */
package DADT.IPMulticast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import DADT.Functions;

/**
 * @author migliava
 */
public class Algorithms {

	private static long TIMEOUT = 100;
    
    static {
        if (System.getProperty("timeout")!=null)
            TIMEOUT = Integer.parseInt(System.getProperty("timeout"));
    }
    
    /**
     * This method calls the collectReplies method and than the consolidateReplies methods
     *
     * @return a collection of objects containing all the replies
     * @throws ClassNotFoundException
     */
	public static Collection collectAndConsolidateReplies(DatagramSocket receivingSocket) {
        
        ArrayList<DatagramPacket> replies = collectReplies(receivingSocket);
        //System.out.println("size:"+replies.size());
        Collection resultData = consolidateReplies(replies);
    
        return resultData;
    }

    
    /**
     * This method collects replies on a socket and returns a collection of DatagramPackets
     * @param receivingSocket
     * @return an ArrayList of DatagramPackets with the replies (each reply is a Collection of Objects)
     */
    public static ArrayList<DatagramPacket> collectReplies(DatagramSocket receivingSocket) {
        ArrayList<DatagramPacket> replies = new ArrayList<DatagramPacket>(); 	//final list of replies
        int repliesN = 0;
        long start = new Date().getTime();
        int millisToTimeout;
        
        DatagramPacket reply = Algorithms.prepareDatagramForReceiving(Functions.PKT_LENGHT);
       
        while ((millisToTimeout = (int) (start + TIMEOUT - new Date().getTime())) > 0) {
        	try {
        	    //DANGER: we could mix replies from different invocations!
        	    //can we use different sockets to be sure that the replies comes
        	    //in only from the correct query?
        		
        		receivingSocket.setSoTimeout(millisToTimeout);
        		
        		//IPMDADTMgr.l.fine("waiting IPALL replies");
        		
        		receivingSocket.receive(reply);
        		replies.add(reply);
        		reply = Algorithms.prepareDatagramForReceiving(Functions.PKT_LENGHT);					
        		repliesN++;
        	} catch (SocketTimeoutException to) {
        	    //System.err.println("timeout expired");
        		IPMDADTMgr.l.info("Timeout expired");
        		//to.printStackTrace();
        		break;
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
    
        IPMDADTMgr.l.info("I received " + repliesN + " IPMALL replies");
        return replies;
    }

    /**
     * This method gets an ArrayList of DatagramPackets and assuming that
     * each DatagramPacket contains a collection of Objects returns a single arrayList
     * with all the Objects.
     * 
     * @param replies is an ArrayList of DatagramPackets with a Collection of Object
     * @return a collection of objects 
     */
    public static Collection consolidateReplies(ArrayList<DatagramPacket> replies) {
       
    	Collection resultData = new ArrayList<DatagramPacket>();
        Iterator i = replies.iterator();
        
        while (i.hasNext()) {
        	try 
        	{
                resultData.addAll((Collection) Algorithms.makeObjectFromUdpPacket((DatagramPacket)i.next()));
            } 
        	catch (ClassNotFoundException e) 
            {
                // Received strange collection 
                e.printStackTrace();
            }
        }
        
        IPMDADTMgr.l.info("containing "+resultData.size()+" results");
        
        return resultData;
    }


    
    /**
     * This method retrieves Objects from UDPPacket
     * 
     * @param pkt a Datagram packet to be read
     * @return an Object containing payload of the datagram packet
     */
    public static Object makeObjectFromUdpPacket(DatagramPacket pkt) throws ClassNotFoundException {
    	assert pkt.getLength() < Functions.PKT_LENGHT;
    	byte[] data = pkt.getData();
    	ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
    	ObjectInputStream objectInputStream;
    	try {
    		objectInputStream = new ObjectInputStream(byteArrayInputStream);
    		return objectInputStream.readObject();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    /**
     * This function builds a new UDPPacket addressed to destination which 
     * contains payload 
     *  
     * @param payload the object to be serialized
     * @param dest the address of the destination
     * @return
     */
    public static DatagramPacket makeUDPPacketFromObject(Serializable payload,
    		SocketAddress dest) {
    	ByteArrayOutputStream buff = new ByteArrayOutputStream();
    	try {
    		new ObjectOutputStream(buff).writeObject(payload);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	//assert buff.size() < Functions.PKT_LENGHT;
    	System.err.println("sent packet "+ buff.size() + "bytes long");
    	try {
            return new DatagramPacket(buff.toByteArray(), buff.size(), dest);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Initialise a datagram to be used for receiving data from the socket
     * @param length TODO
     * @return created datagram packet
     */
    public static DatagramPacket prepareDatagramForReceiving(int length) {
    	byte[] buff = new byte[length];
    	DatagramPacket p = new DatagramPacket(buff,length);
    	return p;
    }

   
    /**
     * Calculate a hash value of the groupname 
     * @param s groupname
     * @return calculated InetAddress for the hash value
     */
    public static InetAddress hash(String s) {
    	try {
    		int hash = s.hashCode();
    		byte hash0 = (byte) (hash % 256);
    		return InetAddress.getByAddress(new byte[] {(byte) 234, 
    													0,
    													0,
    													hash0});
    	} catch (UnknownHostException e) {
    		return null;
    	}
    }

}

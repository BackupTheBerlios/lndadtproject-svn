package DADT.IPMulticast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import DADT.DADTMgr;
import DADT.Functions;



// TODO we need to add a claner Thread to clean up the dead sockets 
// and Thread (also dead weakreferences?), this can be done infrequently.
public  class OperatorListener extends Thread {
	//Logger l = Logger.getLogger("DADTMgr.OperatorListener");
	
	DatagramSocket socket;
	boolean running = false;
	/**
	 * @param groupName
	 * @param m
	 */
	public OperatorListener(String groupName, DatagramSocket m) {
		super("OperatorListener for "+groupName);
		socket = m;
	}

	public void run() {
		running = true;
		
		
		while (running) {
			try {
				
			DatagramPacket p = Algorithms.prepareDatagramForReceiving(Functions.PKT_LENGHT);
				
//				byte[] buff = new byte[DADTMgr.PKT_LENGHT];
//				DatagramPacket p = new DatagramPacket(buff,buff.length);
//				


					DADTMgr.l.info("before socket.receive(p);");

				socket.receive(p);
				
					DADTMgr.l.info("after socket.receive(p);");
				
				
				DADTMgr.l.info("Membership InvocationData received");
				//!!opInvoker.sendDADTinstances(p); //!! commented out on 19-06

			} catch (SocketException se) {
			    if (se.getMessage().startsWith("Socket closed"))
			        DADTMgr.l.info("Socket closed");
			    else 
			        se.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
				
				catch (Exception e) {   
					DADTMgr.l.info("Some unknown exception");
					e.printStackTrace();
				}
		}
	}
	
	public void halt() {
		// TODO send a packet to myself to wake up and set running to false.
		// as an alternative (which could cause packet loss) we could specify a timeout 
		// for the socket and catch the timeout exception or catch the Socket Close exception  
	    running = false;
	    socket.close();
	}
}
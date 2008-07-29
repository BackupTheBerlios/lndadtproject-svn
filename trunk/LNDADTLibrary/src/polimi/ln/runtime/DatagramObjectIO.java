/***
 * * PROJECT
 * *    Logical Neighborhoods - J2ME Run-Time Support 
 * * VERSION
 * *    $LastChangedRevision: 118 $
 * * DATE
 * *    $LastChangedDate: 2008-05-28 19:35:12 +0200 (Wed, 28 May 2008) $
 * * LAST_CHANGE_BY
 * *    $LastChangedBy: lmottola $
 * *
 * *    $Id: DatagramObjectIO.java 118 2008-05-28 17:35:12Z lmottola $
 * *
 * *   Logical Neighborhoods - 
 * *                   Programming Wireless Sensor Networks
 * *                   (One Slice at a Time)
 * *
 * *   This program is free software; you can redistribute it and/or
 * *   modify it under the terms of the GNU Lesser General Public License
 * *   as published by the Free Software Foundation; either version 2.1
 * *   of the License, or (at your option) any later version.
 * *
 * *   This program is distributed in the hope that it will be useful,
 * *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 * *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * *   GNU General Public License for more details.
 * *
 * *   You should have received a copy of the GNU Lesser General Public 
 * *   License along with this program; if not, you may find a copy at 
 * *   the FSF web site at 'www.gnu.org' or 'www.fsf.org', or you may 
 * *   write to the Free Software Foundation, Inc., 59 Temple 
 * *   Place - Suite 330, Boston, MA  02111-1307, USA
 ***/

package polimi.ln.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import polimi.ln.examples.swans.SimulationReferences;


/**
 * This class provides methods for sending and receiving objects using UDP
 * datagrams. Objects serialization is carried out using suitable
 * ByteInputStream and ByteOutputStream objects.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class DatagramObjectIO implements Runnable {

	// The local port we use to receive UDP datagrams
	private int port;

	// The size of the receiver buffer
	private int bufferSize;

	// A reference to a datagram socket used for receiving datagrams
	private DatagramSocket UDPsck;

	// The IPv4 broadcast address
	private static final String INET_BROADCAST_ADDR = "255.255.255.255";

	private boolean running;

	private Vector listeners;

	private int timeout;

	/**
	 * Build a new object for sending and receiving objects via UDP datagrams.
	 * 
	 * @param port
	 *            the port at which we are going to receive UPD datagrams.
	 * @param bufferSize
	 *            the size of the receiver buffer.
	 * @param timeout
	 *            a timeout for receiving UDP datagrams.
	 */
	public DatagramObjectIO(int port, int bufferSize, int timeout) {

		this.port = port;
		this.running = false;
		this.bufferSize = bufferSize;
		this.listeners = new Vector();
		this.timeout = timeout;
	}

	// For SWANS simulation
	public static void main(String[] args) {
		DatagramObjectIO objectIO = SimulationReferences.getObjectIO(Integer
				.parseInt(args[0]));
		objectIO.startSim();
	}

	public void addListener(ObjectIOListener l) {
		listeners.add(l);
	}

	public void startSim() {
		running = true;
		try {
			this.UDPsck = new DatagramSocket(port);
			// UDPsck.setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		run();
	}

	public void start() {
		running = true;
		try {
			UDPsck = new DatagramSocket(port);
			UDPsck.setSoTimeout(timeout);
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		new Thread(this).start();
	}

	/**
	 * Send an object to the Ipv4 local broadcast address. The receiver host has
	 * to wait for incoming objects at the same port as the local
	 * DatagramObjectIO object.
	 * 
	 * @param obj
	 *            the object that gets sent.
	 */
	public void sendBroadcastObject(Object obj) {

		sendObject(obj, INET_BROADCAST_ADDR);
	}

	/**
	 * Send the object o to the the specified Ipv4 address. The receiver host
	 * has to wait for incoming objects at the same port as the local
	 * DatagramObjectIO object.
	 * 
	 * @param obj
	 *            the objects that gets sent.
	 * @param hostAddress
	 *            the IPv4 address of the intended receiver.
	 */
	public void sendObject(Object obj, String hostAddress) {

		try {
			ByteArrayOutputStream byteStreamO = new ByteArrayOutputStream(
					bufferSize);
			ObjectOutputStream oos = new ObjectOutputStream(byteStreamO);
			oos.writeObject(obj);
			oos.flush();

			// Retrieves byte array
			byte[] sendBuf = byteStreamO.toString().getBytes();

			// Sending the serialized object
			DatagramPacket packet = new DatagramPacket(sendBuf, sendBuf.length,
					InetAddress.getByName(hostAddress), port);
			UDPsck.send(packet);
			oos.close();
			byteStreamO.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Receive an object at the local port specified at the creation of the
	 * object.
	 * 
	 * @return the object received.
	 */
	private Object receiveObject() {

		try {
			byte[] recvBuf = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);

			// Receives a UDP datagram
			UDPsck.receive(packet);

			// Unmarshal the object
			String s = new String(packet.getData(), packet.getOffset(), packet
					.getLength());
			recvBuf = s.getBytes();

			ByteArrayInputStream byteStreamI = new ByteArrayInputStream(recvBuf);
			ObjectInputStream ois = new ObjectInputStream(byteStreamI);
			Object obj = ois.readObject();
			ois.close();
			byteStreamI.close();
			return obj;
		} catch (InterruptedIOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (null);
	}

	/**
	 * Return the port currently in use for receiving UDP datagrams.
	 * 
	 * @return the port used for receiving UDP datagrams.
	 */
	public int getPort() {
		return port;
	}

	public void run() {
		while (running) {

			Object o = receiveObject();
			if (o != null) {
				Iterator it = listeners.iterator();
				while (it.hasNext()) {
					// Clone the object for multiple listeners?
					((ObjectIOListener) it.next()).objectReceived(o);
				}
			}
		}
	}

	public void stop() {
		running = false;
	}

}

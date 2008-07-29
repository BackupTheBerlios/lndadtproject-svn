/*
 * Copyright (c) 2007 Sun Microsystems, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package unitn.dadtln.samples;

import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.peripheral.NoAckException;
import com.sun.spot.peripheral.TimeoutException;
import com.sun.spot.peripheral.radio.mhrp.aodv.messages.RERR;
import com.sun.spot.util.IEEEAddress;

import java.io.*;
import java.util.Vector;

import javax.microedition.io.*;

import javax.swing.*;

import org.sunspotworld.demo.util.PacketTypes;

import unitn.dadt.LNSupport.LNSupportReplyMsg;
import unitn.dadt.internals.ResultData;

public class SPOTDataListener extends Thread implements PacketTypes {
        
	private boolean baseStationPresent = false;
    
	private RadiogramConnection conn = null;
    private Radiogram xdg = null;
    
    private boolean running = true;
    private boolean connected = false;
    private long spotAddress = 0;
    
    private ClientNode clientNode;
    
    /**
     * Create a new listener to connect to the remote SPOT over the radio.
     */
    public SPOTDataListener () {
        System.out.println("Lstener constructor");
    	init();
        
        
    }

    public void setMasterApp(ClientNode clientNode){
    	this.clientNode = clientNode;
        
        // we ping sensors in the network
		doSendData(PacketTypes.PING_REQ); 
    
    }
    
    /**
     * Connect to base station & other initialization.
     */
    private void init () {
        RadiogramConnection rcvConn = null;
        try {
            rcvConn = (RadiogramConnection)Connector.open("radiogram://:" + BROADCAST_PORT);
            baseStationPresent = true;
        } catch (Exception ex) {
            baseStationPresent = false;
            System.out.println("Problem connecting to base station: " + ex);
        } finally {
            try {
                if (rcvConn != null) {
                    rcvConn.close();
                }
            } catch (IOException ex) { /* ignore */ }
        }
    }

        /**
     * Send a request to the remote SPOT to start or stop sending accelerometer readings.
     *
     * @param sendIt true to start sending, false to stop
     * @param gView the GraphView display to pass the data to
     */
    public void doSendData(byte msgType){
        
    	System.out.println("doSendData, Command " + msgType);
    	sendCmd(msgType);
    }

    /**
     * Send a simple command request to the remote SPOT.
     *
     * @param cmd the command requested
     */
    private void sendCmd (byte cmd) {
        if (conn != null) {
        	System.out.println("realSendCmd, Command " + cmd);
            try {
                xdg.reset();
                xdg.writeByte(cmd);
                conn.send(xdg);
            } catch (NoAckException nex) {
                connected = false;
            } catch (IOException ex) {
                // ignore any other problems
            }
        }
    }


    
    
     //Main runtime loop to connect to a remote SPOT.
     //Do not call directly. Call start() instead.
     
    public void run () {
        if (baseStationPresent) {
            System.out.println("Client Node Thread Started ...");
            hostLoop();
        }
    }
    

    /**
     * Process data sent by remote SPOT. 
     * Deliver data to the client node
     *
     * @param dg the packet containing the data
     */
    private void receiveMsg (Datagram dg) {
        try {
            final String address = dg.getAddress();
            final double reading = dg.readDouble();
            final String adtName= dg.readUTF();
//            System.out.println(address + ", " + adtName + ", " + reading);
           	
           	// create replyMsg for DADT //
           	
           	Vector replyData = new Vector();
           	replyData.addElement(new ResultData(reading, adtName + " at " + address));
           	
           	LNSupportReplyMsg replyMsg = new LNSupportReplyMsg(0, replyData);
           	
           	clientNode.deliver(replyMsg);
           	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // Wait for a remote SPOT to request a connection.
    private void waitForSpot () {
        RadiogramConnection rcvConn = null;
        DatagramConnection txConn = null;
        spotAddress = 0;
        
        try {
            rcvConn = (RadiogramConnection)Connector.open("radiogram://:" + BROADCAST_PORT);
            rcvConn.setTimeout(10000);             // timeout in 10 seconds
            Datagram dg = rcvConn.newDatagram(rcvConn.getMaximumLength());            
            
            while (true) {
                try {
                    dg.reset();
                    rcvConn.receive(dg);            // wait until we receive a request
                    
                    if (dg.readByte() == LOCATE_DISPLAY_SERVER_REQ) {       // type of packet
                        String addr = dg.getAddress();
                        IEEEAddress ieeeAddr = new IEEEAddress(addr);
                        long macAddress = ieeeAddr.asLong();
                        System.out.println("Received request from: " + ieeeAddr.asDottedHex());
                        Datagram rdg = rcvConn.newDatagram(10);
                        rdg.reset();
                        rdg.setAddress(dg);
                        rdg.writeByte(DISPLAY_SERVER_AVAIL_REPLY);        // packet type
                        rdg.writeLong(macAddress);                        // requestor's ID
                        rcvConn.send(rdg);                                // broadcast it
                        spotAddress = macAddress;
                        break;
                    }
                } catch (TimeoutException ex) {
                   ex.printStackTrace();
                }
            }
        } catch (Exception ex) {
            System.out.println("Error waiting for remote Spot: " + ex.toString());
            ex.printStackTrace();
        } finally {
            try {
                if (rcvConn != null) { 
                    rcvConn.close();
                }
                if (txConn != null) { 
                    txConn.close();
                }
            } catch (IOException ex) { }
        }
    }



    
    
    /**
     * Main receive loop. Receive a packet sent by remote SPOT and handle it.
     */
    private void hostLoop() {
        running = true;
        boolean firstRequest = true;

        while (running) {
        	
        	if (firstRequest) 
        	{
        		waitForSpot();   // connect to a Spot
        		firstRequest = !firstRequest;
        	}
        	
            if (spotAddress != 0)
            {
                try 
                {
                	
                	conn = (RadiogramConnection)Connector.open("radiogram://" + spotAddress + ":" + CONNECTED_PORT);
                    conn.setTimeout(1000);             // timeout every second
                    Radiogram rdg = (Radiogram)conn.newDatagram(conn.getMaximumLength());
                    xdg = (Radiogram)conn.newDatagram(10); // we never send more than 1 or 2 bytes
                    connected = true;
                    
                    /*
                    while (connected) 
                    {
                    */
                        try 
                        {
                            conn.receive(rdg);            // wait until we receive a reply

                        } catch (TimeoutException ex) 
                        {
                            continue;
                        }
                        byte packetType = rdg.readByte();
    
                        switch (packetType) 
                        {
                            case MESSAGE_REPLY:

                                receiveMsg(rdg);
                            	String str = rdg.readUTF();
                                System.out.println("Message from sensor: " + str);
                                break;
                        }
                        /*
                	}
                    */
                } 
                catch (Exception ex) 
                {
             
                	System.out.println("Error communicating with remote Spot: " + ex.toString());
                } 
                finally 
                {
                	try 
                	{
                		conn.close();
                		conn = null;
                	}
                	
                	catch (IOException ex) {
                	}
                }
            }
        }
       
    }

}

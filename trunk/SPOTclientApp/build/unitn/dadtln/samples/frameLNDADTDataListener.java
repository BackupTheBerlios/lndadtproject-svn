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

public class frameLNDADTDataListener extends Thread implements PacketTypes {
        
    private boolean baseStationPresent = false;
    private RadiogramConnection conn = null;
    private Radiogram xdg = null;
    private boolean running = true;
    private boolean connected = false;
    private long spotAddress = 0;
    
    private frameLNDADT guiFrame = null;

    /**
     * Create a new AccelerometerListener to connect to the remote SPOT over the radio.
     */
    public frameLNDADTDataListener () {
        init();
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
     * Specify the GUI window that shows whether connected to a remote SPOT.
     *
     * @param fr the TelemetryFrame GUI that will be used to display the connection status to the remote SPOT
     */
    public void setGUI (frameLNDADT fr) {
        guiFrame = fr;
        updateConnectionStatus(connected);
    }

    /**
     * Update the GUI with the current connection status.
     */
    private void updateConnectionStatus (boolean isConnected) {
        if (guiFrame != null) {
            final String status;
            final boolean connectedp = isConnected;
            final frameLNDADT fr = guiFrame;
            if (!baseStationPresent) {      // not running in separate thread, so safe to call directly
                guiFrame.setConnectionStatus(false, "No Base Station");
            } else {
                if (isConnected) {
                    IEEEAddress ieeeAddr = new IEEEAddress(spotAddress);
                    status = "Connected to " + ieeeAddr.asDottedHex();
                } else {
                    status = "Not Connected";
                }
                SwingUtilities.invokeLater( new Runnable() {
                                                public void run() {
                                                    fr.setConnectionStatus(connectedp, status);
                                                } } );
            }
        }
    }

                                          


    /**
     * Send a request to the remote SPOT to start or stop sending accelerometer readings.
     *
     * @param sendIt true to start sending, false to stop
     * @param gView the GraphView display to pass the data to
     */
    public void doSendData(byte msgType){
        sendCmd(msgType);
    }



    /**
     * Send a request to the remote SPOT to report on radio signal strength.
     */
    public void reconnect() {
        connected = false;
        updateConnectionStatus(connected);
        announceStarting();
    }

    /**
     * Send a simple command request to the remote SPOT.
     *
     * @param cmd the command requested
     */
    private void sendCmd (byte cmd) {
        if (conn != null) {
            try {
                xdg.reset();
                xdg.writeByte(cmd);
                conn.send(xdg);
            } catch (NoAckException nex) {
                connected = false;
                updateConnectionStatus(connected);
            } catch (IOException ex) {
                // ignore any other problems
            }
        }
    }


    /**
     * Main runtime loop to connect to a remote SPOT.
     * Do not call directly. Call start() instead.
     */
    public void run () {
        if (baseStationPresent) {
            System.out.println("Client Node Thread Started ...");
            hostLoop();
        }
    }

    /**
     * Process telemetry data sent by remote SPOT. 
     * Pass the data gathered to the GraphView to be displayed.
     *
     * @param dg the packet containing the accelerometer data
     * @param twoG the scale that was used to collect the data (true = 2G, false = 6G)
     */
    private void receiveMsg (Datagram dg) {
       
    	System.out.println("in receiveMsg");
        
        try {
            final String address = dg.getAddress();
            
            
            final double reading = dg.readDouble();
            
            final String adtName= dg.readUTF();
            System.out.println(address + ", " + adtName + ", " + reading);
            
                       
           	SwingUtilities.invokeLater( new Runnable() {
                   public void run() {
                       guiFrame.updateReceiveText(address + adtName, reading);
                   } } );
           	
           	// create replyMsg for DADT //
           	
           	Vector replyData = new Vector();
           	replyData.addElement(new ResultData(reading, adtName + " at " + address));
           	
           	LNSupportReplyMsg replyMsg = new LNSupportReplyMsg(0, replyData);
           	
           	frameLNDADT.clientNode.deliver(replyMsg);
           	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Broadcast that the host display server is (re)starting.
     */
    private void announceStarting () {
        DatagramConnection txConn = null;
        try {
            txConn = (DatagramConnection)Connector.open("radiogram://broadcast:" + CONNECTED_PORT);
            Datagram dg = txConn.newDatagram(txConn.getMaximumLength());
            dg.writeByte(DISPLAY_SERVER_RESTART);        // packet type
            txConn.send(dg);                             // broadcast it
        } catch (Exception ex) {
            System.out.println("Error sending display server startup message: " + ex.toString());
            ex.printStackTrace();
        } finally {
            try {
                if (txConn != null) { 
                    txConn.close();
                }
            } catch (IOException ex) { /* ignore */ }
        }
    }
    
    /**
     * Wait for a remote SPOT to request a connection.
     */
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
                    announceStarting();
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
            } catch (IOException ex) { /* ignore */ }
        }
    }

    /**
     * Main receive loop. Receive a packet sent by remote SPOT and handle it.
     */
    private void hostLoop() {
        running = true;
        announceStarting();  // announce we are starting up - in case a Spot thinks it's connected to us
        
        boolean firstRequest = true;

        while (running) {
        	
        	if (firstRequest) {
        		waitForSpot();   // connect to a Spot
        		firstRequest = !firstRequest;
        	}
        	
            if (spotAddress != 0) {
                try {
                	
                	conn = (RadiogramConnection)Connector.open("radiogram://" + spotAddress + ":" + CONNECTED_PORT);
                    conn.setTimeout(1000);             // timeout every second
                    Radiogram rdg = (Radiogram)conn.newDatagram(conn.getMaximumLength());
                    xdg = (Radiogram)conn.newDatagram(10); // we never send more than 1 or 2 bytes
                    connected = true;
                    updateConnectionStatus(connected);
                    
                    /*
                    while (connected) {
                    */
                        try {
                            conn.receive(rdg);            // wait until we receive a reply

                        } catch (TimeoutException ex) {
                            continue;
                        }
                        byte packetType = rdg.readByte();
    
                        switch (packetType) {
                            case MESSAGE_REPLY:

                                receiveMsg(rdg);
                            	String str = rdg.readUTF();
                                System.out.println("Message from sensor: " + str);
                                break;
                        }
                        /*
                    }
                    */
                } catch (Exception ex) {
                    System.out.println("Error communicating with remote Spot: " + ex.toString());
                } finally {
                    try {
                        connected = false;
                        updateConnectionStatus(connected);
                        if (conn != null) { 
                            xdg.reset();
                            xdg.writeByte(PacketTypes.DISPLAY_SERVER_QUITTING);        // packet type
                            conn.send(xdg);                                // broadcast it
                            conn.close();
                            conn = null;
                        }
                    } catch (IOException ex) { /* ignore */ }
                }
            }
        }
    }

}

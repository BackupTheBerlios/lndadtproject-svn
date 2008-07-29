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

public class SPOTDataHandler extends Thread implements PacketTypes {
        
	private boolean baseStationPresent = false;
    
	private RadiogramConnection conn = null;
    private Datagram xdg = null;
    
    private boolean running = true;
    private boolean connected = false;
    private long spotAddress = 0;
    
    private ClientNode clientNode;
    
    
    public void run() {
       
       try {
    	   sendCmd(SEND_TEMP_DATA_REQ);
    	   
    	   RadiogramConnection conn = (RadiogramConnection)Connector.open("radiogram://:" + BROADCAST_PORT);
    	   Datagram xdg = conn.newDatagram(conn.getMaximumLength()); 
   
    	       	   
            // Read sensor sample received over the radio
            conn.receive(xdg);
            String addr = xdg.getAddress();  // read sender's Id
          
            
            double reading = xdg.readDouble();
            String adtName= xdg.readUTF();



// create replyMsg for DADT //
            System.out.println("1");       
            Vector<ResultData> replyData = new Vector<ResultData>();
           	System.out.println("2");           	
           	replyData.addElement(new ResultData(reading, adtName + " at " + addr));
           	System.out.println("3");
           	LNSupportReplyMsg replyMsg = new LNSupportReplyMsg (0, replyData);
           	System.out.println("4");
           	clientNode.deliver(replyMsg);
           	System.out.println("5");       
        } 
   		catch (Exception e) {
         	e.printStackTrace();
        }   
	       
    }
    


    public void setMasterApp(ClientNode clientNode){
    	this.clientNode = clientNode;
    }
    
    /**
     * Send a simple command request to the remote SPOT.
     *
     * @param cmd the command requested
     */
    private void sendCmd (byte cmd) {
        
    	DatagramConnection conn;
		try {
			conn = (DatagramConnection)Connector.open("radiogram://broadcast:" + BROADCAST_PORT);
			xdg = conn.newDatagram(conn.getMaximumLength());
		
    	
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


}

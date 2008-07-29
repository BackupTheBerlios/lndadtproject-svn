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

//import org.sunspotworld.demo.AccelMonitor;
import org.sunspotworld.demo.util.*;

/*
import polimi.ln.runtime.messages.ReplyMsg;
*/
import unitn.dadt.internals.Action;
import unitn.dadt.internals.ExpressionTree;

import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;

//import com.sun.spot.sensorboard.peripheral.LEDColor;
//import com.sun.spot.peripheral.Spot;
import com.sun.spot.io.j2me.radiogram.*;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/*
import polimi.ln.nodeAttributes.Node;
import polimi.ln.nodeAttributes.SetAttribute;

import polimi.ln.runtime.LNDeliver;
import polimi.ln.runtime.LogicalNeighborhoods;
import polimi.ln.runtime.messages.ReplyMsg;
*/


import unitn.dadt.LNSupport.NodeMgr;
import unitn.dadt.LNSupport.LNSupportRequestMsg;
import unitn.dadt.internals.DataView;

public class SensorNode extends MIDlet implements //LNDeliver,
        			PacketTypes {
    
	ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
	  
    public Vector ADTinstances = new Vector(); // collection of ADT instances beloging to the sensor node
    private NodeMgr ADTmgr = new NodeMgr(); // ADT manager that coordinates sensors' readings of ADT instances  
    
    RadiogramConnection rCon = null;
    Datagram dg = null;
    Datagram dgReply = null;
    
    public void init(){
  
    	// create ADT instances and bind them to DADT type
        ADTinstances.addElement(new Sensor(Sensor.TEMP, 1.0));
        ADTinstances.addElement(new Sensor(Sensor.LIGHT, 2.0));
    
        for (Enumeration e = ADTinstances.elements(); e.hasMoreElements();)
	    	ADTmgr.bind((Sensor)e.nextElement(), "unitn.dadtln.samples.DSensor");
    }
    



    /**
     * Main application run loop. Called by Spotlet.
     */
    public void run() {
    	rCon = null;
        dg = null;
        
        try {
            // Open up a broadcast connection to the host port
            // where the 'on Desktop' portion of this demo is listening
            rCon = (RadiogramConnection) Connector.open("radiogram://:" + BROADCAST_PORT);
            dg = rCon.newDatagram(rCon.getMaximumLength());  // only sending 12 bytes of data
            dgReply = rCon.newDatagram(rCon.getMaximumLength()); 
            
         // Main data collection loop
        	while (true) {
            
        		
                // Read sensor sample received over the radio
                rCon.receive(dg);
                	// create replyMsg for DADT //
               	leds[7].setColor(LEDColor.ORANGE);
               	leds[7].setOn();
               	
               	handlePacket(SEND_TEMP_DATA_REQ, dg);
            
        	}
            
        } catch (Exception e) {
            System.err.println("Caught " + e + " in connection initialization.");
            System.exit(1);
        }
    }

    	public void deliver(Object msg) {
		if (msg instanceof LNSupportRequestMsg) 
		{
			//get sensor readings (perform specified action) from ADTinstances 
			
			ADTmgr.processRequestMsg(msg/*, ADTinstances*/, rCon, dg, dgReply); //, ln, info); // process request message 
			leds[7].setOff();
		} 
		/*
		else if (msg instanceof ReplyMsg) 
		{
			info.debugPrint("Sensor Node: Got reply message!");
		}
		*/
	}

	
	// TEMPORARY, before LN will be available
	public void handlePacket(byte type, Datagram pkt) {

		
		DataView tempDV = new DataView(new ExpressionTree(new DSensor_isOfType_Property(Sensor.TEMP)));
		Action tempAction = new DSensor_read_Action();
		String DADTClassName = "unitn.dadtln.samples.DSensor";
		
		LNSupportRequestMsg tempMsg = new LNSupportRequestMsg(pkt.getAddress(), tempDV, tempAction, DADTClassName);
		
		
		switch (type) {
			
		    case SEND_TEMP_DATA_REQ:
		    	deliver(tempMsg);
		        leds[1].setRGB(0, 0, 255);
		        leds[1].setOn();        // green = 2G, blue-green = 6G
		        break;
		    
		    case SEND_LIGHT_DATA_REQ:
		        deliver(tempMsg);
		        leds[1].setRGB(0, 255, 0);
		        leds[1].setOn();        // green = 2G, blue-green = 6G
		        break;

		}
	}

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
		
	}


	protected void startApp() throws MIDletStateChangeException {
		init();
		run();
	}

}

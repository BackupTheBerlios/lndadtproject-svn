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
import polimi.ln.runtime.LNDeliver;


import unitn.dadt.internals.Action;
import unitn.dadt.internals.CompleteView;
import unitn.dadt.internals.DataView;
import unitn.dadt.internals.ExpressionTree;


import com.sun.spot.sensorboard.EDemoBoard;
import com.sun.spot.sensorboard.peripheral.ISwitch;
import com.sun.spot.sensorboard.peripheral.ISwitchListener;
import com.sun.spot.sensorboard.peripheral.ITriColorLED;
import com.sun.spot.sensorboard.peripheral.LEDColor;
import com.sun.spot.util.Utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import unitn.dadt.LNSupport.LNCompleteView;
import unitn.dadt.LNSupport.LNSupportConsts;
import unitn.dadt.LNSupport.NodeMgr;
import unitn.dadt.LNSupport.LNSupportRequestMsg;

public class SensorNodeOnSPOT extends MIDlet implements LNDeliver, ISwitchListener {
    
	ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
	
	ISwitch sw1, sw2;                                 //Swithces on the demo sensor board.
	   	
	  
    public Vector ADTinstances = new Vector(); // collection of ADT instances belonging to the sensor node
    private NodeMgr ADTmgr = null; // ADT manager that coordinates sensors' readings of ADT instances  
    
    /*
     * Initialises ADT instances on the SPOT. Provides binding of these to the DADT type
     */
    public void init(){
  
    	
    	// create ADT instances and bind them to DADT type
        ADTinstances.addElement(new Sensor(Sensor.TEMP, 1.0));
        
        ADTinstances.addElement(new Sensor(Sensor.LIGHT, 2.0));

        
    	ADTmgr = new NodeMgr(this, setLNAttributes());
    	
        for (Enumeration e = ADTinstances.elements(); e.hasMoreElements();)
	    	ADTmgr.bind((Sensor)e.nextElement(), "unitn.dadtln.samples.DSensor");
        
        
	   	 sw1 = EDemoBoard.getInstance().getSwitches()[0];  
	     sw2 = EDemoBoard.getInstance().getSwitches()[1];
	     sw1.addISwitchListener(this);
	     sw2.addISwitchListener(this);
     

    }
    

	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		// TODO Auto-generated method stub
		
	}

	protected void pauseApp() {
		// TODO Auto-generated method stub
	}


	protected void startApp() throws MIDletStateChangeException {
		init();
	}
	
	/**
	 * Specifies attributes of the ADT instances (over LN)
	 */
	public Vector setLNAttributes() {
		Vector attributes = new Vector(); // attributes of the sensor node (and its instances) to be used by LN layer
	
		for (Enumeration e = ADTinstances.elements(); e.hasMoreElements();) 
		{

	    	Vector instanceAttr = ((Sensor)e.nextElement()).collectAttributesForLN();	// collects all attributes among all ADT instances of SensorNode
	    	for (Enumeration i = instanceAttr.elements(); i.hasMoreElements();){
	    		attributes.addElement(i.nextElement());	
	    	}
	    }
		return attributes;
		
	}

	public void deliver(byte[] data) {
		
		leds[7].setColor(LEDColor.GREEN);
		leds[7].setOn();
		
    	ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
		DataInputStream deserializer = new DataInputStream(byteStream);
		int msgType;
		try {
			msgType = deserializer.readInt();
			if (msgType == LNSupportConsts.LNSupportRequestMsg) 
			{
				leds[7].setOff();
				// manual "serialization" of the received array of bytes
				LNSupportRequestMsg reqMsg = new LNSupportRequestMsg(deserializer);
			
				System.out.println("Received a request from " + reqMsg.getSender());
				
				Action action = getReqAction(reqMsg.getAction()); 	
				CompleteView view = getReqDADTView(reqMsg.getDADTListView()); 
				
				if ((action == null) || (view == null)){
					System.out.println("Problem while parsing action or view");
					showErrorOnSPOT();
				}
				else 
					ADTmgr.processRequestMsg(reqMsg, action, view); // process request message 
				
			}
			else 
			{
				System.out.println("Unknown message type: " + msgType);	
				showErrorOnSPOT();
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Action getReqAction(String actionName){
		// this method can't be hided in NodeMgr, because it is application specific (to be generated by JADT preprocessor)
		if (actionName.equalsIgnoreCase("DSensor_read_Action"))
		{	
			return new DSensor_read_Action();
		}		
		else if (actionName.equalsIgnoreCase("DSensor_reset_Action")){
			return new DSensor_reset_Action();
		}
		return null;
	} 
	
	
	/*
	 * Construct (deserialize) DADTView from the list of string values
	 * @param expTreeStructure represents vector of strings, which contains data about DADTView
	 * @return relevanr DADT View if it was created, null if there were errors
	 */
	private CompleteView getReqDADTView(Vector expTreeStructure){
		
		int resError = 0;
		int resOK = 1;
		
		int result = resOK; 

		Stack stack = new Stack();
		Enumeration e = expTreeStructure.elements();

		ExpressionTree expTreeBranch1 = null;
		ExpressionTree expTreeBranch2 = null;
		
		do {
			String elem = (String)e.nextElement();
			
			if (isProperty(elem))
			{ // construct branch 1

				expTreeBranch1 = getExpTreeBranch(elem, (String) e.nextElement());
				if (expTreeBranch1 == null){
					result = resError;
					System.out.println("incorrect property name in branch (1)");
					break;
				}
				stack.push(expTreeBranch1);
				System.out.println("construct branch: " + expTreeBranch1.toString());
				
			}
			
			else if (isBoolOp(elem)){

				expTreeBranch2 = (ExpressionTree) stack.pop();
				expTreeBranch1 = (ExpressionTree) stack.pop();
				// case when expTree and expTree2 have to be merged
				expTreeBranch1 = modifyExpTree(expTreeBranch1, expTreeBranch2, Integer.valueOf(elem).intValue());
				if (expTreeBranch1 == null){
					result = resError;
					System.out.println("incorrect property name in branch (2)");
					break;
				}
				stack.push(expTreeBranch1);
				
				expTreeBranch1 = null;
				expTreeBranch2 = null;

			}
			else
			{ // incorrect sequence
				result = resError;
				System.out.println("incorrect sequence");
				break;
			}
			
		} while (e.hasMoreElements());	

		if (result == resOK) 
		{
			return new LNCompleteView(new DataView((ExpressionTree) stack.pop()));
		}
		else 
			return null;
	}
	
	
	/*
	 * Given Property name creates respective property objects to be used for DADT View construction
	 * Application-dependent code, because properties are declared only in the DADT Application 
	 * @param propertyName String name of the Property
	 * @param value String value which can be used in Property constructor
	 * @return Branch of the expression tree
	 */
	private ExpressionTree getExpTreeBranch(String propertyName, String value){
		ExpressionTree expTreeElem = null;

		if (propertyName.equalsIgnoreCase("DSensor_isActive_Property")){
			expTreeElem = new ExpressionTree (new DSensor_isActive_Property());
		}
			
		
		else if (propertyName.equalsIgnoreCase("DSensor_isOfType_Property")){
			expTreeElem = new ExpressionTree (new DSensor_isOfType_Property(Integer.valueOf(value).intValue()));
		}
		
		else if (propertyName.equalsIgnoreCase("DSensor_isPrecise_Property")){
			expTreeElem = new ExpressionTree (new DSensor_isPrecise_Property(Double.valueOf(value).doubleValue()));
		}
		
		return expTreeElem;
	}
	
	/*
	 * Modifies existing tree depending on the type of operation
	 * @param expTree1 Left branch of the tree
	 * @param expTree2 Right branch of the tree
	 * @param boolOp type of logical operation to be performed over the given tree branches
	 */
	private ExpressionTree modifyExpTree(ExpressionTree expTree1, ExpressionTree expTree2, int boolOp) {
		
		
		ExpressionTree modifiedExpTree = null;
		
		if (expTree2 == null){
			switch (boolOp){
				case ExpressionTree.NOT:
					modifiedExpTree = expTree1.not();
					break;
				default:
					break;
			}
		}
		else {
			switch (boolOp){
			case ExpressionTree.AND:
				modifiedExpTree = expTree1.and(expTree2);
				break;
			case ExpressionTree.OR:
				modifiedExpTree = expTree1.or(expTree2);
				break;
			default:
				break;
			}
		}
		
		return modifiedExpTree;
	}
	
	/*
	 * Checks if the string sequence represents PropertyName
	 * @param stringSeq string sequence to be checked
	 * @return true if the string sequence represents PropertyName represents, false - otherwise
	 */
	private boolean isProperty(String stringSeq){
		if (stringSeq.startsWith("DSensor_"))
			return true;
		else
			return false;
	}
	
	/*
	 * Checks if the string sequence represents PropertyName
	 * @param stringSeq string sequence to be checked
	 * @return true if the string sequence represents PropertyName represents, false - otherwise
	 */
	private boolean isBoolOp(String stringSeq){

	    if (!Character.isDigit(stringSeq.charAt(0)))
	    {
	    	return false;
	    }
	    
		int boolOp = Integer.valueOf(stringSeq).intValue();
		switch (boolOp) {
			case ExpressionTree.AND:
			case ExpressionTree.NOT:
			case ExpressionTree.OR:
			case ExpressionTree.LEAF:
				return true;
		default:
			return false;
		}
	}
	
	
	/*
	 * Utility is used to show an error visually
	 */
	private void showErrorOnSPOT(){
       
		ITriColorLED[] leds = EDemoBoard.getInstance().getLEDs();
	    for (int j = 0; j < 2; j ++)
	    {   
			for (int i = 0; i < leds.length; i ++)
	        {
	        	leds[i].setColor(LEDColor.RED);
	        	leds[i].setOn();
	        }
	        Utils.sleep(100);   
	        for (int i = 0; i < leds.length; i ++)
	        {
	        	leds[i].setOff();
	        }
	    }    
	}
	
	// ISwitchListener
	
	public void switchPressed(ISwitch sw) {
        
		//showErrorOnSPOT();
		
		if (sw == sw1) {

        	// ADTinstance 1
    		
			((Sensor) ADTinstances.elementAt(0)).debugShowErrorOnSPOT(LEDColor.CYAN);
			((Sensor) ADTinstances.elementAt(0)).changeActiveState(0);
    		
        	
        } else {
        	
        	// ADTinstance 2

        	((Sensor) ADTinstances.elementAt(1)).debugShowErrorOnSPOT(LEDColor.YELLOW);
        	((Sensor) ADTinstances.elementAt(1)).changeActiveState(1);

        }
    }


	public void switchReleased(ISwitch arg0) {

	}
}

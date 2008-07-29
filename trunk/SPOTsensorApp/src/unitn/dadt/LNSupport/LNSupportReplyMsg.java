package unitn.dadt.LNSupport;

import java.util.Vector;


//import java.io.Serializable;
//import java.util.LinkedList;

/*import DADT.ResultData;*/

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportReplyMsg /*implements Serializable*/ {
   
	    
	private static final long serialVersionUID = 1L;
		
	private int source;
	// javeME deosn't support LinkedLists
	 //private LinkedList/*<ResultData>*/ readings;
	 
	 private Vector readings;

	/**
	 * Creates a new message.
	 * 
	 */
	 //javaME doen't support LinkedList
	//public LNSupportReplyMsg(int sourceNodeId, LinkedList/*<ResultData>*/ readings) {
	 public LNSupportReplyMsg(int sourceNodeId, Vector readings) {
		this.source = sourceNodeId;
		this.readings = readings;
		
	}

	/**
	 * @return sensor readings stored in this message.
	 */
	// javaME doesn't suppot LinkedList 
	//public LinkedList/*<ResultData>*/ getReadings() {
	 public Vector getReadings() {
		System.out.println("getReadings");
		 return readings;
	}
	
	public int getReadingsSender() {
		System.out.println("getReadingsSender");
		return source;
	}
}

package DADT.LNSupport;

import java.io.Serializable;
import java.util.LinkedList;
import DADT.ResultData;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportReplyMsg implements Serializable {
   
	    
	private static final long serialVersionUID = 1L;
		
	private int source;
	private LinkedList<ResultData> readings;

	

	/**
	 * Creates a new message.
	 * 
	 */
	public LNSupportReplyMsg(int sourceNodeId, LinkedList<ResultData> readings) {
		this.source = sourceNodeId;
		this.readings = readings;
		
	}

	/**
	 * @return sensor readings stored in this message.
	 */
	public LinkedList<ResultData> getReadings() {
		return readings;
	}
	
	public int getReadingsSender() {
		return source;
	}
}

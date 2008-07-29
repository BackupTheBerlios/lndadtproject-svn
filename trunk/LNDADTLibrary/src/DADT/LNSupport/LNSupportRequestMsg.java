package DADT.LNSupport;

import java.io.Serializable;

import DADT.DataView;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportRequestMsg implements Serializable {
   
	    
	private static final long serialVersionUID = 1L;
	private int sender;
	
	private String DADTClassName;
	private DataView dv;
	private DADT.Action action;
	

	/**
	 * Creates a new message.
	 * 
	 */
	public LNSupportRequestMsg(int sender, DataView dataview, DADT.Action action, String DADTClassName) {
		this.sender = sender;
		
		this.dv = dataview;
		this.action = action;

		this.DADTClassName = DADTClassName;
	}
	
	/**
	 * @return the action object stored in this message.
	 */	
	public DADT.Action getAction(){
		return action;
	}

	public DataView getDataView() {
		return dv;
	}
	
	/**
	 * @return the original message sender (used to reply back).
	 */
	public int getSender() {
		return sender;
	}
	
	public String getDADTClassName(){
		return DADTClassName;
	}
}

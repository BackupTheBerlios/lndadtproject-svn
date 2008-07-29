package unitn.dadt.LNSupport;

//import java.io.Serializable;

import unitn.dadt.internals.DataView;
import unitn.dadt.internals.Action;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportRequestMsg /*implements Serializable */{
   
	    
	private static final long serialVersionUID = 1L;
	private String sender;
	
	private DataView dv;
	private Action action;
	
	private String DADTClassName;

	/**
	 * Creates a new message.
	 * 
	 */
	public LNSupportRequestMsg(String sender, DataView dataview, Action action, String DADTClassName) {
		this.sender = sender;
		
		this.dv = dataview;
		this.action = action;
		
		this.DADTClassName = DADTClassName;
	}
	
	/**
	 * @return the action object stored in this message.
	 */	
	public Action getAction(){
		return action;
	}

	public DataView getDataView() {
		return dv;
	}
	
	/**
	 * @return the original message sender (used to reply back).
	 */
	public String getSender() {
		return sender;
	}

	public String getDADTClassName(){
		return DADTClassName;
	}
}

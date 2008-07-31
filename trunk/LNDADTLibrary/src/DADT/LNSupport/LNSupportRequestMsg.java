package DADT.LNSupport;

import java.io.Serializable;

import DADT.CompleteView;
import DADT.Action;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportRequestMsg implements Serializable {
   
	    
	private static final long serialVersionUID = 1L;
	private int sender;
	
	
	private String DADTClassName;
	private CompleteView view;
	private Action action;
	

	/**
	 * Creates a new message.
	 * @param sender 
	 * @param DADTview 
	 * @param action 
	 * @param DADTClassName 
	 * 
	 */
	public LNSupportRequestMsg(int sender, CompleteView DADTview, DADT.Action action, String DADTClassName) {
		this.sender = sender;
		
		this.view = DADTview;
		this.action = action;

		this.DADTClassName = DADTClassName;
	}
	
	/**
	 * @return the action object stored in this message.
	 */	
	public DADT.Action getAction(){
		return action;
	}

	/**
	 * @return
	 */
	public CompleteView getDADTView() {
		return view;
	}
	
	/**
	 * @return the original message sender (used to reply back).
	 */
	public int getSender() {
		return sender;
	}
	
	/**
	 * @return
	 */
	public String getDADTClassName(){
		return DADTClassName;
	}
}

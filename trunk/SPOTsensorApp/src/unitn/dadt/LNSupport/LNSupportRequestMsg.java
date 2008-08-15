package unitn.dadt.LNSupport;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import unitn.dadt.internals.CompleteView;
import unitn.dadt.internals.Action;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportRequestMsg {
   
	    
	private static final long serialVersionUID = 1L;
	private int sender;
	private String DADTClassName;
	private CompleteView view;
	private String action;

	/**
	 * Creates a new message.
	 * 
	 */
	public LNSupportRequestMsg(int sender, CompleteView DADTview, String action, String DADTClassName) {
		this.sender = sender;
		
		this.view = DADTview;
		this.action = action;

		this.DADTClassName = DADTClassName;
	}
	
	/**
	 * @return the action object stored in this message.
	 */	
	public String getAction(){
		return action;
	}

	public CompleteView getDADTView() {
		return view;
	}
	
	public int getSender() {
		return sender;
	}
	
	public String getDADTClassName(){
		return DADTClassName;
	}
	
	public byte[] toByteArray(){
		
		//TODO send information about DADT View
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try
		{
			DataOutputStream serializer = new DataOutputStream(byteStream);
			
			serializer.writeUTF("LNSupportRequestMsg");
			serializer.writeInt(sender);
			serializer.writeUTF(action);
			serializer.writeUTF(DADTClassName);
			
			serializer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteStream.toByteArray();
	}
}

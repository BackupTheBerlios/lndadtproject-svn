package unitn.dadt.LNSupport;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import quicktime.io.IOConstants;

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
	
	private Vector viewAsStringList;

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
	
	public LNSupportRequestMsg(DataInputStream deserializer) {
		
		try {
			this.sender = deserializer.readInt();
			this.action = deserializer.readUTF();
			this.DADTClassName = deserializer.readUTF();
			this.viewAsStringList = createDADTView(deserializer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	public Vector getDADTListView() {
		return viewAsStringList;
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
			
			serializer.writeInt(LNSupportConsts.LNSupportRequestMsg);
			serializer.writeInt(sender);
			serializer.writeUTF(action);
			serializer.writeUTF(DADTClassName);
			view.serialize(serializer);
			serializer.writeUTF("#");	// End Of Stream
			
			serializer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return byteStream.toByteArray();
	}
	
	private Vector createDADTView(DataInputStream deserializer) {
		Vector list = new Vector();
		
		try {
			String readStr = deserializer.readUTF();
			
			while (! readStr.equalsIgnoreCase("#")){
				list.addElement(readStr);
				readStr = deserializer.readUTF();
			}
		}
		catch (IOException e){
			System.err.println("nothing is left in the input stream");
		}
		
		return list;
	}
}

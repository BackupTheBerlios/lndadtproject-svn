package unitn.dadt.LNSupport;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import unitn.dadt.internals.ResultData;

/**
 * This class defines a message in the sample application
 * It just contains a selector of adt instances (all, any, specific number).
 * 
 */
public class LNSupportReplyMsg {
   
	    
	private static final long serialVersionUID = 1L;
	private int source;
	private Vector readings;

	public LNSupportReplyMsg(DataInputStream deserializer) {
	
		try {
			this.source = deserializer.readInt();
			
			this.readings = toResultData(deserializer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new message.
	 * 
	 */
	 public LNSupportReplyMsg(int sourceNodeId, Vector readings) {
		this.source = sourceNodeId;
		this.readings = readings;
	 }

	public byte[] toByteArray(){
		
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		DataOutputStream serializer = new DataOutputStream(byteStream);
		
		try
		{	
			serializer.writeInt(LNSupportConsts.LNSupportReplyMsg);	// message type
			serializer.writeInt(source);				// sensor node Id
			serializer.writeInt(readings.size());		// number of readings sent
			
	        for (Enumeration e = readings.elements(); e.hasMoreElements(); ){ 
		        	
	        	ResultData el = (ResultData) e.nextElement(); 
	        	if (el != null){
		        	
	        		serializer.writeUTF(el.getSource());	// ADT instance Id
	        		serializer.writeDouble(el.getData());	// ADT instance reading
	        		
	        		//System.out.println("debug (LNSupportReplyMsg) ResultData, src = " + el.getSource() + ", data = " + el.getData());
	        	}
			}
			serializer.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return byteStream.toByteArray();
	}
	
	private Vector toResultData(DataInputStream deserializer){
		Vector resultList = null;
		
		try {
			int vectorSize = deserializer.readInt();
			if (vectorSize > 0)
			{
				resultList = new Vector();
				for (int i = 0; i < vectorSize; i ++){
					String ADTInstanceId = deserializer.readUTF();
					double reading = deserializer.readDouble();
					
				    resultList.addElement(new ResultData(reading, ADTInstanceId)); 		
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	
	public Vector getReadings(){
		return this.readings;
	}
	
	public int getSource(){
		return this.source;
	}
}

/*
 * 16/09/2005, Created, migliava
 * 19/08/2005, Modified, Khasanova - added signatures for LN support and CLDC support, added constants
 *
 */
package unitn.dadt.internals;


import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import polimi.ln.neighborhoodDefs.Predicate;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface Property {
   
	public static final byte nullValue = 0;
	// property relations
	public static final byte isEqual = 1;
	public static final byte isNotEqual = 2;
	public static final byte isGreater = 3;
	public static final byte isLess = 4;
	
	// property value type
	public static final byte PropertyValueTypeInt = 5;
	public static final byte PropertyValueTypeDouble = 6;
	public static final byte PropertyValueTypeBoolean = 7;
	public static final byte PropertyValueTypeString = 8;
	
	// ----
	public String getDADTClass();
		
	public boolean evaluate(Object o);
    
    public Predicate getDescriptionForLN(); //this method is used to support communication over LN 	
    public void serialize(DataOutputStream stream) throws IOException; 				// handling lack of serialization in CLDC (Sun SPOTs)
}

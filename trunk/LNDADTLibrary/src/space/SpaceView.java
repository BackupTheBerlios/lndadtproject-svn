/*
 * Created on Aug 9, 2005
 *
 */
package space;

import java.io.Serializable;

import DADT.ExpressionTree;
import DADT.Property;

/**
 * @author migliava
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpaceView implements Serializable {
    
    ExpressionTree properties;

    /**
     * @param class1
     * @param string
     * @param classes
     * @param serializables
     */
    public SpaceView(ExpressionTree e) {
        
        properties = e;//new Invocation(spaceDADTClass, methodName, paramTypes, params);

    }

    public boolean isMember(Host spaceADT) {
        //TODO this is a headache loop from the message we get the 
        //spaceview. we call isMember on it passing the instance, then the
        //space.isMember call evaluate on the property (ehm... really it should be the 
        //tree, however) passing the local instance... There is no more direct way?
        return properties.evaluateTree(spaceADT);
    }
    
}

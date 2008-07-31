package unitn.dadtln.samples;

/* javaME doesn't support these
import java.io.Serializable;
import java.util.ArrayList;
*/

import unitn.dadt.internals.Property;
/*
import polimi.ln.neighborhoodDefs.AtomicPredicate;
import polimi.ln.neighborhoodDefs.IntegerSimplePredicate;
import polimi.ln.neighborhoodDefs.SetMapMembershipPredicate;
import polimi.ln.neighborhoodDefs.SetMembershipPredicate;
*/

public class DSensor_isOfType_Property implements /*Serializable,*/ Property {

	public Class getDADTClass() {
		return null;//return DSensor.class;
	}
	private int type;
		
	public DSensor_isOfType_Property(int type)  {
		this.type = type;

	}
	public boolean evaluate(Object o) {
		Sensor local = (Sensor) o;

		{
			return local.type == type;
		}
	}

	/*
	 * To be used later in LN simulation to create predicates
	 */
	/*
	public AtomicPredicate getDescriptionForLN(Object o) {
		
		
		if (o == null)
		{
			return(new SetMembershipPredicate(Sensor.typeToStr(type), 
							SetMembershipPredicate.IS_IN,
							"OnBoardSensors"));
		}
		else 
		{
			
		
			AtomicPredicate predicateValue = null;
			String attrName = Sensor.typeToStr(type);
			
			predicateValue =  ((Property)o).getDescriptionForLN(null);
			attrName = attrName + "_" + ((Property)o).getClassName();
						
			return(new SetMapMembershipPredicate(predicateValue, 
											  SetMembershipPredicate.IS_IN, 
											  attrName));
		}

		//return( new IntegerSimplePredicate ("type", IntegerSimplePredicate.EQUAL, type));
	} 
	*/
    public String toString() {
        return "isOfType_Property (type is " + type + ")";
    }
    
    public String getClassName()
    {
    	return "DSensor_isOfType_Property";
    }
}

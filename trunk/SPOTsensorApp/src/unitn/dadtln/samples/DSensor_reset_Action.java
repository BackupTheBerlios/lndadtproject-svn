package unitn.dadtln.samples;

/* javaME doesn't support this
import java.io.Serializable;
*/


import unitn.dadt.internals.Action;

public class DSensor_reset_Action implements /*Serializable, */ Action  {
	//
	private static final long serialVersionUID = 6050373007371220085L;
	public DSensor_reset_Action()  {

	}
	public Object evaluate(Object o) {
		Sensor local = (Sensor) o;
		{
			local.reset();
			return null;
		}
	}
	
}

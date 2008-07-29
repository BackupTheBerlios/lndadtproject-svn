package unitn.dadtln.samples;

/* javaME doesn't support this
import java.io.Serializable;
*/


import unitn.dadt.internals.Action;
import unitn.dadt.internals.ResultData;

public class DSensor_read_Action implements /*Serializable, */ Action  {
	//
	private static final long serialVersionUID = 6050373007371220085L;
	public DSensor_read_Action()  {

	}
	public Object evaluate(Object o) {
		Sensor local = (Sensor) o;
		{
			System.out.println("Sensor, type = " + local.typeToStr(local.type) + ", precision = " + local.precision);
			return new ResultData(local.read(), local.typeToStr(local.type));
		}
	}
	
}

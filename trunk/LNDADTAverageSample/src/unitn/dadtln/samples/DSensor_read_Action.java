package unitn.dadtln.samples;
import java.io.Serializable;

import DADT.ResultData;

public class DSensor_read_Action implements Serializable, DADT.Action  {
	//
	private static final long serialVersionUID = 6050373007371220085L;
	public DSensor_read_Action()  {

	}
	public Object evaluate(Object o) {
		Sensor local = (Sensor) o;
		{
			System.out.println("Sensor, type = " + local.typeToStr(local.type) + ", precision = " + local.precision);
			//return local.read();
			return new ResultData(local.read(), local.typeToStr(local.type));
		}
	}
	
}

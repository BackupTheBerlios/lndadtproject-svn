package unitn.dadtln.samples;

import polimi.ln.nodeAttributes.dynamic.BooleanProvider;

public class LNSensorIsActiveProvider implements BooleanProvider {
	
	private Sensor sensor;
	
	public LNSensorIsActiveProvider(Object s) {
        this.sensor = (Sensor)s;
    }
	
	public boolean getValue() {
		
		return this.sensor.isActive();
	}

}

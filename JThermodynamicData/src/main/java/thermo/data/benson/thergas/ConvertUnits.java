package thermo.data.benson.thergas;

import java.util.Vector;

import jThergas.data.JThermgasThermoStructureDataPoint;
import thermo.LineCommandsParameters;
import thermo.properties.ThermodynamicProperties;

public class ConvertUnits {
    /** Convert the thermodynamics to calories (if not already calories).
     * 
     * @param energyunits Energy units of the input thermodynamics
     * @param test true if input test
     * @return The conversion information (blank if not conversion occured).
     */
    public static String convertUnits(Vector<JThermgasThermoStructureDataPoint> data, String energyunits,boolean test) {
    	StringBuffer buf = new StringBuffer();
    	if(!energyunits.equals(LineCommandsParameters.calorie)) {
        for (int i = 0; i < data.size(); i++) {
            JThermgasThermoStructureDataPoint point = data.elementAt(i);
            if(energyunits.equals(LineCommandsParameters.joule)) {
            	buf.append(convertJoulesToCalories(point,test));
            }
        }
    	}
        return buf.toString();    	
    }
	/** Convert THERGAS point from joules to calories
	 * @param point The thermodynamic point 
	 * @param test  
	 * @return
	 */
	private static String convertJoulesToCalories(JThermgasThermoStructureDataPoint point,boolean test) {
		StringBuffer buf = new StringBuffer(); 
		double conversion = ThermodynamicProperties.jouleToCalorie();
		if(test) {
			buf.append(point.getStructure() + "\n");
			buf.append("original:  ");
			buf.append(point.getThermodynamics().writeToString() + "\n");
		}
		point.energyConversion(conversion);		
		if(test) {
			buf.append("converted: ");
			buf.append(point.getThermodynamics().writeToString() + "\n");
		}
		
		return buf.toString();
	}

}

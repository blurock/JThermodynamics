/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.properties;

import thermo.data.benson.SetOfThermodynamicTypes;
import thermo.data.benson.ThermoInfoType;

/**
 *
 * @author blurock
 */
public class ThermodynamicProperties extends SProperties {
	
	protected static double calorietojoule = 0.0;
	protected static double jouletocalorie = 0.0;

        protected static SetOfThermodynamicTypes types = new SetOfThermodynamicTypes();
        
        public static void addThermodynamicType(ThermoInfoType type) {
            types.addThermodynamicType(type);
        }
        public static ThermoInfoType getTherodyInfoType(String type) {
            return types.getType(type);
        }
        
        public static double calorieToJoule() {
        	if(calorietojoule == 0.0) {
        		String c2jS = getProperty("thermo.data.calorietojoule");
        		calorietojoule = Double.parseDouble(c2jS);
        		jouletocalorie = 1.0/calorietojoule;
        	}
        	return calorietojoule;
        }
        public static double jouleToCalorie() {
        	if(calorietojoule == 0.0) {
        		String c2jS = getProperty("thermo.data.calorietojoule");
        		calorietojoule = Double.parseDouble(c2jS);
        		jouletocalorie = 1.0/calorietojoule;
        	}
        	return jouletocalorie;
        }
        
        
}

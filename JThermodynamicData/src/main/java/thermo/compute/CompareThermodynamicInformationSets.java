/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import thermo.compare.ThermodynamicDifference;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.StandardThergasBensonThermoType;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class CompareThermodynamicInformationSets {
	private static double verySmall = 1e-10;
	private static double veryClose = 0.0001;

	public CompareThermodynamicInformationSets() {
		
	}

	public static SetOfThermodynamicDifferences computeDifference(SetOfThermodynamicInformation set1,
			SetOfThermodynamicInformation set2) throws ThermodynamicComputeException {
		SetOfThermodynamicDifferences difference = new SetOfThermodynamicDifferences("Difference");
		StandardThergasBensonThermoType standardtemps = new StandardThergasBensonThermoType();
		HashMap<String, ThermodynamicInformation> map1 = createHashTable(set1);
		HashMap<String, ThermodynamicInformation> map2 = createHashTable(set2);
		
		Set<String> keys1 = map1.keySet();
		Iterator<String> iter = keys1.iterator();
		double[] temperatures = standardtemps.getTemperaturesAsDoubleValues();
		while (iter.hasNext()) {
			String key = iter.next();
			ThermodynamicInformation thermo1 = map1.get(key);
			ThermodynamicInformation thermo2 = map2.get(key);
			if (thermo1 != null && thermo2 != null) {
				double enthalpy1 = thermo1.getStandardEnthalpy298();
				double enthalpy2 = thermo2.getStandardEnthalpy298();
				ThermodynamicDifference benson = new ThermodynamicDifference(veryClose);
				benson.initialize(thermo1.getName(), benson.getReference(), temperatures);
				benson.setName(thermo1.getName());
				double enthalpypercent = calculatePercent(enthalpy1, enthalpy2);
				double entropypercent = calculatePercent(thermo1.getStandardEntropy298(),
						thermo2.getStandardEntropy298());
				benson.setStandardEnthalpy(enthalpypercent);
				benson.setStandardEntropy(entropypercent);

				String name = null;

				for (int i = 0; i < temperatures.length; i++) {
					double t = temperatures[i];
					double cppercent = calculatePercent(thermo1.getHeatCapacity(t), thermo2.getHeatCapacity(t));
					benson.addHeatCapacity(name, cppercent, t);
				}
				difference.add(benson);
			} else {
				System.out.println("Can't compare: " + key);
			}
		}
		return difference;
	}

	private static HashMap<String, ThermodynamicInformation> createHashTable(SetOfThermodynamicInformation set) {
		HashMap<String, ThermodynamicInformation> map = new HashMap<String, ThermodynamicInformation>();
		Iterator<ThermodynamicInformation> iter = set.iterator();
		while (iter.hasNext()) {
			ThermodynamicInformation thermo = iter.next();
			map.put(thermo.getName(), thermo);
		}
		return map;
	}

	private static double calculatePercent(double x1, double x2) {
		double percent = 0.0;
		double sum = Math.abs(x1) + Math.abs(x2);

		if (sum > veryClose) {
			percent = (x2 - x1) / sum;
		}
		return percent;
	}

}

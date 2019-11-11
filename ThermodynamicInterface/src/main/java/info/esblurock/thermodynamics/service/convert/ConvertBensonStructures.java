package info.esblurock.thermodynamics.service.convert;

import info.esblurock.thermodynamics.base.benson.BensonThermodynamicBaseElements;
import info.esblurock.thermodynamics.base.benson.SetOfBensonThermodynamicBaseElements;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.HeatCapacityTemperaturePair;
import thermo.data.benson.SetOfBensonThermodynamicBase;

public class ConvertBensonStructures {
	public static SetOfBensonThermodynamicBaseElements convert(SetOfBensonThermodynamicBase bensonset) {
		SetOfBensonThermodynamicBaseElements elements = new SetOfBensonThermodynamicBaseElements();
		for(BensonThermodynamicBase base : bensonset) {
			BensonThermodynamicBaseElements element = new BensonThermodynamicBaseElements();
			element.setID(base.getID());
			element.setdHtoCalories(base.getdHtoCalories());
			element.setReference(base.getReference());
			element.setStandardEnthalpy(base.getStandardEnthalpy());
			element.setStandardEntropy(base.getStandardEntropy());
			element.setThermodynamicType(base.getThermodynamicType());
			for(HeatCapacityTemperaturePair pair : base.getSetOfHeatCapacities()) {
				info.esblurock.thermodynamics.base.benson.HeatCapacityTemperaturePair newpair =
						new info.esblurock.thermodynamics.base.benson.HeatCapacityTemperaturePair(
								pair.getStructureName(),
								pair.getTemperatureValue(),
								pair.getHeatCapacityValue());
				element.add(newpair);
			}
			elements.add(element);
		}
		return elements;
	}
}

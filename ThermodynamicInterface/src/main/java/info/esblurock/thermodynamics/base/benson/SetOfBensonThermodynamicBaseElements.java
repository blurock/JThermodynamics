package info.esblurock.thermodynamics.base.benson;

import java.io.Serializable;
import java.util.ArrayList;

public class SetOfBensonThermodynamicBaseElements extends ArrayList<BensonThermodynamicBaseElements> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	public SetOfBensonThermodynamicBaseElements() {
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		build.append("Set Of Benson Thermodynamic Elements\n");
		for(BensonThermodynamicBaseElements element : this) {
			build.append(element.toString());
		}
		return build.toString();
	}
}

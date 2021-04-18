package thermo.compute;

import java.util.Map;


import thermo.LineCommandsParameters;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

public class ComputeThermodynamics {

	public static boolean executeCommand(String[] args) {
		boolean foundCommand = false;
		Map<String, String> parameters = LineCommandsParameters.parameterSetFromArguments(args);
			String type = new String(args[0]);
			System.out.println(type + "   " + LineCommandsParameters.thermoKeyword);
			if (type.startsWith(LineCommandsParameters.thermoKeyword)) {
				foundCommand = true;
				String method = parameters.get(LineCommandsParameters.methodkey);
				String molform = parameters.get(LineCommandsParameters.methodkey);
				String moldescription = parameters.get(LineCommandsParameters.methodkey);
				String moleculename = parameters.get(LineCommandsParameters.methodkey);
				String outputform = parameters.get(LineCommandsParameters.methodkey);
				String outdetail = parameters.get(LineCommandsParameters.outdetailkey);
				System.out.println("method= " + method);
				System.out.println("molform= " + molform);
				System.out.println("moldescription= " + moldescription);
				System.out.println("moleculename= " + moleculename);
				System.out.println("outputform= " + outputform);
				System.out.println("outdetail= " + outdetail);
				
				
				SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
				ThermodynamicInformation thermo = executeCommand(method, molform, moldescription,
							moleculename, thermodynamics);
				try {
					String thermoS = ThermodynamicOutputFormation.printThermodynamics(thermodynamics, thermo, outputform, outdetail);
				} catch (ThermodynamicComputeException e) {
					e.printStackTrace();
				}
			}
			if (type.startsWith(LineCommandsParameters.thermosetKeyword)) {

			}

		return foundCommand;
	}

	public static void commands() {
		System.out.println(commandFormTHERMO());
	}

	public static String commandFormTHERMO() {
		StringBuffer buf = new StringBuffer();
		buf.append("Expecting: \n");
		buf.append("    THERMO Method MolForm MolDescription MoleculeName\n");
		buf.append("    Where:\n");
		buf.append("         Method: The method of computation: " + 
		LineCommandsParameters.listOfValidParameters(LineCommandsParameters.methodkey) + "\n");
		buf.append(
				"         MolForm: The form of MolDescription:" 
						+ LineCommandsParameters.listOfValidParameters(LineCommandsParameters.methodkey) + "\n");
		buf.append("         MolDescription: The line text molecular description\n");
		buf.append("         MoleculeName: The name of the molecule\n");
		return buf.toString();
	}

	public static ThermodynamicInformation executeCommand(String method, String molform, String moldescription,
			String moleculename, SetOfBensonThermodynamicBase thermodynamics) {
		ThermodynamicInformation fullthermo = null;

		return fullthermo;
	}

}

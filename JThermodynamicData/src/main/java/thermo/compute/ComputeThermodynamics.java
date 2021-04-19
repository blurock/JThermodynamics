package thermo.compute;

import java.util.Map;

import org.openscience.cdk.AtomContainer;

import thermo.LineCommandsParameters;
import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.benson.DB.ThermoSQLConnection;
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
			String molform = parameters.get(LineCommandsParameters.molformkey);
			String moldescription = parameters.get(LineCommandsParameters.moldescrkey);
			String moleculename = parameters.get(LineCommandsParameters.molnamekey);
			String outputform = parameters.get(LineCommandsParameters.outformatkey);
			String outdetail = parameters.get(LineCommandsParameters.outdetailkey);
			System.out.println("method= " + method);
			System.out.println("molform= " + molform);
			System.out.println("moldescription= " + moldescription);
			System.out.println("moleculename= " + moleculename);
			System.out.println("outputform= " + outputform);
			System.out.println("outdetail= " + outdetail);

			SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
			try {
				ThermodynamicInformation thermo = executeCommand(method, molform, moldescription, moleculename,
						thermodynamics);
						
				String thermoS = ThermodynamicOutputFormation.printThermodynamics(thermodynamics, thermo, outputform,
						outdetail);
				System.out.println(thermoS);
						
				System.out.println("method= " + method);
				System.out.println("molform= " + molform);
				System.out.println("moldescription= " + moldescription);
				System.out.println("moleculename= " + moleculename);
				System.out.println("outputform= " + outputform);
				System.out.println("outdetail= " + outdetail);
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
		buf.append("         Method: The method of computation: "
				+ LineCommandsParameters.listOfValidParameters(LineCommandsParameters.methodkey) + "\n");
		buf.append("         MolForm: The form of MolDescription:"
				+ LineCommandsParameters.listOfValidParameters(LineCommandsParameters.methodkey) + "\n");
		buf.append("         MolDescription: The line text molecular description\n");
		buf.append("         MoleculeName: The name of the molecule\n");
		return buf.toString();
	}

	public static ThermodynamicInformation executeCommand(String method, String molform, String moldescription,
			String moleculename, SetOfBensonThermodynamicBase thermodynamics) throws ThermodynamicComputeException {
		ThermodynamicInformation fullthermo = null;
		ThermoSQLConnection c = new ThermoSQLConnection();
		c.connect();
		ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
		StringToAtomContainer convertMoleculeString = new StringToAtomContainer(c);
		AtomContainer molecule = convertMoleculeString.stringToAtomContainer(molform, moldescription);
		fullthermo = compute.computeThermodynamics(molecule, thermodynamics, method);
		return fullthermo;
	}

}

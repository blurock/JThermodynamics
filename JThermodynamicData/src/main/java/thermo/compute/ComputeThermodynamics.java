package thermo.compute;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.openscience.cdk.AtomContainer;

import thermo.LineCommandsParameters;
import thermo.build.ReadThermodynamicsFromExGas;
import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.ThermodynamicInformation;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.exception.ThermodynamicComputeException;

public class ComputeThermodynamics {

	public static boolean executeCommand(String[] args) {
		boolean foundCommand = false;
		Map<String, String> parameters = LineCommandsParameters.parameterSetFromArguments(args);
		System.out.println(parameters);
		List<String> standardargs = LineCommandsParameters.requiredParameterInOrder(args);
		String type = new String(args[0]);
		System.out.println(type + "   " + LineCommandsParameters.thermoKeyword);
		if (type.equals(LineCommandsParameters.thermoKeyword)) {
			foundCommand = true;
			if (standardargs.size() >= 2) {
				parameters.put(LineCommandsParameters.moldescrkey, standardargs.get(1));
				parameters.put(LineCommandsParameters.molnamekey, standardargs.get(2));
				
				String moldescription = parameters.get(LineCommandsParameters.moldescrkey);
				String moleculename = parameters.get(LineCommandsParameters.molnamekey);
				String method = parameters.get(LineCommandsParameters.methodkey);
				String molform = parameters.get(LineCommandsParameters.molformkey);
				String outputform = parameters.get(LineCommandsParameters.outformatkey);
				String outdetail = parameters.get(LineCommandsParameters.outdetailkey);
				String outfile = parameters.get(LineCommandsParameters.outfilekey);
				System.out.println("method= " + method);
				System.out.println("molform= " + molform);
				System.out.println("moldescription= " + moldescription);
				System.out.println("moleculename= " + moleculename);
				System.out.println("outputform= " + outputform);
				System.out.println("outdetail= " + outdetail);
				System.out.println("outfile= " + outfile);

				SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
				try {
					ThermodynamicInformation thermo = executeCommand(method, molform, moldescription, moleculename,
							thermodynamics);
					String title = "Molecule: " + moldescription + " Thermodynamics: " + method;
					String thermoS = ThermodynamicOutputFormation.printThermodynamics(thermodynamics, thermo,
							outputform, outdetail, title, true);
					if (outfile != null) {
						ThermodynamicOutputFormation.printToFile(thermoS, outfile);
					} else {
						System.out.println(thermoS);
					}

					System.out.println("method= " + method);
					System.out.println("molform= " + molform);
					System.out.println("moldescription= " + moldescription);
					System.out.println("moleculename= " + moleculename);
					System.out.println("outputform= " + outputform);
					System.out.println("outdetail= " + outdetail);
					System.out.println("outfile= " + outfile);
				} catch (ThermodynamicComputeException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Expecting two arguments: MoleculeDescription MoleculeName");
			}
		} else if (type.equals(LineCommandsParameters.thermocompareKeyword)) {
			foundCommand = true;
			String comptype = null;
			if(standardargs.size() >= 3) {
				parameters.put(LineCommandsParameters.comparesrc, standardargs.get(1));
				parameters.put(LineCommandsParameters.compareref, standardargs.get(2));
			} else {
				System.out.println("Expecting 2 arguments: CompareType Reference\n");
				System.out.println("   CompType, Type2: The type of reference\n");
				System.out.println("   Reference: The description of the type");
			}
			String comparesrc = parameters.get(LineCommandsParameters.comparesrc);
			String compareref = parameters.get(LineCommandsParameters.compareref);
			String moldescription = parameters.get(LineCommandsParameters.moldescrkey);
			String method = parameters.get(LineCommandsParameters.methodkey);
			String molform = parameters.get(LineCommandsParameters.molformkey);
			String outputform = parameters.get(LineCommandsParameters.outformatkey);
			String outdetail = parameters.get(LineCommandsParameters.outdetailkey);
			String outfile = parameters.get(LineCommandsParameters.outfilekey);

			System.out.println("comptype= " + comptype);
			System.out.println("method= " + method);
			System.out.println("molform= " + molform);
			System.out.println("outputform= " + outputform);
			System.out.println("outdetail= " + outdetail);
			System.out.println("outfile= " + outfile);
			
			try {
				SetOfThermodynamicDifferences  diff = executeCompareCommand(comparesrc, compareref, moldescription, method, molform);
				String title = "Difference: " + comparesrc + " Thermodynamics: " + method;
				String thermoS;
					thermoS = ThermodynamicOutputFormation.printSetThermodynamics(diff,
							outputform, outdetail, title);
				if (outfile != null) {
					ThermodynamicOutputFormation.printToFile(thermoS, outfile);
				} else {
					System.out.println(thermoS);
				}
				
			} catch (ThermodynamicComputeException | IOException e) {
				e.printStackTrace();
			}
			
		} else if (type.equals(LineCommandsParameters.thermosetKeyword)) {
			foundCommand = true;
			if(standardargs.size() >= 2) {
				parameters.put(LineCommandsParameters.moldescrkey, standardargs.get(1));
			
				String moldescription = parameters.get(LineCommandsParameters.moldescrkey);
				String method = parameters.get(LineCommandsParameters.methodkey);
				String molform = parameters.get(LineCommandsParameters.molformkey);
				String outputform = parameters.get(LineCommandsParameters.outformatkey);
				String outdetail = parameters.get(LineCommandsParameters.outdetailkey);
				String outfile = parameters.get(LineCommandsParameters.outfilekey);
				System.out.println("method= " + moldescription);
				System.out.println("method= " + method);
				System.out.println("molform= " + molform);
				System.out.println("outputform= " + outputform);
				System.out.println("outdetail= " + outdetail);
				System.out.println("outfile= " + outfile);
				try {
					SetOfThermodynamicInformation thermodynamics = executeSetCommand(moldescription, method, molform);
				String title = "MoleculeSet: " + moldescription + " Thermodynamics: " + method;
				String thermoS;
					thermoS = ThermodynamicOutputFormation.printSetThermodynamics(thermodynamics,
							outputform, outdetail, title);
				if (outfile != null) {
					ThermodynamicOutputFormation.printToFile(thermoS, outfile);
				} else {
					System.out.println(thermoS);
				}
				} catch (ThermodynamicComputeException e) {
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				
			} else {
				System.out.println("Expecting an argument: MoleculeNamesFile\n");
			}
			
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
	
	public static SetOfThermodynamicInformation executeSetCommand(String moleculefile, String method, String molform) {
		SetOfThermodynamicInformation thermodynamics = null;
		File fileF = new File(moleculefile);
		List< List<String> > nameset = ComputeThermodynamicsFromSet.extractSetsFromFile(fileF, 2);
		thermodynamics = ComputeThermodynamicsFromSet.computeFromSet(nameset.get(0), nameset.get(1), method, molform, method);
		return thermodynamics;
	}

	public static SetOfThermodynamicDifferences executeCompareCommand(String comparesrc, String compareref, String moleculefile, String method, String molform) throws IOException, ThermodynamicComputeException {
	    SetOfThermodynamicInformation compareSet = null;
	    SetOfThermodynamicInformation computeSet = null;
	    if (comparesrc.equals(LineCommandsParameters.nasafile)) {
	    	ReadThermodynamicsFromExGas readTherm = new ReadThermodynamicsFromExGas();
	    	File thermoF = new File(compareref);
	    	compareSet = readTherm.read(thermoF);
	       	computeSet = executeSetCommand(moleculefile,method,molform);
	    } else if (comparesrc.equals(LineCommandsParameters.bensonfile)) {
	    	
	    } else if (comparesrc.equals(LineCommandsParameters.thergascomp)) {
	    	compareSet = executeSetCommand(compareref,LineCommandsParameters.thergascomp,molform);
	       	computeSet = executeSetCommand(compareref,method,molform);
	    } else if (comparesrc.equals(LineCommandsParameters.thermcomp)) {
	    	compareSet = executeSetCommand(compareref,LineCommandsParameters.thermcomp,molform);
	       	computeSet = executeSetCommand(compareref,method,molform);
	    } else if (comparesrc.equals(LineCommandsParameters.bensoncomp)) {
	    	compareSet = executeSetCommand(compareref,LineCommandsParameters.bensoncomp,molform);
	       	computeSet = executeSetCommand(compareref,method,molform);
	    }
    	SetOfThermodynamicDifferences  diff = CompareThermodynamicInformationSets.computeDifference(compareSet, computeSet);
		return diff;
	}
}

package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicException;

public class CorrectionElementTests {
	static ThermoSQLConnection connection;

	static String singleTest = "Single";
	static String fullTest = "Full";
	static String listelements = "List";
	static String elementS = "Element";
	static String opticalTestS = "OpticalSymmetryTest";

	/**
	 * @param args the command line arguments
	 */
	public static boolean executeCommand(String[] args) {
		boolean foundCommand = true;
		if (args.length < 1) {
			foundCommand = false;
		} else {
			String type = args[0];
			System.out.println(type);
			if (type.equalsIgnoreCase(opticalTestS)) {
				if (args.length < 3) {
					System.out.println("Expecting:  " + args[0] + " subtest molecule [other arguments]");
				} else {
					String specificTest = args[1];
					String nancyS = args[2];

					connection = new ThermoSQLConnection();
					connection.connect();

					StringBuffer buf = new StringBuffer();
					boolean success = true;
					NancyLinearFormToMolecule nancyFormToMolecule;
					IAtomContainer molecule = null;
					try {
						nancyFormToMolecule = new NancyLinearFormToMolecule(connection);
						molecule = nancyFormToMolecule.convert(nancyS);

						buf.append("Molecule  -----------------------------------------------\n");
						StructureAsCML cmlstruct = new StructureAsCML(molecule);
						buf.append(cmlstruct.toString() + "\n");
						buf.append("Molecule  -----------------------------------------------\n");
					} catch (SQLException e) {
						System.out.println("Invalid Nancy Linear Form: " + nancyS);
						success = false;
					} catch (CDKException e) {
						System.out.println("Invalid Nancy Linear Form: " + nancyS);
						success = false;
					}
					if (success) {
						ArrayList<String> arguments = new ArrayList<String>();
						for (int i = 3; i < args.length; i++) {
							arguments.add(args[i]);
						}
						if (type.equalsIgnoreCase(opticalTestS)) {
							testOpticalSymmetry(buf, specificTest, molecule, arguments);
						} else {
							foundCommand = false;
						}
						if (foundCommand) {
							System.out.println(buf.toString());
						}
					}
				}
			} else {
				System.out.println("Got here");
				foundCommand = false;
			}
		}
		return foundCommand;
	}

	public static void commands() {
		System.out.println("Expecting the type of Tests:");
		System.out.println(opticalTestS + ":  Test an optical symmetry element(s)");
	}

	private static void testOpticalSymmetry(StringBuffer buf, String specificTest, IAtomContainer molecule,
			ArrayList<String> arguments) {
		boolean success = true;
		SetOfBensonThermodynamicBase thermoset = new SetOfBensonThermodynamicBase();
		try {
			ThermoSQLConnection connection = new ThermoSQLConnection();
			connection.connect();

			if (specificTest.equalsIgnoreCase(singleTest)) {
				if (arguments.size() < 1) {
					opticalSymmetryCommands();
				} else {
					thermoset = TestOpticalSymmetryElement.performSingleTest(buf, molecule, connection,
							arguments.get(0));
				}
			} else if (specificTest.equalsIgnoreCase(fullTest)) {
				thermoset = TestOpticalSymmetryElement.performWithFullSet(buf, molecule, connection);
			} else if (specificTest.equalsIgnoreCase(fullTest)) {

			} else if (specificTest.equalsIgnoreCase(fullTest)) {

			} else {
				opticalSymmetryCommands();
			}
		} catch (SQLException e) {
			buf.append(e.toString());
			success = false;
		} catch (CDKException e) {
			buf.append(e.toString());
			success = false;
		} catch (ThermodynamicException e) {
			buf.append(e.toString());
			success = false;
		}
		if (success) {
			if (thermoset.size() > 0) {
				buf.append("======================================================\n");
				buf.append(thermoset.toString());
				buf.append("======================================================\n");
			} else {
				buf.append("No symmetry corrections computed\n");
			}
		}
	}

	private static void opticalSymmetryCommands() {
		System.out.println(opticalTestS + " SymmetryElement NancyString");
		System.out.println("Test type: " + singleTest + " or " + fullTest);
		System.out.println("NancyString: the molecule name as nancy string");
		System.out.println("Single test:");
		System.out.println("     SymmetryElement: The name of the optical symmetry element");

	}

}

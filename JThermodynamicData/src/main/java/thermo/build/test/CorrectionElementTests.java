package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicException;

public class CorrectionElementTests {
	static ThermoSQLConnection connection;
	static String type;
	static String specificTest = "";
	static String nancyS = "";
	static StringBuffer buf;
	static ArrayList<String> arguments;
	static boolean foundCommand;
	static IAtomContainer molecule;

	static String singleTest = "Single";
	static String fullTest = "Full";
	static String listelements = "List";
	static String elementS = "Element";
	static String opticalTestS = "OpticalSymmetryTest";
	static String bensonAtomTestS = "BensonAtomTest";
	static String linearAtomTestS = "LinearAtomTest";
	static String bensonRuleTestS = "BensonRuleTest";

	/**
	 * @param args the command line arguments
	 */
	public static boolean executeCommand(String[] args) {
		foundCommand = true;
		if (setup(args)) {
			if (type.equalsIgnoreCase(opticalTestS)) {
				testOpticalSymmetry();
			} else if (type.equalsIgnoreCase(bensonAtomTestS)) {
				metaAtomTest();
			} else if (type.equalsIgnoreCase(linearAtomTestS)) {
				linearAtomTest();
			} else if (type.equalsIgnoreCase(bensonRuleTestS)) {
				bensonRuleTest();
			}
			System.out.println(buf.toString());
		}
		return foundCommand;
	}

	public static void commands() {
		System.out.println("Expecting the type of Tests:");
		System.out.println(opticalTestS + ":  Test an optical symmetry element(s)");
		System.out.println(bensonAtomTestS + ": Test of the BensonAtom element(s)");
		System.out.println(linearAtomTestS + ": Test of the LinearAtom element(s)");
		System.out.println(bensonRuleTestS + ":Test of the Benson Group Additivity rule element(s)");
	}

	public static boolean setup(String[] args) {
		boolean success = true;
		if (args.length < 1) {
			success = false;
		} else {
			type = args[0];
			if (type.equalsIgnoreCase(opticalTestS) || type.equalsIgnoreCase(bensonAtomTestS)
					|| type.equalsIgnoreCase(linearAtomTestS)
					|| type.equalsIgnoreCase(bensonRuleTestS)) {
				if (args.length < 3) {
					System.out.println("Expecting:  " + args[0] + " Test NancyString [name]");
					System.out.println("          where Test:");
					System.out.println("                    Single: test a single element (by database name)");
					System.out.println("                    Full: test with the full set of elements");
					System.out.println("                 NancyString: The nancy linear form of the molecule to test");
					
					/*
					opticalSymmetryCommands();
					bensonAtomCommands();
					opticalSymmetryCommands();
					*/
					success = false;
				} else {
					specificTest = args[1];
					nancyS = args[2];
					buf = new StringBuffer();

					connection = new ThermoSQLConnection();
					connection.connect();

					IAtomContainer molecule = getMolecule();
					if (molecule != null) {
						arguments = new ArrayList<String>();
						for (int i = 3; i < args.length; i++) {
							arguments.add(args[i]);
						}
					} else {
						success = false;
					}
				}
			} else {
				foundCommand = false;
				success = false;
			}
		}
		return success;
	}

	public static IAtomContainer getMolecule() {
		NancyLinearFormToMolecule nancyFormToMolecule;
		molecule = null;
		try {
			nancyFormToMolecule = new NancyLinearFormToMolecule(connection);
			molecule = nancyFormToMolecule.convert(nancyS);
			buf.append("Molecule  -----------------------------------------------\n");
			StructureAsCML cmlstruct = new StructureAsCML(molecule);
			buf.append(cmlstruct.toString() + "\n");
			buf.append("Molecule  -----------------------------------------------\n");
		} catch (SQLException e) {
			System.out.println("Invalid Nancy Linear Form: " + nancyS);
		} catch (CDKException e) {
			System.out.println("Invalid Nancy Linear Form: " + nancyS);
		}
		return molecule;
	}

	private static void testOpticalSymmetry() {
		boolean success = true;
		SetOfBensonThermodynamicBase thermoset = new SetOfBensonThermodynamicBase();
		try {
			ThermoSQLConnection connection = new ThermoSQLConnection();
			connection.connect();

			if (specificTest.equalsIgnoreCase(singleTest)) {
				if (arguments.size() < 1) {
					System.out.println("Expecting optical symmetry name");
					opticalSymmetryCommands();
				} else {
					thermoset = TestOpticalSymmetryElement.performSingleTest(buf, molecule, connection,
							arguments.get(0));
				}
			} else if (specificTest.equalsIgnoreCase(fullTest)) {
				thermoset = TestOpticalSymmetryElement.performWithFullSet(buf, molecule, connection);
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

	private static void metaAtomTest() {
		try {
			boolean success = true;
			if (specificTest.equalsIgnoreCase(fullTest)) {
				TestBensonAtom.performWithFullSet(buf, molecule, connection);
			} else if (specificTest.equalsIgnoreCase(singleTest)) {

				if (arguments.size() < 1) {
					System.out.println("Expecting benson atom name");
					bensonAtomCommands();
					success = false;
				} else {
					String benson = arguments.get(0);
					TestBensonAtom.performSingleTest(buf, benson, molecule, connection);
				}
			}
			if (success) {
				buf.append("Molecule after substitution\n");
				StructureAsCML cml = new StructureAsCML(molecule);
				buf.append("Molecule  -----------------------------------------------\n");
				buf.append(cml.toString());
				buf.append("Molecule  -----------------------------------------------\n");
			}

		} catch (CDKException e) {
			buf.append(e.toString());
		}
	}

	private static void linearAtomTest() {
		try {
			boolean success = true;
			if (specificTest.equalsIgnoreCase(fullTest)) {
				molecule = TestLinearAtom.performWithFullSet(buf, molecule, connection);
			} else if (specificTest.equalsIgnoreCase(singleTest)) {

				if (arguments.size() < 1) {
					System.out.println("Expecting linear atom name");
					linearAtomCommands();
					success = false;
				} else {
					String benson = arguments.get(0);
					TestLinearAtom.performSingleTest(buf, benson, molecule, connection);
				}
			}
			if (success) {
				buf.append("Molecule after substitution\n");
				StructureAsCML cml = new StructureAsCML(molecule);
				buf.append("Molecule  -----------------------------------------------\n");
				buf.append(cml.toString());
				buf.append("Molecule  -----------------------------------------------\n");
			}

		} catch (CDKException e) {
			buf.append(e.toString());
		}

	}

	private static void bensonRuleTest() {
		SetOfBensonThermodynamicBase thermo = null;
		ArrayList<IAtom> atmlst = null;
		boolean success = true;
		if (specificTest.equalsIgnoreCase(fullTest)) {
			thermo = TestBensonGroupAdditivityRules.performWithFullSet(buf, molecule, connection);
		} else if (specificTest.equalsIgnoreCase(singleTest)) {

			if (arguments.size() < 1) {
				System.out.println("Expecting linear atom name");
				linearAtomCommands();
				success = false;
			} else {
				String benson = arguments.get(0);
				System.out.println(benson);
				atmlst = TestBensonGroupAdditivityRules.performSingleTest(buf, benson, molecule, connection);
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

	private static void bensonAtomCommands() {
		System.out.println(bensonAtomTestS + " Test NancyString [extra]");
		System.out.println("Test type: " + singleTest + " or " + fullTest);
		System.out.println("NancyString: the molecule name as nancy string");
		System.out.println("Single test:");
		System.out.println("     BensonAtom: The name of the Benson Atom");

	}

	private static void linearAtomCommands() {
		System.out.println(linearAtomTestS + " Test NancyString [extra]");
		System.out.println("Test type: " + singleTest + " or " + fullTest);
		System.out.println("NancyString: the molecule name as nancy string");
		System.out.println("Single test:");
		System.out.println("     linearAtom: The name of the Benson Atom");

	}

}

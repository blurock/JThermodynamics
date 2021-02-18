package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;

import thermo.data.benson.DB.ThermoSQLConnection;

public class ExamineCorrectionElements {
	static ThermoSQLConnection connection;
	static String typeS;
	static String specificTest = "";
	static StringBuffer buf;
	static ArrayList<String> arguments;
	static boolean foundCommand;

	static String listelementsS = "List";
	static String elementS = "Element";
	static String opticalTestS = "OpticalSymmetry";
	static String bensonAtomS = "BensonAtom";
	static String bensonRuleS = "BensonRule";
	static String linearAtomS = "LinearAtom";

	/**
	 * @param args the command line arguments
	 * @throws SQLException
	 */
	public static boolean executeCommand(String[] args) {
		boolean success = setup(args);
		if (success) {
			if (typeS.equalsIgnoreCase(opticalTestS)) {
				examineOpticalSymmetry();
			} else if (typeS.equalsIgnoreCase(bensonAtomS)) {
				examineBensonAtom();
			} else if (typeS.equalsIgnoreCase(linearAtomS)) {
				examineLinearAtom();
			} else if (typeS.equalsIgnoreCase(bensonRuleS)) {
				examineBensonRule();
			}
			System.out.println("------------------------------------");
			System.out.println(buf.toString());
		}
		return foundCommand;
	}
	public static void commands() {
		System.out.println(opticalTestS + "Examind optical symmetry elements");
		System.out.println(bensonAtomS+ ": Examine BensonAtom elements");
		System.out.println(linearAtomS + ": Examine Linear Atom elements");
		System.out.println(bensonRuleS + ": Example Benson Additivity Rule elements");
	}


	private static boolean setup(String[] args) {
		foundCommand = true;
		boolean success = true;
		if (args.length < 1) {
			foundCommand = false;
		} else {
			typeS = args[0];
			if (typeS.equalsIgnoreCase(opticalTestS) | 
					typeS.equalsIgnoreCase(bensonAtomS) |
					typeS.equalsIgnoreCase(linearAtomS) |
					typeS.equalsIgnoreCase(bensonRuleS)) {
				if (args.length < 2) {
					System.out.println("Expecting:  " + args[0] + " command [name]");
					System.out.println("          where command:");
					System.out.println("                    List: list the elements");
					System.out.println("                     Element: examine an element, given by name");
					success = false;
				} else {
					specificTest = args[1];

					connection = new ThermoSQLConnection();
					connection.connect();

					buf = new StringBuffer();

					arguments = new ArrayList<String>();
					for (int i = 2; i < args.length; i++) {
						arguments.add(args[i]);
					}
				}
			} else {
				foundCommand = false;
				success = false;
			}
		}
		return success;
	}

	private static void examineOpticalSymmetry() {
		try {
			if (specificTest.equalsIgnoreCase(listelementsS)) {
				TestOpticalSymmetryElement.listAllElements(buf, connection);
			} else if (specificTest.equalsIgnoreCase(elementS)) {
				if (arguments.size() > 0) {
					String symname = arguments.get(0);
					TestOpticalSymmetryElement.getSymmetryDefinition(buf, symname, connection);
				} else {
					buf.append("Expecting an optical symmetry element name\n");
				}
			}
		} catch (Exception e) {
			buf.append(e.toString());
		}
	}

	private static void examineBensonAtom() {
		try {
			if (specificTest.equalsIgnoreCase(listelementsS)) {
				TestBensonAtom.listAllElements(buf, connection);
			} else if (specificTest.equalsIgnoreCase(elementS)) {
				if (arguments.size() > 0) {
					String benson = arguments.get(0);
					TestBensonAtom.getBensonAtom(benson, buf, connection);
				} else {
					buf.append("Expecting an Benson Atom name\n");
				}
			}
		} catch (Exception e) {
			buf.append(e.toString());
		}
	}
	private static void examineLinearAtom() {
		try {
			if (specificTest.equalsIgnoreCase(listelementsS)) {
				TestLinearAtom.listAllElements(buf, connection);
			} else if (specificTest.equalsIgnoreCase(elementS)) {
				if (arguments.size() > 0) {
					String linear = arguments.get(0);
					TestLinearAtom.getLinearAtom(linear, buf, connection);
				} else {
					buf.append("Expecting an Linear Atom name\n");
				}
			}
		} catch (Exception e) {
			buf.append(e.toString());
		}
	}
	private static void examineBensonRule() {
		try {
			if (specificTest.equalsIgnoreCase(listelementsS)) {
				TestBensonGroupAdditivityRules.listAllElements(buf, connection);
			} else if (specificTest.equalsIgnoreCase(elementS)) {
				if (arguments.size() > 0) {
					String rule = arguments.get(0);
					TestBensonGroupAdditivityRules.getBensonAdditivtyRule(rule, buf, connection);
				} else {
					buf.append("Expecting an Benson Group Additivity Rule name\n");
				}
			}
		} catch (Exception e) {
			buf.append(e.toString());
		}
	}



}

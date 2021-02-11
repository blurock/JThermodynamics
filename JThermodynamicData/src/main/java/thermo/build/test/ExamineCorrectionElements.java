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

	/**
	 * @param args the command line arguments
	 * @throws SQLException
	 */
	public static boolean executeCommand(String[] args) {
		foundCommand = setup(args);
		if (foundCommand) {
			if (typeS.equalsIgnoreCase(opticalTestS)) {
				examineOpticalSymmetry();
			}
			if (typeS.equalsIgnoreCase(bensonAtomS)) {
				System.out.println("BensonAtom");
				examineBensonAtom();
			}
			System.out.println("------------------------------------");
			System.out.println(buf.toString());
		}
		return foundCommand;
	}

	private static boolean setup(String[] args) {
		foundCommand = true;
		boolean success = true;
		if (args.length < 1) {
			foundCommand = false;
		} else {
			typeS = args[0];
			if (typeS.equalsIgnoreCase(opticalTestS) | 
					typeS.equalsIgnoreCase(bensonAtomS)) {
				if (args.length < 2) {
					System.out.println("Expecting:  " + args[0] + " command [other arguments]");
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
				System.out.println(listelementsS);
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

}

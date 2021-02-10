package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;

import thermo.data.benson.DB.ThermoSQLConnection;

public class ExamineCorrectionElements {
	static ThermoSQLConnection connection;

	static String listelementsS = "List";
	static String elementS = "Element";
	static String opticalTestS = "OpticalSymmetry";

	/**
	 * @param args the command line arguments
	 * @throws SQLException
	 */
	public static boolean executeCommand(String[] args) {
		boolean foundCommand = true;
		try {
			if (args.length < 1) {
				foundCommand = false;
			} else {
				String typeS = args[0];
				if (typeS.equalsIgnoreCase(opticalTestS)) {

					if (args.length < 2) {
						System.out.println("Expecting:  " + args[0] + " command [other arguments]");
					} else {
						String commandS = args[1];

						connection = new ThermoSQLConnection();
						connection.connect();

						StringBuffer buf = new StringBuffer();
						boolean success = true;

						ArrayList<String> arguments = new ArrayList<String>();
						for (int i = 2; i < args.length; i++) {
							arguments.add(args[i]);
						}

						if (typeS.equalsIgnoreCase(opticalTestS)) {
							examineOpticalSymmetry(buf, commandS, arguments);
						} else {
							foundCommand = false;
						}
						if (foundCommand) {
							System.out.println(buf.toString());
						}

					}
				}
			}
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
		return foundCommand;
	}

	private static void examineOpticalSymmetry(StringBuffer buf, String commandS, ArrayList<String> arguments)
			throws SQLException {
		boolean success = true;
		if (commandS.equalsIgnoreCase(listelementsS)) {
			TestOpticalSymmetryElement.listAllElements(buf, connection);
		} else if (commandS.equalsIgnoreCase(elementS)) {
			if (arguments.size() > 0) {
				String symname = arguments.get(0);
				TestOpticalSymmetryElement.getSymmetryDefinition(buf, symname, connection);
			} else {
				success = false;
				buf.append("Expecting an optical symmetry element name\n");
			}
		}
	}
}

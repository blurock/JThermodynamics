/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compute;

import jThergas.data.read.ReadFileToString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.NASAPolynomialFromBenson;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;

/**
 * Main class for Thermodyanamic Calculations
 *
 * The arguments on the line are one of two forms: 1. MOLECULE linear name 2.
 * FILE root
 *
 * where: linear: The molecule in nancy linear form name: The name of the
 * molecule root: The root name of a file which has on each line: linear name
 *
 * @author edwardblurock
 */
public class ComputeThermodynamicsFromNancyString {
	static String fileType = "FILE";
	static String moleculeType = "MOLECULE";

	/**
	 * @param args the command line arguments
	 */
	public static boolean executeCommand(String[] args) {
		boolean foundCommand = true;
		
		if (args.length < 1) {
			foundCommand = false;
		} else {
			String type = new String(args[0]);
			if (type.startsWith(fileType)) {
				if(args.length < 2) {
					System.out.println("Expecting:");
					System.out.println("FILE Filename");
					System.out.println("    where Filename is the name of a file with molecules in nancy linear form");
					foundCommand = false;
				} else {
				try {
					String name = new String(args[1]);
					computeFromFile(name);
				} catch (FileNotFoundException ex) {
					Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
				}
				}
			}
			if (type.startsWith(moleculeType)) {
				try {
					if (args.length >= 3) {
						String name = new String(args[1]);
						String nancy = name;
						nancy = cleanup(nancy);
						name = new String(args[2]);
						computeFromNancyLinearForm(nancy, name);
					} else {
						System.out.println("Expecting:");
						System.out.println("MOLECULE Structure Molecule");
						System.out.println("    where:");
						System.out.println("        Structure: The molecule in Nancy linear form");
						System.out.println("        MoleculeName: The name of the molecule");
						foundCommand =false;
					}

				} catch (ThermodynamicComputeException ex) {
					Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
				}
			} else {
				System.out.println("Expecting type: " + fileType + " or " + moleculeType);
			}
		}
		return foundCommand;
	}
	
	public static void commands() {
		System.out.println("MOLECULE: Calculate the thermodynamics from Nancy Linear form");
		System.out.println("FILE: Calculate thermodynamics from a file of molecule names");
	}

	private static void computeFromFile(String fileroot) throws FileNotFoundException {
		ThermoSQLConnection c = new ThermoSQLConnection();
		c.connect();
		ComputeThermodynamicsFromMolecule compute = null;
		try {
			compute = new ComputeThermodynamicsFromMolecule(c);
		} catch (ThermodynamicComputeException ex) {
			Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
		}
		String fileS = new String(fileroot + ".dat");
		File fileF = new File(fileS);
		ReadFileToString readfile = new ReadFileToString();
		readfile.read(fileF);
		StringTokenizer tok = new StringTokenizer(readfile.outputString, "\n");
		String outputS = fileroot + ".thm";
		PrintWriter prt = new PrintWriter(new FileOutputStream(outputS));
		prt.println("THERM");
		prt.println("   300.000  1000.000  5000.000");
		while (tok.hasMoreTokens()) {
			String line = tok.nextToken();
			System.out.println(line);
			try {
				StringTokenizer linetok = new StringTokenizer(line);
				String nancy = linetok.nextToken();
				String name = linetok.nextToken();
				ThermodynamicInformation thermo = compute.computeThermodynamics(nancy);
				NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
				nasa.name = name;
				prt.print(nasa.toString());
			} catch (ThermodynamicComputeException ex) {
				Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		prt.println("END");
		prt.close();
	}

	private static void computeFromNancyLinearForm(String nancy, String name) throws ThermodynamicComputeException {
		ThermoSQLConnection c = new ThermoSQLConnection();
		c.connect();
		ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
		SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
		ThermodynamicInformation thermo = compute.computeThermodynamics(nancy, thermodynamics);
		System.out.println("Compute From Nancy Linear Form: -->" + nancy + "<--");
		System.out.println("-------------- Corrections --------------");
		System.out.println(thermodynamics.toString());
		System.out.println("-------------- Total --------------");
		System.out.println(thermo.toString());
		NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo,
				compute.getMolecule());
		nasa.name = name;
		System.out.println("-------------- NASA Polynomial --------------");
		System.out.println(nasa.toString());
	}

	public static String cleanup(String token) {
		if (token.startsWith("\"")) {
			token = token.substring(1, token.length());
		}
		if (token.endsWith("\"")) {
			token = token.substring(0, token.length()-1);
		}
		if (token.startsWith("'")) {
			token = token.substring(1, token.length());
		}
		if (token.endsWith("'")) {
			token = token.substring(0, token.length()-1);
		}
		return token;
	}
}

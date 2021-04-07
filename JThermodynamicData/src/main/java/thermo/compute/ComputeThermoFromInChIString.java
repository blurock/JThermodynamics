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
import thermo.data.benson.ThermodynamicInformation;
import thermo.exception.ThermodynamicComputeException;
import org.openscience.cdk.exception.CDKException;

/** Main class for Thermodynamic Calculations
 *
 * The arguments on the line are one of two forms:
 * 1. MOLECULE linear name
 * 2. FILE root
 *
 * where:
 * InChI: The molecule in InChI form
 * name: The name of the molecule
 * root: The root name of a file which has on each line: inchi name
 *
 * @author amritjalan
 */
public class ComputeThermoFromInChIString {
    static String fileType = "FILE";
    static String moleculeType = "InChI";

    /**
     * @param args the command line arguments
     */
    public static boolean executeCommand(String[] args) {
    	boolean foundCommand = true;
        if(args.length < 1) {
        	foundCommand = false;
        } else {
            String type = args[0];
            if(type.startsWith(fileType)) {
            	if(args.length < 2) {
					System.out.println("Expecting:");
					System.out.println("INCHIFILE Filename");
					System.out.println("    where Filename is the name of a file with molecules in InchI form");
					foundCommand = false;            		
            	} else {
                try {
                	String name = new String(args[1]);
                    computeFromFile(name,false);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CDKException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	}
            } if(type.startsWith(moleculeType)) {
                try {
                    if(args.length >= 3) {
                    	String name = new String(args[1]);
                        String inchi = name;
                        name = new String(args[2]);
                        computeFromInChI(inchi,name,false);
                        } else {
                            System.out.println("Expecting name of molecule as third argument");
                        }

                } catch (ThermodynamicComputeException ex) {
                    Logger.getLogger(ComputeThermodynamicsFromNancyString.class.getName()).log(Level.SEVERE, null, ex);
                } catch (CDKException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } else {
                foundCommand = false;
            }
        }
        return foundCommand;
    }
    
    public static void commands() {
		System.out.println("INCHI: Calculate the thermodynamics from InchI form");
		System.out.println("INCHIFILE: Calculate thermodynamics from a file of molecule names in InchI");    	
    }
    
    private static void computeFromFile(String fileroot, boolean frombensonradical) throws FileNotFoundException, CDKException {
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = null;
        try {
            compute = new ComputeThermodynamicsFromMolecule(c);
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(ComputeThermoFromInChIString.class.getName()).log(Level.SEVERE, null, ex);
        }
        String fileS = new String(fileroot + ".dat");
        File fileF = new File(fileS);
        ReadFileToString readfile = new ReadFileToString();
        readfile.read(fileF);
        StringTokenizer tok = new StringTokenizer(readfile.outputString,"\n");
        String outputS = fileroot + ".thm";
        PrintWriter  prt = new PrintWriter(new FileOutputStream(outputS));
        prt.println("THERM");
        prt.println("   300.000  1000.000  5000.000");
        while(tok.hasMoreTokens()) {
            String line = tok.nextToken();
            System.out.println(line);
            try {
                StringTokenizer linetok = new StringTokenizer(line);
                String inchi = linetok.nextToken();
                String name = linetok.nextToken();
                ThermodynamicInformation thermo = compute.computeThermodynamicsFromInChI(inchi,frombensonradical);
                NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
                nasa.name = name;
                prt.print(nasa.toString());
            } catch (ThermodynamicComputeException ex) {
                Logger.getLogger(ComputeThermoFromInChIString.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        prt.println("END");
         prt.close();
    }
    private static void computeFromInChI(String inchi,
    		String name,
    		boolean frombensonradical) throws ThermodynamicComputeException, CDKException {
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
        ComputeThermodynamicsFromMolecule compute = new ComputeThermodynamicsFromMolecule(c);
        ThermodynamicInformation thermo = compute.computeThermodynamicsFromInChI(inchi,frombensonradical);
        NASAPolynomialFromBenson nasa = new NASAPolynomialFromBenson((BensonThermodynamicBase) thermo);
        nasa.name = name;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import thermo.exception.ThermodynamicComputeException;

/**
 *
 * @author edwardblurock
 */
public class CompareThermodynamics {
	
    private static void oneLinePerMolecule(String[] args) throws ThermodynamicComputeException {
    	if(args.length < 4) {
    		System.out.println("Expecting: Compare Filename method molform");
    	} else {
    		String fileS = args[1];
    		String method = args[2];
    		String molform = args[3];
    		File file = new File(fileS);
    		CompareJThergasWithExGasThermo compare = new CompareJThergasWithExGasThermo(file);
    		compare.compare(molform,method);
    	}
    }

    private static void corrsAndThermo(String[] args) throws IOException, FileNotFoundException, ThermodynamicComputeException {
        String corrsS = args[1];
        String thermoS = args[2];
        String method = args[3];
        String molform = args[4];
        boolean frombensonradical = false;
        File corrsF = new File(corrsS);
        File thermoF = new File(thermoS);
        CompareWithCorrsAndExGas compare = new CompareWithCorrsAndExGas();
        compare.compare(corrsF, thermoF,method,molform);
    }
    static String oneLineS = "OneLine";
    static String twoFilesS = "CorrsAndThermo";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length >= 2) {
            try {

                String commandS = args[0];
                if (commandS.equalsIgnoreCase(oneLineS)) {
                    oneLinePerMolecule(args);
                } else if (commandS.equalsIgnoreCase(twoFilesS)) {
                    try {
                        corrsAndThermo(args);
                    } catch (IOException ex) {
                        Logger.getLogger(CompareThermodynamics.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (ThermodynamicComputeException ex) {
                Logger.getLogger(CompareThermodynamics.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Compare thermodynamics");
            System.out.println("OneLine File");
            System.out.println("      File:  The name of the file with the following format on each line:");
            System.out.println("                   Molecule in Nancy linear form");
            System.out.println("                   The name of the molecule");
            System.out.println("                   14 NASA polynomial coefficients");
            System.out.println("CorrsAndThermo CorrFile ThermoFile");
            System.out.println("      CorrFile: One each line a Nancy string and a molecule name");
            System.out.println("      ");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("");
        }
    }

}

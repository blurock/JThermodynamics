package thermo.compute;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openscience.cdk.AtomContainer;

import jThergas.data.read.ReadFileToString;
import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.SetOfThermodynamicInformation;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.exception.ThermodynamicComputeException;

/**
 * @author edwardblurock
 *
 */
public class ComputeThermodynamicsFromSet {
	
	
	
	/** Compute the thermodynamics for a set of molecules
	 * 
	 * @param names The set molecule descriptions (as specified by molform)
	 * @param method The method to use to calculate the thermodynamics
	 * @param molform The form of the molecule description
	 * @param nameOfList The name of the list
	 * @return The thermodynamic information for all the molecules
	 * 
	 * If there is an error in the calculation, then an empty BensonThermodynamicBase will be in the list for that molecule.
	 */
	public static SetOfThermodynamicInformation computeFromSet(List<String> descrs, List<String> molnames, 
			String method, String molform, String nameOfList) {
		SetOfThermodynamicInformation thermodynamicSet = new SetOfThermodynamicInformation(method);

		ThermoSQLConnection c = new ThermoSQLConnection();
		c.connect();
		ComputeThermodynamicsFromMolecule compute = null;
		StringToAtomContainer convertMoleculeString = null;
		try {
			convertMoleculeString = new StringToAtomContainer(c);
			compute = new ComputeThermodynamicsFromMolecule(c);
		} catch (ThermodynamicComputeException ex) {
			Logger.getLogger(ComputeThermodynamicsFromSet.class.getName()).log(Level.SEVERE, null, ex);
		}
		Iterator<String> iter = descrs.iterator();
		Iterator<String> nameiter = molnames.iterator();
			while(iter.hasNext()) {
				String molS = iter.next();
				String molname = nameiter.next();
				try {
					AtomContainer molecule = convertMoleculeString.stringToAtomContainer(molform, molS);
					SetOfBensonThermodynamicBase thermodynamics = new SetOfBensonThermodynamicBase();
					BensonThermodynamicBase thermo = (BensonThermodynamicBase) compute.computeThermodynamics(molecule, thermodynamics, method);
					thermo.setID(molS);
					thermo.setReference(molname);
					thermodynamicSet.add(thermo);
				} catch (ThermodynamicComputeException ex) {
					BensonThermodynamicBase base = new BensonThermodynamicBase();
					thermodynamicSet.add(base);
					Logger.getLogger(ComputeThermodynamicsFromSet.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		return thermodynamicSet;
	}
	
	
	/** From a file of names, this extracts the list of names
	 * The names can be multi-column. There is one list per column
	 * 
	 * 
	 * @param fileF Source file
	 * @param numlineset How many names expected in the list
	 * @return The lists of names, column-wise
	 * 
	 * If a line does not have enough columns, an empty string is added in the column as a place-holder.
	 * 
	 * 
	 */
	public static List< List<String> > extractSetsFromFile(File fileF, int numlineset) {
		List< List<String> > nameset = new ArrayList< List<String> >();
		for(int i=0;i<numlineset;i++) {
			List<String> subset = new ArrayList<String>();
			nameset.add(subset);
		}
		ReadFileToString readfile = new ReadFileToString();
		readfile.read(fileF);
		StringTokenizer tok = new StringTokenizer(readfile.outputString, "\n");
		while (tok.hasMoreTokens()) {
			String line = tok.nextToken();
			StringTokenizer linetok = new StringTokenizer(line);
			for(int i=0;i<numlineset;i++) {
				if(linetok.hasMoreElements()) {
					String name = linetok.nextToken();
					nameset.get(i).add(name);
				} else {
					nameset.get(i).add("");
				}
			}
		}
		return nameset;
	}

}

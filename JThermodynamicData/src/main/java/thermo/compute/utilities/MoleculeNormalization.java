package thermo.compute.utilities;

import java.io.IOException;
import java.sql.SQLException;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomType.Hybridization;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.exception.ThermodynamicComputeException;

/**
 * @author edwardblurock
 *
 */
public class MoleculeNormalization {
	
    public static SQLSubstituteBackMetaAtomIntoMolecule substitute = null;
    static String nancyLinearFormType = "NancyLinearForm";

	
	/** addHydrogens
	 * 
	 * This routine adds missing hydrogens.
	 * Its main purpose is to add hydrogens to AtomContainers from SMILES
	 * 
	 * @param molecule The molecule to add hydrogens to
	 * 
	 * @throws CDKException If an error occurs in the process
	 * 
	 */
	public static void addHydrogens(IAtomContainer molecule) throws CDKException {
	    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance((IChemObjectBuilder) molecule.getBuilder());

	    for (int i = 0; i < molecule.getAtomCount(); i++) {
	        IAtom atom = molecule.getAtom(i);
	        atom.setHybridization((Hybridization) CDKConstants.UNSET);
	        IAtomType type = matcher.findMatchingAtomType(molecule, molecule.getAtom(i));
	        AtomTypeManipulator.configure(molecule.getAtom(i), type);
	        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
	        adder.addImplicitHydrogens(molecule, molecule.getAtom(i));
	        int num = atom.getImplicitHydrogenCount();
	        for (int h = 0; h < num; h++) {
	            Atom hydrogen = new Atom("H");
	            molecule.addAtom(hydrogen);
	            Bond bnd = new Bond(atom, hydrogen);
	            molecule.addBond(bnd);
	        }
	        atom.setImplicitHydrogenCount(num);
	    }

	}
	
	public static void normalizeMolecule(AtomContainer molecule, ThermoSQLConnection c) throws ThermodynamicComputeException {
        try {
            //CDKHueckelAromaticityDetector aromatic = new CDKHueckelAromaticityDetector();
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
           // boolean sromaticB = CDKHueckelAromaticityDetector.detectAromaticity(molecule);
        	if(substitute == null) {
        		substitute = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, c);
        	}
			substitute.substitute(molecule);
		} catch (ClassNotFoundException | SQLException | CDKException | IOException e) {
			throw new ThermodynamicComputeException("Error in normailization (meta atoms)");
		}

	}

}

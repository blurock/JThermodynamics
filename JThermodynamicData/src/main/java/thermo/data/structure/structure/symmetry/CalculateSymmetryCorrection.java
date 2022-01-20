/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.MetaAtomDefinition;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.SubstituteBackMetaAtomsIntoMolecule;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.exception.ThermodynamicException;

/**
 *
 * @author edwardblurock
 */
public class CalculateSymmetryCorrection extends CalculateSymmetryCorrectionInterface {
    protected CalculateOpticalSymmetryCorrection optical;
    protected CalculateInternalSymmetryCorrection internal;
    protected CalculateExternalSymmetryCorrection external;
    protected SubstituteLinearStructures substitutions;
    protected SubstituteBackMetaAtomsIntoMolecule substituteBack;
	String nancyLinearFormType = "NancyLinearForm";

    public CalculateSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException {
        super(c);
            optical = new CalculateOpticalSymmetryCorrection(connect);
            external = new CalculateExternalSymmetryCorrection(connect);
            internal = new CalculateInternalSymmetryCorrection(connect,external);
			try {
				substitutions = new SubstituteLinearStructures(connect);
				substituteBack = new SQLSubstituteBackMetaAtomIntoMolecule(nancyLinearFormType, connect);
			} catch (ClassNotFoundException | SQLException | CDKException | IOException e) {
				throw new ThermodynamicException(e.toString());
			}
			
    }
    
    /**  Calculate symmetry contributions
     * 
     * @param setOfOpticalDefinitions Meta atom for optical definitions
     * @param setOfExternalDefinitions Meta atoms for external definitions
     * @param secondaryDefinitions Meta atoms for secondary atoms
     * @param setOfInternalDefinitions Meta atoms for internal definition
     * @param substitutions Linear meta atoms 
     * @param nancylinearform meta atoms for Nancy linear forms
     * @throws ThermodynamicException
     */
    public CalculateSymmetryCorrection(SetOfSymmetryDefinitions setOfOpticalDefinitions,
    		SetOfSymmetryDefinitions setOfExternalDefinitions,
    		SetOfSymmetryDefinitions secondaryDefinitions,
    		SetOfSymmetryDefinitions setOfInternalDefinitions,
    		SetOfMetaAtomsForSubstitution substitutions,
    		ArrayList<MetaAtomDefinition> nancylinearform) {
            optical = new CalculateOpticalSymmetryCorrection(setOfOpticalDefinitions);
            external = new CalculateExternalSymmetryCorrection(setOfExternalDefinitions,secondaryDefinitions);
            internal = new CalculateInternalSymmetryCorrection(setOfInternalDefinitions,external);
			this.substitutions = new SubstituteLinearStructures(substitutions);
			substituteBack = new SubstituteBackMetaAtomsIntoMolecule(nancylinearform);
    }

    public CalculateSymmetryCorrection() {
    	
    }
    public SymmetryDefinition getSymmetryDefinition() {
    	return null;
    }

    /*
     * Here the top calls of external, internal and optical symmetries are made.
     * 
     * It is important for later SymmetryMatch recognition that internal and external
     * symmetry recognition uses the same reference molecule. This is done because if the external 
     * symmetry matches the atom match of the internal symmetry, then one of the 
     * internal symmetry matches should be eliminated. Test case ch3/ch(ch3)/ch3 should have 
     * internal symmetry of 27 (not 81) and external symmetry of 3.
     * 
     * Optical symmetry can be dependent on external symmetry because if there is an assymmetric carbon,
     * with an external symmetry of greater than one, then that is not an optical symmetry.
     * It is not certain whether this case comes up
     * This is not programmed at the moment.
     * 
     * 
     */
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
		StructureAsCML cmlstruct;
		IAtomContainer newmolecule = null;
		try {
			cmlstruct = new StructureAsCML(mol);
			newmolecule = substitutions.substitute(cmlstruct);
			substituteBack.substitute(newmolecule);
		} catch (CDKException | IOException e) {
			throw new ThermodynamicException(e.toString());
		}
		
        boolean exfound = external.calculate(newmolecule, corrections);
        boolean infound = internal.calculate(newmolecule, corrections);
        boolean opfound = optical.calculate(mol, corrections);
        return exfound | infound | opfound;
    }
    
    
    /**
     * 
     * @param R The radical species
     * @param RH The radical species with the hydrogen added
     * @param corrections The corrections
     * @throws ThermodynamicException
     * 
     * This makes a call for both the radical and the 
     * 
     */
    void calculate(IAtomContainer R, IAtomContainer RH, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();
        SetOfBensonThermodynamicBase correctionsRH = new SetOfBensonThermodynamicBase();
        this.calculate(RH, correctionsRH);
        this.calculate(R, correctionsR);
    }

}

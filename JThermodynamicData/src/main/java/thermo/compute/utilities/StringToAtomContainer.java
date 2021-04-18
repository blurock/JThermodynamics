package thermo.compute.utilities;

import java.sql.SQLException;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomType.Hybridization;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.SmilesValencyChecker;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.SProperties;

public class StringToAtomContainer {
    InChIGeneratorFactory inchifactory;
    ThermoSQLConnection connect;
    NancyLinearFormToMolecule nancyFormToMolecule;


    public StringToAtomContainer(ThermoSQLConnection c) {
    	connect = c;
    }
    
    public AtomContainer stringToAtomContainer(String molform, String moldescription) throws ThermodynamicComputeException {
    	String cleaned = cleanup(moldescription);
    	AtomContainer molecule = null;
    	String molS = cleanup(moldescription);
    	if(molform.equals(SProperties.getProperty("thermo.parameter.nancy"))) {
    		molecule = nancylinearToAtomContainer(molS);
    	} else if(molform.equals(SProperties.getProperty("thermo.parameter.inchi"))) {
    		molecule = inchiToAtomContainer(molS);
    	} else if(molform.equals(SProperties.getProperty("thermo.parameter.smiles"))) {
    		molecule = smilesToAtomContainer(molS);
    	} else {
    		throw new ThermodynamicComputeException("MolDescription type unknown: " + molform);
    	}
    	return molecule;
    }
    
    public AtomContainer inchiToAtomContainer(String inchi) throws ThermodynamicComputeException {
    	AtomContainer molecule = null;
        if(inchifactory == null) {
            try {
				inchifactory = InChIGeneratorFactory.getInstance();
			} catch (CDKException e) {
				throw new ThermodynamicComputeException("Cannot convert to INCHI (factory error): '" + inchi + "'");
			}
        }
        InChIToStructure istruct;
		try {
			istruct = inchifactory.getInChIToStructure(inchi, DefaultChemObjectBuilder.getInstance());
	        molecule = new AtomContainer(istruct.getAtomContainer());
		} catch (CDKException e) {
			throw new ThermodynamicComputeException("Cannot convert from INCHI: '" + inchi + "'");
		}
        return molecule;
    }
    
    public AtomContainer nancylinearToAtomContainer(String nancy) throws ThermodynamicComputeException {
       	AtomContainer molecule = null;   	
        try {
			nancyFormToMolecule = new NancyLinearFormToMolecule(connect);
		} catch (SQLException e1) {
			throw new ThermodynamicComputeException("Cannot convert from Nancy Linear Form: '" + nancy + "'");
		}
        try {
			molecule = (AtomContainer) nancyFormToMolecule.convert(nancy);
		} catch (SQLException e) {
			throw new ThermodynamicComputeException("Cannot convert from Nancy Linear Form: '" + nancy + "'");
		}
        return molecule;
    }

    public AtomContainer smilesToAtomContainer(String smiles) throws ThermodynamicComputeException {
        IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        
        AtomContainer molecule = null;
    try {
        SmilesParser parser = new SmilesParser(builder);            
        molecule = (AtomContainer) parser.parseSmiles(smiles);
        MoleculeNormalization.addHydrogens(molecule);
    } catch (InvalidSmilesException ex) {
        molecule = null;
    } catch (CDKException ex) {
        molecule = null;
    }
    if(molecule == null) {
		throw new ThermodynamicComputeException("Cannot convert from SMILES: '" + smiles + "'");
    }
   return molecule;
}
	public static String cleanup(String token) {
		if (token.startsWith("\"") && token.endsWith("\"")) {
			token = token.substring(1, token.length());
			token = token.substring(0, token.length()-1);
		}
		if (token.startsWith("'") && token.endsWith("'")) {
			token = token.substring(1, token.length());
			token = token.substring(0, token.length()-1);
		}
		return token;
	}


}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.structure.symmetry;

import java.sql.SQLException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.libio.cml.Convertor;
import org.openscience.cdk.tools.ILoggingTool;
import org.openscience.cdk.tools.LoggingToolFactory;
import org.openscience.cdk.exception.CDKException;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.symmetry.DB.SQLSetOfSymmetryDefinitions;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

/**
 *
 * @author edwardblurock
 */
public class CalculateOpticalSymmetryCorrection extends CalculateSymmetryCorrectionInterface  {
	
    private static ILoggingTool logger = LoggingToolFactory.createLoggingTool(CalculateOpticalSymmetryCorrection.class);


String opticalS = "OpticalIsomer";
String referenceS = "Optical Symmetry Correction";
SetOfSymmetryDefinitions setOfDefinitions;
DetermineSymmetryFromSingleDefinition fromSingleDefinition;
DetermineTotalOpticalSymmetry determineTotal;
double gasConstant;

    public CalculateOpticalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException  {
       super(c);
        try {
            setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, opticalS);
            initialize();
         } catch (SQLException ex) {
            throw new ThermodynamicException(ex.toString());
        }
    }
    
    public CalculateOpticalSymmetryCorrection(SetOfSymmetryDefinitions setOfOpticalDefinitions)  {
             this.setOfDefinitions = setOfOpticalDefinitions;
             initialize();
     }
    public CalculateOpticalSymmetryCorrection() {
    	
    }
    
    public SymmetryDefinition getSymmetryDefinition() {
    	return fromSingleDefinition.getSymmetryDefinition();
    }

    
    public void setStructureOpticalIsomer(SetOfSymmetryDefinitions set) {
    	this.setOfDefinitions = set;
    }
    public void initialize() {
        fromSingleDefinition = new DetermineSymmetryFromSingleDefinition();
        determineTotal = new DetermineTotalOpticalSymmetry(fromSingleDefinition, setOfDefinitions);
        String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
        gasConstant = Double.valueOf(gasconstantS).doubleValue();    	
    }
    
    
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
    	boolean found = false;
    	try {
    		StructureAsCML cml = new StructureAsCML(mol);
            double opticalsymmetry = calculateOpticalSymmetry(mol, corrections);
            if (opticalsymmetry > 0.0) {
            	found = true;
                Double optD = Double.valueOf(opticalsymmetry);
                String optS = referenceS + " (" + optD.toString() + ")";
                double correction = calculateCorrection(opticalsymmetry);
                BensonThermodynamicBase benson = new BensonThermodynamicBase(opticalS, null, 0.0, correction);
                benson.setID(optS);
                benson.setReference("Symmetry");
                corrections.add(benson);
            }
        } catch (CDKException ex) {
            throw new ThermodynamicException(ex.toString());
        }
    	return found;
    }
    
    public double calculateCorrection(double opticalsymmetry) {
    	return gasConstant * Math.log(opticalsymmetry);
    }
    
        public double calculateOpticalSymmetry(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws CDKException {
            return  (double) determineTotal.determineSymmetry(mol,corrections);
    }

}

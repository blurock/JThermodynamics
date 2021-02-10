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
SQLSetOfSymmetryDefinitions setOfDefinitions;
DetermineSymmetryFromSingleDefinition fromSingleDefinition;
DetermineTotalOpticalSymmetry determineTotal;
double gasConstant;

    public CalculateOpticalSymmetryCorrection(ThermoSQLConnection c) throws ThermodynamicException  {
       super(c);
        try {
            setOfDefinitions = new SQLSetOfSymmetryDefinitions(connect, opticalS);
            fromSingleDefinition = new DetermineSymmetryFromSingleDefinition();
            determineTotal = new DetermineTotalOpticalSymmetry(fromSingleDefinition, setOfDefinitions);
            String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
            gasConstant = Double.valueOf(gasconstantS).doubleValue();
        } catch (SQLException ex) {
            throw new ThermodynamicException(ex.toString());
        }
    }
    public boolean calculate(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws ThermodynamicException {
    	boolean found = false;
    	try {
    		StructureAsCML cml = new StructureAsCML(mol);
            double opticalsymmetry = calculateOpticalSymmetry(mol, corrections);
            if (opticalsymmetry > 0.0) {
            	found = true;
                Double optD = new Double(opticalsymmetry);
                String optS = referenceS + " (" + optD.toString() + ")";
                double correction = gasConstant * Math.log(opticalsymmetry);
                BensonThermodynamicBase benson = new BensonThermodynamicBase(opticalS, null, 0.0, correction);
                benson.setReference(optS);
                corrections.add(benson);
            }
        } catch (CDKException ex) {
            throw new ThermodynamicException(ex.toString());
        }
    	return found;
    }
        public double calculateOpticalSymmetry(IAtomContainer mol, SetOfBensonThermodynamicBase corrections) throws CDKException {
            return  (double) determineTotal.determineSymmetry(mol,corrections);
    }

}

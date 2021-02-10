package thermo.build.test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.symmetry.CalculateOpticalSymmetryCorrection;
import thermo.data.structure.structure.symmetry.DetermineSymmetryFromSingleDefinition;
import thermo.data.structure.structure.symmetry.SymmetryDefinition;
import thermo.data.structure.structure.symmetry.DB.SQLSymmetryDefinition;
import thermo.exception.ThermodynamicException;
import thermo.properties.SProperties;

public class TestOpticalSymmetryElement {
	static String opticalS = "OpticalIsomer";
	
	public static SetOfBensonThermodynamicBase performSingleTest(StringBuffer buf, 
			IAtomContainer molecule,
			ThermoSQLConnection connection,
			String symname) throws SQLException, CDKException {
        SQLSymmetryDefinition sqldef = new SQLSymmetryDefinition(connection);
        HashSet<SymmetryDefinition> set = sqldef.retrieveStructuresFromDatabase(symname);
        Iterator<SymmetryDefinition> iter = set.iterator();
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();
        if(iter.hasNext()) {
        	SymmetryDefinition def = iter.next();
			buf.append("======================================================\n");
        	buf.append("Symmetry Element: " + symname + "\n");
        	buf.append(def.toString());
			buf.append("======================================================\n");
        	DetermineSymmetryFromSingleDefinition fromSingleDefinition = new DetermineSymmetryFromSingleDefinition();
        	int symmetry = fromSingleDefinition.determineSymmetry(def, molecule);
        	double symD = fromSingleDefinition.computeSymmetryContribution(symmetry);
        	if(symD > 1.0) {
        		buf.append("The Symmetry of " +  symname + ": " + symD + "\n");
        		String opticalS = symname + "(" + symD + ")";
        		String gasconstantS = SProperties.getProperty("thermo.data.gasconstant.clasmolsk");
        		double gasConstant = Double.valueOf(gasconstantS).doubleValue();
        		double correction = gasConstant * Math.log(symD);
                BensonThermodynamicBase benson = new BensonThermodynamicBase(opticalS, null, 0.0, correction);
                benson.setReference(opticalS);
                correctionsR.add(benson);
        	} else {
        		buf.append("Symmetry element not found in molecule: " + symname + "\n");
        	}
        } else {
        	buf.append("Symmetry Element not found in database: " + symname);
        }

        return correctionsR;

	}
	public static SetOfBensonThermodynamicBase performWithFullSet(StringBuffer buf, 
			IAtomContainer molecule,
			ThermoSQLConnection connection) throws ThermodynamicException {
        CalculateOpticalSymmetryCorrection optical = new CalculateOpticalSymmetryCorrection(connection);
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();

        optical.calculate(molecule,correctionsR);
        
        buf.append(correctionsR.toString());
		return correctionsR;
	}

	public static ArrayList<String> listAllElements(StringBuffer buf, ThermoSQLConnection connection) throws SQLException {
        SQLSymmetryDefinition sqldef = new SQLSymmetryDefinition(connection);
        String[] names = sqldef.retrieveSymmetryNamesOfTypeFromDatabase(opticalS);
        ArrayList<String> answer = new ArrayList<String>();
        buf.append("Optical Symmetry Elements\n");
        for(int i=0;i<names.length;i++) {
        	answer.add(names[i]);
        	buf.append(names[i] + "\n");
        }
		return answer;
	}
	
	public static SymmetryDefinition getSymmetryDefinition(StringBuffer buf, String symname, ThermoSQLConnection connection) throws SQLException {
		SymmetryDefinition symdef = null;
        SQLSymmetryDefinition sqldef = new SQLSymmetryDefinition(connection);
        HashSet<SymmetryDefinition> set = sqldef.retrieveStructuresFromDatabase(symname);
        Iterator<SymmetryDefinition> iter = set.iterator();
        if(iter.hasNext()) {
        	symdef = iter.next();
        	buf.append("Optical Symmetry Element: " + symname + "\n");
        	buf.append(symdef.toString());
        }
        return symdef;
	}
}

package symmetry;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.SetOfBensonThermodynamicBase;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.symmetry.CalculateOpticalSymmetryCorrection;
import thermo.exception.ThermodynamicException;

public class TestDatabaseOpticalSymmetry {

	@Test
	public void test() {
		try {
        ThermoSQLConnection c = new ThermoSQLConnection();
        c.connect();
		String nancy = "ch(ch3)(#1)/ch(ch3)/ch2/ch(ch3)/ch(.)/ch2/1";
		NancyLinearFormToMolecule nancyFormToMolecule = new NancyLinearFormToMolecule(c);
		
		IAtomContainer molecule = nancyFormToMolecule.convert(nancy);
        System.out.println("Molecule  -----------------------------------------------");
        System.out.println(molecule.toString());
        System.out.println("Molecule  -----------------------------------------------");

        CalculateOpticalSymmetryCorrection optical = new CalculateOpticalSymmetryCorrection(c);
        SetOfBensonThermodynamicBase correctionsR = new SetOfBensonThermodynamicBase();

        optical.calculate(molecule,correctionsR);
		} catch(SQLException ex) {
			
		} catch(ThermodynamicException ex) {
			
		}
        
	}

}

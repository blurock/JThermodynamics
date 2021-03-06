/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compute;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import thermo.compute.ComputeAdiabaticFlameTemperature;
import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicComputeException;
import thermo.properties.SProperties;
import thermo.test.GenerateStructures;

/**
 *
 * @author edwardblurock
 */
public class TestComputeAdiabaticFlameTemperature {

    public TestComputeAdiabaticFlameTemperature() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void computeForPropane() {
        try {
            StructureAsCML propanecml = GenerateStructures.createPropane();
            ThermoSQLConnection connection = new ThermoSQLConnection();
            if (connection.connect()) {
                ComputeAdiabaticFlameTemperature flametemp = new ComputeAdiabaticFlameTemperature(connection);
                double beginT = 300;
                String nancy = "ch3/ch2/ch3";
        		StringToAtomContainer convertMoleculeString = new StringToAtomContainer(connection);
        		AtomContainer molecule = convertMoleculeString.stringToAtomContainer(SProperties.getProperty("thermo.parameter.nancy"), nancy);
                
                double flameTemperature = flametemp.computeFlameTemperatureOxygen(molecule, beginT,SProperties.getProperty("thermo.parameter.thergaskey"));

                System.out.println("Adiabatic Flame Temperature at " + beginT + " for propane is " + flameTemperature);
            }
        } catch (ThermodynamicComputeException ex) {
            Logger.getLogger(TestComputeAdiabaticFlameTemperature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestComputeAdiabaticFlameTemperature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestComputeAdiabaticFlameTemperature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestComputeAdiabaticFlameTemperature.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestComputeAdiabaticFlameTemperature.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

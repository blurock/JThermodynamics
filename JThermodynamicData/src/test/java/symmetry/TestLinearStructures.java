/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package symmetry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToGeneralStructure;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.matching.SubstituteLinearStructures;

/**
 *
 * @author edwardblurock
 */
public class TestLinearStructures {

    public TestLinearStructures() {
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
    public void linearStructuresInDatabase() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            //SQLMetaAtomInfo sqlmeta = new SQLMetaAtomInfo(connect);
            NancyLinearFormToMolecule linear = new NancyLinearFormToMolecule(connect);

            IAtomContainer mol = linear.convert("'*'C///CR");
            StructureAsCML cmlstruct = new StructureAsCML(mol);
            System.out.println(cmlstruct.getCmlStructureString());

            NancyLinearFormToGeneralStructure nancy = new NancyLinearFormToGeneralStructure(connect);
            IAtomContainer converted = nancy.convert("'*'C///CR");
            StructureAsCML cmlconvert = new StructureAsCML(converted);
            System.out.println(cmlconvert.getCmlStructureString());

            
        } catch (CDKException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @Test
    public void substituteInLinearAtoms() {
        try {
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();

            SubstituteLinearStructures subs = new SubstituteLinearStructures(connect);
            System.out.println(subs.toString());
            NancyLinearFormToMolecule nancy = new NancyLinearFormToMolecule(connect);
            IAtomContainer mol = nancy.convert("ch2(c///ch)/c///c/c///ch");
            StructureAsCML cmlstruct = new StructureAsCML(mol);
            System.out.println(cmlstruct.getCmlStructureString());
            System.out.println("===========================================");
            IAtomContainer newmolecule = subs.substitute(cmlstruct);
            StructureAsCML newcmlstruct = new StructureAsCML(newmolecule);
            System.out.println(newcmlstruct.getCmlStructureString());


        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestLinearStructures.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
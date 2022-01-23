/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package molecules;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.structure.structure.AtomCounts;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.exception.ThermodynamicComputeException;
import thermo.test.GenerateStructures;

/**
 *
 * @author edwardblurock
 */
public class TestAtomCountsForMolecule {

    public TestAtomCountsForMolecule() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void simpleTest() {
        try {
        	String molform = "NANCY";
        	String moldescription = "ch(ch3)(.)/ch3";
        	HashSet<MetaAtomInfo> metaatoms = new HashSet<MetaAtomInfo>();
    		StringToAtomContainer convertMoleculeString = new StringToAtomContainer(metaatoms);
            IAtomContainer atoms = convertMoleculeString.stringToAtomContainer(molform, moldescription);
            AtomCounts counts = new AtomCounts(atoms);
            counts.generateHashCode(atoms);
            System.out.println("Methyl Test: " + counts.isomerName());
            String[] atomnames = counts.getAtomStringArray(4);
            int[] atomcounts = counts.correspondingAtomCount(4);
            for(int i=0;i<atomnames.length;i++) {
                System.out.println("Atom: " + atomnames[i] + "\t Count:" + atomcounts[i]);
            }

        } catch (ThermodynamicComputeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
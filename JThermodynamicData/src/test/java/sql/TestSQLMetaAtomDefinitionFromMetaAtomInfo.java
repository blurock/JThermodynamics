/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import thermo.data.benson.BensonGroupStructure;
import thermo.data.benson.BensonGroupStructuresFromMolecule;
import thermo.data.benson.BensonThermodynamicBase;
import thermo.data.benson.CML.CMLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLBensonThermodynamicBase;
import thermo.data.benson.DB.SQLGroupElement;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.benson.SetOfBensonGroupStructures;
import thermo.data.structure.structure.DB.SQLMetaAtomDefinitionFromMetaAtomInfo;
import thermo.data.structure.structure.SetOfMetaAtomsForSubstitution;
import thermo.data.structure.structure.StructureAsCML;
import thermo.test.GenerateStructures;

/**
 *
 * @author blurock
 */
public class TestSQLMetaAtomDefinitionFromMetaAtomInfo {

    public TestSQLMetaAtomDefinitionFromMetaAtomInfo() {
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
    public void testSQLMetaAtomDefinitionFromMetaAtomInfo() {
        String typeS = "BensonAtom";
        ThermoSQLConnection connect = new ThermoSQLConnection();
        if (connect.connect()) {
            try {
                SQLMetaAtomDefinitionFromMetaAtomInfo sqldefs = new SQLMetaAtomDefinitionFromMetaAtomInfo(connect);

                SetOfMetaAtomsForSubstitution set = sqldefs.createSubstitutionSets(typeS);
                System.out.println(set.toString());

                StructureAsCML struct = GenerateStructures.createCH3CHO();
                IAtomContainer mol = set.substitute(struct);
                StructureAsCML substruct = new StructureAsCML(mol);

                BensonGroupStructuresFromMolecule generate = new BensonGroupStructuresFromMolecule();
                SetOfBensonGroupStructures structures = generate.deriveBensonGroupStructures(substruct.getMolecule());
                System.out.println(structures.toString());

                SQLGroupElement sqlelement = new SQLGroupElement(connect);
                SQLBensonThermodynamicBase sqlthermo = new SQLBensonThermodynamicBase(connect);
                Iterator<BensonGroupStructure> i = structures.iterator();
                while (i.hasNext()) {
                    BensonGroupStructure grp = i.next();
                    grp.setStructureName(null);
                    String[] names = sqlelement.queryNameSet(grp);

                    for (int n = 0; n < names.length; n++) {
                        String name = names[n];
                        System.out.println("Thermodynamics for " + name);

                        HashSet vec = sqlthermo.retrieveStructuresFromDatabase(name);
                        Iterator<BensonThermodynamicBase> iter = vec.iterator();
                        
                        BensonThermodynamicBase thermo = iter.next();
                        CMLBensonThermodynamicBase cmlthermo = new CMLBensonThermodynamicBase();
                        cmlthermo.setStructure(thermo);
                        String cmlthermoS = cmlthermo.restore();
                        System.out.println(cmlthermoS);
                    }
                }
            } catch (CDKException ex) {
                Logger.getLogger(TestSQLMetaAtomDefinitionFromMetaAtomInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestSQLMetaAtomDefinitionFromMetaAtomInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TestSQLMetaAtomDefinitionFromMetaAtomInfo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(TestSQLMetaAtomDefinitionFromMetaAtomInfo.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void printListOfBensonStructures(String title, List<BensonGroupStructure> structures) {
        System.out.println("Benson Structures: " + title);
        Iterator<BensonGroupStructure> i = structures.iterator();
        while (i.hasNext()) {
            BensonGroupStructure struct = i.next();
            System.out.println(struct.toString());
        }

    }
}
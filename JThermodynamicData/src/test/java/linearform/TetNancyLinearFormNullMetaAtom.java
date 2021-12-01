package linearform;

import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.ISingleElectron;

import thermo.compute.utilities.StringToAtomContainer;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.linearform.NancyLinearFormToMolecule;
import thermo.data.structure.structure.MetaAtomInfo;
import thermo.data.structure.structure.StructureAsCML;
import thermo.data.structure.structure.DB.SQLSubstituteBackMetaAtomIntoMolecule;
import thermo.exception.ThermodynamicComputeException;

public class TetNancyLinearFormNullMetaAtom {

    NancyLinearFormToMolecule nancy = null;

    public TetNancyLinearFormNullMetaAtom() throws SQLException {
        ThermoSQLConnection connect = new ThermoSQLConnection();
        if (connect.connect()) {
            nancy = new NancyLinearFormToMolecule(new HashSet<MetaAtomInfo>());
        } else {
            throw new SQLException("Failure to connect to Thermodynamic Database");
        }
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

    private void convertToMolecule(String molS) throws CDKException, SQLException {
		HashSet<MetaAtomInfo> metaatoms = new HashSet<MetaAtomInfo>();
		StringToAtomContainer convert = new StringToAtomContainer(metaatoms);
		String form = "NANCY";
		AtomContainer molecule;
		try {
			molecule = convert.stringToAtomContainer(form, molS);
                StructureAsCML cmlstruct = new StructureAsCML(molecule);
                System.out.println("Nancy Linear Form: " + molS);
                System.out.println(cmlstruct.getCmlStructureString());
                System.out.println("-------------------------------------------------------");
		} catch (ThermodynamicComputeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Test
    public void branchedAlkaneTest() {
        try {
            String molS = "ch2//c(ch3)/ch3";
            //String molS = "h";
            //String molS = "ch4";
            //String molS = "ch3(.)";
            //String molS = ".ch3";
            //String molS = "ch3/ch3";
            //String molS = "ch3/ch2/ch3";
            //String molS = ".c(//o)/h";
            //String molS = ".c(//o)h";
            //String molS = "c(.)h2/c(/ch3)2/ch2/ch(/ch3)2";
            //String molS=" .ch(/ch3)/ch2/o/oh";
            //String molS = "c(.)h2/c(/ch3)2/ch2/ch(/ch3)2";
            //String molS = "c(#1)h2/ch2/ch2/ch2/o/1";
            //String molS = ".o/o/ch(/o/oh)/ch2/ch2/ch3";
            //String molS = "c(//o)2";
            //String molS = "c(//o)(ch3)";
            //String molS = ".c(//ch2)/h";
            //String molS = "ch2(ch2(.))/ch2/ch2/ch//ch2";
            //String molS = "ch2(#1)/ch2/ch2/1";
            //String molS = "c(#1)&c(#2)&ch&ch&ch&ch&1,1&c(ch2(.))&c(#3)&c(#4)&ch&2,3&ch&ch&ch&ch&4";
            //String molS = "c(#1)&ch&ch&ch&ch&ch&1,1/c(ch2(.))/ch3";
            //String molS = "ch3/c(ch3)h/ch3";
            //String molS = "F/c(X)h/R";
            //String molS = "\'co\'h/ch3";
            //String molS = "c'Br'4";
            //String molS = "cf2//ch2";
            //String molS = "c(ch3)h//ch/ch3";
            //String molS = "c(ch3)2//ch/ch3";
            //String molS ="ch2(o'no2')/ch(o'no2')/ch2/o'no2'";
            //String molS = "'co'(#1)/ch2/ch2/ch2/ch2/ch2/1";
            //String molS = "ch3/co/h";
            //String molS = "ch3/'Br'";
            //convertToMolecule("of2");
            //convertToMolecule("coh/ch3");
            //convertToMolecule("ch2(c///ch)/c///ch");
            convertToMolecule(molS);
        } catch (SQLException ex) {
            Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CDKException ex) {
            Logger.getLogger(TestNancyLinearForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

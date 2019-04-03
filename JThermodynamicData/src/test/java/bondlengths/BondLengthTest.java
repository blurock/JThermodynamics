/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bondlengths;

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
import thermo.build.ReadBondLengthTable;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.bonds.BondLength;
import thermo.data.structure.bonds.DB.SQLBondLength;

/**
 *
 * @author edwardblurock
 */
public class BondLengthTest {

    public BondLengthTest() {
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
    public void simple() {
        String a1 = "c";
        String a2 = "o";
        int bondorder = 2;
        double bondlength = 3.0;
        String src = "Test";

        BondLength bond = new BondLength(a1, a2, bondorder, bondlength, src);
        System.out.println(bond.toString());
    }
    @Test
    public void readFromLine() {
        String bondLine = "c h 1 3.0";
        String source = "Test";
        BondLength bond = new BondLength();
        bond.readLine(bondLine, source);
        System.out.println(bond.toString());
    }
    @Test
    public void putInDatabase() {
        try {
            String bondLine = "c h 1 3.0";
            String source = "Test";
            BondLength bond = new BondLength();
            bond.readLine(bondLine,source);
            System.out.println(bond.toString());
            ThermoSQLConnection connect = new ThermoSQLConnection();
            connect.connect();
            SQLBondLength sql = new SQLBondLength(connect);
            sql.addToDatabase(bond);

            if(sql.query(bond)) {
                System.out.println("Query Successful: " + bond.toString());
            }

            HashSet<BondLength> vec = sql.retrieveStructuresFromDatabase(sql.keyFromStructure(bond));
            if(vec.size()==1) {
                Iterator<BondLength> iter = vec.iterator();
                BondLength bnd = iter.next();
                System.out.println("Retrieved: " + bnd.toString());
            }
            sql.deleteByKey(sql.keyFromStructure(bond));
            if(!sql.query(bond)) {
                System.out.println("Delete Successful: " + bond.toString());
            }

        } catch (SQLException ex) {
            Logger.getLogger(BondLengthTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
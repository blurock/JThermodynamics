/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.bonds.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.bonds.BondLength;

/**
 *
 * @author edwardblurock
 */
public class SQLBondLength extends SQLStructureThermoAbstractInterface {

    /** construct
     *
     * @param connect
     */
    public SQLBondLength(ThermoSQLConnection connect) {
        super(connect);
        tableName = "BondLength";
        tableKey = "ElementName";
    }

    @Override
    public String[] formulateInsert(Object structure) {
         BondLength bond = (BondLength) structure;
        String[] commands = new String[1];
        String command = "INSERT INTO BondLength "
                + "(ElementName, Atom1, Atom2, BondOrder, BondLength, Source) "
                + "Values("
                + "\"" + this.keyFromStructure(bond) +"\","
                + "\"" + bond.getAtom1() + "\","
                + "\"" + bond.getAtom2() + "\","
                + bond.getBondOrder() + ","
                + bond.getBondLength() + ","
                + "\"" + bond.getSource() + "\""
                + ")";
        commands[0] = command;
        return commands;

    }

    @Override
    public boolean query(Object structure) throws SQLException {
        BondLength bond = (BondLength) structure;
        Statement statement = database.createStatement();
        String sqlquery = "SELECT Atom1, Atom2, BondOrder, BondLength FROM BondLength WHERE "
                + "ElementName=\"" + this.keyFromStructure(bond) + "\";";
        ResultSet elements = statement.executeQuery(sqlquery);
        return elements.first();
    }

    @Override
    public HashSet<BondLength> retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT Atom1, Atom2, BondOrder, BondLength, Source FROM BondLength WHERE "
                + "ElementName=\"" + name + "\";";
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet<BondLength> vec = null;
        boolean first = elements.first();
        if(first) {
            vec = new HashSet<BondLength>(1);
            String atom1 = elements.getString("Atom1");
            String atom2 = elements.getString("Atom2");
            int order = elements.getInt("BondOrder");
            double length = elements.getDouble("BondLength");
            String src = elements.getString("Source");
            BondLength found = new BondLength(atom1,atom2,order,length,src);
            vec.add(found);
        }
        return vec;
    }
    
    @Override
    public String keyFromStructure(Object structure) {
        BondLength bond = (BondLength) structure;
        return bond.getAtom1() + "." + bond.getAtom2() + "." + bond.getBondOrder();
    }

}

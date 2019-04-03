/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.structure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.structure.AtomCount;

/**
 *
 * @author edwardblurock
 */
public class SQLAtomCount extends SQLStructureThermoAbstractInterface {

    /** Constructor
     *
     * @param connect The connection to the SQL database
     */
    public SQLAtomCount(ThermoSQLConnection connect) {
        super(connect);
        tableName = "AtomCounts";
        tableKey = "AtomCountKey";
     }
     @Override
    public String[] formulateInsert(Object structure) {
        AtomCount count = (AtomCount) structure;
        String key = keyFromStructure(structure);
        String command = "INSERT INTO AtomCounts (AtomCountKey, Molecule, AtomSymbol, AtomCount) "
                + "VALUES("
                + "\"" + key + "\", "
                + "\"" + count.getMolecule() + "\", "
                + "\"" + count.getSymbolName() + "\", "
                + count.getAtomCount()
                + ");";
     String[] commands = new String[1];
     commands[0] = command;
     return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HashSet<AtomCount> retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT Molecule, AtomSymbol, AtomCount FROM AtomCounts WHERE Molecule=\""
                + name + "\"";
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet<AtomCount> vec = transferAllElements(elements);
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        AtomCount count = (AtomCount) structure;
        String key = count.getMolecule() + "-" + count.getSymbolName();
        return key;
       
    }

    private HashSet<AtomCount> transferAllElements(ResultSet elements) throws SQLException {
        HashSet<AtomCount> vec = new HashSet<AtomCount>();
        boolean next = elements.first();
        while(next) {
            AtomCount count = new AtomCount();
            count.setMolecule(elements.getString("Molecule"));
            count.setSymbolName(elements.getString("AtomSymbol"));
            count.setAtomCount(new Integer(elements.getString("AtomCount")));
            vec.add(count);
            next = elements.next();
        }
        return vec;

    }

}

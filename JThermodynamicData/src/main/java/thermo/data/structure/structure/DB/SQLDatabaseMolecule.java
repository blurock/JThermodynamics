/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.structure.structure.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;
import thermo.data.structure.DB.SQLStructureAsCML;
import thermo.data.structure.structure.DatabaseMolecule;

/**
 *
 * @author edwardblurock
 */
public class SQLDatabaseMolecule extends SQLStructureThermoAbstractInterface {

    public SQLDatabaseMolecule(ThermoSQLConnection connect) {
        super(connect);
        tableName = "DatabaseMolecule";
        tableKey = "Molecule";
    }

    @Override
    public String[] formulateInsert(Object structure) {
        DatabaseMolecule molecule = (DatabaseMolecule) structure;
        String[] command = new String[1];
        SQLStructureAsCML sqlcml = new SQLStructureAsCML(null);
        String cmlmolecule = sqlcml.cmlStructureStringToSQLString(molecule.getCMLStructure());
        command[0] = "INSERT INTO DatabaseMolecule (Molecule, CMLStructure, Source) "
                + "VALUES("
                + "\"" + molecule.getMolecule() + "\", "
                + "\"" + cmlmolecule + "\", "
                + "\"" + molecule.getSource() + "\");";

        return command;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        Statement statement = database.createStatement();
        boolean ans = false;
        DatabaseMolecule molecule = (DatabaseMolecule) structure;
        if (molecule.getMolecule() != null) {
            String sqlquery = "SELECT Molecule, CMLStructure, Source FROM DatabaseMolecule WHERE Molecule=\"" + molecule.getMolecule() + "\"";
            ResultSet elements = statement.executeQuery(sqlquery);
            boolean next = elements.first();
            HashSet vec = null;
            if (next) {
                molecule.setCMLStructure("CMLStructure");
                molecule.setSource("Source");
                ans = true;
            }
        }
        return ans;
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
        Statement statement = database.createStatement();
        String sqlquery = "SELECT Molecule, CMLStructure, Source FROM DatabaseMolecule WHERE Molecule=\"" + name + "\"";
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        HashSet vec = null;
        if (next) {
            vec = new HashSet(1);
            DatabaseMolecule molecule =
                    new DatabaseMolecule(elements.getString("Molecule"),
                    elements.getString("CMLStructure"),
                    elements.getString("Source"));
            vec.add(molecule);
        } else {
            vec = new HashSet();
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        DatabaseMolecule molecule = (DatabaseMolecule) structure;
        return molecule.getMolecule();
    }
}

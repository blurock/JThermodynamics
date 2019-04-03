/*
 */
package thermo.data.benson.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import thermo.data.benson.BensonConnectAtomStructure;
import thermo.data.benson.BensonGroupStructure;

/**
 * This is the class that performs the manipulations of the SQL 'GroupElement'
 * table.
 *
 * This class basically is responsible for storing the information of
 * {@link BensonGroupStructure} into the GroupElement table.
 *
 * The GroupElement table is basically one connection of the
 * {@link BensonGroupStructure} with the following elements: <ul> <li>
 * CenterAtom: The center atom string <li> ConnectingAtom: The connecting atom
 * string <li> Count: The multiplicity of the connection <li> ElementName: The
 * name of the {@link BensonGroupStructure} <li> ConnectElementName: unique key
 * name for this element </ul> This class decomposes the
 * {@link BensonGroupStructure} class and creates and entry in the table for
 * each connection. The information of the connections in
 * {@link BensonGroupStructure} is contained in the set of table entries with
 * the same 'ElementName'.
 *
 * @author blurock
 */
public class SQLGroupElement {

    private ThermoSQLConnection database;

    /**
     * The constructor with the connection to the database
     *
     * @param db
     */
    public SQLGroupElement(ThermoSQLConnection db) {
        database = db;
    }

    /**
     * Add the {@link BensonGroupStructure} to the GroupElement table
     *
     * Call {@link SQLGroupElement#formulateInsert(thermo.data.benson.BensonGroupStructure)
     * }
     * to a set of SQL commands and execute them.
     *
     * @param structure The set of connections to be put in the GroupElement
     * table
     * @throws java.sql.SQLException
     */
    public void addToDatabase(BensonGroupStructure structure) throws SQLException {
        Statement statement = database.createStatement();
        String[] commands = formulateInsert(structure);
        for (int i = 0; i < commands.length; i++) {
            statement.execute(commands[i]);
        }
    }

    public void deleteGroupElementFromDatabase(BensonGroupStructure structure) throws SQLException {
        String name = structure.getStructureName();
        String sqlcommand = "DELETE From GroupElement where ElementName=\"" + name + "\"";
        Statement statement = database.createStatement();
        statement.execute(sqlcommand);
    }

    /**
     * Create a string of SQL commands to copy the entire GroupElement table
     *
     * @return SQL commands to copy the entire GroupElement table
     * @throws java.sql.SQLException
     */
    public String databaseCopy() throws SQLException {
        StringBuilder buf = new StringBuilder();
        HashSet<BensonGroupStructure> vec = retrieveDatabase();
        Iterator<BensonGroupStructure> iter = vec.iterator();
        while (iter.hasNext()) {
            String[] commands = formulateInsert(iter.next());
            for (int j = 0; j < commands.length; j++) {
                buf.append(commands[j]);
            }
        }
        return buf.toString();
    }

    /**
     * Create SQL commands to put {@link BensonGroupStructure} into GroupElement
     *
     * The set of connections is looped over and for each, a GroupElement table
     * element is created.
     *
     * @param structure The class of connections
     * @return The set of SQL commands
     */
    public String[] formulateInsert(BensonGroupStructure structure) {
        //Vector<String> vec = new Vector<String>();
        String name = structure.getStructureName();
        String centerS = structure.getCenterAtomS();
        Vector<BensonConnectAtomStructure> con = structure.getBondedAtoms();
        String[] vec;
        if (con.size() > 0) {
            vec = new String[con.size()];
            for (int i = 0; i < con.size(); i++) {
                String conS = con.elementAt(i).getConnectedAtomS();
                int multiplicity = con.elementAt(i).getMultiplicity();
                String idS = name + "-" + String.valueOf(i);
                String sqlcommand = "INSERT INTO GroupElement (ElementName, CenterAtom, ConnectingAtom,Count,ConnectElementName)"
                        + "VALUES("
                        + "\"" + name + "\","
                        + "\"" + centerS + "\","
                        + "\"" + conS + "\","
                        + multiplicity + ","
                        + "\"" + idS + "\")"
                        + ";";
                vec[i] = sqlcommand;
            }
        } else {
            vec = new String[1];
            String idS = name + "-0";
            String sqlcommand = "INSERT INTO GroupElement (ElementName, CenterAtom, ConnectingAtom,Count,ConnectElementName)"
                    + "VALUES("
                    + "\"" + name + "\","
                    + "\"" + centerS + "\","
                    + "\"None\", 0"
                    + ");";
            vec[0] = sqlcommand;
        }
        return vec;
    }

    /**
     * All elements in the GroupElement table are deleted
     *
     * @throws java.sql.SQLException
     */
    public void initializeStructureInDatabase() throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM GroupElement");
    }

    /**
     * All elements in the GroupElement table are deleted
     *
     * @throws java.sql.SQLException
     */
    public void initializeStructureInDatabase(String reference) throws SQLException {
        Statement statement = database.createStatement();
        statement.execute("DELETE FROM GroupElement WHERE ");
    }

    /**
     * Fill in the {@link BensonGroupStructure} with info from the GroupElement
     * table
     *
     * if the name of structure ({@link BensonGroupStructure#getStructureName()
     * }) is null, then query the structure for the matching element by calling {@link SQLGroupElement#queryName(thermo.data.benson.BensonGroupStructure)
     * }
     *
     * @param structure The basis of the query
     * @return The filled in structure
     * @throws java.sql.SQLException
     */
    public boolean query(BensonGroupStructure structure) throws SQLException {
        boolean success = false;
        if (structure.getStructureName() == null) {
            success = queryName(structure);
        }
        return success;
    }

    /**
     * This is the heart of finding a matching {@link BensonGroupStructure} from
     * the GroupElement database
     *
     * A query is set up for an element that matches all the connections in the
     * {@link BensonGroupStructure}. This match returns the first match, i.e.
     * assuming a unique match. {@link SQLGroupElement#queryNameSet(thermo.data.benson.BensonGroupStructure)
     * }
     * is the routine that finds multiple matches.
     *
     * @param structure where the name should be filled in
     * @return true if a matching connection was found
     * @throws java.sql.SQLException
     */
    private boolean queryName(BensonGroupStructure structure) throws SQLException {
        boolean success = false;
        StringBuffer sqlqueryB = new StringBuffer();
        sqlqueryB.append("SELECT DISTINCT p1.ElementName");
        Vector<BensonConnectAtomStructure> bonded = structure.getBondedAtoms();
        sqlqueryB.append(" From ");
        if (bonded.size() > 0) {
            for (int i = 0; i <= bonded.size(); i++) {
                if (i != 0) {
                    sqlqueryB.append(" INNER JOIN ");
                }
                String iS = String.valueOf(i + 1);
                sqlqueryB.append("GroupElement as p" + iS + " ");
            }
            sqlqueryB.append(" WHERE ");

            for (int i = 0; i < bonded.size(); i++) {
                if (i != 0) {
                    sqlqueryB.append(" AND ");
                }
                String iS = String.valueOf(i + 1);
                String ip1S = String.valueOf(i + 2);
                sqlqueryB.append(" p" + iS + ".ElementName = p" + ip1S + ".ElementName ");
            }
            sqlqueryB.append("AND");
            sqlqueryB.append(" p1.CenterAtom = \"" + structure.getCenterAtomS() + "\"");
        } else {
            sqlqueryB.append(" GroupElement as p1 WHERE CenterAtom = \"");
            sqlqueryB.append(structure.getCenterAtomS().toLowerCase());
            sqlqueryB.append("\" AND ConnectingAtom = \"None\"");
        }

        for (int i = 0; i < bonded.size(); i++) {
            String iS = String.valueOf(i + 1);
            sqlqueryB.append(" AND p" + iS + ".ConnectingAtom = \"" + bonded.elementAt(i).getConnectedAtomS() + "\" ");
            sqlqueryB.append(" AND p" + iS + ".Count = " + bonded.elementAt(i).getMultiplicity() + " ");
        }
        sqlqueryB.append(";");
        //System.out.println(sqlqueryB.toString());
        Statement statement = database.createStatement();
        ResultSet set = statement.executeQuery(sqlqueryB.toString());
        if (set.first()) {
            String name = set.getString("ElementName");
            //System.out.println("Query name: " + name);
            structure.setStructureName(name);
            success = true;
        }
        return success;
    }

    /**
     * This is the heart of finding a set of matching
     * {@link BensonGroupStructure} from the GroupElement database A query is
     * set up for an element that matches all the connections in the
     * {@link BensonGroupStructure}.
     *
     *
     * @param structure
     * @return The array of names matching the connections
     * @throws java.sql.SQLException
     */
    public String[] queryNameSet(BensonGroupStructure structure) throws SQLException {
        boolean success = false;
        StringBuffer sqlqueryB = new StringBuffer();
        sqlqueryB.append("SELECT DISTINCT p1.ElementName");
        Vector<BensonConnectAtomStructure> bonded = structure.getBondedAtoms();
        sqlqueryB.append(" From ");
        if (bonded.size() > 0) {
            for (int i = 0; i <= bonded.size(); i++) {
                if (i != 0) {
                    sqlqueryB.append(" INNER JOIN ");
                }
                String iS = String.valueOf(i + 1);
                sqlqueryB.append("GroupElement as p" + iS + " ");
            }
            sqlqueryB.append(" WHERE ");

            for (int i = 0; i < bonded.size(); i++) {
                if (i != 0) {
                    sqlqueryB.append(" AND ");
                }
                String iS = String.valueOf(i + 1);
                String ip1S = String.valueOf(i + 2);
                sqlqueryB.append(" p" + iS + ".ElementName = p" + ip1S + ".ElementName ");
            }
            sqlqueryB.append("AND");
            sqlqueryB.append(" p1.CenterAtom = \"" + structure.getCenterAtomS() + "\"");
        } else {
            sqlqueryB.append(" GroupElement as p1 WHERE CenterAtom = \"");
            sqlqueryB.append(structure.getCenterAtomS());
            sqlqueryB.append("\" AND ConnectingAtom = \"None\"");
        }

        for (int i = 0; i < bonded.size(); i++) {
            String iS = String.valueOf(i + 1);
            sqlqueryB.append(" AND p" + iS + ".ConnectingAtom = \"" + bonded.elementAt(i).getConnectedAtomS() + "\" ");
            sqlqueryB.append(" AND p" + iS + ".Count = " + bonded.elementAt(i).getMultiplicity() + " ");
        }
        sqlqueryB.append(";");
        //System.out.println(sqlqueryB.toString());
        Statement statement = database.createStatement();
        ResultSet set = statement.executeQuery(sqlqueryB.toString());
        Vector<String> namesV = new Vector();
        boolean next = set.first();
        while (next) {
            String name = set.getString("ElementName");
            //System.out.println("Query name: '" + name + "'");
            namesV.add(name);
            next = set.next();
        }
        String[] names = new String[namesV.size()];
        for (int i = 0; i < namesV.size(); i++) {
            names[i] = namesV.elementAt(i);
        }

        return names;
    }

    /**
     * Construct a vector of {@link BensonGroupStructure} from the database
     *
     * @return the vector of {@link BensonGroupStructure} from the database
     * @throws java.sql.SQLException
     */
    public HashSet<BensonGroupStructure> retrieveDatabase() throws SQLException {
        HashSet<BensonGroupStructure> vec = new HashSet<BensonGroupStructure>();
        Statement statement = database.createStatement();
        String sqlquery = "SELECT ElementName From GroupElement;";
        ResultSet names = statement.executeQuery(sqlquery);
        boolean next = names.first();
        while (next) {
            String name = names.getString("ElementName");
            BensonGroupStructure structure = retrieveStructureFromDatabase(name);
            vec.add(structure);
        }
        return vec;
    }

    /**
     * Given an ElementName construct a {@link BensonGroupStructure}
     *
     * @param name The ElementName with in the GroupElement table
     * @return The matching structure
     * @throws java.sql.SQLException
     */
    public BensonGroupStructure retrieveStructureFromDatabase(String name) throws SQLException {
        BensonGroupStructure grp = new BensonGroupStructure(name);
        Statement statement = database.createStatement();
        String sqlquery = "SELECT ElementName From GroupElement;";
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        if (next) {
            grp.setCenterAtomS(elements.getString("CenterAtom"));
        }
        while (next) {
            String atom = elements.getString("ConnectingAtom");
            int m = elements.getInt("Count");
            BensonConnectAtomStructure connect = new BensonConnectAtomStructure(name, m);
            grp.addBondedAtom(connect);
        }
        return grp;
    }

    public void deleteUsingElementName(String name) throws SQLException {
        Statement statement = database.createStatement();
        String del = "DELETE From GroupElement WHERE ElementName=\"" + name + "\";";
        statement.execute(del);
    }

    /**
     *
     * @param names
     * @throws SQLException
     */
    public void deleteUsingElementNames(HashSet<String> names) throws SQLException {
        Iterator<String> name = names.iterator();
        while (name.hasNext()) {
            deleteUsingElementName(name.next());
        }
    }
}

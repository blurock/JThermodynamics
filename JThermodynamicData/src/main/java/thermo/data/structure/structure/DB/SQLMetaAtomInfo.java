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
import thermo.data.structure.structure.MetaAtomInfo;

/**
 *
 * @author blurock
 */
public class SQLMetaAtomInfo extends SQLStructureThermoAbstractInterface {

    
    public SQLMetaAtomInfo(ThermoSQLConnection connect) {
        super(connect);
        tableName = "MetaAtomInfo";
        tableKey = "MetaAtomKey";
     }
    @Override
    public String[] formulateInsert(Object structure) {
        MetaAtomInfo info = (MetaAtomInfo) structure;
        String[] commands = new String[1];
        
        String key = this.keyFromStructure(info);
        String command = "INSERT INTO MetaAtomInfo (MetaAtomKey, MetaAtomType, MetaAtomName, ElementName) " 
                + "VALUES("
                + "\"" + key + "\", "
                + "\"" + info.getMetaAtomType() + "\", " 
                + "\"" + info.getMetaAtomName() + "\", "
                + "\"" + info.getElementName()  + "\" "
                + ");";
        commands[0] = command;
        
        return commands;

    }

    @Override
    public boolean query(Object structure) throws SQLException {
        MetaAtomInfo info = (MetaAtomInfo) structure;
        String sqlquery = "SELECT MetaAtomType, MetaAtomName, ElementName FROM MetaAtomInfo ";
        if(info.getMetaAtomType() != null) {
            if(info.getMetaAtomName() != null) {
        sqlquery += " WHERE "
                + "MetaAtomType=\"" + info.getMetaAtomType() + "\""
                + "AND " 
                + "MetaAtomName=\"" + info.getMetaAtomName() + "\""
                + ";";
            } else {
        sqlquery += "WHERE "
                + "MetaAtomType=\"" + info.getMetaAtomType() + "\""
                + ";";
            }
        }
        //System.out.println(sqlquery);
        Statement statement = database.createStatement();
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet vec = transferAllElements(elements);
        return true;        
    }

    @Override
    public HashSet retrieveStructuresFromDatabase(String name) throws SQLException {
         Statement statement = database.createStatement();
        String sqlquery = "SELECT MetaAtomType, MetaAtomName, ElementName  FROM MetaAtomInfo WHERE MetaAtomKey=\""
                + name + "\";";
        //System.out.println(sqlquery);
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet vec = transferAllElements(elements);
        return vec;
    }
    public HashSet transferAllElements(ResultSet elements) throws SQLException {
        HashSet vec = new HashSet();
        boolean next = elements.first();
        while(next) {
            MetaAtomInfo info = new MetaAtomInfo();
            info.setElementName(elements.getString("ElementName"));
            info.setMetaAtomType(elements.getString("MetaAtomType"));
            info.setMetaAtomName(elements.getString("MetaAtomName"));

            vec.add(info);
            next = elements.next();
        }
        return vec;
        
    }
    @Override
    public String keyFromStructure(Object structure) {
        MetaAtomInfo info = (MetaAtomInfo) structure;
        String key = info.getMetaAtomType() + "." + info.getMetaAtomName();
        return key;
    }
    public HashSet retrieveMetaAtomTypesFromDatabase(String name) throws SQLException {
         Statement statement = database.createStatement();
        String sqlquery = "SELECT MetaAtomType, MetaAtomName, ElementName  FROM MetaAtomInfo WHERE MetaAtomType=\""
                + name + "\";";
        ResultSet elements = statement.executeQuery(sqlquery);
        HashSet vec = transferAllElements(elements);
        return vec;
    }
    

}

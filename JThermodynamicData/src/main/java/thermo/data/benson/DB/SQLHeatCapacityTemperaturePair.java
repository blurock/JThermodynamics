/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package thermo.data.benson.DB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import thermo.data.benson.HeatCapacityTemperaturePair;

/**
 *
 * @author blurock
 */
public class SQLHeatCapacityTemperaturePair extends SQLStructureThermoAbstractInterface {

    public SQLHeatCapacityTemperaturePair(ThermoSQLConnection connect) {
        super(connect);
        tableName = "HeatCapacityElement";
        tableKey = "ElementName";
    }
    
    @Override
    public String[] formulateInsert(Object obj) {
        HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) obj;
        String[] commands = new String[1];
        
        String command = "INSERT INTO HeatCapacityElement (ElementName, Temperature, HeatCapacity, Reference) " 
                + "VALUES("
                + "\"" + pair.getStructureName() + "\"," 
                + String.valueOf(pair.getTemperatureValue()) + ","
                + pair.getHeatCapacityValue() + ","
                + "\"" + pair.getReference() + "\"" 
                + ");";
        commands[0] = command;
        
        return commands;
    }

    @Override
    public boolean query(Object structure) throws SQLException {
        boolean success = false;
        HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) structure;
        
        if(pair.getStructureName() == null) {
            
        } else if(pair.getStructureName().length() == 0) {
            
        } else {
            
        }
        return success;
    }

    @Override
    public HashSet<HeatCapacityTemperaturePair> retrieveStructuresFromDatabase(String name) throws SQLException {
        HashSet<HeatCapacityTemperaturePair> vec = new HashSet<HeatCapacityTemperaturePair>();
        Statement statement = database.createStatement();
        String sqlquery = "SELECT ElementName, HeatCapacity, Temperature, Reference From HeatCapacityElement WHERE ElementName=\""
                + name + "\";";
        //System.out.println(sqlquery);
        ResultSet elements = statement.executeQuery(sqlquery);
        boolean next = elements.first();
        while(next) {
            HeatCapacityTemperaturePair pair = new HeatCapacityTemperaturePair();
            pair.setStructureName(elements.getString("ElementName"));
            pair.setHeatCapacityValue(elements.getDouble("HeatCapacity"));
            pair.setTemperatureValue(elements.getDouble("Temperature"));
            pair.setReference(elements.getString("Reference"));
            vec.add(pair);
            next = elements.next();
        }
        return vec;
    }

    @Override
    public String keyFromStructure(Object structure) {
        HeatCapacityTemperaturePair pair = (HeatCapacityTemperaturePair) structure;
        return pair.getStructureName();
    }
        
}

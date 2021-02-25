package molecules;


import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Test;

import thermo.DB.SQLStructureFactory;
import thermo.data.benson.DB.SQLStructureThermoAbstractInterface;
import thermo.data.benson.DB.ThermoSQLConnection;

public class SQLStructureAsCMLTest {

	@Test
	public void test() {
		ThermoSQLConnection connect = new ThermoSQLConnection();
		boolean connected = connect.connect();
		if(connected) {
		SQLStructureFactory factory = SQLStructureFactory.valueOf("structure");
		SQLStructureThermoAbstractInterface structureinterface = factory.getInterface(connect);
		
		ArrayList<String> names;
		try {
			names = structureinterface.retrieveDatabaseNames();
			System.out.println(names);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		} else {
			System.out.println("Not connected");
		}
	}

}

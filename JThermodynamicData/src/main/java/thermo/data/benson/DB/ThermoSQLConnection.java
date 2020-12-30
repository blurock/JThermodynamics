/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thermo.data.benson.DB;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import thermo.properties.SProperties;

/**
 *
 * @author blurock
 */
public class ThermoSQLConnection {

    private Connection connection;

    /**
     * Connect to the SQL 'Thermodynamics' database
     *
     * Using the SQL password for the 'Thermodynamics' database, the
     * {@link Connection} is made.
     *
     * The database connection is made through the system parameter 
     *
     * @return true if the connection is successful
     */
    public boolean connect() {

        String conS = SProperties.getProperty("thermo.database.connection");
        String user = SProperties.getProperty("thermo.database.dbuser");
        String pass = SProperties.getProperty("thermo.database.dbpassword");
        return connect(conS, user, pass);
    }

    /**
     *
     * @param conS The mysql connector (such as jdbc:mysql://localhost:3306/thermodynamics)
     * @param user The user name of root 
     * @param pass The password
     * @return true if the connection was successful
     */
    public boolean connect(String conS, String user, String pass) {
    	
    	//boolean success = connectHikari(conS,user,pass);
    	boolean success = connectLocal(conS,user,pass);
        return success;
    }
    public boolean connectHikari(String conS, String user, String pass) {
    	boolean success = true;
        // Saving credentials in environment variables is convenient, but not secure - consider a more
        // secure solution such as https://cloud.google.com/kms/ to help keep secrets safe.

        // The configuration object specifies behaviors for the connection pool.
        HikariConfig config = new HikariConfig();

        // Configure which instance and what database user to connect with.
        config.setJdbcUrl(conS);
        config.setUsername(user); // e.g. "root", "postgres"
        config.setPassword(pass); // e.g. "my-password"

        // For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections.
        // See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.
        config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
        config.addDataSourceProperty("cloudSqlInstance", "jthermodynamics:europe-west1:jthermodynamics");
        config.addDataSourceProperty("useSSL", "false");

        // ... Specify additional connection properties here.
        // [START_EXCLUDE]
        // [START cloud_sql_mysql_servlet_limit]
        // maximumPoolSize limits the total number of concurrent connections this pool will keep. Ideal
        // values for this setting are highly variable on app design, infrastructure, and database.
        config.setMaximumPoolSize(5);
        // minimumIdle is the minimum number of idle connections Hikari maintains in the pool.
        // Additional connections will be established to meet this value unless the pool is full.
        config.setMinimumIdle(5);
        // [END cloud_sql_mysql_servlet_limit]

        // [START cloud_sql_mysql_servlet_timeout]
        // setConnectionTimeout is the maximum number of milliseconds to wait for a connection checkout.
        // Any attempt to retrieve a connection from this pool that exceeds the set limit will throw an
        // SQLException.
        config.setConnectionTimeout(10000); // 10 seconds
        // idleTimeout is the maximum amount of time a connection can sit in the pool. Connections that
        // sit idle for this many milliseconds are retried if minimumIdle is exceeded.
        config.setIdleTimeout(600000); // 10 minutes
        // [END cloud_sql_mysql_servlet_timeout]

        // [START cloud_sql_mysql_servlet_backoff]
        // Hikari automatically delays between failed connection attempts, eventually reaching a
        // maximum delay of `connectionTimeout / 2` between attempts.
        // [END cloud_sql_mysql_servlet_backoff]
        // [START cloud_sql_mysql_servlet_lifetime]
        // maxLifetime is the maximum possible lifetime of a connection in the pool. Connections that
        // live longer than this many milliseconds will be closed and reestablished between uses. This
        // value should be several minutes shorter than the database's timeout value to avoid unexpected
        // terminations.
        config.setMaxLifetime(1800000); // 30 minutes
        // [END cloud_sql_mysql_servlet_lifetime]

        // [END_EXCLUDE]
        // Initialize the connection pool using the configuration object.
        DataSource pool = new HikariDataSource(config);
        try {
            connection = pool.getConnection();

            /*
            connection = DriverManager.getConnection(conS, user, pass);
             */
            if (connection != null) {
//                Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,"Successful connection");
            } else {
                Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO, "Unsuccessful connection");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                    "Trying to connect and failed with SQLException");

            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } catch (Exception ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.INFO,
                    "Trying to connect and failed with simple exception");
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, ex.toString());
            success = false;
        }
        return success;
    }
    
    public boolean connectLocal(String conS, String user, String pass) {
    	boolean success = true;
    	try {
			connection = DriverManager.getConnection(conS, user, pass);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	return success;
    }
    

    /**
     * Close the connection
     *
     * @return true if successful
     */
    public boolean close() {
        boolean success = true;
        try {

            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ThermoSQLConnection.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        }
        return success;
    }

    /**
     * Create a {@link Statement} from the {@link Connection}
     *
     * @return the {@link Statement} to be able to form SQL queries on the
     * thermodynamics database
     * @throws java.sql.SQLException
     */
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * Create a {@link PreparedStatement} from the {@link Connection}
     *
     * @param s The query
     * @return a {@link PreparedStatement} from the {@link Connection} with the
     * query
     * @throws java.sql.SQLException
     */
    public PreparedStatement createPreparedStatement(String s) throws SQLException {
        return connection.prepareStatement(s);
    }

    /**
     * Set auto commit for the connection.
     *
     * @param on
     * @throws java.sql.SQLException
     */
    public void setAutoCommit(boolean on) throws SQLException {
        connection.setAutoCommit(on);
    }

}

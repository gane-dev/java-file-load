

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import org.apache.log4j.Logger;
/**
 * This program demonstrates how to make database connection with Oracle
 * database server.
 *
 *
 */
public class JdbcOracleConnection {
    final static Logger logger = Logger.getLogger(JdbcOracleConnection.class);
    public static Connection getConnection(String connectionString) {

        Connection conn1 = null;

        try {

            //String dbURL1 = "jdbc:oracle:thin:" + gdevaraj/password5Ganesh@purststscan.avendra.com:1521/purstest.avendra.com";
            //String dbURL1 = "jdbc:oracle:thin:" + propertyObject.getPropValues("db_user") + "/" + propertyObject.getPropValues("db_pwd") + "@" +propertyObject.getPropValues("conn_string") ;
            // String dbURL1 = "jdbc:oracle:thin:data_load/BlueJay1@dracdb01-scan:1529/PURSRDEV_SRV";
            conn1 = DriverManager.getConnection(connectionString);
            if (conn1 != null) {
                conn1.setAutoCommit(false);
                return  conn1;

            }
            return  null;
        }

 catch (SQLException ex) {
     logger.error("Error",ex);
     return  null;

        }
        finally {
//            try {
//                if (conn1 != null && !conn1.isClosed()) {
//                    conn1.close();
//                }
//
//            } catch (SQLException ex) {
//                logger.error(ex.getStackTrace());
//            }
        }
    }
}
import org.apache.log4j.Logger;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoadFiles {
    final static Logger logger = Logger.getLogger(LoadFiles.class);
    static Connection conn;

    public static void main(String[] args)  {
        GetPropertyValues propertyObject = new GetPropertyValues();
        //log starting the load process
        logger.info("Start Load Process");

        FileManagement manageFiles;
        String filePath="";
        String archivePath="";
        String errorPath="";
        //get database connection
        try{
            String connString = "jdbc:oracle:thin:" +
                    propertyObject.getPropValues("db_user") + "/" +
                    propertyObject.getPropValues("db_pwd") + "@"
                    +propertyObject.getPropValues("conn_string");
            conn = JdbcOracleConnection.getConnection(connString);
            if (conn == null) {
                System.out.println("Error=> " + "DB Connection Error");
                logger.error(connString);
                logger.error("DB Connection Error");
                System.exit(0);
            }
            System.out.println("Info=> " + "DB Connection acquired");
            logger.error("DB Connection acquired");

        }
        catch (Exception iox)
        {
            System.out.println("Error=> " + "Unable to get Connection string");
            logger.error("DB Connection Error");
            System.exit(0);
        }
        filePath = propertyObject.getPropValues("file_path");
        archivePath = propertyObject.getPropValues("archive_path");
        errorPath = propertyObject.getPropValues("error_path");
        if (filePath != "" && archivePath != "" && errorPath != "") {
            //call Load Excel files
            System.out.println("Log=> " + "Loading Files started");
            try {
                manageFiles = new FileManagement(filePath, archivePath, errorPath, conn);
                int result = manageFiles.ProcessFiles();
                if (result == 0) {
                    System.out.println("Log=> " + "Loading Files completed");
                    logger.info("Loading Files completed");
                } else {
                    System.out.println("Log=> " + "Loading Files failed");
                    logger.error("Loading Files failed");
                }



            } catch (Exception ex) {
                System.out.println("Log=> " + "Loading Files failed");
                logger.error("Loading Files failed",ex);
            }
            finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                      // conn.rollback();
                        conn.close();
                    }
                }
                catch (SQLException sqx){
                    logger.error(sqx.getStackTrace());
                }
                manageFiles =null;
                propertyObject=null;
            }
        }

        else
        {
            System.out.println("Log=> " + "Missing file path");
            logger.error("Missing file path");
            System.exit(0);
        }
        }

}

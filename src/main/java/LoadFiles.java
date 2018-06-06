import org.apache.log4j.Logger;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoadFiles {
    final static Logger logger = Logger.getLogger(LoadFiles.class);
    static Connection conn;
    static  String connectionString = "";
    public  static Connection getConnection()
    {
       return JdbcOracleConnection.getConnection(connectionString);
    }
    public static void main(String[] args) {
        GetPropertyValues propertyObject = new GetPropertyValues("config.properties");
        //log starting the load process
        logger.info("Start Load Process");

        FileManagement manageFiles;
        String filePath = "";
        String archivePath = "";
        String errorPath = "";
        FileType fileType;
        //boolean excelType=true;
        //get database connection

            try {
                connectionString
                        = "jdbc:oracle:thin:" +
                        propertyObject.getPropValues("db_user") + "/" +
                        propertyObject.getPropValues("db_pwd") + "@"
                        + propertyObject.getPropValues("conn_string");
                // conn = JdbcOracleConnection.getConnection(connectionString);
                CommonObjects.setConnectionString(connectionString);
//            if (conn == null) {
//                System.out.println("Error=> " + "DB Connection Error");
//                logger.error(connectionString);
//                logger.error("DB Connection Error");
//                System.exit(0);
//            }
//            System.out.println("Info=> " + "DB Connection acquired");
//            logger.error("DB Connection acquired");

            } catch (Exception iox) {
                System.out.println("Error=> " + "Unable to get Connection string");
                logger.error("DB Connection Error");
                System.exit(0);
            }
        if (args[0].toUpperCase().equals("COPY")) {
            FileCopy fileCopy = new FileCopy();
            fileCopy.CopyFiles(args[1],args[2]);
            logger.info("File Copy completed");
        }
        else if (args[0].toUpperCase().equals("RUNTIME")){
                JobRuntime jobRuntime = new JobRuntime();
                jobRuntime.GetJobRuntimes(args[1],args[2]);
        }
        else if (args[0].toUpperCase().equals("JOBS")){
            RunJobs jobs = new RunJobs();
            jobs.RunUC4Jobs();
        }
        else {
            switch (args[0]) {
                case "0": {
                    fileType = FileType.EXCEL;
                    break;
                }
                case "1": {
                    fileType = FileType.EXCEL_OPTION;
                    break;
                }
                case "2": {
                    fileType = FileType.TEXT_PIPE_USAGE;
                    break;
                }
                case "3": {
                    fileType = FileType.TEXT_BANG_USAGE;
                    break;
                }
                case "4": {
                    fileType = FileType.TEXT_SYS_USAGE;
                    break;
                }
                case "5": {
                    fileType = FileType.TEXT_TAB_USAGE;
                    break;
                }
                case "6": {
                    fileType = FileType.TEXT_CAT_USAGE;
                    break;
                }
                case "7": {
                    fileType = FileType.TEXT_OPTION_USAGE;
                    break;
                }
                case "8": {
                    fileType = FileType.EXCEL_SHORT;
                    break;
                }
                default:
                    fileType = FileType.EXCEL;
            }

            filePath = propertyObject.getPropValues(fileType.name().toLowerCase() + "_file_path");
            archivePath = propertyObject.getPropValues(fileType.name().toLowerCase() + "_archive_path");
            errorPath = propertyObject.getPropValues(fileType.name().toLowerCase() + "_error_path");
            if (filePath != "" && archivePath != "" && errorPath != "") {

                //call Load Excel files
                CommonObjects.setFilePath(filePath);
                CommonObjects.setArchivePath(archivePath);
                CommonObjects.setErrorPath(errorPath);
                System.out.println("Log=> " + "Loading Files started");
                try {
                    manageFiles = new FileManagement(filePath, archivePath, errorPath, fileType);
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
                    logger.error("Loading Files failed", ex);
                } finally {
                    try {
                        if (conn != null && !conn.isClosed()) {
                            // conn.rollback();
                            conn.close();
                        }
                    } catch (SQLException sqx) {
                        logger.error(sqx.getStackTrace());
                    }
                    manageFiles = null;
                    propertyObject = null;
                }
            } else {
                System.out.println("Log=> " + "Missing file path");
                logger.error("Missing file path");
                System.exit(0);
            }
        }
    }

}


import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.*;
import java.nio.file.Path;
public class FileManagement {
    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    final static Logger logger = Logger.getLogger(FileManagement.class);
    String filePath="";
    String archivePath="";
    String errorPath="";
    static Connection conn;
    public  FileManagement(String p_filePath, String p_archivePath, String p_errorPath,Connection p_connection)
    {
        filePath=p_filePath;
        archivePath=p_archivePath;
        errorPath=p_errorPath;
        conn = p_connection;
    }
    public int ProcessFiles()
    {
        try
        {

         Files.newDirectoryStream(Paths.get(filePath),
                path -> path.toString().endsWith(".xlsx"))
                    .forEach(filePath -> {
                        try {
                            LoadFile(filePath.toFile());
                        }
                        catch(IOException ix)
                        {
                            logger.error("Error",ix);
                        }
                    });
            return  0;
        }

        catch (IOException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
    }
    public void LoadFile(File p_file) throws IOException {
        String fileName = p_file.getName();
        int fileId = ReadDB.getFileId(conn);
        String distId = ReadDB.getSupplierId(fileName.substring(0,fileName.indexOf("_")),conn);
        logger.info(fileName);
        logger.info(fileId);
        logger.info(distId);
        ExcelFile exFile = new ExcelFile(conn,p_file,fileId,fileName,distId,archivePath);
        try {

            int result = exFile.LoadFile();
            Date date = new Date();

            String archiveFileName = fileName.substring(0,fileName.lastIndexOf("."))+ sdf.format(date) +".xlsx";
            if (result == 1) {
                //empty - move to archive folder
                Files.copy(p_file.toPath(), new File(archivePath + "/" + archiveFileName).toPath());
            } else if (result == -1) {
                //error - move to error folder
                Files.copy(p_file.toPath(), new File(errorPath + "/" + archiveFileName).toPath());
            } else {
                //success - move to archive
                Files.copy(p_file.toPath(), new File(archivePath + "/" + archiveFileName).toPath());
            }
            Files.delete(p_file.toPath());
            conn.commit();
        } catch (IOException ix) {
            logger.error("Error",ix);

        }
        catch (SQLException ex) {
            logger.error("Error",ex);
        }
        finally {

            conn =null;
        }
        }
    }

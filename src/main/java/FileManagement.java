
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
  //  static Connection conn;
    int fileCount=0;
    FileType fileType;
    public  FileManagement(String p_filePath, String p_archivePath, String p_errorPath,FileType p_fileType)
    {
        filePath=p_filePath;
        archivePath=p_archivePath;
        errorPath=p_errorPath;

        fileType =p_fileType;
    }
    public int ProcessFiles()
    {

        try
        {

         Files.newDirectoryStream(Paths.get(filePath),"*.*")
            //    path -> path.toString().endsWith(".xlsx")  )
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
        finally {

            //conn =null;
        }
    }

    private int ArchiveFile(Path filePath,String fileName,boolean error)
    {
        Date date = new Date();

        String archiveFileName;
        try{
            if (fileType == FileType.EXCEL || fileType == FileType.EXCEL_OPTION|| fileType == FileType.EXCEL_SHORT)
                archiveFileName = fileName.substring(0, fileName.lastIndexOf(".")) + sdf.format(date) + ".xlsx";
            else
                archiveFileName= fileName.substring(0,fileName.lastIndexOf("."))+ sdf.format(date) +".txt";
        if (error)
            //error - move to error folder
            Files.copy(filePath, new File(errorPath + "/" + archiveFileName).toPath());
        else
            //empty - move to archive folder
            Files.copy(filePath, new File(archivePath + "/" + archiveFileName).toPath());


        Files.delete(filePath);
            return  -0;

    } catch (IOException ix) {
        logger.error("Error",ix);
        return  -1;
    }
        catch (Exception ix) {
            logger.error("Error",ix);
            return  -1;
        }
    }


    public void LoadFile(File p_file) throws IOException {

        String fileName = p_file.getName();

        logger.info(fileName);

        int archiveResult=-1;
        ExcelTextFile exFile =new ExcelTextFile(p_file,fileType);

        try {
            Date date = new Date();
            int result;
           result= exFile.LoadFile();


            if (result == -1) {
                //error - move to error folder
                archiveResult=  ArchiveFile(p_file.toPath(),fileName,true );
               if (archiveResult ==-1)
                   logger.error("Failed archive:"+p_file.toPath());
                //conn.rollback();
            } else {
                //success - move to archive
                archiveResult=  ArchiveFile(p_file.toPath(),fileName,false );
                if (archiveResult ==-1)
                    logger.error("Failed archive:"+p_file.toPath());
              //  conn.commit();
            }


        }
        catch (Exception ex) {
            logger.error("Error",ex);
        }

        }
    }

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class FileCopy {
    String excel_file_path="";
    String text_pipe_file_path="";

    String text_bang_file_path="";
    String text_cat_file_path="";
    String text_sys_file_path="";
    String text_tab_file_path="";
    String default_file_path="";
    String excel_short_file_path="";
Connection conn;
    final static Logger logger = Logger.getLogger(FileManagement.class);
    private String startDateTime="";
    private String endDateTime="";
    private String excelQuery =" select FILE_NAME,FILE_PATH from  ld_loader_log " +
            "where date_loaded > to_Date( '%1$s' ,'MMDDYYYYHH24MI') " +
            "and date_loaded < to_Date(  '%2$s' ,'MMDDYYYYHH24MI') " +
            "and file_option='Excel'" +
            "and table_name='DIST_MASTER_STG1'";


    private String textQuery ="select FILE_NAME,FILE_PATH  from  ld_loader_log " +
            "where date_loaded > to_Date(  '%1$s' ,'MMDDYYYYHH24MI') " +
            "and date_loaded < to_Date( '%2$s' ,'MMDDYYYYHH24MI') " +
            "and file_option <> 'Excel' " +
            "and table_name <> 'DIST_MASTER_STG1'";
    public FileCopy()
    {
        conn = JdbcOracleConnection.getConnection(CommonObjects.getConnectionString());
        GetPropertyValues propertyObject = new GetPropertyValues("filecopy.properties");
        excel_file_path=propertyObject.getPropValues("excel_file_path");
        text_bang_file_path=propertyObject.getPropValues("text_bang_file_path");

        text_pipe_file_path=propertyObject.getPropValues("text_pipe_file_path");
        text_cat_file_path=propertyObject.getPropValues("text_cat_file_path");
        text_tab_file_path=propertyObject.getPropValues("text_tab_file_path");
        text_sys_file_path=propertyObject.getPropValues("text_sys_file_path");
        default_file_path=propertyObject.getPropValues("default_file_path");
        excel_short_file_path=propertyObject.getPropValues("excel_short_file_path");
    }
    public int CopyFiles(String p_startDateTime,String p_endDateTime)
    {

        startDateTime = p_startDateTime;
        endDateTime = p_endDateTime;
        try {
            GetFiles(true);
            GetFiles(false);

            return 0;
        }
        catch (Exception ex)
        {
            logger.error("error",ex);
            return -1;
        }
        finally {


            try {
                if (conn != null && !conn.isClosed()) {
                    // conn.rollback();
                    conn.close();
                }
            } catch (SQLException sqx) {
                logger.error(sqx.getStackTrace());
            }
        }
    }
    private int GetFiles(boolean excel)
    {
        Statement stmt = null;
        String  l_fileName= "";
        String  l_filePath= "";
        String sql ="";

        ResultSet rs;
        try {
            if (excel)
                sql = String.format(excelQuery,startDateTime,endDateTime);
            else
                sql = String.format(textQuery,startDateTime,endDateTime);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                l_fileName=  rs.getString(1);
                l_filePath=  rs.getString(2);
                CopyFile(l_fileName,l_filePath);
            }
            return  0;
        }
        catch (SQLException sx)
        {
            logger.error("Error",sx);
            return -1;

        }
        finally {
            rs=null;
            stmt=null;

        }

    }
    private int CopyFile(String fileName,String filePath)
    {

        try {
            if (GetCopyPath(filePath).isPresent())
                Files.copy(new File(filePath + "/" +fileName).toPath() , new File(GetCopyPath(filePath).get() + "/" + fileName).toPath());
            else
                return 0;
        }
        catch (IOException iox)
        {
            logger.error("Error",iox);
            return -1;
        }
        return 0;
    }
    private Optional<String> GetCopyPath(String filePath) {
        Optional<String> copyPath = null;
        if (filePath.toUpperCase().contains("SHORT"))
            copyPath = Optional.of(excel_short_file_path);
        if (filePath.toUpperCase().contains("EXCEL"))
            copyPath = Optional.of(excel_file_path);
        else if (filePath.toUpperCase().contains("GTIN"))
            copyPath = Optional.of(default_file_path);
        else if (filePath.toUpperCase().contains("PIPE"))
            copyPath = Optional.of(text_pipe_file_path);
        else if (filePath.toUpperCase().contains("BANG"))
            copyPath = Optional.of(text_bang_file_path);
        else if (filePath.toUpperCase().contains("CAT"))
            copyPath = Optional.of(text_cat_file_path);
        else if (filePath.toUpperCase().contains("SYS"))
            copyPath = Optional.of(text_sys_file_path);
        else if (filePath.toUpperCase().contains("TAB"))
            copyPath = Optional.of(text_tab_file_path);
        else
            copyPath = Optional.of(default_file_path);

        File directory = new File(copyPath.get());
        if (!directory.exists()) {
            directory.mkdirs();
        }
            return copyPath;

    }

}

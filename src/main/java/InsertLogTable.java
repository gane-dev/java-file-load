
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import java.time.LocalDate;


import org.apache.log4j.Logger;

public class InsertLogTable {
    final static Logger logger = Logger.getLogger(InsertLogTable.class);
     private String insertSQL ="INSERT INTO LD_LOADER_LOG " +
            "(FILE_NAME, " +
            "RECORDS_SKIPPED," +
            "RECORDS_READ," +
            "RECORDS_REJECTED," +
            "RECORDS_DISCARDED," +
            //"DATE_LOADED," +
            "FILE_MAP," +
            "TABLE_NAME," +
            "FILE_ID," +
            //"DATE_LOADED_TRUNC," +
            "FILE_PATH," +
            "FILE_OPTION," +
            "DIST_ID," +
            "RECORDS_LOADED," +
            "MASTER_FILE_ID" +

            " ) "  + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement insertStatement;

    public int InsertRecord(String p_fileName,Connection p_conn,int totalRecords,String p_distId,int p_fileId,String p_archivePath)
    {
        try
        {
            insertStatement=p_conn.prepareStatement(insertSQL);
            insertStatement.setString(1,p_fileName);
            insertStatement.setInt(2,0);
            insertStatement.setInt(3,totalRecords);
            insertStatement.setInt(4,0);
            insertStatement.setInt(5,0);
            //insertStatement.setString(6, LocalDate.now().toString());
            insertStatement.setString(6, p_fileName.substring(0,p_fileName.indexOf("_")-1) + "_Pervasive");
            insertStatement.setString(7, "DIST_MASTER_STG1");
            insertStatement.setInt(8, p_fileId);
            //insertStatement.setString(10, LocalDate.now().toString());
            insertStatement.setString(9,p_archivePath);
            insertStatement.setString(10,"Excel");
            insertStatement.setString(11,p_distId);
            insertStatement.setInt(12,0);
            insertStatement.setString(13,"M" + p_fileId);
            insertStatement.execute();
            return  0;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
        finally {
          //  insertSQL="";
        //    insertStatement=null;

        }
    }
}

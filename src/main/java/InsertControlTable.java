
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import org.apache.log4j.Logger;
public class InsertControlTable {
    final static Logger logger = Logger.getLogger(InsertControlTable.class);
    private String insertSQL ="INSERT INTO DIST_CONTROL_TOTALS " +
            "(FILE_ID, " +
            "TOTAL_ROW_COUNT," +
            "TOTAL_QTY," +
            "TOTAL_SPEND," +
            "MASTER_FILE_ID" +
            " ) "  + " VALUES (?,?,?,?,?)";
    PreparedStatement insertStatement;

    public int InsertRecord(Connection p_conn,int p_fileId,Double totalRecords,Double usageTotal,Double spendTotal)
    {
        try
        {
            insertStatement=p_conn.prepareStatement(insertSQL);
            insertStatement.setInt(1,p_fileId);
            insertStatement.setDouble(2,totalRecords);
            insertStatement.setDouble(3,usageTotal);
            insertStatement.setDouble(4,spendTotal);
             insertStatement.setString(5,"M" + p_fileId);
            insertStatement.execute();
            return  0;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
        finally {
         //   insertSQL="";
         //   insertStatement=null;

        }
    }
}



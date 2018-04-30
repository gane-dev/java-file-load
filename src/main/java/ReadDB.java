
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class ReadDB {
    final static Logger logger = Logger.getLogger(JdbcOracleConnection.class);

    public static int getFileId(Connection p_conn)
    {
        Statement stmt = null;
        int result = -1;
        String sql ="SELECT LD_FILEID_SEQ.NEXTVAL FROM DUAL";
        try {
            stmt = p_conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result=  rs.getInt(1);
            }
            return  result;
        }
        catch (SQLException sx)
        {
            logger.error("Error",sx);
            return -1;

        }

    }
    public static String getSupplierId(String p_initials,Connection p_conn)
    {
        logger.info("Initials:"+p_initials);
        Statement stmt = null;
        String result = "";
        String sql ="select erm_supplier_id from avendra_supplier where supplier_id = '" +p_initials +  "'";
        try {
            stmt = p_conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                result=  rs.getString(1);
            }
            return  result;
        }
        catch (SQLException sx)
        {
            logger.error("Error",sx);
            return "";

        }

    }
}

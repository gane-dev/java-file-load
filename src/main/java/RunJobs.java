import com.sun.org.apache.bcel.internal.generic.RET;
import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RunJobs {
    Connection conn;
    final static Logger logger = Logger.getLogger(FileManagement.class);
    private Map<String,String> jobMap= null;
    public RunJobs()
    {
        conn = JdbcOracleConnection.getConnection(CommonObjects.getConnectionString());
        AddMapValues();
    }
    private void AddMapValues()
    {
        jobMap = new LinkedHashMap<String, String>();
        jobMap.put("Validation","{call Pkg_DRT.Load_Validation}");
        jobMap.put("Clean Match","{call P_Clean_Mtch('',0)}");
        jobMap.put("Usage Match","{call Matching_pkg.P_USAGE}");
        jobMap.put("Customer Matching","{call Matching_pkg.P_CUSTOMER}");
        jobMap.put("Item Matching","{call pkg_item_matching.p_main(0)}");
        jobMap.put("Spend Posting","{call Spend_posting.P_Post_Spend_All('',0)}");


    }
    public int RunUC4Jobs() {
        CallableStatement callableStatement = null;
        String jobSql ="";
        logger.info("****************************Start Scheduled Jobs******************************");
        try {
            for(String k:jobMap.keySet()) {
                jobSql = jobMap.get(k);
                logger.info("Job Start:"+k);
                callableStatement = conn.prepareCall(jobSql);
                callableStatement.executeUpdate();
                conn.commit();
                logger.info("Job End:"+k);

            }

        } catch (SQLException sqx) {
            logger.error("Error", sqx);
            return -1;
        }
        finally {
            callableStatement=null;
            try {
                if (conn != null && !conn.isClosed()) {
                    // conn.rollback();
                    conn.close();
                }
            } catch (SQLException sqx) {
                logger.error(sqx.getStackTrace());
            }
        }
        return 0;
    }
}

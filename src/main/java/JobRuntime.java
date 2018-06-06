import org.apache.log4j.Logger;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JobRuntime {
   private String runtimeQuery = "select trunc(sum(runtime),2) from ( " +
    "select (max(load_finish)-min(load_start))*60*24 runtime from load_log " +
    "where load_num in ( select load_num from load_log where step like '%3$s'"+
            " and rownum < %4$s and load_Start  > to_Date( '%1$s','MMDDYYYYHH24MI')" +
    " and load_Start < to_Date('%2$s','MMDDYYYYHH24MI'))  group by load_num)";

    private String totalQuery = "select trunc((a.load_finish-b.load_start)*60*24,2) from ( "+
            "select load_start from load_log where step = 'POP ERM CUSTOMERS' and load_Start  > to_Date('%1$s','MMDDYYYYHH24MI') "+
            " and load_Start < to_Date('%2$s','MMDDYYYYHH24MI')) b,(select load_finish from load_log where step = 'LOG TO CLP/ERM'" +
            " and load_Start  > to_Date('%1$s','MMDDYYYYHH24MI') and load_Start < to_Date('%2$s','MMDDYYYYHH24MI')) a";
    Connection conn;
    final static Logger log = Logger.getLogger(FileManagement.class);
    private String startDateTime="";
    private String endDateTime="";
    private Map<String,String> jobMap= null;
    public  JobRuntime()
    {
        conn = JdbcOracleConnection.getConnection(CommonObjects.getConnectionString());
        GetPropertyValues propertyObject = new GetPropertyValues("filecopy.properties");
        AddMapValues();
    }
    private void AddMapValues()
    {
        jobMap = new HashMap<String, String>();
        jobMap.put("Validation","%UPDATE MASTER STG1%");
        jobMap.put("Clean match","%CLEAN MTCH%");
        jobMap.put("Usage match","%MOVE USAGE%");
        //jobMap.put("Usage match1","%ANALYZE DIST_USAGE_MTCH%");
        jobMap.put("Customer Matching","%CUST EXACT MATCHING%");
        jobMap.put("Item Matching","%ITEM EXACT MATCHING%");
        jobMap.put("Spend Post","%POST SPEND ALL%");
      //  jobMap.put("Calculate Rebate","%REBATE CALCULATE%");


    }
    public  int GetJobRuntimes(String p_startDateTime,String p_endDateTime)
    {
        startDateTime = p_startDateTime;
        endDateTime = p_endDateTime;
        Double totalTime=0.0;
        Double runtime=0.0;
        Optional<String> jobTime;
        try {
            log.info("****************************Runtime*******************************");

            // GetFiles(true);
            for(String k: jobMap.keySet()) {
                jobTime = GetRuntime(k, Optional.empty());
                if (jobTime.isPresent()) {
                    runtime = Double.parseDouble(jobTime.get());
                    if (!k.equals("Validation"))
                        totalTime += runtime;
                    log.info("Job Name:" + k + " Runtime: "+ jobTime.get());
                }
                

            }
            jobTime = GetRuntime(null,Optional.of(totalQuery));
            if (jobTime.isPresent()) {
                    runtime = Double.parseDouble(jobTime.get());
                    log.info("Total Runtime: " + jobTime.get());
                    log.info("Percentage: " + (totalTime * 100) / runtime);
            }

            log.info("****************************End Runtime*******************************");
            return 0;
        }
        catch (Exception ex)
        {
            log.error("error",ex);
            return -1;
        }
        finally {


            try {
                if (conn != null && !conn.isClosed()) {
                    // conn.rollback();
                    conn.close();
                }
            } catch (SQLException sqx) {
                log.error(sqx.getStackTrace());
            }
        }
    }
    private Optional<String > GetRuntime(String key,Optional<String> query)
    {
        Statement stmt = null;
        String sql ="";
        ResultSet rs;
        String runtime="";
        try {
            if (query.isPresent())
                sql = String.format(query.get(),startDateTime,endDateTime);
            else {
                if (key.equals("Validation"))
                    sql = String.format(runtimeQuery, startDateTime, endDateTime, jobMap.get(key),100);
                else
                    sql = String.format(runtimeQuery, startDateTime, endDateTime, jobMap.get(key),2);
            }
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                runtime=  rs.getString(1);

            }
            if (runtime.equals(""))
               return Optional.empty();
                else
                     return  Optional.of(runtime);
        }
        catch (SQLException sx)
        {
            log.error("Error",sx);
            return Optional.empty();

        }
        catch (Exception ex)
        {
            log.error("Error",ex);
            return null;
        }
        finally {
            rs=null;
            stmt=null;

        }

    }

}

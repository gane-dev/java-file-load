import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;

import java.sql.Connection;

public interface InsertTable {
    final static Logger logger = Logger.getLogger(InsertTable.class);
    final String patt = "[^0-9.-]";
    final  String firstCellPattern = "[^0-9n]";
    int InsertRecord(boolean finalCommit,Row excelRow,Double totalRecords);
    int AddRow(Row excelRow,String rec);
    final static  int commitCount=1000;

}

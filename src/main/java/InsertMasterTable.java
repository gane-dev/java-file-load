import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.util.HashMap;
import java.util.Map;
public class InsertMasterTable {
    final static Logger logger = Logger.getLogger(InsertMasterTable.class);
    private Connection conn;
    Map fieldLength;
    private String insertSQL = "INSERT INTO DIST_MASTER_STG1 " +
            "( BEGIN_USAGE_DATE," +
            "END_USAGE_DATE," +
            "DIST_ID," +
            "DIST_CUST_NUM," +
            "DIST_CUST_NAME," +
            "DIST_CUST_ADDRESS," +
            "DIST_CUST_ADDRESS2," +
            "DIST_CUST_CITY," +
            "DIST_CUST_STATE," +
            "DIST_CUST_ZIP_CD," +
          //  "CUST_ZIP_CD_PLUS," +
            "DIST_PROD_NUM," +
            "DIST_GTIN," +
            "MFR_NAME," +
            "MFR_PROD_NUM," +
            "BRAND_NAME," +
            "CASE_PACK_LITERAL," +
            "CASE_PACK_QTY," +
            "UNIT_OF_ISSUE," +
            "UNIT_OF_MEASURE_DESC," +
            "DIST_PROD_DESC," +
            "TOTAL_USAGE_IN_UNITS," +
            "DIST_SIZE_INDICATOR," +
            "WEIGHT_SHIPPED," +
            "DIST_WEIGHT_SHIPPED_INDICATOR," +
            "TOTAL_DIST_SELL_DOLLARS," +
            "CURRENCY_TYPE," +
            "DIST_INVOICE_ID," +
            "DIST_INVOICE_DT," +
            "DIST_PO_ID," +
            "DIST_PO_DT," +
//            "BOX_BEEF_YN" +
//            "PRIVATE_LABEL_YN" +
//            "SOURCED_YN" +
//            "DROP_SHIP_FLAG" +
//            "VENDOR_CODE" +
//            "STD_MARKUP_FLAG" +
//            "DIVERSITY_TYPE" +
            "STATUS, " +
            "MASTER_FILE_ID" +

            " ) "
            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement insertStatement;
    int rowCount = 0;
    int fileId = -1;
    int rowLength = -1;

    public InsertMasterTable(Connection conObject, int p_fileId, int p_rowLength) {
        conn = conObject;
        fileId = p_fileId;

        rowLength = p_rowLength;
        fieldLength = CreateMap();


    }

    private Map CreateMap()

    {
        Map temp = new HashMap<Integer,Integer>();
        temp.put(1, 20);
        temp.put(2, 20);
        temp.put(3, 7);
        temp.put(4, 40);
        temp.put(5, 50);
        temp.put(6, 50);
        temp.put(7, 50);
        temp.put(8, 25);
        temp.put(9, 2);
        temp.put(10, 10);
        //temp.put(11, 10); country code ignore
        temp.put(12, 40);
        temp.put(13, 14);
        temp.put(14, 50);
        temp.put(15,40);
        temp.put(16, 50);
        temp.put(17, 30);
        temp.put(21, 50);
        temp.put(23, 3);
        temp.put(25, 3);
        temp.put(27, 15);
        temp.put(28, 30);
        temp.put(29, 20);
        temp.put(30, 20);
        temp.put(31, 20);



        return  temp;
    }
    private int AddRow(Row excelRow)
    {
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        int i=1;
       // String patt = "^\\d*\\.\\d+|\\d+\\.\\d*$";
        String patt = "[^0-9.]";

        try {
            //for (Cell cell : excelRow) {
            for(int idx =0;idx < 31;idx++)
            {
               // Cell cell = excelRow.getCell(idx);

                //String cellValue = dataFormatter.formatCellValue(excelRow.getCell(idx).getStringCellValue());
                String cellValue =dataFormatter.formatCellValue(excelRow.getCell(idx)).trim();
                    if (idx != 10) {
                        if (!cellValue.isEmpty()) {
                            if (idx == 17 || idx == 18 || idx == 19 || idx == 21 || idx == 23 || idx == 25) {
                                cellValue = cellValue.replaceAll(patt,"");
                                if (cellValue.equals(""))
                                    insertStatement.setObject(i, null, Types.DECIMAL);
                                else
                                    insertStatement.setDouble(i, Double.parseDouble(cellValue));
                            }
                            else
                                insertStatement.setString(i, cellValue.substring(0, cellValue.length() > Integer.parseInt(fieldLength.get(idx + 1).toString()) ? Integer.parseInt(fieldLength.get(idx + 1).toString()) : cellValue.length()));
                        } else {
                            if (idx == 17 || idx == 18 || idx == 19 || idx == 21 || idx == 23 || idx == 25)
                                insertStatement.setObject(i, null, Types.DECIMAL);
                            else
                                insertStatement.setObject(i, null, Types.NVARCHAR);
                        }
                        i = i + 1;
                    }


            }
            insertStatement.setString(i, "N");
            i=i+1;
            insertStatement.setString(i, "M"+ fileId);
            insertStatement.addBatch();
            return  0;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
    }
    public int InsertBatch(Row excelRow, boolean execute)
    {
        try
        {
            if (execute)
            {
                insertStatement.executeBatch();
        //        conn.commit();
                return 0;
            }
            if (this.insertStatement ==null ) {
                insertStatement=conn.prepareStatement(insertSQL);
           }
            if (AddRow(excelRow) ==0 ) {
//                if (rowCount > 100) {
//                    insertStatement.executeBatch();
//                   // conn.commit();
//                    rowCount = 0;
//                }
                return 0;
            }
            else {
                logger.error("InsertBatch:Failed loading records");
                return -1;
            }

        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
        finally {
          //  insertSQL="";

        }
    }
}

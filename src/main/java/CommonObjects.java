import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;

public class CommonObjects {
    static String archivePath="";
    static String filePath="";
    static String errorPath="";
    static  String connectionString="";
    public static String TableQuery(TableType type) {
        String qry="";
        switch (type) {
            case DIST_MASTER_STG1: {
                qry ="INSERT INTO DIST_MASTER_STG1 " +
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
                break;
            }

            case DIST_ITEM_STG1: {
                qry ="INSERT INTO DIST_ITEM_STG1 " +
                        "( BEGIN_USAGE_DATE," +
                        "END_USAGE_DATE," +
                        "DIST_ID," +

                        "DIST_PROD_NUM," +
                        "MFR_NAME," +
                        "MFR_PROD_NUM," +
                        "BRAND_NAME," +
                        "DIST_GTIN_UPC," +
                        "CASE_PACK_LITERAL," +
                        "CASE_PACK_QTY," +
                        "UNIT_OF_ISSUE," +
                        "UNIT_OF_MEASURE_DESC," +
                        "DIST_PROD_DESC," +
                        "DIST_ALL_ITEM_ID," +
                        "FILE_ID" +
                        "STATUS, " +
                        "MASTER_FILE_ID" +
                        " ) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                break;
            }
            case DIST_CUST_STG1:
            {   qry = "INSERT INTO DIST_CUST_STG1 " +
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
                    "CUST_COUNTRY_CD," +
                    "FILE_ID" +
                    "STATUS, " +
                    "MASTER_FILE_ID" +
                    " ) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                break;
            }
            case DIST_USAGE_STG1:
            {  qry= "INSERT INTO DIST_USAGE_STG1 " +
                    "( BEGIN_USAGE_DATE," +
                    "END_USAGE_DATE," +
                    "DIST_ID," +
                    "DIST_CUST_NUM," +
                    "DIST_PROD_NUM," +
                    "DIST_GTIN," +
                    "TOTAL_USAGE_IN_UNITS," +
                    "DIST_SIZE_INDICATOR," +

                    "WEIGHT_SHIPPED," +
                    "WEIGHT_SHIPPED_INDICATOR," +
                    "TOTAL_DIST_SELL_DOLLARS," +
                    "CURRENCY_TYPE," +
                    "DIST_INVOICE_ID," +
                    "DIST_INVOICE_DT," +
                    "DIST_PO_ID," +
                    "DIST_PO_DT," +
                    "FILE_ID" +
                    "STATUS, " +
                    "MASTER_FILE_ID" +
                    " ) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                break;
            }
            case LD_LOADER_LOG:
            {
                qry="INSERT INTO LD_LOADER_LOG " +
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
                break;
            }
            case DIST_CONTROL_TOTALS:
            {
                qry ="INSERT INTO DIST_CONTROL_TOTALS " +
                    "(FILE_ID, " +
                    "TOTAL_ROW_COUNT," +
                    "TOTAL_QTY," +
                    "TOTAL_SPEND," +
                    "MASTER_FILE_ID" +
                    " ) "  + " VALUES (?,?,?,?,?)";
                break;
            }
            case LD_SRC_FILE_TRACK:
            {
                qry ="INSERT INTO LD_SRC_FILE_TRACK " +
                        "(SOURCE_TYPE, " +
                        "SOURCE_ID," +
                        "ENTITY," +
                        "FILE_NAME," +
                        "FILE_TYPE" +
                        "FILE_REC_COUNT" +
                        "LOAD_REC_COUNT" +
                        "TAB_REC_COUNT" +
                        "STATUS" +
                        "TABLE_NAME" +
                        "FILE_ID" +
                        " ) "  + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                break;
            }
        }
        return  qry;
    }

    public static int[] DecimalIndex(TableType type) {

        switch (type) {
            case DIST_MASTER_STG1: {
                int[]temp = {17 , 18  , 21 , 23 , 25};
                return  temp;
            }

            case DIST_ITEM_STG1: {
                int[]temp = {9 , 10};
                return  temp;
            }
            case DIST_CUST_STG1:
            {   int[] temp = {};
                return  temp;
            }
            case DIST_USAGE_STG1:
            {
                int[] temp = {6 , 7 , 8 , 10};
                return  temp;
            }
        }
        return  null;
    }
    public static Map ReturnMap(TableType type)
    {
        Map temp = new HashMap<Integer,Integer>();
        switch (type){
            case DIST_MASTER_STG1:
            {
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
                temp.put(11, 10); //country code ignore
                temp.put(12, 40);
                temp.put(13, 14);
                temp.put(14, 50);
                temp.put(15,40);
                temp.put(16, 50);
                temp.put(17, 30);
                temp.put(18, 0);
                temp.put(19, 0);
                temp.put(20, 3);
                temp.put(21, 50);
                temp.put(22, 0);
                temp.put(23, 3);
                temp.put(24, 0);
                temp.put(25, 3);
                temp.put(26, 0);
                temp.put(27, 15);
                temp.put(28, 30);
                temp.put(29, 20);
                temp.put(30, 20);
                temp.put(31, 20);
                break;
            }
            case DIST_ITEM_STG1:
            {
                temp.put(1, 20);
                temp.put(2, 20);
                temp.put(3, 7);
                temp.put(4, 40);
                temp.put(5, 50);
                temp.put(6, 40);
                temp.put(7, 50);
                temp.put(8, 25);
                temp.put(9, 30);
                temp.put(10, 0);
                temp.put(11, 0);
                temp.put(12, 3);
                temp.put(13, 50);
                temp.put(14, 15);
                break;
            }
            case DIST_CUST_STG1:
            {
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

                temp.put(11, 3);

                break;
            }
            case DIST_USAGE_STG1:
            {
                temp.put(1, 20);
                temp.put(2, 20);
                temp.put(3, 7);
                temp.put(4, 40);
                temp.put(5, 40);
                temp.put(6, 14);
                temp.put(7, 0);
                temp.put(8, 0);
                temp.put(9, 0);
                temp.put(10, 3);
                temp.put(11, 0);
                temp.put(12, 15);
                temp.put(13, 30);
                temp.put(14, 20);
                temp.put(15,20);
                temp.put(16, 20);
                break;
            }

        }
        return temp;
    }
    public static void setArchivePath(String path)
    {
        archivePath=path;
    }
    public static String getArchivePath()
    {
        return archivePath;
    }
    public static void setFilePath(String path)
    {
        filePath=path;
    }
    public static String getFilePath()
    {
        return filePath;
    }
    public static void setErrorPath(String path)
    {
        errorPath=path;
    }
    public static String getErrorPath()
    {
       return errorPath ;
    }
    public static void setConnectionString(String path)
    {
        connectionString=path;
    }
    public static String getConnectionString()
    {
        return connectionString ;
    }

}

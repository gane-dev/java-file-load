import javax.print.DocFlavor;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CommonObjects {
    static String archivePath="";
    static String filePath="";
    static String errorPath="";
    static  String connectionString="";
    static String qry_excel_master="";
    static String qry_excel_option_master="";
    static String qry_item="";
    static String qry_usage="";
    static String qry_cust="";
    static String qry_log="";
    static String qry_control="";
    static String qry_track="";
    static Map<String,FieldType> map_excel_master = null;
    static Map<String,FieldType> map_excel_option_master = null;
    static Map<String,FieldType> map_item = null;
    static Map<String,FieldType> map_cust = null;
    static Map<String,FieldType> map_usage = null;
    static String qry_opt_usage ="";
    static Map<String,FieldType> map_option_usage = null;
    public static Map<String,FieldType> FieldMappings(TableType type, FileType fileType) {
        switch (type) {

            case DIST_MASTER_STG1: {
                if (fileType == FileType.EXCEL) {
                    if (map_excel_master == null) {
                        map_excel_master =  new HashMap<String, FieldType>();

                        map_excel_master.put("A", new FieldType(1, "Begin Usage Date", "date", 20));
                        map_excel_master.put("B", new FieldType(2, "End Usage Date", "date", 20));
                        map_excel_master.put("C", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                        map_excel_master.put("D", new FieldType(4, "Customer Number", "char", 40));
                        map_excel_master.put("E", new FieldType(5, "Name", "char", 50));
                        map_excel_master.put("F", new FieldType(6, "Address", "char", 50));

                        map_excel_master.put("G", new FieldType(7, "Address2", "char", 50));
                        map_excel_master.put("H", new FieldType(8, "City", "char", 25));
                        map_excel_master.put("I", new FieldType(9, "State", "char", 2));

                        map_excel_master.put("J", new FieldType(10, "Zip", "char", 10));
                        map_excel_master.put("K", new FieldType(11, "CUST_COUNTRY_CD", "char", 3));
                        map_excel_master.put("L", new FieldType(12, "Distributor Product Number", "char", 40));

                        map_excel_master.put("M", new FieldType(13, "GTIN/UPC", "char", 14));
                        map_excel_master.put("N", new FieldType(14, "Manufacturer Name", "char", 50));
                        map_excel_master.put("O", new FieldType(15, "Man Product Num", "char", 40));

                        map_excel_master.put("P", new FieldType(16, "Brand Name", "char", 50));
                        map_excel_master.put("Q", new FieldType(17, "Unit of Measure Literal", "char", 30));
                        map_excel_master.put("R", new FieldType(18, "Case Pack Qty", "decimal", 0));

                        map_excel_master.put("S", new FieldType(19, "Unit of Issue", "decimal", 0));
                        map_excel_master.put("T", new FieldType(20, "Unit of Measure Code", "char", 3));
                        map_excel_master.put("U", new FieldType(21, "Product Description", "char", 50));

                        map_excel_master.put("V", new FieldType(22, "Qty", "decimal", 0));
                        map_excel_master.put("W", new FieldType(23, "Qty  Indicator", "char", 3));
                        map_excel_master.put("X", new FieldType(24, "Weight Shipped", "decimal", 0));

                        map_excel_master.put("Y", new FieldType(25, "Weight Shipped Indicator", "char", 3));
                        map_excel_master.put("Z", new FieldType(26, "Total Sell Dollars", "decimal", 0));
                        map_excel_master.put("AA", new FieldType(27, "Currency Code", "char", 15));

                        map_excel_master.put("AB", new FieldType(28, "Invoice Num", "char", 30));

                        map_excel_master.put("AC", new FieldType(29, "Invoice Date", "date", 20));
                        map_excel_master.put("AD", new FieldType(30, "PO Num", "char", 20));
                        map_excel_master.put("AE", new FieldType(31, "PO Date", "date", 20));

                    }
                      return map_excel_master;

                } else {
                    if (map_excel_option_master == null) {
                        map_excel_option_master =  new HashMap<String, FieldType>();
                        map_excel_option_master.put("A", new FieldType(1, "Begin Usage Date", "date", 20));
                        map_excel_option_master.put("B", new FieldType(2, "End Usage Date", "date", 20));
                        map_excel_option_master.put("C", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                        map_excel_option_master.put("D", new FieldType(4, "Customer Number", "char", 40));
                        map_excel_option_master.put("E", new FieldType(5, "Name", "char", 50));
                        map_excel_option_master.put("F", new FieldType(6, "Address", "char", 50));

                        map_excel_option_master.put("G", new FieldType(7, "Address2", "char", 50));
                        map_excel_option_master.put("H", new FieldType(8, "City", "char", 25));
                        map_excel_option_master.put("I", new FieldType(9, "State", "char", 2));

                        map_excel_option_master.put("J", new FieldType(10, "Zip", "char", 10));
                        map_excel_option_master.put("K", new FieldType(11, "Country Code", "char", 10));
                        map_excel_option_master.put("L", new FieldType(12, "Distributor Product Number", "char", 40));

                        //map_excel_option_master.put("M", new FieldType(13, "GTIN/UPC", "char", 14));
                        map_excel_option_master.put("N", new FieldType(13, "Manufacturer Name", "char", 50));
                        map_excel_option_master.put("O", new FieldType(14, "Man Product Num", "char", 40));

                        //map_excel_option_master.put("P1", new FieldType(16, "Brand Name", "char", 50));
                        map_excel_option_master.put("Q", new FieldType(15, "Unit of Measure Literal", "char", 30));
                        //map_excel_option_master.put("R1", new FieldType(18, "Case Pack Qty", "decimal", 0));

                        //map_excel_option_master.put("S1", new FieldType(19, "Unit of Issue", "decimal", 0));
                        //map_excel_option_master.put("T1", new FieldType(20, "Unit of Measure Code", "char", 3));
                        map_excel_option_master.put("U", new FieldType(16, "Product Description", "char", 50));

                        map_excel_option_master.put("V", new FieldType(17, "Qty", "decimal", 0));
                        map_excel_option_master.put("W", new FieldType(18, "Qty  Indicator", "char", 3));
                        //map_excel_option_master.put("X1", new FieldType(24, "Weight Shipped", "decimal", 0));

                        //map_excel_option_master.put("Y1", new FieldType(25, "Weight Shipped Indicator", "char", 3));
                        map_excel_option_master.put("Z", new FieldType(19, "Total Sell Dollars", "decimal", 0));
                        map_excel_option_master.put("AA", new FieldType(20, "Currency Code", "char", 15));

                        map_excel_option_master.put("AB", new FieldType(21, "Invoice Num", "char", 30));

                        map_excel_option_master.put("AC", new FieldType(22, "Invoice Date", "date", 20));
                        map_excel_option_master.put("AD", new FieldType(23, "PO Num", "char", 20));
                        map_excel_option_master.put("AE", new FieldType(24, "PO Date", "date", 20));

                    }
                        return map_excel_option_master;
                }
            }
            case DIST_ITEM_STG1: {
                if (map_item == null) {
                    map_item =  new HashMap<String, FieldType>();
                    map_item.put("1", new FieldType(1, "Begin Usage Date", "char", 20));
                    map_item.put("2", new FieldType(2, "End Usage Date", "char", 20));
                    map_item.put("3", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                    map_item.put("4", new FieldType(4, "Customer Number", "char", 40));
                    map_item.put("5", new FieldType(5, "Name", "char", 50));
                    map_item.put("6", new FieldType(6, "Address", "char", 50));

                    map_item.put("7", new FieldType(7, "Address2", "char", 50));
                    map_item.put("8", new FieldType(8, "City", "char", 25));
                    map_item.put("9", new FieldType(9, "State", "char", 30));

                    map_item.put("10", new FieldType(10, "Zip", "decimal", 0));
                    map_item.put("11", new FieldType(11, "Country Code", "decimal", 0));
                    map_item.put("12", new FieldType(12, "Distributor Product Number", "char", 3));

                    map_item.put("13", new FieldType(13, "GTIN/UPC", "char", 50));
                    map_item.put("14", new FieldType(14, "Manufacturer Name", "char", 15));

                }
                return map_item;
            }

            case DIST_CUST_STG1: {
                if (map_cust == null) {
                    map_cust =  new HashMap<String, FieldType>();
                    map_cust.put("1", new FieldType(1, "Begin Usage Date", "char", 20));
                    map_cust.put("2", new FieldType(2, "End Usage Date", "char", 20));
                    map_cust.put("3", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                    map_cust.put("4", new FieldType(4, "Customer Number", "char", 40));
                    map_cust.put("5", new FieldType(5, "Name", "char", 50));
                    map_cust.put("6", new FieldType(6, "Address", "char", 50));

                    map_cust.put("7", new FieldType(7, "Address2", "char", 50));
                    map_cust.put("8", new FieldType(8, "City", "char", 25));
                    map_cust.put("9", new FieldType(9, "State", "char", 2));

                    map_cust.put("10", new FieldType(10, "Zip", "char", 10));
                    map_cust.put("11", new FieldType(11, "Country Code", "char", 3));


                }
                  return map_cust;
            }

            case DIST_USAGE_STG1:

            {
                if (fileType == FileType.TEXT_TAB_USAGE || fileType == FileType.TEXT_BANG_USAGE
                        || fileType == FileType.TEXT_PIPE_USAGE || fileType == FileType.TEXT_CAT_USAGE || fileType == FileType.TEXT_SYS_USAGE) {
                    if (map_usage == null) {
                        map_usage =  new HashMap<String, FieldType>();
                        map_usage.put("1", new FieldType(1, "Begin Usage Date", "char", 20));
                        map_usage.put("2", new FieldType(2, "End Usage Date", "char", 20));
                        map_usage.put("3", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                        map_usage.put("4", new FieldType(4, "Customer Number", "char", 40));
                        map_usage.put("5", new FieldType(5, "Name", "char", 40));
                        map_usage.put("6", new FieldType(6, "Address", "char", 14));

                        map_usage.put("7", new FieldType(7, "Address2", "decimal", 0));
                        map_usage.put("8", new FieldType(8, "City", "char", 3));
                        map_usage.put("9", new FieldType(9, "State", "decimal", 0));

                        map_usage.put("10", new FieldType(10, "Zip", "char", 3));
                        map_usage.put("11", new FieldType(11, "Country Code", "decimal", 0));
                        map_usage.put("12", new FieldType(12, "Distributor Product Number", "char", 15));

                        map_usage.put("13", new FieldType(13, "GTIN/UPC", "char", 30));
                        map_usage.put("14", new FieldType(14, "Manufacturer Name", "char", 20));
                        map_usage.put("15", new FieldType(15, "Man Product Num", "char", 20));

                        map_usage.put("16", new FieldType(16, "Brand Name", "char", 20));

                    }
                        return map_usage;
                } else if (fileType == FileType.TEXT_OPTION_USAGE) {
                    if (map_option_usage == null) {
                        map_option_usage =  new HashMap<String, FieldType>();
                        map_option_usage.put("1", new FieldType(1, "Begin Usage Date", "char", 20));
                        map_option_usage.put("2", new FieldType(2, "End Usage Date", "char", 20));
                        map_option_usage.put("3", new FieldType(3, "Distributor Warehouse ID", "char", 7));

                        map_option_usage.put("4", new FieldType(4, "Customer Number", "char", 40));
                        map_option_usage.put("5", new FieldType(5, "Name", "char", 40));
                        //map_option_usage.put("6", new FieldType(6, "Address", "char", 14));

                        map_option_usage.put("7", new FieldType(6, "Address2", "decimal", 0));
                        map_option_usage.put("8", new FieldType(7, "City", "char", 3));
                        //map_option_usage.put("9", new FieldType(9, "State", "decimal", 0));

                        //map_option_usage.put("10", new FieldType(10, "Zip", "char", 3));
                        map_option_usage.put("11", new FieldType(8, "Country Code", "decimal", 0));
                        map_option_usage.put("12", new FieldType(9, "Distributor Product Number", "char", 15));

                        map_option_usage.put("13", new FieldType(10, "GTIN/UPC", "char", 30));
                        map_option_usage.put("14", new FieldType(11, "Manufacturer Name", "char", 20));
                        map_option_usage.put("15", new FieldType(12, "Man Product Num", "char", 20));

                        map_option_usage.put("16", new FieldType(13, "Brand Name", "char", 20));

                    }
                        return map_option_usage;
                }
            }
        }
        return null;
    }
    public static String TableQuery(TableType type, FileType fileType) {

        switch (type) {

            case DIST_MASTER_STG1: {
                if ( fileType == FileType.EXCEL) {
                    if (qry_excel_master.equals(""))
                        qry_excel_master = "INSERT INTO DIST_MASTER_STG1 " +
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

                                "STATUS, " +
                                "MASTER_FILE_ID" +

                                " ) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    return qry_excel_master;
                }
                else {
                    if (qry_excel_option_master.equals(""))
                        qry_excel_option_master = "INSERT INTO DIST_MASTER_STG1 " +
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
                                //"DIST_GTIN," +
                                "MFR_NAME," +
                                "MFR_PROD_NUM," +
                                //"BRAND_NAME," +
                                "CASE_PACK_LITERAL," +
                                "CASE_PACK_QTY," +
                                //"UNIT_OF_ISSUE," +
                                //"UNIT_OF_MEASURE_DESC," +
                                "DIST_PROD_DESC," +
                                "TOTAL_USAGE_IN_UNITS," +
                                "DIST_SIZE_INDICATOR," +
                                //"WEIGHT_SHIPPED," +
                                //"DIST_WEIGHT_SHIPPED_INDICATOR," +
                                "TOTAL_DIST_SELL_DOLLARS," +
                                "CURRENCY_TYPE," +
                                "DIST_INVOICE_ID," +
                                "DIST_INVOICE_DT," +
                                "DIST_PO_ID," +
                                "DIST_PO_DT," +

                                "STATUS, " +
                                "MASTER_FILE_ID" +

                                " ) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    return qry_excel_option_master;
                }
            }

            case DIST_ITEM_STG1: {
                if (qry_item.equals(""))
                    qry_item = "INSERT INTO DIST_ITEM_STG1 " +
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
                            "DIST_ALT_ITEM_ID," +
                            "FILE_ID," +
                            "STATUS, " +
                            "MASTER_FILE_ID" +
                            " ) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    return qry_item;
                }

            case DIST_CUST_STG1:

            {   if (qry_cust.equals(""))
                qry_cust = "INSERT INTO DIST_CUST_STG1 " +
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

                    "FILE_ID," +
                    "STATUS, " +
                    "MASTER_FILE_ID" +
                    " ) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            return  qry_cust;
            }
            case DIST_USAGE_STG1:

            {
                if (fileType == FileType.TEXT_TAB_USAGE || fileType == FileType.TEXT_BANG_USAGE
                        || fileType == FileType.TEXT_PIPE_USAGE  || fileType == FileType.TEXT_CAT_USAGE   || fileType == FileType.TEXT_SYS_USAGE) {
                    if (qry_usage.equals(""))
                        qry_usage = "INSERT INTO DIST_USAGE_STG1 " +
                                "( BEGIN_USAGE_DATE," +
                                "END_USAGE_DATE," +
                                "DIST_ID," +
                                "DIST_CUST_NUM," +
                                "DIST_PROD_NUM," +
                                "DIST_GTIN," +
                                "TOTAL_USAGE_IN_UNITS," +
                                "DIST_SIZE_INDICATOR," +

                                "WEIGHT_SHIPPED," +
                                "WEIGHT_SHIPPED_INDICATOR, " +
                                "DIST_WEIGHT_SHIPPED_INDICATOR, " +
                                "TOTAL_DIST_SELL_DOLLARS," +
                                "CURRENCY_TYPE," +
                                "DIST_INVOICE_ID," +
                                "DIST_INVOICE_DT," +
                                "DIST_PO_ID," +
                                "DIST_PO_DT," +
                                "FILE_ID," +
                                "STATUS, " +
                                "MASTER_FILE_ID" +
                                " ) "
                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    return qry_usage;
                }
                else if (fileType == FileType.TEXT_OPTION_USAGE)
            {
                if (qry_opt_usage.equals(""))
                    qry_opt_usage = "INSERT INTO DIST_USAGE_STG1 " +
                            "( BEGIN_USAGE_DATE," +
                            "END_USAGE_DATE," +
                            "DIST_ID," +
                            "DIST_CUST_NUM," +
                            "DIST_PROD_NUM," +
                         //   "DIST_GTIN," +
                            "TOTAL_USAGE_IN_UNITS," +
                            "DIST_SIZE_INDICATOR," +

                           // "WEIGHT_SHIPPED," +
                         //   "WEIGHT_SHIPPED_INDICATOR, " +
                         //   "DIST_WEIGHT_SHIPPED_INDICATOR, " +
                            "TOTAL_DIST_SELL_DOLLARS," +
                            "CURRENCY_TYPE," +
                            "DIST_INVOICE_ID," +
                            "DIST_INVOICE_DT," +
                            "DIST_PO_ID," +
                            "DIST_PO_DT," +
                            "FILE_ID," +
                            "STATUS, " +
                            "MASTER_FILE_ID" +
                            " ) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                return qry_opt_usage;
            }
//                else if (fileType == FileType.TEXT_CAT_USAGE)
//                {
//                    if (qry_cat_usage.equals(""))
//                        qry_cat_usage = "INSERT INTO DIST_USAGE_STG1 " +
//                                "( BEGIN_USAGE_DATE," +
//                                "END_USAGE_DATE," +
//                                "DIST_ID," +
//                                "DIST_CUST_NUM," +
//                                "DIST_PROD_NUM," +
//                                "DIST_GTIN," +
//                                "TOTAL_USAGE_IN_UNITS," +
//                                "DIST_SIZE_INDICATOR," +
//
//                                "WEIGHT_SHIPPED," +
//                                "WEIGHT_SHIPPED_INDICATOR, " +
//                                "DIST_WEIGHT_SHIPPED_INDICATOR, " +
//                                "TOTAL_DIST_SELL_DOLLARS," +
//                                "CURRENCY_TYPE," +
//                                "DIST_INVOICE_ID," +
//                                "DIST_INVOICE_DT," +
//                                "DIST_PO_ID," +
//                                "DIST_PO_DT," +
//                                "FILE_ID," +
//                                "STATUS, " +
//                                "MASTER_FILE_ID" +
//                                " ) "
//                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                    return qry_cat_usage;
//                }
//                else if (fileType == FileType.TEXT_OPTION_USAGE)
//                {
//                    if (qry_opt_usage.equals(""))
//                        qry_opt_usage = "INSERT INTO DIST_USAGE_STG1 " +
//                                "( BEGIN_USAGE_DATE," +
//                                "END_USAGE_DATE," +
//                                "DIST_ID," +
//                                "DIST_CUST_NUM," +
//                                "DIST_PROD_NUM," +
//                                "DIST_GTIN," +
//                                "TOTAL_USAGE_IN_UNITS," +
//                                "DIST_SIZE_INDICATOR," +
//
//                                "WEIGHT_SHIPPED," +
//                                "WEIGHT_SHIPPED_INDICATOR, " +
//                                "DIST_WEIGHT_SHIPPED_INDICATOR, " +
//                                "TOTAL_DIST_SELL_DOLLARS," +
//                                "CURRENCY_TYPE," +
//                                "DIST_INVOICE_ID," +
//                                "DIST_INVOICE_DT," +
//                                "DIST_PO_ID," +
//                                "DIST_PO_DT," +
//                                "FILE_ID," +
//                                "STATUS, " +
//                                "MASTER_FILE_ID" +
//                                " ) "
//                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                    return qry_opt_usage;
//                }
//                else if (fileType == FileType.TEXT_SYS_USAGE)
//                {
//                    if (qry_sys_usage.equals(""))
//                        qry_sys_usage = "INSERT INTO DIST_USAGE_STG1 " +
//                                "( BEGIN_USAGE_DATE," +
//                                "END_USAGE_DATE," +
//                                "DIST_ID," +
//                                "DIST_CUST_NUM," +
//                                "DIST_PROD_NUM," +
//                                "DIST_GTIN," +
//                                "TOTAL_USAGE_IN_UNITS," +
//                                "DIST_SIZE_INDICATOR," +
//
//                                "WEIGHT_SHIPPED," +
//                                "WEIGHT_SHIPPED_INDICATOR, " +
//                                "DIST_WEIGHT_SHIPPED_INDICATOR, " +
//                                "TOTAL_DIST_SELL_DOLLARS," +
//                                "CURRENCY_TYPE," +
//                                "DIST_INVOICE_ID," +
//                                "DIST_INVOICE_DT," +
//                                "DIST_PO_ID," +
//                                "DIST_PO_DT," +
//                                "FILE_ID," +
//                                "STATUS, " +
//                                "MASTER_FILE_ID" +
//                                " ) "
//                                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//                    return qry_sys_usage;
//                }
            }
            case LD_LOADER_LOG:
            {
                if(qry_log.equals(""))
                qry_log="INSERT INTO LD_LOADER_LOG " +
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
                return qry_log;
            }
            case DIST_CONTROL_TOTALS:
            {
                    if (qry_control.equals(""))
                qry_control ="INSERT INTO DIST_CONTROL_TOTALS " +
                    "(FILE_ID, " +
                    "TOTAL_ROW_COUNT," +
                    "TOTAL_QTY," +
                    "TOTAL_SPEND," +
                    "MASTER_FILE_ID" +
                    " ) "  + " VALUES (?,?,?,?,?)";
                    return  qry_control;
            }
            case LD_SRC_FILE_TRACK:
            {
                if (qry_track.equals(""))
                qry_track ="INSERT INTO LD_SRC_FILE_TRACK " +
                        "(SOURCE_TYPE, " +
                        "SOURCE_ID," +
                        "ENTITY," +
                        "FILE_NAME," +
                        "FILE_TYPE," +
                        "FILE_REC_COUNT," +
                        "LOAD_REC_COUNT," +
                        "TAB_REC_COUNT," +
                        "STATUS," +
                        "TABLE_NAME," +
                        "FILE_ID" +
                        " ) "  + " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                return  qry_track;
            }
        }
        return "";
    }

    public static int[] DecimalIndex(TableType type, FileType fileType) {

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
                int[] temp = {6 ,  8 , 10};
                return  temp;
            }
        }
        return  null;
    }
    public static Map ReturnMap(TableType type, FileType fileType)
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
                temp.put(8, 3);
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
    static Map<String,Integer> dataTypes=null;
    public static Map<String,Integer> GetDataType()
    {
        if (dataTypes ==null)
        {
            dataTypes= new HashMap<String, Integer>();
            dataTypes.put("decimal",Types.DECIMAL);
            dataTypes.put("char",Types.NVARCHAR);
            dataTypes.put("date",Types.NVARCHAR);
            return  dataTypes;
        }
            return  dataTypes;
    }
}

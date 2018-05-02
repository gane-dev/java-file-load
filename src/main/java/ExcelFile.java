import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
//import java.text.DecimalFormat;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;


public class ExcelFile {
    final static Logger logger = Logger.getLogger(ExcelFile.class);
    private File file;
    private int fileId = -1;
    private Connection conn = null;
    private String fileName = "";
    private String distId = "";
    private String archivePath = "";

    public ExcelFile(Connection p_conn, File p_file, int p_fileId, String p_fileName, String p_distId, String p_archivePath) {
        conn = p_conn;
        file = p_file;
        fileId = p_fileId;
        fileName = p_fileName;
        distId = p_distId;
        archivePath = p_archivePath;
    }
    private Double processControlTotals(Cell cell)
    {
        DataFormatter dataFormatter = new DataFormatter();
        String patt = "[^0-9.-]";
        String cellValue;
        Double result = -1.0 ;
            if (cell !=null)
            {
                     cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue != "") {
                    cellValue = cellValue.trim().replaceAll(patt,"");
                    if (cellValue != "")
                        result = Double.parseDouble(cellValue);
                }
            }
            return  result;
    }
    public int LoadFile() {

        DataFormatter dataFormatter = new DataFormatter();
        int lastRowNum = -1;
        int rowIndex = 1;
        int cacheSize=1;
        logger.info("Open excel");
        Workbook workbook =null;
        //SXSSFWorkbook workbook=null;
        Sheet sheet=null;
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        try {
            workbook = WorkbookFactory.create(file);
           //  workbook= new SXSSFWorkbook(file,100);
            sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int masterResult=-2;
            logger.info("totalRows:" + totalRows);
            Row rec;
            if (sheet.getPhysicalNumberOfRows() > 1) {
                lastRowNum = sheet.getLastRowNum();
                Row header = sheet.getRow(0);
                int n = header.getLastCellNum();
                InsertMasterTable InsertRecords = new InsertMasterTable(conn, fileId, n);
//                if (n < 38) {
//                    //skip file - not enough cells
//                    logger.error("Not enough columns:" + fileName);
//                    return -1;
//                }
                while (rowIndex < lastRowNum) {
                   rec= sheet.getRow(rowIndex);

                    if (InsertRecords.InsertBatch(rec, false) != 0) {
                        //error - loading records
                        logger.error("LoadFile:Error loading records:" + fileName);
                        return -1;
                    }
                    rowIndex++;
                    cacheSize++;
                    if (cacheSize > 100 ) {
                        masterResult = InsertRecords.InsertBatch(null, true);
                        cacheSize =1;
                    }
                    if (masterResult == -1)
                        return -1;
                }
                masterResult = InsertRecords.InsertBatch(null, true);
                if (masterResult == 0) {
                    //after completing
                    //insert into ld log table
                    InsertLogTable logTable = new InsertLogTable();
                    int logResult = logTable.InsertRecord(fileName, conn, sheet.getPhysicalNumberOfRows(), distId, fileId, archivePath);
                    if (logResult == 0) {
                        conn.commit();
                        //insert into dist control table
                        rec = sheet.getRow(lastRowNum);
                        String patt = "[^0-9.-]";

                        if (dataFormatter.formatCellValue(rec.getCell(0)).contains("ZZZ")) {
                           Double totalCount = processControlTotals(rec.getCell(1));
                            Double usageCount = processControlTotals(rec.getCell(2));
                            Double spendCount = processControlTotals(rec.getCell(3));
                            if (totalCount != -1.0 && usageCount != -1.0 && spendCount !=-1.0) {
                                InsertControlTable insertCntrl = new InsertControlTable();
                                int result = insertCntrl.InsertRecord(conn, fileId, totalCount, usageCount, spendCount);
                                if (result == 0) {
                                    System.out.println("Log=> " + "File load completed:" + fileName);
                                    logger.info("File load completed:" + fileName);
                                    conn.commit();
                                } else {
                                    System.out.println("Log=> " + "File load failed:" + fileName);
                                    logger.error("File load failed:" + fileName);
                                    conn.rollback();
                                }
                            }
                            else
                                return  -1;
                        }
                    } else

                    {
                        conn.rollback();
                        System.out.println("Log=> " + "Error updating loader log table:" + fileName);
                        logger.error("Error updating loader log table:" + fileName);
                        return -1;
                    }

                } else {

                    //skip file - empty
                    logger.info("File empty :" + fileName);
                    return 1;
                }

                return 0;
            }
        }catch (IOException ex) {

            logger.error("Error",ex);
            return -1;
        } catch (InvalidFormatException ifx) {
            logger.error("Error",ifx);
            return -1;
        } catch (SQLException sqex) {
            logger.error("Error",sqex);
            return -1;
        }
        finally {

            sheet=null;
            if (workbook != null)

            {
                try{
                workbook.close();

            workbook = null;}
                catch (IOException ex) {

                    logger.error("Error",ex);
                    return -1;
                }
            }}
        return 0;
    }
}


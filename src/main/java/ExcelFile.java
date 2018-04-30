import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;


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

    public int LoadFile() {

        DataFormatter dataFormatter = new DataFormatter();
        int lastRowNum = -1;
        int rowIndex = 1;
        logger.info("Open excel");
        Workbook workbook =null;
        Sheet sheet=null;
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        try {
            workbook = WorkbookFactory.create(file);
             sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            logger.info("totalRows:" + totalRows);

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
                while (rowIndex < totalRows) {
                    Row rec = sheet.getRow(rowIndex);
                    if (InsertRecords.InsertBatch(rec, false) != 0) {
                        //error - loading records
                        logger.error("LoadFile:Error loading records:" + fileName);
                        return -1;
                    }
                    rowIndex++;
                }
                int masterResult = InsertRecords.InsertBatch(null, true);
                if (masterResult == 0) {
                    //after completing
                    //insert into ld log table
                    InsertLogTable logTable = new InsertLogTable();
                    int logResult = logTable.InsertRecord(fileName, conn, sheet.getPhysicalNumberOfRows(), distId, fileId, archivePath);
                    if (logResult == 0) {
                        conn.commit();
                        //insert into dist control table
                        Row rec = sheet.getRow(lastRowNum);
                        ;
                        if (dataFormatter.formatCellValue(rec.getCell(0)).contains("ZZZ")) {
                            Double totalCount = Double.parseDouble(dataFormatter.formatCellValue(rec.getCell(1)));
                            Double usageCount = Double.parseDouble(dataFormatter.formatCellValue(rec.getCell(2)));
                            Double spendCount = Double.parseDouble(dataFormatter.formatCellValue(rec.getCell(3)));
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


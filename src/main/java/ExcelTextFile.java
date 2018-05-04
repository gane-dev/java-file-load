import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
//import java.text.DecimalFormat;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.omg.PortableInterceptor.LOCATION_FORWARD;


public class ExcelTextFile {
    final static Logger logger = Logger.getLogger(ExcelTextFile.class);
    private File file;
    boolean excel=true;

    public ExcelTextFile(File p_file,boolean p_excel) {

        file = p_file;
        excel =p_excel;
    }

    public int LoadFile() {


        if (excel)
            return processExcelFile();
        else
            return processTextFile();
    }
    private int processTextFile() {
        int rowIndex = 1;
        int cacheSize = 1;
        logger.info("Open Text");

        int masterResult = -2;
        Scanner sc = null;
        InsertTableBase textInsert =null;
        if (file.getName().toUpperCase().indexOf("_U") > 0) {

            textInsert = new InsertTableBase(TableType.DIST_USAGE_STG1,file.getName());
        } else if (file.getName().toUpperCase().indexOf("_C") > 0) {

            textInsert = new InsertTableBase(TableType.DIST_CUST_STG1,file.getName());
        } else if (file.getName().toUpperCase().indexOf("_I") > 0) {
            textInsert = new InsertTableBase(TableType.DIST_ITEM_STG1,file.getName());
        }
        try {
            sc = new Scanner(file);
            // Check if there is another line of input
            while (sc.hasNextLine()) {
               String str = sc.nextLine();
               textInsert.AddRow(null,str);
                rowIndex++;
            }
            masterResult = textInsert.InsertRecord(true,null, rowIndex+0.0);
            if (masterResult != 0)
            {
                System.out.println("Log=> " + "Error updating loader log table:" + file.getName());
                logger.error("Error updating loader log table:" + file.getName());
                return -1;
            }

        else {

            //skip file - empty
            logger.info("File empty :" + file.getName());
            return 1;
        }

        } catch (FileNotFoundException e) {
            logger.error("Error",e);
        }
        catch (Exception e) {
            logger.error("Error",e);
        }
        return 0;
    }
    private  int processExcelFile()
    {

        int lastRowNum = -1;
        int rowIndex = 1;

        logger.info("Open excel");
        Workbook workbook =null;
        InsertTableBase addRecords=null;
        Sheet sheet=null;
        // Creating a Workbook from an Excel file (.xls or .xlsx)
        try {
            workbook = WorkbookFactory.create(file);

            sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();
            int masterResult=-2;
            logger.info("totalRows:" + totalRows);
            Row rec;
            if (sheet.getPhysicalNumberOfRows() > 1) {
                lastRowNum = sheet.getLastRowNum();
                Row header = sheet.getRow(0);
                int n = header.getLastCellNum();
                addRecords = new InsertTableBase(TableType.DIST_MASTER_STG1,file.getName());
//
                while (rowIndex < lastRowNum) {
                   rec= sheet.getRow(rowIndex);
                    int result  =addRecords.AddRow(rec,null);
                    if ( result == 0) {
                        rowIndex++;
                    }
                   else if (result ==-2)
                       //commit error
                       return  result;
                       //else add row error, skipping row
                }
                //final batch commit
                rec = sheet.getRow(lastRowNum);
                masterResult = addRecords.InsertRecord(true,rec, rowIndex+0.0);
                if (masterResult != 0)
                {
                        System.out.println("Log=> " + "Error updating loader log table:" + file.getName());
                        logger.error("Error updating loader log table:" + file.getName());
                        return -1;
                    }

                } else {

                    //skip file - empty
                    logger.info("File empty :" + file.getName());
                    return 1;
                }

                return 0;

        }catch (IOException ex) {

            logger.error("Error",ex);
            return -1;
        } catch (InvalidFormatException ifx) {
            logger.error("Error",ifx);
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

    }
}


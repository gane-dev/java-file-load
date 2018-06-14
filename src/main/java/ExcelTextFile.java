
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
//import java.text.DecimalFormat;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

public class ExcelTextFile {
    final static Logger logger = Logger.getLogger(ExcelTextFile.class);
    private File file;
    FileType fileType;
    InsertTableBase insertTable =null;
    private String delimit="[|]";
    public ExcelTextFile(File p_file,FileType p_fileType) {

        file = p_file;
        fileType =p_fileType;
        if (fileType == FileType.EXCEL || fileType == FileType.EXCEL_OPTION || fileType == FileType.EXCEL_SHORT) {
            insertTable = new InsertTableBase(TableType.DIST_MASTER_STG1, file.getName(), fileType);
        }
        else {
            if (file.getName().toUpperCase().indexOf("_U") > 0) {

                insertTable = new InsertTableBase(TableType.DIST_USAGE_STG1, file.getName(), fileType);
            } else if (file.getName().toUpperCase().indexOf("_C") > 0) {

                insertTable = new InsertTableBase(TableType.DIST_CUST_STG1, file.getName(), fileType);
            } else if (file.getName().toUpperCase().indexOf("_I") > 0) {
                insertTable = new InsertTableBase(TableType.DIST_ITEM_STG1, file.getName(), fileType);
            }
            if (fileType == FileType.TEXT_BANG_USAGE)
                delimit = "[!]";
            else if (fileType == FileType.TEXT_TAB_USAGE)
                delimit = "[\t]";
        }
    }

    public int LoadFile() {


        if (fileType == FileType.EXCEL || fileType == FileType.EXCEL_OPTION || fileType == FileType.EXCEL_SHORT)
            //return processExcelFile();
            return processExcelAsXml();
        //else if(fileType == FileType.EXCEL_OPTION)
          //  return processExcelAsXml();
        else
            return processTextFiles();
    }
    private int processExcelAsXml()
    {
        OPCPackage pkg=null;
        try {
            pkg = OPCPackage.open(file);
            XSSFReader r = new XSSFReader(pkg);
            SharedStringsTable sst = r.getSharedStringsTable();

            XMLReader parser = fetchSheetParser(sst,insertTable);


            // To look up the Sheet Name / Sheet Order / rID,
            //  you need to process the core Workbook stream.
            // Normally it's of the form rId# or rSheet# InputStream sheet2 = r.getSheet("rId2");
           // InputSource sheetSource = new InputSource(sheet2);
            Iterator<InputStream> sheets = r.getSheetsData();
            if(sheets.hasNext()) {
                InputStream sheet = sheets.next();
                InputSource sheetSource = new InputSource(sheet);
                parser.parse(sheetSource);
                sheet.close();

               int masterResult = insertTable.InsertRecord(true,null, null,0.0);
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
        }
        catch (SAXException sax)
        {
            logger.error("Error",sax);
            return  -1;
        }
        catch (OpenXML4JException opx)
        {
            logger.error("Error",opx);
            return  -1;
        }
        catch (IOException iox)
        {
            logger.error("Error",iox);
            return  -1;
        }
        finally {
            try{pkg.close();}catch (IOException iox) {logger.error("Error",iox);};
        }
    }
    private XMLReader fetchSheetParser(SharedStringsTable sst,InsertTableBase insertTable)  throws SAXException {
        XMLReader parser =
                XMLReaderFactory.createXMLReader(
                        "org.apache.xerces.parsers.SAXParser"
                );
        ContentHandler handler = new SheetHandler(sst,insertTable);
        parser.setContentHandler(handler);
        return parser;
    }
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private Map<String,String> row=new HashMap<String,String>();
        InsertTableBase insertTable =null;
        private SheetHandler(SharedStringsTable sst, InsertTableBase p_insertTable ) {
            this.sst = sst;
            this.insertTable = p_insertTable;
        }
        private String key="";
        private boolean controlRecord =false;
        private  int rowNum=1;
        private int cellNum=1;
        private int AddCellToRow(String p_key, String p_val) {
            int result = 0;
            try {


                if (!p_val.equals("")) {
                    row.put(key, p_val);
                    key = "";
                    //  val="";
                    if (p_val.toUpperCase().equals("ZZZZZ") || controlRecord) {
                        controlRecord = true;
                        insertTable.AddControlRec(p_val);
                    }

                } else {
                    //key =p_key;
                    int rwNum = new Scanner(p_key).useDelimiter("\\D+").nextInt();
                    key = p_key.substring(0, p_key.indexOf(String.valueOf(rwNum)));
                    //  if (!(key.substring(1).equals(String.valueOf(rowNum))))
                    if ((!(rwNum == rowNum)) && !controlRecord)

                    {
                        //next row
                        rowNum++;
                        result = insertTable.AddRow(row);
                        if (result == -1)
                            return -1;
                        insertTable.rowCount++;
                        row = new HashMap<String, String>();
                    }
                }

            } catch (Exception ex) {
                return  -1;
            }
            return  0;
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
               // System.out.print(attributes.getValue("r") + " - ");
                AddCellToRow(attributes.getValue("r"),"");
                //r - rownum
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once
            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                nextIsString = false;
                //idx -- cell number-1
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
                AddCellToRow("",lastContents);
                //System.out.println(lastContents);
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }
    }
    public Map<String,String> AddTextCells(String rec) {

        Map<String, String> textCells = new HashMap<String, String>();
        Scanner sc = new Scanner(rec);
        sc.useDelimiter(delimit);
        int idx = 1;
        while (sc.hasNext()) {
            textCells.put(String.valueOf(idx), sc.next());
            idx++;
        }
        return textCells;
    }
    private int processTextFiles() {
        int rowIndex = 1;
        int cacheSize = 1;
        logger.info("Open Text");

        int masterResult = -2;
        Scanner sc = null;
        String str=null;
        try {
            sc = new Scanner(file);
            // Check if there is another line of input
            while (sc.hasNextLine()) {
                str = sc.nextLine();
                insertTable.AddRow(AddTextCells(str));
                //rowIndex++;
                insertTable.rowCount++;
            }
            masterResult = insertTable.InsertRecord(true,null, str,rowIndex+0.0);
            if (masterResult != 0)
            {
                System.out.println("Log=> " + "Error updating loader log table:" + file.getName());
                logger.error("Error updating loader log table:" + file.getName());
                return -1;
            }

            else {

                System.out.println("Log=> " +"Completed :" + file.getName());
                logger.info("Completed :" + file.getName());
                return 0;
            }

        } catch (FileNotFoundException e) {
            logger.error("Error",e);
        }
        catch (Exception e) {
            logger.error("Error",e);
        }
        finally {
            sc.close();
            sc=null;
        }
        return 0;
    }
    private int processTextFile() {
        int rowIndex = 1;
        int cacheSize = 1;
        logger.info("Open Text");

        int masterResult = -2;
        Scanner sc = null;
        InsertTableBase textInsert =null;
        String str=null;
        if (file.getName().toUpperCase().indexOf("_U") > 0) {

            textInsert = new InsertTableBase(TableType.DIST_USAGE_STG1,file.getName(),fileType);
        } else if (file.getName().toUpperCase().indexOf("_C") > 0) {

            textInsert = new InsertTableBase(TableType.DIST_CUST_STG1,file.getName(),fileType);
        } else if (file.getName().toUpperCase().indexOf("_I") > 0) {
            textInsert = new InsertTableBase(TableType.DIST_ITEM_STG1,file.getName(),fileType);
        }
        try {
            sc = new Scanner(file);
            // Check if there is another line of input
            while (sc.hasNextLine()) {
              str = sc.nextLine();
               textInsert.AddRow(null,str);
                rowIndex++;
            }
            masterResult = textInsert.InsertRecord(true,null, str,rowIndex+0.0);
            if (masterResult != 0)
            {
                System.out.println("Log=> " + "Error updating loader log table:" + file.getName());
                logger.error("Error updating loader log table:" + file.getName());
                return -1;
            }

        else {

            System.out.println("Log=> " +"Completed :" + file.getName());
            logger.info("Completed :" + file.getName());
            return 0;
        }

        } catch (FileNotFoundException e) {
            logger.error("Error",e);
        }
        catch (Exception e) {
            logger.error("Error",e);
        }
        finally {
            sc.close();
            sc=null;
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
                addRecords = new InsertTableBase(TableType.DIST_MASTER_STG1,file.getName(),fileType);
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
                masterResult = addRecords.InsertRecord(true,rec,null, rowIndex+0.0);
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

/*
Convert serial number to date
public static DateTime FromExcelSerialDate(int SerialDate)
{
    if (SerialDate > 59) SerialDate -= 1; //Excel/Lotus 2/29/1900 bug
    return new DateTime(1899, 12, 31).AddDays(SerialDate);
}
 */
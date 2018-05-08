import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Scanner;


public  class InsertTableBase implements  InsertTable{
    private Connection conn;
    Map fieldLength;
    private String insertSQL;
    PreparedStatement insertStatement;
    int rowCount = 0;
    int fileId = -1;
    String fileName;
    int[] decimalIndex;
    TableType tableType;
    int skippedRow =0;
    String distId;
    int totalRecords=0;

    public InsertTableBase( TableType p_table,String p_fileName) {
        conn = JdbcOracleConnection.getConnection(CommonObjects.getConnectionString());

        tableType=p_table;

        fieldLength = CommonObjects.ReturnMap(p_table) ;
        decimalIndex=CommonObjects.DecimalIndex(p_table);
        insertSQL = CommonObjects.TableQuery(p_table);
        fileName = p_fileName;

        fileId = ReadDB.getFileId(conn);
         distId= ReadDB.getSupplierId(fileName.substring(0,fileName.indexOf("_")),conn);
    }
    public int InsertRecord(boolean finalCommit,Row excelRow,String rec,Double totalRecords){
        try {


                    insertStatement.executeBatch();
                    if (finalCommit) {
                        AddLoaderLog(totalRecords);
                        AddControlLog(excelRow,rec);
                        if (tableType != TableType.DIST_MASTER_STG1)
                            AddSrcTrack();
                        conn.commit();
                    }
                    else
                    {
                        insertStatement=conn.prepareStatement(insertSQL);
                    }



            return 0;
        } catch (SQLException e) {

            logger.error("Error",e);
                return -2;
            }
    }
    private int AddSrcTrack()
    {
        try {
            insertStatement =  conn.prepareStatement(CommonObjects.TableQuery(TableType.LD_SRC_FILE_TRACK));
            insertStatement.setString(1,"DISTU");
            insertStatement.setString(2,distId);
            String entity ="";

            switch (tableType){
                case DIST_CUST_STG1:
                {
                    entity="CUST";
                    break;
                }
                case DIST_ITEM_STG1:
                {
                    entity="ITEM";
                    break;
                }
                case DIST_USAGE_STG1:
                {
                    entity="USAGE";
                    break;
                }
            }
            insertStatement.setString(3,entity);
            insertStatement.setString(4, fileName);
            insertStatement.setString(5,"R");
            insertStatement.setInt(6,0);
            insertStatement.setInt(7,0);
            insertStatement.setInt(8,0);
            insertStatement.setString(9,"L");
            insertStatement.setString(10, tableType.name());
            insertStatement.setInt(11, fileId);


            insertStatement.execute();
            return  0;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
    }
    public int AddRow(Row excelRow,String rec){

        int result=-1;
        switch (tableType) {
            case DIST_MASTER_STG1: {
                result = AddExcelRow(excelRow);
                break;
            }
            case DIST_CUST_STG1:
            case DIST_USAGE_STG1:
            case DIST_ITEM_STG1: {
                result = AddTextRow(rec);
                break;
            }

        }

        rowCount++;
        if (rowCount > commitCount ){
            result = InsertRecord(false,null,null,-1.0);
            rowCount=1;
        }
        return result;//-2  for db commit error //-1 for add row error

    }
    private Double processControlTotals(Cell cell,String textCell)
    {
        DataFormatter dataFormatter = new DataFormatter();
       // String patt = "[^0-9.-]";
        String cellValue;
        Double result = 0.0 ;
        if (cell !=null)
            cellValue = dataFormatter.formatCellValue(cell);
        else
            cellValue = textCell;
        if (cellValue != "") {
            cellValue = cellValue.trim().replaceAll(patt,"");
            if (cellValue != "")
                result = Double.parseDouble(cellValue);
        }

        return  result;
    }
    private int AddControlLog(Row excelRow,String rec) {
        Double usageCount =0.0;
        Double spendCount=0.0;
        Double totalCount=0.0;
        String cellValue;
        try {
        DataFormatter dataFormatter = new DataFormatter();
        if (excelRow != null) {

            if (dataFormatter.formatCellValue(excelRow.getCell(0)).contains("ZZZ")) {
                 totalCount = processControlTotals(excelRow.getCell(1),null);
                 usageCount = processControlTotals(excelRow.getCell(2),null);
                 spendCount = processControlTotals(excelRow.getCell(3),null);


            }

        }

         else if (rec != null) {
            Scanner sc = new Scanner(rec);
            sc.useDelimiter("[|]");
            if (sc.hasNext()) {
                cellValue=sc.next();
                if (!cellValue.isEmpty() ) {

                    if (cellValue.equals("ZZZZZ")) {
                        if (sc.hasNext()) {
                            totalCount = processControlTotals(null, sc.next());
                            if (sc.hasNext()) {
                                usageCount = processControlTotals(null, sc.next());
                                if (sc.hasNext())
                                    spendCount = processControlTotals(null, sc.next());
                            }
                        }
                    }
                }
            }

            }
            return InsertControlTotal(totalCount, usageCount, spendCount);
            } catch (Exception ex) {
                logger.error("Error", ex);
                return -1;
            }

    }
    private int InsertControlTotal(Double p_totalRecords, Double usageCount,Double spendCount)
    {
        try {
            insertStatement = conn.prepareStatement(CommonObjects.TableQuery(TableType.DIST_CONTROL_TOTALS));
            insertStatement.setInt(1, fileId);
            insertStatement.setDouble(2, p_totalRecords);
            insertStatement.setDouble(3, usageCount);
            insertStatement.setDouble(4, spendCount);
            insertStatement.setString(5, "M" + fileId);
            insertStatement.execute();
            return 0;

        }
        catch (SQLException ex) {
        logger.error("Error", ex);
        return -1;
    }
    }
    private int  AddLoaderLog(Double totalRecords)
    {
        try {
            insertStatement =  conn.prepareStatement(CommonObjects.TableQuery(TableType.LD_LOADER_LOG));
        insertStatement.setString(1,fileName);
        insertStatement.setInt(2,0);
        insertStatement.setInt(3,totalRecords.intValue());
        insertStatement.setInt(4,0);
        insertStatement.setInt(5,0);
        //insertStatement.setString(6, LocalDate.now().toString());
        insertStatement.setString(6, fileName.substring(0,fileName.indexOf("_")-1).toUpperCase() + "_Pervasive");
        insertStatement.setString(7, tableType.name());
        insertStatement.setInt(8, fileId);
        //insertStatement.setString(10, LocalDate.now().toString());
        insertStatement.setString(9,CommonObjects.getArchivePath());

        insertStatement.setString(10,"TEXT_PIPE");
        insertStatement.setString(11,distId);
        insertStatement.setInt(12,0);
        insertStatement.setString(13,"M" + fileId);
        insertStatement.execute();
        return  0;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }

    }
    private int AddTextRow(String rec)
    {

        int i=1;
        int idx=0;

        try {
            if (insertStatement  == null)
                insertStatement  =conn.prepareStatement(insertSQL);
            Scanner sc = new Scanner(rec);
            sc.useDelimiter("[|]");
            while(sc.hasNext() && idx < fieldLength.size()){

            String cellValue = sc.next();
            if ( idx ==0 && cellValue.replaceAll(firstCellPattern,"").length() < 5) {skippedRow++; return 0;}

                if (ArrayUtils.contains(decimalIndex, idx)) {
                    if (!cellValue.isEmpty()) {
                        cellValue = cellValue.replaceAll(patt, "");
                        if (cellValue.equals(""))
                            insertStatement.setObject(i, null, Types.DECIMAL);
                        else
                            insertStatement.setDouble(i, Double.parseDouble(cellValue));
                    } else
                        insertStatement.setObject(i, null, Types.DECIMAL);
                } else {
                    if (!cellValue.isEmpty())
                        insertStatement.setString(i, cellValue.substring(0, cellValue.length() > Integer.parseInt(fieldLength.get(idx + 1).toString()) ? Integer.parseInt(fieldLength.get(idx + 1).toString()) : cellValue.length()));
                    else
                        insertStatement.setObject(i, null, Types.NVARCHAR);
                }

                idx = idx + 1;
                i = i + 1;
            }
                insertStatement.setInt(i, fileId);
                i = i + 1;
                insertStatement.setString(i, "N");
                i = i + 1;
                insertStatement.setString(i, "M" + fileId);
                insertStatement.addBatch();
                return 0;

        }
        catch (NumberFormatException numEx){
            logger.error("Error",numEx);
            return  -1;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }

    }
    private int AddExcelRow(Row excelRow)
    {
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        int i=1;


        try {
            if (insertStatement  == null)
                insertStatement  =conn.prepareStatement(insertSQL);
            for(int idx =0;idx < fieldLength.size();idx++)
            {
                String cellValue =dataFormatter.formatCellValue(excelRow.getCell(idx)).trim();
                if (idx != 10) {

                    if (ArrayUtils.contains( decimalIndex, idx ))
                    {
                        if (!cellValue.isEmpty()) {
                            cellValue = cellValue.replaceAll(patt,"");
                            if (cellValue.equals(""))
                                insertStatement.setObject(i, null, Types.DECIMAL);
                            else
                                insertStatement.setDouble(i, Double.parseDouble(cellValue));
                        }
                        else
                            insertStatement.setObject(i, null, Types.DECIMAL);
                    }
                    else
                    {
                        if (!cellValue.isEmpty())
                            insertStatement.setString(i, cellValue.substring(0, cellValue.length() > Integer.parseInt(fieldLength.get(idx + 1).toString()) ? Integer.parseInt(fieldLength.get(idx + 1).toString()) : cellValue.length()));
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
        catch (NumberFormatException numEx){
            logger.error("Error",numEx);
            return  -1;
        }
        catch (SQLException ex)
        {
            logger.error("Error",ex);
            return -1;
        }
        finally {
            excelRow = null;
        }
    }

}

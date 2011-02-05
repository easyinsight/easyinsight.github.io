package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.logging.LogClass;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: James Boe
 * Date: Apr 28, 2008
 * Time: 6:46:52 PM
 */
@SuppressWarnings({"unchecked"})
public class XSSFExcelUploadFormat extends UploadFormat {

    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public boolean test(byte[] data) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private String createName(String key, int index) {
        if (key == null || "".equals(key.trim())) {
            return String.valueOf("Column " + index);
        }
        if (key.length() > 50) {
            key = key.substring(0, 50);
        }
        return key.trim();
    }

    protected GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap, Map<String, AnalysisItem> analysisItems) {
        try {
            GridData gridData = new GridData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            XSSFWorkbook wb = new XSSFWorkbook(bais);
            XSSFSheet sheet = wb.getSheetAt(0);



            // okay, first thing...
            // are the column headers horizontal or vertical?


            //Value[][] grid;


            String[] headerColumns = getHeaderColumns(sheet);





            List<Value[]> gridList = new ArrayList<Value[]>();

            Iterator<Row> rit = sheet.rowIterator();
            rit.next();
            for (; rit.hasNext(); ) {
                Row row = rit.next();
                Value[] values = new Value[row.getLastCellNum()];
                boolean foundAtLeastOneValue = false;
                Iterator<Cell> cit = row.cellIterator();

                for (; cit.hasNext(); ) {

                    Cell cell = cit.next();
                    if (cell.toString().length() > 0) {
                        foundAtLeastOneValue = true;
                    }
                    Value cellValue = getCellValue(cell);
                    values[cell.getColumnIndex()] = cellValue;
                }
                if (!foundAtLeastOneValue) {
                    continue;
                }
                gridList.add(values);

                if (dataTypeGuesser != null) {
                    for (int headerKeyCounter = 0; headerKeyCounter < headerColumns.length; headerKeyCounter++) {
                        if (headerKeyCounter < values.length) {
                            Value value = values[headerKeyCounter];
                            if (value != null) {
                                String headerColumn = headerColumns[headerKeyCounter];
                                if (headerColumn == null) {
                                    headerColumn = String.valueOf(headerKeyCounter);
                                }
                                Key key;
                                if (keyMap == null) {
                                    key = new NamedKey(createName(headerColumn, headerKeyCounter));
                                } else {
                                    key = keyMap.get(createName(headerColumn, headerKeyCounter));
                                }
                                dataTypeGuesser.addValue(key, value);
                            }
                        }
                    }
                }
            }

            gridData.rowCount = gridList.size();
            Value[][] grid = new Value[gridList.size()][];
            gridList.toArray(grid);
            gridData.grid = grid;
            gridData.headerColumns = headerColumns;
            return gridData;
        } catch (IOException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void persist(Connection conn, long feedID) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM EXCEL_UPLOAD_FORMAT WHERE FEED_ID = ?");
        clearStmt.setLong(1, feedID);
        clearStmt.executeUpdate();
        PreparedStatement insertFormatStmt = conn.prepareStatement("INSERT INTO EXCEL_UPLOAD_FORMAT (FEED_ID, EXCEL_MODEL) VALUES (?, ?)");
        insertFormatStmt.setLong(1, feedID);
        insertFormatStmt.setInt(2, FileBasedFeedDefinition.HSSF_MODEL);
        insertFormatStmt.execute();
    }

    @NotNull
    private static Value getCellValue(Cell cell) {
        Value obj;
        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_BLANK:
                obj = new EmptyValue();
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                obj = new StringValue(String.valueOf(cell.getBooleanCellValue()));
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    obj = new DateValue(cell.getDateCellValue());
                } else {
                    obj = new NumericValue(cell.getNumericCellValue());
                }
                break;
            case XSSFCell.CELL_TYPE_STRING:
                String value = cell.getRichStringCellValue().getString();
                Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
                Matcher matcher = pattern.matcher(value);
                if (matcher.find()) {
                    String substring = value.substring(matcher.start(), matcher.end());
                    try {
                        Date date = defaultDateFormat.parse(substring);
                        obj = new DateValue(date);
                    } catch (ParseException e) {
                        // ignore
                        obj = new StringValue(value.trim());
                    }
                } else {
                    obj = new StringValue(value.trim());
                }
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
            default:
                obj = new EmptyValue();
        }
        return obj;
    }

    private String[] getHeaderColumns(XSSFSheet sheet) {
        XSSFRow row = sheet.getRow(0);
        List<String> rowList = new ArrayList<String>();
        for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ) {
            Cell cell = cit.next();
            String header = cell.toString().trim();
            if ("".equals(header)) {
                continue;
            }
            if (header.length() > 100) {
                header = header.substring(0, 100).trim();
            }
            rowList.add(header);
        }
        String[] rows = new String[rowList.size()];
        rowList.toArray(rows);
        return rows;
    }
}
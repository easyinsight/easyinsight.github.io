package com.easyinsight.userupload;

import com.easyinsight.core.*;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jetbrains.annotations.NotNull;

/**
 * User: James Boe
 * Date: Apr 28, 2008
 * Time: 6:46:52 PM
 */
@SuppressWarnings({"unchecked"})
public class ExcelUploadFormat extends UploadFormat {

    private static DateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private String createName(String key) {
        if (key == null) {
            return "";
        }
        if (key.length() > 50) {
            key = key.substring(0, 50);
        }
        return key.trim();
    }

    protected GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap) {
        try {
            GridData gridData = new GridData();

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook wb = new HSSFWorkbook(bais);
            HSSFSheet sheet = wb.getSheetAt(0);



            // okay, first thing...
            // are the column headers horizontal or vertical?

            int orientation = findHeaderDirection(sheet);

            //Value[][] grid;


            String[] headerColumns = getHeaderColumns(sheet, orientation);


            boolean gotValue = false;
            Set<String> headerValuesObtained = new HashSet<String>(Arrays.asList(headerColumns));

            int rowModifier = orientation == VERTICAL_HEADERS ? 1 : 0;
            int columnModifier = orientation == HORIZONTAL_HEADERS ? 1 : 0;

            //grid = new Value[sheet.getLastRowNum() + columnModifier][];

            List<Value[]> gridList = new ArrayList<Value[]>();

            Iterator<Row> rit = sheet.rowIterator();
            if (orientation == VERTICAL_HEADERS) {
                rit.next();
            }
            int rowCounter = 0;
            for (; rit.hasNext(); ) {
                Row row = rit.next();
                if (row.getLastCellNum() < columnModifier) {
                    continue;
                }
                Value[] values = new Value[row.getLastCellNum() - columnModifier];
                boolean foundAtLeastOneValue = false;
                Iterator<Cell> cit = row.cellIterator();
                if (orientation == HORIZONTAL_HEADERS) {
                    Cell cell = cit.next();
                    if (cell.toString() != null && !"".equals(cell.toString())) {
                        foundAtLeastOneValue = true;
                    }
                }
                for (; cit.hasNext(); ) {

                    Cell cell = cit.next();
                    if (cell.toString().length() > 0) {
                        foundAtLeastOneValue = true;
                    }
                    Value cellValue = getCellValue(cell);
                    values[cell.getColumnIndex() - columnModifier] = cellValue;
                }
                if (!foundAtLeastOneValue) {
                    continue;
                }
                gridList.add(values);
                //grid[row.getRowNum() - rowModifier] = values;

                if (orientation == VERTICAL_HEADERS) {
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
                                        key = new NamedKey(createName(headerColumn));
                                    } else {
                                        key = keyMap.get(createName(headerColumn));
                                    }
                                    dataTypeGuesser.addValue(key, value);
                                    headerValuesObtained.remove(headerColumn);
                                    if (headerValuesObtained.isEmpty()) {
                                        gotValue = true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                     if (dataTypeGuesser != null) {
                        Value value = values[0];
                        if (value != null) {
                            String headerColumn = headerColumns[rowCounter];
                            if (headerColumn == null) {
                                headerColumn = String.valueOf(rowCounter);
                            }
                            Key key;
                            if (keyMap == null) {
                                key = new NamedKey(createName(headerColumn));
                            } else {
                                key = keyMap.get(createName(headerColumn));
                            }
                            dataTypeGuesser.addValue(key, value);
                            headerValuesObtained.remove(headerColumn);
                            if (headerValuesObtained.isEmpty()) {
                                gotValue = true;
                            }
                        }
                    }
                }
                rowCounter++;
            }

            gridData.orientation = orientation;
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

    private int findHeaderDirection(HSSFSheet sheet) {
        boolean topRowIsDates = false;
        Pattern pattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}");
        HSSFRow row = sheet.getRow(0);
        for (Iterator<Cell> cit = row.cellIterator(); cit.hasNext(); ) {
            Cell cell = cit.next();
            Matcher matcher = pattern.matcher(cell.toString());
            if (matcher.find()) {
                topRowIsDates = true;
            }
        }
        return topRowIsDates ? HORIZONTAL_HEADERS : VERTICAL_HEADERS;
    }

    public void persist(Connection conn, long feedID) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM EXCEL_UPLOAD_FORMAT WHERE FEED_ID = ?");
        clearStmt.setLong(1, feedID);
        clearStmt.executeUpdate();
        PreparedStatement insertFormatStmt = conn.prepareStatement("INSERT INTO EXCEL_UPLOAD_FORMAT (FEED_ID) VALUES (?)");
        insertFormatStmt.setLong(1, feedID);
        insertFormatStmt.execute();
    }

    @NotNull
    private static Value getCellValue(Cell cell) {
        Value obj;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_BLANK:
                obj = new EmptyValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                obj = new StringValue(String.valueOf(cell.getBooleanCellValue()));
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:                                                
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    obj = new DateValue(cell.getDateCellValue());
                } else {
                    obj = new NumericValue(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
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
            case HSSFCell.CELL_TYPE_FORMULA:
            default:
                obj = new EmptyValue();
        }
        return obj;
    }

    private String[] getHeaderColumns(HSSFSheet sheet, int alignment) {
        if (alignment == VERTICAL_HEADERS) {
            HSSFRow row = sheet.getRow(0);
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
        } else {
            List<String> rowList = new ArrayList<String>();
            Iterator<Row> rit = (Iterator<Row>)sheet.rowIterator();
            for (; rit.hasNext(); ) {
                Row row = rit.next();
                Cell cell = row.getCell(0);
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
}

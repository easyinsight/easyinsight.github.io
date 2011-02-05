package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.*;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.logging.LogClass;

import java.util.*;
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

    private static SimpleDateFormat defaultDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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
            HSSFWorkbook wb = new HSSFWorkbook(bais);
            HSSFSheet sheet = wb.getSheetAt(0);

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
            case HSSFCell.CELL_TYPE_BLANK:
                obj = new EmptyValue();
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                obj = new StringValue(String.valueOf(cell.getBooleanCellValue()));
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:                                                
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    DateValue dateValue = new DateValue(cell.getDateCellValue());
                    dateValue.setFormat(defaultDateFormat.toPattern());
                    obj = dateValue;
                } else {
                    obj = new NumericValue(cell.getNumericCellValue());
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:
                String value = cell.getRichStringCellValue().getString().trim();
                SimpleDateFormat format = guessDate(value);
                if (format == null) {
                    obj = new StringValue(value);
                } else {
                    try {
                        Date date = format.parse(value);
                        DateValue dateValue = new DateValue(date);
                        dateValue.setFormat(format.toPattern());
                        obj = dateValue;
                    } catch (ParseException e) {
                        // ignore
                        obj = new StringValue(value);
                    }
                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
            default:
                obj = new EmptyValue();
        }
        return obj;
    }

    private static SimpleDateFormat[] dateFormats = new SimpleDateFormat[] {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz"),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z"),
            new SimpleDateFormat("yyyy-MM-dd"),
            new SimpleDateFormat("MM-dd-yy"),
            new SimpleDateFormat("MM-dd-yyyy"),
            new SimpleDateFormat("MM/dd/yy"),
            new SimpleDateFormat("yyyy/MM/dd"),
            new SimpleDateFormat("MM/dd/yyyy")
    };

    private static SimpleDateFormat guessDate(String value) {
        SimpleDateFormat matchedFormat = null;
        for (SimpleDateFormat dateFormat : dateFormats) {
            try {
                dateFormat.parse(value);
                matchedFormat = dateFormat;
                break;
            } catch (ParseException e) {
                // didn't work...
            }
        }
        return matchedFormat;
    }

    private String[] getHeaderColumns(HSSFSheet sheet) {
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
    }
}

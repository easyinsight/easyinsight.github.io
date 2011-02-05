package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.*;
import com.easyinsight.dataset.ColumnSegment;
import com.csvreader.CsvReader;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/*
 * User: abaldwin
 * Date: Aug 14, 2009
 * Time: 6:49:22 PM
 */
public class CsvFileUploadFormat extends UploadFormat {

    private String createName(String key, int index) {
        if (key == null || "".equals(key.trim())) {
            return String.valueOf("Column " + index);
        }
        if (key.length() > 50) {
            key = key.substring(0, 50);
        }
        return key.trim();
    }

    public boolean test(byte[] data) {
        boolean valid = false;
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(data);
        CharsetMatch charsetMatch = charsetDetector.detect();
        String charsetName = charsetMatch.getName();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        CsvReader r= new CsvReader(stream, Charset.forName(charsetName));
        boolean foundHeaders = false;
        boolean foundRecord = true;
        try {
            while (!foundHeaders && foundRecord) {
                r.readHeaders();
                int headerLength = r.getHeaderCount();
                foundRecord = r.readRecord();
                if (r.getColumnCount() > 1 && r.getColumnCount() == headerLength) {
                    foundHeaders = true;
                    valid = true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return valid;
    }

    protected GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap, Map<String, AnalysisItem> analysisItems) {

        GridData gridData = new GridData();
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(data);
        CharsetMatch charsetMatch = charsetDetector.detect();
        String charsetName = charsetMatch.getName();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        CsvReader r= new CsvReader(stream, Charset.forName(charsetName));
        List<Value[]> grid = new ArrayList<Value[]>();
        String[] headerColumns = null;
        try {
            boolean foundHeaders = false;
            boolean foundRecord = true;
            r.readRecord();
            while (!foundHeaders && foundRecord) {
                headerColumns = r.getValues();
                gridData.headerColumns = headerColumns;
                foundRecord = r.readRecord();
                if (r.getColumnCount() > 1 && r.getColumnCount() == headerColumns.length) {
                    foundHeaders = true;
                }
            }
            if (!foundHeaders) {
                throw new RuntimeException("We were unable to find headers in the file.");
            }
            do {
                if (r.getColumnCount() != headerColumns.length) {
                    continue;
                }
                String[] values = new String[r.getColumnCount()];
                Value[] convertedValues = new Value[r.getColumnCount()];
                ColumnSegment columnSegment = new ColumnSegment();
                for(int j = 0;j < r.getColumnCount();j++) {
                    String string = r.get(j);
                    Value value = transformValue(string, analysisItems == null ? null : analysisItems.get(headerColumns[j]));

                    convertedValues[j] = value;
                }

                columnSegment.setValues(convertedValues);
                grid.add(convertedValues);

                if (dataTypeGuesser != null) {
                    for (int headerKeyCounter = 0; headerKeyCounter < headerColumns.length; headerKeyCounter++) {
                        if (headerKeyCounter < values.length) {
                            Value value = convertedValues[headerKeyCounter];
                            if (value != null) {
                                Key key;
                                if (keyMap == null) {
                                    key = new NamedKey(createName(headerColumns[headerKeyCounter], headerKeyCounter));
                                } else {
                                    key = keyMap.get(createName(headerColumns[headerKeyCounter], headerKeyCounter));
                                }
                                dataTypeGuesser.addValue(key, value);
                            }
                        }
                    }
                }
            } while (r.readRecord());
            gridData.rowCount = grid.size();
            Value[][] v = new Value[grid.size()][];
            for(int i = 0; i < grid.size();i++) {
                v[i] = new Value[grid.get(i).length];
                for(int j = 0;j < grid.get(i).length;j++) {
                    v[i][j] = grid.get(i)[j];
                }
            }
            gridData.grid = v;
            gridData.headerColumns = headerColumns;
        }
        catch (IOException e) {

        }
        return gridData;
    }

    private Value transformValue(String string, AnalysisItem analysisItem) {
        if (analysisItem == null) {
            Value value;
            if (string == null) {
                value = new EmptyValue();
            } else {
                string = string.trim();
                if ("".equals(string)) {
                    value = new EmptyValue();
                } else {
                    if (NumericValue.testValue(string)) {
                        value = new NumericValue(NumericValue.produceDoubleValue(string));
                    } else {
                        value = new StringValue(string);
                    }
                }
            }
            return value;
        } else {
            Value value;
            if (string == null) {
                value = new EmptyValue();
            } else {
                string = string.trim();
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    value = new NumericValue(NumericValue.produceDoubleValue(string));
                } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                    value = date.renameMeLater(new StringValue(string));
                } else {
                    value = new StringValue(string);
                }
            }
            return value;
        }
    }

    public void persist(Connection conn, long feedID) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM FLAT_FILE_UPLOAD_FORMAT WHERE FEED_ID = ?");
        clearStmt.setLong(1, feedID);
        clearStmt.executeUpdate();
        PreparedStatement insertFormatStmt = conn.prepareStatement("INSERT INTO FLAT_FILE_UPLOAD_FORMAT (FEED_ID, DELIMITER_PATTERN," +
                "DELIMITER_ESCAPE) VALUES (?, ?, ?)");
        insertFormatStmt.setLong(1, feedID);
        insertFormatStmt.setString(2, ",");
        insertFormatStmt.setString(3, "\"");
        insertFormatStmt.execute();
    }
}

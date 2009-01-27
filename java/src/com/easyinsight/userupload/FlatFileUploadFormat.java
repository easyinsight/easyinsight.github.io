package com.easyinsight.userupload;

import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.stream.google.IDataTypeGuesser;
import com.easyinsight.core.*;

import java.util.*;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

/**
 * User: James Boe
 * Date: Apr 28, 2008
 * Time: 6:47:08 PM
 */
public class FlatFileUploadFormat extends UploadFormat {
    private String delimiter;
    private String escapeSequence;

    public FlatFileUploadFormat() {
    }

    public FlatFileUploadFormat(String delimiter, String escapeSequence) {
        this.delimiter = delimiter;
        this.escapeSequence = escapeSequence;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getEscapeSequence() {
        return escapeSequence;
    }

    public void setEscapeSequence(String escapeSequence) {
        this.escapeSequence = escapeSequence;
    }    

    protected GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap) {
        GridData gridData = new GridData();

        String csvResults = new String(data);
        String[] csvLines = csvResults.split("\n");
        String headerLine = csvLines[0];
        Pattern pattern = Pattern.compile("\\" + delimiter);

        String[] initHeaderColumns = headerLine.split("\\" + delimiter);
        String[] headerColumns = new String[initHeaderColumns.length];
        for (int i = 0; i < initHeaderColumns.length; i++) {
            String initHeaderColumn = initHeaderColumns[i];
            headerColumns[i] = initHeaderColumn.trim();
        }
        Value[][] grid = new Value[csvLines.length - 1][];

        boolean gotValue = false;
        Set<String> headerValuesObtained = new HashSet<String>(Arrays.asList(headerColumns));

        for (int j = 1; j < csvLines.length; j++) {

            String csvLine = csvLines[j];
            //String[] values = new String[headerColumns.length];

            String[] values = pattern.split(csvLine);
            ColumnSegment columnSegment = new ColumnSegment();
            Value[] convertedValues = new Value[values.length];
            for (int i = 0; i < values.length; i++) {
                Value convertedValue;
                if (values[i] == null || "".equals(values[i])) {
                    convertedValue = new EmptyValue();
                } else {
                    convertedValue = new StringValue(values[i].trim());
                }
                convertedValues[i] = convertedValue;
            }
            columnSegment.setValues(convertedValues);
            grid[j - 1] = convertedValues;

            if (dataTypeGuesser != null && !gotValue) {
                for (int headerKeyCounter = 0; headerKeyCounter < headerColumns.length; headerKeyCounter++) {
                    Value value = convertedValues[headerKeyCounter];
                    if (value != null) {
                        Key key;
                        if (keyMap == null) {
                            key = new NamedKey(headerColumns[headerKeyCounter]);
                        } else {
                            key = keyMap.get(headerColumns[headerKeyCounter]);
                        }
                        dataTypeGuesser.addValue(key, value);
                        headerValuesObtained.remove(headerColumns[headerKeyCounter]);
                        if (headerValuesObtained.isEmpty()) {
                            gotValue = true;
                        }
                    }
                }
            }
        }

        gridData.orientation = VERTICAL_HEADERS;
        gridData.rowCount = csvLines.length - 1;
        gridData.grid = grid;
        gridData.headerColumns = headerColumns;
        return gridData;
    }

    public void persist(Connection conn, long feedID) throws SQLException {
        PreparedStatement insertFormatStmt = conn.prepareStatement("INSERT INTO FLAT_FILE_UPLOAD_FORMAT (FEED_ID, DELIMITER_PATTERN," +
                "DELIMITER_ESCAPE) VALUES (?, ?, ?)");
        insertFormatStmt.setLong(1, feedID);
        insertFormatStmt.setString(2, delimiter);
        insertFormatStmt.setString(3, escapeSequence);
        insertFormatStmt.execute();
    }
}

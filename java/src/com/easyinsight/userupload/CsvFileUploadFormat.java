package com.easyinsight.userupload;

import com.easyinsight.core.*;
import com.easyinsight.dataset.ColumnSegment;
import com.csvreader.CsvReader;

import java.util.*;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Aug 14, 2009
 * Time: 6:49:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class CsvFileUploadFormat extends UploadFormat {
    protected GridData createGridData(byte[] data, IDataTypeGuesser dataTypeGuesser, Map<String, Key> keyMap) {

        GridData gridData = new GridData();
        ByteArrayInputStream stream = new ByteArrayInputStream(data);
        CsvReader r= new CsvReader(stream, Charset.defaultCharset());
        List<Value[]> grid = new ArrayList<Value[]>();
        String[] headerColumns = null;
        Set<String> headerValuesObtained = null;
        try {
            boolean gotValue = false;
            if(r.readHeaders()) {
                headerColumns = r.getHeaders();
                gridData.headerColumns = headerColumns;
                headerValuesObtained = new HashSet<String>(Arrays.asList(headerColumns));
            }
            while(r.readRecord()) {
                String[] values = new String[r.getColumnCount()];
                ColumnSegment columnSegment = new ColumnSegment();
                for(int j = 0;j < r.getColumnCount();j++) {
                    values[j] = r.get(j);
                }
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
                grid.add(convertedValues);

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

package com.easyinsight.api.v2;

import com.easyinsight.api.DatePair;
import com.easyinsight.api.NumberPair;
import com.easyinsight.api.Row;
import com.easyinsight.api.StringPair;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 6:26:07 PM
 */
public class SerializedLoad {
    private List<SerialRow> rows;

    public List<SerialRow> getRows() {
        return rows;
    }

    public void setRows(List<SerialRow> rows) {
        this.rows = rows;
    }

    public List<Row> toRows() {
        List<Row> rows = new ArrayList<Row>();
        for (SerialRow serialRow : this.rows) {
            Row row = new Row();
            StringPair[] stringValues = null;
            if (serialRow.getStringValues() != null) {
                stringValues = new StringPair[serialRow.getStringValues().length];
                for (int i = 0; i < serialRow.getStringValues().length; i++) {
                    SerialStringValue stringPair = serialRow.getStringValues()[i];
                    StringPair stringValue = new StringPair();
                    stringValue.setKey(stringPair.getKey());
                    stringValue.setValue(stringPair.getValue());
                    stringValues[i] = stringValue;
                }
            }
            NumberPair[] numericValues = null;
            if (serialRow.getNumberValues() != null) {
                numericValues = new NumberPair[serialRow.getNumberValues().length];
                for (int i = 0; i < serialRow.getNumberValues().length; i++) {
                    SerialNumericValue numericValue = serialRow.getNumberValues()[i];
                    NumberPair numberPair = new NumberPair();
                    numberPair.setKey(numericValue.getKey());
                    numberPair.setValue(numericValue.getValue());
                    numericValues[i] = numberPair;
                }
            }
            DatePair[] dateValues = null;
            if (serialRow.getDateValues() != null) {
                dateValues = new DatePair[serialRow.getDateValues().length];
                for (int i = 0; i < serialRow.getDateValues().length; i++) {
                    SerialDateValue datePair = serialRow.getDateValues()[i];
                    DatePair dateValue = new DatePair();
                    dateValue.setKey(datePair.getKey());
                    dateValue.setValue(datePair.getValue());
                    dateValues[i] = dateValue;
                }
            }
            row.setStringPairs(stringValues);
            row.setNumberPairs(numericValues);
            row.setDatePairs(dateValues);
            rows.add(row);
        }
        return rows;
    }

    public static SerializedLoad fromRows(List<Row> rows) {
        SerializedLoad load = new SerializedLoad();
        load.rows = new ArrayList<SerialRow>();
        for (Row row : rows) {
            SerialRow serialRow = new SerialRow();
            SerialStringValue[] serialStringValues = null;
            if (row.getStringPairs() != null) {
                serialStringValues = new SerialStringValue[row.getStringPairs().length];
                for (int i = 0; i < row.getStringPairs().length; i++) {
                    StringPair stringPair = row.getStringPairs()[i];
                    SerialStringValue stringValue = new SerialStringValue();
                    stringValue.setKey(stringPair.getKey());
                    stringValue.setValue(stringPair.getValue());
                    serialStringValues[i] = stringValue;
                }
            }
            SerialNumericValue[] serialNumericValues = null;
            if (row.getNumberPairs() != null) {
                serialNumericValues = new SerialNumericValue[row.getNumberPairs().length];
                for (int i = 0; i < row.getNumberPairs().length; i++) {
                    NumberPair numberPair = row.getNumberPairs()[i];
                    SerialNumericValue numericValue = new SerialNumericValue();
                    numericValue.setKey(numberPair.getKey());
                    numericValue.setValue(numberPair.getValue());
                    serialNumericValues[i] = numericValue;
                }
            }
            SerialDateValue[] serialDateValues = null;
            if (row.getDatePairs() != null) {
                serialDateValues = new SerialDateValue[row.getDatePairs().length];
                for (int i = 0; i < row.getDatePairs().length; i++) {
                    DatePair datePair = row.getDatePairs()[i];
                    SerialDateValue stringValue = new SerialDateValue();
                    stringValue.setKey(datePair.getKey());
                    stringValue.setValue(datePair.getValue());
                    serialDateValues[i] = stringValue;
                }
            }
            serialRow.setStringValues(serialStringValues);
            serialRow.setNumberValues(serialNumericValues);
            serialRow.setDateValues(serialDateValues);
            load.rows.add(serialRow);
        }
        return load;
    }
}

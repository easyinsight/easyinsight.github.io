package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.DatePair;
import com.easyinsight.rowutil.v3web.NumberPair;
import com.easyinsight.rowutil.v3web.Row;
import com.easyinsight.rowutil.v3web.StringPair;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:07 AM
 */
public class DataRow {
    private Map<String, String> stringValues = new HashMap<String, String>();
    private Map<String, Number> numberValues = new HashMap<String, Number>();
    private Map<String, Date> dateValues = new HashMap<String, Date>();

    public void addValue(String key, String value) {
        stringValues.put(key, value);
    }

    public void addValue(String key, Number value) {
        numberValues.put(key, value);
    }

    public void addValue(String key, Date value) {
        dateValues.put(key, value);
    }

    Row toRow() throws DatatypeConfigurationException {
        Row row = new Row();
        for (Map.Entry<String, String> stringEntry : stringValues.entrySet()) {
            StringPair stringPair = new StringPair();
            stringPair.setKey(stringEntry.getKey());
            stringPair.setValue(stringEntry.getValue());
            row.getStringPairs().add(stringPair);
        }
        for (Map.Entry<String, Number> numberEntry : numberValues.entrySet()) {
            NumberPair numberPir = new NumberPair();
            numberPir.setKey(numberEntry.getKey());
            numberPir.setValue(numberEntry.getValue().doubleValue());
            row.getNumberPairs().add(numberPir);
        }
        for (Map.Entry<String, Date> dateEntry : dateValues.entrySet()) {
            DatePair datePair = new DatePair();
            datePair.setKey(dateEntry.getKey());
            GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
            cal.setTime(dateEntry.getValue());
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
            datePair.setValue(xmlCalendar);
            row.getDatePairs().add(datePair);
        }
        return row;
    }
}

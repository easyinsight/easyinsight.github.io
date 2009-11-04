package com.easyinsight.rowutil;

import java.rmi.RemoteException;
import java.util.*;

import com.easyinsight.rowutil.webservice.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;

/**
 * User: jamesboe
 * Date: Oct 2, 2009
 * Time: 10:38:16 PM
 */
public class RowUtil {

    private List<Row> rows = new ArrayList<Row>();

    private String[] paramNames;
    private String dataSource;

    private String key;
    private String secretKey;
    private RowMethod rowMethod;

    private WhereUtil whereUtil;
    private Where where;

    /**
     * Creates a utility object for passing data into Easy Insight.
     * @param rowMethod specifies what particular method will be called on Easy Insight
     * @param key your user API key in Easy Insight, found in the API page
     * @param secretKey your user API secret key in Easy Insight, found in the API page
     * @param dataSource the name of the data source you're passing data into. If the data source doesn't already exist, it will be created by that name.
     * @param params the set of fields you're passing data in for on the data source. The data source metadata will be changed dynamically to match this set of fields.
     */
    public RowUtil(RowMethod rowMethod, String key, String secretKey, String dataSource, String... params) {
        this.rowMethod = rowMethod;
        this.key = key;
        this.secretKey = secretKey;
        this.dataSource = dataSource;
        paramNames = params;
    }

    /**
     * Sends the accumulated data across into Easy Insight and clears out all data state.
     * @throws RemoteException if something goes wrong in sending the data to Easy Insight
     */
    public void flush() throws RemoteException {
        if (rowMethod == RowMethod.UPDATE && whereUtil == null) {
            throw new RowException("You need to specify a Where clause via where() when you have a RowMethod of UPDATE.");
        }
        BasicAuthUncheckedPublishServiceService service = new BasicAuthUncheckedPublishServiceService();
        BasicAuthUncheckedPublish port = service.getBasicAuthUncheckedPublishServicePort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, key);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, secretKey);
        if (rowMethod == RowMethod.ADD) {
            port.addRows(dataSource, this.rows);
        } else if (rowMethod == RowMethod.REPLACE) {
            port.replaceRows(dataSource, this.rows);
        } else if (rowMethod == RowMethod.UPDATE) {
            port.updateRows(dataSource, this.rows, where);
        }
        rows = new ArrayList<Row>();
        where = null;
        whereUtil = null;
    }

    /**
     * Creates or returns a where clause defining a conditional update on the data.
     * @return the Where object
     */
    public WhereUtil where() {
        if (rowMethod != RowMethod.UPDATE) {
            throw new RowException("You can only specify a Where clause when you have a RowMethod of UPDATE.");
        }

        if (whereUtil == null) {
            this.where = new Where();
            whereUtil = new WhereUtil(this.where);
        }
        return whereUtil;
    }

    /**
     * Defines a row of data. Parameters will be matched by order with the parameter names defined in the constructor. Accepted types are String, Number, and Date.
     * @param params the set of parameters being passed into the row
     */
    public void newRow(Object... params) {
        try {
            Row row = new Row();
            List<StringPair> stringValues = new ArrayList<StringPair>();
            List<NumberPair> doubleValues = new ArrayList<NumberPair>();
            List<DatePair> dateValues = new ArrayList<DatePair>();
            for (int i = 0; i < params.length; i++) {
                String key = paramNames[i];
                Object param = params[i];
                if (param instanceof String) {
                    StringPair stringPair = new StringPair();
                    stringPair.setKey(key);
                    stringPair.setValue((String) param);
                    stringValues.add(stringPair);
                } else if (param instanceof Number) {
                    NumberPair numberPair = new NumberPair();
                    numberPair.setKey(key);
                    numberPair.setValue(((Number) param).doubleValue());
                    doubleValues.add(numberPair);
                } else if (param instanceof Date) {
                    DatePair datePair = new DatePair();
                    datePair.setKey(key);
                    GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                    cal.setTime((Date) param);
                    XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                    datePair.setValue(xmlCalendar);
                    dateValues.add(datePair);
                }
            }
            row.getStringPairs().addAll(stringValues);
            row.getNumberPairs().addAll(doubleValues);
            row.getDatePairs().addAll(dateValues);
            rows.add(row);
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
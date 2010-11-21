package com.easyinsight.rowutil;

import com.easyinsight.rowutil.transactional.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.rmi.RemoteException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Oct 2, 2009
 * Time: 10:38:16 PM
 */
public class TransactionalLoad {

    private List<Row> rows = new ArrayList<Row>();

    private String[] paramNames;
    private String dataSource;

    private String key;
    private String secretKey;
    private RowMethod rowMethod;

    private boolean changeDataSourceToMatch;

    private int bufferSize;

    private String transactionString;

    /**
     * Creates a utility object for passing data into Easy Insight.
     * @param rowMethod specifies what particular method will be called on Easy Insight
     * @param key your user API key in Easy Insight, found in the API page
     * @param secretKey your user API secret key in Easy Insight, found in the API page
     * @param dataSource the name of the data source you're passing data into. If the data source doesn't already exist, it will be created by that name.
     * @param changeDataSourceToMatch change the data source if the fields don't match
     * @param params the set of fields you're passing data in for on the data source. The data source metadata will be changed dynamically to match this set of fields.
     */
    public TransactionalLoad(RowMethod rowMethod, String key, String secretKey, String dataSource, boolean changeDataSourceToMatch,
                             int bufferSize, String... params) {
        this.rowMethod = rowMethod;
        this.key = key;
        this.secretKey = secretKey;
        this.dataSource = dataSource;
        this.changeDataSourceToMatch = changeDataSourceToMatch;
        this.bufferSize = bufferSize;
        paramNames = params;
    }

    public void startData() throws RemoteException {
        BasicAuthTransactionalLoadAPIService service = new BasicAuthTransactionalLoadAPIService();
        EITransactionalLoad port = service.getBasicAuthTransactionalLoadAPIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, key);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, secretKey);
        transactionString = port.beginTransaction(dataSource, rowMethod == RowMethod.REPLACE, changeDataSourceToMatch);
    }

    /**
     * Sends the accumulated data across into Easy Insight and clears out all data state.
     * @throws java.rmi.RemoteException if something goes wrong in sending the data to Easy Insight
     */
    public void flush() throws RemoteException {
        if (rows.size() > 0) {
            BasicAuthTransactionalLoadAPIService service = new BasicAuthTransactionalLoadAPIService();
            EITransactionalLoad port = service.getBasicAuthTransactionalLoadAPIPort();
            ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, key);
            ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, secretKey);
            port.loadRows(rows, transactionString);
            CommitResult commitResult = port.commit(transactionString);
            successful = successful && commitResult.isSuccessful();
            if (commitResult.getFailedRows() != null) {
                failureMessage = commitResult.getFailureMessage();
                for (RowStatus rowStatus : commitResult.getFailedRows()) {
                    failedRows.add(toRowResult(rowStatus));
                }
            }
            rows = new ArrayList<Row>();
        }
    }

    private String failureMessage = null;

    public TransactionResults generateResults() {
        return new TransactionResults(successful, failedRows, failureMessage);
    }

    private RowResult toRowResult(RowStatus rowStatus) {
        List<StringPair> stringPairs;
        if (rowStatus.getRow().getStringPairs() == null) {
            stringPairs = new ArrayList<StringPair>();
        } else {
            stringPairs = rowStatus.getRow().getStringPairs();
        }
        List<NumberPair> numberPairs;
        if (rowStatus.getRow().getNumberPairs() == null) {
            numberPairs = new ArrayList<NumberPair>();
        } else {
            numberPairs = rowStatus.getRow().getNumberPairs();
        }
        List<DatePair> datePairs;
        if (rowStatus.getRow().getDatePairs() == null) {
            datePairs = new ArrayList<DatePair>();
        } else {
            datePairs = rowStatus.getRow().getDatePairs();
        }
        return new RowResult(stringPairs, numberPairs, datePairs, rowStatus.getFailureMessage());
    }

    private List<RowResult> failedRows = new ArrayList<RowResult>();
    private boolean successful = true;

    private void checkCapacity() throws RemoteException {
        if (rows.size() == bufferSize) {
            flush();
        }
    }

    /**
     * Defines a row of data. Parameters will be matched by order with the parameter names defined in the constructor. Accepted types are String, Number, and Date.
     * @param params the set of parameters being passed into the row
     */
    public void newRow(Object... params) throws RemoteException {
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
            checkCapacity();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
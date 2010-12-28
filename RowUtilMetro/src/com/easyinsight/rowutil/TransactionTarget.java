package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.BasicAuthEIV3APIService;
import com.easyinsight.rowutil.v3web.EIDataV3;
import com.easyinsight.rowutil.v3web.Row;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/28/10
 * Time: 12:57 PM
 */
public class TransactionTarget {
    private List<DataRow> rows = new ArrayList<DataRow>();
    private int bufferSize;

    private String dataSourceKey;
    private String apiKey;
    private String apiSecretKey;

    private String transactionID;

    private boolean replaceData;

    TransactionTarget(String dataSourceKey, String apiKey, String apiSecretKey, boolean replaceData, int bufferSize) {
        this.dataSourceKey = dataSourceKey;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.replaceData = replaceData;
        this.bufferSize = bufferSize;
    }

    public void beginTransaction() {
        getPort().beginTransaction(dataSourceKey, replaceData, true);
    }

    private EIDataV3 getPort() {
        BasicAuthEIV3APIService service = new BasicAuthEIV3APIService();
        EIDataV3 port = service.getBasicAuthEIV3APIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, apiKey);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, apiSecretKey);
        return port;
    }

    public void commit() throws DatatypeConfigurationException {
        flush();
        getPort().commit(transactionID);
    }

    public void rollback() {
        getPort().rollback(transactionID);
    }

    public void flush() throws DatatypeConfigurationException {
        List<Row> wsRows = new ArrayList<Row>();
        for (DataRow dataRow : rows) {
            wsRows.add(dataRow.toRow());
        }
        getPort().loadRows(wsRows, transactionID);
    }

    private void checkCapacity() throws DatatypeConfigurationException {
        if (rows.size() == bufferSize) {
            flush();
        }
    }

    public DataRow newRow() throws DatatypeConfigurationException {
        checkCapacity();
        DataRow dataRow = new DataRow();
        rows.add(dataRow);
        return dataRow;
    }
}

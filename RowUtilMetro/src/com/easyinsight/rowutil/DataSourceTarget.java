package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.BasicAuthEIV3APIService;
import com.easyinsight.rowutil.v3web.EIDataV3;
import com.easyinsight.rowutil.v3web.Row;
import com.easyinsight.rowutil.v3web.Where;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 9:59 AM
 */
public class DataSourceTarget {

    public static final int ADD_ROWS = 1;
    public static final int REPLACE_ROWS = 2;
    public static final int UPDATE_ROWS = 3;

    private String dataSourceKey;
    private String apiKey;
    private String apiSecretKey;

    private int operation;

    private List<DataRow> rows = new ArrayList<DataRow>();

    DataSourceTarget(String dataSourceKey, String apiKey, String apiSecretKey, int operation) {
        this.dataSourceKey = dataSourceKey;
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.operation = operation;
    }

    void setWhereClauses(WhereClause[] whereClauses) {
        this.whereClauses = Arrays.asList(whereClauses);
    }

    private List<WhereClause> whereClauses;

    /**
     * Provides you with a <code>DataRow</code> object, representing one row of data in the data set you're adding
     * to the data source in Easy Insight.
     * @return
     */
    public DataRow newRow() {
        DataRow dataRow = new DataRow();
        rows.add(dataRow);
        return dataRow;
    }

    /**
     * Flushes all the rows you've created on this object and publishes them into Easy Insight.
     * @throws DatatypeConfigurationException
     */
    public void flush() throws DatatypeConfigurationException {
        BasicAuthEIV3APIService service = new BasicAuthEIV3APIService();
        EIDataV3 port = service.getBasicAuthEIV3APIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, apiKey);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, apiSecretKey);
        List<Row> wsRows = new ArrayList<Row>();
        for (DataRow dataRow : rows) {
            wsRows.add(dataRow.toRow());
        }
        if (operation == ADD_ROWS) {
            port.addRows(dataSourceKey, wsRows, false);
        } else if (operation == REPLACE_ROWS) {
            port.replaceRows(dataSourceKey, wsRows, false);
        } else if (operation == UPDATE_ROWS) {
            Where where = new Where();
            for (WhereClause whereClause : whereClauses) {
                whereClause.addToWhere(where);
            }
            port.updateRows(dataSourceKey, wsRows, where, false);
        }
    }
}

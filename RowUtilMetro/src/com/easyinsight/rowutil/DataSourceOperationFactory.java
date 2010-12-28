package com.easyinsight.rowutil;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:20 AM
 */
public class DataSourceOperationFactory {

    private String apiKey;
    private String apiSecretKey;
    private String dataSourceKey;

    public DataSourceOperationFactory(String apiKey, String apiSecretKey, String dataSourceKey) {
        this.apiKey = apiKey;
        this.apiSecretKey = apiSecretKey;
        this.dataSourceKey = dataSourceKey;
    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }

    /**
     * Creates a <code>DataSourceTarget</code> object that you can use to add a set of rows to the data source
     * referenced in construction of this <code>DataSourceOperationFactory</code> object.
     * @return
     */
    public DataSourceTarget addRowsOperation() {
        return new DataSourceTarget(dataSourceKey, apiKey, apiSecretKey, DataSourceTarget.ADD_ROWS);
    }

    /**
     * Creates a <code>DataSourceTarget</code> object that you can use to replace a set of rows to the data source
     * referenced in construction of this <code>DataSourceOperationFactory</code> object.
     * @return
     */
    public DataSourceTarget replaceRowsOperation() {
        return new DataSourceTarget(dataSourceKey, apiKey, apiSecretKey, DataSourceTarget.REPLACE_ROWS);
    }

    /**
     * Creates a <code>DataSourceTarget</code> object that you can use to update a set of rows to the data source
     * referenced in construction of this <code>DataSourceOperationFactory</code> object.
     * @param whereClauses
     * @return
     */
    public DataSourceTarget updateRowsOperation(WhereClause... whereClauses) {
        DataSourceTarget target = new DataSourceTarget(dataSourceKey, apiKey, apiSecretKey, DataSourceTarget.UPDATE_ROWS);
        target.setWhereClauses(whereClauses);
        return target;
    }

    /**
     * Creates a <code>TransactionTarget</code> object that you can use to add a set of rows to the data source
     * using transactional boundaries.
     * @return
     */
    public TransactionTarget addRowsTransaction() {
        return new TransactionTarget(dataSourceKey, apiKey, apiSecretKey, false, 512);
    }

    /**
     * Creates a <code>TransactionTarget</code> object that you can use to replace a set of rows to the data source
     * using transactional boundaries.
     * @return
     */
    public TransactionTarget replaceRowsTransaction() {
        return new TransactionTarget(dataSourceKey, apiKey, apiSecretKey, true, 512);
    }
}

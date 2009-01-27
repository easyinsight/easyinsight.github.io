package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.dataset.DataSet;
import com.sforce.soap.enterprise.Soap;

/**
 * User: James Boe
 * Date: Jul 5, 2008
 * Time: 5:33:14 PM
 */
public class SalesforceDataRetrieval {
    public static final int ACCOUNTS = 1;
    public static final int OPPORTUNITIES = 2;

    public DataSet getDataSet(int subject, Soap port) {
        DataSet dataSet;
        switch (subject) {
            case ACCOUNTS:
                dataSet = new AccountsQueryNode().getDataSet(port);
                break;
            case OPPORTUNITIES:
                dataSet = new OpportunitiesQueryNode().getDataSet(port);
                break;
            default:
                throw new RuntimeException();
        }
        return dataSet;
    }
}

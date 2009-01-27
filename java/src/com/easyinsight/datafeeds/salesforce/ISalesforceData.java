package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.dataset.DataSet;
import com.sforce.soap.enterprise.Soap;

/**
 * User: James Boe
 * Date: Jul 5, 2008
 * Time: 5:50:22 PM
 */
public interface ISalesforceData {
    public DataSet getDataSet(Soap port);
}

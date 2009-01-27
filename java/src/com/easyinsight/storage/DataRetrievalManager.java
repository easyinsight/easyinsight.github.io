package com.easyinsight.storage;

/**
 * User: James Boe
 * Date: Jul 23, 2008
 * Time: 3:37:41 PM
 */
public class DataRetrievalManager {

    private static IDataRetrieval dataRetrieval;

    public static IDataRetrieval instance() {
        if (dataRetrieval == null) {
            //dataRetrieval = new S3DataRetrieval();
            dataRetrieval = new DataRetrieval();
        }
        return dataRetrieval;
    }
}

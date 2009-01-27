package com.easyinsight.api;

/**
 * User: James Boe
 * Date: Jan 20, 2009
 * Time: 11:18:52 AM
 */
public enum Comparison {
    GREATER_THAN, EQUAL_TO, LESS_THAN;

    public com.easyinsight.storage.Comparison createStorageComparison() {
        if (this == GREATER_THAN) {
            return com.easyinsight.storage.Comparison.EQUAL_TO;
        } else if (this == LESS_THAN) {
            return com.easyinsight.storage.Comparison.LESS_THAN;
        } else {
            return com.easyinsight.storage.Comparison.GREATER_THAN;
        }
    }
}

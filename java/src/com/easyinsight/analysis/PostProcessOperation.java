package com.easyinsight.analysis;

import com.easyinsight.datafeeds.CompositeFeedConnection;

/**
 * User: jamesboe
 * Date: 12/17/12
 * Time: 1:37 PM
 */
public class PostProcessOperation {
    private AnalysisItem target;
    private CompositeFeedConnection connection;
    private AnalysisItem fromField;

    public AnalysisItem getTarget() {
        return target;
    }

    public CompositeFeedConnection getConnection() {
        return connection;
    }

    public AnalysisItem getFromField() {
        return fromField;
    }

    public void setTarget(AnalysisItem target) {
        this.target = target;
    }

    public void setConnection(CompositeFeedConnection connection) {
        this.connection = connection;
    }

    public void setFromField(AnalysisItem fromField) {
        this.fromField = fromField;
    }
}

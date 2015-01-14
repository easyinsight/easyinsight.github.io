package com.easyinsight.analysis;

import com.easyinsight.userupload.UploadContext;

import java.io.Serializable;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/12/15
 * Time: 11:30 AM
 */
public class UploadContextBlob implements Serializable {
    private String name;
    private long dataSourceID;
    private boolean update;
    private UploadContext uploadContext;
    private List<AnalysisItem> analysisItems;
    boolean accountVisible;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UploadContext getUploadContext() {
        return uploadContext;
    }

    public void setUploadContext(UploadContext uploadContext) {
        this.uploadContext = uploadContext;
    }

    public List<AnalysisItem> getAnalysisItems() {
        return analysisItems;
    }

    public void setAnalysisItems(List<AnalysisItem> analysisItems) {
        this.analysisItems = analysisItems;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }
}

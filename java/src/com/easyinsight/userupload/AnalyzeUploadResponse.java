package com.easyinsight.userupload;

import java.util.Collection;

/**
 * User: jamesboe
 * Date: 6/11/13
 * Time: 3:40 PM
 */
public class AnalyzeUploadResponse {
    private Collection<FieldUploadInfo> fieldUploadInfos;
    private String error;

    public Collection<FieldUploadInfo> getFieldUploadInfos() {
        return fieldUploadInfos;
    }

    public void setFieldUploadInfos(Collection<FieldUploadInfo> fieldUploadInfos) {
        this.fieldUploadInfos = fieldUploadInfos;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

package com.easyinsight.userupload;

/**
 * User: James Boe
 * Date: Jun 30, 2008
 * Time: 10:14:03 PM
 */
public class UploadFormatAnalysis {
    private UserUploadAnalysis userUploadAnalysis;
    private UploadFormat uploadFormat;

    public UserUploadAnalysis getUserUploadAnalysis() {
        return userUploadAnalysis;
    }

    public void setUserUploadAnalysis(UserUploadAnalysis userUploadAnalysis) {
        this.userUploadAnalysis = userUploadAnalysis;
    }

    public UploadFormat getUploadFormat() {
        return uploadFormat;
    }

    public void setUploadFormat(UploadFormat uploadFormat) {
        this.uploadFormat = uploadFormat;
    }
}

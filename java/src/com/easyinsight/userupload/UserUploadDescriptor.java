package com.easyinsight.userupload;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 9:44:20 PM
 */
public class UserUploadDescriptor {
    private String name;
    private String fileName;
    private long dataFeedID;

    public UserUploadDescriptor() {
    }

    public UserUploadDescriptor(String name, String fileName, long dataFeedID) {
        this.name = name;
        this.fileName = fileName;
        this.dataFeedID = dataFeedID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }
}

package com.easyinsight.solutions;

/**
* User: jamesboe
* Date: 2/24/14
* Time: 9:30 AM
*/
public class ReportValidation {
    private boolean exists;
    private int existingVersion;
    private int requestedVersion;
    private String name;

    public int getRequestedVersion() {
        return requestedVersion;
    }

    public void setRequestedVersion(int requestedVersion) {
        this.requestedVersion = requestedVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public int getExistingVersion() {
        return existingVersion;
    }

    public void setExistingVersion(int existingVersion) {
        this.existingVersion = existingVersion;
    }
}

package com.easyinsight.datafeeds.cloudwatch;

/**
 * User: jamesboe
 * Date: Sep 2, 2009
 * Time: 3:28:01 PM
 */
public class EC2Info {
    private String instanceID;
    private String amiID;
    private String hostName;
    private String group;

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }

    public String getAmiID() {
        return amiID;
    }

    public void setAmiID(String amiID) {
        this.amiID = amiID;
    }
}

package com.easyinsight.api;

import com.easyinsight.api.dynamic.DynamicServiceDefinition;

/**
 * User: James Boe
 * Date: Sep 1, 2008
 * Time: 7:16:17 PM
 */
public class DynamicServiceDescriptor {
    private long feedID;
    private String feedName;
    private String wsdlURL;
    private DynamicServiceDefinition dynamicServiceDefinition;
    private long serviceID;

    public DynamicServiceDescriptor() {
    }

    public DynamicServiceDescriptor(long feedID, String feedName, String wsdlURL, long serviceID, DynamicServiceDefinition dynamicServiceDefinition) {
        this.feedID = feedID;
        this.feedName = feedName;
        this.wsdlURL = wsdlURL;
        this.serviceID = serviceID;
        this.dynamicServiceDefinition = dynamicServiceDefinition;
    }

    public DynamicServiceDefinition getDynamicServiceDefinition() {
        return dynamicServiceDefinition;
    }

    public void setDynamicServiceDefinition(DynamicServiceDefinition dynamicServiceDefinition) {
        this.dynamicServiceDefinition = dynamicServiceDefinition;
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getWsdlURL() {
        return wsdlURL;
    }

    public void setWsdlURL(String wsdlURL) {
        this.wsdlURL = wsdlURL;
    }

    public long getServiceID() {
        return serviceID;
    }

    public void setServiceID(long serviceID) {
        this.serviceID = serviceID;
    }
}

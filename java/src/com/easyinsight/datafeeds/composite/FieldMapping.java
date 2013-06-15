package com.easyinsight.datafeeds.composite;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 4/1/11
 * Time: 10:45 AM
 */
public class FieldMapping implements Serializable {
    private String federatedKey;
    private String sourceKey;

    public String getFederatedKey() {
        return federatedKey;
    }

    public void setFederatedKey(String federatedKey) {
        this.federatedKey = federatedKey;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }
}

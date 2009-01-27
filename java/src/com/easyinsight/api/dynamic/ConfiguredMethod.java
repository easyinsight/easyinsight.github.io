package com.easyinsight.api.dynamic;

import com.easyinsight.AnalysisItem;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Nov 11, 2008
 * Time: 2:33:30 PM
 */
public class ConfiguredMethod {
    private int methodType;
    private String methodName;
    private List<AnalysisItem> keys;

    public ConfiguredMethod() {
    }

    public ConfiguredMethod(int methodType, String methodName, List<AnalysisItem> keys) {
        this.methodType = methodType;
        this.methodName = methodName;
        this.keys = keys;
    }

    public MethodFactory createMethodFactory(long feedID, List<AnalysisItem> allItems, String paramClassName,
                                             String paramName, String paramSingleName) {
        MethodFactory methodFactory;
        if (methodType == 1) {
            List<AnalysisItem> dataItems = new ArrayList<AnalysisItem>(allItems);
            dataItems.removeAll(keys);
            methodFactory = new UpdateMethod(feedID, methodName, dataItems, paramClassName, paramName, paramSingleName, keys);
        } else {
            throw new UnsupportedOperationException();
        }
        return methodFactory;
    }

    @Override
    public ConfiguredMethod clone() {
        try {
            ConfiguredMethod configuredMethod = (ConfiguredMethod) super.clone();
            configuredMethod.setKeys(null);
            return configuredMethod;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMethodType() {
        return methodType;
    }

    public void setMethodType(int methodType) {
        this.methodType = methodType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<AnalysisItem> getKeys() {
        return keys;
    }

    public void setKeys(List<AnalysisItem> keys) {
        this.keys = keys;
    }
}

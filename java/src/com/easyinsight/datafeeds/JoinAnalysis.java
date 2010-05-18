package com.easyinsight.datafeeds;

import com.easyinsight.core.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: May 17, 2010
 * Time: 5:22:13 PM
 */
public class JoinAnalysis {
    private List<String> unjoinedSourceValues = new ArrayList<String>();
    private List<String> unjoinedTargetValues = new ArrayList<String>();
    private List<String> joinedValues = new ArrayList<String>();
    private String sourceDataSourceName;
    private String targetDataSourceName;

    public String getSourceDataSourceName() {
        return sourceDataSourceName;
    }

    public void setSourceDataSourceName(String sourceDataSourceName) {
        this.sourceDataSourceName = sourceDataSourceName;
    }

    public String getTargetDataSourceName() {
        return targetDataSourceName;
    }

    public void setTargetDataSourceName(String targetDataSourceName) {
        this.targetDataSourceName = targetDataSourceName;
    }

    public List<String> getUnjoinedSourceValues() {
        return unjoinedSourceValues;
    }

    public void setUnjoinedSourceValues(List<String> unjoinedSourceValues) {
        this.unjoinedSourceValues = unjoinedSourceValues;
    }

    public List<String> getUnjoinedTargetValues() {
        return unjoinedTargetValues;
    }

    public void setUnjoinedTargetValues(List<String> unjoinedTargetValues) {
        this.unjoinedTargetValues = unjoinedTargetValues;
    }

    public List<String> getJoinedValues() {
        return joinedValues;
    }

    public void setJoinedValues(List<String> joinedValues) {
        this.joinedValues = joinedValues;
    }
}

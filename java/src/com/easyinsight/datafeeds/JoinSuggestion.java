package com.easyinsight.datafeeds;

import com.easyinsight.core.DataSourceDescriptor;

import java.util.List;

/**
 * User: jamesboe
 * Date: 2/9/11
 * Time: 11:30 AM
 */
public class JoinSuggestion {
    private String sourceType;
    private String targetType;
    private List<DataSourceDescriptor> possibleSources;
    private List<DataSourceDescriptor> possibleTargets;

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public List<DataSourceDescriptor> getPossibleSources() {
        return possibleSources;
    }

    public void setPossibleSources(List<DataSourceDescriptor> possibleSources) {
        this.possibleSources = possibleSources;
    }

    public List<DataSourceDescriptor> getPossibleTargets() {
        return possibleTargets;
    }

    public void setPossibleTargets(List<DataSourceDescriptor> possibleTargets) {
        this.possibleTargets = possibleTargets;
    }
}

package com.easyinsight.core;

/**
 * User: jamesboe
 * Date: 8/6/14
 * Time: 1:09 PM
 */
public class DataSourceGroupDescriptor extends DataSourceDescriptor {
    private boolean partOfGroup;

    public DataSourceGroupDescriptor() {
    }

    public DataSourceGroupDescriptor(String name, long id, int dataSourceType, boolean accountVisible, int dataSourceBehavior) {
        super(name, id, dataSourceType, accountVisible, dataSourceBehavior);
    }

    public boolean isPartOfGroup() {
        return partOfGroup;
    }

    public void setPartOfGroup(boolean partOfGroup) {
        this.partOfGroup = partOfGroup;
    }
}

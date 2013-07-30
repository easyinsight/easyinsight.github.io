package com.easyinsight.userupload;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.tag.Tag;

import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 13, 2009
 * Time: 3:49:26 PM
 */
public class MyDataTree {
    private List<EIDescriptor> objects;
    private boolean includeGroup;
    private int reportCount;
    private int dashboardCount;
    private int dataSourceCount;
    private boolean accountOverSize;
    private List<Tag> tags;

    public MyDataTree() {
    }

    public MyDataTree(List<EIDescriptor> objects, boolean includeGroup) {
        this.objects = objects;
        this.includeGroup = includeGroup;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public boolean isAccountOverSize() {
        return accountOverSize;
    }

    public void setAccountOverSize(boolean accountOverSize) {
        this.accountOverSize = accountOverSize;
    }

    public int getReportCount() {
        return reportCount;
    }

    public void setReportCount(int reportCount) {
        this.reportCount = reportCount;
    }

    public int getDashboardCount() {
        return dashboardCount;
    }

    public void setDashboardCount(int dashboardCount) {
        this.dashboardCount = dashboardCount;
    }

    public int getDataSourceCount() {
        return dataSourceCount;
    }

    public void setDataSourceCount(int dataSourceCount) {
        this.dataSourceCount = dataSourceCount;
    }

    public List<EIDescriptor> getObjects() {
        return objects;
    }

    public void setObjects(List<EIDescriptor> objects) {
        this.objects = objects;
    }

    public boolean isIncludeGroup() {
        return includeGroup;
    }

    public void setIncludeGroup(boolean includeGroup) {
        this.includeGroup = includeGroup;
    }
}

package com.easyinsight.userupload;

import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 13, 2009
 * Time: 3:49:26 PM
 */
public class MyDataTree {
    private List<Object> objects;
    private boolean includeGroup;

    public MyDataTree() {
    }

    public MyDataTree(List<Object> objects, boolean includeGroup) {
        this.objects = objects;
        this.includeGroup = includeGroup;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

    public boolean isIncludeGroup() {
        return includeGroup;
    }

    public void setIncludeGroup(boolean includeGroup) {
        this.includeGroup = includeGroup;
    }
}

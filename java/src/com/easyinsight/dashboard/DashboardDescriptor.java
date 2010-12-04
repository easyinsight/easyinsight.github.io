package com.easyinsight.dashboard;

import com.easyinsight.core.EIDescriptor;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:45 PM
 */
public class DashboardDescriptor extends EIDescriptor {

    public DashboardDescriptor() {
    }

    /*public DashboardDescriptor(String name, long id) {
        super(name, id);
    }*/

    public DashboardDescriptor(String name, long id, String urlKey) {
        super(name, id, urlKey);
    }

    @Override
    public int getType() {
        return EIDescriptor.DASHBOARD;
    }
}

package com.easyinsight.dashboard;

/**
 * User: jamesboe
 * Date: 9/19/11
 * Time: 12:31 PM
 */
public interface IDashboardVisitor {
    public void accept(DashboardElement dashboardElement);
}

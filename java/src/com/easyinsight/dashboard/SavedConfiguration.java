package com.easyinsight.dashboard;

/**
 * User: jamesboe
 * Date: 10/22/13
 * Time: 6:34 PM
 */
public class SavedConfiguration {
    private String name;
    private long id;
    private DashboardStackPositions dashboardStackPositions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DashboardStackPositions getDashboardStackPositions() {
        return dashboardStackPositions;
    }

    public void setDashboardStackPositions(DashboardStackPositions dashboardStackPositions) {
        this.dashboardStackPositions = dashboardStackPositions;
    }
}

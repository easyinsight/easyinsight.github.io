package com.easyinsight.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 10/22/13
 * Time: 6:34 PM
 */
public class SavedConfiguration implements Serializable {
    private String name;
    private String urlKey;
    private long id;
    private DashboardStackPositions dashboardStackPositions;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

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

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("url", urlKey);
        return jo;
    }
}

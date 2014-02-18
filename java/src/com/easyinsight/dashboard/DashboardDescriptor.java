package com.easyinsight.dashboard;

import com.easyinsight.core.EIDescriptor;
import com.easyinsight.tag.Tag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:45 PM
 */
public class DashboardDescriptor extends EIDescriptor {

    private long dataSourceID;
    private List<Tag> tags;

    public DashboardDescriptor() {
    }

    public DashboardDescriptor(String name, long id, String urlKey, long dataSourceID, int role, String ownerName, boolean accountVisible) {
        super(name, id, urlKey, accountVisible);
        this.dataSourceID = dataSourceID;
        setRole(role);
        setAuthor(ownerName);
    }

    public DashboardDescriptor(String name, long id, String urlKey, long dataSourceID, int role, String ownerName, boolean accountVisible, int folder) {
        super(name, id, urlKey, accountVisible);
        this.dataSourceID = dataSourceID;
        setRole(role);
        setAuthor(ownerName);
        setFolder(folder);
    }

    public DashboardDescriptor(String name, long id, String urlKey, long dataSourceID, int role, String ownerName, boolean accountVisible, int folder, Date createdDate, Date modifiedDate) {
        super(name, id, urlKey, accountVisible);
        this.dataSourceID = dataSourceID;
        setRole(role);
        setAuthor(ownerName);
        setFolder(folder);
        setCreationDate(createdDate);
        setModifiedDate(modifiedDate);
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public int getType() {
        return EIDescriptor.DASHBOARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        DashboardDescriptor that = (DashboardDescriptor) o;

        if (dataSourceID != that.dataSourceID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (dataSourceID ^ (dataSourceID >>> 32));
        return result;
    }

    @Override
    public JSONObject toJSON() throws JSONException {
        JSONObject jo = super.toJSON();
        jo.put("type", "dashboard");
        JSONArray tags = new JSONArray();
        if(getTags() != null) {
            for(Tag t : getTags()) {
                tags.put(t.toJSON());
            }
        }
        jo.put("tags", tags);
        return jo;
    }
}

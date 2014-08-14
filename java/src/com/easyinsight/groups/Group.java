package com.easyinsight.groups;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.export.ExportMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 3:59:33 PM
 */
public class Group {
    private long groupID;
    private String name;
    private String description;
    private List<GroupUser> groupUsers = new ArrayList<GroupUser>();
    private String urlKey;
    private boolean dataSourcesAutoIncludeChildren;

    public boolean isDataSourcesAutoIncludeChildren() {
        return dataSourcesAutoIncludeChildren;
    }

    public void setDataSourcesAutoIncludeChildren(boolean dataSourcesAutoIncludeChildren) {
        this.dataSourcesAutoIncludeChildren = dataSourcesAutoIncludeChildren;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public List<GroupUser> getGroupUsers() {
        return groupUsers;
    }

    public void setGroupUsers(List<GroupUser> groupUsers) {
        this.groupUsers = groupUsers;
    }

    public long getGroupID() {
        return groupID;
    }

    public void setGroupID(long groupID) {
        this.groupID = groupID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("description", getDescription());
        jo.put("id", getGroupID());
        jo.put("auto_include", isDataSourcesAutoIncludeChildren());
        jo.put("users", new JSONArray(getGroupUsers().stream().map((a) -> {
            try {
                return a.toJSON(md);
            } catch(JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList())));
        return jo;
    }


    public static JSONObject getJSON(Group g, EIConnection conn, ExportMetadata md) throws JSONException {
        JSONObject jo = g.toJSON(md);
        List<DataSourceDescriptor> dataSources = new GroupService().getGroupDataSources(g.getGroupID());
        jo.put("data_sources", dataSources.stream().map((a) -> {
            try {
                return a.toJSON(md);
            } catch(JSONException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        return jo;
    }

    public static Group fromJSON(net.minidev.json.JSONObject jsonObject) {
        Group g = new Group();
        g.setName(String.valueOf(jsonObject.get("name")));
        g.setGroupID(Long.valueOf(String.valueOf(jsonObject.get("id"))));
        g.setDescription(String.valueOf(jsonObject.get("description")));
        g.setDataSourcesAutoIncludeChildren(Boolean.parseBoolean(String.valueOf(jsonObject.get("auto_include"))));

        return g;
    }
}

package com.easyinsight.userupload;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: jamesboe
 * Date: 10/12/12
 * Time: 10:34 AM
 */
public class CustomFolder {
    private String name;
    private long id;

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

    public JSONObject toJSON() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("id", getId());
        return jo;
    }
}

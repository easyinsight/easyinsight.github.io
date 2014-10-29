package com.easyinsight.datafeeds;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * User: James Boe
 * Date: Oct 3, 2008
 * Time: 6:11:03 PM
 */
public abstract class FeedConsumer implements Serializable {

    public static final int USER = 1;
    public static final int GROUP = 2;

    private String name;

    public FeedConsumer() {
    }

    public FeedConsumer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract int type();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedConsumer that = (FeedConsumer) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public FeedConsumer(net.minidev.json.JSONObject jsonObject) {
        setName(String.valueOf(jsonObject.get("name")));
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        return jo;
    }
}

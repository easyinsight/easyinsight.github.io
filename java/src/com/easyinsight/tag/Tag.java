package com.easyinsight.tag;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 7/25/13
 * Time: 3:51 PM
 */
public class Tag implements Serializable {
    private long id;
    private String name;
    private boolean dataSource;
    private boolean report;
    private boolean field;

    public Tag() {
    }

    public Tag(long id, String name, boolean dataSource, boolean report, boolean field) {
        this.id = id;
        this.name = name;
        this.dataSource = dataSource;
        this.report = report;
        this.field = field;
    }

    public boolean isField() {
        return field;
    }

    public void setField(boolean field) {
        this.field = field;
    }

    public boolean isDataSource() {
        return dataSource;
    }

    public void setDataSource(boolean dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isReport() {
        return report;
    }

    public void setReport(boolean report) {
        this.report = report;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return name;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", getName());
        jo.put("id", getId());
        return jo;
    }
}

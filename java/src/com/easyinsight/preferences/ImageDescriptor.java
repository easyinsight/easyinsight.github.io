package com.easyinsight.preferences;

import com.easyinsight.export.ExportMetadata;
import org.json.JSONException;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Nov 17, 2010
 * Time: 1:34:17 PM
 */

@Entity
@Table(name="image_data")
public class ImageDescriptor implements Serializable {
    private long id;
    private String name;
    private String contentType;

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

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public JSONObject toJSON(ExportMetadata md) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put("name", name);
        jo.put("content_type", contentType);
        jo.put("id", id);
        return jo;
    }
}

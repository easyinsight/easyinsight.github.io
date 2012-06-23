package com.easyinsight.preferences;

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
}

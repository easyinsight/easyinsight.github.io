package com.easyinsight.datafeeds.basecampnext;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 3/29/12
 * Time: 1:43 PM
 */
public class Project {
    private String id;
    private String name;
    private String description;
    private String url;
    private boolean archived;
    private Date updatedAt;

    public Project(String id, String name, String description, String url, Date updatedAt, boolean archived) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.updatedAt = updatedAt;
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}

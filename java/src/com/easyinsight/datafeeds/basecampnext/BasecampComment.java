package com.easyinsight.datafeeds.basecampnext;

import java.util.Date;

/**
 * User: jamesboe
 * Date: 7/6/14
 * Time: 1:28 PM
 */
public class BasecampComment {
    private String id;
    private String projectID;
    private Date createdAt;
    private String todoID;
    private String author;
    private String content;

    public BasecampComment(String id, Date createdAt, String todoID, String author, String content, String projectID) {
        this.id = id;
        this.createdAt = createdAt;
        this.todoID = todoID;
        this.author = author;
        this.content = content;
        this.projectID = projectID;
    }

    public String getProjectID() {
        return projectID;
    }

    public String getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getTodoID() {
        return todoID;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}

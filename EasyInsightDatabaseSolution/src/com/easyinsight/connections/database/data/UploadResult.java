package com.easyinsight.connections.database.data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 3/10/11
 * Time: 3:47 PM
 */

@Entity
public class UploadResult {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private Date startTime;
    private Date endTime;
    private boolean success;
    @Column(length = 4096, precision = 4096, columnDefinition = "LONG VARCHAR")
    private String message;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Query query;

    @Column(length = 4096, precision = 4096, columnDefinition = "LONG VARCHAR")
    private String stackTrace;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}

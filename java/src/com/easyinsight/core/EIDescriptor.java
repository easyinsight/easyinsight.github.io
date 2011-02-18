package com.easyinsight.core;

import com.easyinsight.security.Roles;

import java.util.Date;

/**
 * User: James Boe
 * Date: Mar 28, 2009
 * Time: 8:30:25 AM
 */
public abstract class EIDescriptor {

    public static final int DATA_SOURCE = 1;
    public static final int REPORT = 2;
    public static final int GROUP = 3;
    public static final int GOAL_TREE = 4;
    public static final int SOLUTION = 5;
    public static final int PACKAGE = 8;
    public static final int SCORECARD = 9;
    public static final int LOOKUP_TABLE = 10;
    public static final int DASHBOARD = 11;

    private String name;
    private String urlKey;
    private String author;
    private Date creationDate;
    private long id;
    private int role = Roles.SUBSCRIBER;

    public abstract int getType();

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

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

    public EIDescriptor() {
    }

    public EIDescriptor(String name, long id) {
        this.name = name;
        this.id = id;
    }

    protected EIDescriptor(String name, long id, String urlKey) {
        this.name = name;
        this.id = id;
        this.urlKey = urlKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EIDescriptor that = (EIDescriptor) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }
}

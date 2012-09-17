package com.easyinsight.connections.database.data;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 9/16/12
 * Time: 8:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class FieldInfo {

    public static final byte DEFAULT = 0;
    public static final byte MEASURE = 1;
    public static final byte GROUPING = 2;
    public static final byte DATE = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Query query;

    private String fieldName;
    private String columnName;
    private byte type;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}

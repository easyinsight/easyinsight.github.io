package com.easyinsight.audit;

import javax.persistence.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 5/13/11
 * Time: 7:50 PM
 */
@Entity
@Table(name="action_data_source_log")
@PrimaryKeyJoinColumn(name="action_log_id")
public class ActionDataSourceLog extends ActionLog {

    public static final int EDIT = 1;
    public static final int VIEW = 2;
    public static final int EXPORT_XLS = 3;
    public static final int EXPORT_PDF = 4;
    public static final int EXPORT_PNG = 5;

    @Column(name="data_source_id")
    private long dataSourceID;

    @Transient
    private String dataSourceName;

    public ActionDataSourceLog() {
    }

    public ActionDataSourceLog(long userID, int actionType, long dataSourceID) {
        super(userID, actionType);
        this.dataSourceID = dataSourceID;
    }

    public ActionDataSourceLog(long dataSourceID, String dataSourceName, int actionType, Date date) {
        this.dataSourceID = dataSourceID;
        this.dataSourceName = dataSourceName;
        setActionType(actionType);
        setActionDate(date);
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ActionDataSourceLog that = (ActionDataSourceLog) o;

        if (dataSourceID != that.dataSourceID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (dataSourceID ^ (dataSourceID >>> 32));
        return result;
    }
}


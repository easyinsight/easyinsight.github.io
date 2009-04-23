package com.easyinsight.scheduler;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.util.Date;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 8:24:36 PM
 */
@Entity
@Table(name="data_source_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class DataSourceTaskGenerator extends TaskGenerator {

    @Column(name="data_source_id")
    private long dataSourceID;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected ScheduledTask createTask() {
        DataSourceScheduledTask dataSourceScheduledTask = new DataSourceScheduledTask();
        dataSourceScheduledTask.setDataSourceID(dataSourceID);
        return dataSourceScheduledTask;
    }
}

package com.easyinsight.database;

import com.easyinsight.scheduler.TaskGenerator;
import com.easyinsight.scheduler.ScheduledTask;

import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Entity;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 9:13:15 PM
 */
@Entity
@Table(name="db_snapshot_scheduler")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class DatabaseVolumeScheduler extends TaskGenerator {

    public DatabaseVolumeScheduler() {
        // daily
        setTaskInterval(24 * 60 * 60 * 1000);
    }

    protected ScheduledTask createTask() {
        return new VolumeSnapshotTask();
    }
}
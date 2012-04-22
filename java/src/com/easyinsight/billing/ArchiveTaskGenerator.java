package com.easyinsight.billing;

import com.easyinsight.scheduler.ArchiveDataSourceTask;
import com.easyinsight.scheduler.ScheduledTask;
import com.easyinsight.scheduler.TaskGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 29, 2009
 * Time: 2:46:59 PM
 */
@Entity
@Table(name="archive_task_generator")
@PrimaryKeyJoinColumn(name="task_generator_id")
public class ArchiveTaskGenerator extends TaskGenerator {

    public ArchiveTaskGenerator() {
        super();
        setTaskInterval(24 * 60 * 60 * 1000);
    }

    protected ScheduledTask createTask() {
        return new ArchiveDataSourceTask();
    }
}

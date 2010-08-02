package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.persistence.*;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 8:03:09 PM
 */
@Entity
@Table(name="task_generator")
@Inheritance(strategy= InheritanceType.JOINED)
public class TaskGenerator {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="task_generator_id")
    private long taskGeneratorID;
    @Column(name="task_interval")
    private int taskInterval;
    @Column(name="last_task_date")
    private Date lastTaskDate;
    @Column(name="start_task_date")
    private Date startTaskDate;
    @Column(name="requires_backfill")
    private boolean requiresBackfill;

    public List<ScheduledTask> generateTasks(Date now, EIConnection conn) throws SQLException {
        List<ScheduledTask> tasks = new ArrayList<ScheduledTask>();
        // we run at 10:02
        // lastTime is going to be 10:00

        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        long lastTime = now.getTime() / taskInterval * taskInterval;
        Date lastScheduledRun = findStartTaskDate();
        long lastValidTime = lastScheduledRun.getTime() / taskInterval * taskInterval;

        while (lastValidTime < lastTime) {
            tasks.add(defineTask(lastValidTime + taskInterval));
            lastValidTime += taskInterval;
        }
        if (!isRequiresBackfill() && tasks.size() > 0) {
            tasks = tasks.subList(tasks.size() - 1, tasks.size());
        }
        if (tasks.size() > 0) {
            setLastTaskDate(new Date(lastTime));
        }
        return tasks;
    }

    protected ScheduledTask defineTask(long time) {
        ScheduledTask task = createTask();
        task.setStatus(ScheduledTask.SCHEDULED);
        task.setExecutionDate(new Date(time));
        task.setTaskGeneratorID(getTaskGeneratorID());
        return task;
    }

    protected ScheduledTask createTask() {
        throw new OrphanTaskException("Orphan task generator " + getTaskGeneratorID());
    }

    public boolean isRequiresBackfill() {
        return requiresBackfill;
    }

    public void setRequiresBackfill(boolean requiresBackfill) {
        this.requiresBackfill = requiresBackfill;
    }

    public long getTaskGeneratorID() {
        return taskGeneratorID;
    }

    public void setTaskGeneratorID(long taskGeneratorID) {
        this.taskGeneratorID = taskGeneratorID;
    }

    public Date findStartTaskDate() {
        if (lastTaskDate != null) {
            return lastTaskDate;
        }
        return startTaskDate;
    }

    public int getTaskInterval() {
        return taskInterval;
    }

    public void setTaskInterval(int taskInterval) {
        this.taskInterval = taskInterval;
    }

    public Date getLastTaskDate() {
        return lastTaskDate;
    }

    public void setLastTaskDate(Date lastTaskDate) {
        this.lastTaskDate = lastTaskDate;
    }

    public Date getStartTaskDate() {
        return startTaskDate;
    }

    public void setStartTaskDate(Date startTaskDate) {
        this.startTaskDate = startTaskDate;
    }
}

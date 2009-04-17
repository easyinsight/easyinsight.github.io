package com.easyinsight.scheduler;

import javax.persistence.*;
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
public abstract class TaskGenerator {

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

    public List<ScheduledTask> generateTasks(Date now) {
        List<ScheduledTask> tasks = new ArrayList<ScheduledTask>();
        Date initDate = findStartTaskDate();
        for (long startTime = initDate.getTime() + taskInterval; startTime < now.getTime(); startTime += taskInterval) {
            tasks.add(createTask(startTime));
        }        
        return tasks;
    }

    protected abstract ScheduledTask createTask(long time);

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

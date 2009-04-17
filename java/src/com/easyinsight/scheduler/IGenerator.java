package com.easyinsight.scheduler;

import java.util.List;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:31:41 PM
 */
public interface IGenerator {
    public List<ScheduledTask> createTasks();
    public long getInterval();
}

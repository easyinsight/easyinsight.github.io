package com.easyinsight.scheduler;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataTypeMutex;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.logging.LogClass;

import java.sql.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.*;

import org.hibernate.Session;
import org.hibernate.Query;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 4:38:42 PM
 */
public class Scheduler {

    public static final String[] taskClassArray = { "com.easyinsight.users.AccountTimeScheduler",
        "com.easyinsight.database.DatabaseVolumeScheduler", "com.easyinsight.billing.BillingTaskGenerator",
    "com.easyinsight.billing.AccountSyncTaskGenerator"};

    public static final int TASK_LIMIT = 5;

    public static final int ONE_MINUTE = 60000;

    public static final String SCHEDULE_LOCK = "SCHEDULE";
    public static final String TASK_LOCK = "TASK";

    private ThreadPoolExecutor executor;
    private Map<FeedType, ThreadPoolExecutor> limitedExectutorMap = new HashMap<>();
    private Timer timer;
    private boolean running = false;
    private boolean taskRunning = false;
    private boolean scheduleRunning = false;
    private Thread thread;

    private int claimed = 0;

    private static Scheduler instance;

    public static Scheduler instance() {
        return instance;
    }

    public static void initialize() {
        instance = new Scheduler();
    }

    // simplest level...

    private Scheduler() {
        executor = new ThreadPoolExecutor(TASK_LIMIT, TASK_LIMIT, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        timer = new Timer("Scheduler Timer");        
    }

    public void start() {
        running = true;        
        long nextMinute = System.currentTimeMillis() / ONE_MINUTE * ONE_MINUTE + ONE_MINUTE;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (running) {
                    scheduleRunning = true;
                    scheduleTasks();
                    scheduleRunning = false;
                }
            }
        }, new Date(nextMinute), ONE_MINUTE);
        launchThread();
        assignDefaultGenerators();
    }

    public void stop() {
        System.out.println("Stopping scheduler...");
        timer.cancel();
        executor.shutdown();
        for (ThreadPoolExecutor tpe : limitedExectutorMap.values()) {
            tpe.shutdown();
        }
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
        while (taskRunning && scheduleRunning) {
            System.out.println("Waiting for task and scheduling to stop...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // ignore
            }
        }
        System.out.println("Stopped scheduler");
    }

    private void launchThread() {
        this.thread = new Thread() {
            @Override
            public void run() {
                try {
                    executeScheduledTasks();
                } catch (InterruptedException ie) {
                    // ignore
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        };
        thread.setName("Scheduler");
        thread.start();
    }

    private void scheduleTasks() {
        boolean locked;
        locked = obtainLock(SCHEDULE_LOCK);
        if (locked) {
            try {
                Date now = new Date();
                // retrieve the task generators
                EIConnection conn = Database.instance().getConnection();
                Session session = Database.instance().createSession(conn);
                try {
                    conn.setAutoCommit(false);
                    List<TaskGenerator> taskGenerators = retrieveTaskGenerators(session);                    
                    for (TaskGenerator taskGenerator : taskGenerators) {
                        List<ScheduledTask> tasks;
                        try {
                            tasks = taskGenerator.generateTasks(now, conn);
                        } catch (OrphanTaskException e) {
                            continue;
                        }
                        for (ScheduledTask task : tasks) {
                            LogClass.info("Scheduling " + task.getClass().getName() + " for execution on " + task.getExecutionDate());
                            session.save(task);
                        }
                        if (!tasks.isEmpty()) {
                            taskGenerator.setLastTaskDate(now);                            
                        }
                        session.update(taskGenerator);
                    }
                    session.flush();
                    conn.commit();
                } catch (Exception e) {
                    LogClass.error(e);
                    conn.rollback();
                } finally {
                    session.close();
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
            } finally {
                releaseLock(SCHEDULE_LOCK);
            }
        }
    }

    private List<TaskGenerator> retrieveTaskGenerators(Session session) {
        return session.createQuery("from TaskGenerator where disabledGenerator = ?").setBoolean(0, false).list();
    }

    private void releaseLock(String lockName) {
        boolean successful = false;
        do {
            Connection conn = Database.instance().getConnection();
            try {
                PreparedStatement lockStmt = conn.prepareStatement("DELETE FROM DISTRIBUTED_LOCK WHERE LOCK_NAME = ?");
                lockStmt.setString(1, lockName);
                lockStmt.executeUpdate();
                lockStmt.close();
                successful = true;
            } catch (SQLException e) {
                LogClass.error(e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // ignore
                }
            } finally {
                Database.closeConnection(conn);
            }
        } while (!successful);
    }

    private boolean obtainLock(String lock) {
        boolean locked = false;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement lockStmt = conn.prepareStatement("INSERT INTO DISTRIBUTED_LOCK (LOCK_NAME, LOCK_TIME) VALUES (?, ?)");
            lockStmt.setString(1, lock);
            lockStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            lockStmt.execute();
            locked = true;
            lockStmt.close();
            conn.commit();
        } catch (SQLException e) {
            LogClass.debug("Failed to obtain distributed lock, assuming another app server has it.");
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return locked;
    }

    public void executeScheduledTasks() throws InterruptedException {
        // retrieve the set of tasks which are in the SCHEDULED state, limit N
        // add them to the thread pool
        while (running) {
            taskRunning = true;
            boolean obtainedLock = false;
            while (!obtainedLock) {
                obtainedLock = obtainLock(TASK_LOCK);
                if (!obtainedLock) {
                    Thread.sleep(1000);
                }
            }
            List<ScheduledTask> tasks;
            try {
                tasks = claimScheduledTasks();
            } finally {
                releaseLock(TASK_LOCK);
            }
            if (tasks.isEmpty()) {
                Thread.sleep(60000);
            } else {
                for (ScheduledTask task : tasks) {
                    boolean rerouted = false;
                    if (task instanceof DataSourceScheduledTask) {
                        DataSourceScheduledTask dataSourceScheduledTask = (DataSourceScheduledTask) task;
                        long dataSourceID = dataSourceScheduledTask.getDataSourceID();
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            PreparedStatement query = conn.prepareStatement("SELECT DATA_FEED.FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                            query.setLong(1, dataSourceID);
                            ResultSet rs = query.executeQuery();
                            if (rs.next()) {
                                int dataSourceType = rs.getInt(1);
                                FeedType feedType = new FeedType(dataSourceType);
                                Semaphore semaphore = DataTypeMutex.mutex().getMutexMap().get(feedType);
                                if (semaphore != null) {
                                    ThreadPoolExecutor executor = limitedExectutorMap.get(feedType);
                                    if (executor == null) {
                                        int limit = DataTypeMutex.mutex().getLockRequiredTypes().contains(feedType) ? 3 : 1;
                                        executor = new ThreadPoolExecutor(limit, limit, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
                                        limitedExectutorMap.put(feedType, executor);
                                    }
                                    rerouted = true;
                                    executor.execute(task);
                                }
                            }
                        } catch (Exception e) {
                            LogClass.error(e);
                        } finally {
                            Database.closeConnection(conn);
                        }
                    }
                    if (!rerouted) {
                        executor.execute(task);
                    }
                }
            }
            taskRunning = false;
        }
    }

    private List<ScheduledTask> claimScheduledTasks() {
        List<ScheduledTask> results;
        Session session = Database.instance().createSession();
        int curClaimed = 0;
        try {
            session.getTransaction().begin();
            Query query;
            if (ConfigLoader.instance().isEmailRunner()) {
                query = session.createQuery("from ScheduledTask where status = ?").
                        setInteger(0, ScheduledTask.SCHEDULED);
            } else {
                query = session.createQuery("from ScheduledTask where status = ? and taskType = ?").
                        setInteger(0, ScheduledTask.SCHEDULED).setInteger(1, ScheduledTask.OTHER);
            }
            query.setMaxResults(executor.getMaximumPoolSize() - executor.getActiveCount());
            results = query.list();
            for (ScheduledTask task : results) {
                task.setStatus(ScheduledTask.RUNNING);
                session.update(task);
                curClaimed++;
            }
            session.getTransaction().commit();
            claimed = claimed + curClaimed;
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return results;
    }

    public void assignDefaultGenerators() {
        Set<String> taskClasses = new HashSet<String>(Arrays.asList(taskClassArray));
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List<TaskGenerator> generators = session.createQuery("from TaskGenerator").list();
            for (TaskGenerator generator : generators) {
                taskClasses.remove(generator.getClass().getName());
            }
            for (String taskClass : taskClasses) {
                TaskGenerator taskGenerator = (TaskGenerator) Class.forName(taskClass).newInstance();
                taskGenerator.setRequiresBackfill(false);
                taskGenerator.setStartTaskDate(new Date());                
                session.save(taskGenerator);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    public void saveTask(ScheduledTask t) {
        EIConnection conn = Database.instance().getConnection();

        try {
            conn.setAutoCommit(false);
            saveTask(t, conn);
            conn.commit();
        }
        catch(Exception e) {
            conn.rollback();
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        finally {
            Database.closeConnection(conn);
        }
    }

    public void saveTask(ScheduledTask t, Connection conn) {
        t.setStartedDate(new Date());
        Session session = Database.instance().createSession(conn);
        try {
            session.save(t);
            session.flush();
            if(t.getStatus() == ScheduledTask.INMEMORY)
                executor.execute(t);
        }
        catch(Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public int getClaimed() {
        return claimed;
    }
}

package com.easyinsight.scheduler;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import java.util.concurrent.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.Session;
import org.hibernate.Query;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 4:38:42 PM
 */
public class Scheduler {

    public static final String[] taskClassArray = { "com.easyinsight.users.AccountTimeScheduler",
        "com.easyinsight.database.DatabaseVolumeScheduler", "com.easyinsight.billing.BillingTaskGenerator"};

    public static final int TASK_LIMIT = 5;

    public static final int ONE_MINUTE = 60000;

    public static final String SCHEDULE_LOCK = "SCHEDULE";
    public static final String TASK_LOCK = "TASK";

    private ThreadPoolExecutor executor;
    private Timer timer;
    private boolean running = false;
    private Thread thread;

    private static Scheduler instance;

    public static Scheduler instance() {
        return instance;
    }

    public static void initialize() {
        instance = new Scheduler();
    }

    // simplest level...

    private Scheduler() {
        executor = new ThreadPoolExecutor(5, 5, 5000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        timer = new Timer("Scheduler Timer");        
    }

    public void start() {
        running = true;        
        long nextMinute = System.currentTimeMillis() / ONE_MINUTE * ONE_MINUTE + ONE_MINUTE;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                if (running) {
                    scheduleTasks();
                }
            }
        }, new Date(nextMinute), ONE_MINUTE);
        launchThread();
        assignDefaultGenerators();
    }

    public void stop() {
        timer.cancel();
        executor.shutdown();
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
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
        return session.createQuery("from TaskGenerator").list();
    }

    private void releaseLock(String lockName) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement lockStmt = conn.prepareStatement("DELETE FROM DISTRIBUTED_LOCK WHERE LOCK_NAME = ?");
            lockStmt.setString(1, lockName);
            lockStmt.executeUpdate();
            lockStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private boolean obtainLock(String lock) {
        boolean locked = false;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement lockStmt = conn.prepareStatement("INSERT INTO DISTRIBUTED_LOCK (LOCK_NAME) VALUES (?)");
            lockStmt.setString(1, lock);
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
                    executor.execute(task);
                }
            }
        }
    }

    private List<ScheduledTask> claimScheduledTasks() {
        List<ScheduledTask> results;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            Query query = session.createQuery("from ScheduledTask where status = ?").
                setInteger(0, ScheduledTask.SCHEDULED);
            query.setMaxResults(TASK_LIMIT);
            results = query.list();
            for (ScheduledTask task : results) {
                task.setStatus(ScheduledTask.RUNNING);
                session.update(task);
            }
            session.getTransaction().commit();
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

}

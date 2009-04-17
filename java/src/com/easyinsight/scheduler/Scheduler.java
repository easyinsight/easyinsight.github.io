package com.easyinsight.scheduler;

import com.easyinsight.database.Database;
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

    public static final int TASK_LIMIT = 5;

    public static final int ONE_MINUTE = 60000;

    public static final String SCHEDULE_LOCK = "SCHEDULE";

    private ThreadPoolExecutor executor;
    private Timer timer;
    private boolean running = false;
    private LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
    private Thread thread;

    // simplest level...

    public Scheduler() {
        executor = new ThreadPoolExecutor(5, 5, 5000, TimeUnit.MILLISECONDS, tasks);
        timer = new Timer();        
    }

    public void start() {
        running = true;        
        long nextMinute = System.currentTimeMillis() / ONE_MINUTE * ONE_MINUTE + ONE_MINUTE;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                scheduleTasks();
            }
        }, new Date(nextMinute), ONE_MINUTE);
        launchThread();
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
                executeScheduledTasks();
            }
        };
        thread.start();
    }

    public List<IGenerator> getGenerators() {
        return Arrays.asList();
    }

    private void scheduleTasks() {
        boolean locked = false;
        locked = obtainLock(locked);
        if (locked) {
            try {
                Date now = new Date();
                // retrieve the task generators
                Session session = Database.instance().createSession();
                try {
                    session.getTransaction().begin();
                    List<TaskGenerator> taskGenerators = retrieveTaskGenerators(session);
                    for (TaskGenerator taskGenerator : taskGenerators) {
                        List<ScheduledTask> tasks = taskGenerator.generateTasks(now);
                        taskGenerator.setLastTaskDate(now);
                        for (ScheduledTask task : tasks) {
                            session.save(task);
                        }
                    }
                    session.getTransaction().commit();
                } catch (Exception e) {
                    session.getTransaction().rollback();
                } finally {
                    session.close();
                }
            } finally {
                releaseLock();
            }
        }
    }

    private List<TaskGenerator> retrieveTaskGenerators(Session session) {
        return session.createQuery("from TaskGenerator").list();
    }

    private void releaseLock() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement lockStmt = conn.prepareStatement("DELETE FROM DISTRIBUTED_LOCK WHERE LOCK_NAME = ?");
            lockStmt.setString(1, SCHEDULE_LOCK);
            lockStmt.executeUpdate();
        } catch (SQLException e) {
            // we didn't get lock
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    private boolean obtainLock(boolean locked) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement lockStmt = conn.prepareStatement("INSERT INTO DISTRIBUTED_LOCK (LOCK_NAME) VALUES (?)");
            lockStmt.setString(1, SCHEDULE_LOCK);
            lockStmt.execute();
            locked = true;
        } catch (SQLException e) {
            // we didn't get lock
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return locked;
    }

    public void executeScheduledTasks() {
        // retrieve the set of tasks which are in the SCHEDULED state, limit N
        // add them to the thread pool
        while (running) {
            List<ScheduledTask> tasks = claimScheduledTasks();
            if (tasks.isEmpty()) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                    // we don't care
                }
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
}

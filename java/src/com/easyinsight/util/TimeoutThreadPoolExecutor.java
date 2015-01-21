package com.easyinsight.util;

import com.easyinsight.analysis.AsyncRequestRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.*;

public class TimeoutThreadPoolExecutor extends ThreadPoolExecutor {
    private final long timeout;
    private final TimeUnit timeoutUnit;

    private final ScheduledExecutorService timeoutExecutor = Executors.newSingleThreadScheduledExecutor();
    private final ConcurrentMap<Runnable, ScheduledFuture> runningTasks = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, Runnable> tasksByRequestID = new ConcurrentHashMap<>();

    public TimeoutThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, long timeout, TimeUnit timeoutUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public TimeoutThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, long timeout, TimeUnit timeoutUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public TimeoutThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler, long timeout, TimeUnit timeoutUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    public TimeoutThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler, long timeout, TimeUnit timeoutUnit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
    }

    @Override
    public void shutdown() {
        timeoutExecutor.shutdown();
        super.shutdown();
    }

    @NotNull
    @Override
    public List<Runnable> shutdownNow() {
        timeoutExecutor.shutdownNow();
        return super.shutdownNow();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        if(timeout > 0) {
            final ScheduledFuture<?> scheduled = timeoutExecutor.schedule(new TimeoutTask(t), timeout, timeoutUnit);
            runningTasks.put(r, scheduled);
            if (r instanceof AsyncRequestRunnable) {
                AsyncRequestRunnable asyncRequestRunnable = (AsyncRequestRunnable) r;
                tasksByRequestID.put(asyncRequestRunnable.getRequestID(), r);
            }
        }
    }

    public void cancelRequest(long requestID) {
        Runnable r = tasksByRequestID.remove(requestID);
        ScheduledFuture timeoutTask = runningTasks.remove(r);
        if(timeoutTask != null) {
            timeoutTask.cancel(false);
        }
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        ScheduledFuture timeoutTask = runningTasks.remove(r);
        if(timeoutTask != null) {
            timeoutTask.cancel(false);
        }
        if (r instanceof AsyncRequestRunnable) {
            AsyncRequestRunnable asyncRequestRunnable = (AsyncRequestRunnable) r;
            tasksByRequestID.remove(asyncRequestRunnable.getRequestID());
        }
    }

    class TimeoutTask implements Runnable {
        private final Thread thread;

        public TimeoutTask(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            thread.interrupt();
        }
    }
}

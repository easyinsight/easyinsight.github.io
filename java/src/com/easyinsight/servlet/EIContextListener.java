package com.easyinsight.servlet;

import com.easyinsight.admin.HealthListener;
import com.easyinsight.analysis.CurrencyRetrieval;
import com.easyinsight.analysis.ReportCache;
import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.migration.Migrations;
import com.easyinsight.datafeeds.CacheTimer;
import com.easyinsight.datafeeds.DataSourceTypeRegistry;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.database.DatabaseListener;
import com.easyinsight.datafeeds.migration.MigrationManager;
import com.easyinsight.eventing.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.scheduler.Scheduler;
import com.easyinsight.scorecard.LongKPIRefreshEvent;
import com.easyinsight.scorecard.LongKPIRefreshListener;
import com.easyinsight.security.DefaultSecurityProvider;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DatabaseManager;
import com.easyinsight.util.ServiceUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

/**
 * User: jamesboe
 * Date: 10/7/13
 * Time: 7:24 PM
 */
public class EIContextListener implements ServletContextListener {

    public static final long HOUR = 1000 * 60 * 60;

    private Scheduler scheduler;
    private HealthListener healthListener;

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            LogClass.info("Starting the core Easy Insight server...");
            if (Database.instance() == null) {
                SecurityUtil.setSecurityProvider(new DefaultSecurityProvider());
                Database.initialize();
                SystemSettings.initialize();
                ServiceUtil.initialize();

                CurrencyRetrieval.initialize();
                if (ConfigLoader.instance().isDatabaseListener()) {
                    DatabaseListener.initialize();
                }
                new Migrations().migrate();
                // create schedulers...

                DatabaseManager.instance();
                DataSourceTypeRegistry dataSourceTypeRegistry = new DataSourceTypeRegistry();
                MigrationManager migrationManager = new MigrationManager();
                migrationManager.setDataSourceTypeRegistry(dataSourceTypeRegistry);
                migrationManager.migrate();
                FeedRegistry.initialize();
                ReportCache.initialize();
                if (ConfigLoader.instance().isTaskRunner()) {
                    Scheduler.initialize();
                    scheduler = Scheduler.instance();
                }
                EventDispatcher.instance().start();
                EventDispatcher.instance().registerListener(AsyncCreatedEvent.ASYNC_CREATED, new AsyncCreatedListener());
                EventDispatcher.instance().registerListener(AsyncRunningEvent.ASYNC_RUNNING, new AsyncRunningListener());
                EventDispatcher.instance().registerListener(AsyncCompletedEvent.ASYNC_COMPLETED, new AsyncCompletedListener());
                EventDispatcher.instance().registerListener(LongKPIRefreshEvent.LONG_KPI_REFRESH_EVENT, LongKPIRefreshListener.instance());
                if (ConfigLoader.instance().isTaskRunner()) {
                    scheduler.start();
                }
                try {
                    MemCachedManager.initialize();
                    //MemCachedManager.flush();
                } catch (Exception e) {
                    LogClass.error(e);
                }
                healthListener = new HealthListener();
                healthThread = new Thread(healthListener);
                healthThread.setName("Health Listener");
                healthThread.setDaemon(true);
                healthThread.start();
                if (ConfigLoader.instance().isDatabaseListener()) {
                    CacheTimer.initialize();
                    CacheTimer.instance().start();
                }
            }
            LogClass.info("Started the server.");
        } catch (Throwable e) {
            LogClass.error(e);
        }
    }

    private Thread healthThread;

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LogClass.info("Shutting down...");

        CacheTimer.instance().stop();
        try {
            MemCachedManager.instance().shutdown();
        } catch (Exception e) {
            LogClass.error(e);
        }

        if (DatabaseListener.instance() != null) {
            DatabaseListener.instance().stop();
        }
        if (scheduler != null) {
            scheduler.stop();
        }
        try {
            healthListener.stop();
            healthThread.interrupt();
        } catch (Exception e) {
            LogClass.error(e);
        }
        EventDispatcher.instance().setRunning(false);
        EventDispatcher.instance().interrupt();
        SystemSettings.instance().stop();
        Database.instance().shutdown();

        DatabaseManager.instance().shutdown();



        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (Exception e) {
                LogClass.error(e);
            }

        }

        LogClass.info("Servlet shutdown complete...");
    }
}
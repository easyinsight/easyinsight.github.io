package com.easyinsight.database;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.cfg.AnnotationConfiguration;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.text.MessageFormat;

import com.easyinsight.logging.LogClass;
import com.easyinsight.config.ConfigLoader;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 12:50:36 PM
 */
public class Database {

    public static final int CURRENT_VERSION = 61;

    private ComboPooledDataSource dataSource;
    private SessionFactory sessionFactory;
    private static Database instance;
    private String id;
    private boolean addHibernate;

    private String urlTemplate = "jdbc:mysql://{0}:{1}/{2}";

    private Database(String host, String port, String databaseName, String userName, String password, boolean addHibernate, String id) {
        dataSource = setupDataSource(host, port, databaseName, userName, password);
        this.id = id;
        this.addHibernate = addHibernate;
        if (addHibernate) {
            try {
                AnnotationConfiguration configuration = new AnnotationConfiguration().configure();
                String url = MessageFormat.format(urlTemplate, host, port, databaseName);
                configuration.setProperty("hibernate.connection.url", url);
                configuration.setProperty("hibernate.connection.username", userName);
                configuration.setProperty("hibernate.connection.password", password);
                sessionFactory = configuration.buildSessionFactory();
                Thread thread = new Thread(new KeepAliveThread(this));
                thread.setDaemon(true);
                thread.start();
            } catch (Throwable e) {
                LogClass.error(e);
            }
        }
    }

    public String getID() {
        return this.id;
    }

    public static Database instance() {
        return instance;
    }

    public static Database create(String host, String port, String databaseName, String userName, String password, String id) {
        return new Database(host, port, databaseName, userName, password, false, id);
    }

    public static void initialize() {
        if (instance == null) {
            instance = new Database(ConfigLoader.instance().getDatabaseHost(), ConfigLoader.instance().getDatabasePort(),
                ConfigLoader.instance().getDatabaseName(), ConfigLoader.instance().getDatabaseUserName(),
                ConfigLoader.instance().getDatabasePassword(), true, "Core");
        }
    }

    public Session createSession() {
        if (!addHibernate) {
            throw new UnsupportedOperationException("This is a storage database, with no Hibernate.");
        }
        return sessionFactory.openSession();
    }

    public Session createSession(Connection conn) {
        if (!addHibernate) {
            throw new UnsupportedOperationException("This is a storage database, with no Hibernate.");
        }
        return sessionFactory.openSession(conn);
    }

    public StatelessSession createStatelessSession() {
        if (!addHibernate) {
            throw new UnsupportedOperationException("This is a storage database, with no Hibernate.");
        }
        return sessionFactory.openStatelessSession();
    }

    public EIConnection getConnection() {
        try {
            return new EIConnection(dataSource.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private ComboPooledDataSource setupDataSource(String host, String port, String databaseName, String userName, String password) {
        /*connectionPool = new GenericObjectPool(null);

        connectionPool.setMinIdle(5);
        connectionPool.setMaxActive(20);

        connectionPool.setTestOnBorrow(true);*/

        String url = MessageFormat.format(urlTemplate, host, port, databaseName);

        /*ConnectionFactory connectionFactory =
        	new DriverManagerConnectionFactory(url, userName, password);

       new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);*/

        ComboPooledDataSource source = new ComboPooledDataSource();
        source.setJdbcUrl(url);
        source.setUser(userName);
        source.setPassword(password);
        return source;

        //return new PoolingDataSource(connectionPool);
    }

    public void shutdown() {
        try {
            dataSource.close();
            //connectionPool.close();
            if (sessionFactory != null) {
                sessionFactory.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public long getAutoGenKey(Statement stmt) {
        try {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            } else {
                throw new RuntimeException("No entry found in result set for autogenerated key");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getActiveConnections() {
        try {
            return dataSource.getNumConnections();
        } catch (SQLException e) {
            LogClass.error(e);
            return -1;
        }
    }

    public int getIdleConnections() {
        try {
            return dataSource.getNumIdleConnectionsAllUsers();
        } catch (SQLException e) {
            LogClass.error(e);
            return -1;
        }
    }

    public int getMaxConnections() {
        return dataSource.getMaxPoolSize();
    }

    public static Object deproxy(Object obj) {
        Hibernate.initialize(obj);

        if (obj == null) {
            return null;
        }

        if (HibernateProxy.class.isInstance(obj)) {
            HibernateProxy proxy = (HibernateProxy) obj;
            return proxy.getHibernateLazyInitializer().getImplementation();
        }

        return obj;
    }
}

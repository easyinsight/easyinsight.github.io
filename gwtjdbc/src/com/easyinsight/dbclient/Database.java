package com.easyinsight.dbclient;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 * User: jamesboe
 * Date: Apr 12, 2010
 * Time: 5:17:05 PM
 */
public class Database {

    private SessionFactory sessionFactory;
    private static Database instance;

    public Database() {
        instance = this;
        Configuration configuration = new AnnotationConfiguration().configure();
        sessionFactory = configuration.buildSessionFactory();
    }

    public void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static Database instance() {
        return instance;
    }

    public void createSchema() {
        if (!schemaDefined()) {
            defineNewSchema();
        } else {
            upgradeSchema();
        }
    }

    public Session getSession() {
        return sessionFactory.openSession();
    }

    private void upgradeSchema() {
    }

    private void defineNewSchema() {
    }

    private boolean schemaDefined() {
        return false;
    }
}

package com.easyinsight.connections.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.apache.derby.jdbc.EmbeddedDriver;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 9:37:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class DataConnection {

    public static SessionFactory factory = createFactory();

    public static SessionFactory createFactory() {
        AnnotationConfiguration conf = new AnnotationConfiguration();
        conf.configure();
        factory = conf.buildSessionFactory();
        return factory;
    }

    public static Session getSession() throws SQLException {
        return factory.openSession();
    }
    
}

package com.easyinsight.connections.database.data;

import com.easyinsight.connections.database.DataConnection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.xml.ws.BindingProvider;

import org.hibernate.Session;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 27, 2010
 * Time: 8:59:56 AM
 */
@Entity
public class EIUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    private String publicKey;
    private String secretKey;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public boolean validateCredentials() {
        try {
            // TODO: make an API call here
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }


    private static EIUser instance;
    public static EIUser instance() throws SQLException {
        Session session = DataConnection.getSession();
        try {
            List vals = session.createQuery("from EIUser").list();
            if(vals.size() == 0)
                return null;
            else
                return (EIUser) vals.get(0);
        }
        finally {
            session.close();
        }
    }
}

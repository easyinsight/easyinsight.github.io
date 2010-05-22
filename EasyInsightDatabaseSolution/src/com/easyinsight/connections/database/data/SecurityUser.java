package com.easyinsight.connections.database.data;

import com.easyinsight.util.Base64;
import com.easyinsight.connections.database.DataConnection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 11, 2010
 * Time: 2:00:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class SecurityUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void createPassword(String unencryptedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            password = Base64.encodeBytes(md.digest(unencryptedPassword.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public boolean matchPassword(String unencryptedPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return password.equals(Base64.encodeBytes(md.digest(unencryptedPassword.getBytes("UTF-8"))));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long count() {
        Session s = null;
        try {
            s = DataConnection.getSession();
            return s.createQuery("from SecurityUser").list().size();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(s != null)
                s.close();
        }
        return -1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}

package com.easyinsight;

import com.easyinsight.users.Credentials;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.security.*;
import java.security.cert.CertificateException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;

import org.bouncycastle.util.encoders.Base64;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: May 6, 2009
 * Time: 3:26:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class PasswordStorage {

    private static KeyStore encryptStore;
    private static PrivateKey privateKey;
    private static PublicKey publicKey;


    private static String password = "dmskey";

    static {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream is = PasswordStorage.class.getClassLoader().getResourceAsStream("encrypt.jks");
            ks.load(is, password.toCharArray());
            encryptStore = ks;
            KeyStore.PrivateKeyEntry k = (KeyStore.PrivateKeyEntry) encryptStore.getEntry("dbencrypt", new KeyStore.PasswordProtection(password.toCharArray()));
            privateKey = k.getPrivateKey();
            publicKey = k.getCertificate().getPublicKey();
        } catch (Exception e) {
            LogClass.error(e);
            encryptStore = null;
            privateKey = null;
        }
    }

    public static void setSessionTicket(String sessionTicket, long feedId, Connection conn) throws SQLException {
        String encryptedTicket = encryptString(sessionTicket);

        PreparedStatement updateStatement = conn.prepareStatement("UPDATE session_id_storage set session_id = ? WHERE data_feed_id = ?");
        updateStatement.setString(1, encryptedTicket);
        updateStatement.setLong(2, feedId);

        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO session_id_storage(data_feed_id, session_id) VALUES (?,?)");
        insertStatement.setLong(1, feedId);
        insertStatement.setString(2, encryptedTicket);

        int rows = updateStatement.executeUpdate();
        if(rows == 0)
            insertStatement.execute();
        new FeedStorage().removeFeed(feedId);
    }

    public static void clearSessionTicket(long feedId, Connection conn) throws SQLException {
        PreparedStatement deleteStatement = conn.prepareStatement("DELETE from SESSION_ID_STORAGE WHERE data_feed_id = ?");
        deleteStatement.setLong(1, feedId);
        deleteStatement.execute();
        new FeedStorage().removeFeed(feedId);
    }

    public static String getSessionTicket(long feedId, Connection conn) throws SQLException {
        PreparedStatement selectStmt = conn.prepareStatement("SELECT session_id from session_id_storage WHERE data_feed_id = ?");
        selectStmt.setLong(1, feedId);
        ResultSet rs = selectStmt.executeQuery();
        String decodedUsername = null;
        if(rs.next()) {
            String username = rs.getString(1);
            decodedUsername = decryptString(username);
        }

        return decodedUsername;
    }

    public static void setPasswordCredentials(String username, String password, long feedId, Connection conn) throws SQLException {

            String encryptedUsername = encryptString(username);
            String encryptedPassword = encryptString(password);


            PreparedStatement updateStatement = conn.prepareStatement("UPDATE PASSWORD_STORAGE SET username = ?, password = ? WHERE data_feed_id = ?");
            updateStatement.setString(1, encryptedUsername);
            updateStatement.setString(2, encryptedPassword);
            updateStatement.setLong(3, feedId);

            PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO password_storage(data_feed_id, username, password) VALUES(?,?,?)");
            insertStatement.setLong(1, feedId);
            insertStatement.setString(2, encryptedUsername);
            insertStatement.setString(3, encryptedPassword);

            int rows = updateStatement.executeUpdate();
            if(rows == 0)
                insertStatement.execute();
            new FeedStorage().removeFeed(feedId);
    }

    public static void clearPasswordCredentials(long feedId, Connection conn) throws SQLException {
        PreparedStatement deleteStatement = conn.prepareStatement("DELETE from PASSWORD_STORAGE WHERE data_feed_id = ?");
        deleteStatement.setLong(1, feedId);
        deleteStatement.execute();
        new FeedStorage().removeFeed(feedId);
    }

    public static String encryptString(String username) {
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            String encryptedUsername = new String(Base64.encode(c.doFinal(username.getBytes("UTF-8"))));
            return encryptedUsername;
        }
        catch(Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static Credentials getPasswordCredentials(long feedId, Connection conn) throws SQLException {
        PreparedStatement selectStmt = conn.prepareStatement("SELECT username, password from password_storage WHERE data_feed_id = ?");
        selectStmt.setLong(1, feedId);
        ResultSet rs = selectStmt.executeQuery();
        if(rs.next()) {
            Credentials credentials = new Credentials();
            String username = rs.getString(1);
            String password = rs.getString(2);
            Cipher cipher = null;
                String decodedUsername = decryptString(username);
                String decodedPassword = decryptString(password);
                credentials.setUserName(decodedUsername);
                credentials.setPassword(decodedPassword);

            return credentials;
        }

        return null;
    }

    public static String decryptString(String username) {
        try {
            Cipher cipher;
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            String decodedUsername = new String(cipher.doFinal(Base64.decode(username.getBytes("UTF-8"))));
            return decodedUsername;
        }
        catch(Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

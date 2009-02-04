package com.easyinsight.dbservice;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jan 31, 2009
 * Time: 6:58:15 PM
 */
public abstract class DBConfiguration {
    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
    public abstract String validate();
    public abstract void save(Connection conn) throws SQLException, StringEncrypter.EncryptionException;

    public abstract String getType();

    public abstract void load(Connection conn) throws SQLException, StringEncrypter.EncryptionException;
}

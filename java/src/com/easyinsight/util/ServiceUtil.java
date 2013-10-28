package com.easyinsight.util;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 9:04 AM
 */
public class ServiceUtil {

    public static final int RUNNING = 1;
    public static final int DONE = 2;
    public static final int FAILED = 3;

    private static ServiceUtil instance;

    public static void initialize() {
        instance = new ServiceUtil();
    }

    public static ServiceUtil instance() {
        return instance;
    }

    public String longRunningCall(long itemID) {
        String callID = itemID + "-" + SecurityUtil.getUserID() + "-" + System.currentTimeMillis();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO refresh_connection_cache (call_data_id, status) values (?, ?)");
            insertStmt.setString(1, callID);
            insertStmt.setInt(2, RUNNING);
            insertStmt.execute();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        //MemCachedManager.add(callID, 10000, new CallData());
        return callID;
    }

    public void updateStatusMessage(String callDataID, String statusMessage) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("UPDATE refresh_connection_cache set message = ? where call_data_id = ?");
            insertStmt.setString(1, statusMessage);
            insertStmt.setString(2, callDataID);
            insertStmt.execute();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateStatus(String callDataID, int status) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("UPDATE refresh_connection_cache set status = ? where call_data_id = ?");
            insertStmt.setInt(1, status);
            insertStmt.setString(2, callDataID);
            insertStmt.executeUpdate();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateResult(String callDataID, Object result) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("UPDATE refresh_connection_cache set result_object = ? where call_data_id = ?");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(result);
            oos.flush();
            insertStmt.setBytes(1, baos.toByteArray());
            insertStmt.setString(2, callDataID);
            insertStmt.executeUpdate();
            insertStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateStatus(String callDataID, int status, Object result) {
        updateStatus(callDataID, status);
        updateResult(callDataID, result);
    }

    public void updateStatus(String callDataID, int status, String message) {
        updateStatus(callDataID, status);
        updateStatusMessage(callDataID, message);
    }

    public CallData getCallData(String callDataID) {
        CallData callData = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT message, status, result_object FROM refresh_connection_cache where call_data_id = ?");
            queryStmt.setString(1, callDataID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String message = rs.getString(1);
                int status = rs.getInt(2);
                callData = new CallData();
                callData.setStatusMessage(message);
                callData.setStatus(status);
                byte[] bytes = rs.getBytes(3);
                if (bytes != null) {
                    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    callData.setResult(ois.readObject());
                }
            }
            queryStmt.close();
            if (callData == null) {
                return null;
            }
            if (callData.getStatus() == DONE || callData.getStatus() == FAILED) {
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM refresh_connection_cache WHERE call_data_id = ?");
                deleteStmt.setString(1, callDataID);
                deleteStmt.executeUpdate();
                deleteStmt.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }

        return callData;
    }
}
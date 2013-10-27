package com.easyinsight.util;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.security.SecurityUtil;

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
        MemCachedManager.add(callID, 10000, new CallData());
        return callID;
    }

    public void updateStatusMessage(String callDataID, String statusMessage) {
        CallData callData = (CallData) MemCachedManager.instance().get(callDataID);
        if (callData == null) {
            callData = new CallData();
        }
        callData.setStatusMessage(statusMessage);
        MemCachedManager.instance().delete(callDataID);
        MemCachedManager.instance().add(callDataID, 10000, callData);
    }

    public void updateStatus(String callDataID, int status) {
        CallData callData = (CallData) MemCachedManager.instance().get(callDataID);
        if (callData == null) {
            callData = new CallData();
        }
        callData.setStatus(status);
        MemCachedManager.instance().delete(callDataID);
        MemCachedManager.instance().add(callDataID, 10000, callData);
    }

    public void updateResult(String callDataID, Object result) {
        CallData callData = (CallData) MemCachedManager.instance().get(callDataID);
        if (callData == null) {
            callData = new CallData();
        }
        callData.setResult(result);
        MemCachedManager.instance().delete(callDataID);
        MemCachedManager.instance().add(callDataID, 10000, callData);
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
        CallData callData = (CallData) MemCachedManager.instance().get(callDataID);
        if (callData == null) {
            return null;
        }
        if (callData.getStatus() == DONE || callData.getStatus() == FAILED) {
            MemCachedManager.instance().delete(callDataID);
        }
        return callData;
    }
}
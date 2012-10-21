package com.easyinsight.util;

import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 9:04 AM
 */
public class ServiceUtil {

    //private Map<Long, Map<String, CallData>> callDataMap = new HashMap<Long, Map<String, CallData>>();

    private JCS callDataMap = getCache("serviceUtil");

    private JCS getCache(String cacheName) {

        try {
            return JCS.getInstance(cacheName);
        } catch (Exception e) {
            LogClass.error(e);
        }
        return null;
    }

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

    public void establishCount(String callDataID, int count) {
        CallData callData = get(callDataID);
        callData.setRequiredCount(count);
        refreshCache(callDataID);
    }

    public String longRunningCall(long itemID) {
        String callID = itemID + "-" + SecurityUtil.getUserID() + "-" + System.currentTimeMillis();
        try {
            callDataMap.put(callID, new CallData());
        } catch (CacheException e) {
            LogClass.error(e);
        }
        return callID;
    }

    private CallData get(String callDataID) {
        return (CallData) callDataMap.get(callDataID);
    }

    public void updateStatusMessage(String callDataID, String statusMessage) {
        get(callDataID).setStatusMessage(statusMessage);
        refreshCache(callDataID);
    }

    public void updateStatus(String callDataID, int status) {
        get(callDataID).setStatus(status);
        refreshCache(callDataID);
    }

    private void refreshCache(String callDataID) {
        CallData callData = get(callDataID);
        try {
            callDataMap.put(callDataID, callData);
        } catch (CacheException e) {
            LogClass.error(e);
        }
    }

    public void incrementDone(String callDataID) {
        CallData callData = get(callDataID);
        callData.incrementCount();
        refreshCache(callDataID);
    }

    public void updateStatus(String callDataID, int status, Object result) {
        get(callDataID).setResult(result);
        get(callDataID).setStatus(status);
        refreshCache(callDataID);
    }

    public void updateStatus(String callDataID, int status, String message) {
        get(callDataID).setStatusMessage(message);
        get(callDataID).setStatus(status);
        refreshCache(callDataID);
    }

    public CallData getCallData(String callDataID) {
        //System.out.println("retrieving call data for " + callDataID);
        CallData callData = get(callDataID);
        if (callData == null) {
            return null;
        }
        if (callData.getStatus() == DONE || callData.getStatus() == FAILED) {
            try {
                callDataMap.remove(callDataID);
            } catch (CacheException e) {
                LogClass.error(e);
            }
        }
        return callData;
    }
}

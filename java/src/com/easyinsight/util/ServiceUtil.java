package com.easyinsight.util;

import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.apache.jcs.JCS;
import org.apache.jcs.access.exception.CacheException;

import java.util.HashMap;
import java.util.Map;

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

    public String longRunningCall(long itemID) {
        String callID = itemID + "-" + SecurityUtil.getUserID() + "-" + System.currentTimeMillis();
        Map<String, CallData> callMap = (Map<String, CallData>) callDataMap.get(SecurityUtil.getUserID());
        if (callMap == null) {
            callMap = new HashMap<String, CallData>();
            try {
                callDataMap.put(SecurityUtil.getUserID(), callMap);
            } catch (CacheException e) {
                throw new RuntimeException(e);
            }
        }
        callMap.put(callID, new CallData());
        return callID;
    }

    private Map<String, CallData> get() {
        return (Map<String, CallData>) callDataMap.get(SecurityUtil.getUserID());
    }

    public void updateStatusMessage(String callDataID, String statusMessage) {
        get().get(callDataID).setStatusMessage(statusMessage);
    }

    public void updateStatus(String callDataID, int status) {
        get().get(callDataID).setStatus(status);
    }

    public void updateStatus(String callDataID, int status, Object result) {
        get().get(callDataID).setResult(result);
        get().get(callDataID).setStatus(status);
    }

    public void updateStatus(String callDataID, int status, String message) {
        get().get(callDataID).setStatusMessage(message);
        get().get(callDataID).setStatus(status);
    }

    public CallData getCallData(String callDataID) {
        System.out.println("retrieving call data for " + callDataID);
        CallData callData = get().get(callDataID);
        if (callData == null) {
            return null;
        }
        if (callData.getStatus() == DONE || callData.getStatus() == FAILED) {
            get().remove(callDataID);
        }
        return callData;
    }
}
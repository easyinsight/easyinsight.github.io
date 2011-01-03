package com.easyinsight.util;

import com.easyinsight.security.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 1/3/11
 * Time: 9:04 AM
 */
public class ServiceUtil {

    private Map<Long, Map<String, CallData>> callDataMap = new HashMap<Long, Map<String, CallData>>();

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
        Map<String, CallData> callMap = callDataMap.get(SecurityUtil.getUserID());
        if (callMap == null) {
            callMap = new HashMap<String, CallData>();
            callDataMap.put(SecurityUtil.getUserID(), callMap);
        }
        callMap.put(callID, new CallData());
        return callID;
    }

    public void updateStatusMessage(String callDataID, String statusMessage) {
        callDataMap.get(SecurityUtil.getUserID()).get(callDataID).setStatusMessage(statusMessage);
    }

    public void updateStatus(String callDataID, int status) {
        callDataMap.get(SecurityUtil.getUserID()).get(callDataID).setStatus(status);
    }

    public void updateStatus(String callDataID, int status, Object result) {
        callDataMap.get(SecurityUtil.getUserID()).get(callDataID).setStatus(status);
        callDataMap.get(SecurityUtil.getUserID()).get(callDataID).setResult(result);
    }

    public CallData getCallData(String callDataID) {
        CallData callData = callDataMap.get(SecurityUtil.getUserID()).get(callDataID);
        if (callData.getStatus() == DONE || callData.getStatus() == FAILED) {
            callDataMap.get(SecurityUtil.getUserID()).remove(callDataID);
        }
        return callData;
    }
}

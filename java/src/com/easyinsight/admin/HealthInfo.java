package com.easyinsight.admin;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Aug 17, 2008
 * Time: 1:49:49 PM
 */
public class HealthInfo implements Serializable {

    private static final String HEALTH_XML = "<health>\r\n<currentMemory>{0}</currentMemory>\r\n" +
            "<freeMemory>{1}</freeMemory>\r\n<maxMemory>{2}</maxMemory>\r\n<threadCount>{3}</threadCount>\r\n" +
            "<activeDBConnections>{4}</activeDBConnections>\r\n<activeUsers>{5}</activeUsers>\r\n<systemLoadAverage>{6}</systemLoadAverage>\r\n" +
            "<clientCount>{7}</clientCount>\r\n<server>{8}</server>\r\n</health>\r\n";

    private long currentMemory;
    private long freeMemory;
    private long maxMemory;
    private int threadCount;
    private int activeDBConnections;
    private int idleDBConnections;
    private int activeUsers;
    private long majorCollectionCount;
    private long majorCollectionTime;
    private long minorCollectionCount;
    private long minorCollectionTime;
    private double systemLoadAverage;
    private long compilationTime;
    private int clientCount;
    private String server;

    public String toXML() {
        return MessageFormat.format(HEALTH_XML, currentMemory, freeMemory, maxMemory, threadCount, activeDBConnections,
                activeUsers, systemLoadAverage, clientCount, server);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(long maxMemory) {
        this.maxMemory = maxMemory;
    }

    public int getClientCount() {
        return clientCount;
    }

    public void setClientCount(int clientCount) {
        this.clientCount = clientCount;
    }

    public long getCompilationTime() {
        return compilationTime;
    }

    public void setCompilationTime(long compilationTime) {
        this.compilationTime = compilationTime;
    }

    public long getMajorCollectionCount() {
        return majorCollectionCount;
    }

    public void setMajorCollectionCount(long majorCollectionCount) {
        this.majorCollectionCount = majorCollectionCount;
    }

    public long getMajorCollectionTime() {
        return majorCollectionTime;
    }

    public void setMajorCollectionTime(long majorCollectionTime) {
        this.majorCollectionTime = majorCollectionTime;
    }

    public long getMinorCollectionCount() {
        return minorCollectionCount;
    }

    public void setMinorCollectionCount(long minorCollectionCount) {
        this.minorCollectionCount = minorCollectionCount;
    }

    public long getMinorCollectionTime() {
        return minorCollectionTime;
    }

    public void setMinorCollectionTime(long minorCollectionTime) {
        this.minorCollectionTime = minorCollectionTime;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    public void setSystemLoadAverage(double systemLoadAverage) {
        this.systemLoadAverage = systemLoadAverage;
    }

    public long getCurrentMemory() {
        return currentMemory;
    }

    public void setCurrentMemory(long currentMemory) {
        this.currentMemory = currentMemory;
    }

    public long getFreeMemory() {
        return freeMemory;
    }

    public void setFreeMemory(long freeMemory) {
        this.freeMemory = freeMemory;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getActiveDBConnections() {
        return activeDBConnections;
    }

    public void setActiveDBConnections(int activeDBConnections) {
        this.activeDBConnections = activeDBConnections;
    }

    public int getIdleDBConnections() {
        return idleDBConnections;
    }

    public void setIdleDBConnections(int idleDBConnections) {
        this.idleDBConnections = idleDBConnections;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }
}

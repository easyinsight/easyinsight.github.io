package com.easyinsight.admin;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.management.JMX;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.management.ThreadInfo;
import java.lang.management.GarbageCollectorMXBean;

import flex.management.runtime.messaging.client.FlexClientManagerControlMBean;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 10:57:40 AM
 */
public class AdminService {

    

    public void threadDump() {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(true, true);
        for (ThreadInfo threadInfo : threadInfos) {
            LogClass.debug(threadInfo.toString());
        }
    }

    public HealthInfo getHealthInfo() {
        try {
            HealthInfo healthInfo = new HealthInfo();
            healthInfo.setActiveDBConnections(Database.instance().getActiveConnections());
            healthInfo.setIdleDBConnections(Database.instance().getIdleConnections());
            Runtime runtime = Runtime.getRuntime();
            long maxMemory = runtime.maxMemory();
            long totalAllocatedMemory = runtime.totalMemory();
            long freeUnallocated = maxMemory - totalAllocatedMemory;
            long freeMemory = runtime.freeMemory() + freeUnallocated;
            healthInfo.setFreeMemory(freeMemory);
            long currentMemory = maxMemory - freeMemory;
            healthInfo.setCurrentMemory(currentMemory);
            ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            healthInfo.setThreadCount(threadMXBean.getThreadCount());
            healthInfo.setSystemLoadAverage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            healthInfo.setCompilationTime(ManagementFactory.getCompilationMXBean().getTotalCompilationTime());
            for (GarbageCollectorMXBean garbageBean : ManagementFactory.getGarbageCollectorMXBeans()) {
                if ("Copy".equals(garbageBean.getName())) {
                    healthInfo.setMinorCollectionCount(garbageBean.getCollectionCount());
                    healthInfo.setMinorCollectionTime(garbageBean.getCollectionTime());
                } else {
                    healthInfo.setMajorCollectionCount(garbageBean.getCollectionCount());
                    healthInfo.setMajorCollectionTime(garbageBean.getCollectionTime());
                }
            }
            MBeanServer platformServer = ManagementFactory.getPlatformMBeanServer();
            FlexClientManagerControlMBean mb = JMX.newMBeanProxy(platformServer, new ObjectName("flex.runtime.app:type=MessageBroker.FlexClientManager,MessageBroker=MessageBroker1,id=FlexClientManager"),
                    FlexClientManagerControlMBean.class);
            healthInfo.setClientCount(mb.getFlexClientCount());
            return healthInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

    }
}

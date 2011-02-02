package com.easyinsight.admin;

import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import flex.management.runtime.messaging.client.FlexClientManagerControlMBean;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.InetAddress;

/**
 * User: jamesboe
 * Date: Apr 13, 2010
 * Time: 11:01:32 AM
 */
public class AdminProcessor {
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
            healthInfo.setMaxMemory(maxMemory);
            healthInfo.setServer(InetAddress.getLocalHost().getHostName());
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
            try {
                FlexClientManagerControlMBean mb = JMX.newMBeanProxy(platformServer, new ObjectName("flex.runtime.app:type=MessageBroker.FlexClientManager,MessageBroker=MessageBroker1,id=FlexClientManager"),
                        FlexClientManagerControlMBean.class);
                healthInfo.setClientCount(mb.getFlexClientCount());
            } catch (Exception e) {
                //LogClass.error(e);
            }
            return healthInfo;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

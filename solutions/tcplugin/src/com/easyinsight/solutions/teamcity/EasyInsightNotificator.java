package com.easyinsight.solutions.teamcity;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.vcs.VcsRoot;

import java.util.*;
import java.io.IOException;

import org.jetbrains.annotations.NotNull;
import com.easyinsight.solutions.teamcity.webservice.*;

import javax.xml.rpc.ServiceException;

/**
 * User: James Boe
 * Date: Jan 28, 2009
 * Time: 9:55:45 PM
 */
public class EasyInsightNotificator implements Notificator {

    private static final String TYPE = "easyInsightNotifier";
    private static final String TYPE_NAME = "Easy Insight Metrics Notifier";
    private static final String EI_LOGIN = "easyInsightLogin";
    private static final String EI_PASSWORD = "easyInsightPassword";

    private static final PropertyKey EI_LOGIN_KEY = new NotificatorPropertyKey(TYPE, EI_LOGIN);
    private static final PropertyKey EI_PASSWORD_KEY = new NotificatorPropertyKey(TYPE, EI_PASSWORD);


    public EasyInsightNotificator(NotificatorRegistry notificatorRegistry) throws IOException {
        List<UserPropertyInfo> userProps = new ArrayList<UserPropertyInfo>();
        userProps.add(new UserPropertyInfo(EI_LOGIN, "Easy Insight ID"));
        userProps.add(new UserPropertyInfo(EI_PASSWORD, "Easy Insight Password"));
        notificatorRegistry.register(this, userProps);
    }


    public void notifyBuildStarted(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
    }

    public void notifyBuildSuccessful(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        emitStats(sRunningBuild, sUsers);
    }

    private void emitStats(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        for (SUser user : sUsers) {
            String eiUserName = user.getPropertyValue(EI_LOGIN_KEY);
            String eiPassword = user.getPropertyValue(EI_PASSWORD_KEY);
            BuildStatistics buildStatistics = sRunningBuild.getFullStatistics();
            if (buildStatistics.getAllTestCount() > 0) {
                try {
                    UncheckedPublishService service = new BasicAuthUncheckedPublishServiceServiceLocator().getBasicAuthUncheckedPublishServicePort();
                    ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setUsername(eiUserName);
                    ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setPassword(eiPassword);
                    Row row = new Row();
                    StringPair stringPair = new StringPair();
                    stringPair.setKey("Project");
                    stringPair.setValue(sRunningBuild.getProjectId());
                    NumberPair allTestCountPair = new NumberPair();
                    allTestCountPair.setKey("All Test Count");
                    allTestCountPair.setValue(buildStatistics.getAllTestCount());
                    NumberPair failedTestCount = new NumberPair();
                    failedTestCount.setKey("Failed Test Count");
                    failedTestCount.setValue(buildStatistics.getFailedTestCount());
                    NumberPair passedTestCount = new NumberPair();
                    passedTestCount.setKey("Passed Test Count");
                    passedTestCount.setValue(buildStatistics.getPassedTestCount());
                    NumberPair testElapsedTime = new NumberPair();
                    testElapsedTime.setKey("Test Elapsed Time");
                    testElapsedTime.setValue(buildStatistics.getTotalDuration());
                    DatePair datePair = new DatePair();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sRunningBuild.getFinishDate());
                    datePair.setKey("Date of Run");
                    datePair.setValue(cal);
                    row.setStringPairs(new StringPair[] { stringPair} );
                    row.setNumberPairs(new NumberPair[] { allTestCountPair, failedTestCount, passedTestCount, testElapsedTime} );
                    row.setDatePairs(new DatePair[] { datePair });
                    service.addRow("teamcity", row);
                } catch (ServiceException e) {
                    e.printStackTrace();
                } catch (java.rmi.RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        emitStats(sRunningBuild, sUsers);
    }

    public void notifyLabelingFailed(Build build, VcsRoot vcsRoot, Throwable throwable, Set<SUser> sUsers) {
    }

    public void notifyBuildFailing(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
    }

    public void notifyBuildProbablyHanging(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
    }

    public void notifyResponsibleChanged(SBuildType sBuildType, Set<SUser> sUsers) {
    }

    @NotNull
    public String getNotificatorType() {
        return TYPE;
    }

    @NotNull
    public String getDisplayName() {
        return TYPE_NAME;
    }
}

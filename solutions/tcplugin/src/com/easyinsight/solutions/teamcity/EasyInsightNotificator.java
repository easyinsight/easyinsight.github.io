package com.easyinsight.solutions.teamcity;

import jetbrains.buildServer.notification.Notificator;
import jetbrains.buildServer.notification.NotificatorRegistry;
import jetbrains.buildServer.serverSide.*;
import jetbrains.buildServer.Build;
import jetbrains.buildServer.users.SUser;
import jetbrains.buildServer.users.NotificatorPropertyKey;
import jetbrains.buildServer.users.PropertyKey;
import jetbrains.buildServer.vcs.VcsRoot;
import jetbrains.buildServer.vcs.SVcsModification;

import java.util.*;
import java.io.IOException;
import java.io.File;

import org.jetbrains.annotations.NotNull;
import com.easyinsight.solutions.teamcity.validated.*;

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
    private static final String TEST_API_KEY = "easyInsightTestAPIKey";
    private static final String COVERAGE_API_KEY = "easyInsightCoverageAPIKey";

    private static final PropertyKey EI_LOGIN_KEY = new NotificatorPropertyKey(TYPE, EI_LOGIN);
    private static final PropertyKey EI_PASSWORD_KEY = new NotificatorPropertyKey(TYPE, EI_PASSWORD);
    private static final PropertyKey EI_TEST_KEY = new NotificatorPropertyKey(TYPE, TEST_API_KEY);
    private static final PropertyKey EI_COVERAGE_KEY = new NotificatorPropertyKey(TYPE, COVERAGE_API_KEY);

    public EasyInsightNotificator(NotificatorRegistry notificatorRegistry) throws IOException {
        List<UserPropertyInfo> userProps = new ArrayList<UserPropertyInfo>();
        userProps.add(new UserPropertyInfo(EI_LOGIN, "Easy Insight Key"));
        userProps.add(new UserPropertyInfo(EI_PASSWORD, "Easy Insight Secret Key"));
        userProps.add(new UserPropertyInfo(TEST_API_KEY, "Test Result Data Source"));
        userProps.add(new UserPropertyInfo(COVERAGE_API_KEY, "Coverage Result Data Source"));
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
            String eiTestKey = user.getPropertyValue(EI_TEST_KEY);
            String eiCoverageKey = user.getPropertyValue(EI_COVERAGE_KEY);
            BuildStatistics buildStatistics = sRunningBuild.getFullStatistics();
            if (buildStatistics.getAllTestCount() > 0) {
                try {
                    BasicAuthValidatingPublishServiceServiceLocator locator = new BasicAuthValidatingPublishServiceServiceLocator();
                    BasicAuthValidatedPublish service = locator.getBasicAuthValidatingPublishServicePort();
                    ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(eiUserName);
                    ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(eiPassword);
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


                    long duration = sRunningBuild.getFullStatistics().getTotalDuration();
                    NumberPair durationPair = new NumberPair();
                    durationPair.setKey("Duration");
                    durationPair.setValue(duration);
                    File artifactsDirectory = sRunningBuild.getArtifactsDirectory();

                    if (artifactsDirectory.exists()) {
                        File file = new File(artifactsDirectory, "coverage.txt");
                        if (file.exists()) {
                            new CoverageDataParse().parseData(file.getAbsolutePath(), service, eiCoverageKey);
                        }
                    }

                    for (List<ArtifactInfo> artifactInfos : sRunningBuild.getProvidedArtifacts().getArtifacts().values()) {
                        for (ArtifactInfo artifactInfo : artifactInfos) {
                            if (artifactInfo.getArtifactPath().endsWith("coverage.txt")) {
                                new CoverageDataParse().parseData(artifactInfo.getArtifactPath(), service, eiCoverageKey);
                            }
                        }
                    }
                    for (List<ArtifactInfo> artifactInfos : sRunningBuild.getDownloadedArtifacts().getArtifacts().values()) {
                        for (ArtifactInfo artifactInfo : artifactInfos) {
                            if (artifactInfo.getArtifactPath().endsWith("coverage.txt")) {
                                new CoverageDataParse().parseData(artifactInfo.getArtifactPath(), service, eiCoverageKey);
                            }
                        }
                    }
                    for (SVcsModification change : sRunningBuild.getContainingChanges()) {
                        change.getUserName();
                        change.getChanges().size();
                    }
                    StringPair agentPair = new StringPair();
                    agentPair.setKey("Build Agent");
                    agentPair.setValue(sRunningBuild.getAgentName());
                    
                    DatePair datePair = new DatePair();
                    Calendar cal = Calendar.getInstance();
                    datePair.setKey("Date of Run");
                    datePair.setValue(cal);
                    DayWhere dayWhere = new DayWhere();
                    dayWhere.setKey("Date of Run");
                    dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
                    dayWhere.setYear(cal.get(Calendar.YEAR));
                    row.setStringPairs(new StringPair[] { stringPair, agentPair} );
                    row.setNumberPairs(new NumberPair[] { allTestCountPair, failedTestCount, passedTestCount, testElapsedTime, durationPair} );
                    row.setDatePairs(new DatePair[] { datePair });
                    Where where = new Where();
                    where.setDayWheres(new DayWhere[] { dayWhere });
                    service.updateRow(eiTestKey, row, where);
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

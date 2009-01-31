package com.easyinsight.solutions.tcplugin;

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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyBuildSuccessful(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        for (SUser user : sUsers) {
            String eiUserName = user.getPropertyValue(EI_LOGIN_KEY);
            String eiPassword = user.getPropertyValue(EI_PASSWORD_KEY);
            ShortStatistics shortStatistics = sRunningBuild.getShortStatistics();
            BuildStatisticsOptions buildStatisticsOptions = new BuildStatisticsOptions();
            BuildStatistics buildStatistics = sRunningBuild.getBuildStatistics(buildStatisticsOptions);
            
            try {
                UncheckedPublishService service = new BasicAuthUncheckedPublishServiceServiceLocator().getBasicAuthUncheckedPublishServicePort();
                ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setUsername(eiUserName);
                ((BasicAuthUncheckedPublishServiceServiceSoapBindingStub)service).setPassword(eiPassword);
                Row row = new Row();
                StringPair stringPair = new StringPair();
                stringPair.setKey("project");
                stringPair.setValue(sRunningBuild.getProjectId());
                NumberPair numberPair = new NumberPair();
                numberPair.setKey("blah");
                numberPair.setValue(shortStatistics.getCompilationErrorsCount());
                row.setStringPairs(new StringPair[] { stringPair} );
                row.setNumberPairs(new NumberPair[] { numberPair} );
                service.addRow("teamcity", row);
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (java.rmi.RemoteException e) {
                e.printStackTrace();
            }
            /*
            IPublishService service = new UncheckedPublishServiceService().getUncheckedPublishServicePort();
            org.apache.cxf.endpoint.Client client = org.apache.cxf.frontend.ClientProxy.getClient(service);
            org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();
            Map<String,Object> outProps = new HashMap<String,Object>();

            WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
            outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
            // Specify our username
            outProps.put(WSHandlerConstants.USER, eiUserName);
            // Password type : plain text
            outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
            // for hashed password use:
            //properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
            // Callback used to retrieve password for given user.
            outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordCallback.class.getName());
            cxfEndpoint.getOutInterceptors().add(wssOut);
            */
        }
    }

    public void notifyBuildFailed(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyLabelingFailed(Build build, VcsRoot vcsRoot, Throwable throwable, Set<SUser> sUsers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyBuildFailing(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyBuildProbablyHanging(SRunningBuild sRunningBuild, Set<SUser> sUsers) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void notifyResponsibleChanged(SBuildType sBuildType, Set<SUser> sUsers) {
        //To change body of implemented methods use File | Settings | File Templates.
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

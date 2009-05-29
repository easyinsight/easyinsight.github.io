package com.easyinsight.scheduler;

import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.email.UserStub;
import com.easyinsight.users.Credentials;
import com.easyinsight.database.Database;


import javax.persistence.*;
import java.util.List;
import java.util.Date;
import java.util.UUID;
import java.sql.Connection;

import org.hibernate.Session;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.MessageBroker;

/**
 * User: James Boe
 * Date: Apr 15, 2009
 * Time: 5:35:53 PM
 */
@Entity
@Table(name="server_refresh_task")
@PrimaryKeyJoinColumn(name="scheduled_task_id")
public class ServerRefreshScheduledTask extends ScheduledTask {

    @Column(name="data_source_id")
    private long dataSourceID;
    
    @Column(name="user_id")
    private long userID;

    private static FeedStorage feedStorage = new FeedStorage();

    @Transient
    private Credentials refreshCreds;

    @Transient
    private ServerDataSourceDefinition dataSource;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected void execute(Date now, Connection conn) throws Exception {
        dataSource = (ServerDataSourceDefinition) feedStorage.getFeedDefinitionData(dataSourceID);

        AsyncMessage message = new AsyncMessage();
        message.setDestination("generalNotifications");
        message.setMessageId(UUID.randomUUID().toString());
 
        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.ADD);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage(null);
        message.setBody(info);
        MessageBroker.getMessageBroker(null).routeMessageToService(message, null);

        UserStub dataSourceUser = null;
        List<FeedConsumer> owners = dataSource.getUploadPolicy().getOwners();
        for (FeedConsumer owner : owners){
            if (owner.type() == FeedConsumer.USER) {
                dataSourceUser = (UserStub) owner;
            }
        }
        if (dataSourceUser == null) {
            throw new RuntimeException();
        }
        dataSource.refreshData(refreshCreds, dataSourceUser.getAccountID(), now);
        Session session = Database.instance().createSession(conn);
    }

    @Override
    protected void onFailure(Session s, String error) {
        AsyncMessage message = new AsyncMessage();
        message.setDestination("generalNotifications");
        message.setMessageId(UUID.randomUUID().toString());

        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.ERROR);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage(error);
        message.setBody(info);
        MessageBroker.getMessageBroker(null).routeMessageToService(message, null);
        super.onFailure(s, error);
    }

    @Override
    protected void onComplete(Session session) {
        AsyncMessage message = new AsyncMessage();
        message.setDestination("generalNotifications");
        message.setMessageId(UUID.randomUUID().toString());

        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.COMPLETE);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage("Completed!");
        message.setBody(info);
        MessageBroker.getMessageBroker(null).routeMessageToService(message, null);
        super.onComplete(session);
    }

    public Credentials getRefreshCreds() {
        return refreshCreds;
    }

    public void setRefreshCreds(Credentials refreshCreds) {
        this.refreshCreds = refreshCreds;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}
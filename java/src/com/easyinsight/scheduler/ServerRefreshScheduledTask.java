package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.users.Credentials;
import com.easyinsight.eventing.MessageUtils;


import javax.persistence.*;
import java.util.List;
import java.util.Date;

import org.hibernate.Session;

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
    private IServerDataSourceDefinition dataSource;

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    protected void execute(Date now, EIConnection conn) throws Exception {
        dataSource = (IServerDataSourceDefinition) feedStorage.getFeedDefinitionData(dataSourceID);
 
        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.ADD);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage(null);
        MessageUtils.sendMessage("generalNotifications", info);

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
        dataSource.refreshData(dataSourceUser.getAccountID(), now, null);
    }

    @Override
    protected void onFailure(Session s, String error) {
        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.ERROR);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage(error);
        MessageUtils.sendMessage("generalNotifications", info);
        super.onFailure(s, error);
    }

    @Override
    protected void onComplete(Session session) {

        RefreshEventInfo info = new RefreshEventInfo();
        info.setTaskId(getScheduledTaskID());
        info.setUserId(getUserID());
        info.setAction(RefreshEventInfo.COMPLETE);
        info.setFeedId(getDataSourceID());
        info.setFeedName(dataSource.getFeedName());
        info.setMessage("Completed!");
        MessageUtils.sendMessage("generalNotifications", info);
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
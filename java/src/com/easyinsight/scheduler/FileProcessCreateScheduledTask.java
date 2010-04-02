package com.easyinsight.scheduler;

import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.User;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.eventing.AsyncRunningEvent;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.eventing.AsyncCompletedEvent;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.sql.Connection;

import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 10, 2009
 * Time: 9:10:02 PM
 */
@Entity
@Table(name="file_process_create_scheduled_task")
@PrimaryKeyJoinColumn(name = "task_id")
public class FileProcessCreateScheduledTask extends ScheduledTask {

    public long getUploadID() {
        return uploadID;
    }

    public void setUploadID(long uploadID) {
        this.uploadID = uploadID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name="upload_id")
    private long uploadID;
    
    @Column(name="upload_name")
    private String name;

    @Column(name="user_id")
    private long userID;

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    @Column(name="account_id")
    private long accountID;

    @Override
    protected void onComplete(Session session) {
        AsyncCompletedEvent ev = new AsyncCompletedEvent();
        ev.setTask(this);
        ev.setUserID(userID);
        ev.setFeedID(0);
        ev.setFeedName(name);
        EventDispatcher.instance().dispatch(ev);
        super.onComplete(session);
    }

    @Transient
    private long feedID;
    @Transient
    private long analysisID;

    protected void execute(Date now, EIConnection conn) throws Exception {
        UserUploadService.RawUploadData rawUploadData = UserUploadService.retrieveRawData(uploadID, conn);
        UploadFormat uploadFormat = new UploadFormatTester().determineFormat(rawUploadData.getUserData());
        AsyncRunningEvent ev = new AsyncRunningEvent();
        ev.setTask(this);
        ev.setUserID(userID);
        ev.setFeedID(0);
        ev.setFeedName(name);
        EventDispatcher.instance().dispatch(ev);
        //createFeed(conn,  rawUploadData.getUserData(), uploadFormat);
    }

    public void createFeed(Connection conn, byte[] bytes, UploadFormat uploadFormat, List<AnalysisItem> fields) throws Exception {
        DataStorage tableDef = null;
        try {
            PersistableDataSetForm dataSet = UploadAnalysisCache.instance().getDataSet(uploadID);
            if (dataSet == null) {
                dataSet = uploadFormat.createDataSet(bytes, fields);
            }
            for (AnalysisItem field : fields) {
                dataSet.refreshKey(field.getKey());
            }

            FileBasedFeedDefinition feedDefinition = new FileBasedFeedDefinition();
            feedDefinition.setUploadFormat(uploadFormat);
            feedDefinition.setFeedName(name);
            User user = UserUploadService.retrieveUser(conn, userID);
            feedDefinition.setOwnerName(user.getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(user.getUserID(), user.getAccount().getAccountID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(fields);
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, dataSet.toDataSet(), uploadPolicy);
            tableDef = result.getTableDefinitionMetadata();
            feedID = result.getFeedID();
            tableDef.commit();
            conn.commit();

        }
        catch(Exception e) {
            if(tableDef != null) {
                tableDef.rollback();
            }
            throw e;
        }
        finally {
            if(tableDef != null)
                tableDef.closeConnection();
        }
    }

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}

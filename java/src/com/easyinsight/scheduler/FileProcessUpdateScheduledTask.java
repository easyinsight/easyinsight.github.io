package com.easyinsight.scheduler;

import com.easyinsight.storage.DataStorage;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.eventing.AsyncRunningEvent;
import com.easyinsight.eventing.EventDispatcher;
import com.easyinsight.eventing.AsyncCompletedEvent;

import javax.persistence.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.SQLException;

import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 11, 2009
 * Time: 4:27:39 PM
 */
@Entity
@Table(name="file_process_update_scheduled_task")
@PrimaryKeyJoinColumn(name="task_id")
public class FileProcessUpdateScheduledTask extends ScheduledTask {

    @Column(name = "feed_id")
    private long feedID;
    @Column(name="update_flag")
    private boolean update;
    @Column(name="upload_id")
    private long uploadID;
    @Column(name="user_id")
    private long userID;
    @Column(name="account_id")
    private long accountID;

    @Transient
    private boolean background;
    @Transient
    private String feedName;


    public long getFeedID() {
        return feedID;
    }

    public boolean isUpdate() {
        return update;
    }

    public long getUploadID() {
        return uploadID;
    }


    protected void execute(Date now, Connection conn) throws Exception {
        UserUploadService.RawUploadData rawUploadData = UserUploadService.retrieveRawData(uploadID, conn);
        background = true;
        updateData(feedID, update, conn, rawUploadData);

    }

    @Override
    protected void onComplete(Session session) {
        AsyncCompletedEvent ev = new AsyncCompletedEvent();
        ev.setTask(this);
        ev.setUserID(userID);
        ev.setFeedID((int) feedID);
        ev.setFeedName(feedName);
        EventDispatcher.instance().dispatch(ev);
        super.onComplete(session);
    }

    public void updateData(long feedID, boolean update, Connection conn, UserUploadService.RawUploadData rawUploadData) throws SQLException {
        DataStorage metadata = null;
        try {
            FileBasedFeedDefinition feedDefinition = (FileBasedFeedDefinition) UserUploadService.getFeedDefinition(feedID);
            if(background) {
                AsyncRunningEvent ev = new AsyncRunningEvent();
                ev.setTask(this);
                ev.setUserID(userID);
                feedName = feedDefinition.getFeedName();
                ev.setFeedID(feedDefinition.getDataFeedID());
                ev.setFeedName(feedDefinition.getFeedName());
                EventDispatcher.instance().dispatch(ev);
            }
            metadata = DataStorage.writeConnection(feedDefinition, conn, accountID);
            PersistableDataSetForm form = feedDefinition.getUploadFormat().createDataSet(rawUploadData.getUserData(), feedDefinition.getFields());
            if (update) {
                //DataRetrievalManager.instance().storeData(feedID, form);
                metadata.truncate();
                metadata.insertData(form.toDataSet());
            } else {
                //DataRetrievalManager.instance().appendData(feedID, form);
                metadata.insertData(form.toDataSet());
            }
            metadata.commit();
        }
        catch(SQLException se) {
            metadata.rollback();
            throw se;
        }
    }


    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setUploadID(long uploadID) {
        this.uploadID = uploadID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}

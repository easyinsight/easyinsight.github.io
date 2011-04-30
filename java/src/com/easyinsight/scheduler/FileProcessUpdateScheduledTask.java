package com.easyinsight.scheduler;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.userupload.UserUploadService;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 11, 2009
 * Time: 4:27:39 PM
 */
public class FileProcessUpdateScheduledTask {

    private long feedID;
    private boolean update;
    private long uploadID;
    private long userID;
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

    public void updateData(long feedID, boolean update, Connection conn, byte[] bytes) throws Exception {
        DataStorage metadata = null;
        try {
            FileBasedFeedDefinition feedDefinition = (FileBasedFeedDefinition) new FeedStorage().getFeedDefinitionData(feedID, conn);
            /*if(background) {
                AsyncRunningEvent ev = new AsyncRunningEvent();
                ev.setTask(this);
                ev.setUserID(userID);
                feedName = feedDefinition.getFeedName();
                ev.setFeedID(feedDefinition.getDataFeedID());
                ev.setFeedName(feedDefinition.getFeedName());
                EventDispatcher.instance().dispatch(ev);
            }*/
            AnalysisMeasure rowCount = null;
            for (AnalysisItem field : feedDefinition.getFields()) {
                if (field.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) field;
                    if (measure.isRowCountField()) {
                        rowCount = measure;
                    }
                }
            }
            metadata = DataStorage.writeConnection(feedDefinition, conn, accountID);
            PersistableDataSetForm form = feedDefinition.getUploadFormat().createDataSet(bytes, feedDefinition.getFields());
            if (update) {
                //DataRetrievalManager.instance().storeData(feedID, form);
                metadata.truncate();
                metadata.insertData(form.toDataSet(rowCount));
            } else {
                //DataRetrievalManager.instance().appendData(feedID, form);
                metadata.insertData(form.toDataSet(rowCount));
            }
            metadata.commit();
        }
        catch(Exception se) {
            if (metadata != null) metadata.rollback();
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

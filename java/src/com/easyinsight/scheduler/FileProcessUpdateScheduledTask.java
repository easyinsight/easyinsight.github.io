package com.easyinsight.scheduler;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceInternalService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.userupload.DefaultFormatMapper;
import com.easyinsight.userupload.UploadFormat;
import com.easyinsight.userupload.UserUploadService;

import javax.persistence.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    private List<AnalysisItem> newFields;

    public void setNewFields(List<AnalysisItem> newFields) {
        this.newFields = newFields;
    }

    public long getFeedID() {
        return feedID;
    }

    public boolean isUpdate() {
        return update;
    }

    public long getUploadID() {
        return uploadID;
    }

    public void updateData(long feedID, boolean update, EIConnection conn, byte[] bytes) throws Exception {
        DataStorage metadata = null;
        try {
            FileBasedFeedDefinition feedDefinition = (FileBasedFeedDefinition) new FeedStorage().getFeedDefinitionData(feedID, conn);
            AnalysisMeasure rowCount = null;
            for (AnalysisItem field : feedDefinition.getFields()) {
                if (field.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) field;
                    if (measure.isRowCountField()) {
                        rowCount = measure;
                    }
                }
            }
            if (newFields != null && newFields.size() > 0) {
                feedDefinition.getFields().addAll(newFields);
            }
            new DataSourceInternalService().updateFeedDefinition(feedDefinition, conn);
            metadata = DataStorage.writeConnection(feedDefinition, conn, accountID);
            UploadFormat uploadFormat = feedDefinition.getUploadFormat();
            PersistableDataSetForm form = uploadFormat.createDataSet(bytes, feedDefinition.getFields(), new DefaultFormatMapper(feedDefinition.getFields()));
            if (update) {
                metadata.truncate();
                metadata.insertData(form.toDataSet(rowCount));
            } else {
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

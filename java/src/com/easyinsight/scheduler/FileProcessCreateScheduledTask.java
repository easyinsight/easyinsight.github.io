package com.easyinsight.scheduler;

import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.User;
import com.easyinsight.userupload.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedCreation;

import javax.persistence.*;
import java.util.List;
import java.sql.Connection;

import org.hibernate.Session;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 10, 2009
 * Time: 9:10:02 PM
 */
public class FileProcessCreateScheduledTask {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private long userID;

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    private long accountID;

    @Transient
    private long feedID;
    @Transient
    private long analysisID;

    public void createFeed(Connection conn, byte[] bytes, UploadFormat uploadFormat, List<AnalysisItem> fields) throws Exception {
        DataStorage tableDef = null;
        try {
            PersistableDataSetForm dataSet = uploadFormat.createDataSet(bytes, fields);

            for (AnalysisItem field : fields) {
                dataSet.refreshKey(field.getKey());
            }

            boolean hasCount = false;
            for (AnalysisItem field : fields) {
                if ("count".equals(field.toDisplay().toLowerCase())) {
                    hasCount = true;
                }
            }

            AnalysisMeasure countMeasure = null;
            if (!hasCount) {
                countMeasure = new AnalysisMeasure("Count", AggregationTypes.SUM);
                countMeasure.setRowCountField(true);
                fields.add(countMeasure);
            }

            FileBasedFeedDefinition feedDefinition = new FileBasedFeedDefinition();
            feedDefinition.setUploadFormat(uploadFormat);
            feedDefinition.setFeedName(name);
            User user = UserUploadService.retrieveUser(conn, userID);
            feedDefinition.setOwnerName(user.getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(user.getUserID(), user.getAccount().getAccountID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(fields);
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, dataSet.toDataSet(countMeasure), uploadPolicy);
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

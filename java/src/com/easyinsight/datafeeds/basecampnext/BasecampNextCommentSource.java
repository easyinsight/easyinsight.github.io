package com.easyinsight.datafeeds.basecampnext;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/6/14
 * Time: 2:22 PM
 */
public class BasecampNextCommentSource extends BasecampNextBaseSource {

    public static final String COMMENT_ID = "Comment ID";
    public static final String COMMENT_PROJECT_ID = "Comment Project ID";
    public static final String COMMENT_TODO_ID = "Comment Todo ID";
    public static final String COMMENT_CREATED_AT = "Comment Created At";
    public static final String COMMENT_AUTHOR = "Comment Author";
    public static final String COMMENT_COUNT = "Comment Count";
    public static final String COMMENT_CONTENT = "Comment Body";

    public BasecampNextCommentSource() {
        setFeedName("Comments");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(COMMENT_ID, new AnalysisDimension());
        fieldBuilder.addField(COMMENT_PROJECT_ID, new AnalysisDimension());
        fieldBuilder.addField(COMMENT_TODO_ID, new AnalysisDimension());
        fieldBuilder.addField(COMMENT_CREATED_AT, new AnalysisDateDimension());
        fieldBuilder.addField(COMMENT_AUTHOR, new AnalysisDimension());
        fieldBuilder.addField(COMMENT_COUNT, new AnalysisMeasure());
        fieldBuilder.addField(COMMENT_CONTENT, new AnalysisText());
    }

    @Override
    protected String getUpdateKeyName() {
        return COMMENT_ID;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {

        try {
            BasecampNextCompositeSource basecampNextCompositeSource = (BasecampNextCompositeSource) parentDefinition;

            List<BasecampComment> comments;

            while ((comments = (List<BasecampComment>) basecampNextCompositeSource.next(BasecampNextCompositeSource.COMMENTS)) != null) {
                for (BasecampComment basecampComment : comments) {
                    DataSet dataSet = new DataSet();
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(COMMENT_ID), basecampComment.getId());
                    row.addValue(keys.get(COMMENT_TODO_ID), basecampComment.getTodoID());
                    row.addValue(keys.get(COMMENT_CREATED_AT), basecampComment.getCreatedAt());
                    row.addValue(keys.get(COMMENT_AUTHOR), basecampComment.getAuthor());
                    row.addValue(keys.get(COMMENT_CONTENT), basecampComment.getContent());
                    row.addValue(keys.get(COMMENT_PROJECT_ID), basecampComment.getProjectID());
                    row.addValue(keys.get(COMMENT_COUNT), 1);
                    if (lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                        IDataStorage.insertData(dataSet);
                    } else {
                        StringWhere stringWhere = new StringWhere(keys.get(COMMENT_ID), basecampComment.getId());
                        IDataStorage.updateData(dataSet, Arrays.asList((IWhere) stringWhere));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BASECAMP_NEXT_COMMENT;
    }
}

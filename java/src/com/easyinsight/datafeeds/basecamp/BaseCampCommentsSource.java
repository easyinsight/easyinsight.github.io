package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.*;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:36:57 PM
 */
public class BaseCampCommentsSource extends BaseCampBaseSource {

    public static final String COMMENT_AUTHOR = "Comment Author";
    public static final String MILESTONE_ID = "Milestone ID";
    public static final String COMMENT_BODY = "Comment Body";
    public static final String COMMENT_ID = "Comment ID";
    public static final String COMMENT_CREATED_ON = "Comment Created On";

    public static final String COUNT = "Count";

    public BaseCampCommentsSource() {
        setFeedName("Comments");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(COUNT, COMMENT_AUTHOR, MILESTONE_ID, COMMENT_BODY, COMMENT_ID, COMMENT_CREATED_ON);
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_COMMENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_BODY), true));
        AnalysisDateDimension commentCreatedOnDim = new AnalysisDateDimension(keys.get(COMMENT_CREATED_ON), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(commentCreatedOnDim);              
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        DataSet ds = new DataSet();
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        boolean writeDuring = dataStorage != null && !parentDefinition.isAdjustDates();
        if (source.isIncludeMilestoneComments()) {

        String url = source.getUrl();
        DateFormat df = new XmlSchemaDateFormat();


        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");

        Builder builder = new Builder();
        try {
            BaseCampCache basecampCache = source.getOrCreateCache(client);
            Document projects = runRestRequest("/projects.xml", client, builder, url, null, true, parentDefinition);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectName = queryField(curProject, "name/text()");                                
                loadingProgress(i, projectNodes.size(), "Synchronizing with comments of " + projectName + "...", false);
                String projectStatus = queryField(curProject, "status/text()");
                if (!source.isIncludeArchived() && "archived".equals(projectStatus)) {
                    continue;
                }
                if (!source.isIncludeInactive() && "inactive".equals(projectStatus)) {
                    continue;
                }
                String projectIdToRetrieve = queryField(curProject, "id/text()");

                Document milestoneList = runRestRequest("/projects/" + projectIdToRetrieve + "/milestones/list", client, builder, url, null, false, parentDefinition);

                Nodes milestoneCacheNodes = milestoneList.query("/milestones/milestone");
                for (int milestoneIndex = 0; milestoneIndex < milestoneCacheNodes.size(); milestoneIndex++) {
                    Node milestoneNode = milestoneCacheNodes.get(milestoneIndex);
                    String milestoneID = queryField(milestoneNode, "id/text()");
                    Document comments = runRestRequest("/milestones/" + milestoneID + "/comments.xml", client, builder, url, null, false, parentDefinition);
                    Nodes commentNodes = comments.query("/comments/comment");
                    for (int j = 0; j < commentNodes.size(); j++) {
                        Node commentNode = commentNodes.get(j);
                        String commentID = queryField(commentNode, "id/text()");
                        String authorName = basecampCache.getUserName(queryField(commentNode, "author-id"));
                        String body = queryField(commentNode, "body/text()");
                        String createdDateString = queryField(commentNode, "created-on/text()");
                        Date createdDate = null;
                        if(createdDateString != null )
                            createdDate = df.parse(createdDateString);
                        IRow row = ds.createRow();
                        row.addValue(keys.get(COMMENT_ID), commentID);
                        row.addValue(keys.get(MILESTONE_ID), milestoneID);
                        row.addValue(keys.get(COMMENT_AUTHOR), authorName);
                        row.addValue(keys.get(COMMENT_BODY), body);
                        row.addValue(keys.get(COMMENT_CREATED_ON), createdDate);
                        row.addValue(keys.get(COUNT), 1);
                    }
                }

                if (writeDuring) {
                    dataStorage.insertData(ds);
                    ds = new DataSet();
                }

            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        }
        if (!writeDuring) {
            if (parentDefinition.isAdjustDates()) {
                ds = adjustDates(ds);
            }
            return ds;
        } else {
            return null;
        }
    }





    @Override
    public int getVersion() {
        return 1;
    }
}
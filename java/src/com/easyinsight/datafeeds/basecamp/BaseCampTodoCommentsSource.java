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
public class BaseCampTodoCommentsSource extends BaseCampBaseSource {

    public static final String COMMENT_AUTHOR = "Todo Comment Author";
    public static final String TODO_ID = "Todo Parent ID";
    public static final String COMMENT_BODY = "Todo Comment Body";
    public static final String COMMENT_ID = "Todo Comment ID";
    public static final String COMMENT_CREATED_ON = "Todo Comment Created On";

    public static final String COUNT = "Todo Comment Count";

    public BaseCampTodoCommentsSource() {
        setFeedName("Todo Comments");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COUNT, COMMENT_AUTHOR, TODO_ID, COMMENT_BODY, COMMENT_ID, COMMENT_CREATED_ON);
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_TODO_COMMENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODO_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_BODY), true));
        AnalysisDateDimension commentCreatedOnDim = new AnalysisDateDimension(keys.get(COMMENT_CREATED_ON), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(commentCreatedOnDim);
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        DataSet ds = new DataSet();
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        boolean writeDuring = dataStorage != null && !parentDefinition.isAdjustDates();
        if (source.isIncludeTodoComments()) {

            String url = source.getUrl();
            DateFormat df = new XmlSchemaDateFormat();


            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false, conn);
            HttpClient client = getHttpClient(token.getTokenValue(), "");

            Builder builder = new Builder();
            try {
                BaseCampCache basecampCache = source.getOrCreateCache(client);
                Document projects = runRestRequest("/projects.xml", client, builder, url, null, true, parentDefinition, false);
                Nodes projectNodes = projects.query("/projects/project");
                for (int i = 0; i < projectNodes.size(); i++) {
                    Node curProject = projectNodes.get(i);
                    String projectName = queryField(curProject, "name/text()");
                    String projectID = queryField(curProject, "id/text()");
                    loadingProgress(i, projectNodes.size(), "Synchronizing with comments of " + projectName + "...", callDataID);
                    String projectStatus = queryField(curProject, "status/text()");
                    if (!source.isIncludeArchived() && "archived".equals(projectStatus)) {
                        continue;
                    }
                    if (!source.isIncludeInactive() && "inactive".equals(projectStatus)) {
                        continue;
                    }
                    Document todoLists = runRestRequest("/projects/" + projectID + "/todo_lists.xml", client, builder, url, null, false, parentDefinition, false);
                    Nodes todoListNodes = todoLists.query("/todo-lists/todo-list");
                    if (todoListNodes.size() > 0) {
                        for (int j = 0; j < todoListNodes.size(); j++) {
                            Node todoListNode = todoListNodes.get(j);
                            String todoListId = queryField(todoListNode, "id/text()");


                            Document todoItems = runRestRequest("/todo_lists/" + todoListId + "/todo_items.xml", client, builder, url, null, false, parentDefinition, false);

                            Nodes todoItemNodes = todoItems.query("/todo-items/todo-item");
                            if (todoItemNodes.size() > 0) {
                                for (int k = 0; k < todoItemNodes.size(); k++) {
                                    Node todoItem = todoItemNodes.get(k);
                                    String todoID = queryField(todoItem, "id/text()");
                                    String completed = queryField(todoItem, "completed/text()").toLowerCase();
                                    if ("false".equals(completed)) {
                                        Document comments = runRestRequest("/todo_items/" + todoID + "/comments.xml", client, builder, url, null, false, parentDefinition, false);
                                        Nodes commentNodes = comments.query("/comments/comment");
                                        for (int l = 0; l < commentNodes.size(); l++) {
                                            Node commentNode = commentNodes.get(l);
                                            String commentID = queryField(commentNode, "id/text()");
                                            String authorName = basecampCache.getUserName(queryField(commentNode, "author-id"));
                                            String body = queryField(commentNode, "body/text()");
                                            String createdDateString = queryField(commentNode, "created-at/text()");
                                            Date createdDate = null;
                                            if (createdDateString != null)
                                                createdDate = df.parse(createdDateString);
                                            IRow row = ds.createRow();
                                            row.addValue(keys.get(COMMENT_ID), commentID);
                                            row.addValue(keys.get(TODO_ID), todoID);
                                            row.addValue(keys.get(COMMENT_AUTHOR), authorName);
                                            row.addValue(keys.get(COMMENT_BODY), body);
                                            row.addValue(keys.get(COMMENT_CREATED_ON), createdDate);
                                            row.addValue(keys.get(COUNT), 1);
                                        }
                                    }
                                }
                            }
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
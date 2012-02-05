package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:36:57 PM
 */
public class BaseCampTodoCommentsSource extends BaseCampBaseSource {

    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String COMMENT_AUTHOR = "Todo Comment Author";
    public static final String TODO_ID = "Todo Parent ID";
    public static final String COMMENT_BODY = "Todo Comment Body";
    public static final String COMMENT_ID = "Todo Comment ID";
    public static final String COMMENT_CREATED_ON = "Todo Comment Created On";
    public static final String PROJECT_ID = "Todo Comment Project ID";

    public static final String COUNT = "Todo Comment Count";

    public BaseCampTodoCommentsSource() {
        setFeedName("Todo Comments");
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentSource;
        return !source.isIncrementalRefresh();
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COUNT, COMMENT_AUTHOR, TODO_ID, COMMENT_BODY, COMMENT_ID, COMMENT_CREATED_ON, PROJECT_ID);
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_TODO_COMMENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMMENT_ID), true));
        AnalysisDimension projectDimension = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectDimension.setHidden(true);
        analysisItems.add(projectDimension);
        analysisItems.add(new AnalysisDimension(keys.get(TODO_ID), true));
        analysisItems.add(new AnalysisText(keys.get(COMMENT_BODY)));
        AnalysisDateDimension commentCreatedOnDim = new AnalysisDateDimension(keys.get(COMMENT_CREATED_ON), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(commentCreatedOnDim);
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        DataSet ds = new DataSet();
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        DateFormat deadlineTimeFormat = new SimpleDateFormat(XMLDATETIMEFORMAT);
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
                    String projectChangedOnString = queryField(curProject, "last-changed-on/text()");

                    Date projectChangedAt;
                    if (projectChangedOnString == null) {
                        projectChangedAt = new Date();
                    } else {
                        projectChangedAt = deadlineTimeFormat.parse(projectChangedOnString);
                    }

                    if (lastRefreshDate == null) {
                        lastRefreshDate = new Date(1);
                    }
                    long delta = lastRefreshDate.getTime() - projectChangedAt.getTime();

                    long daysSinceChange = delta / (60 * 60 * 1000 * 24);

                    if (source.isIncrementalRefresh() && daysSinceChange > 2) {
                        continue;
                    }
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
                                            row.addValue(keys.get(PROJECT_ID), projectID);
                                            row.addValue(keys.get(COMMENT_CREATED_ON), createdDate);
                                            row.addValue(keys.get(COUNT), 1);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!source.isIncrementalRefresh() || lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                        IDataStorage.insertData(ds);
                    } else {
                        StringWhere stringWhere = new StringWhere(keys.get(PROJECT_ID), projectID);
                        IDataStorage.updateData(ds, Arrays.asList((IWhere) stringWhere));
                    }
                    ds = new DataSet();
                }
            } catch (ReportException re) {
                throw re;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    protected String getUpdateKeyName() {
        return PROJECT_ID;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new BaseCampTodoComments1To2(this));
    }

    @Override
    public int getVersion() {
        return 2;
    }
}
package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.jetbrains.annotations.NotNull;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.Node;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:36:57 PM
 */
public class BaseCampTodoSource extends BaseCampBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String TODOLISTNAME = "To-do List";
    public static final String MILESTONENAME = "Milestone";
    public static final String DEADLINE = "Milestone Deadline";

    public static final String COMPLETED = "Completed";
    public static final String CONTENT = "Content";
    public static final String CREATEDDATE = "Created On";
    public static final String COMPLETEDDATE = "Completed On";
    public static final String RESPONSIBLEPARTYNAME = "Responsible Party";
    public static final String RESPONSIBLEPARTYID = "Responsible Party ID";
    public static final String CREATORNAME = "Creator";
    public static final String CREATORID = "Creator ID";
    public static final String ITEMID = "Item ID";

    public static final String PROJECTNAME = "Project Name";
    public static final String PROJECTSTATUS = "Project Status";
    public static final String PROJECTID = "Project ID";

    public static final String TODOLISTDESC = "To-do List Description";
    public static final String TODOLISTID = "To-do List ID";
    public static final String TODOLISTPRIVATE = "To-do List Privacy";
    public static final String COUNT = "Count";

    public static final String COMPLETERNAME = "Completer";
    public static final String COMPLETERID = "Completer ID";

    public static final String ITEMCYCLE = "Item Cycle";

    public BaseCampTodoSource() {
        setFeedName("Todo");
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        String url = source.getUrl();
        DateFormat df = new XmlSchemaDateFormat();
        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false);
        if (token == null && credentials.getUserName() != null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.BASECAMP_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID());
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        try {
            Document projects = runRestRequest("/projects.xml", client, builder, url, null);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectName = queryField(curProject, "name/text()");
                String projectStatus = queryField(curProject, "status/text()");
                String projectIdToRetrieve = queryField(curProject, "id/text()");

                Document milestoneList = runRestRequest("/projects/" + projectIdToRetrieve + "/milestones/list", client, builder, url, null);
                Document todoLists = runRestRequest("/projects/" + projectIdToRetrieve + "/todo_lists.xml", client, builder, url, null);
                Nodes todoListNodes = todoLists.query("/todo-lists/todo-list");
                for(int j = 0;j < todoListNodes.size();j++) {
                    Node todoListNode = todoListNodes.get(j);
                    String todoListName = queryField(todoListNode, "name/text()");
                    String todoListId = queryField(todoListNode, "id/text()");
                    String todoListDesc = queryField(todoListNode, "description/text()");
                    String todoListPrivacy = "true".equalsIgnoreCase(queryField(todoListNode, "private/text()")) ? "private" : "public";
                    String milestoneIdToRetrieve = queryField(todoListNode, "milestone-id/text()");
                    Nodes milestoneNodes = milestoneList.query("/milestones/milestone[id/text()=" + milestoneIdToRetrieve + "]");
                    Node milestoneNode;
                    String milestoneName = null;
                    Date milestoneDeadline = null;
                    if(milestoneNodes.size() > 0) {
                        milestoneNode = milestoneNodes.get(0);
                        milestoneName = queryField(milestoneNode, "title/text()");
                        String milestoneDl = queryField(milestoneNode, "deadline/text()");
                        milestoneDeadline = deadlineFormat.parse(milestoneDl);
                    }


                    try {
                        Document todoItems = runRestRequest("/todo_lists/" + todoListId + "/todo_items.xml", client, builder, url, null);

                        Nodes todoItemNodes = todoItems.query("/todo-items/todo-item");
                        for(int k = 0;k < todoItemNodes.size();k++) {
                            Node todoItem = todoItemNodes.get(k);
                            String responsiblePartyId = queryField(todoItem, "responsible-party-id/text()");
                            String responsiblePartyName = retrieveContactInfo(client, builder, peopleCache, responsiblePartyId, url);
                            String creatorId = queryField(todoItem, "creator-id/text()");
                            String creatorName = retrieveContactInfo(client, builder, peopleCache, creatorId, url);
                            String completerId = queryField(todoItem, "completer-id/text()");
                            String completerName = retrieveContactInfo(client, builder, peopleCache, completerId, url);
                            String createdDateString = queryField(todoItem, "created-on/text()");
                            Date createdDate = null;
                            if(createdDateString != null )
                                createdDate = df.parse(createdDateString);
                            String completedDateString = queryField(todoItem, "completed-on/text()");
                            Date completedDate = null;
                            if(completedDateString != null)
                                completedDate = df.parse(completedDateString);

                            IRow row = ds.createRow();
                            row.addValue(keys.get(PROJECTNAME), projectName);
                            row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                            row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                            row.addValue(keys.get(MILESTONENAME), milestoneName);
                            row.addValue(keys.get(DEADLINE), new DateValue(milestoneDeadline));
                            row.addValue(keys.get(TODOLISTDESC), todoListDesc);
                            row.addValue(keys.get(TODOLISTNAME), todoListName);
                            row.addValue(keys.get(TODOLISTID), todoListId);
                            row.addValue(keys.get(TODOLISTPRIVATE), todoListPrivacy);
                            row.addValue(keys.get(ITEMID), queryField(todoItem, "id/text()"));
                            row.addValue(keys.get(CONTENT), queryField(todoItem, "content/text()"));
                            row.addValue(keys.get(COMPLETED), queryField(todoItem, "completed/text()").toLowerCase());
                            row.addValue(keys.get(RESPONSIBLEPARTYID), responsiblePartyId);
                            row.addValue(keys.get(RESPONSIBLEPARTYNAME), responsiblePartyName);
                            row.addValue(keys.get(CREATORID), creatorId);
                            row.addValue(keys.get(CREATORNAME), creatorName);
                            row.addValue(keys.get(COMPLETEDDATE), new DateValue(completedDate));
                            row.addValue(keys.get(CREATEDDATE), new DateValue(createdDate));
                            row.addValue(keys.get(COMPLETERNAME), completerName);
                            row.addValue(keys.get(COMPLETERID), completerId);

                            row.addValue(keys.get(COUNT), new NumericValue(1));
                        }
                    } catch (Exception e) {
                        LogClass.debug("Orphan data for todo list " + todoListId);
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(ITEMID, CREATORNAME, CREATORID, RESPONSIBLEPARTYNAME,
                RESPONSIBLEPARTYID, CONTENT, COMPLETED, CREATEDDATE, COMPLETEDDATE,
                TODOLISTNAME, MILESTONENAME, DEADLINE, PROJECTNAME, PROJECTSTATUS,
                PROJECTID, TODOLISTDESC, TODOLISTID, TODOLISTPRIVATE, COMPLETERNAME, COMPLETERID, COUNT, ITEMCYCLE);
    }



    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension itemDim = new AnalysisDimension(keys.get(ITEMID), true);
        analysisItems.add(itemDim);
        analysisItems.add(new AnalysisDimension(keys.get(CREATORNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(CREATORID), true));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLEPARTYNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLEPARTYID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETED), true));
        AnalysisDateDimension createdDim = new AnalysisDateDimension(keys.get(CREATEDDATE), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(createdDim);
        AnalysisDateDimension completedDim = new AnalysisDateDimension(keys.get(COMPLETEDDATE), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(completedDim);
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONENAME), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(DEADLINE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTSTATUS), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTDESC), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTPRIVATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETERNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETERID), true));
        analysisItems.add(new AnalysisStep(keys.get(ITEMCYCLE), true, AnalysisDateDimension.DAY_LEVEL, createdDim, completedDim, itemDim));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

}

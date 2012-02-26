package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
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
    public static final String ALTDATEFORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";

    /*
    e6630db11f381ce469305018e1e773b6ad4a6a14
Client Secret:
f845c2a78ca4df6a19cd23515deda0ce826ff8d0
     */

    public static final String TODOLISTNAME = "To-do List";
    public static final String MILESTONENAME = "Milestone";
    public static final String MILESTONE_OWNER = "Milestone Owner";
    public static final String DEADLINE = "Milestone Deadline";
    public static final String MILESTONE_COMPLETED_ON = "Milestone Completed On";
    public static final String MILESTONE_CREATED_ON = "Milestone Created On";
    public static final String MILESTONE_ID = "Milestone ID";

    public static final String COMPLETED = "Completed";
    public static final String COMPANY = "Company";
    public static final String CONTENT = "Content";
    public static final String TODO_ITEM_NAME = "To-do Name";
    public static final String CREATEDDATE = "Created On";
    public static final String DUEON = "Due On";
    public static final String COMPLETEDDATE = "Completed On";
    public static final String RESPONSIBLEPARTYNAME = "Responsible Party";
    public static final String RESPONSIBLEPARTYID = "Responsible Party ID";
    public static final String CREATORNAME = "Creator";
    public static final String CREATORID = "Creator ID";
    public static final String ITEMID = "Item ID";
    public static final String CALENDAR_EVENT_TYPE = "Calendar Event Type";
    public static final String CALENDAR_EVENT_START = "Calendar Event Start";

    public static final String MILESTONE_LAST_COMMENT = "Latest Milestone Comment";

    public static final String PROJECTNAME = "Project Name";
    public static final String PROJECTSTATUS = "Project Status";
    public static final String PROJECTID = "Project ID";
    public static final String PROJECT_CREATION_DATE = "Project Created On";
    public static final String ANNOUNCEMENT = "Project Announcement";

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

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentSource;
        return !source.isIncrementalRefresh();
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        String url = source.getUrl();
        DateFormat df = new XmlSchemaDateFormat();
        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);
        DateFormat deadlineTimeFormat = new SimpleDateFormat(XMLDATETIMEFORMAT);
        DateFormat altFormat = new SimpleDateFormat(ALTDATEFORMAT);

        if (lastRefreshDate == null) {
            lastRefreshDate = new Date(1);
        }

        //DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false, conn);

        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
            BaseCampCache basecampCache = source.getOrCreateCache(client);
            Document projects = runRestRequest("/projects.xml", client, builder, url, null, true, parentDefinition, false);
            Nodes projectNodes = projects.query("/projects/project");
            for (int i = 0; i < projectNodes.size(); i++) {
                DataSet ds = new DataSet();
                Node curProject = projectNodes.get(i);
                String projectName = queryField(curProject, "name/text()");
                String projectCreatedAtString = queryField(curProject, "created-on/text()");
                String projectChangedOnString = queryField(curProject, "last-changed-on/text()");


                Date projectCreatedAt = deadlineFormat.parse(projectCreatedAtString);
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

                /*if (source.isIncrementalRefresh() && daysSinceChange > 2) {
                    continue;
                }*/

                String announcement = queryField(curProject, "announcement/text()");
                loadingProgress(i, projectNodes.size(), "Synchronizing with todo items of " + projectName + "...", callDataID);
                String projectStatus = queryField(curProject, "status/text()");
                if ("template".equals(projectStatus)) {
                    continue;
                }
                if (!source.isIncludeArchived() && "archived".equals(projectStatus)) {
                    continue;
                }
                if (!source.isIncludeInactive() && "inactive".equals(projectStatus)) {
                    continue;
                }
                String projectIdToRetrieve = queryField(curProject, "id/text()");

                Map<String, MilestoneInfo> milestoneMap = new HashMap<String, MilestoneInfo>();

                if (!"archived".equals(projectStatus)) {
                    Document milestoneList;

                    int milestoneCount;
                    int page = 0;
                    try {
                        do {
                            milestoneCount = 0;
                            if (page == 0) {
                                milestoneList = runRestRequest("/projects/" + projectIdToRetrieve + "/calendar_entries.xml", client, builder, url, null, false, parentDefinition, false);
                            } else {
                                milestoneList = runRestRequest("/projects/" + projectIdToRetrieve + "/calendar_entries.xml?page=" + page, client, builder, url, null, false, parentDefinition, false);
                            }

                            Nodes milestoneCacheNodes = milestoneList.query("/calendar-entries/calendar-entry");
                            for (int milestoneIndex = 0; milestoneIndex < milestoneCacheNodes.size(); milestoneIndex++) {
                                milestoneCount++;
                                Node milestoneNode = milestoneCacheNodes.get(milestoneIndex);
                                String id = queryField(milestoneNode, "id/text()");
                                String milestoneName = queryField(milestoneNode, "title/text()");
                                String milestoneDl = queryField(milestoneNode, "deadline/text()");
                                if (milestoneDl == null) {
                                    milestoneDl = queryField(milestoneNode, "due-at/text()");
                                }
                                String startAtString = queryField(milestoneNode, "start-at/text()");
                                String type = queryField(milestoneNode, "type/text()");

                                Date startDate = null;
                                if (startAtString != null) {
                                    startDate = deadlineFormat.parse(startAtString);
                                }
                                Date milestoneDeadline = null;
                                try {
                                    if (milestoneDl != null && "Milestone".equals(type)) {
                                        try {
                                            milestoneDeadline = deadlineFormat.parse(milestoneDl);
                                        } catch (ParseException e) {
                                            milestoneDeadline = altFormat.parse(milestoneDl);
                                        }
                                    } else if (milestoneDl != null) {
                                        try {
                                            milestoneDeadline = deadlineFormat.parse(milestoneDl);
                                        } catch (ParseException e) {
                                            milestoneDeadline = deadlineTimeFormat.parse(milestoneDl);
                                        }
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                String milestoneCreatedOnString = queryField(milestoneNode, "created-on/text()");
                                Date milestoneCreatedOn = deadlineFormat.parse(milestoneCreatedOnString);
                                String milestoneCompletedOnString = queryField(milestoneNode, "completed-on/text()");
                                Date milestoneCompletedOn = null;
                                if (milestoneCompletedOnString != null) {
                                    milestoneCompletedOn = deadlineFormat.parse(milestoneCompletedOnString);
                                }
                                String milestoneOwner = null;
                                String responsiblePartyId = queryField(milestoneNode, "responsible-party-id/text()");
                                if (responsiblePartyId != null) {
                                    milestoneOwner = basecampCache.getUserName(responsiblePartyId);
                                }
                                milestoneMap.put(id, new MilestoneInfo(milestoneName, milestoneCreatedOn, milestoneCompletedOn, milestoneDeadline, milestoneOwner, type, startDate));
                            }

                            page++;
                        } while (milestoneCount == 50);
                    } catch (Http403Exception e) {
                        // ignore
                    }
                }


                Document todoLists = runRestRequest("/projects/" + projectIdToRetrieve + "/todo_lists.xml", client, builder, url, null, false, parentDefinition, false);
                Nodes todoListNodes = todoLists.query("/todo-lists/todo-list");
                if (todoListNodes.size() > 0) {
                    for (int j = 0; j < todoListNodes.size(); j++) {
                        Node todoListNode = todoListNodes.get(j);
                        String todoListName = queryField(todoListNode, "name/text()");
                        String todoListId = queryField(todoListNode, "id/text()");
                        String todoListDesc = queryField(todoListNode, "description/text()");
                        String todoListPrivacy = "true".equalsIgnoreCase(queryField(todoListNode, "private/text()")) ? "private" : "public";
                        String milestoneIdToRetrieve = queryField(todoListNode, "milestone-id/text()");
                        MilestoneInfo milestoneInfo = milestoneMap.get(milestoneIdToRetrieve);
                        String milestoneName = null;
                        Date milestoneCreatedOn = null;
                        Date milestoneCompletedOn = null;
                        Date milestoneDeadline = null;
                        String milestoneComment = null;
                        String milestoneOwner = null;
                        Date eventStart = null;
                        String eventType = null;
                        if (milestoneInfo != null) {
                            milestoneName = milestoneInfo.milestoneName;
                            milestoneCreatedOn = milestoneInfo.milestoneCreatedOn;
                            milestoneCompletedOn = milestoneInfo.milestoneCompletedOn;
                            milestoneDeadline = milestoneInfo.milestoneDeadline;
                            milestoneOwner = milestoneInfo.milestoneOwner;
                            eventStart = milestoneInfo.startDate;
                            eventType = milestoneInfo.type;
                            milestoneInfo.published = true;
                        }

                        try {
                            Document todoItems = runRestRequest("/todo_lists/" + todoListId + "/todo_items.xml", client, builder, url, null, false, parentDefinition, false);

                            Nodes todoItemNodes = todoItems.query("/todo-items/todo-item");
                            if (todoItemNodes.size() > 0) {
                                for (int k = 0; k < todoItemNodes.size(); k++) {
                                    Node todoItem = todoItemNodes.get(k);
                                    String responsiblePartyId = queryField(todoItem, "responsible-party-id/text()");
                                    String responsiblePartyName = basecampCache.getUserName(responsiblePartyId);
                                    String creatorId = queryField(todoItem, "creator-id/text()");
                                    String creatorName = basecampCache.getUserName(creatorId);
                                    String completerId = queryField(todoItem, "completer-id/text()");
                                    String completerName = basecampCache.getUserName(completerId);
                                    String createdDateString = queryField(todoItem, "created-on/text()");
                                    Date createdDate = null;
                                    if (createdDateString != null)
                                        createdDate = df.parse(createdDateString);
                                    String completedDateString = queryField(todoItem, "completed-on/text()");
                                    Date completedDate = null;
                                    if (completedDateString != null)
                                        completedDate = df.parse(completedDateString);

                                    String dueOnString = queryField(todoItem, "due-at/text()");
                                    Date dueOnDate = null;
                                    if (dueOnString != null)
                                        dueOnDate = df.parse(dueOnString);

                                    IRow row = ds.createRow();
                                    row.addValue(keys.get(PROJECTNAME), projectName);
                                    row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                                    row.addValue(keys.get(PROJECT_CREATION_DATE), projectCreatedAt);
                                    row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                                    row.addValue(keys.get(MILESTONENAME), milestoneName);
                                    if (milestoneDeadline != null) {
                                        row.addValue(keys.get(DEADLINE), new DateValue(milestoneDeadline));
                                    }
                                    row.addValue(keys.get(TODOLISTDESC), todoListDesc);
                                    row.addValue(keys.get(TODOLISTNAME), todoListName);
                                    row.addValue(keys.get(TODOLISTID), todoListId);
                                    row.addValue(keys.get(TODOLISTPRIVATE), todoListPrivacy);
                                    row.addValue(keys.get(ITEMID), queryField(todoItem, "id/text()"));
                                    row.addValue(keys.get(CONTENT), queryField(todoItem, "content/text()"));
                                    row.addValue(keys.get(COMPLETED), queryField(todoItem, "completed/text()").toLowerCase());
                                    row.addValue(keys.get(ANNOUNCEMENT), announcement);
                                    row.addValue(keys.get(RESPONSIBLEPARTYID), responsiblePartyId);
                                    row.addValue(keys.get(RESPONSIBLEPARTYNAME), responsiblePartyName);
                                    row.addValue(keys.get(CREATORID), creatorId);
                                    row.addValue(keys.get(MILESTONE_LAST_COMMENT), milestoneComment);
                                    row.addValue(keys.get(MILESTONE_CREATED_ON), milestoneCreatedOn);
                                    row.addValue(keys.get(MILESTONE_COMPLETED_ON), milestoneCompletedOn);
                                    row.addValue(keys.get(MILESTONE_OWNER), milestoneOwner);
                                    if (dueOnDate != null) {
                                        row.addValue(keys.get(DUEON), new DateValue(dueOnDate));
                                    }
                                    row.addValue(keys.get(CREATORNAME), creatorName);
                                    if (completedDate != null)
                                        row.addValue(keys.get(COMPLETEDDATE), new DateValue(completedDate));
                                    if (createdDate != null)
                                        row.addValue(keys.get(CREATEDDATE), new DateValue(createdDate));
                                    row.addValue(keys.get(COMPLETERNAME), completerName);
                                    row.addValue(keys.get(COMPLETERID), completerId);
                                    row.addValue(keys.get(MILESTONE_ID), milestoneIdToRetrieve);
                                    /*if (eventStart != null) {
                                        row.addValue(keys.get(CALENDAR_EVENT_START), new DateValue(eventStart));
                                    }*/
                                    if (keys.containsKey(CALENDAR_EVENT_TYPE)) {
                                        row.addValue(keys.get(CALENDAR_EVENT_TYPE), eventType);
                                    }
                                    row.addValue(keys.get(COUNT), new NumericValue(1));
                                }
                            } else {
                                IRow row = ds.createRow();
                                row.addValue(keys.get(PROJECTNAME), projectName);
                                row.addValue(keys.get(ANNOUNCEMENT), announcement);
                                row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                                row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                                row.addValue(keys.get(PROJECT_CREATION_DATE), projectCreatedAt);
                                row.addValue(keys.get(MILESTONENAME), milestoneName);
                                if (milestoneDeadline != null) {
                                    row.addValue(keys.get(DEADLINE), new DateValue(milestoneDeadline));
                                }
                                row.addValue(keys.get(TODOLISTDESC), todoListDesc);
                                row.addValue(keys.get(TODOLISTNAME), todoListName);
                                row.addValue(keys.get(TODOLISTID), todoListId);
                                row.addValue(keys.get(TODOLISTPRIVATE), todoListPrivacy);
                                row.addValue(keys.get(MILESTONE_LAST_COMMENT), milestoneComment);
                                row.addValue(keys.get(MILESTONE_CREATED_ON), milestoneCreatedOn);
                                row.addValue(keys.get(MILESTONE_COMPLETED_ON), milestoneCompletedOn);
                                row.addValue(keys.get(MILESTONE_OWNER), milestoneOwner);
                                row.addValue(keys.get(MILESTONE_ID), milestoneIdToRetrieve);
                                if (keys.containsKey(CALENDAR_EVENT_TYPE)) {
                                    row.addValue(keys.get(CALENDAR_EVENT_TYPE), eventType);
                                }
                                /*if (eventStart != null) {
                                    row.addValue(keys.get(CALENDAR_EVENT_START), eventStart);
                                }
                                row.addValue(keys.get(CALENDAR_EVENT_TYPE), eventType);*/
                            }
                        } catch (Exception e) {
                            IRow row = ds.createRow();
                            row.addValue(keys.get(PROJECTNAME), projectName);
                            row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                            row.addValue(keys.get(PROJECT_CREATION_DATE), projectCreatedAt);
                            row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                            row.addValue(keys.get(MILESTONENAME), milestoneName);
                            if (milestoneDeadline != null) {
                                row.addValue(keys.get(DEADLINE), new DateValue(milestoneDeadline));
                            }
                            row.addValue(keys.get(TODOLISTDESC), todoListDesc);
                            row.addValue(keys.get(TODOLISTNAME), todoListName);
                            row.addValue(keys.get(TODOLISTID), todoListId);
                            row.addValue(keys.get(TODOLISTPRIVATE), todoListPrivacy);
                            row.addValue(keys.get(MILESTONE_LAST_COMMENT), milestoneComment);
                            row.addValue(keys.get(MILESTONE_CREATED_ON), milestoneCreatedOn);
                            row.addValue(keys.get(MILESTONE_COMPLETED_ON), milestoneCompletedOn);
                            row.addValue(keys.get(MILESTONE_OWNER), milestoneOwner);
                            row.addValue(keys.get(MILESTONE_ID), milestoneIdToRetrieve);
                            if (keys.containsKey(CALENDAR_EVENT_TYPE)) {
                                row.addValue(keys.get(CALENDAR_EVENT_TYPE), eventType);
                            }
                            /*row.addValue(keys.get(CALENDAR_EVENT_START), eventStart);
                            row.addValue(keys.get(CALENDAR_EVENT_TYPE), eventType);*/
                        }
                    }
                } else {
                    IRow row = ds.createRow();
                    row.addValue(keys.get(PROJECTNAME), projectName);
                    row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                    row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                    row.addValue(keys.get(PROJECT_CREATION_DATE), projectCreatedAt);
                }

                for (Map.Entry<String, MilestoneInfo> entry : milestoneMap.entrySet()) {
                    if (!entry.getValue().published) {
                        IRow row = ds.createRow();
                        row.addValue(keys.get(PROJECTNAME), projectName);
                        row.addValue(keys.get(PROJECTSTATUS), projectStatus);
                        row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                        row.addValue(keys.get(MILESTONE_CREATED_ON), entry.getValue().milestoneCreatedOn);
                        row.addValue(keys.get(MILESTONE_COMPLETED_ON), entry.getValue().milestoneCompletedOn);
                        row.addValue(keys.get(MILESTONE_OWNER), entry.getValue().milestoneOwner);
                        if (entry.getValue().milestoneDeadline != null) {
                            row.addValue(keys.get(DEADLINE), entry.getValue().milestoneDeadline);
                        }
                        row.addValue(keys.get(MILESTONENAME), entry.getValue().milestoneName);
                        row.addValue(keys.get(MILESTONE_ID), entry.getKey());
                        if (keys.containsKey(CALENDAR_EVENT_TYPE)) {
                            row.addValue(keys.get(CALENDAR_EVENT_TYPE), entry.getValue().type);
                        }
                        /*row.addValue(keys.get(CALENDAR_EVENT_START), entry.getValue().startDate);
                        row.addValue(keys.get(CALENDAR_EVENT_TYPE), entry.getValue().type);*/
                    }
                }
                if (!source.isIncrementalRefresh() || lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                    IDataStorage.insertData(ds);
                } else {
                    StringWhere stringWhere = new StringWhere(keys.get(PROJECTID), projectIdToRetrieve);
                    IDataStorage.updateData(ds, Arrays.asList((IWhere) stringWhere));
                }
            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected String getUpdateKeyName() {
        return PROJECTID;
    }

    private static class MilestoneInfo {
        String milestoneName = null;
        Date milestoneCreatedOn = null;
        Date milestoneCompletedOn = null;
        Date milestoneDeadline = null;
        String milestoneOwner = null;
        boolean published = false;
        String type;
        Date startDate;

        private MilestoneInfo(String milestoneName, Date milestoneCreatedOn, Date milestoneCompletedOn, Date milestoneDeadline, String milestoneOwner, String type, Date startDate) {
            this.milestoneName = milestoneName;
            this.milestoneCreatedOn = milestoneCreatedOn;
            this.milestoneCompletedOn = milestoneCompletedOn;
            this.milestoneDeadline = milestoneDeadline;
            this.milestoneOwner = milestoneOwner;
            this.type = type;
            this.startDate = startDate;
        }
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ITEMID, CREATORNAME, CREATORID, RESPONSIBLEPARTYNAME,
                RESPONSIBLEPARTYID, CONTENT, COMPLETED, CREATEDDATE, COMPLETEDDATE,
                TODOLISTNAME, MILESTONENAME, DEADLINE, PROJECTNAME, PROJECTSTATUS, MILESTONE_COMPLETED_ON,
                PROJECTID, TODOLISTDESC, TODOLISTID, TODOLISTPRIVATE, COMPLETERNAME, COMPLETERID, COUNT, ITEMCYCLE, MILESTONE_LAST_COMMENT, DUEON,
                MILESTONE_CREATED_ON, MILESTONE_OWNER, MILESTONE_OWNER, ANNOUNCEMENT, PROJECT_CREATION_DATE, MILESTONE_ID, CALENDAR_EVENT_TYPE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension itemDim = new AnalysisDimension(keys.get(ITEMID), true);
        analysisItems.add(itemDim);
        analysisItems.add(new AnalysisDimension(keys.get(CREATORNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(CREATORID), true));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLEPARTYNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLEPARTYID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTENT), TODO_ITEM_NAME));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETED), true));
        AnalysisDateDimension createdDim = new AnalysisDateDimension(keys.get(CREATEDDATE), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(createdDim);
        AnalysisDateDimension completedDim = new AnalysisDateDimension(keys.get(COMPLETEDDATE), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(completedDim);
        AnalysisDateDimension dueOnDim = new AnalysisDateDimension(keys.get(DUEON), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(dueOnDim);
        AnalysisDateDimension milestoneCompletedOnDim = new AnalysisDateDimension(keys.get(MILESTONE_COMPLETED_ON), true, AnalysisDateDimension.DAY_LEVEL);
        analysisItems.add(milestoneCompletedOnDim);
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(ANNOUNCEMENT), true));
        //analysisItems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_START), true));
        analysisItems.add(new AnalysisDimension(keys.get(CALENDAR_EVENT_TYPE), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONENAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONE_OWNER), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(MILESTONE_LAST_COMMENT), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(DEADLINE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTSTATUS), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(MILESTONE_CREATED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTDESC), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TODOLISTPRIVATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETERNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPLETERID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(PROJECT_CREATION_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisStep(keys.get(ITEMCYCLE), true, AnalysisDateDimension.DAY_LEVEL, createdDim, completedDim, itemDim));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public int getVersion() {
        return 8;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new BaseCampTodo1To2(this), new BaseCampTodo2To3(this), new BaseCampTodo3To4(this),
                new BaseCampTodo4To5(this), new BaseCampTodo5To6(this), new BaseCampTodo6To7(this), new BaseCampTodo7To8(this));
    }
}

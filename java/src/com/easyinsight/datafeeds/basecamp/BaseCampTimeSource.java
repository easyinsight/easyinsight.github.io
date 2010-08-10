package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
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
 * Time: 9:37:38 PM
 */
public class BaseCampTimeSource extends BaseCampBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String PROJECTID = "Project ID";

    public static final String DATE = "Date";
    public static final String PERSONID = "Person ID";
    public static final String HOURS = "Hours";
    public static final String DESCRIPTION = "Description";
    public static final String PROJECTNAME = "Project Name";

    public static final String COUNT = "Count";
    private static final String PERSONNAME = "Person Name";

    public static final String TODOID = "Todo ID";

    public BaseCampTimeSource() {
        setFeedName("Time Tracking");
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_TIME;
    }

    


    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        BaseCampCompositeSource baseCampCompositeSource = (BaseCampCompositeSource) parentDefinition;
        String url = baseCampCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);


        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        if (token == null && credentials.getUserName() != null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.BASECAMP_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        } else if (token != null && credentials != null && credentials.getUserName() != null && !"".equals(credentials.getUserName()) &&
                !credentials.getUserName().equals(token.getTokenValue())) {
            token.setTokenValue(credentials.getUserName());
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
            BaseCampCache basecampCache = baseCampCompositeSource.getOrCreateCache(client);
            Document projects = runRestRequest("/projects.xml", client, builder, url, null, true);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectIdToRetrieve = queryField(curProject, "id/text()");
                String projectName = queryField(curProject, "name/text()");                
                loadingProgress(i, projectNodes.size(), "Synchronizing with time tracking data of " + projectName + "...", false);
                String projectStatus = queryField(curProject, "status/text()");
                if ("template".equals(projectStatus)) {
                    continue;
                }
                if (!baseCampCompositeSource.isIncludeArchived() && "archived".equals(projectStatus)) {
                    continue;
                }
                if (!baseCampCompositeSource.isIncludeInactive() && "inactive".equals(projectStatus)) {
                    continue;
                }

                boolean hasEntries = false;
                EIPageInfo info = new EIPageInfo();
                info.currentPage = 1;
                    do {
                        try {
                            Document todoLists;
                            if (info.currentPage == 1) {
                                todoLists = runRestRequest("/projects/" + projectIdToRetrieve + "/time_entries.xml", client, builder, url, info, false);
                            } else {
                                todoLists = runRestRequest("/projects/" + projectIdToRetrieve + "/time_entries.xml?page=" + info.currentPage, client, builder, url, info, false);
                            }

                            Nodes todoListNodes = todoLists.query("/time-entries/time-entry");
                            for(int j = 0;j < todoListNodes.size();j++) {
                                Node todoListNode = todoListNodes.get(j);
                                String personID = queryField(todoListNode, "person-id/text()");
                                String timeHours = queryField(todoListNode, "hours/text()");
                                String timeDescription = queryField(todoListNode, "description/text()");
                                String todoItemID = queryField(todoListNode, "todo-item-id/text()");
                                String personName = basecampCache.getUserName(personID);
                                Date date = deadlineFormat.parse(queryField(todoListNode, "date/text()"));

                                IRow row = ds.createRow();
                                row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                                row.addValue(keys.get(PROJECTNAME), projectName);
                                row.addValue(keys.get(PERSONNAME), personName);
                                row.addValue(keys.get(PERSONID), personID);
                                row.addValue(keys.get(HOURS), new NumericValue(Double.parseDouble(timeHours)));
                                row.addValue(keys.get(DESCRIPTION), timeDescription);
                                row.addValue(keys.get(TODOID), todoItemID);
                                row.addValue(keys.get(DATE), new DateValue(date));
                                row.addValue(keys.get(COUNT), new NumericValue(1));
                                hasEntries = true;
                            }
                            if (dataStorage != null) {
                                dataStorage.insertData(ds);
                                ds = new DataSet();
                            }
                        } catch (Exception e) {
                            if ("Premature end of file.".equals(e.getMessage())) {
                                LogClass.debug(e.getMessage());
                            } else if ("403 error".equals(e.getMessage())) {
                                break;
                            } else {
                                LogClass.error("Error " + e.getMessage() + " while retrieving page " + info.currentPage + " of "+ info.MaxPages);
                                LogClass.error(e);   
                            }
                        }
                    } while(info.currentPage++ < info.MaxPages);
                if (!hasEntries) {
                    IRow row = ds.createRow();
                    row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                    row.addValue(keys.get(PROJECTNAME), projectName);
                    if (dataStorage != null) {
                        dataStorage.insertData(ds);
                        ds = new DataSet();
                    }
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (dataStorage == null) {
            return ds;
        } else {
            return null;
        }
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(PERSONID, PERSONNAME, HOURS, DESCRIPTION, PROJECTNAME, PROJECTID, COUNT, TODOID, DATE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension projectDim = new AnalysisDimension(keys.get(PROJECTID), true);
        projectDim.setHidden(true);
        analysisItems.add(projectDim);
        analysisItems.add(new AnalysisDimension(keys.get(DESCRIPTION), true));
        AnalysisDimension personDim = new AnalysisDimension(keys.get(PERSONID), true);
        personDim.setHidden(true);
        analysisItems.add(personDim);
        analysisItems.add(new AnalysisDimension(keys.get(PERSONNAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECTNAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(HOURS), AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDateDimension(keys.get(DATE), true,  AnalysisDateDimension.DAY_LEVEL));
        AnalysisDimension todoDimension = new AnalysisDimension(keys.get(TODOID), true);
        todoDimension.setHidden(true);
        analysisItems.add(todoDimension);
        return analysisItems;
    }

}

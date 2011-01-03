package com.easyinsight.datafeeds.pivotaltracker;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Apr 14, 2010
 * Time: 2:57:07 PM
 */
public class PivotalTrackerBaseSource extends ServerDataSourceDefinition {

    public static final String XMLDATETIMEFORMAT = "yyyy/MM/dd HH:mm:ss zzz";

    public static final String PROJECT_ID = "Project ID";
    public static final String PROJECT_NAME = "Project Name";
    public static final String PROJECT_INITIAL_VELOCITY = "Project Initial Velocity";
    public static final String PROJECT_CURRENT_VELOCITY = "Project Current Velocity";
    public static final String PROJECT_LABELS = "Project Tags";

    public static final String ITERATION_ID = "Iteration ID";
    public static final String ITERATION_STATE = "Iteration State";
    public static final String ITERATION_START_DATE = "Iteration Start Date";
    public static final String ITERATION_FINISH_DATE = "Iteration Finish Date";
    public static final String ITERATION_NUMBER = "Iteration Number";

    public static final String STORY_NAME = "Story Name";
    public static final String STORY_REQUESTED_BY = "Story Requested By";
    public static final String STORY_OWNED_BY = "Story Owned By";
    public static final String STORY_STATE = "Story State";
    public static final String STORY_TYPE = "Story Type";
    public static final String STORY_ESTIMATE = "Story Estimate";
    public static final String STORY_LABELS = "Story Tags";
    public static final String STORY_CREATED_AT = "Story Created At";
    public static final String STORY_UPDATED_AT = "Story Updated At";
    public static final String STORY_ACCEPTED_AT = "Story Accepted At";
    public static final String STORY_URL = "Story URL";
    public static final String STORY_COUNT = "Story Count";

    private String ptUserName;
    private String ptPassword;

    public String getPtUserName() {
        return ptUserName;
    }

    public void setPtUserName(String ptUserName) {
        this.ptUserName = ptUserName;
    }

    public String getPtPassword() {
        return ptPassword;
    }

    public void setPtPassword(String ptPassword) {
        this.ptPassword = ptPassword;
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        FilterValueDefinition filterValueDefinition = new FilterValueDefinition(findAnalysisItem(PivotalTrackerBaseSource.ITERATION_STATE), true, Arrays.asList((Object)"Current"));
        kpis.add(KPIUtil.createKPIWithFilters("Stories Remaining in Current Iterations", "user.png", (AnalysisMeasure) findAnalysisItem(PivotalTrackerBaseSource.STORY_COUNT),
                Arrays.asList((FilterDefinition) filterValueDefinition), KPI.BAD, 7));
        return kpis;
    }

    protected String getToken(String userName, String password) throws IOException, ParsingException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        HttpMethod restMethod = new GetMethod("https://www.pivotaltracker.com/services/v3/tokens/active");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        client.executeMethod(restMethod);
        doc = new Builder().build(restMethod.getResponseBodyAsStream());
        String token = doc.query("/token/guid/text()").get(0).getValue();
        return token;
    }

    @Override
    public String validateCredentials() {
        try {
            getToken(ptUserName, ptPassword);
            return null;
        } catch (ParsingException pe) {
            return "These credentials were rejected as invalid by Pivotal Tracker. Please double check your values for username and password.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PERSONAL;
    }

    protected Document runRestRequest(HttpClient httpClient, String token, String url) throws IOException, ParsingException {
        HttpMethod restMethod = new GetMethod(url);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        restMethod.setRequestHeader("X-TrackerToken", token);
        httpClient.executeMethod(restMethod);
        return new Builder().build(restMethod.getResponseBodyAsStream());
    }

    protected String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    public PivotalTrackerBaseSource() {
        setFeedName("Pivotal Tracker");
    }

    public FeedType getFeedType() {
        return FeedType.PIVOTAL_TRACKER;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
    }

    public void exchangeTokens(EIConnection conn) throws Exception {
        Token tokenObj = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.PIVOTAL_TRACKER_TOKEN, getDataFeedID(), false, conn);
        try {
            if (tokenObj == null && ptUserName != null && ptPassword != null) {
                String tokenValue = getToken(ptUserName, ptPassword);
                tokenObj = new Token();
                tokenObj.setTokenValue(tokenValue);
                tokenObj.setTokenType(TokenStorage.PIVOTAL_TRACKER_TOKEN);
                tokenObj.setUserID(SecurityUtil.getUserID());
                new TokenStorage().saveToken(tokenObj, getDataFeedID(), conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID) {
        Token tokenObj = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.PIVOTAL_TRACKER_TOKEN, getDataFeedID(), false, conn);
        String token = tokenObj.getTokenValue();
        try {
            DataSet dataSet = new DataSet();
            DateFormat dateFormat = new SimpleDateFormat(XMLDATETIMEFORMAT);
            HttpClient httpClient = new HttpClient();
            Document projects = runRestRequest(httpClient, token, "https://www.pivotaltracker.com/services/v3/projects");
            Nodes projectNodes = projects.query("/projects/project");
            for (int i = 0; i < projectNodes.size(); i++) {
                Node curProject = projectNodes.get(i);
                String id = queryField(curProject, "id/text()");
                String name = queryField(curProject, "name/text()");
                loadingProgress(i, projectNodes.size(), "Synchronizing with " + name + "...", callDataID);
                int initialVelocity = Integer.parseInt(queryField(curProject, "initial_velocity/text()"));
                int currentVelocity = Integer.parseInt(queryField(curProject, "current_velocity/text()"));
                String labels = queryField(curProject, "labels/text()");
                Document currentIterations = runRestRequest(httpClient, token, "https://www.pivotaltracker.com/services/v3/projects/" + id +
                    "/iterations/current");
                parseIterations(keys, dataSet, dateFormat, id, name, initialVelocity, currentVelocity, labels, currentIterations, "Current");
                Document backlogIterations = runRestRequest(httpClient, token, "https://www.pivotaltracker.com/services/v3/projects/" + id +
                    "/iterations/backlog");
                parseIterations(keys, dataSet, dateFormat, id, name, initialVelocity, currentVelocity, labels, backlogIterations, "Backlog");
                Document doneIterations = runRestRequest(httpClient, token, "https://www.pivotaltracker.com/services/v3/projects/" + id +
                    "/iterations/done");
                parseIterations(keys, dataSet, dateFormat, id, name, initialVelocity, currentVelocity, labels, doneIterations, "Done");
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void parseIterations(Map<String, Key> keys, DataSet dataSet, DateFormat dateFormat, String id, String name,
                                 int initialVelocity, int currentVelocity, String labels, Document iterations, String iterationState) throws ParseException {
        Nodes iterationNodes = iterations.query("/iterations/iteration");
        for (int j = 0; j < iterationNodes.size(); j++) {
            Node curIteration = iterationNodes.get(j);
            String iterationID = queryField(curIteration, "id/text()");
            String iterationStartDateString = queryField(curIteration, "start/text()");
            Date iterationStartDate = null;
            if (iterationStartDateString != null) {
                iterationStartDate = dateFormat.parse(iterationStartDateString);
            }
            String iterationFinishDateString = queryField(curIteration, "finish/text()");
            Date iterationFinishDate = null;
            if (iterationFinishDateString != null) {
                iterationFinishDate = dateFormat.parse(iterationFinishDateString);
            }
            Nodes stories = curIteration.query("stories/story");
            for (int k = 0; k < stories.size(); k++) {
                Node storyNode = stories.get(k);
                String storyName = queryField(storyNode, "name/text()");
                String requestedBy = queryField(storyNode, "requested_by/text()");
                String ownedBy = queryField(storyNode, "owned_by/text()");
                String state = queryField(storyNode, "current_state/text()");
                String estimateString = queryField(storyNode, "estimate/text()");
                int estimate = 0;
                if (estimateString != null) {
                    estimate = Integer.parseInt(estimateString);
                }
                String storyType = queryField(storyNode, "story_type/text()");
                String storyLabels = queryField(storyNode, "labels/text()");
                String storyCreatedAtString = queryField(storyNode, "created_at/text()");
                Date storyCreatedAt = null;
                if (storyCreatedAtString != null) {
                    storyCreatedAt = dateFormat.parse(storyCreatedAtString);
                }
                String storyUpdatedAtString = queryField(storyNode, "updated_at/text()");
                Date storyUpdatedAt = null;
                if (storyUpdatedAtString != null) {
                    storyUpdatedAt = dateFormat.parse(storyUpdatedAtString);
                }
                String storyAcceptedAtString = queryField(storyNode, "accepted_at/text()");
                Date storyAcceptedAt = null;
                if (storyAcceptedAtString != null) {
                    storyAcceptedAt = dateFormat.parse(storyAcceptedAtString);
                }
                String storyURL = queryField(storyNode, "url/text()");
                IRow row = dataSet.createRow();
                row.addValue(keys.get(PROJECT_NAME), name);
                row.addValue(keys.get(PROJECT_ID), id);
                row.addValue(keys.get(PROJECT_INITIAL_VELOCITY), initialVelocity);
                row.addValue(keys.get(PROJECT_CURRENT_VELOCITY), currentVelocity);
                row.addValue(keys.get(PROJECT_LABELS), labels);
                row.addValue(keys.get(ITERATION_ID), iterationID);
                row.addValue(keys.get(ITERATION_START_DATE), iterationStartDate);
                row.addValue(keys.get(ITERATION_FINISH_DATE), iterationFinishDate);
                row.addValue(keys.get(ITERATION_STATE), iterationState);
                row.addValue(keys.get(STORY_NAME), storyName);
                row.addValue(keys.get(STORY_REQUESTED_BY), requestedBy);
                row.addValue(keys.get(STORY_OWNED_BY), ownedBy);
                row.addValue(keys.get(STORY_STATE), state);
                row.addValue(keys.get(STORY_TYPE), storyType);
                row.addValue(keys.get(STORY_URL), storyURL);
                row.addValue(keys.get(STORY_LABELS), storyLabels);
                row.addValue(keys.get(STORY_ESTIMATE), estimate);
                row.addValue(keys.get(STORY_COUNT), new NumericValue(1));
                row.addValue(keys.get(STORY_CREATED_AT), storyCreatedAt);
                row.addValue(keys.get(STORY_UPDATED_AT), storyUpdatedAt);
                row.addValue(keys.get(STORY_ACCEPTED_AT), storyAcceptedAt);
            }
        }
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(PROJECT_ID, PROJECT_NAME, PROJECT_INITIAL_VELOCITY, PROJECT_CURRENT_VELOCITY, PROJECT_LABELS,
                ITERATION_ID, ITERATION_START_DATE, ITERATION_FINISH_DATE, ITERATION_NUMBER, ITERATION_STATE, STORY_NAME,
                STORY_REQUESTED_BY, STORY_OWNED_BY, STORY_STATE, STORY_ESTIMATE, STORY_LABELS, STORY_CREATED_AT, STORY_TYPE,
                STORY_UPDATED_AT, STORY_ACCEPTED_AT, STORY_URL, STORY_COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_ID), true));
        analysisItems.add(new AnalysisMeasure(keys.get(PROJECT_INITIAL_VELOCITY), AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(keys.get(PROJECT_CURRENT_VELOCITY), AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisList(keys.get(PROJECT_LABELS), true, ","));
        analysisItems.add(new AnalysisDimension(keys.get(PROJECT_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(ITERATION_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(ITERATION_START_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(ITERATION_FINISH_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(ITERATION_NUMBER), true));
        analysisItems.add(new AnalysisDimension(keys.get(ITERATION_STATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_NAME), true));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_REQUESTED_BY), true));
        analysisItems.add(new AnalysisMeasure(keys.get(STORY_ESTIMATE), AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(STORY_COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisList(keys.get(STORY_LABELS), true, ","));
        analysisItems.add(new AnalysisDateDimension(keys.get(STORY_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(STORY_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(STORY_ACCEPTED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_OWNED_BY), true));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_STATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_TYPE), true));
        analysisItems.add(new AnalysisDimension(keys.get(STORY_URL), true));
        return analysisItems;
    }
}

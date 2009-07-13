package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.analysis.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.ws.security.util.XmlSchemaDateFormat;
import org.jetbrains.annotations.NotNull;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Nodes;
import nu.xom.Node;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * User: James Boe
 * Date: Jun 16, 2009
 * Time: 9:37:38 PM
 */
public class BaseCampTimeSource extends ServerDataSourceDefinition {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String PROJECTID = "Project ID";

    public static final String DATE = "Date";
    public static final String PERSONID = "Person ID";
    public static final String HOURS = "Hours";
    public static final String DESCRIPTION = "Description";

    public static final String COUNT = "Count";
    private static final String PERSONNAME = "Person Name";

    public static final String TODOID = "Todo ID";

    public BaseCampTimeSource() {
        setFeedName("Time Tracking");
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_TIME;
    }

    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    private Document runRestRequest(String path, HttpClient client, Builder builder, String url, EIPageInfo pageInfo) throws BaseCampLoginException {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
            if(pageInfo != null) {
                pageInfo.MaxPages = Integer.parseInt(restMethod.getResponseHeader("X-Pages").getValue());
            }
        }
        catch (nu.xom.ParsingException e) {
                throw new BaseCampLoginException("Invalid username/password.");
        }
        catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return doc;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition) {
        BaseCampCompositeSource baseCampCompositeSource = (BaseCampCompositeSource) parentDefinition;
        String url = baseCampCompositeSource.getUrl();
        DateFormat df = new XmlSchemaDateFormat();
        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        HttpClient client = getHttpClient(credentials.getUserName(), credentials.getPassword());
        Builder builder = new Builder();
        Map<String, String> peopleCache = new HashMap<String, String>();
        try {
            Document projects = runRestRequest("/projects.xml", client, builder, url, null);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectIdToRetrieve = queryField(curProject, "id/text()");

                EIPageInfo info = new EIPageInfo();
                info.currentPage = 1;
                do {
                    Document todoLists = runRestRequest("/projects/" + projectIdToRetrieve + "/time_entries.xml?page=" + info.currentPage, client, builder, url, info);
                    Nodes todoListNodes = todoLists.query("/time-entries/time-entry");
                    for(int j = 0;j < todoListNodes.size();j++) {
                        Node todoListNode = todoListNodes.get(j);
                        String personID = queryField(todoListNode, "person-id/text()");
                        String timeHours = queryField(todoListNode, "hours/text()");
                        String timeDescription = queryField(todoListNode, "description/text()");
                        String todoItemID = queryField(todoListNode, "todo-item-id/text()");
                        String personName = retrieveContactInfo(client, builder, peopleCache, personID, url);

                        IRow row = ds.createRow();
                        row.addValue(keys.get(PROJECTID), projectIdToRetrieve);
                        row.addValue(keys.get(PERSONNAME), personName);
                        row.addValue(keys.get(PERSONID), personID);
                        row.addValue(keys.get(HOURS), new NumericValue(Double.parseDouble(timeHours)));
                        row.addValue(keys.get(DESCRIPTION), timeDescription);
                        row.addValue(keys.get(TODOID), todoItemID);
                        row.addValue(keys.get(COUNT), new NumericValue(1));
                    }
                } while(info.currentPage++ < info.MaxPages);
            }
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return ds;
    }

    private String retrieveContactInfo(HttpClient client, Builder builder, Map<String, String> peopleCache, String contactId, String url) throws BaseCampLoginException {
        String contactName = null;
        if(contactId != null) {
            contactName = peopleCache.get(contactId);
            if(contactName == null) {
                Document contactInfo = runRestRequest("/contacts/person/" + contactId, client, builder, url, null);
                contactName = queryField(contactInfo, "/person/first-name/text()") + " " + queryField(contactInfo, "/person/last-name/text()");
                peopleCache.put(contactId, contactName);
            }

        }
        return contactName;
    }

    private static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(PERSONID, PERSONNAME, HOURS, DESCRIPTION, PROJECTID, COUNT, TODOID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension projectDim = new AnalysisDimension(keys.get(PROJECTID), true);
        projectDim.setHidden(true);
        analysisItems.add(projectDim);
        analysisItems.add(new AnalysisDimension(keys.get(DESCRIPTION), true));
        analysisItems.add(new AnalysisDimension(keys.get(PERSONID), true));
        analysisItems.add(new AnalysisDimension(keys.get(PERSONNAME), true));
        analysisItems.add(new AnalysisMeasure(keys.get(HOURS), AggregationTypes.SUM));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        analysisItems.add(new AnalysisDimension(keys.get(TODOID), true));
        return analysisItems;
    }

    private class EIPageInfo {
        public int MaxPages;
        public int currentPage;
    }

}

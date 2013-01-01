package com.easyinsight.datafeeds.sendgrid;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.dataset.DataSet;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Apr 14, 2010
 * Time: 9:49:28 PM
 */
public class SendGridFeed extends Feed {

    private static final String GET_STRING = "https://sendgrid.com/api/stats.get.xml";
    private static DateFormat outboundDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public List<FilterDefinition> getIntrinsicFilters(EIConnection conn) {
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        AnalysisItem dateField = null;
        for (AnalysisItem analysisItem : getFields()) {
            if (analysisItem.getKey().toKeyString().equals(SendGridDataSource.DATE)) {
                dateField = analysisItem;
            }
        }
        rollingFilterDefinition.setField(dateField);
        rollingFilterDefinition.setIntrinsic(true);
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.WEEK);
        rollingFilterDefinition.setApplyBeforeAggregation(true);
        return Arrays.asList((FilterDefinition) rollingFilterDefinition);
    }

    private String getUserName() {
        SendGridDataSource sendGridDataSource = (SendGridDataSource) getDataSource();
        return sendGridDataSource.getSgUserName();
    }

    private String getPassword() {
        SendGridDataSource sendGridDataSource = (SendGridDataSource) getDataSource();
        return sendGridDataSource.getSgPassword();
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn, WSAnalysisDefinition report, List<FilterDefinition> otherFilters, FilterDefinition requester) throws ReportException {
        try {
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            if (SendGridDataSource.CATEGORY.equals(analysisItem.getKey().toKeyString())) {
                HttpClient httpClient = new HttpClient();
                String url = "https://sendgrid.com/api/stats.get.xml";
                GetMethod getMethod = new GetMethod(url);
                getMethod.setQueryString(new NameValuePair[] { new NameValuePair("api_user", getUserName()),
                    new NameValuePair("api_key", getPassword()), new NameValuePair("list", "true")});
                httpClient.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());
                Nodes categories = doc.query("/categories/category");
                for (int i = 0; i < categories.size(); i++) {
                    Node categoryNode = categories.get(i);
                    String category = categoryNode.getValue();
                    metadata.addValue(analysisItem, new StringValue(category), insightRequestMetadata);
                }
            } else if (SendGridDataSource.DATE.equals(analysisItem.getKey().toKeyString())) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.YEAR, -1);
                metadata.addValue(analysisItem, new DateValue(cal.getTime()), insightRequestMetadata);
                metadata.addValue(analysisItem, new DateValue(new Date()), insightRequestMetadata);
            }
            return metadata;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {

            Date startDate = null;
            Date endDate = null;

            boolean retrieveCategory = false;

            for (AnalysisItem analysisItem : analysisItems) {
                if (SendGridDataSource.CATEGORY.equals(analysisItem.getKey().toKeyString())) {
                    retrieveCategory = true;
                }
            }

            Map<String, List<Key>> keys = new HashMap<String, List<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                List<Key> keyList = keys.get(analysisItem.getKey().toKeyString());
                if (keyList == null) {
                    keyList = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyList);
                }
                keyList.add(analysisItem.createAggregateKey());
            }

            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition.getField().getKey().toKeyString().equals(SendGridDataSource.DATE)) {
                    if (filterDefinition instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition dateRange = (FilterDateRangeDefinition) filterDefinition;
                        startDate = dateRange.getStartDate();
                        endDate = dateRange.getEndDate();
                    } else if (filterDefinition instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filterDefinition;
                        endDate = insightRequestMetadata.getNow();
                        startDate = new Date(MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, endDate));
                    }
                }
            }
            String startDateString = outboundDateFormat.format(startDate);
            String endDateString = outboundDateFormat.format(endDate);
            DataSet dataSet = new DataSet();

            Document statsDocs = null;
            if (retrieveCategory) {
                HttpClient httpClient = new HttpClient();
                String url = "https://sendgrid.com/api/stats.get.xml";
                GetMethod getMethod = new GetMethod(url);
                getMethod.setQueryString(new NameValuePair[] { new NameValuePair("api_user", getUserName()),
                    new NameValuePair("api_key", getPassword()), new NameValuePair("list", "true")});
                httpClient.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());
                if (doc.toString().indexOf("error") != -1) {
                    //throw new ReportException(new CredentialRequirement(getFeedID(), getName(), CredentialsDefinition.STANDARD_USERNAME_PW));
                }
                Nodes categories = doc.query("/categories/category");
                List<NameValuePair> pairs = Arrays.asList(new NameValuePair("api_user", getUserName()),
                    new NameValuePair("api_key", getPassword()), new NameValuePair("start_date", startDateString), new NameValuePair("end_date", endDateString));
                pairs = new ArrayList<NameValuePair>(pairs);
                for (int i = 0; i < categories.size(); i++) {
                    Node categoryNode = categories.get(i);
                    String category = categoryNode.getValue();


                    if (analysisItems.size() <= 2) {
                        IRow row = dataSet.createRow();
                        addValue(keys, SendGridDataSource.CATEGORY, new StringValue(category), row);
                        addValue(keys, SendGridDataSource.DATE, new DateValue(new Date()), row);
                    }
                    pairs.add(new NameValuePair("category[]", category));
                }

                if (analysisItems.size() > 2) {
                    NameValuePair[] array = new NameValuePair[pairs.size()];
                    pairs.toArray(array);
                    getMethod.setQueryString(array);
                    httpClient.executeMethod(getMethod);
                    statsDocs = new Builder().build(getMethod.getResponseBodyAsStream());
                }
            } else {
                HttpClient httpClient = new HttpClient();
                String url = "https://sendgrid.com/api/stats.get.xml";
                GetMethod getMethod = new GetMethod(url);
                getMethod.setQueryString(new NameValuePair[] { new NameValuePair("api_user", getUserName()),
                    new NameValuePair("api_key", getPassword()), new NameValuePair("start_date", startDateString), new NameValuePair("end_date", endDateString)});

                httpClient.executeMethod(getMethod);
                statsDocs = new Builder().build(getMethod.getResponseBodyAsStream());
            }
            if (statsDocs != null) {
                dataSet = new DataSet();
                Nodes days = statsDocs.query("/stats/day");
                for (int i = 0; i < days.size(); i++) {
                    IRow row = dataSet.createRow();
                    Node dayNode = days.get(i);
                    String dateString = dayNode.query("date/text()").get(0).getValue();


                    Date date = outboundDateFormat.parse(dateString);
                    addValue(keys, SendGridDataSource.DATE, new DateValue(date), row);

                    int requests = getValue(dayNode, "requests/text()");
                    addValue(keys, SendGridDataSource.REQUESTS, new NumericValue(requests), row);

                    int delivered = getValue(dayNode, "delivered/text()");
                    addValue(keys, SendGridDataSource.DELIVERED, new NumericValue(delivered), row);

                    int bounces = getValue(dayNode, "bounces/text()");
                    addValue(keys, SendGridDataSource.BOUNCES, new NumericValue(bounces), row);

                    int repeatBounces = getValue(dayNode, "repeat_bounces/text()");
                    addValue(keys, SendGridDataSource.REPEAT_BOUNCES, new NumericValue(repeatBounces), row);

                    int unsubscribes = getValue(dayNode, "unsubscribes/text()");
                    addValue(keys, SendGridDataSource.UNSUBSCRIBES, new NumericValue(unsubscribes), row);

                    int repeatUnsubscribes = getValue(dayNode, "repeat_unsubscribes/text()");
                    addValue(keys, SendGridDataSource.REPEAT_UNSUBSCRIBES, new NumericValue(repeatUnsubscribes), row);

                    int clicks = getValue(dayNode, "clicks/text()");
                    addValue(keys, SendGridDataSource.CLICKS, new NumericValue(clicks), row);

                    int opens = getValue(dayNode, "opens/text()");
                    addValue(keys, SendGridDataSource.OPENS, new NumericValue(opens), row);

                    int spamReports = getValue(dayNode, "spamreports/text()");
                    addValue(keys, SendGridDataSource.SPAM_REPORTS, new NumericValue(spamReports), row);

                    int repeatSpamReports = getValue(dayNode, "repeat_spamreports/text()");
                    addValue(keys, SendGridDataSource.REPEAT_SPAM_REPORTS, new NumericValue(repeatSpamReports), row);

                    int invalidEmails = getValue(dayNode, "invalid_email/text()");
                    addValue(keys, SendGridDataSource.INVALID_EMAILS, new NumericValue(invalidEmails), row);

                    Nodes categoryNodes = dayNode.query("category/text()");
                    if (categoryNodes.size() > 0) {
                        String category = categoryNodes.get(0).getValue();
                        addValue(keys, SendGridDataSource.CATEGORY, new StringValue(category), row);
                    }

                }
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void addValue(Map<String, List<Key>> keys, String keyName, Value value, IRow row) {
        List<Key> keyList = keys.get(keyName);
        if (keyList != null) {
            for (Key key : keyList) {
                row.addValue(key, value);
            }
        }
    }

    private int getValue(Node node, String name) {
        Nodes nodes = node.query(name);
        if (nodes.size() > 0) {
            Node child = nodes.get(0);
            return Integer.parseInt(child.getValue());
        } else {
            return 0;
        }
    }
}

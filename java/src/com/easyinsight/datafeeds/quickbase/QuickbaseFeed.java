package com.easyinsight.datafeeds.quickbase;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Account;
import com.easyinsight.users.QuickbaseExternalLogin;
import com.easyinsight.users.User;
import nu.xom.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hibernate.Session;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/16/10
 * Time: 12:44 PM
 */
public class QuickbaseFeed extends Feed {

    private static final String REQUEST = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-20000</options></qdbapi>";
    private static final String REQUEST_2 = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><clist>{2}</clist><fmt>structured</fmt><options>num-20000.skp-{3}</options></qdbapi>";

    private static final String REQUEST_Q = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><query>{2}</query><clist>{3}</clist><fmt>structured</fmt><options>num-20000</options></qdbapi>";
    private static final String REQUEST_Q2 = "<qdbapi><ticket>{0}</ticket><apptoken>{1}</apptoken><query>{2}</query><clist>{3}</clist><fmt>structured</fmt><options>num-20000.skp-{4}</options></qdbapi>";

    private String databaseID;

    private long indexID;

    public QuickbaseFeed(String databaseID) {
        this.databaseID = databaseID;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            // if all analysis items are indexed, retrieve data from the indexed field
            DataSet dataSet = new DataSet();
            boolean indexed = true;
            for (AnalysisItem analysisItem : analysisItems) {
                if ("Wtd Procedures".equals(analysisItem.toDisplay())) {
                    continue;
                }
                if (!analysisItem.getKey().indexed()) {
                    indexed = false;
                }
            }

            if (indexed) {
                DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
                try {
                    insightRequestMetadata.setGmtData(getDataSource().gmtTime());
                    dataSet = source.retrieveData(analysisItems, filters, null, insightRequestMetadata);
                } catch (SQLException e) {
                    LogClass.error(e);
                    throw new RuntimeException(e);
                } finally {
                    source.closeConnection();
                }
            } else {

                QuickbaseCompositeSource quickbaseCompositeSource = (QuickbaseCompositeSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
                String sessionTicket = quickbaseCompositeSource.getSessionTicket();
                try {
                    runQuery(analysisItems, filters, insightRequestMetadata, conn, dataSet, quickbaseCompositeSource, sessionTicket);
                } catch (ReportException e) {
                    if (quickbaseCompositeSource.isPreserveCredentials()) {
                        quickbaseCompositeSource.exchangeTokens(conn, null, null);
                        runQuery(analysisItems, filters, insightRequestMetadata, conn, dataSet, quickbaseCompositeSource, sessionTicket);
                    } else {
                        Session session = Database.instance().createSession(conn);
                        try {
                            Account account = (Account) session.createQuery("from Account where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list().get(0);
                            if (account.getExternalLogin() == null || !(account.getExternalLogin() instanceof QuickbaseExternalLogin)) {
                                throw e;
                            } else {
                                QuickbaseExternalLogin quickbaseExternalLogin = (QuickbaseExternalLogin) account.getExternalLogin();
                                if (quickbaseExternalLogin.getSessionTicket() != null) {
                                    sessionTicket = quickbaseExternalLogin.getSessionTicket();
                                    runQuery(analysisItems, filters, insightRequestMetadata, conn, dataSet, quickbaseCompositeSource, sessionTicket);
                                }
                            }
                        } finally {
                            session.close();
                        }
                    }
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private void runQuery(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, EIConnection conn, DataSet dataSet, QuickbaseCompositeSource quickbaseCompositeSource, String sessionTicket) throws IOException, ParsingException, SQLException {
        String applicationToken = quickbaseCompositeSource.getApplicationToken();
        String host = quickbaseCompositeSource.getHost();
        String fullPath = "https://" + host + "/db/" + databaseID;
        HttpPost httpRequest = new HttpPost(fullPath);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        httpRequest.setHeader("QUICKBASE-ACTION", "API_DoQuery");
        BasicHttpEntity entity = new BasicHttpEntity();
        StringBuilder columnBuilder = new StringBuilder();
        Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
        for (AnalysisItem analysisItem : analysisItems) {
            String[] tokens = analysisItem.getKey().toBaseKey().toKeyString().split("\\.");
            if (tokens.length > 1) {
                String fieldID = tokens[1];
                map.put(fieldID, analysisItem);
                columnBuilder.append(fieldID).append(".");
            }
        }
        StringBuilder queryBuilder = new StringBuilder();
        String query = null;
        for (FilterDefinition filter : filters) {
            String key = filter.getField().getKey().toBaseKey().toKeyString().split("\\.")[1];
            if (filter instanceof FilterValueDefinition) {
                FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                String value = filterValueDefinition.getFilteredValues().get(0).toString();
                queryBuilder.append("{'" + key + "'.CT.'" + value + "'}");
                queryBuilder.append(" AND ");
            } else if (filter instanceof FilterDateRangeDefinition) {
                FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                long startTime = filterDateRangeDefinition.getStartDate().getTime();
                long endTime = filterDateRangeDefinition.getEndDate().getTime();
                queryBuilder.append("{'" + key + "'.OAF.'" + startTime + "'} AND {'" + key + "'.BF.'" + endTime + "'");
                queryBuilder.append(" AND ");
            } else if (filter instanceof RollingFilterDefinition) {
                RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                Date now = insightRequestMetadata.getNow();
                long startTime = MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, now);
                long endTime = MaterializedRollingFilterDefinition.findEndDate(rollingFilterDefinition, now);
                long workingEndDate = endTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
                long workingStartDate = startTime + insightRequestMetadata.getUtcOffset() * 1000 * 60;
                if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.AFTER) {
                    queryBuilder.append("{'" + key + "'.BF.'" + workingEndDate + "'");
                    queryBuilder.append(" AND ");
                } else if (rollingFilterDefinition.getCustomBeforeOrAfter() == RollingFilterDefinition.BEFORE) {
                    queryBuilder.append("{'" + key + "'.OAF.'" + workingStartDate + "'}");
                    queryBuilder.append(" AND ");
                } else {
                    queryBuilder.append("{'" + key + "'.OAF.'" + workingStartDate + "'} AND {'" + key + "'.BF.'" + workingEndDate + "'");
                    queryBuilder.append(" AND ");
                }
            }
        }
        if (queryBuilder.length() > 0) {
            query = queryBuilder.toString();
            query = query.substring(0, query.length() - 5);
        }
        columnBuilder.deleteCharAt(columnBuilder.length() - 1);
        int masterCount = 0;
        int count;

        do {
            count = 0;
            String requestBody;
            if (query == null) {
                if (masterCount == 0) {
                    requestBody = MessageFormat.format(REQUEST, sessionTicket, applicationToken, columnBuilder.toString());
                } else {
                    requestBody = MessageFormat.format(REQUEST_2, sessionTicket, applicationToken, columnBuilder.toString(), String.valueOf(masterCount));
                }
            } else {
                if (masterCount == 0) {
                    requestBody = MessageFormat.format(REQUEST_Q, sessionTicket, applicationToken, query, columnBuilder.toString());
                } else {
                    requestBody = MessageFormat.format(REQUEST_Q2, sessionTicket, applicationToken, query, columnBuilder.toString(), String.valueOf(masterCount));
                }
            }
            System.out.println(requestBody);
            byte[] contentBytes = requestBody.getBytes();
            entity.setContent(new ByteArrayInputStream(contentBytes));
            entity.setContentLength(contentBytes.length);
            httpRequest.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            String string = client.execute(httpRequest, responseHandler);
            Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));

            Nodes errors = doc.query("/qdbapi/errcode/text()");
            if (errors.size() > 0) {
                Node error = errors.get(0);
                if (!"0".equals(error.getValue())) {
                    String errorDetail = doc.query("/qdbapi/errdetail/text()").get(0).getValue();
                    throw new ReportException(new DataSourceConnectivityReportFault(errorDetail, getParentSource(conn)));
                }
            }

            Nodes records = doc.query("/qdbapi/table/records/record");
            for (int i = 0; i < records.size(); i++) {
                Element record = (Element) records.get(i);
                IRow row = dataSet.createRow();
                count++;
                masterCount++;
                Elements childElements = record.getChildElements();
                for (int j = 0; j < childElements.size(); j++) {
                    Element childElement = childElements.get(j);
                    if (childElement.getLocalName().equals("f")) {
                        String fieldID = childElement.getAttribute("id").getValue();
                        AnalysisItem analysisItem = map.get(fieldID);
                        String value = childElement.getValue();
                        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION) && !"".equals(value)) {
                            row.addValue(analysisItem.createAggregateKey(), new Date(Long.parseLong(value)));
                        } else {
                            row.addValue(analysisItem.createAggregateKey(), value);
                        }
                    }
                }
            }
        } while (count == 20000);
    }
}

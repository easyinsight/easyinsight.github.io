package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.Pipeline;
import com.easyinsight.storage.DataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 4:00:05 PM
 */
public class SalesforceFeed extends Feed {

    private String sobjectName;

    public SalesforceFeed(String sobjectName) {
        this.sobjectName = sobjectName;
    }

    private static class AuthFailed extends Exception {

    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        DataSet dataSet;
        boolean indexed = true;
        for (AnalysisItem analysisItem : analysisItems) {
            if (!analysisItem.getKey().indexed()) {
                System.out.println(analysisItem.toDisplay() + " was not indexed");
                indexed = false;
            }
        }

        if (indexed) {
            DataStorage source = DataStorage.readConnection(getFields(), getFeedID());
            try {
                insightRequestMetadata.setGmtData(getDataSource().gmtTime());
                dataSet = source.retrieveData(analysisItems, filters, null, insightRequestMetadata);
                return dataSet;
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                source.closeConnection();
            }
        } else {
            SalesforceBaseDataSource salesforceBaseDataSource;
            try {
                salesforceBaseDataSource = (SalesforceBaseDataSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                return getDataSet(salesforceBaseDataSource, analysisItems, filters, insightRequestMetadata);
            } catch (ReportException re) {
                throw re;
            } catch (AuthFailed authFailed) {
                try {
                    salesforceBaseDataSource.refreshTokenInfo();
                    new FeedStorage().updateDataFeedConfiguration(salesforceBaseDataSource);
                    return getDataSet(salesforceBaseDataSource, analysisItems, filters, insightRequestMetadata);
                } catch (ReportException re) {
                    throw re;
                } catch (AuthFailed authFailed1) {
                    throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Salesforce data.", salesforceBaseDataSource));
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private DataSet getDataSet(SalesforceBaseDataSource salesforceBaseDataSource, Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata) throws ReportException, AuthFailed {

        try {

            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT+");
            Set<String> keys = new HashSet<String>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.isDerived()) {
                    continue;
                }
                String keyString = analysisItem.getKey().toKeyString();
                boolean alreadyThere = keys.add(keyString);
                if (alreadyThere) {
                    queryBuilder.append(keyString);
                    queryBuilder.append(",");
                }
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append("+from+");
            queryBuilder.append(sobjectName);
            StringBuilder whereBuilder = new StringBuilder();
            for (FilterDefinition filter : filters) {
                if (!filter.getPipelineName().equals(Pipeline.BEFORE) || !filter.validForQuery()) {
                    continue;
                }
                if (filter instanceof FilterValueDefinition) {
                    FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                    if (filterValueDefinition.getFilteredValues().size() == 1) {
                        whereBuilder.append(filterValueDefinition.getField().getKey().toKeyString());
                        whereBuilder.append("=");
                        whereBuilder.append("'");
                        whereBuilder.append(filterValueDefinition.getFilteredValues().get(0).toString());
                        whereBuilder.append("'");
                    } else if (filterValueDefinition.getFilteredValues().size() > 1) {
                        List<Object> values = filterValueDefinition.getFilteredValues();
                        whereBuilder.append("(");
                        Iterator<Object> iter = values.iterator();
                        while (iter.hasNext()) {
                            Object value = iter.next();
                            whereBuilder.append(filterValueDefinition.getField().getKey().toKeyString());
                            whereBuilder.append("=");
                            whereBuilder.append("'");
                            whereBuilder.append(value.toString());
                            whereBuilder.append("'");
                            if (iter.hasNext()) {
                                whereBuilder.append(" OR ");
                            }
                        }
                        whereBuilder.append(")");
                    }
                } else if (filter instanceof FilterDateRangeDefinition) {
                    SimpleDateFormat sdf;
                    AnalysisDateDimension date = (AnalysisDateDimension) filter.getField();
                    if (date.isDateOnlyField()) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    }
                    FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                    whereBuilder.append(filterDateRangeDefinition.getField().getKey().toKeyString());
                    whereBuilder.append(" >= ");
                    whereBuilder.append(sdf.format(filterDateRangeDefinition.getStartDate()));
                    whereBuilder.append(" AND ");
                    whereBuilder.append(filterDateRangeDefinition.getField().getKey().toKeyString());
                    whereBuilder.append(" <= ");
                    whereBuilder.append(sdf.format(filterDateRangeDefinition.getEndDate()));
                } else if (filter instanceof RollingFilterDefinition) {
                    SimpleDateFormat sdf;
                    AnalysisDateDimension date = (AnalysisDateDimension) filter.getField();
                    if (date.isDateOnlyField()) {
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    } else {
                        sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                    }
                    RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                    Date endDate = insightRequestMetadata.getNow();
                    Date startDate = new Date(MaterializedRollingFilterDefinition.findStartDate(rollingFilterDefinition, endDate));
                    whereBuilder.append(filter.getField().getKey().toKeyString());
                    whereBuilder.append(" >= ");
                    whereBuilder.append(sdf.format(startDate));
                    whereBuilder.append(" AND ");
                    whereBuilder.append(filter.getField().getKey().toKeyString());
                    whereBuilder.append(" <= ");
                    whereBuilder.append(sdf.format(endDate));
                }
                whereBuilder.append(" AND ");
            }
            if (whereBuilder.length() > 0) {
                whereBuilder.delete(whereBuilder.length() - 5, whereBuilder.length() - 1);
            }
            //String where = CharEscapers.uriQueryStringEscaper().escape(whereBuilder.toString());
            String where = URLEncoder.encode(whereBuilder.toString(), "UTF-8");
            //String where = whereBuilder.toString();
            if (where.length() > 0) {
                queryBuilder.append("+WHERE+").append(where);
            }

            String url = salesforceBaseDataSource.getInstanceName() + "/services/data/v20.0/query/?q=" + queryBuilder.toString();
            boolean moreData;
            DataSet dataSet = new DataSet();
            do {
                HttpGet httpRequest = new HttpGet(url);
                httpRequest.setHeader("Accept", "application/xml");
                httpRequest.setHeader("Content-Type", "application/xml");
                httpRequest.setHeader("Authorization", "OAuth " + salesforceBaseDataSource.getAccessToken());


                org.apache.http.client.HttpClient cc = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String string = cc.execute(httpRequest, responseHandler);

                Builder builder = new Builder();
                Document doc = builder.build(new ByteArrayInputStream(string.getBytes()));

                Nodes records = doc.query("/QueryResult/records");
                for (int i = 0; i < records.size(); i++) {
                    IRow row = dataSet.createRow();
                    Node record = records.get(i);
                    for (AnalysisItem analysisItem : analysisItems) {
                        if (analysisItem.isCalculated()) {
                            continue;
                        }
                        Nodes results = record.query(analysisItem.getKey().toKeyString() + "/text()");
                        if (results.size() > 0) {
                            String value = results.get(0).getValue();
                            row.addValue(analysisItem.createAggregateKey(), value);
                        }
                    }
                }

                Nodes nextRecords = doc.query("/QueryResults/nextRecordsUrl/text()");
                if (nextRecords.size() == 1) {
                    url = nextRecords.get(0).getValue();
                    moreData = true;
                } else {
                    moreData = false;
                }
            } while (moreData);

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (HttpResponseException hre) {
            if ("Unauthorized".equals(hre.getMessage())) {
                throw new AuthFailed();
            } else {
                throw new ReportException(new ServerError(hre.getMessage()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

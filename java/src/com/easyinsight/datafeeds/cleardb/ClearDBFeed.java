package com.easyinsight.datafeeds.cleardb;

import com.cleardb.app.ClearDBQueryException;
import com.cleardb.app.Client;
import com.easyinsight.analysis.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/30/10
 * Time: 10:49 PM
 */
public class ClearDBFeed extends Feed {
    private String table;

    public ClearDBFeed(String table) {
        this.table = table;
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        try{
            ClearDBCompositeSource compositeSource = (ClearDBCompositeSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
            DataSet dataSet = new DataSet();
            Client client = new Client(compositeSource.getCdApiKey(), compositeSource.getAppToken());
            StringBuilder selectBuilder = new StringBuilder();
            String from = table;
            StringBuilder whereBuilder = new StringBuilder();
            StringBuilder groupByBuilder = new StringBuilder();
            Collection<AnalysisItem> groupByItems = new HashSet<AnalysisItem>();
            boolean aggregateQuery = insightRequestMetadata.isAggregateQuery();
            List<AnalysisItem> analysisItems = Arrays.asList(analysisItem);
            createSelectClause(analysisItems, selectBuilder, groupByItems, aggregateQuery);
            selectBuilder = selectBuilder.deleteCharAt(selectBuilder.length() - 1);
            String sql = "SELECT " + selectBuilder.toString() + " FROM " + from;
            JSONObject results = client.query(sql);
            JSONArray response = results.getJSONArray("response");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (int i = 0; i < response.length(); i++) {
                JSONObject responseRow = (JSONObject) response.get(i);
                IRow row = dataSet.createRow();
                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    try {
                        row.addValue(analysisItem.createAggregateKey(), dateFormat.parse(responseRow.getString(analysisItem.getKey().toKeyString())));
                    } catch (ParseException e) {
                    }
                } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    row.addValue(analysisItem.createAggregateKey(), responseRow.getDouble(analysisItem.getKey().toKeyString()));
                } else {
                    row.addValue(analysisItem.createAggregateKey(), responseRow.getString(analysisItem.getKey().toKeyString()));
                }
            }
            AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
            for (IRow row : dataSet.getRows()) {
                metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
            }
            return metadata;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            ClearDBCompositeSource compositeSource = (ClearDBCompositeSource) new FeedStorage().getFeedDefinitionData(getDataSource().getParentSourceID(), conn);
            DataSet dataSet = new DataSet();
            Client client = new Client(compositeSource.getCdApiKey(), compositeSource.getAppToken());
            StringBuilder selectBuilder = new StringBuilder();
            String from = table;
            StringBuilder whereBuilder = new StringBuilder();
            StringBuilder groupByBuilder = new StringBuilder();
            Collection<AnalysisItem> groupByItems = new HashSet<AnalysisItem>();
            filters = eligibleFilters(filters);
            boolean aggregateQuery = insightRequestMetadata.isAggregateQuery();
            createSelectClause(analysisItems, selectBuilder, groupByItems, aggregateQuery);
            selectBuilder = selectBuilder.deleteCharAt(selectBuilder.length() - 1);
            createWhereClause(filters, whereBuilder);
            if (aggregateQuery) {
                groupByBuilder = createGroupByClause(groupByBuilder, groupByItems);
            } else {
                groupByItems = new ArrayList<AnalysisItem>();
            }
            String sql = "SELECT " + selectBuilder.toString() + " FROM " + from + " GROUP BY " + groupByBuilder.toString();
            JSONObject results = client.query(sql);
            JSONArray response = results.getJSONArray("response");
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            for (int i = 0; i < response.length(); i++) {
                JSONObject responseRow = (JSONObject) response.get(i);
                IRow row = dataSet.createRow();
                for (AnalysisItem analysisItem : analysisItems) {
                    if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        try {
                            row.addValue(analysisItem.createAggregateKey(), dateFormat.parse(responseRow.getString(analysisItem.getKey().toKeyString())));
                        } catch (ParseException e) {
                        }
                    } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                        row.addValue(analysisItem.createAggregateKey(), responseRow.getDouble(analysisItem.getKey().toKeyString()));
                    } else {
                        row.addValue(analysisItem.createAggregateKey(), responseRow.getString(analysisItem.getKey().toKeyString()));
                    }
                }
            }
            return dataSet;
        } catch (ClearDBQueryException e) {
            if (e.getMessage().equals("Request failed (code 1001). Please check your API key and try again.")) {
                try {
                    throw new ReportException(new DataSourceConnectivityReportFault("It looks like your API key may have changed.", getParentSource(conn)));
                } catch (SQLException e1) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private Collection<FilterDefinition> eligibleFilters(@Nullable Collection<FilterDefinition> filters) {
        Collection<FilterDefinition> eligibleFilters = new ArrayList<FilterDefinition>();
        if (filters != null) {
            for (FilterDefinition filterDefinition : filters) {
                if (filterDefinition.isApplyBeforeAggregation() && filterDefinition.validForQuery()) {
                    eligibleFilters.add(filterDefinition);
                }
            }
        }
        return eligibleFilters;
    }

    private StringBuilder createGroupByClause(StringBuilder groupByBuilder, Collection<AnalysisItem> groupByItems) {
        if (groupByItems.size() > 0) {
            for (AnalysisItem key : groupByItems) {
                String columnName = key.getKey().toKeyString();
                groupByBuilder.append(columnName);
                groupByBuilder.append(",");
            }
            groupByBuilder = groupByBuilder.deleteCharAt(groupByBuilder.length() - 1);
        }
        return groupByBuilder;
    }

    private void createWhereClause(Collection<FilterDefinition> filters, StringBuilder whereBuilder) {
        /*if (filters != null && filters.size() > 0) {
            Iterator<FilterDefinition> filterIter = filters.iterator();
            while (filterIter.hasNext()) {
                FilterDefinition filterDefinition = filterIter.next();
                whereBuilder.append(filterDefinition.toQuerySQL(getTableName()));
                if (filterIter.hasNext()) {
                    whereBuilder.append(" AND ");
                }
            }
        }*/
    }

    private void createSelectClause(Collection<AnalysisItem> reportItems, StringBuilder selectBuilder, Collection<AnalysisItem> groupByItems, boolean aggregateQuery) {
        for (AnalysisItem analysisItem : reportItems) {
            if (analysisItem.isDerived()) {
                throw new RuntimeException("Attempt made to query a derived analysis item");
            }
            String columnName = analysisItem.getKey().toKeyString();
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE) && aggregateQuery) {
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                int aggregation = analysisMeasure.getQueryAggregation();
                if (aggregation == AggregationTypes.SUM) {
                    columnName = "SUM(" + columnName + ") as " + columnName;
                } else if (aggregation == AggregationTypes.AVERAGE) {
                    columnName = "AVG(" + columnName + ") as " + columnName;
                } else if (aggregation == AggregationTypes.COUNT) {
                    columnName = "COUNT(DISTINCT " + columnName + ") as " + columnName;
                } else if (aggregation == AggregationTypes.MAX) {
                    columnName = "MAX(" + columnName + ") as " + columnName;
                } else if (aggregation == AggregationTypes.MIN) {
                    columnName = "MIN(" + columnName + ") as " + columnName;
                } else {
                    groupByItems.add(analysisItem);
                }
            } else {
                groupByItems.add(analysisItem);
            }
            selectBuilder.append(columnName);
            selectBuilder.append(",");
        }
    }
}

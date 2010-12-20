package com.easyinsight.api.v3;

import com.easyinsight.analysis.*;
import com.easyinsight.api.*;
import com.easyinsight.api.Row;
import com.easyinsight.api.v2.DataSourceInfo;
import com.easyinsight.core.*;
import com.easyinsight.core.StringValue;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.userupload.UploadPolicy;

import javax.jws.WebParam;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jun 21, 2010
 * Time: 12:13:29 PM
 */
public abstract class EIV3API implements IEIV3API {

    protected abstract long getUserID();

    protected abstract long getAccountID();

    public boolean validateCredentials() {
        return true;
    }

    public String defineDataSource(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name="fields") FieldDefinition[] fields) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, new FieldDefinitions(fields), null, conn, true);
            dataStorage = callData.dataStorage;
            dataStorage.truncate();
            dataStorage.insertData(callData.dataSet);
            dataStorage.commit();
            conn.commit();
            return null;
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void replaceRows(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "rows") Row[] rows, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);

            CallData callData = convertData(dataSourceName, rows, conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            dataStorage.truncate();
            dataStorage.insertData(callData.dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void addRow(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "row") Row row, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        if (row == null) {
            throw new ServiceRuntimeException("You must specify at least one Row.");
        }
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, row, conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            dataStorage.insertData(callData.dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void addRows(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "rows") Row[] rows, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, rows, conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            dataStorage.insertData(callData.dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void updateRow(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "row") Row row, @WebParam(name = "where") Where where, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        if (row == null) {
            throw new ServiceRuntimeException("You must specify at least one Row.");
        }
        DataStorage dataStorage = null;
        try {
            List<IWhere> wheres = createWheres(where);
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, new RowBasedFields(row), Arrays.asList(row), conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            dataStorage.updateData(callData.dataSet, wheres);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void updateRows(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "rows") Row[] rows, @WebParam(name = "where") Where where, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            List<IWhere> wheres = createWheres(where);
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, rows, conn, changeDataSourceToMatch);
            dataStorage = callData.dataStorage;
            dataStorage = callData.dataStorage;
            dataStorage.updateData(callData.dataSet, wheres);
            dataStorage.commit();
            conn.commit();
        } catch (ServiceRuntimeException sre) {
            LogClass.debug(sre.getMessage());
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw sre;
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new ServiceRuntimeException("An internal error occurred on attempting to process the provided data. The error has been logged for our engineers to examine.");
        } finally {
            conn.setAutoCommit(true);
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public void deleteRows(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "where") Where where) {
        throw new ServiceRuntimeException("This operation has not yet been implemented--coming soon!");
    }

    public DataSourceInfo getSourceInfo(@WebParam(name = "dataSourceName") String dataSourceName) {
        throw new ServiceRuntimeException("This operation has not yet been implemented--coming soon!");
    }

    private static class CallData {
        DataStorage dataStorage;
        DataSet dataSet;

        private CallData(DataStorage dataStorage, DataSet dataSet) {
            this.dataStorage = dataStorage;
            this.dataSet = dataSet;
        }
    }

    private CallData convertData(String dataSourceName, Row row, EIConnection conn, boolean updateIfNecessary) throws Exception {
        if (row == null) {
            throw new ServiceRuntimeException("You must include at least one row for this operation.");
        }
        return convertData(dataSourceName, new RowBasedFields(row), Arrays.asList(row), conn, updateIfNecessary);
    }

    private CallData convertData(String dataSourceName, Row[] rows, EIConnection conn, boolean updateIfNecessary) throws Exception {
        if (rows == null) {
            throw new ServiceRuntimeException("You must include at least one row for this operation.");
        }
        return convertData(dataSourceName, new RowBasedFields(rows[0]), Arrays.asList(rows), conn, updateIfNecessary);
    }

    private static interface Fields {
        public List<AnalysisItem> toAnalysisItems();
    }

    private static class FieldDefinitions implements Fields {
        private FieldDefinition[] fieldDefinitions;

        private FieldDefinitions(FieldDefinition[] fieldDefinitions) {
            this.fieldDefinitions = fieldDefinitions;
        }

        public List<AnalysisItem> toAnalysisItems() {
            List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                AnalysisItem analysisItem;
                if (fieldDefinition.getDisplayName() == null) {
                    fieldDefinition.setDisplayName(fieldDefinition.getInternalName());
                }
                if (fieldDefinition.getFieldType() == FieldType.GROUPING) {
                    analysisItem = new AnalysisDimension(new NamedKey(fieldDefinition.getInternalName()), fieldDefinition.getDisplayName());
                } else if (fieldDefinition.getFieldType() == FieldType.DATE) {
                    analysisItem = new AnalysisDateDimension(new NamedKey(fieldDefinition.getInternalName()), fieldDefinition.getDisplayName(), AnalysisDateDimension.DAY_LEVEL);
                } else if (fieldDefinition.getFieldType() == FieldType.LATITUDE) {
                    analysisItem = new AnalysisLatitude(new NamedKey(fieldDefinition.getInternalName()), true, fieldDefinition.getDisplayName());
                } else if (fieldDefinition.getFieldType() == FieldType.LONGITUDE) {
                    analysisItem = new AnalysisLongitude(new NamedKey(fieldDefinition.getInternalName()), true, fieldDefinition.getDisplayName());
                } else if (fieldDefinition.getFieldType() == FieldType.POSTAL_CODE) {
                    analysisItem = new AnalysisZipCode(new NamedKey(fieldDefinition.getInternalName()), true);
                } else if (fieldDefinition.getFieldType() == FieldType.MEASURE) {
                    int aggregation = AggregationTypes.SUM;
                    if (fieldDefinition.getMeasureAggregationType() != null) {
                        switch (fieldDefinition.getMeasureAggregationType()) {
                            case SUM:
                                aggregation = AggregationTypes.SUM;
                                break;
                            case AVERAGE:
                                aggregation = AggregationTypes.AVERAGE;
                                break;
                            case COUNT:
                                aggregation = AggregationTypes.COUNT;
                                break;
                            case MIN:
                                aggregation = AggregationTypes.MIN;
                                break;
                            case MAX:
                                aggregation = AggregationTypes.MAX;
                                break;
                            case MEDIAN:
                                aggregation = AggregationTypes.MEDIAN;
                                break;
                        }
                    }
                    int formatting = FormattingConfiguration.NUMBER;
                    if (fieldDefinition.getMeasureFormattingType() != null) {
                        switch (fieldDefinition.getMeasureFormattingType()) {
                            case BYTES:
                                formatting = FormattingConfiguration.BYTES;
                                break;
                            case CURRENCY:
                                formatting = FormattingConfiguration.CURRENCY;
                                break;
                            case INTERVAL_MILLISECONDS:
                                formatting = FormattingConfiguration.MILLISECONDS;
                                break;
                            case INTERVAL_SECONDS:
                                formatting = FormattingConfiguration.SECONDS;
                                break;
                            case PERCENTAGE:
                                formatting = FormattingConfiguration.PERCENTAGE;
                                break;
                        }
                    }
                    analysisItem = new AnalysisMeasure(new NamedKey(fieldDefinition.getInternalName()), fieldDefinition.getDisplayName(), aggregation,
                            true, formatting);
                } else {
                    throw new RuntimeException();
                }

                analysisItems.add(analysisItem);
            }
            return analysisItems;
        }
    }

    private static class RowBasedFields implements Fields {

        private Row row;

        private RowBasedFields(Row row) {
            this.row = row;
        }

        public List<AnalysisItem> toAnalysisItems() {
            List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
            StringPair[] stringPairs = row.getStringPairs();
            if (stringPairs != null) {
                for (StringPair stringPair : stringPairs) {
                    analysisItems.add(new AnalysisDimension(stringPair.getKey(), true));
                }
            }
            NumberPair[] numberPairs = row.getNumberPairs();
            if (numberPairs != null) {
                for (NumberPair numberPair : numberPairs) {
                    analysisItems.add(new AnalysisMeasure(numberPair.getKey(), AggregationTypes.SUM));
                }
            }
            DatePair[] datePairs = row.getDatePairs();
            if (datePairs != null) {
                for (DatePair datePair : datePairs) {
                    analysisItems.add(new AnalysisDateDimension(datePair.getKey(), true, AnalysisDateDimension.DAY_LEVEL));
                }
            }
            return analysisItems;
        }
    }

    private CallData convertData(String dataSourceName, Fields fields, List<Row> rows, EIConnection conn, boolean updateIfNecessary) throws Exception {
        if (dataSourceName == null) {
            throw new ServiceRuntimeException("You must specify a data source name or API key.");
        }
        DataStorage dataStorage;
        DataSet dataSet;
        Map<Long, Boolean> dataSourceIDs = findDataSourceIDsByName(dataSourceName, conn);
        List<AnalysisItem> analysisItems = fields.toAnalysisItems();
        if (updateIfNecessary) {
            if (dataSourceIDs.size() == 0) {
                long userID = getUserID();
                // create new data source
                FeedDefinition feedDefinition = new FeedDefinition();
                if (dataSourceName.length() < 3) {
                    throw new ServiceRuntimeException("The data source name must be at least three characters.");
                }
                if (dataSourceName.length() > 30) {
                    throw new ServiceRuntimeException("The data source name must be less than thirty characters.");
                }
                feedDefinition.setFeedName(dataSourceName);
                feedDefinition.setUncheckedAPIEnabled(true);
                feedDefinition.setValidatedAPIEnabled(true);
                UploadPolicy uploadPolicy = new UploadPolicy(userID, getAccountID());
                feedDefinition.setUploadPolicy(uploadPolicy);
                feedDefinition.setFields(analysisItems);
                FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
                dataStorage = result.getTableDefinitionMetadata();
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                Map.Entry<Long, Boolean> entry = dataSourceIDs.entrySet().iterator().next();
                if (!entry.getValue()) {
                    throw new ServiceRuntimeException("This data source has been set to prohibit use of changeDataSourceToMatch as true.");
                }
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(entry.getKey());
                boolean newFieldsFound = false;
                List<AnalysisItem> previousItems = feedDefinition.getFields();
                for (AnalysisItem newItem : analysisItems) {
                    boolean newKey = true;
                    for (AnalysisItem previousItem : previousItems) {
                        if (newItem.getKey().equals(previousItem.getKey()) && newItem.getType() == previousItem.getType()) {
                            // matched the item...
                            newKey = false;
                        }
                    }
                    if (newKey) {
                        newFieldsFound = true;
                    }
                }
                if (newFieldsFound) {
                    feedDefinition.setFields(analysisItems);
                    feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
                    dataStorage.migrate(previousItems, analysisItems, false);
                    new DataSourceInternalService().updateComposites(feedDefinition, conn);
                } else {
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
                }
            }
            if (rows == null) {
                dataSet = new DataSet();
            } else {
                dataSet = toDataSet(rows);
            }
        } else {
            if (dataSourceIDs.size() == 0) {
                throw new ServiceRuntimeException("No data source was found by that name or API key.");
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that name. Please specify an API key for the data source instead.");
            } else {
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceIDs.keySet().iterator().next(), conn);
                DataTransformation dataTransformation = new DataTransformation(feedDefinition);
                if (rows == null) {
                    dataSet = new DataSet();
                } else {
                    dataSet = dataTransformation.toDataSet(rows);
                }
                dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
            }
        }
        return new CallData(dataStorage, dataSet);
    }

    private Map<Long, Boolean> findDataSourceIDsByName(String dataSourceName, Connection conn) throws SQLException {
        Map<Long, Boolean> dataSourceIDs = new HashMap<Long, Boolean>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID, DATA_FEED.UNCHECKED_API_ENABLED" +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, user WHERE " +
                    "UPLOAD_POLICY_USERS.user_id = user.user_id AND user.user_id = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND (DATA_FEED.FEED_NAME = ? OR " +
                "DATA_FEED.API_KEY = ?) AND DATA_FEED.VISIBLE = ?");
        queryStmt.setLong(1, getUserID());
        queryStmt.setString(2, dataSourceName);
        queryStmt.setString(3, dataSourceName);
        queryStmt.setBoolean(4, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dataSourceIDs.put(rs.getLong(1), rs.getBoolean(2));
        }
        return dataSourceIDs;
    }

    protected final DataSet toDataSet(Row row) {
        DataSet dataSet = new DataSet();
        dataSet.addRow(toRow(row));
        return dataSet;
    }

    protected final DataSet toDataSet(List<Row> rows) {
        DataSet dataSet = new DataSet();
        for (Row row : rows) {
            dataSet.addRow(toRow(row));
        }
        return dataSet;
    }

    private IRow toRow(Row row) {
        IRow transformedRow = new com.easyinsight.analysis.Row();
        StringPair[] stringPairs = row.getStringPairs();
        if (stringPairs != null) {
            for (StringPair stringPair : stringPairs) {
                if (stringPair.getKey() == null) {
                    throw new ServiceRuntimeException("StringPair with value " + stringPair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(stringPair.getKey(), stringPair.getValue() == null ? new EmptyValue() : new StringValue(stringPair.getValue()));
            }
        }
        NumberPair[] numberPairs = row.getNumberPairs();
        if (numberPairs != null) {
            for (NumberPair numberPair : numberPairs) {
                if (numberPair.getKey() == null) {
                    throw new ServiceRuntimeException("NumberPair with value " + numberPair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(numberPair.getKey(), new NumericValue(numberPair.getValue()));
            }
        }
        DatePair[] datePairs = row.getDatePairs();
        if (datePairs != null) {
            for (DatePair datePair : datePairs) {
                if (datePair.getKey() == null) {
                    throw new ServiceRuntimeException("DatePair with value " + datePair.getValue() + " had no key--key is a required field.");
                }
                transformedRow.addValue(datePair.getKey(), datePair.getValue() == null ? new EmptyValue() : new DateValue(datePair.getValue()));
            }
        }
        return transformedRow;
    }

    protected final List<IWhere> createWheres(Where where) {
        List<IWhere> wheres = new ArrayList<IWhere>();
        StringWhere[] stringWheres = where.getStringWheres();
        if (stringWheres != null) {
            for (StringWhere stringWhere : stringWheres) {
                if (stringWhere.getKey() == null) {
                    throw new ServiceRuntimeException("StringWhere with value " + stringWhere.getValue() + " had no key--key is a required field.");
                }
                wheres.add(new com.easyinsight.storage.StringWhere(new NamedKey(stringWhere.getKey()), stringWhere.getValue()));
            }
        }
        NumberWhere[] numberWheres = where.getNumberWheres();
        if (numberWheres != null) {
            for (NumberWhere numberWhere : numberWheres) {
                if (numberWhere.getKey() == null) {
                    throw new ServiceRuntimeException("NumberWhere with value " + numberWhere.getValue() + " had no key--key is a required field.");
                }
                Comparison comparison = numberWhere.getComparison();
                if (comparison == null) {
                    throw new ServiceRuntimeException("NumberWhere with key " + numberWhere.getKey() + " had no comparison--comparison is a required field.");
                }
                wheres.add(new com.easyinsight.storage.NumericWhere(new NamedKey(numberWhere.getKey()), numberWhere.getValue(), comparison.createStorageComparison()));
            }
        }
        DateWhere[] dateWheres = where.getDateWheres();
        if (dateWheres != null) {
            for (DateWhere dateWhere : dateWheres) {
                if (dateWhere.getKey() == null) {
                    throw new ServiceRuntimeException("DateWhere with value " + dateWhere.getValue() + " had no key--key is a required field.");
                }
                Comparison comparison = dateWhere.getComparison();
                if (comparison == null) {
                    throw new ServiceRuntimeException("DateWhere with key " + dateWhere.getKey() + " had no comparison--comparison is a required field.");
                }
                wheres.add(new com.easyinsight.storage.DateWhere(new NamedKey(dateWhere.getKey()), dateWhere.getValue(), comparison.createStorageComparison()));
            }
        }
        DayWhere[] dayWheres = where.getDayWheres();
        if (dayWheres != null) {
            for (DayWhere dayWhere : dayWheres) {
                if (dayWhere.getKey() == null) {
                    throw new ServiceRuntimeException("DayWhere with value " + dayWhere.getDayOfYear() + " had no key--key is a required field.");
                }
                if (dayWhere.getYear() == 0) {
                    throw new ServiceRuntimeException("DayWhere with key " + dayWhere.getKey() + " was set to year 0.");
                }
                wheres.add(new com.easyinsight.storage.DayWhere(new NamedKey(dayWhere.getKey()), dayWhere.getYear(), dayWhere.getDayOfYear()));
            }
        }
        return wheres;
    }
}

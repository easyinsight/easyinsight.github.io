package com.easyinsight.api.v2;

import com.easyinsight.analysis.*;
import com.easyinsight.api.*;
import com.easyinsight.api.Row;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
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
public abstract class EIV2API implements IEIV2API {

    protected abstract long getUserID();

    protected abstract long getAccountID();

    public boolean validateCredentials() {
        return true;
    }

    public void replaceRows(@WebParam(name = "dataSourceName") String dataSourceName, @WebParam(name = "rows") Row[] rows, @WebParam(name = "changeDataSourceToMatch") boolean changeDataSourceToMatch) {
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, Arrays.asList(rows), conn, changeDataSourceToMatch);
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
            CallData callData = convertData(dataSourceName, Arrays.asList(rows), conn, changeDataSourceToMatch);
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
        DataStorage dataStorage = null;
        try {
            List<IWhere> wheres = createWheres(where);
            conn.setAutoCommit(false);
            CallData callData = convertData(dataSourceName, Arrays.asList(row), conn, changeDataSourceToMatch);
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
            CallData callData = convertData(dataSourceName, Arrays.asList(rows), conn, changeDataSourceToMatch);
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public DataSourceInfo getSourceInfo(@WebParam(name = "dataSourceName") String dataSourceName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private static class CallData {
        DataStorage dataStorage;
        DataSet dataSet;

        private CallData(DataStorage dataStorage, DataSet dataSet) {
            this.dataStorage = dataStorage;
            this.dataSet = dataSet;
        }
    }

    private CallData convertData(String dataSourceName, Row row, EIConnection conn, boolean updateIfNecessary) throws SQLException {
        return convertData(dataSourceName, Arrays.asList(row), conn, updateIfNecessary);
    }

    private CallData convertData(String dataSourceName, List<Row> rows, EIConnection conn, boolean updateIfNecessary) throws SQLException {
        DataStorage dataStorage;
        DataSet dataSet;
        Map<Long, Boolean> dataSourceIDs = findDataSourceIDsByName(dataSourceName, conn);
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        Row firstRow = rows.get(0);
        StringPair[] stringPairs = firstRow.getStringPairs();
        if (stringPairs != null) {
            for (StringPair stringPair : stringPairs) {
                analysisItems.add(new AnalysisDimension(stringPair.getKey(), true));
            }
        }
        NumberPair[] numberPairs = firstRow.getNumberPairs();
        if (numberPairs != null) {
            for (NumberPair numberPair : numberPairs) {
                analysisItems.add(new AnalysisMeasure(numberPair.getKey(), AggregationTypes.SUM));
            }
        }
        DatePair[] datePairs = firstRow.getDatePairs();
        if (datePairs != null) {
            for (DatePair datePair : datePairs) {
                analysisItems.add(new AnalysisDateDimension(datePair.getKey(), true, AnalysisDateDimension.DAY_LEVEL));
            }
        }
        if (updateIfNecessary) {
            if (dataSourceIDs.size() == 0) {
                long userID = getUserID();
                // create new data source
                FeedDefinition feedDefinition = new FeedDefinition();
                if (dataSourceName == null || dataSourceName.length() < 3) {
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
                throw new ServiceRuntimeException("More than one data source was found by that name.");
            } else {
                Map.Entry<Long, Boolean> entry = dataSourceIDs.entrySet().iterator().next();
                if (!entry.getValue()) {
                    throw new ServiceRuntimeException("This data source does not allow use of the unchecked API.");
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
                } else {
                    dataStorage = DataStorage.writeConnection(feedDefinition, conn, getAccountID());
                }
            }
            dataSet = toDataSet(rows);
        } else {
            if (dataSourceIDs.size() == 0) {
                throw new ServiceRuntimeException("No data source was found by that API key.");
            } else if (dataSourceIDs.size() > 1) {
                throw new ServiceRuntimeException("More than one data source was found by that API key--this should never happen.");
            } else {
                FeedStorage feedStorage = new FeedStorage();
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataSourceIDs.keySet().iterator().next(), conn);
                DataTransformation dataTransformation = new DataTransformation(feedDefinition);
                dataSet = dataTransformation.toDataSet(rows);
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
                "DATA_FEED.API_KEY = ?)");
        queryStmt.setLong(1, getUserID());
        queryStmt.setString(2, dataSourceName);
        queryStmt.setString(3, dataSourceName);
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

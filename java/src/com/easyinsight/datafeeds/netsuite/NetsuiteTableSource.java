package com.easyinsight.datafeeds.netsuite;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.netsuite.client.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/8/14
 * Time: 8:45 AM
 */
public class NetsuiteTableSource extends ServerDataSourceDefinition {

    public static final String INTERNAL_ID = "InternalId";

    private String table;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    private transient SearchResult searchResult;

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        if (getFields().size() > 0) {
            return;
        }
        try {
            fieldBuilder.addField(INTERNAL_ID, new AnalysisDimension());
            NetsuiteCompositeSource netsuiteCompositeSource = (NetsuiteCompositeSource) parentDefinition;
            SearchRecordBasic searchRecordBasic = createSearchRecord();
            if (netsuiteCompositeSource.getPort() == null) {
                System.out.println("NULL PORT");
            }
            if (searchRecordBasic instanceof ItemSearchBasic) {
                // limit to inventory items
                ItemSearchBasic itemSearchBasic = (ItemSearchBasic) searchRecordBasic;
                SearchEnumMultiSelectField type = new SearchEnumMultiSelectField();
                type.getSearchValue().add("_inventoryItem");
                type.setOperator(SearchEnumMultiSelectFieldOperator.ANY_OF);
                itemSearchBasic.setType(type);
            }
            searchResult = netsuiteCompositeSource.getPort().search(searchRecordBasic);
            RecordList recordList = searchResult.getRecordList();
            List<Record> records = recordList.getRecord();
            if (records.size() > 0) {
                Record record = records.get(0);
                Method[] methods = record.getClass().getMethods();
                for (Method m : methods) {
                    if (m.getName().startsWith("get")) {
                        String fieldName = m.getName().substring("get".length());
                        Object o = m.invoke(record);
                        if (o instanceof String) {
                            String s = (String) o;
                            //System.out.println("string " + s);
                            if ("InternalId".equals(fieldName)) {
                                continue;
                            }
                            fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                        } else if (o instanceof Number) {
                            Number n = (Number) o;
                            //System.out.println("number " + n);
                            fieldBuilder.addField(fieldName, new AnalysisMeasure(fieldName));
                        } else if (o instanceof XMLGregorianCalendar) {
                            XMLGregorianCalendar cal = (XMLGregorianCalendar) o;
                            //System.out.println("cal " + cal);
                            fieldBuilder.addField(fieldName, new AnalysisDateDimension(fieldName));
                        } else if (o instanceof RecordRef) {
                            RecordRef recordRef = (RecordRef) o;
                            //System.out.println("record ref " + recordRef.getExternalId());
                            fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                        }
                    }
                }
            }
            fieldBuilder.addField("Count", new AnalysisMeasure());
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM NETSUITE_TABLE WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO NETSUITE_TABLE (DATA_SOURCE_ID, NETSUITE_TABLE) " +
                "VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, table);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT NETSUITE_TABLE FROM NETSUITE_TABLE WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            table = rs.getString(1);
        }
        queryStmt.close();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.NETSUITE_TABLE;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    @Override
    protected String getUpdateKeyName() {
        return "InternalId";
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {

            NetsuiteCompositeSource netsuiteCompositeSource = (NetsuiteCompositeSource) parentDefinition;
            if (netsuiteCompositeSource.getPort() == null) {
                System.out.println("NULL PORT");
            }
            SearchRecordBasic searchRecordBasic = createSearchRecord();

            if (searchRecordBasic instanceof ItemSearchBasic) {
                ItemSearchBasic itemSearchBasic = (ItemSearchBasic) searchRecordBasic;
                SearchEnumMultiSelectField type = new SearchEnumMultiSelectField();
                type.getSearchValue().add("_inventoryItem");
                type.setOperator(SearchEnumMultiSelectFieldOperator.ANY_OF);
                itemSearchBasic.setType(type);
            }
            if (lastRefreshDate != null) {
                SearchDateField lastModified = new SearchDateField();
                lastModified.setOperator(SearchDateFieldOperator.AFTER);
                GregorianCalendar cal = (GregorianCalendar) GregorianCalendar.getInstance();
                cal.setTime(lastRefreshDate);
                cal.add(Calendar.DAY_OF_YEAR, -2);
                XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
                lastModified.setSearchValue(xmlGregorianCalendar);
                Method method = searchRecordBasic.getClass().getMethod("setLastModifiedDate", SearchDateField.class);
                method.invoke(searchRecordBasic, lastModified);
                //itemSearchBasic.setLastModifiedDate(lastModified);
            }
            int currentPage = -1;
            int pageSize = 0;
            String searchID = null;
            do {
                RecordList recordList;
                if (searchID == null) {
                    searchResult = netsuiteCompositeSource.getPort().search(searchRecordBasic);
                    currentPage = searchResult.getPageIndex();
                    pageSize = searchResult.getTotalPages();
                    searchID = searchResult.getSearchId();
                } else {
                    searchResult = netsuiteCompositeSource.getPort().searchMoreWithId(searchID, currentPage + 1);
                    currentPage = searchResult.getPageIndex();
                }

                recordList = searchResult.getRecordList();
                List<Record> records = recordList.getRecord();
                for (Record record : records) {
                    DataSet dataSet = new DataSet();
                    IRow row = dataSet.createRow();
                    String idFieldValue = null;
                    Method[] methods = record.getClass().getMethods();
                    for (Method m : methods) {
                        if (m.getName().startsWith("get")) {
                            String fieldName = m.getName().substring("get".length());

                            Object o = m.invoke(record);
                            if (o instanceof String) {
                                String s = (String) o;
                                //System.out.println("string " + s);
                                if ("InternalId".equals(fieldName)) {
                                    idFieldValue = s;
                                }
                                row.addValue(keys.get(fieldName), s);
                            } else if (o instanceof Number) {
                                Number n = (Number) o;
                                //System.out.println("number " + n);
                                row.addValue(keys.get(fieldName), n);
                            } else if (o instanceof XMLGregorianCalendar) {
                                XMLGregorianCalendar cal = (XMLGregorianCalendar) o;
                                //System.out.println("cal " + cal);
                                row.addValue(keys.get(fieldName), cal.toGregorianCalendar().getTime());
                            } else if (o instanceof RecordRef) {
                                RecordRef recordRef = (RecordRef) o;
                                //System.out.println("record ref " + recordRef.getExternalId());
                                row.addValue(keys.get(fieldName), recordRef.getExternalId());
                            }
                        }
                    }
                    if (lastRefreshDate == null || lastRefreshDate.getTime() < 100) {
                        IDataStorage.insertData(dataSet);
                    } else {
                        StringWhere stringWhere = new StringWhere(keys.get("InternalId"), idFieldValue);
                        IDataStorage.updateData(dataSet, Arrays.asList((IWhere) stringWhere));
                    }
                }
            } while (currentPage < pageSize);
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private SearchRecordBasic createSearchRecord() {
        try {
            Class clazz = Class.forName("com.easyinsight.datafeeds.netsuite.client." + table + "SearchBasic");
            return (SearchRecordBasic) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

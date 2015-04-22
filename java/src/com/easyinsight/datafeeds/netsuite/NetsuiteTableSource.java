package com.easyinsight.datafeeds.netsuite;

import com.easyinsight.PasswordStorage;
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
    private String searchID;
    private String accountID;
    private String nsUsername;
    private String nsPassword;
    private String netsuiteRole;

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getNsUsername() {
        return nsUsername;
    }

    public void setNsUsername(String nsUsername) {
        this.nsUsername = nsUsername;
    }

    public String getNsPassword() {
        return nsPassword;
    }

    public void setNsPassword(String nsPassword) {
        this.nsPassword = nsPassword;
    }

    public String getNetsuiteRole() {
        return netsuiteRole;
    }

    public void setNetsuiteRole(String netsuiteRole) {
        this.netsuiteRole = netsuiteRole;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public String getSearchID() {
        return searchID;
    }

    public void setSearchID(String searchID) {
        this.searchID = searchID;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    private transient SearchResult searchResult;

    private transient NetSuitePortType port;

    @Override
    protected void beforeRefresh(EIConnection conn) {
        super.beforeRefresh(conn);
        if (port == null) {
            try {
                NetSuiteService service = new NetSuiteService(new URL("https://webservices.na1.netsuite.com/wsdl/v2014_1_0/netsuite.wsdl"));


                port = service.getNetSuitePort();

                ((BindingProvider) port).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

                Passport passport = new Passport();
                passport.setEmail(nsUsername);
                passport.setAccount(accountID);
                passport.setPassword(nsPassword);
                RecordRef role = new RecordRef();
                role.setInternalId(netsuiteRole);
                passport.setRole(role);
                Status status = port.login(passport).getStatus();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private NetSuitePortType getOrCreatePort() {
        if (port == null) {
            try {
                NetSuiteService service = new NetSuiteService(new URL("https://webservices.na1.netsuite.com/wsdl/v2014_1_0/netsuite.wsdl"));


                port = service.getNetSuitePort();

                ((BindingProvider) port).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);

                Passport passport = new Passport();
                passport.setEmail(nsUsername);
                passport.setAccount(accountID);
                passport.setPassword(nsPassword);
                RecordRef role = new RecordRef();
                role.setInternalId(netsuiteRole);
                passport.setRole(role);
                Status status = port.login(passport).getStatus();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return port;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        /*if (getFields().size() > 0) {
            return;
        }*/
        try {
            //fieldBuilder.addField(INTERNAL_ID, new AnalysisDimension());

            SearchRecordBasic searchRecordBasic = createSearchRecord();
            if (port == null) {
                System.out.println("NULL PORT");
            }

            SearchRecord searchRecord;
            if (searchRecordBasic instanceof ItemSearchBasic) {
                // limit to inventory items
                ItemSearchBasic itemSearchBasic = (ItemSearchBasic) searchRecordBasic;
                SearchEnumMultiSelectField type = new SearchEnumMultiSelectField();
                type.getSearchValue().add("_inventoryItem");
                type.setOperator(SearchEnumMultiSelectFieldOperator.ANY_OF);
                itemSearchBasic.setType(type);
                if (searchID == null || "".equals(searchID)) {
                    searchRecord = itemSearchBasic;
                } else {
                    ItemSearchAdvanced itemSearchAdvanced = new ItemSearchAdvanced();
                    //itemSearchAdvanced.setSavedSearchId("731");
                    itemSearchAdvanced.setSavedSearchId(searchID);
                    ItemSearch itemSearch = new ItemSearch();
                    itemSearch.setBasic(itemSearchBasic);
                    itemSearchAdvanced.setCriteria(itemSearch);
                    searchRecord = itemSearchAdvanced;
                }
            } else {
                searchRecord = searchRecordBasic;

            }
            searchResult = getOrCreatePort().search(searchRecord);

            if (searchRecord instanceof SearchRecordBasic) {
                RecordList recordList = searchResult.getRecordList();
                List<Record> records = recordList.getRecord();
                if (records.size() > 0) {
                    Object record = records.get(0);
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
                            } else if (o instanceof List) {
                                List list = (List) o;
                                if (list.size() > 0) {
                                    Object searchColumnField = list.get(0);
                                    if (searchColumnField instanceof SearchColumnTextNumberField) {
                                        SearchColumnTextNumberField searchField = (SearchColumnTextNumberField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnEnumSelectField) {
                                        SearchColumnEnumSelectField searchField = (SearchColumnEnumSelectField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnBooleanField) {
                                        SearchColumnBooleanField searchField = (SearchColumnBooleanField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDateField) {
                                        SearchColumnDateField searchField = (SearchColumnDateField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDateDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnSelectField) {
                                        SearchColumnSelectField searchField = (SearchColumnSelectField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnLongField) {
                                        SearchColumnLongField searchField = (SearchColumnLongField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnStringField) {
                                        SearchColumnStringField searchField = (SearchColumnStringField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDoubleField) {
                                        SearchColumnDoubleField searchField = (SearchColumnDoubleField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisMeasure(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnSelectCustomField) {
                                        SearchColumnSelectCustomField searchField = (SearchColumnSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnStringCustomField) {
                                        SearchColumnStringCustomField searchField = (SearchColumnStringCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnLongCustomField) {
                                        SearchColumnLongCustomField searchField = (SearchColumnLongCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDoubleCustomField) {
                                        SearchColumnDoubleCustomField searchField = (SearchColumnDoubleCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisMeasure(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnEnumMultiSelectCustomField) {
                                        SearchColumnEnumMultiSelectCustomField searchField = (SearchColumnEnumMultiSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnBooleanCustomField) {
                                        SearchColumnBooleanCustomField searchField = (SearchColumnBooleanCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDateCustomField) {
                                        SearchColumnDateCustomField searchField = (SearchColumnDateCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDateDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnMultiSelectCustomField) {
                                        SearchColumnMultiSelectCustomField searchField = (SearchColumnMultiSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    }
                                } else {

                                }
                            }
                        }
                    }
                }
            } else {
                SearchRowList rowList = searchResult.getSearchRowList();
                List<SearchRow> searchRows = rowList.getSearchRow();
                if (searchRows.size() > 0) {
                    Object record = searchRows.get(0);
                    Method getRecordMethod = record.getClass().getMethod("getBasic");
                    record = getRecordMethod.invoke(record);
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
                            } else if (o instanceof List) {
                                List list = (List) o;
                                if (list.size() > 0) {
                                    Object searchColumnField = list.get(0);
                                    if (searchColumnField instanceof SearchColumnTextNumberField) {
                                        SearchColumnTextNumberField searchField = (SearchColumnTextNumberField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnEnumSelectField) {
                                        SearchColumnEnumSelectField searchField = (SearchColumnEnumSelectField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnBooleanField) {
                                        SearchColumnBooleanField searchField = (SearchColumnBooleanField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDateField) {
                                        SearchColumnDateField searchField = (SearchColumnDateField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDateDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnSelectField) {
                                        SearchColumnSelectField searchField = (SearchColumnSelectField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnLongField) {
                                        SearchColumnLongField searchField = (SearchColumnLongField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnStringField) {
                                        SearchColumnStringField searchField = (SearchColumnStringField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDoubleField) {
                                        SearchColumnDoubleField searchField = (SearchColumnDoubleField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisMeasure(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnSelectCustomField) {
                                        SearchColumnSelectCustomField searchField = (SearchColumnSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnStringCustomField) {
                                        SearchColumnStringCustomField searchField = (SearchColumnStringCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnLongCustomField) {
                                        SearchColumnLongCustomField searchField = (SearchColumnLongCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDoubleCustomField) {
                                        SearchColumnDoubleCustomField searchField = (SearchColumnDoubleCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisMeasure(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnEnumMultiSelectCustomField) {
                                        SearchColumnEnumMultiSelectCustomField searchField = (SearchColumnEnumMultiSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnBooleanCustomField) {
                                        SearchColumnBooleanCustomField searchField = (SearchColumnBooleanCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnDateCustomField) {
                                        SearchColumnDateCustomField searchField = (SearchColumnDateCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDateDimension(fieldName));
                                    } else if (searchColumnField instanceof SearchColumnMultiSelectCustomField) {
                                        SearchColumnMultiSelectCustomField searchField = (SearchColumnMultiSelectCustomField) searchColumnField;
                                        fieldBuilder.addField(fieldName, new AnalysisDimension(fieldName));
                                    }
                                } else {

                                }
                            }
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO NETSUITE_TABLE (DATA_SOURCE_ID, NETSUITE_TABLE, SEARCH_ID," +
                "NETSUITE_ACCOUNT_ID, NETSUITE_USERNAME, NETSUITE_PASSWORD, NETSUITE_ROLE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, table);
        insertStmt.setString(3, searchID);
        insertStmt.setString(4, accountID);
        insertStmt.setString(5, nsUsername);
        insertStmt.setString(6, nsPassword != null ? PasswordStorage.encryptString(nsPassword) : null);
        insertStmt.setString(7, netsuiteRole);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT NETSUITE_TABLE, SEARCH_ID, NETSUITE_ACCOUNT_ID, NETSUITE_USERNAME, NETSUITE_PASSWORD, NETSUITE_ROLE FROM " +
                "NETSUITE_TABLE WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            table = rs.getString(1);
            searchID = rs.getString(2);
            accountID = rs.getString(3);
            nsUsername = rs.getString(4);
            String nsPassword = rs.getString(5);
            if (nsPassword != null) {
                this.nsPassword = PasswordStorage.decryptString(nsPassword);
            }
            netsuiteRole = rs.getString(6);
        }
        queryStmt.close();
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.STORED_PULL;
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


            SearchRecord searchRecordBasic = createSearchRecord();
            SearchRecord searchRecord;
            if (searchRecordBasic instanceof ItemSearchBasic) {
                // limit to inventory items
                ItemSearchBasic itemSearchBasic = (ItemSearchBasic) searchRecordBasic;
                SearchEnumMultiSelectField type = new SearchEnumMultiSelectField();
                type.getSearchValue().add("_inventoryItem");
                type.setOperator(SearchEnumMultiSelectFieldOperator.ANY_OF);
                itemSearchBasic.setType(type);
                if (searchID == null || "".equals(searchID)) {
                    searchRecord = itemSearchBasic;
                } else {
                    ItemSearchAdvanced itemSearchAdvanced = new ItemSearchAdvanced();
                    itemSearchAdvanced.setSavedSearchId(searchID);
                    ItemSearch itemSearch = new ItemSearch();
                    itemSearch.setBasic(itemSearchBasic);
                    itemSearchAdvanced.setCriteria(itemSearch);
                    searchRecord = itemSearchAdvanced;
                }
            } else {
                searchRecord = searchRecordBasic;
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
            long start = System.currentTimeMillis();
            do {
                RecordList recordList;
                if (searchID == null) {
                    searchResult = getOrCreatePort().search(searchRecord);
                    if (searchResult.getTotalRecords() == 0) {
                        // nothing modified
                        break;
                    }
                    currentPage = searchResult.getPageIndex();
                    pageSize = searchResult.getTotalPages();
                    searchID = searchResult.getSearchId();
                } else {
                    searchResult = getOrCreatePort().searchMoreWithId(searchID, currentPage + 1);
                    currentPage = searchResult.getPageIndex();
                }

                if (searchRecord == searchRecordBasic) {
                    recordList = searchResult.getRecordList();
                    List<Record> records = recordList.getRecord();
                    for (Record record : records) {
                    /*InventoryItem itemSearchRecord = (InventoryItem) record;
                    if ("387".equals(itemSearchRecord.getInternalId())) {
                        System.out.println("blah");
                    }

                    if (itemSearchRecord.getCustomFieldList() != null) {

                    }*/
                        DataSet dataSet = new DataSet();
                        IRow row = dataSet.createRow();
                        row.addValue("Count", 1);
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
                } else {
                    SearchRowList rowList = searchResult.getSearchRowList();
                    List<SearchRow> searchRows = rowList.getSearchRow();
                    for (SearchRow searchRow : searchRows) {
                        Method getRecordMethod = searchRow.getClass().getMethod("getBasic");
                        Object record = getRecordMethod.invoke(searchRow);
                        DataSet dataSet = new DataSet();
                        IRow row = dataSet.createRow();
                        row.addValue("Count", 1);
                        String idFieldValue = null;
                        Method[] methods = record.getClass().getMethods();
                        for (Method m : methods) {
                            if (m.getName().startsWith("get")) {
                                String fieldName = m.getName().substring("get".length());
                                Object o = m.invoke(record);
                                if (o instanceof List) {
                                    List list = (List) o;
                                    if (list.size() > 0) {
                                        Object searchColumnField = list.get(0);
                                        if (searchColumnField instanceof SearchColumnTextNumberField) {
                                            SearchColumnTextNumberField searchField = (SearchColumnTextNumberField) searchColumnField;
                                            if ("InternalId".equals(fieldName)) {
                                                idFieldValue = searchField.getSearchValue();
                                            }
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnEnumSelectField) {
                                            SearchColumnEnumSelectField searchField = (SearchColumnEnumSelectField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnBooleanField) {
                                            SearchColumnBooleanField searchField = (SearchColumnBooleanField) searchColumnField;
                                            row.addValue(keys.get(fieldName), String.valueOf(searchField.isSearchValue()));
                                        } else if (searchColumnField instanceof SearchColumnDateField) {
                                            SearchColumnDateField searchField = (SearchColumnDateField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue().toGregorianCalendar().getTime());
                                        } else if (searchColumnField instanceof SearchColumnSelectField) {
                                            SearchColumnSelectField searchField = (SearchColumnSelectField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue().getInternalId());
                                        } else if (searchColumnField instanceof SearchColumnLongField) {
                                            SearchColumnLongField searchField = (SearchColumnLongField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnStringField) {
                                            SearchColumnStringField searchField = (SearchColumnStringField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnDoubleField) {
                                            SearchColumnDoubleField searchField = (SearchColumnDoubleField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnSelectCustomField) {
                                            SearchColumnSelectCustomField searchField = (SearchColumnSelectCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue().getInternalId());
                                        } else if (searchColumnField instanceof SearchColumnStringCustomField) {
                                            SearchColumnStringCustomField searchField = (SearchColumnStringCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnLongCustomField) {
                                            SearchColumnLongCustomField searchField = (SearchColumnLongCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnDoubleCustomField) {
                                            SearchColumnDoubleCustomField searchField = (SearchColumnDoubleCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnEnumMultiSelectCustomField) {
                                            SearchColumnEnumMultiSelectCustomField searchField = (SearchColumnEnumMultiSelectCustomField) searchColumnField;
                                            //row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        } else if (searchColumnField instanceof SearchColumnBooleanCustomField) {
                                            SearchColumnBooleanCustomField searchField = (SearchColumnBooleanCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), String.valueOf(searchField.isSearchValue()));
                                        } else if (searchColumnField instanceof SearchColumnDateCustomField) {
                                            SearchColumnDateCustomField searchField = (SearchColumnDateCustomField) searchColumnField;
                                            row.addValue(keys.get(fieldName), searchField.getSearchValue().toGregorianCalendar().getTime());
                                        } else if (searchColumnField instanceof SearchColumnMultiSelectCustomField) {
                                            SearchColumnMultiSelectCustomField searchField = (SearchColumnMultiSelectCustomField) searchColumnField;
                                            //row.addValue(keys.get(fieldName), searchField.getSearchValue());
                                        }
                                    }
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
                }

            } while (currentPage < pageSize);
            System.out.println("Elapsed time = " + (System.currentTimeMillis() - start) + " ms.");
            port.logout();
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

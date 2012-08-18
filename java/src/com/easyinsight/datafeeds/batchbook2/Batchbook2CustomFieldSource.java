package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/13/12
 * Time: 9:18 AM
 */
public class Batchbook2CustomFieldSource extends Batchbook2BaseSource {

    private String customFieldID;

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fieldList = new ArrayList<AnalysisItem>();
        Key personIDKey = keys.get(getFeedName() + " PersonID");
        if (personIDKey == null) {
            personIDKey = new NamedKey(getFeedName() + " PersonID");
        }
        Key companyIDKey = keys.get(getFeedName() + " CompanyID");
        if (companyIDKey == null) {
            companyIDKey = new NamedKey(getFeedName() + " CompanyID");
        }
        fieldList.add(new AnalysisDimension(personIDKey));
        fieldList.add(new AnalysisDimension(companyIDKey));
        Batchbook2CompositeSource batchbookCompositeSource = (Batchbook2CompositeSource) parentDefinition;
        HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbookCompositeSource.getToken(), "");
        List customFields = (List) runRestRequest("/custom_field_sets.json", httpClient, batchbookCompositeSource).get("custom_field_sets");
        for (Object customFieldObject : customFields) {
            Map customField = (Map) customFieldObject;
            String customFieldID = customField.get("id").toString();
            if (customFieldID.equals(customFieldID)) {
                List attributes = (List) customField.get("custom_field_definitions_attributes");
                for (Object attributeObject : attributes) {
                    Map attribute = (Map) attributeObject;
                    String attributeID = attribute.get("id").toString();
                    String name = (String) attribute.get("name");
                    String type = (String) attribute.get("custom_field_type");
                    AnalysisItem analysisItem = null;
                    Key key = keys.get(attributeID);
                    if (key == null) {
                        key = new NamedKey(attributeID);
                    }
                    if ("CustomField::Text".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("CustomField::Number".equals(type)) {
                        analysisItem = new AnalysisMeasure(key, name, AggregationTypes.SUM);
                    } else if ("Yes or No".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("CustomField::Phone".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("CustomField::Email".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("CustomField::URL".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("CustoField::Date".equals(type)) {
                        analysisItem = new AnalysisDateDimension(key, name, AnalysisDateDimension.DAY_LEVEL);
                    } else if ("CustomField::RecurringDate".equals(type)) {
                        analysisItem = new AnalysisDateDimension(key, name, AnalysisDateDimension.DAY_LEVEL);
                    } else if ("CustomField::Currency".equals(type)) {
                        analysisItem = new AnalysisMeasure(key, name, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY);
                    } else if ("CustomField::AssignedTo".equals(type)) {
                        analysisItem = new AnalysisDimension(key, name);
                    } else if ("MultipleChoice".equals(type)) {

                    }
                    if (analysisItem != null) {
                        fieldList.add(analysisItem);
                    }
                }
            }
        }
        return fieldList;
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement stmt = conn.prepareStatement("SELECT CUSTOM_FIELD_ID FROM batchbook_custom_field_source WHERE DATA_SOURCE_ID = ?");
        stmt.setLong(1, getDataFeedID());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            setCustomFieldID(rs.getString(1));
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM batchbook_custom_field_source WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO batchbook_custom_field_source (data_source_id, custom_field_id) VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, customFieldID);
        insertStmt.execute();
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            List<Person> people = batchbook2CompositeSource.getOrCreateCache(httpClient).getPeople();
            for (Person person : people) {
                List<CustomFieldValue> values = person.getCustomFieldValues();
                for (CustomFieldValue value : values) {
                    if (value.getCustomFieldID().equals(customFieldID)) {
                        IRow row = dataSet.createRow();
                        row.addValue(keys.get(getFeedName() + " PersonID"), person.getId());
                        for (Map.Entry<String, String> entry : value.getValueMap().entrySet()) {
                            row.addValue(keys.get(entry.getKey()), entry.getValue());
                        }
                    }
                }
            }
            List<Company> companies = batchbook2CompositeSource.getOrCreateCache(httpClient).getCompanies();
            for (Company company : companies) {
                List<CustomFieldValue> values = company.getCustomFieldValues();
                for (CustomFieldValue value : values) {
                    if (value.getCustomFieldID().equals(customFieldID)) {
                        IRow row = dataSet.createRow();
                        row.addValue(keys.get(getFeedName() + " CompanyID"), company.getId());
                        for (Map.Entry<String, String> entry : value.getValueMap().entrySet()) {
                            row.addValue(keys.get(entry.getKey()), entry.getValue());
                        }
                    }
                }
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_CUSTOM;
    }

    public String getCustomFieldID() {
        return customFieldID;
    }

    public void setCustomFieldID(String customFieldID) {
        this.customFieldID = customFieldID;
    }
}

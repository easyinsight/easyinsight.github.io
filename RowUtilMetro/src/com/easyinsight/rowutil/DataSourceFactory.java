package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.*;

import javax.xml.ws.BindingProvider;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 9:45 AM
 */
public class DataSourceFactory {

    private String key;
    private String secretKey;

    private String dataSourceKey;

    DataSourceFactory(String key, String secretKey, String dataSourceKey) {
        this.key = key;
        this.secretKey = secretKey;
        this.dataSourceKey = dataSourceKey;
    }

    private List<FieldDefinition> fieldDefinitions = new ArrayList<FieldDefinition>();

    public void addPostalCode(String postalCodeName) {
        addPostalCode(postalCodeName, postalCodeName);
    }

    public void addPostalCode(String postalCodeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(postalCodeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.POSTAL_CODE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addLongitude(String longitudeName) {
        addLongitude(longitudeName, longitudeName);
    }

    public void addLongitude(String longitudeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(longitudeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.LONGITUDE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addLatitude(String latitudeName) {
        addLatitude(latitudeName, latitudeName);
    }

    public void addLatitude(String latitudeName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(latitudeName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.LATITUDE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addTags(String tagsName) {
        addTags(tagsName, tagsName);
    }

    public void addTags(String tagsName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(tagsName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.TAGS);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addGrouping(String groupingName) {
        addGrouping(groupingName, groupingName);
    }

    public void addGrouping(String groupingName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setDefaultGroupBy(true);
        fieldDefinition.setInternalName(groupingName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.GROUPING);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addDate(String dateName) {
        addDate(dateName, dateName);
    }

    public void addDate(String dateName, String displayName) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(dateName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setFieldType(FieldType.DATE);
        fieldDefinitions.add(fieldDefinition);
    }

    public void addMeasure(String measureName) {
        addMeasure(measureName, measureName);
    }

    public void addMeasure(String measureName, String displayName) {
        addMeasure(measureName, displayName, MeasureAggregationType.SUM, MeasureFormattingType.NONE);
    }

    public void addMeasure(String measureName, String displayName, MeasureAggregationType measureAggregationType,
                           MeasureFormattingType measureFormattingType) {
        FieldDefinition fieldDefinition = new FieldDefinition();
        fieldDefinition.setInternalName(measureName);
        fieldDefinition.setDisplayName(displayName);
        fieldDefinition.setMeasureAggregationType(measureAggregationType);
        fieldDefinition.setMeasureFormattingType(measureFormattingType);
        fieldDefinition.setFieldType(FieldType.MEASURE);
        fieldDefinitions.add(fieldDefinition);
    }

    /**
     * Defines the actual data source inside of Easy Insight and returns a <code>DataSourceOperationFactory</code>
     * object that you can use to publish data into the data source.
     * @return
     */
    public DataSourceOperationFactory defineDataSource() {
        BasicAuthEIV3APIService service = new BasicAuthEIV3APIService();
        EIDataV3 port = service.getBasicAuthEIV3APIPort();
        ((BindingProvider)port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, key);
        ((BindingProvider)port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, secretKey);
        String dataSourceKey = port.defineDataSource(this.dataSourceKey, fieldDefinitions, null);
        return new DataSourceOperationFactory(key, secretKey, dataSourceKey);
    }
}

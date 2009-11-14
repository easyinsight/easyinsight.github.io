
package com.easyinsight.datafeeds.custom.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.easyinsight.datafeeds.custom.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetPossibleStringValues_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPossibleStringValues");
    private final static QName _GetColumnsResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getColumnsResponse");
    private final static QName _GetSpecificationsResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getSpecificationsResponse");
    private final static QName _GetRowsResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getRowsResponse");
    private final static QName _GetRows_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getRows");
    private final static QName _GetPropertiesForDefinition_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPropertiesForDefinition");
    private final static QName _GetColumns_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getColumns");
    private final static QName _GetPossibleDateValues_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPossibleDateValues");
    private final static QName _DefineDataSourceResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "defineDataSourceResponse");
    private final static QName _GetSpecifications_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getSpecifications");
    private final static QName _GetFields_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getFields");
    private final static QName _GetPropertiesForDefinitionResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPropertiesForDefinitionResponse");
    private final static QName _GetPossibleDateValuesResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPossibleDateValuesResponse");
    private final static QName _DefineDataSource_QNAME = new QName("http://sampleimpl.easyinsight.com/", "defineDataSource");
    private final static QName _GetFieldsResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getFieldsResponse");
    private final static QName _GetPossibleStringValuesResponse_QNAME = new QName("http://sampleimpl.easyinsight.com/", "getPossibleStringValuesResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.easyinsight.datafeeds.custom.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link Where }
     * 
     */
    public Where createWhere() {
        return new Where();
    }

    /**
     * Create an instance of {@link DefineDataSource }
     * 
     */
    public DefineDataSource createDefineDataSource() {
        return new DefineDataSource();
    }

    /**
     * Create an instance of {@link GetSpecificationsResponse }
     * 
     */
    public GetSpecificationsResponse createGetSpecificationsResponse() {
        return new GetSpecificationsResponse();
    }

    /**
     * Create an instance of {@link GetColumns }
     * 
     */
    public GetColumns createGetColumns() {
        return new GetColumns();
    }

    /**
     * Create an instance of {@link GetPropertiesForDefinition }
     * 
     */
    public GetPropertiesForDefinition createGetPropertiesForDefinition() {
        return new GetPropertiesForDefinition();
    }

    /**
     * Create an instance of {@link GetFields }
     * 
     */
    public GetFields createGetFields() {
        return new GetFields();
    }

    /**
     * Create an instance of {@link FilterSpecification }
     * 
     */
    public FilterSpecification createFilterSpecification() {
        return new FilterSpecification();
    }

    /**
     * Create an instance of {@link DefineDataSourceResponse }
     * 
     */
    public DefineDataSourceResponse createDefineDataSourceResponse() {
        return new DefineDataSourceResponse();
    }

    /**
     * Create an instance of {@link DateValue }
     * 
     */
    public DateValue createDateValue() {
        return new DateValue();
    }

    /**
     * Create an instance of {@link GetPossibleStringValues }
     * 
     */
    public GetPossibleStringValues createGetPossibleStringValues() {
        return new GetPossibleStringValues();
    }

    /**
     * Create an instance of {@link DateWhere }
     * 
     */
    public DateWhere createDateWhere() {
        return new DateWhere();
    }

    /**
     * Create an instance of {@link GetFieldsResponse }
     * 
     */
    public GetFieldsResponse createGetFieldsResponse() {
        return new GetFieldsResponse();
    }

    /**
     * Create an instance of {@link NumberWhere }
     * 
     */
    public NumberWhere createNumberWhere() {
        return new NumberWhere();
    }

    /**
     * Create an instance of {@link GetColumnsResponse }
     * 
     */
    public GetColumnsResponse createGetColumnsResponse() {
        return new GetColumnsResponse();
    }

    /**
     * Create an instance of {@link GetPossibleDateValues }
     * 
     */
    public GetPossibleDateValues createGetPossibleDateValues() {
        return new GetPossibleDateValues();
    }

    /**
     * Create an instance of {@link Folder }
     * 
     */
    public Folder createFolder() {
        return new Folder();
    }

    /**
     * Create an instance of {@link GetPossibleDateValuesResponse }
     * 
     */
    public GetPossibleDateValuesResponse createGetPossibleDateValuesResponse() {
        return new GetPossibleDateValuesResponse();
    }

    /**
     * Create an instance of {@link GetRows }
     * 
     */
    public GetRows createGetRows() {
        return new GetRows();
    }

    /**
     * Create an instance of {@link NumberValue }
     * 
     */
    public NumberValue createNumberValue() {
        return new NumberValue();
    }

    /**
     * Create an instance of {@link GetPossibleStringValuesResponse }
     * 
     */
    public GetPossibleStringValuesResponse createGetPossibleStringValuesResponse() {
        return new GetPossibleStringValuesResponse();
    }

    /**
     * Create an instance of {@link GetRowsResponse }
     * 
     */
    public GetRowsResponse createGetRowsResponse() {
        return new GetRowsResponse();
    }

    /**
     * Create an instance of {@link StringValue }
     * 
     */
    public StringValue createStringValue() {
        return new StringValue();
    }

    /**
     * Create an instance of {@link PropertyMetadata }
     * 
     */
    public PropertyMetadata createPropertyMetadata() {
        return new PropertyMetadata();
    }

    /**
     * Create an instance of {@link Field }
     * 
     */
    public Field createField() {
        return new Field();
    }

    /**
     * Create an instance of {@link StringWhere }
     * 
     */
    public StringWhere createStringWhere() {
        return new StringWhere();
    }

    /**
     * Create an instance of {@link GetSpecifications }
     * 
     */
    public GetSpecifications createGetSpecifications() {
        return new GetSpecifications();
    }

    /**
     * Create an instance of {@link Row }
     * 
     */
    public Row createRow() {
        return new Row();
    }

    /**
     * Create an instance of {@link GetPropertiesForDefinitionResponse }
     * 
     */
    public GetPropertiesForDefinitionResponse createGetPropertiesForDefinitionResponse() {
        return new GetPropertiesForDefinitionResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleStringValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPossibleStringValues")
    public JAXBElement<GetPossibleStringValues> createGetPossibleStringValues(GetPossibleStringValues value) {
        return new JAXBElement<GetPossibleStringValues>(_GetPossibleStringValues_QNAME, GetPossibleStringValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetColumnsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getColumnsResponse")
    public JAXBElement<GetColumnsResponse> createGetColumnsResponse(GetColumnsResponse value) {
        return new JAXBElement<GetColumnsResponse>(_GetColumnsResponse_QNAME, GetColumnsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSpecificationsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getSpecificationsResponse")
    public JAXBElement<GetSpecificationsResponse> createGetSpecificationsResponse(GetSpecificationsResponse value) {
        return new JAXBElement<GetSpecificationsResponse>(_GetSpecificationsResponse_QNAME, GetSpecificationsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRowsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getRowsResponse")
    public JAXBElement<GetRowsResponse> createGetRowsResponse(GetRowsResponse value) {
        return new JAXBElement<GetRowsResponse>(_GetRowsResponse_QNAME, GetRowsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRows }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getRows")
    public JAXBElement<GetRows> createGetRows(GetRows value) {
        return new JAXBElement<GetRows>(_GetRows_QNAME, GetRows.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertiesForDefinition }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPropertiesForDefinition")
    public JAXBElement<GetPropertiesForDefinition> createGetPropertiesForDefinition(GetPropertiesForDefinition value) {
        return new JAXBElement<GetPropertiesForDefinition>(_GetPropertiesForDefinition_QNAME, GetPropertiesForDefinition.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetColumns }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getColumns")
    public JAXBElement<GetColumns> createGetColumns(GetColumns value) {
        return new JAXBElement<GetColumns>(_GetColumns_QNAME, GetColumns.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleDateValues }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPossibleDateValues")
    public JAXBElement<GetPossibleDateValues> createGetPossibleDateValues(GetPossibleDateValues value) {
        return new JAXBElement<GetPossibleDateValues>(_GetPossibleDateValues_QNAME, GetPossibleDateValues.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineDataSourceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "defineDataSourceResponse")
    public JAXBElement<DefineDataSourceResponse> createDefineDataSourceResponse(DefineDataSourceResponse value) {
        return new JAXBElement<DefineDataSourceResponse>(_DefineDataSourceResponse_QNAME, DefineDataSourceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSpecifications }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getSpecifications")
    public JAXBElement<GetSpecifications> createGetSpecifications(GetSpecifications value) {
        return new JAXBElement<GetSpecifications>(_GetSpecifications_QNAME, GetSpecifications.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFields }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getFields")
    public JAXBElement<GetFields> createGetFields(GetFields value) {
        return new JAXBElement<GetFields>(_GetFields_QNAME, GetFields.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPropertiesForDefinitionResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPropertiesForDefinitionResponse")
    public JAXBElement<GetPropertiesForDefinitionResponse> createGetPropertiesForDefinitionResponse(GetPropertiesForDefinitionResponse value) {
        return new JAXBElement<GetPropertiesForDefinitionResponse>(_GetPropertiesForDefinitionResponse_QNAME, GetPropertiesForDefinitionResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleDateValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPossibleDateValuesResponse")
    public JAXBElement<GetPossibleDateValuesResponse> createGetPossibleDateValuesResponse(GetPossibleDateValuesResponse value) {
        return new JAXBElement<GetPossibleDateValuesResponse>(_GetPossibleDateValuesResponse_QNAME, GetPossibleDateValuesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DefineDataSource }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "defineDataSource")
    public JAXBElement<DefineDataSource> createDefineDataSource(DefineDataSource value) {
        return new JAXBElement<DefineDataSource>(_DefineDataSource_QNAME, DefineDataSource.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetFieldsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getFieldsResponse")
    public JAXBElement<GetFieldsResponse> createGetFieldsResponse(GetFieldsResponse value) {
        return new JAXBElement<GetFieldsResponse>(_GetFieldsResponse_QNAME, GetFieldsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetPossibleStringValuesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://sampleimpl.easyinsight.com/", name = "getPossibleStringValuesResponse")
    public JAXBElement<GetPossibleStringValuesResponse> createGetPossibleStringValuesResponse(GetPossibleStringValuesResponse value) {
        return new JAXBElement<GetPossibleStringValuesResponse>(_GetPossibleStringValuesResponse_QNAME, GetPossibleStringValuesResponse.class, null, value);
    }

}

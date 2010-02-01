package com.easyinsight.datafeeds.custom;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.datafeeds.CredentialRequirement;
import com.easyinsight.datafeeds.CredentialsDefinition;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.custom.client.*;
import com.easyinsight.datafeeds.custom.client.DateValue;
import com.easyinsight.datafeeds.custom.client.StringValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 8, 2009
 * Time: 8:52:22 PM
 */
public class CustomDataFeed extends Feed {

    private String wsdl;
    private DataProvider port;
    private static final QName SERVICE_NAME = new QName("http://db.easyinsight.com/", "DatabaseServerService");

    public CustomDataFeed(String wsdl) {
        this.wsdl = wsdl;
    }

    public DataProvider getPort(Credentials credentials) {
        try {
            if (port == null) {
                URL url = new URL(wsdl);
                DataProviderService ss = new DataProviderService(url, SERVICE_NAME);
                port = ss.getDataProviderPort();
                BindingProvider provider = (BindingProvider) port;
                Map<String, Object> requestContext = provider.getRequestContext();
                requestContext.put(BindingProvider.USERNAME_PROPERTY, credentials.getUserName());
                requestContext.put(BindingProvider.PASSWORD_PROPERTY, credentials.getPassword());
            }
            return port;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
        if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            List<DateValue> dateValues = getPort(credentials).getPossibleDateValues(analysisItem.getKey().toKeyString(), new ArrayList<Property>());
            metadata.addValue(analysisItem, new com.easyinsight.core.DateValue(dateValues.get(0).getValue().toGregorianCalendar().getTime()), insightRequestMetadata);
            metadata.addValue(analysisItem, new com.easyinsight.core.DateValue(dateValues.get(1).getValue().toGregorianCalendar().getTime()), insightRequestMetadata);
        } else if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
            List<StringValue> stringValues = getPort(credentials).getPossibleStringValues(analysisItem.getKey().toKeyString(), new ArrayList<Property>());
            for (StringValue stringValue : stringValues) {
                Value value;
                if (stringValue.getValue() == null) {
                    value = new EmptyValue();
                } else {
                    value =new com.easyinsight.core.StringValue(stringValue.getValue());
                }
                metadata.addValue(analysisItem, value, insightRequestMetadata);
            }
        }
        return metadata;
    }

    @Override
    public Set<CredentialRequirement> getCredentialRequirement(boolean allSources) {
        CredentialRequirement credentialRequirement = new CredentialRequirement(getFeedID(), getName(), CredentialsDefinition.STANDARD_USERNAME_PW);
        Set<CredentialRequirement> set = new HashSet<CredentialRequirement>();
        set.add(credentialRequirement);
        return set;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, Collection<Key> additionalNeededKeys) throws TokenMissingException {
        List<Field> keys = new ArrayList<Field>();
        for (AnalysisItem analysisItem : analysisItems) {
            Field field = new Field();
            field.setKey(analysisItem.getKey().toKeyString());
            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                field.setFieldType(FieldType.MEASURE);
            } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                field.setFieldType(FieldType.DATE);
            } else {
                field.setFieldType(FieldType.GROUPING);
            }
            keys.add(field);
        }
        Credentials credentials = insightRequestMetadata.getCredentialForDataSource(getFeedID());
        List<com.easyinsight.datafeeds.custom.client.Row> rows = getPort(credentials).getRows(keys, new Where(), new ArrayList<Property>());
        DataSet dataSet = new DataSet();
        for (com.easyinsight.datafeeds.custom.client.Row row : rows) {
            IRow localRow = dataSet.createRow();
            if (row.getStringValues() != null) {
                for (StringValue stringValue : row.getStringValues()) {
                    localRow.addValue(new NamedKey(stringValue.getKey()), stringValue.getValue());
                }
            }
            if (row.getNumberValues() != null) {
                for (NumberValue numberValue : row.getNumberValues()) {
                    localRow.addValue(new NamedKey(numberValue.getKey()), numberValue.getValue());
                }
            }
            if (row.getDateValues() != null) {
                for (DateValue dateValue : row.getDateValues()) {
                    localRow.addValue(new NamedKey(dateValue.getKey()), dateValue.getValue().toGregorianCalendar().getTime());
                }
            }
        }
        return dataSet;
    }

    @Override
    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

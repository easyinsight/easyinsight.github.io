package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.kpi.KPI;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
import com.easyinsight.users.Account;
import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import com.sforce.soap.partner.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Transient;
import javax.xml.ws.BindingProvider;
import java.util.*;
import java.sql.Connection;

/**
 * User: abaldwin
 * Date: Jul 8, 2009
 * Time: 10:10:17 AM
 */
public class SalesforceBaseDataSource extends ServerDataSourceDefinition {

    public static final String OPPORTUNITY = "Opportunity";
    public static final String ACCOUNT = "Account";
    public static final String LEAD = "Lead";
    public static final String CAMPAIGN = "Campaign";
    public static final String CONTACT = "Contact";
    public static final String CASE = "Case";

    @Transient
    protected SessionHeader sessionHeader;

    @Transient
    protected Soap service;

    @Override
    public Map<String, Key> newDataSourceFields(Credentials credentials) {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            if(service == null || sessionHeader == null)
                login(credentials);
            keys.putAll(getKeysForObject(OPPORTUNITY));
            keys.putAll(getKeysForObject(ACCOUNT));
            keys.putAll(getKeysForObject(LEAD));
            keys.putAll(getKeysForObject(CAMPAIGN));
            keys.putAll(getKeysForObject(CONTACT));
            keys.putAll(getKeysForObject(CASE));
            return keys;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getRequiredAccountTier() {
        return Account.PROFESSIONAL;
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.STANDARD_USERNAME_PW;
    }

    public boolean isConfigured() {
        return true;
    }

    @Override
    public Feed createFeedObject() {
        return new SalesforceFeed();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SALESFORCE;
    }

    private Map<String, Key> getKeysForObject(String s) throws InvalidSObjectFault, UnexpectedErrorFault {
        Map<String, Key> keys = new HashMap<String, Key>();
        DescribeSObjectResultType response = service.describeSObject(sessionHeader, s);
        for(FieldType f : response.getFields()) {
            if(!"REFERENCE".equals(f.getType().name()))
                keys.put(s + "." + f.getName(), new NamedKey(s + "." + f.getName()));
        }
        return keys;
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        try {
            if(sessionHeader == null)
                login(credentials);

            List<AnalysisItem> items = new LinkedList<AnalysisItem>();
            List<AnalysisItem> opportunityItems = getAnalysisItemsForObject(OPPORTUNITY, keys);
            List<AnalysisItem> accountItems = getAnalysisItemsForObject(ACCOUNT, keys);
            List<AnalysisItem> leadItems = getAnalysisItemsForObject(LEAD, keys);
            List<AnalysisItem> campaignItems = getAnalysisItemsForObject(CAMPAIGN, keys);
            List<AnalysisItem> caseItems = getAnalysisItemsForObject(CASE, keys);
            List<AnalysisItem> contactItems = getAnalysisItemsForObject(CONTACT, keys);
            defineFolder("Opportunities").getChildItems().addAll(opportunityItems);
            defineFolder("Accounts").getChildItems().addAll(accountItems);
            defineFolder("Leads").getChildItems().addAll(leadItems);
            defineFolder("Campaigns").getChildItems().addAll(campaignItems);
            defineFolder("Cases").getChildItems().addAll(caseItems);
            defineFolder("Contacts").getChildItems().addAll(contactItems);
            items.addAll(opportunityItems);
            items.addAll(accountItems);
            items.addAll(leadItems);
            items.addAll(campaignItems);
            items.addAll(caseItems);
            items.addAll(contactItems);
            return items;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getDataSourceType() {
        return DataSourceInfo.LIVE;
    }

    private List<AnalysisItem> getAnalysisItemsForObject(String s, Map<String, Key> keys) throws InvalidSObjectFault, UnexpectedErrorFault {
        List<AnalysisItem> items = new LinkedList<AnalysisItem>();
        DescribeSObjectResultType response = service.describeSObject(sessionHeader, s);
        for(FieldType f : response.getFields()) {
            String fieldName = s + "." + f.getName();
            String friendlyName = f.getName();
            if (friendlyName.endsWith("__c")) {
                friendlyName = friendlyName.substring(0, friendlyName.length() - 3);
            }
            if("BOOLEAN".equals(f.getType().name()) ||
                    "STRING".equals(f.getType().name()) ||
                    "TEXTAREA".equals(f.getType().name()) ||
                    "PHONE".equals(f.getType().name()) ||
                    "URL".equals(f.getType().name()) ||
                    "PICKLIST".equals(f.getType().name())) {
                items.add(new AnalysisDimension(keys.get(fieldName), friendlyName));
            }
            else if("DOUBLE".equals(f.getType().name()) || "INT".equals(f.getType().name())) {
                items.add(new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM));
            } else if ("CURRENCY".equals(f.getType().name())) {
                AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.SUM);
                FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                formattingConfiguration.setFormattingType(FormattingConfiguration.CURRENCY);
                analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                items.add(analysisMeasure);
            } else if ("PERCENT".equals(f.getType().name())) {
                AnalysisMeasure analysisMeasure = new AnalysisMeasure(keys.get(fieldName), friendlyName, AggregationTypes.AVERAGE);
                FormattingConfiguration formattingConfiguration = new FormattingConfiguration();
                formattingConfiguration.setFormattingType(FormattingConfiguration.PERCENTAGE);
                analysisMeasure.setFormattingConfiguration(formattingConfiguration);
                items.add(analysisMeasure);
            } else if ("DATE".equals(f.getType().name())) {
                items.add(new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL));
            } else if ("DATETIME".equals(f.getType().name())) {
                AnalysisDateDimension dateDimension = new AnalysisDateDimension(keys.get(fieldName), friendlyName, AnalysisDateDimension.DAY_LEVEL);
                dateDimension.setCustomDateFormat("yyyy-MM-dd'T'HH:mm:SS.sss'Z'");
                items.add(dateDimension);
            }
        }
        return items;
    }

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public String validateCredentials(Credentials credentials) {
        try {
            if (sessionHeader == null) login(credentials);
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private void login(Credentials c) throws InvalidIdFault, UnexpectedErrorFault, LoginFault {
        if(service == null) {
            SforceService sf = new SforceService();
            service = sf.getSoap();
        }

        LoginResultType result = service.login(c.getUserName(), c.getPassword());
        ((BindingProvider) service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, result.getServerUrl());
        sessionHeader = new SessionHeader();
        sessionHeader.setSessionId(result.getSessionId());
    }

    @NotNull
    protected List<String> getKeys() {
        return new ArrayList<String>();
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        
        return kpis;
    }
}

package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.users.Credentials;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.AggregationTypes;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.dataset.DataSet;
import com.sforce.soap.partner.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Transient;
import javax.xml.transform.Result;
import javax.xml.ws.BindingProvider;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 8, 2009
 * Time: 10:10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class SalesforceBaseDataSource extends ServerDataSourceDefinition {

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
            keys.putAll(getKeysForObject("Opportunity"));
            keys.putAll(getKeysForObject("Account"));
            keys.putAll(getKeysForObject("Lead"));
            return keys;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

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
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials) {
        try {
            if(sessionHeader == null)
                login(credentials);

            List<AnalysisItem> items = new LinkedList<AnalysisItem>();
            items.addAll(getAnalysisItemsForObject("Opportunity", keys));

            return super.createAnalysisItems(keys, dataSet, credentials);    //To change body of overridden methods use File | Settings | File Templates.
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<AnalysisItem> getAnalysisItemsForObject(String s, Map<String, Key> keys) throws InvalidSObjectFault, UnexpectedErrorFault {
        List<AnalysisItem> items = new LinkedList<AnalysisItem>();
        DescribeSObjectResultType response = service.describeSObject(sessionHeader, s);
        for(FieldType f : response.getFields()) {
            String fieldName = s + "." + f.getName();
            if("BOOLEAN".equals(f.getType().name()) ||
                    "STRING".equals(f.getType().name()) ||
                    "TEXTAREA".equals(f.getType().name()) ||
                    "PHONE".equals(f.getType().name()) ||
                    "URL".equals(f.getType().name())) {
                items.add(new AnalysisDimension(keys.get(fieldName), fieldName));
            }
            else if("DOUBLE".equals(f.getType().name()) || "CURRENCY".equals(f.getType().name()) || "INT".equals(f.getType().name())) {
                items.add(new AnalysisMeasure(keys.get(fieldName), AggregationTypes.SUM));
            }


        }

        return null;  //To change body of created methods use File | Settings | File Templates.
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

    @Override
    public CredentialsResponse refreshData(Credentials credentials, long accountID, Date now, FeedDefinition parentDefinition) {
        return super.refreshData(credentials, accountID, now, parentDefinition);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @NotNull
    protected List<String> getKeys() {
        return new ArrayList<String>();
    }
}

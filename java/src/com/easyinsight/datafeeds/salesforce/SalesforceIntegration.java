package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.users.Credentials;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.dataset.DataSet;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.SObject;

import javax.xml.ws.BindingProvider;
import javax.persistence.Transient;
import java.util.Set;
import java.util.List;
import java.util.HashSet;

import org.w3c.dom.Element;

/**
 * User: jboe
 * Date: Jul 15, 2009
 * Time: 11:38:49 AM
 */
public class SalesforceIntegration {

    public static final String[] querySubjectOrder = new String[] { "Opportunity", "Account " };

    @Transient
    protected SessionHeader sessionHeader;

    @Transient
    protected Soap service;

    public DataSet query(Credentials credentials, Set<String> querySubjects, List<String> fields) throws InvalidSObjectFault, MalformedQueryFault, InvalidIdFault, InvalidFieldFault, UnexpectedErrorFault, InvalidQueryLocatorFault, LoginFault {
        DataSet dataSet = new DataSet();
        if (sessionHeader == null) {
            login(credentials);
        }
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select ");
        for (String field : fields) {
            queryBuilder.append(field);
            queryBuilder.append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(" from ");
        for (String querySubject : querySubjects) {
            queryBuilder.append(querySubject);
            queryBuilder.append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        QueryResultType queryResultType = service.query(sessionHeader, queryBuilder.toString());
        if (queryResultType.isDone()) {
            List<SObject> objects = queryResultType.getRecords();
            for (SObject object : objects) {
                IRow row = dataSet.createRow();
                List<Object> blahs = object.getAny();
                for (Object blah : blahs) {
                    Element element = (Element) blah;
                    System.out.println("name = " + element.getNodeName());
                    System.out.println("value = " + element.getTextContent());
                    String keyName = element.getNodeName().split(":")[1];
                    row.addValue(keyName, element.getTextContent());
                }
            } 
        }
        return dataSet;
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
}

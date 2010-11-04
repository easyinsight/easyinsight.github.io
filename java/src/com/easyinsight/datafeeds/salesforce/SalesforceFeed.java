package com.easyinsight.datafeeds.salesforce;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
import com.sforce.soap.partner.*;

import javax.persistence.Transient;
import javax.xml.ws.BindingProvider;
import java.util.*;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 4:00:05 PM
 */
public class SalesforceFeed extends Feed {

    private static final String[] subjectOrder = new String[] { "Account, Opportunity"};

    @Transient
    protected SessionHeader sessionHeader;

    @Transient
    protected Soap service;

    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) throws ReportException {
        /*AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        try {
            if (sessionHeader == null)
                login(insightRequestMetadata.getCredentialForDataSource(getFeedID()));
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select ");
            Set<String> subjects = new HashSet<String>();
            String keyString = analysisItem.getKey().toKeyString();
            String[] keyTokens = keyString.split("\\.");
            String querySubject = keyTokens[0];
            queryBuilder.append(keyString);
            queryBuilder.append(",");
            subjects.add(querySubject);
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(" from ");
            queryBuilder.append(findCoreSubject(subjects));
            *//*for (String querySubject : querySubjects) {
                queryBuilder.append(querySubject);
                queryBuilder.append(",");
            }*//*
            QueryResultType queryResultType = service.query(sessionHeader, queryBuilder.toString());
            if (queryResultType.isDone()) {
                List<SObject> objects = queryResultType.getRecords();
                for (SObject object : objects) {
                    List<Object> blahs = object.getAny();
                    for (Object blah : blahs) {
                        Element element = (Element) blah;
                        metadata.addValue(analysisItem, new StringValue(element.getTextContent()), insightRequestMetadata);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return metadata;*/
        throw new UnsupportedOperationException();
    }
    
    public String findCoreSubject(Set<String> subjects) {
        List<String> subjectList = new ArrayList<String>(subjects);
        Collections.sort(subjectList, new Comparator<String>() {

            public int compare(String o1, String o2) {
                Integer index1 = findIndex(o1);
                Integer index2 = findIndex(o2);
                return index1.compareTo(index2);
            }
        });
        return subjectList.get(0);
    }

    private int findIndex(String subject) {
       for (int i = 0; i < subjectOrder.length; i++) {
            String testSubject = subjectOrder[i];
            if (subject.equals(testSubject)) {
                return i;
            }
        }
        return -1;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws ReportException {
        /*DataSet dataSet = new DataSet();
        try {
            if (sessionHeader == null)
                login(insightRequestMetadata.getCredentialForDataSource(getFeedID()));
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("select ");
            Map<String, AnalysisItem> keyLookupMap = new HashMap<String, AnalysisItem>();
            Set<String> subjects = new HashSet<String>();
            for (AnalysisItem analysisItem : analysisItems) {
                String keyString = analysisItem.getKey().toKeyString();
                String[] keyTokens = keyString.split("\\.");
                String querySubject = keyTokens[0];
                queryBuilder.append(keyString);
                keyLookupMap.put(keyString, analysisItem);
                queryBuilder.append(",");
                subjects.add(querySubject);
            }
            queryBuilder.deleteCharAt(queryBuilder.length() - 1);
            queryBuilder.append(" from ");
            queryBuilder.append(findCoreSubject(subjects));
            for (String querySubject : querySubjects) {
                queryBuilder.append(querySubject);
                queryBuilder.append(",");
            }
            QueryResultType queryResultType = service.query(sessionHeader, queryBuilder.toString());
            if (queryResultType.isDone()) {
                List<SObject> objects = queryResultType.getRecords();
                for (SObject object : objects) {
                    IRow row = dataSet.createRow();
                    List<Object> blahs = object.getAny();
                    for (Object blah : blahs) {
                        Element element = (Element) blah;
                        parseElement(element, row, keyLookupMap, object.getType());

                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;*/
        throw new UnsupportedOperationException();
    }

    private void parseElement(Element element, IRow row, Map<String, AnalysisItem> keyLookupMap, String type) {
        if (element.getChildNodes().getLength() == 1) {
            String keyName = element.getNodeName().split(":")[1];
            String qualifiedName = type + "." + keyName;
            AnalysisItem analysisItem = keyLookupMap.get(qualifiedName);
            row.addValue(analysisItem.createAggregateKey(), element.getTextContent());
        } else if (element.getChildNodes().getLength() > 1) {
            String querySubject = element.getNodeName().split(":")[1];
            NodeList childList = element.getChildNodes();
            for (int i = 0; i < childList.getLength(); i++) {
                Node child = childList.item(i);
                String qualifiedName = querySubject + "." + child.getNodeName().split(":")[1];
                AnalysisItem analysisItem = keyLookupMap.get(qualifiedName);
                if (analysisItem != null) {
                    row.addValue(analysisItem.createAggregateKey(), child.getTextContent());
                }
            }
        }
    }

    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void login(Credentials c) throws InvalidIdFault, UnexpectedErrorFault, LoginFault {
        if (service == null) {
            SforceService sf = new SforceService();
            service = sf.getSoap();
        }

        LoginResultType result = service.login(c.getUserName(), c.getPassword());
        ((BindingProvider) service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, result.getServerUrl());
        sessionHeader = new SessionHeader();
        sessionHeader.setSessionId(result.getSessionId());
    }
}

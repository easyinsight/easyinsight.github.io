package test.salesforce;

import junit.framework.TestCase;
import com.sforce.soap.partner.*;
import com.sforce.soap.partner.sobject.SObject;
import com.easyinsight.datafeeds.salesforce.SalesforceBaseDataSource;
import com.easyinsight.datafeeds.salesforce.SalesforceIntegration;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.users.Credentials;

import javax.xml.ws.BindingProvider;
import javax.xml.namespace.QName;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.binding.soap.SoapHeader;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.util.*;

import test.util.TestUtil;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 8, 2009
 * Time: 3:57:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesForceConnectionTest extends TestCase {

    private SessionHeader sessionHeader;
    private Soap service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        SforceService sf = new SforceService();
        service = sf.getSoap();
        LoginResultType result = service.login("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR");
        String sessionID = result.getSessionId();
        sessionHeader = new SessionHeader();
        sessionHeader.setSessionId(sessionID);
        ((BindingProvider) service).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, result.getServerUrl());
    }

    public void testGetSessionId() {
        SforceService sf = new SforceService();
        Soap s = sf.getSoap();
        try {
            LoginResultType result = s.login("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR");
            String sessionID = result.getSessionId();
        } catch (Exception ex) {
            ex.printStackTrace();
            assertEquals(false, true);
        }
    }

    public void testUseSessionId() {
        SforceService sf = new SforceService();
        Soap s = sf.getSoap();
        try {
            LoginResultType result = s.login("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR");
            String sessionID = result.getSessionId();
            SessionHeader h = new SessionHeader();
            h.setSessionId(sessionID);
            ((BindingProvider) s).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, result.getServerUrl());
            GetUserInfoResultType response = s.getUserInfo(h);
            response.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            assertEquals(false, true);
        }
    }

    public void testGetAccounts() {

        try {
            DescribeSObjectResultType response = service.describeSObject(sessionHeader, "Contact");
            QueryResultType result = service.query(sessionHeader, "select FirstName, LastName from Contact");
            result.isDone();
        } catch (InvalidFieldFault invalidFieldFault) {
            invalidFieldFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidIdFault invalidIdFault) {
            invalidIdFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidQueryLocatorFault invalidQueryLocatorFault) {
            invalidQueryLocatorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidSObjectFault invalidSObjectFault) {
            invalidSObjectFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedQueryFault malformedQueryFault) {
            malformedQueryFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnexpectedErrorFault unexpectedErrorFault) {
            unexpectedErrorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void testJoin() {

        try {
            DescribeSObjectResultType response = service.describeSObject(sessionHeader, "Contact");
            QueryResultType result = service.query(sessionHeader, "select Product.Name, Opportunity.Name, Account.Name, Account.NumberOfEmployees from Product");
            result.isDone();
            List<SObject> objects = result.getRecords();
            for (SObject object : objects) {
                List<Object> blahs = object.getAny();
                for (Object blah : blahs) {
                    Element element = (Element) blah;
                    if (element.getChildNodes().getLength() == 1) {
                        System.out.println("type = " + object.getType());
                        System.out.println("name = " + element.getNodeName().split(":")[1]);
                        System.out.println("value = " + element.getTextContent());
                    } else if (element.getChildNodes().getLength() > 1) {
                        String querySubject = element.getNodeName().split(":")[1];
                        NodeList childList = element.getChildNodes();
                        for (int i = 0; i < childList.getLength(); i++) {
                            Node child = childList.item(i);
                            System.out.println("\tchild name = " + querySubject + "." + child.getNodeName().split(":")[1]);
                            System.out.println("\tchild value = " + child.getTextContent());
                        }
                    }
                }
            }
        } catch (InvalidFieldFault invalidFieldFault) {
            invalidFieldFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidIdFault invalidIdFault) {
            invalidIdFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidQueryLocatorFault invalidQueryLocatorFault) {
            invalidQueryLocatorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidSObjectFault invalidSObjectFault) {
            invalidSObjectFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedQueryFault malformedQueryFault) {
            malformedQueryFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnexpectedErrorFault unexpectedErrorFault) {
            unexpectedErrorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void testGetOpportunities() {

        try {
            DescribeSObjectResultType response = service.describeSObject(sessionHeader, "Opportunity");
            String selectString = "";
            boolean first = true;
            for(FieldType field : response.getFields()) {
                if(!first)
                    selectString += ", ";
                selectString += field.getName();
                first = false;
            }


            QueryResultType result = service.query(sessionHeader, "select " + selectString +  " from Opportunity");
            result.isDone();
        } catch (InvalidFieldFault invalidFieldFault) {
            invalidFieldFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidIdFault invalidIdFault) {
            invalidIdFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidQueryLocatorFault invalidQueryLocatorFault) {
            invalidQueryLocatorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidSObjectFault invalidSObjectFault) {
            invalidSObjectFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedQueryFault malformedQueryFault) {
            malformedQueryFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnexpectedErrorFault unexpectedErrorFault) {
            unexpectedErrorFault.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void testQuery() throws InvalidSObjectFault, MalformedQueryFault, InvalidIdFault, InvalidFieldFault, UnexpectedErrorFault, LoginFault, InvalidQueryLocatorFault {
        SalesforceIntegration salesforceIntegration = new SalesforceIntegration();
        Set<String> subjects = new HashSet<String>();
        subjects.add("Opportunity");
        List<String> fields = Arrays.asList("Name");
        Credentials credentials = new Credentials("jboe99@gmail.com", "e@symone$rKxLSrt0eol9SbnHAr8UbZOR");
        salesforceIntegration.query(credentials, subjects, fields);
    }
}

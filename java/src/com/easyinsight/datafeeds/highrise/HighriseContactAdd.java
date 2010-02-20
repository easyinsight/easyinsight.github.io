package com.easyinsight.datafeeds.highrise;

import com.easyinsight.logging.LogClass;
import com.easyinsight.users.*;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * User: abaldwin
 * Date: Jul 24, 2009
 * Time: 11:35:38 AM
 */
public class HighriseContactAdd {

    private static final String HIGHRISE_API_USERNAME = "";
    private static final String HIGHRISE_API_PASSWORD = "12badd250ea4398233e0b5ca10078e75";

    private static final String HIGHRISE_API_ENDPOINT = "https://easyinsight.highrisehq.com/companies";

    // user signs up for account
    // triggers email notification
    // adds the following records to highrise:
    // contact
    //      is there already a contact with this email address?
    // company
    //      for the moment, is there already a company by this name?
    //      create a note associating the company to the account ID
    // deal
    //      set pending
    //      price = monthly price
    //      price type = month
    //      duration = 12

    // our end goal here is to create a federated sales and marketing pipeline
    // where did the user come from that led them to sign up?
    // probably some additional properties on user signup to help guide our sales activities

    // we need that coordinated sales and marketing view
    // need competitive matrix showing the combined results of:
    //      crunchbase
    //      alexa
    //      rivalmap
    // need our IT view
    // 

    private static final String COMPANY_XML = "<company><name>{0}</name>" +
            "<contact-data><email-addresses><email-address><address>{1}</address></email-address></email-addresses></contact-data></company>";

    private static final String DEAL_XML = "<deal><name>{0}</name><party_id>{1}<party_id><price-type>month</price-type>" +
            "<price>{2}</price><category>{3}</category></deal>";

    private static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    private static class Company {

        // primary key = company name?

        private String companyName;
        private String companyID;
        private Deal deal;
        private List<Contact> contact = new ArrayList<Contact>();

        public void synchronize(Map<String, Company> companies, long accountID) {

            // GET /companies/search.xml?term=companyName

            // if exists, PUT
            // if does not exist, POST

            // if we change over to having a primary email address as the join
            // does that help?

            // 
        }
    }

    private static class Deal {

        // primary key = deal name

        private String dealName;
        private String dealID;

        public void synchronize() {

        }
    }

    private static class Contact {

        // primary key = email address

        private String emailAddress;

        public void synchronize() {
            
        }
    }

    public void synchronizeData() {
        List<Company> companies = new ArrayList<Company>();
        List<AccountAdminTO> accounts = new EIAccountManagementService().getAccounts();
        for (AccountAdminTO account : accounts) {
            Company company = new Company();
            company.companyName = account.getName();
            Deal deal = new Deal();
            company.deal = deal;

            for (UserTransferObject user : account.getAdminUsers()) {
                Contact contact = new Contact();
                contact.emailAddress = user.getEmail();

            }

            deal.dealName = account.getName();
            switch (account.getAccountType()) {
                case Account.PERSONAL:
                case Account.BASIC:
                case Account.PROFESSIONAL:
                case Account.PREMIUM:
                case Account.ENTERPRISE:
                    
            }
            switch (account.getAccountState()) {
                case Account.ACTIVE:
                    if (account.getAccountType() > Account.PERSONAL) {
                        
                    }
                default:
            }
            companies.add(company);
        }
        for (Company company : companies) {
            //company.synchronize();
        }
    }

    private void synchronize(AccountAdminTO account, Company company, HttpClient client) {
        
    }

    private void addNew(AccountAdminTO account, HttpClient client) throws IOException {        
        String companyXML = MessageFormat.format(COMPANY_XML, account.getName(), account.getAdminUsers().get(0).getEmail());
        PostMethod method = new PostMethod(HIGHRISE_API_ENDPOINT);
        StringRequestEntity entity = new StringRequestEntity(companyXML, "text/xml", "UTF-8");
        method.setRequestEntity(entity);
        client.executeMethod(method);
    }

    public void synchronize() {
        try {
            HttpClient client = getHttpClient(HIGHRISE_API_USERNAME, HIGHRISE_API_PASSWORD);
            Builder builder = new Builder();
            String url = "http://easyinsight.highrisehq.com";

            // 

            // check for presence of a company by this name
            // retrieve all companies in Highrise
            Map<String, Company> existingCompanies = new HashMap<String, Company>();
            Document companies = runGetRequest("/companies.xml", client, builder, url, null);
            Nodes companyNodes = companies.query("/companies/company");
            for (int i = 0; i < companyNodes.size(); i++) {
                Node companyNode = companyNodes.get(i);
                String companyName = queryField(companyNode, "name/text()");
                String companyID = queryField(companyNode, "id/text()");
                Company company = new Company();
                company.companyName = companyName;
                company.companyID = companyID;
                Nodes emails = companyNode.query("/company/contact-data/email-addresses/email-address");
                if (emails != null) {
                    for (int j = 0; j < emails.size(); j++) {
                        Node emailNode = emails.get(j);
                        String email = emailNode.getValue();
                        existingCompanies.put(email, company);
                    }
                }
            }
            List<AccountAdminTO> accounts = new EIAccountManagementService().getAccounts();
            Iterator<AccountAdminTO> accountIter = accounts.iterator();
            while (accountIter.hasNext()) {
                AccountAdminTO account = accountIter.next();
                for (UserTransferObject user : account.getAdminUsers()) {
                    Company company = existingCompanies.get(user.getEmail());
                    if (company != null) {
                        synchronize(account, company, client);
                        accountIter.remove();                                               
                        break;
                    }
                }
            }
            for (AccountAdminTO account : accounts) {
                addNew(account, client);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    protected static Document runGetRequest(String path, HttpClient client, Builder builder, String url, EIPageInfo pageInfo) throws HighRiseLoginException {
        HttpMethod restMethod = new GetMethod(url + path);
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        try {
            client.executeMethod(restMethod);
            doc = builder.build(restMethod.getResponseBodyAsStream());
            if(pageInfo != null) {
                pageInfo.MaxPages = Integer.parseInt(restMethod.getResponseHeader("X-Pages").getValue());
            }
        }
        catch (nu.xom.ParsingException e) {
                throw new HighRiseLoginException("Invalid username/password.");
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return doc;
    }

    private static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    private Map<String, Long> retrieveCategoryInfo(HttpClient client, Builder builder, Map<String, String> categoryCache, String categoryID, String url) throws HighRiseLoginException {
        Map<String, Long> categoryMap = new HashMap<String, Long>();
        try {
            String contactName = null;
            if(categoryID != null) {
                contactName = categoryCache.get(categoryID);
                if(contactName == null) {
                    Document contactInfo = runGetRequest("/deal_categories/" + categoryID + ".xml", client, builder, url, null);
                    Nodes dealNodes = contactInfo.query("/deal-category");

                    if (dealNodes.size() > 0) {
                        Node deal = dealNodes.get(0);
                        //contactName = queryField(deal, "name/text()");
                    }

                    categoryCache.put(categoryID, contactName);
                }

            }

        } catch (HighRiseLoginException e) {

        }
        return categoryMap;
    }
}
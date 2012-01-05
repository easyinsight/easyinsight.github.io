package com.easyinsight.datafeeds.constantcontact;

import com.csvreader.CsvReader;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp3.CommonsHttp3OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 1/4/12
 * Time: 4:11 PM
 */
public class ContactRetrieval {


    public Set<Contact> retrieve(final ConstantContactCompositeSource ccSource, final List<ContactList> contactLists) throws Exception {
        
        final Set<Contact> contacts = new HashSet<Contact>();

        final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa zzz");

        String file = retrieveFile("active", ccSource);
        CsvReader csvReader = new CsvReader(new ByteArrayInputStream(file.getBytes()), Charset.forName("UTF-8"));
        csvReader.readHeaders();
        while (csvReader.readRecord()) {
            String email = csvReader.get("Email Address");
            for (ContactList contactList : contactLists) {
                String inList = csvReader.get("List: " + contactList.getShortName());
                if ("x".equals(inList)) {
                    contactList.getUsers().add(email);
                }
            }
            String firstName = csvReader.get("First Name");
            String lastName = csvReader.get("Last Name");
            String companyName = csvReader.get("Company Name");
            String jobTitle = csvReader.get("Job Title");
            String dateAddedString = csvReader.get("Date Added");
            Date dateAdded = dateFormat.parse(dateAddedString);
            contacts.add(new Contact(email, null, null, firstName + " " + lastName, firstName, lastName, companyName, jobTitle,
                    csvReader.get("Home Phone"), csvReader.get("Work Phone"), csvReader.get("City"), csvReader.get("State"), csvReader.get("Country"),
                    csvReader.get("Postal"), null,
                    "Custom field 1", "Custom field 2", "Custom field 3", "Custom field 4", "Custom field 5", "Custom field 6",
                    "Custom field 7", "Custom field 8", "Custom field 9", "Custom field 10", "Custom field 11", "Custom field 12",
                    "Custom field 13", "Custom field 14", "Custom field 15", null, null, null, dateAdded, dateAdded, csvReader.get("Added By")));
        }


        return contacts;
    }

    private String retrieveFile(String contactListID, ConstantContactCompositeSource ccSource) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, ParsingException, InterruptedException {
        Document doc = startBulkUpload(contactListID, ccSource);
        String id = doc.query("/entry/id/text()").get(0).getValue();

        System.out.println(id);

        id = id.replace("http://", "https://");

        boolean found = false;

        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConstantContactCompositeSource.CONSUMER_KEY, ConstantContactCompositeSource.CONSUMER_SECRET);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(ccSource.getTokenKey(), ccSource.getTokenSecret());
        HttpGet httpRequest = new HttpGet(id);
        httpRequest.setHeader("Accept", "application/xml");
        httpRequest.setHeader("Content-Type", "application/xml");
        consumer.sign(httpRequest);

        org.apache.http.client.HttpClient client = new DefaultHttpClient();
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };

        do {
            String string = client.execute(httpRequest, responseHandler);
            string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
            string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
            string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
            Document result = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
            String status = result.query("/entry/content/Activity/Status/text()").get(0).getValue();
            System.out.println(status);
            if ("COMPLETE".equals(status)) {
                found = true;
            } else {
                Thread.sleep(5000);
            }
        } while (!found);

        HttpGet getFile = new HttpGet(id + ".csv");
        consumer.sign(getFile);

        return client.execute(getFile, responseHandler);
    }

    private static Document startBulkUpload(String listID, ConstantContactCompositeSource ccSource) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, ParsingException {
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();

        OAuthConsumer consumer = new CommonsHttp3OAuthConsumer(ConstantContactCompositeSource.CONSUMER_KEY, ConstantContactCompositeSource.CONSUMER_SECRET);
        consumer.setMessageSigner(new HmacSha1MessageSigner());
        consumer.setTokenWithSecret(ccSource.getTokenKey(), ccSource.getTokenSecret());

        PostMethod postMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/activities");
        postMethod.setRequestHeader("Accept", "application/xml");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        List<String> columns = Arrays.asList("EMAIL%20ADDRESS", "FIRST%20NAME", "LAST%20NAME", "JOB%20TITLE", "COMPANY%20NAME", "WORK%20PHONE", "HOME%20PHONE", "CITY", "STATE", "COUNTRY", "POSTAL%20CODE",
                "CUSTOM%20FIELD%201", "CUSTOM%20FIELD%202", "CUSTOM%20FIELD%203", "CUSTOM%20FIELD%204", "CUSTOM%20FIELD%205",
                "CUSTOM%20FIELD%206", "CUSTOM%20FIELD%207", "CUSTOM%20FIELD%208", "CUSTOM%20FIELD%209", "CUSTOM%20FIELD%2010",
                "CUSTOM%20FIELD%2011", "CUSTOM%20FIELD%2012", "CUSTOM%20FIELD%2013", "CUSTOM%20FIELD%2014", "CUSTOM%20FIELD%2015");
        StringBuilder sb = new StringBuilder();
        for (String column : columns) {
            sb.append("columns=").append(column).append("&");
        }
        String ops = "activityType=EXPORT_CONTACTS&fileType=CSV&exportOptDate=true&exportOptSource=true&exportListName=true&sortBy=DATE_DESC&"+sb.toString()+"listId="+
        URLEncoder.encode("http://api.constantcontact.com/ws/customers/jboe99/lists/" + listID, "UTF-8");
        RequestEntity requestEntity = new StringRequestEntity(ops, "application/x-www-form-urlencoded", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        consumer.sign(postMethod);
        client.executeMethod(postMethod);
        String string = postMethod.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        return new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
    }
}

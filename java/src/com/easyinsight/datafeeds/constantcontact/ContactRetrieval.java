package com.easyinsight.datafeeds.constantcontact;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
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
import org.json.JSONException;
import org.json.JSONObject;

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

        String file = retrieveFile("Active", ccSource);
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
                    csvReader.get("Custom field 1"), csvReader.get("Custom field 2"), csvReader.get("Custom field 3"), csvReader.get("Custom field 4"),
                    csvReader.get("Custom field 5"), csvReader.get("Custom field 6"),
                    csvReader.get("Custom field 7"), csvReader.get("Custom field 8"), csvReader.get("Custom field 9"), csvReader.get("Custom field 10"),
                    csvReader.get("Custom field 11"), csvReader.get("Custom field 12"),
                    csvReader.get("Custom field 13"), csvReader.get("Custom field 14"), csvReader.get("Custom field 15"), null, null, null, dateAdded, dateAdded, csvReader.get("Added By")));
        }


        return contacts;
    }

    private String retrieveFile(String contactListID, ConstantContactCompositeSource ccSource) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, ParsingException, InterruptedException, ParseException, JSONException {
        String id = startBulkUpload(contactListID, ccSource);


        System.out.println(id);

        id = "https://api.constantcontact.com/v2/activities/" + id + "?api_key=" + ConstantContactCompositeSource.KEY;

        boolean found = false;

        HttpClient httpClient = new HttpClient();


        do {
            GetMethod getMethod = new GetMethod(id);
            getMethod.setRequestHeader("Authorization", "Bearer " + ccSource.getAccessToken());
            getMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
            getMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
            httpClient.executeMethod(getMethod);
            Map results = (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(getMethod.getResponseBodyAsString());
            String status = results.get("status").toString();
            System.out.println(status);
            if ("COMPLETE".equals(status)) {
                found = true;
            } else if ("ERROR".equals(status)) {
                throw new ReportException(new DataSourceConnectivityReportFault("Contact retrieval error.", ccSource));
            } else {
                Thread.sleep(5000);
            }
        } while (!found);

        GetMethod getFile = new GetMethod(id + ".csv" + "?api_key=" + ConstantContactCompositeSource.KEY);
        getFile.setRequestHeader("Authorization", "Bearer " + ccSource.getAccessToken());
        getFile.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        getFile.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        httpClient.executeMethod(getFile);
        return getFile.getResponseBodyAsString();
    }

    private static String startBulkUpload(String listID, ConstantContactCompositeSource ccSource) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException, IOException, ParsingException, JSONException, ParseException {
        HttpClient client = new HttpClient();



        PostMethod postMethod = new PostMethod("https://api.constantcontact.com/v2/activities/exportcontacts?api_key=" + ConstantContactCompositeSource.KEY);
        System.out.println(postMethod.getURI().toString());
        postMethod.setRequestHeader("Authorization", "Bearer " + ccSource.getAccessToken());
        postMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        postMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        JSONObject requestObject = new JSONObject();
        requestObject.put("file_type", "CSV");
        requestObject.put("sort_by", "EMAIL_ADDRESS");
        requestObject.put("export_date_added", true);
        requestObject.put("export_added_by", true);
        requestObject.put("lists", Arrays.asList(listID));
        requestObject.put("column_names", Arrays.asList("Email Address", "First Name", "Last Name", "Job Title", "Company Name", "Work Phone",
                "Home Phone", "City", "US State/CA Province", "Country", "Zip/Postal Code",
                "Custom field 1", "Custom field 2", "Custom field 3", "Custom field 4", "Custom field 5",
                "Custom field 6", "Custom field 7", "Custom field 8", "Custom field 9", "Custom field 10",
                "Custom field 11", "Custom field 12", "Custom field 13", "Custom field 14", "Custom field 15"));
        StringRequestEntity entity = new StringRequestEntity(requestObject.toString(), "application/json", "UTF-8");
        postMethod.setRequestEntity(entity);
        System.out.println(requestObject.toString());
        client.executeMethod(postMethod);
        System.out.println(postMethod.getResponseBodyAsString());
        Map results = (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(postMethod.getResponseBodyAsString());
        return results.get("id").toString();
    }
}

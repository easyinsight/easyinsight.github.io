package com.easyinsight.admin;

import com.csvreader.CsvReader;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.constantcontact.ContactList;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import net.minidev.json.parser.JSONParser;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/9/11
 * Time: 2:04 PM
 */
public class ConstantContactSync {


    public static final int CREATION_DAY = 1;
    public static final int ONE_WEEK = 2;
    public static final int TWO_WEEKS = 3;
    public static final int THREE_WEEKS = 4;
    public static final int FOUR_WEEKS = 5;
    public static final int ONE_DAY = 6;

    
    public static void updateContactLists() throws IOException, ParsingException, InterruptedException {
        Calendar cal = Calendar.getInstance();
        /*for (int i = 0; i < 32; i++) {

            // create drip marketing lists for the last 31 days

            newContactList(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }*/
        List<User> payingUsers = new ArrayList<User>();
        List<User> reactivateUsers = new ArrayList<User>();
        List<User> otherUsers = new ArrayList<User>();
        Map<String, List<User>> dripMarketingIncludeList = new HashMap<String, List<User>>();
        Map<String, List<User>> dripMarketingRemoveList = new HashMap<String, List<User>>();
        Map<String, String> dripMarketingMap = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, ContactList> map = new HashMap<String, ContactList>();
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();


            List<ContactList> contactLists = getContactLists();


            for (ContactList contactList : contactLists) {
                map.put(contactList.getShortName(), contactList);
            }

            List accounts = session.createQuery("from Account").list();
            for (Object obj : accounts) {
                Account account = (Account) obj;
                Date accountCreationDate = account.getCreationDate();

                for (User user : account.getUsers()) {
                    if ("".equals(user.getEmail().trim())) {
                        continue;
                    }
                    if (user.isOptInEmail()) {
                        if (account.getAccountState() == Account.ACTIVE) {
                            payingUsers.add(user);
                        } else if (account.getAccountState() == Account.REACTIVATION_POSSIBLE) {
                            reactivateUsers.add(user);
                        } else {
                            otherUsers.add(user);
                        }
                    }

                    /*if (accountCreationDate != null) {
                        System.out.println("testing " + accountCreationDate);
                        long then = accountCreationDate.getTime();
                        long now = System.currentTimeMillis();
                        long delta = now - then;
                        int days = (int) (delta / (1000 * 60 * 60 * 24));
                        System.out.println("days ago = " + days);
                        if (days < 32) {
                            String dripListName = "Drip Marketing " + sdf.format(account.getCreationDate());
                            String dripListID = map.get(dripListName).getId();
                            if (account.getAccountState() == Account.TRIAL && user.isOptInEmail()) {
                                List<User> users = dripMarketingIncludeList.get(dripListID);
                                if (users == null) {
                                    users = new ArrayList<User>();
                                    dripMarketingIncludeList.put(dripListID, users);
                                }
                                users.add(user);
                                dripMarketingMap.put(user.getEmail(), dripListName);
                            } else {
                                List<User> users = dripMarketingRemoveList.get(dripListID);
                                if (users == null) {
                                    users = new ArrayList<User>();
                                    dripMarketingRemoveList.put(dripListID, users);
                                }
                                users.add(user);
                                dripMarketingMap.put(user.getEmail(), null);
                            }
                        }
                    }*/
                }
            }

            
            

            
            // retrieve all users and their contact lists
            // for each drip marketing list, are all the users correctly sync'd?
            

            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        Set<String> purgeLists = getUsers(dripMarketingMap, map);
        purgeLists.add("43");
        purgeLists.add("44");
        purgeLists.add("45");

        removeUsersFromContactList(purgeLists);
        if (payingUsers.size() > 0) {
            addUsersToContactList("43", payingUsers);
        }
        if (reactivateUsers.size() > 0) {
            addUsersToContactList("44", otherUsers);
        }
        if (otherUsers.size() > 0) {
            addUsersToContactList("45", otherUsers);
        }

        /*for (String string : purgeLists) {
            if (string.equals("43") || string.equals("44") || string.equals("45")) {
                continue;
            }
            System.out.println("Purging " + string);
            List<User> users = dripMarketingIncludeList.get(string);
            if (users != null && users.size() > 0) {
                System.out.println("users size = " + users.size());
                addUsersToContactList(string, users);
            }
        }*/
    }
    
    public static void newContactList(Date date) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String name = "Drip Marketing " + sdf.format(date);
        String xml = "<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "  <id>data:,</id>\n" +
                "  <title />\n" +
                "  <author />\n" +
                "    <updated>2008-04-16</updated>\n" +
                "    <content type=\"application/vnd.ctct+xml\">\n" +
                "    <ContactList xmlns=\"http://ws.constantcontact.com/ns/1.0/\">\n" +
                "      <OptInDefault>false</OptInDefault>\n" +
                "            <Name>"+name+"</Name>\n" +
                "            <SortOrder>99</SortOrder>\n" +
                "        </ContactList>\n" +
                "    </content>\n" +
                "</entry>";
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod restMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/lists");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/atom+xml");
        StringRequestEntity entity = new StringRequestEntity(xml, "text/xml", "UTF-8");
        restMethod.setRequestEntity(entity);
        client.executeMethod(restMethod);
        System.out.println(restMethod.getResponseBodyAsString());
    }

    public static void getContact() throws IOException {
        
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        String email = URLEncoder.encode("eivideo@easy-insight.com", "UTF-8");
        System.out.println(email);
        GetMethod getMethod = new GetMethod("https://api.constantcontact.com/ws/customers/jboe99/contacts?email=" + email);
        client.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());
    }

    public static void addUsersToContactList(String contactListID, List<User> users) throws IOException, ParsingException, InterruptedException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod postMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/activities");
        postMethod.setRequestHeader("Accept", "application/xml");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder sb = new StringBuilder();
        sb.append("Email Address, First Name, Last Name\n");
        for (User user : users) {
            sb.append("\"").append(user.getEmail()).append("\",");
            sb.append("\"").append(user.getFirstName()).append("\",");
            sb.append("\"").append(user.getName()).append("\"\n");
        }
        System.out.println(sb.toString());
        //String blah = "Email Address, First Name, Last Name\n\"LiteraryCoder@gmail.com\",\"James\",\"Boe\"";
        String ops = "activityType=ADD_CONTACTS&data="+URLEncoder.encode(sb.toString(), "UTF-8")+"&lists="+
                URLEncoder.encode("http://api.constantcontact.com/ws/customers/jboe99/lists/" + contactListID, "UTF-8");
        RequestEntity requestEntity = new StringRequestEntity(ops, "application/x-www-form-urlencoded", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        client.executeMethod(postMethod);
        //System.out.println(postMethod.getResponseBodyAsString());
        Builder builder = new Builder();
        String response = postMethod.getResponseBodyAsString();
        System.out.println(response);
        response = response.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        response = response.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        response = response.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        Document doc = builder.build(new ByteArrayInputStream(response.getBytes()));
        String id = doc.query("/entry/id/text()").get(0).getValue();

        System.out.println(id);
        id = id.replace("http://", "https://");
        GetMethod getMethod = new GetMethod(id);
        getMethod.setRequestHeader("Accept", "application/xml");
        getMethod.setRequestHeader("Content-Type", "application/xml");
        boolean found = false;
        int tries = 0;
        do {
            client.executeMethod(getMethod);
            String string = getMethod.getResponseBodyAsString();
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
        } while (!found && tries++ < 30);
    }

    public static Set<String> getUsers(Map<String, String> dripMarketingMap, Map<String, ContactList> map) throws IOException, ParsingException, InterruptedException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod postMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/activities");
        postMethod.setRequestHeader("Accept", "application/xml");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder sb = new StringBuilder();
        sb.append("EMAIL%20ADDRESS");
        String ops = "activityType=EXPORT_CONTACTS&fileType=CSV&exportOptDate=false&exportOptSource=false&exportListName=true&sortBy=DATE_DESC&columns="+sb.toString()+"&listId="+
                URLEncoder.encode("http://api.constantcontact.com/ws/customers/jboe99/lists/active", "UTF-8");
        StringRequestEntity requestEntity = new StringRequestEntity(ops, "application/x-www-form-urlencoded", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        client.executeMethod(postMethod);

        Builder builder = new Builder();
        String response = postMethod.getResponseBodyAsString();
        System.out.println(response);
        response = response.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        response = response.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        response = response.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        Document doc = builder.build(new ByteArrayInputStream(response.getBytes()));
        String id = doc.query("/entry/id/text()").get(0).getValue();

        System.out.println(id);
        id = id.replace("http://", "https://");
        GetMethod getMethod = new GetMethod(id);
        getMethod.setRequestHeader("Accept", "application/xml");
        getMethod.setRequestHeader("Content-Type", "application/xml");
        boolean found = false;
        int tries = 0;
        do {
            client.executeMethod(getMethod);
            String string = getMethod.getResponseBodyAsString();
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
        } while (!found && tries++ < 30);
        GetMethod fileMethod = new GetMethod(id + ".csv");
        client.executeMethod(fileMethod);
        String csvResults = fileMethod.getResponseBodyAsString();
        System.out.println(csvResults);
        CsvReader csvReader = new CsvReader(new ByteArrayInputStream(csvResults.getBytes()), Charset.forName("UTF-8"));
        csvReader.readHeaders();
        Set<String> invalidLists = new HashSet<String>();
        while (csvReader.readRecord()) {
            String email = csvReader.get("Email Address");
            String dripMarketingList = dripMarketingMap.get(email);
            if (dripMarketingList == null) {
                for (String header : csvReader.getHeaders()) {
                    if (header.startsWith("Drip")) {
                        String value = csvReader.get(header);
                        if ("x".equals(value)) {
                            // need to rebuild header's list
                            invalidLists.add(map.get(header).getId());
                        }
                    }
                }
            } else {
                String value = csvReader.get(dripMarketingList);
                if (!"x".equals(value)) {
                    invalidLists.add(map.get(dripMarketingList).getId());
                }
            }
        }
        return invalidLists;
    }

    public static void removeUsersFromContactList(Set<String> purgeList) throws IOException, ParsingException, InterruptedException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod postMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/activities");
        postMethod.setRequestHeader("Accept", "application/xml");
        postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        StringBuilder sb = new StringBuilder();
        for (String list : purgeList) {
            sb.append("&lists=");
            sb.append(URLEncoder.encode("http://api.constantcontact.com/ws/customers/jboe99/lists/" + list, "UTF-8"));
        }
        System.out.println(sb.toString());
        String ops = "activityType=SV_CLEAR_INT" + sb.toString();
        RequestEntity requestEntity = new StringRequestEntity(ops, "application/x-www-form-urlencoded", "UTF-8");
        postMethod.setRequestEntity(requestEntity);
        client.executeMethod(postMethod);
        Builder builder = new Builder();
        String response = postMethod.getResponseBodyAsString();
        System.out.println(response);
        response = response.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        response = response.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        response = response.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        Document doc = builder.build(new ByteArrayInputStream(response.getBytes()));
        String id = doc.query("/entry/id/text()").get(0).getValue();

        System.out.println(id);
        id = id.replace("http://", "https://");
        GetMethod getMethod = new GetMethod(id);
        getMethod.setRequestHeader("Accept", "application/xml");
        getMethod.setRequestHeader("Content-Type", "application/xml");
        boolean found = false;
        int tries = 0;
        do {
            client.executeMethod(getMethod);
            String string = getMethod.getResponseBodyAsString();
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
        } while (!found && tries++ < 30);
    }
    
    private static List<ContactList> getContactLists() throws IOException, ParsingException {
        List<ContactList> contactLists = new ArrayList<ContactList>();
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        GetMethod getMethod = new GetMethod("https://api.constantcontact.com/ws/customers/jboe99/lists");
        getMethod.setRequestHeader("Accept", "application/xml");
        getMethod.setRequestHeader("Content-Type", "application/xml");
        client.executeMethod(getMethod);
        System.out.println(getMethod.getResponseBodyAsString());
        String string = getMethod.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        boolean hasMoreData;
        do {
            hasMoreData = false;
            Nodes nodes = doc.query("/feed/entry");
            for (int i = 0; i < nodes.size(); i++) {


                Node node = nodes.get(i);
                String idString = node.query("id/text()").get(0).getValue();
                String id = idString.split("/")[7];
                String name = node.query("content/ContactList/Name/text()").get(0).getValue();
                String shortName = node.query("content/ContactList/ShortName/text()").get(0).getValue();
                contactLists.add(new ContactList(id, name, shortName));
            }

            Nodes links = doc.query("/feed/link");

            for (int i = 0; i < links.size(); i++) {
                Element link = (Element) links.get(i);
                Attribute attribute = link.getAttribute("rel");
                if (attribute != null && "next".equals(attribute.getValue())) {
                    String linkURL = link.getAttribute("href").getValue();
                    hasMoreData = true;
                    getMethod = new GetMethod("https://api.constantcontact.com" + linkURL);
                    getMethod.setRequestHeader("Accept", "application/xml");
                    getMethod.setRequestHeader("Content-Type", "application/xml");
                    client.executeMethod(getMethod);
                    doc = new Builder().build(getMethod.getResponseBodyAsStream());
                    break;
                }
            }
        } while (hasMoreData);
        return contactLists;
    }

    public static void update(String email, String name, String firstName, String lastName, Collection<String> contactListIDs) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PutMethod putMethod = new PutMethod("https://api.constantcontact.com/ws/customers/jboe99/contacts/73");
        putMethod.setRequestHeader("Accept", "application/xml");
        putMethod.setRequestHeader("Content-Type", "application/atom+xml");
        StringBuilder sb = new StringBuilder();
        sb.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "  <title type=\"text\">Contact: eivideo@easy-insight.com</title>\n" +
                "  <updated>2012-01-12T23:02:39.116Z</updated>\n" +
                "  <author><name>Constant Contact</name></author>\n" +
                "  <id>http://api.constantcontact.com/ws/customers/jboe99/contacts/73</id>\n" +
                "  <summary type=\"text\">Contact</summary>\n" +
                "  <content type=\"application/vnd.ctct+xml\">\n" +
                "    <Contact xmlns=\"http://ws.constantcontact.com/ns/1.0/\">\n");
        sb.append("      <EmailAddress>"+email+"</EmailAddress>");
        sb.append("<Name>" + name + "</Name>");
        sb.append("<FirstName>" + firstName + "</FirstName>");
        sb.append("<LastName>" + lastName + "</LastName>");
        sb.append("<Status>Active</Status>");
        sb.append("      <OptInSource>ACTION_BY_CUSTOMER</OptInSource>\n" +
                "      <ContactLists>\n");
        for (String contactID : contactListIDs)  {
            sb.append("        <ContactList id=\"http://api.constantcontact.com/ws/customers/jboe99/lists/"+contactID+"\" />\n");
        }
        sb.append("      </ContactLists>\n" +
                "    </Contact>\n" +
                "  </content>\n" +
                "</entry>");
        String body = sb.toString();
        StringRequestEntity entity = new StringRequestEntity(body, "text/xml", "UTF-8");
        putMethod.setRequestEntity(entity);
        client.executeMethod(putMethod);
        System.out.println(putMethod.getResponseBodyAsString());
    }

    
    public static void publish(String email, String name, String firstName, String lastName) throws IOException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod restMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/contacts");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/atom+xml");
        String content = "<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "  <title type=\"text\"> </title>\n" +
                "  <updated>2008-07-23T14:21:06.407Z</updated>\n" +
                "  <author></author>\n" +
                "  <id>data:,none</id>\n" +
                "  <summary type=\"text\">Contact</summary>\n" +
                "  <content type=\"application/vnd.ctct+xml\">\n" +
                "    <Contact xmlns=\"http://ws.constantcontact.com/ns/1.0/\">\n";
        String emailXML =        "      <EmailAddress>"+email+"</EmailAddress>";
        String nameXML = "<Name>" + name + "</Name>";
        String firstNameXML = "<FirstName>" + firstName + "</FirstName>";
        String lastNameXML = "<LastName>" + lastName + "</LastName>";
        String statusXML = "<Status>Active</Status>";
        String content4XML = "      <OptInSource>ACTION_BY_CUSTOMER</OptInSource>\n" +
                "      <ContactLists>\n" +
                "        <ContactList id=\"http://api.constantcontact.com/ws/customers/jboe99/lists/7\" />\n" +
                "      </ContactLists>\n" +
                "    </Contact>\n" +
                "  </content>\n" +
                "</entry>";
        String body = content + emailXML + nameXML + firstNameXML + lastNameXML + statusXML + content4XML;
        System.out.println(body);
        StringRequestEntity entity = new StringRequestEntity(body, "text/xml", "UTF-8");
        restMethod.setRequestEntity(entity);
        client.executeMethod(restMethod);
        System.out.println(restMethod.getResponseBodyAsString());

    }
}

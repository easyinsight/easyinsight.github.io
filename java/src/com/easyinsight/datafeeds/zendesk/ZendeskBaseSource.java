package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.analysis.DataSourceConnectivityReportFault;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.logging.LogClass;
import net.minidev.json.parser.JSONParser;
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
import sun.misc.BASE64Encoder;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 6:38 PM
 */
public abstract class ZendeskBaseSource extends ServerDataSourceDefinition {

    protected static HttpClient getHttpClient(String username, String password) {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        return client;
    }

    protected static String queryField(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if(results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    protected static String queryField(Map n, String xpath) {
        Object obj = n.get(xpath);
        if(obj != null)
            return obj.toString();
        else
            return null;
    }

    protected Map queryList(String queryString, ZendeskCompositeSource zendeskCompositeSource, HttpClient client) {
        HttpMethod restMethod = new GetMethod(queryString);
        restMethod.setRequestHeader("Content-Type", "Content-Type: application/json; charset=utf-8");
        restMethod.setRequestHeader("User-Agent", "Easy Insight (http://www.easy-insight.com/)");
        boolean successful = false;
        int retryCount = 0;
        Map results = null;
        do {
            try {
                client.executeMethod(restMethod);
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Authentication to Zendesk failed.", zendeskCompositeSource));
                } else if (restMethod.getStatusCode() >= 500) {
                    throw new RuntimeException("Zendesk server error--please try again later.");
                }
                System.out.println(restMethod.getResponseBodyAsString());
                results = (Map) new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                restMethod.releaseConnection();
                successful = true;
            } catch (ReportException re) {
                throw re;
            } catch (Exception e) {
                LogClass.error(e);
                retryCount++;
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Zendesk could not be reached due to a large number of current users, please try again in a bit.");
        }

        return results;
    }

    public Document runRestRequest(ZendeskCompositeSource zendeskCompositeSource, HttpClient client, String path, Builder builder) throws InterruptedException {
        HttpMethod restMethod = new GetMethod(zendeskCompositeSource.getUrl() + path);
        String data;
        if (path.startsWith("/search") || path.startsWith("/api/v2"))
        {
            // add  Authorization header with base64 encoded "<username>:<password>"
            StringBuilder toEncode = new StringBuilder();
            toEncode.append(zendeskCompositeSource.getZdUserName()).append(":").append(zendeskCompositeSource.getZdPassword());
            byte[] bytes;
            try {
                bytes = toEncode.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            BASE64Encoder base64Encoder = new BASE64Encoder();
            restMethod.setRequestHeader("Authorization", "Basic " + base64Encoder.encode(bytes));
        }
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        boolean successful = false;
        Document doc = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                data = restMethod.getResponseBodyAsString();
                doc = builder.build(new ByteArrayInputStream(data.getBytes("UTF-8")));
//                doc = builder.build(restMethod.getResponseBodyAsStream());


                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 404) {
                    throw new ReportException(new DataSourceConnectivityReportFault("No Zendesk system was found at " +
                            zendeskCompositeSource.getUrl() + ". If your Zendesk account is using domain mapping, please use the actual Zendesk URL instead of the mapped domain.",
                            zendeskCompositeSource));
                }
                successful = true;
            } catch (Exception e) {
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Zendesk instance at " +
                            zendeskCompositeSource.getUrl(), zendeskCompositeSource));
                } else if (statusLine.indexOf("503") != -1) {
                    Thread.sleep(20000);
                    retryCount++;
                } else {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                }
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Zendesk could not be reached due to a large number of current users, please try again in a bit.");
        }
        return doc;
    }

    public Object runJSONRestRequest(ZendeskCompositeSource zendeskCompositeSource, HttpClient client, String path, Builder builder) throws InterruptedException {

        HttpMethod restMethod;
        if(path.startsWith("https://"))
            restMethod = new GetMethod(path);
        else
            restMethod = new GetMethod(zendeskCompositeSource.getUrl() + path);


        /*if (path.startsWith("/search"))
        {
            // add  Authorization header with base64 encoded "<username>:<password>"
            StringBuilder toEncode = new StringBuilder();
            toEncode.append(zendeskCompositeSource.getZdUserName()).append(":").append(zendeskCompositeSource.getZdPassword());
            byte[] bytes;
            try {
                bytes = toEncode.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            BASE64Encoder base64Encoder = new BASE64Encoder();
            restMethod.setRequestHeader("Authorization", "Basic " + base64Encoder.encode(bytes));
        }*/
        /*
        {"count":18,"results":
        [
        {"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},
        "due_at":null,"subject":"Ticket 25","forum_topic_id":null,"has_incidents":false,"type":null,
        "recipient":null,"id":25,"submitter_id":25249165,"assignee_id":25249165,"priority":null,"description":
        "Ticket 25","created_at":"2011-03-29T20:14:21Z",
        "fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],
        "custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],
        "external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,
        "url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/25.json","collaborator_ids":[],"updated_at":"2011-04-15T02:28:43Z",
        "organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},
        "due_at":null,"subject":"Blah!","forum_topic_id":null,"has_incidents":false,"type":"question","recipient":null,"id":3,"submitter_id":25249165,
        "assignee_id":25249165,"priority":"normal","description":"Blah!","created_at":"2011-03-29T17:26:55Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},
        {"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/3.json","collaborator_ids":[],"updated_at":"2011-04-23T02:05:59Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Blah blah blah","forum_topic_id":null,"has_incidents":false,"type":"question","recipient":null,"id":4,"submitter_id":25249165,"assignee_id":25249165,"priority":"urgent","description":"Blah Blah!","created_at":"2011-03-29T18:45:28Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/4.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:00Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Tag 6","forum_topic_id":null,"has_incidents":false,"type":"problem","recipient":null,"id":6,"submitter_id":25249165,"assignee_id":25249165,"priority":"urgent","description":"Tag 6","created_at":"2011-03-29T18:46:11Z","fields":[{"id":519737,"value":null},{"id":519811,"value":""},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":["tag6"],"custom_fields":[{"id":519737,"value":null},{"id":519811,"value":""},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/6.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:00Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 7","forum_topic_id":null,"has_incidents":false,"type":"problem","recipient":null,"id":7,"submitter_id":25249165,"assignee_id":25249165,"priority":"normal","description":"Ticket 7","created_at":"2011-03-29T20:09:41Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/7.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:01Z","organization_id":445979,"requester_id":25249165},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 11","forum_topic_id":null,"has_incidents":false,"type":"question","recipient":null,"id":11,"submitter_id":25249165,"assignee_id":25249165,"priority":"high","description":"Ticket 11","created_at":"2011-03-29T20:10:51Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/11.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:02Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 18","forum_topic_id":null,"has_incidents":false,"type":"question","recipient":null,"id":18,"submitter_id":25249165,"assignee_id":25249165,"priority":"urgent","description":"Ticket 18","created_at":"2011-03-29T20:12:36Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/18.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:03Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 22","forum_topic_id":null,"has_incidents":false,"type":"incident","recipient":null,"id":22,"submitter_id":25249165,"assignee_id":25249165,"priority":"normal","description":"Ticket 22","created_at":"2011-03-29T20:13:35Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/22.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:04Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 23","forum_topic_id":null,"has_incidents":false,"type":"problem","recipient":null,"id":23,"submitter_id":25249165,"assignee_id":25249165,"priority":"low","description":"Ticket 23","created_at":"2011-03-29T20:13:52Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/23.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:04Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 24","forum_topic_id":null,"has_incidents":false,"type":"question","recipient":null,"id":24,"submitter_id":25249165,"assignee_id":25249165,"priority":"low","description":"Ticket 24","created_at":"2011-03-29T20:14:06Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/24.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:06Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{},"rel":null,"from":{}},"channel":"web"},"due_at":null,"subject":"Ticket 26","forum_topic_id":null,"has_incidents":false,"type":"incident","recipient":null,"id":26,"submitter_id":25249165,"assignee_id":25249165,"priority":"urgent","description":"Ticket 26","created_at":"2011-03-29T20:14:39Z","fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"tags":[],"custom_fields":[{"id":519737,"value":null},{"id":519812,"value":null},{"id":519813,"value":null},{"id":519814,"value":null},{"id":519816,"value":"0"}],"external_id":null,"problem_id":null,"status":"closed","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/26.json","collaborator_ids":[],"updated_at":"2011-04-23T02:06:06Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"jboe99@gmail.com","name":"Jim Miller"}},"channel":"email"},"due_at":null,"subject":"blah2","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":59,"submitter_id":25265821,"assignee_id":null,"priority":null,"description":"blah2","created_at":"2012-10-08T20:03:27Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/59.json","collaborator_ids":[],"updated_at":"2012-10-08T20:03:27Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"cendie@easy-insight.com","name":"Cendie Lee"}},"channel":"email"},"due_at":null,"subject":"help help","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":62,"submitter_id":275195118,"assignee_id":null,"priority":null,"description":"i need some support for my issues.... special issues!!  ;-P\n\n-- \nCendie Lee\nMarketing Associate\nEasy Insight\ncendie@easy-insight.com","created_at":"2012-10-08T20:09:30Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/62.json","collaborator_ids":[],"updated_at":"2012-10-08T20:09:30Z","organization_id":445979,"requester_id":275195118},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"cendielee@yahoo.com","name":"Cendie Tsamadou"}},"channel":"email"},"due_at":null,"subject":"help","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":63,"submitter_id":272697167,"assignee_id":null,"priority":null,"description":"help help","created_at":"2012-10-08T20:12:18Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/63.json","collaborator_ids":[],"updated_at":"2012-10-08T20:12:18Z","organization_id":null,"requester_id":272697167},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"cendie@easy-insight.com","name":"Cendie Lee"}},"channel":"email"},"due_at":null,"subject":"fire","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":60,"submitter_id":275195118,"assignee_id":null,"priority":null,"description":"FIRE\n\n-- \nCendie Lee\nMarketing Associate\nEasy Insight\ncendie@easy-insight.com","created_at":"2012-10-08T20:05:25Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/60.json","collaborator_ids":[],"updated_at":"2012-10-08T20:05:25Z","organization_id":445979,"requester_id":275195118},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"jboe99@gmail.com","name":"Jim Miller"}},"channel":"email"},"due_at":null,"subject":"Hekp!","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":64,"submitter_id":25265821,"assignee_id":null,"priority":null,"description":"Hekp! How does this look?","created_at":"2012-10-08T20:16:00Z","fields":[{"id":21720687,"value":""}],"tags":[],"custom_fields":[{"id":21720687,"value":""}],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/64.json","collaborator_ids":[],"updated_at":"2012-10-14T23:37:22Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"jboe99@gmail.com","name":"Jim Miller"}},"channel":"email"},"due_at":null,"subject":"asdf","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":61,"submitter_id":25265821,"assignee_id":null,"priority":null,"description":"asdf","created_at":"2012-10-08T20:08:17Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/61.json","collaborator_ids":[],"updated_at":"2012-10-08T20:08:17Z","organization_id":null,"requester_id":25265821},{"result_type":"ticket","via":{"source":{"to":{"address":"support@easyinsight.zendesk.com","name":"Easy Insight"},"rel":null,"from":{"address":"jboe99@gmail.com","name":"Jim Miller"}},"channel":"email"},"due_at":null,"subject":"blah","forum_topic_id":null,"has_incidents":false,"type":null,"recipient":"support@easyinsight.zendesk.com","id":58,"submitter_id":25265821,"assignee_id":null,"priority":null,"description":"blah","created_at":"2012-10-08T19:57:26Z","fields":[],"tags":[],"custom_fields":[],"external_id":null,"problem_id":null,"status":"new","group_id":118982,"satisfaction_rating":null,"url":"https:\/\/easyinsight.zendesk.com\/api\/v2\/tickets\/58.json","collaborator_ids":[],"updated_at":"2012-10-08T19:57:26Z","organization_id":null,"requester_id":25265821}],"previous_page":null,"next_page":null}
         */
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(zendeskCompositeSource.getZdUserName(), zendeskCompositeSource.getZdPassword());
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        restMethod.setRequestHeader("Accept", "application/json");
        restMethod.setRequestHeader("Content-Type", "application/json");
        boolean successful = false;

        Object obj = null;
        int retryCount = 0;
        do {
            try {
                client.executeMethod(restMethod);
                obj = new net.minidev.json.parser.JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(restMethod.getResponseBodyAsStream());
                if (restMethod.getStatusCode() == 401) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                } else if (restMethod.getStatusCode() == 404) {
                    throw new ReportException(new DataSourceConnectivityReportFault("No Zendesk system was found at " +
                            zendeskCompositeSource.getUrl() + ". If your Zendesk account is using domain mapping, please use the actual Zendesk URL instead of the mapped domain.",
                            zendeskCompositeSource));
                }
                successful = true;
            } catch (Exception e) {
                if(restMethod == null || restMethod.getStatusLine() == null)
                    throw new RuntimeException(e);
                String statusLine = restMethod.getStatusLine().toString();
                if ("HTTP/1.1 404 Not Found".equals(statusLine)) {
                    throw new ReportException(new DataSourceConnectivityReportFault("Could not locate a Zendesk instance at " +
                            zendeskCompositeSource.getUrl(), zendeskCompositeSource));
                } else if (statusLine.indexOf("503") != -1) {
                    Thread.sleep(20000);
                    retryCount++;
                } else {
                    throw new ReportException(new DataSourceConnectivityReportFault("Invalid Zendesk credentials in connecting to " +
                            zendeskCompositeSource.getUrl() + ".",
                            zendeskCompositeSource));
                }
            }
        } while (!successful && retryCount < 10);
        if (!successful) {
            throw new RuntimeException("Zendesk could not be reached due to a large number of current users, please try again in a bit.");
        }
        return obj;
    }
}

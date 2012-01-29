package com.easyinsight.admin;

import com.csvreader.CsvReader;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.constantcontact.ContactList;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.User;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.hibernate.Session;

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
    public static void main(String[] args) throws Exception, ParsingException, InterruptedException {

         //publish("eivideo@easy-insight.com", "James Boe", "James", "Birkholz");
        //getContact();
        //update("eivideo@easy-insight.com", "James Boe", "James", "Miller", Arrays.asList("7", "9"));
        //String blah = "Email Address, First Name, Last Name\n\"eivideo@easy-insight.com\",\"James\",\"Boe\"";
        //System.out.println(URLEncoder.encode(blah, "UTF-8"));
        //newContactList();
        //alt();
        //remove();

        // on nightly task...
        // run one add user sync
        //      add user to all list
        //      add user to active subscriber list if account is active
        //      add user to expired/trial list if account is trial, delinquent, etc
        //      if account was created < 31 days ago, add to the appropriate drip marketing contact list
        // send newsletter to appropriate drip marketing contact list
        
        //getUsers();
        createCampaign();
        /*String string = "http://api.constantcontact.com/ws/customers/jboe99/campaigns/1109097494089";
        System.out.println();*/
    }

    public static final int CREATION_DAY = 1;
    public static final int ONE_WEEK = 2;
    public static final int TWO_WEEKS = 3;
    public static final int THREE_WEEKS = 4;
    public static final int FOUR_WEEKS = 5;

    private static String getSubject(int marketingType) {
        switch (marketingType) {
            case CREATION_DAY:
                return "Some tips to get you started with Easy Insight";
            case ONE_WEEK:
                return "Tips for the best Easy Insight experience";
            case TWO_WEEKS:
                return "More tips for the best Easy Insight experience";
            case THREE_WEEKS:
                return "More tips for the best Easy Insight experience";
            case FOUR_WEEKS:
                return "Your Easy Insight trial is almost over";
        }
        return "";
    }

    private static String getContent(int marketingType) {
        switch (marketingType) {
            case CREATION_DAY:
                return "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <title>What's new with Easy Insight...</title>\n" +
                        "    <style type=\"text/css\" media=\"screen\">\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: \"Lucida Grande\", verdana, arial, helvetica, sans-serif;\n" +
                        "        }\n" +
                        "\n" +
                        "        a {\n" +
                        "            color: #2c6487;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"width:100%;text-align:center;margin:0 auto;\">\n" +
                        "<div style=\"width:580px;margin:0 auto;\">\n" +
                        "    <div style=\"background-color: #F0F0F0;padding:15px\">\n" +
                        "        <div style=\"background-color: #FFFFFF\">\n" +
                        "            <div style=\"background-color: #FAFAFA;padding:5px\">\n" +
                        "                <div>\n" +
                        "                    <a href=\"http://www.easy-insight.com/\"><img src=\"http://www.easy-insight.com/images/logo2.PNG\" alt=\"Easy Insight Logo\" name=\"logo\" id=\"logo\"/></a>\n" +
                        "                </div>\n" +
                        "                <div>\n" +
                        "                    <a href=\"https://www.easy-insight.com/app\">Sign Into Easy Insight Now</a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "            <div style=\"padding:10px\">\n" +
                        "\n" +
                        "                <div style=\"padding-top:40px;padding-left:10px;padding-right:10px;padding-bottom:25px;float:left;width:275px\">\n" +
                        "                    <img src=\"http://www.easy-insight.com/images/ReportEditorEmail.jpg\" alt=\"Report Editor Image\"/>\n" +
                        "                </div>\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Building Reports</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">You can easily build your own reports inside of Easy Insight using our simple drag and drop report editor. Whether it's a simple list, a chart, or something more involved, you can create any type of visualization through the editor. For more information on building reports, take a look at <a href=\"http://www.easy-insight.com/documentation/reportediting.html\">http://www.easy-insight.com/documentation/reportediting.html</a>.</p>\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Prebuilt Reports and Dashboards</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">A variety of prebuilt reports and dashboards are available to help you get started. Check out the Exchange inside Easy Insight to find a wide variety of available reports and dashboards. In particular, the Recommended Reports section will provide you with a curated set of reports and dashboards designed around common use cases for your particular data source.</p>\n" +
                        "\n" +
                        "                <h2 style=\"font-size:16px\">Filtering</h2>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Want to see only the set of tasks completed in the last week? Only deals belonging to John Smith and Jane Jones? Take advantage of filters to refine your reports down to the key subsets of information you actually want to look at. Check out the filtering documentation at <a href=\"http://www.easy-insight.com/documentation/filters.html\">http://www.easy-insight.com/documentation/filters.html</a> for more information.</p>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!</p>\n" +
                        "                <div align=\"center\">\n" +
                        "                    <a href=\"http://twitter.com/easyinsight\"><img border=\"0\" src=\"http://www.easy-insight.com/images/twitter.png\" alt=\"Follow us on Twitter\"/></a>\n" +
                        "                    <a href=\"http://www.facebook.com/pages/Easy-Insight/445964470213\"><img border=\"0\" src=\"http://www.easy-insight.com/images/facebook.png\" alt=\"Follow us on Facebook\"/></a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
            case ONE_WEEK:
                return "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <title>What's new with Easy Insight...</title>\n" +
                        "    <style type=\"text/css\" media=\"screen\">\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: \"Lucida Grande\", verdana, arial, helvetica, sans-serif;\n" +
                        "        }\n" +
                        "\n" +
                        "        a {\n" +
                        "            color: #2c6487;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"width:100%;text-align:center;margin:0 auto;\">\n" +
                        "<div style=\"width:580px;margin:0 auto;\">\n" +
                        "    <div style=\"background-color: #F0F0F0;padding:15px\">\n" +
                        "        <div style=\"background-color: #FFFFFF\">\n" +
                        "            <div style=\"background-color: #FAFAFA;padding:5px\">\n" +
                        "                <div>\n" +
                        "                    <a href=\"http://www.easy-insight.com/\"><img src=\"http://www.easy-insight.com/images/logo2.PNG\" alt=\"Easy Insight Logo\" name=\"logo\" id=\"logo\"/></a>\n" +
                        "                </div>\n" +
                        "                <div>\n" +
                        "                    <a href=\"https://www.easy-insight.com/app\">Sign Into Easy Insight Now</a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "            <div style=\"padding:10px\">\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Scheduled Report Email Delivery</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">Want a list of the hottest leads in your sales system in your inbox at 6 AM every morning? A chart of hours billed by project sent to your executive team every month? You can schedule any report in Easy Insight to be emailed on a regular basis. Take a look at the Scheduling page inside of the application to get started.</p>\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Exporting to Excel and PDF</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">You can export any report inside of Easy Insight out into Excel or PDF formats. From the report view, just click on the Export button at the top of the screen. From the window that pops up, you can export into these formats for printing or emailing reports to other members of your team.</p>\n" +
                        "\n" +
                        "                <h2 style=\"font-size:16px\">Custom Fields</h2>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">You can greatly expand your capabilities for reporting on any particular SaaS system with custom fields. Your CRM system doesn't have an Expected Close Date or Target Revenue? You can add custom fields in Easy Insight through multiple mechanisms. Check out our documentation at <a href=\"http://www.easy-insight.com/documentation/customfields.html\">http://www.easy-insight.com/documentation/customfields.html</a> for further information about these capabilities.</p>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!</p>\n" +
                        "                <div align=\"center\">\n" +
                        "                    <a href=\"http://twitter.com/easyinsight\"><img border=\"0\" src=\"http://www.easy-insight.com/images/twitter.png\" alt=\"Follow us on Twitter\"/></a>\n" +
                        "                    <a href=\"http://www.facebook.com/pages/Easy-Insight/445964470213\"><img border=\"0\" src=\"http://www.easy-insight.com/images/facebook.png\" alt=\"Follow us on Facebook\"/></a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
            case TWO_WEEKS:
                return "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <title>What's new with Easy Insight...</title>\n" +
                        "    <style type=\"text/css\" media=\"screen\">\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: \"Lucida Grande\", verdana, arial, helvetica, sans-serif;\n" +
                        "        }\n" +
                        "\n" +
                        "        a {\n" +
                        "            color: #2c6487;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"width:100%;text-align:center;margin:0 auto;\">\n" +
                        "<div style=\"width:580px;margin:0 auto;\">\n" +
                        "    <div style=\"background-color: #F0F0F0;padding:15px\">\n" +
                        "        <div style=\"background-color: #FFFFFF\">\n" +
                        "            <div style=\"background-color: #FAFAFA;padding:5px\">\n" +
                        "                <div>\n" +
                        "                    <a href=\"http://www.easy-insight.com/\"><img src=\"http://www.easy-insight.com/images/logo2.PNG\" alt=\"Easy Insight Logo\" name=\"logo\" id=\"logo\"/></a>\n" +
                        "                </div>\n" +
                        "                <div>\n" +
                        "                    <a href=\"https://www.easy-insight.com/app\">Sign Into Easy Insight Now</a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "            <div style=\"padding:10px\">\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Connect to More Systems</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">If you just started using Easy Insight to report on a single system, take a look at our Connections page to see what other systems we might be able to help you with as well. If you just started using Easy Insight for Highrise reporting, but have Zendesk and Harvest in house as well, you can connect your Easy Insight account to those systems as well to get the same sophisticated reporting across all three systems!</p>\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Calculations</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">Easy Insight has a powerful set of scripting capabilities for building out custom calculations. You can take advantage of a wide variety of functions to build out more in depth analysis on your reports. Take a look at the documentation at <a href=\"http://www.easy-insight.com/documentation/scripting.html\">http://www.easy-insight.com/documentation/scripting.html</a> for more information.</p>\n" +
                        "\n" +
                        "                <h2 style=\"font-size:16px\">API</h2>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Want to publish your own data into Easy Insight? Easy Insight provides a REST API to make it easy for you to publish in custom data from any system. Our API documentation and helper libraries are available at <a href=\"http://www.easy-insight.com/documentation/api.html\">http://www.easy-insight.com/documentation/api.html</a>.</p>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!</p>\n" +
                        "                <div align=\"center\">\n" +
                        "                    <a href=\"http://twitter.com/easyinsight\"><img border=\"0\" src=\"http://www.easy-insight.com/images/twitter.png\" alt=\"Follow us on Twitter\"/></a>\n" +
                        "                    <a href=\"http://www.facebook.com/pages/Easy-Insight/445964470213\"><img border=\"0\" src=\"http://www.easy-insight.com/images/facebook.png\" alt=\"Follow us on Facebook\"/></a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
            case THREE_WEEKS:
                return "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <title>What's new with Easy Insight...</title>\n" +
                        "    <style type=\"text/css\" media=\"screen\">\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: \"Lucida Grande\", verdana, arial, helvetica, sans-serif;\n" +
                        "        }\n" +
                        "\n" +
                        "        a {\n" +
                        "            color: #2c6487;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"width:100%;text-align:center;margin:0 auto;\">\n" +
                        "<div style=\"width:580px;margin:0 auto;\">\n" +
                        "    <div style=\"background-color: #F0F0F0;padding:15px\">\n" +
                        "        <div style=\"background-color: #FFFFFF\">\n" +
                        "            <div style=\"background-color: #FAFAFA;padding:5px\">\n" +
                        "                <div>\n" +
                        "                    <a href=\"http://www.easy-insight.com/\"><img src=\"http://www.easy-insight.com/images/logo2.PNG\" alt=\"Easy Insight Logo\" name=\"logo\" id=\"logo\"/></a>\n" +
                        "                </div>\n" +
                        "                <div>\n" +
                        "                    <a href=\"https://www.easy-insight.com/app\">Sign Into Easy Insight Now</a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "            <div style=\"padding:10px\">\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Building Dashboards</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">Once you have a handful of reports built up in your Easy Insight system, you can take the next step by connecting them into full interactive dashboards! Take a look at the dashboard documentation at <a href=\"http://www.easy-insight.com/documentation/dashboards.html\">http://www.easy-insight.com/documentation/dashboards.html</a> for more information on how you can build your own dashboards.</p>\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Combining Data Sources</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">Have multiple Highrise systems you want to combine into a single view? Want to join data from your Constant Contact system with your Salesforce system? Easy Insight has tremendous capabilities around joining data from disparate data sources. Documentation around how to combine data sources is available at <a href=\"http://www.easy-insight.com/documentation/datasources.html#r12\">http://www.easy-insight.com/documentation/datasources.html#r12</a>.</p>\n" +
                        "\n" +
                        "                <h2 style=\"font-size:16px\">Customizing Look and Feel</h2>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Want to style Easy Insight to meet your corporate branding or just to look a little more like your desktop? You can define styling at a user or account level from the Account Settings page. Whether you just want to change the application background to a particular favorite image or overhaul the entire color scheme, check out the options offered under Account and User Skins to make the look of Easy Insight match your preferences. You can also customize the individual appearance of particular reports by clicking on the \"Edit Report Properties...\" button in the report editor.</p>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">Have any questions or comments? Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com!</p>\n" +
                        "                <div align=\"center\">\n" +
                        "                    <a href=\"http://twitter.com/easyinsight\"><img border=\"0\" src=\"http://www.easy-insight.com/images/twitter.png\" alt=\"Follow us on Twitter\"/></a>\n" +
                        "                    <a href=\"http://www.facebook.com/pages/Easy-Insight/445964470213\"><img border=\"0\" src=\"http://www.easy-insight.com/images/facebook.png\" alt=\"Follow us on Facebook\"/></a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
            case FOUR_WEEKS:
                return "<html>\n" +
                        "<head>\n" +
                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                        "    <title>What's new with Easy Insight...</title>\n" +
                        "    <style type=\"text/css\" media=\"screen\">\n" +
                        "        body {\n" +
                        "            margin: 0;\n" +
                        "            padding: 0;\n" +
                        "            font-family: \"Lucida Grande\", verdana, arial, helvetica, sans-serif;\n" +
                        "        }\n" +
                        "\n" +
                        "        a {\n" +
                        "            color: #2c6487;\n" +
                        "            text-decoration: none;\n" +
                        "        }\n" +
                        "\n" +
                        "\n" +
                        "    </style>\n" +
                        "\n" +
                        "\n" +
                        "</head>\n" +
                        "<body style=\"width:100%;text-align:center;margin:0 auto;\">\n" +
                        "<div style=\"width:580px;margin:0 auto;\">\n" +
                        "    <div style=\"background-color: #F0F0F0;padding:15px\">\n" +
                        "        <div style=\"background-color: #FFFFFF\">\n" +
                        "            <div style=\"background-color: #FAFAFA;padding:5px\">\n" +
                        "                <div>\n" +
                        "                    <a href=\"http://www.easy-insight.com/\"><img src=\"http://www.easy-insight.com/images/logo2.PNG\" alt=\"Easy Insight Logo\" name=\"logo\" id=\"logo\"/></a>\n" +
                        "                </div>\n" +
                        "                <div>\n" +
                        "                    <a href=\"https://www.easy-insight.com/app\">Sign Into Easy Insight Now</a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "            <div style=\"padding:10px\">\n" +
                        "\n" +
                        "            <h2 style=\"font-size:16px\">Your Free Trial is About to Expire</h2>\n" +
                        "\n" +
                        "            <p style=\"font-size:13px;text-align:left\">Your 30 day free trial with Easy Insight is about to expire. You can enter billing information from the Account page by clicking on \"Set Up Billing.\" Once your trial expires, you'll also be automatically redirected to the billing page. Have any questions about pricing? Check out <a href=\"http://www.easy-insight.com/pricing.html\">http://www.easy-insight.com/pricing.html</a>.</p>\n" +
                        "\n" +
                        "                <p style=\"font-size:13px;text-align:left\">We hope you've enjoyed your experience with using Easy Insight! Please don't hesitate to contact us at sales@easy-insight.com or support@easy-insight.com if you have any questions.</p>\n" +
                        "                <div align=\"center\">\n" +
                        "                    <a href=\"http://twitter.com/easyinsight\"><img border=\"0\" src=\"http://www.easy-insight.com/images/twitter.png\" alt=\"Follow us on Twitter\"/></a>\n" +
                        "                    <a href=\"http://www.facebook.com/pages/Easy-Insight/445964470213\"><img border=\"0\" src=\"http://www.easy-insight.com/images/facebook.png\" alt=\"Follow us on Facebook\"/></a>\n" +
                        "                </div>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "    </div>\n" +
                        "\n" +
                        "</div>\n" +
                        "\n" +
                        "</body>\n" +
                        "</html>";
        }
        return "";
    }

    private static void createDripMarketingCampaign(ContactList contactList, int marketingType) throws IOException, ParsingException {
        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String now = sdf.format(new Date());
        String subject = getSubject(marketingType);
        String name = "Drip Marketing - " + now + " - " + marketingType;
        String content = getContent(marketingType);
        String contentHTML = StringEscapeUtils.escapeHtml(content);
        sb.append("<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "  <link href=\"/ws/customers/jboe99/campaigns\" rel=\"edit\" />\n" +
                "  <id>http://api.constantcontact.com/ws/customers/jboe99/campaigns</id>\n" +
                "  <title type=\"text\">"+name+"</title>\n" +
                "  <updated>" + now + "</updated>\n" +
                "  <author>\n" +
                "    <name>James Boe</name>\n" +
                "  </author>\n" +
                "  <content type=\"application/vnd.ctct+xml\">\n" +
                "    <Campaign xmlns=\"http://ws.constantcontact.com/ns/1.0/\">\n" +
                "      <Name>"+name+"</Name>\n" +
                "      <Status>Draft</Status>\n" +
                "      <Date>"+now+"</Date>\n" +
                "      <Subject>"+subject+"</Subject>\n" +
                "      <FromName>jboe@easy-insight.com</FromName>\n" +
                "      <ViewAsWebpage>NO</ViewAsWebpage>\n" +
                "      <ViewAsWebpageLinkText></ViewAsWebpageLinkText>\n" +
                "      <ViewAsWebpageText></ViewAsWebpageText>\n" +
                "      <PermissionReminder>NO</PermissionReminder>\n" +
                "      <PermissionReminderText>You're receiving this email because of your relationship with Easy Insight. \n" +
                "Please &lt;ConfirmOptin>&lt;a style=\"color:#0000ff;\">confirm&lt;/a>&lt;/ConfirmOptin> \n" +
                "your continued interest in receiving email from us.</PermissionReminderText>\n" +
                "      <GreetingSalutation>Dear</GreetingSalutation>\n" +
                "      <GreetingName>FirstName</GreetingName>\n" +
                "      <GreetingString>Greetings!</GreetingString>\n" +
                "      <OrganizationName>Easy Insight</OrganizationName>\n" +
                "      <OrganizationAddress1>1401 Wewatta St Unit 606</OrganizationAddress1>\n" +
                "      <OrganizationAddress2></OrganizationAddress2>\n" +
                "      <OrganizationAddress3></OrganizationAddress3>\n" +
                "      <OrganizationCity>Denver</OrganizationCity>\n" +
                "      <OrganizationState>CO</OrganizationState>\n" +
                "      <OrganizationInternationalState></OrganizationInternationalState>\n" +
                "      <OrganizationCountry>us</OrganizationCountry>\n" +
                "      <OrganizationPostalCode>80202</OrganizationPostalCode>\n" +
                "      <IncludeForwardEmail>NO</IncludeForwardEmail>\n" +
                "      <ForwardEmailLinkText></ForwardEmailLinkText>\n" +
                "      <IncludeSubscribeLink>NO</IncludeSubscribeLink>\n" +
                "      <SubscribeLinkText></SubscribeLinkText>\n" +
                "      <EmailContentFormat>HTML</EmailContentFormat>\n" +
                "      <EmailContent>"+contentHTML+"</EmailContent>\n" +
                "      <EmailTextContent>&lt;Text>This is the text version.&lt;/Text></EmailTextContent>\n" +
                "      <StyleSheet></StyleSheet>\n" +
                "      <ContactLists>\n" +
                "        <ContactList id=\"http://api.constantcontact.com/ws/customers/jboe99/lists/"+contactList.getId()+"\">\n" +
                "          <link xmlns=\"http://www.w3.org/2005/Atom\" href=\"/ws/customers/jboe99/lists/"+contactList.getId()+"\" rel=\"self\" />\n" +
                "        </ContactList>\n" +
                "      </ContactLists>\n" +
                "      <FromEmail>\n" +
                "<Email id=\"http://api.constantcontact.com/ws/customers/jboe99/settings/emailaddresses/1\">\n" +
                "          <link xmlns=\"http://www.w3.org/2005/Atom\" href=\"/ws/customers/jboe99/settings/emailaddresses/1\"\n" +
                "          rel=\"self\" />\n" +
                "        </Email>" +
                "        <EmailAddress>sales@easy-insight.com</EmailAddress>\n" +
                "      </FromEmail>\n" +
                "      <ReplyToEmail>\n" +
                "<Email id=\"http://api.constantcontact.com/ws/customers/jboe99/settings/emailaddresses/1\">\n" +
                "          <link xmlns=\"http://www.w3.org/2005/Atom\" href=\"/ws/customers/jboe99/settings/emailaddresses/1\"\n" +
                "          rel=\"self\" />\n" +
                "        </Email>"+
                "        <EmailAddress>sales@easy-insight.com</EmailAddress>\n" +
                "      </ReplyToEmail>\n" +
                "    </Campaign>\n" +
                "  </content>\n" +
                "  <source>\n" +
                "    <id>http://api.constantcontact.com/ws/customers/jboe99/campaigns</id>\n" +
                "    <title type=\"text\">Campaigns for customer: jboe99</title>\n" +
                "    <link href=\"campaigns\" />\n" +
                "    <link href=\"campaigns\" rel=\"self\" />\n" +
                "    <author>\n" +
                "      <name>jboe99</name>\n" +
                "    </author>\n" +
                "    <updated>"+now+"</updated>\n" +
                "  </source>\n" +
                "</entry>");
        System.out.println(sb.toString());
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials("cec7e39c-25fc-43e6-a423-bf02de492d87%jboe99", "e@symone$");
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        PostMethod restMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/campaigns");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/atom+xml");
        StringRequestEntity entity = new StringRequestEntity(sb.toString(), "text/xml", "UTF-8");
        restMethod.setRequestEntity(entity);
        client.executeMethod(restMethod);
        String string = restMethod.getResponseBodyAsString();
        string = string.replace("xmlns=\"http://www.w3.org/2005/Atom\"", "");
        string = string.replace("xmlns=\"http://ws.constantcontact.com/ns/1.0/\"", "");
        string = string.replace("xmlns=\"http://www.w3.org/2007/app\"", "");
        System.out.println(string);
        Document doc = new Builder().build(new ByteArrayInputStream(string.getBytes("UTF-8")));
        String campaignID = doc.query("/entry/id/text()").get(0).getValue().split("/")[7];
        System.out.println(campaignID);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, 8);
        Date timeToRun = cal.getTime();
        String timeToRunString = sdf.format(timeToRun);

        /*String sendCampaignXML = "<entry xmlns=\"http://www.w3.org/2005/Atom\">\n" +
                "    <link href=\"/ws/customers/jboe99/campaigns/"+campaignID+"/schedules/1\" rel=\"edit\" />\n" +
                "    <id>http://api.constantcontact.com/ws/customers/jboe99/campaigns/"+campaignID+"/schedules/1</id>\n" +
                "    <title type=\"text\">"+now+"</title>\n" +
                "    <updated>"+now+"</updated>\n" +
                "    <author>\n" +
                "        <name>jboe99</name>\n" +
                "    </author>\n" +
                "    <content type=\"application/vnd.ctct+xml\">\n" +
                "        <Schedule xmlns=\"http://ws.constantcontact.com/ns/1.0/\">\n" +
                "            <ScheduledTime>"+timeToRunString+"</ScheduledTime>\n" +
                "        </Schedule>\n" +
                "    </content>\n" +
                "</entry>";

        PostMethod launchMethod = new PostMethod("https://api.constantcontact.com/ws/customers/jboe99/campaigns/" + campaignID + "/schedules");
        launchMethod.setRequestHeader("Accept", "application/xml");
        launchMethod.setRequestHeader("Content-Type", "application/atom+xml");
        StringRequestEntity launchEntity = new StringRequestEntity(sendCampaignXML, "text/xml", "UTF-8");
        launchMethod.setRequestEntity(launchEntity);
        client.executeMethod(launchMethod);
        String launchresult = launchMethod.getResponseBodyAsString();
        System.out.println(launchresult);*/
    }
    
    private static ContactList findContactList(Date date, List<ContactList> contactLists) {
        SimpleDateFormat dripSDF = new SimpleDateFormat("yyyy-MM-dd");
        String listName = "Drip Marketing " + dripSDF.format(date);
        for (ContactList contactList : contactLists) {
            if (contactList.getName().equals(listName)) {
                return contactList;
            }
        }
        return null;
    }

    public static void createCampaign() throws Exception {
        List<ContactList> contactLists = getContactLists();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -14);
        ContactList creationDayContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(creationDayContactList, CREATION_DAY);
    }
    
    public static void createAndLaunchDripMarketingCampaigns() throws Exception {
        List<ContactList> contactLists = getContactLists();

        Calendar cal = Calendar.getInstance();
        // today
        ContactList creationDayContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(creationDayContactList, CREATION_DAY);
        // one week ago

        cal.add(Calendar.DAY_OF_YEAR, -7);

        ContactList oneWeekContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(oneWeekContactList, ONE_WEEK);

        // two weeks ago

        cal.add(Calendar.DAY_OF_YEAR, -7);

        ContactList twoWeekContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(twoWeekContactList, TWO_WEEKS);

        // three weeks ago

        cal.add(Calendar.DAY_OF_YEAR, -7);

        ContactList threeWeeksContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(threeWeeksContactList, THREE_WEEKS);

        // and four weeks ago

        cal.add(Calendar.DAY_OF_YEAR, -7);

        ContactList fourWeeksContactList = findContactList(cal.getTime(), contactLists);
        createDripMarketingCampaign(fourWeeksContactList, FOUR_WEEKS);
    }
    
    public static void updateContactLists() throws IOException, ParsingException, InterruptedException {
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 32; i++) {

            // create drip marketing lists for the last 31 days

            newContactList(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, -1);
        }
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

                    if (accountCreationDate != null) {
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
                    }
                }
            }

            
            

            
            // retrieve all users and their contact lists
            // for each drip marketing list, are all the users correctly sync'd?
            

            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.getTransaction().commit();
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

        for (String string : purgeLists) {
            if (string.equals("43") || string.equals("44") || string.equals("45")) {
                continue;
            }
            System.out.println("Purging " + string);
            List<User> users = dripMarketingIncludeList.get(string);
            if (users != null && users.size() > 0) {
                System.out.println("users size = " + users.size());
                addUsersToContactList(string, users);
            }
        }
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

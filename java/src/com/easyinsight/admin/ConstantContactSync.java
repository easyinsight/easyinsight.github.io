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

    public static String getContent(int marketingType) {
        switch (marketingType) {
            case CREATION_DAY:
                return "<html>\n" +
                        "<body>\n" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
                        "       style=\"margin:0;padding: 10px 0 0;background-color:#fafafa;min-height:100%;width:100%\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                   style=\"border:1px solid #dddddd;background-color:#ffffff\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-bottom:0;margin-top: 10px\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"border-collapse:collapse;color:#202020;font-family:Arial,serif;font-size:34px;font-weight:bold;line-height:100%;padding:0;text-align:center;vertical-align:middle\">\n" +
                        "                                    <img alt=\"Easy Insight Logo\"\n" +
                        "                                         style=\"border:0;outline:none;text-decoration:none\" src=\"http://www.easy-insight.com/images/logo2.PNG\">\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse;background-color:#ffffff\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                                <div style=\"color:#505050;font-family:Arial,serif;font-size:14px;line-height:150%;text-align:left\">\n" +
                        "\n" +
                        "                                                    Dear {1},\n" +
                        "                                                    <br><br>\n" +
                        "                                                    Welcome to Easy Insight! This is the first of four emails that we\n" +
                        "                                                    will send you to help you get up and running with Easy Insight!\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Sign in here: <a href=\"https://www.easy-insight.com/app\">Easy\n" +
                        "                                                    Insight Login</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Getting Started with Connections</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Your first step is to connect to your data with Easy Insight. Check out the links below for a detailed example of how to set up your first connection.\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#highriseConnection\">Connecting\n" +
                        "                                                        to Highrise</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#basecampConnection\">Connecting\n" +
                        "                                                        to Basecamp</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Building Reports</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Once you have a connection, you're ready to start building your own\n" +
                        "                                                    custom reports using our drag and drop editor:\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Introduction_to_the_Report_Editor\">Report\n" +
                        "                                                        Editor Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#reportEditorIntro\">Intro\n" +
                        "                                                        to Building Reports</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#basecampCustomReports\">Building\n" +
                        "                                                        Custom Reports on Basecamp</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#highriseCustomReports\">Building\n" +
                        "                                                        Custom Reports on Highrise</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Filtering</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Filters restrict reports to show data based on criteria you choose, such as limiting results to one customer or project or a specific time range:\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Filters\">Filter\n" +
                        "                                                        Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#basicFilters\">Filter\n" +
                        "                                                        Screencast</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Have any questions or comments? Please don't hesitate to contact us\n" +
                        "                                                    at sales@easy-insight.com or support@easy-insight.com!\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-top:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td colspan=\"2\" valign=\"middle\"\n" +
                        "                                                style=\"border-collapse:collapse;background-color:#fafafa;border:0\">\n" +
                        "                                                <div style=\"color:#707070;font-family:Arial,serif;font-size:12px;line-height:125%;text-align:center\">\n" +
                        "                                                    &nbsp;<a\n" +
                        "                                                        href=\"https://twitter.com/#!/easyinsight\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">Follow on Twitter</a>&nbsp;|&nbsp;<a\n" +
                        "                                                        href=\"{0}\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">unsubscribe from this list</a>&nbsp;\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>\n" +
                        "            <br></td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";
            case ONE_WEEK:
                return "<html>\n" +
                        "<body>\n" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
                        "       style=\"margin:0;padding: 10px 0 0;background-color:#fafafa;min-height:100%;width:100%\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                   style=\"border:1px solid #dddddd;background-color:#ffffff\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-bottom:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"border-collapse:collapse;color:#202020;font-family:Arial,serif;font-size:34px;font-weight:bold;line-height:100%;padding:0;text-align:center;vertical-align:middle\">\n" +
                        "                                    <img alt=\"Easy Insight Logo\"\n" +
                        "                                         style=\"border:0;outline:none;text-decoration:none\" src=\"http://www.easy-insight.com/images/logo2.PNG\">\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse;background-color:#ffffff\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                                <div style=\"color:#505050;font-family:Arial,serif;font-size:14px;line-height:150%;text-align:left\">\n" +
                        "\n" +
                        "                                                    Dear {1},\n" +
                        "                                                    <br><br>\n" +
                        "                                                    We hope you are enjoying using Easy Insight to get the most of your data! This is the second of four emails that we will send you to help you get up and running with Easy Insight!\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Sign in here: <a href=\"https://www.easy-insight.com/app\">Easy\n" +
                        "                                                    Insight Login</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    Here are a few simple ways to integrate Easy Insight into your daily routine:\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Exporting to Excel, PDF, and More!</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    You can export any report from Easy Insight to Excel, PDF, or PNG for easy sharing with your team. You'll find the Export button on your report and dashboard views.\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Scheduled Report Email Delivery</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    You can export those reports automatically by email on your schedule. Send out a list of hot leads every morning, a project status report every week, or a budget report monthly.\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Email_Scheduling_and_Exporting\">Export and Scheduling Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <b>Mobile Display of Reports</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    You can view reports on your phone or tablet, making it easier than ever to keep a close eye on the pulse of your business. You can display any report in HTML by clicking on the \"View in HTML\" option.\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://www.easy-insight.com/app/html\">HTML Version of Application (mobile friendly)</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <b>Embedding Reports into External Sites</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Eliminate the need for users to log into multiple systems! You can embed any Easy Insight report into a variety of systems.\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Embedding_Reports_in_External_Systems\">Embedding Easy Insight Reports</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Have any questions or comments? Please don't hesitate to contact us\n" +
                        "                                                    at sales@easy-insight.com or support@easy-insight.com!\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-top:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td colspan=\"2\" valign=\"middle\"\n" +
                        "                                                style=\"border-collapse:collapse;background-color:#fafafa;border:0\">\n" +
                        "                                                <div style=\"color:#707070;font-family:Arial,serif;font-size:12px;line-height:125%;text-align:center\">\n" +
                        "                                                    &nbsp;<a\n" +
                        "                                                        href=\"https://twitter.com/#!/easyinsight\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">Follow on Twitter</a>&nbsp;|&nbsp;<a\n" +
                        "                                                        href=\"{0}\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">unsubscribe from this list</a>&nbsp;\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>\n" +
                        "            <br></td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";
            case TWO_WEEKS:
                return "<html>\n" +
                        "<body>\n" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
                        "       style=\"margin:0;padding: 10px 0 0;background-color:#fafafa;min-height:100%;width:100%\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                   style=\"border:1px solid #dddddd;background-color:#ffffff\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-bottom:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"border-collapse:collapse;color:#202020;font-family:Arial,serif;font-size:34px;font-weight:bold;line-height:100%;padding:0;text-align:center;vertical-align:middle\">\n" +
                        "                                    <img alt=\"Easy Insight Logo\"\n" +
                        "                                         style=\"border:0;outline:none;text-decoration:none\" src=\"http://www.easy-insight.com/images/logo2.PNG\">\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse;background-color:#ffffff\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                                <div style=\"color:#505050;font-family:Arial,serif;font-size:14px;line-height:150%;text-align:left\">\n" +
                        "\n" +
                        "                                                    Dear {1},\n" +
                        "                                                    <br><br>\n" +
                        "                                                    We hope you are enjoying using Easy Insight to get the most of your data! This is the third of four emails that we will send you to help you get up and running with Easy Insight!\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Sign in here: <a href=\"https://www.easy-insight.com/app\">Easy\n" +
                        "                                                    Insight Login</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Connect to More Systems</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Take a look at our Connections page and see other systems Easy Insight integrates with. For example, you can incorporate your Highrise, Zendesk, and Harvest data into a single executive dashboard:\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Data_Sources\">Combining Data Sources</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Custom Fields</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Need to add Probability or Close Date to your Highrise data or an Estimate to your Basecamp data? You can easily add custom fields to any data source:\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Custom_Fields\">Custom Field Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#basicFilters\">Custom Field Screencast</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <b>Scripting</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Easy Insight has a powerful set of scripting capabilities for building complex calculations. Take advantage of our wide variety of functions for more in depth analysis of your data:\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Scripting\">Scripting Documentation</a>\n" +
                        "                                                    <br><br>\n" +
                        "\n" +
                        "                                                    Have any questions or comments? Please don't hesitate to contact us\n" +
                        "                                                    at sales@easy-insight.com or support@easy-insight.com!\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-top:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td colspan=\"2\" valign=\"middle\"\n" +
                        "                                                style=\"border-collapse:collapse;background-color:#fafafa;border:0\">\n" +
                        "                                                <div style=\"color:#707070;font-family:Arial,serif;font-size:12px;line-height:125%;text-align:center\">\n" +
                        "                                                    &nbsp;<a\n" +
                        "                                                        href=\"https://twitter.com/#!/easyinsight\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">Follow on Twitter</a>&nbsp;|&nbsp;<a\n" +
                        "                                                        href=\"{0}\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">unsubscribe from this list</a>&nbsp;\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>\n" +
                        "            <br></td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";
            case THREE_WEEKS:
                return "<html>\n" +
                        "<body>\n" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
                        "       style=\"margin:0;padding: 10px 0 0;background-color:#fafafa;min-height:100%;width:100%\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                   style=\"border:1px solid #dddddd;background-color:#ffffff\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-bottom:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"border-collapse:collapse;color:#202020;font-family:Arial,serif;font-size:34px;font-weight:bold;line-height:100%;padding:0;text-align:center;vertical-align:middle\">\n" +
                        "                                    <img alt=\"Easy Insight Logo\"\n" +
                        "                                         style=\"border:0;outline:none;text-decoration:none\" src=\"http://www.easy-insight.com/images/logo2.PNG\">\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse;background-color:#ffffff\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                                <div style=\"color:#505050;font-family:Arial,serif;font-size:14px;line-height:150%;text-align:left\">\n" +
                        "\n" +
                        "                                                    Dear {1},\n" +
                        "                                                    <br><br>\n" +
                        "                                                    We hope you are enjoying using Easy Insight to get the most of your data! This is the fourth of four emails that we will send you to help you get up and running with Easy Insight!\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Sign in here: <a href=\"https://www.easy-insight.com/app\">Easy\n" +
                        "                                                    Insight Login</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Building Dashboards</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Once you have the reports you want built in Easy Insight, you can easily create your own personal interactive dashboards:\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Building_Dashboards\">Building Dashboards Documentation</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>Customizing the Look and Feel of your Reports</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Style Easy Insight to meet your corporate branding or use skins to make Easy Insight match your preferences:\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"https://wiki.easy-insight.com/wiki/index.php/Styling\">Styling Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/screencasts.html#stylingReports\">Customizing Look and Feel Screencast</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <b>API</b>\n" +
                        "                                                    <br>\n" +
                        "                                                    Easy Insight provides a set of REST APIs to simplify reporting on your data from any source within EI:\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <a href=\"http://www.easy-insight.com/api/restapi.html\">API Documentation</a>\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Have any questions or comments? Please don't hesitate to contact us\n" +
                        "                                                    at sales@easy-insight.com or support@easy-insight.com!\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-top:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td colspan=\"2\" valign=\"middle\"\n" +
                        "                                                style=\"border-collapse:collapse;background-color:#fafafa;border:0\">\n" +
                        "                                                <div style=\"color:#707070;font-family:Arial,serif;font-size:12px;line-height:125%;text-align:center\">\n" +
                        "                                                    &nbsp;<a\n" +
                        "                                                        href=\"https://twitter.com/#!/easyinsight\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">Follow on Twitter</a>&nbsp;|&nbsp;<a\n" +
                        "                                                        href=\"{0}\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">unsubscribe from this list</a>&nbsp;\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>\n" +
                        "            <br></td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
                        "</body>\n" +
                        "</html>";
            case FOUR_WEEKS:
                return "<html>\n" +
                        "<body>\n" +
                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"\n" +
                        "       style=\"margin:0;padding: 10px 0 0;background-color:#fafafa;min-height:100%;width:100%\">\n" +
                        "    <tbody>\n" +
                        "    <tr>\n" +
                        "        <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                   style=\"border:1px solid #dddddd;background-color:#ffffff\">\n" +
                        "                <tbody>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-bottom:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td style=\"border-collapse:collapse;color:#202020;font-family:Arial,serif;font-size:34px;font-weight:bold;line-height:100%;padding:0;text-align:center;vertical-align:middle\">\n" +
                        "                                    <img alt=\"Easy Insight Logo\"\n" +
                        "                                         style=\"border:0;outline:none;text-decoration:none\" src=\"http://www.easy-insight.com/images/logo2.PNG\">\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse;background-color:#ffffff\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                                <div style=\"color:#505050;font-family:Arial,serif;font-size:14px;line-height:150%;text-align:left\">\n" +
                        "\n" +
                        "                                                    Dear {1},\n" +
                        "                                                    <br><br>\n" +
                        "                                                    We hope you have enjoyed your experience using Easy Insight!\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Sign in here: <a href=\"https://www.easy-insight.com/app\">Easy\n" +
                        "                                                    Insight Login</a>\n" +
                        "                                                    <br><br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Your 30 day free trial is about to expire.  You can enter billing information from your Account Page by clicking Set Up Billing.  Once your trial expires you will be automatically directed to this page!  If you have questions about pricing, check out our <a href=\"http://www.easy-insight.com/pricing.html\">Pricing Page</a>.\n" +
                        "                                                    <br>\n" +
                        "                                                    <br>\n" +
                        "                                                    Have any questions or comments? Please don't hesitate to contact us\n" +
                        "                                                    at sales@easy-insight.com or support@easy-insight.com!\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                <tr>\n" +
                        "                    <td align=\"center\" valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\"\n" +
                        "                               style=\"background-color:#ffffff;border-top:0\">\n" +
                        "                            <tbody>\n" +
                        "                            <tr>\n" +
                        "                                <td valign=\"top\" style=\"border-collapse:collapse\">\n" +
                        "                                    <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\">\n" +
                        "                                        <tbody>\n" +
                        "                                        <tr>\n" +
                        "                                            <td colspan=\"2\" valign=\"middle\"\n" +
                        "                                                style=\"border-collapse:collapse;background-color:#fafafa;border:0\">\n" +
                        "                                                <div style=\"color:#707070;font-family:Arial,serif;font-size:12px;line-height:125%;text-align:center\">\n" +
                        "                                                    &nbsp;<a\n" +
                        "                                                        href=\"https://twitter.com/#!/easyinsight\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">Follow on Twitter</a>&nbsp;|&nbsp;<a\n" +
                        "                                                        href=\"{0}\"\n" +
                        "                                                        style=\"color:#336699;font-weight:normal;text-decoration:underline\"\n" +
                        "                                                        target=\"_blank\">unsubscribe from this list</a>&nbsp;\n" +
                        "                                                </div>\n" +
                        "                                            </td>\n" +
                        "                                        </tr>\n" +
                        "                                        </tbody>\n" +
                        "                                    </table>\n" +
                        "                                </td>\n" +
                        "                            </tr>\n" +
                        "                            </tbody>\n" +
                        "                        </table>\n" +
                        "                    </td>\n" +
                        "                </tr>\n" +
                        "                </tbody>\n" +
                        "            </table>\n" +
                        "            <br></td>\n" +
                        "    </tr>\n" +
                        "    </tbody>\n" +
                        "</table>\n" +
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

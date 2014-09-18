package com.easyinsight.admin;

import com.easyinsight.database.EIConnection;
import com.easyinsight.email.SendGridEmail;
import com.easyinsight.util.RandomTextGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 9/1/14
 * Time: 4:20 PM
 */
public class LeadNurtureShell {

    public static final EmailShell FIRST_EMAIL = createFirstEmail();
    public static final EmailShell SECOND_EMAIL = createSecondEmail();
    public static final EmailShell THIRD_EMAIL = createThirdEmail();
    public static final EmailShell FOURTH_EMAIL = createFourthEmail();
    public static final EmailShell FIFTH_EMAIL = createFifthEmail();

    public String generate(int email) throws SQLException {
        switch (email) {
            case 1:
                return generate(FIRST_EMAIL);
            case 2:
                return generate(SECOND_EMAIL);
            case 3:
                return generate(THIRD_EMAIL);
            case 4:
                return generate(FOURTH_EMAIL);
            case 5:
                return generate(FIFTH_EMAIL);
        }
        return "";
    }

    public String generate(EmailShell emailShell) throws SQLException {
        return this.string.replace("{0}", "").replace("{1}", emailShell.firstParagraph).replace("{2}", emailShell.image1)
                .replace("{3}", emailShell.header1).replace("{4}", emailShell.headerParagraph1).replace("{5}", emailShell.url1)
                .replace("{6}", emailShell.image2)
                .replace("{7}", emailShell.header2).replace("{8}", emailShell.headerParagraph2).replace("{9}", emailShell.url2).
                        replace("{10}", emailShell.image3)
                .replace("{11}", emailShell.header3).replace("{12}", emailShell.headerParagraph3).replace("{13}", emailShell.url3).replace("{14}", "").
                        replace("{15}", "").replace("{17}", "").replace("{18}", "");
    }

    public void generate(EIConnection conn, long userID, String emailAddress, EmailShell emailShell, String name) throws SQLException {

        PreparedStatement queryUnsubscribeStmt = conn.prepareStatement("SELECT unsubscribe_key from user_unsubscribe_key WHERE USER_ID = ?");
        PreparedStatement insertKeyStmt = conn.prepareStatement("INSERT INTO USER_UNSUBSCRIBE_KEY (USER_ID, UNSUBSCRIBE_KEY) VALUES (?, ?)");
        queryUnsubscribeStmt.setLong(1, userID);
        ResultSet unsubscribeRS = queryUnsubscribeStmt.executeQuery();
        String unsubscribeKey;
        if (unsubscribeRS.next()) {
            unsubscribeKey = unsubscribeRS.getString(1);
        } else {
            unsubscribeKey = RandomTextGenerator.generateText(25);
            insertKeyStmt.setLong(1, userID);
            insertKeyStmt.setString(2, unsubscribeKey);
            insertKeyStmt.execute();
        }
        queryUnsubscribeStmt.close();
        insertKeyStmt.close();

        String unsubscribeURL = "https://www.easy-insight.com/app/unsubscribe?user=" + unsubscribeKey;

        String replaced = viewAsWebpage.replace("{1}", "https://www.easy-insight.com/app/emailAsWebsite.jsp?email=1");

        String emailLine = this.emailLine.replace("{1}", unsubscribeURL);

        name = "&nbsp;" + name;

        String string = this.string.replace("{0}", name).replace("{1}", emailShell.firstParagraph).replace("{2}", emailShell.image1)
                .replace("{3}", emailShell.header1).replace("{4}", emailShell.headerParagraph1).replace("{5}", emailShell.url1)
                .replace("{6}", emailShell.image2)
                .replace("{7}", emailShell.header2).replace("{8}", emailShell.headerParagraph2).replace("{9}", emailShell.url2).
                        replace("{10}", emailShell.image3)
                .replace("{11}", emailShell.header3).replace("{12}", emailShell.headerParagraph3).replace("{13}", emailShell.url3).replace("{14}", emailLine).
                replace("{15}", replaced).replace("{17}", "bgcolor=\"#F2F2F2\"").replace("{18}", "background-color:#f2f2f2");

        new SendGridEmail().sendEmail(userID, emailAddress, "", emailShell.subject, string);

    }

    private static EmailShell createFirstEmail() {
        EmailShell email = new EmailShell();
        email.subject = "Welcome to Easy Insight!";
        email.name = "James";
        email.firstParagraph = "Welcome to Easy Insight! Try the tips below for a quick and successful start with Easy Insight. If you have any questions at all, please contact us at support@easy-insight.com or 1-(720)-316-8174.";
        email.image1 = "connect.png";
        email.image2 = "monitor.png";
        email.image3 = "hammer.png";
        email.url1 = "https://www.easy-insight.com/app/websiteDocs/Connecting_to_your_Data";
        email.url2 = "https://www.easy-insight.com/app/screencasts.jsp#navigating";
        email.url3 = "https://www.easy-insight.com/app/websiteDocs/Getting_Started_with_Reports";
        email.header1 = "Connecting Your Data";
        email.header2 = "Navigating the Easy Insight Interface";
        email.header3 = "Building Reports";
        email.headerParagraph1 = "Create connections from Easy Insight to your data in a couple of clicks to get started.";
        email.headerParagraph2 = "Learn how to navigate the Easy Insight user interface, from the data source list to reports and dashboards.";
        email.headerParagraph3 = "Watch a quick screencast around building a simple report from scratch as your introduction to the report editor.";
        return email;
    }


    private static EmailShell createSecondEmail() {
        EmailShell email = new EmailShell();
        email.subject = "Building reports in Easy Insight";
        email.name = "James";
        email.firstParagraph = "We hope you're off to a good start with Easy Insight! Try the guides below for getting the most out of building your own reports. If you have any questions at all, please contact us at support@easy-insight.com or 1-(720)-316-8174.";
        email.image1 = "image.png";
        email.image2 = "document.png";
        email.image3 = "pyramid.png";
        email.url1 = "https://www.easy-insight.com/app/websiteDocs/Report_Types";
        email.url2 = "https://www.easy-insight.com/app/websiteDocs/List";
        email.url3 = "https://www.easy-insight.com/app/websiteDocs/Getting_Started_with_Filters";
        email.header1 = "Gallery of report types";
        email.header2 = "Tips and tricks of manipulating list reports";
        email.header3 = "Filter Options";
        email.headerParagraph1 = "Explore a gallery of the report types available to you in Easy Insight to find the right visualization for your needs.";
        email.headerParagraph2 = "List reports provide the backbone to everything else--learn how to use them to their best.";
        email.headerParagraph3 = "Easy Insight gives you many different ways to slice and dice your data, as explained by this reference.";
        return email;
    }

    private static EmailShell createThirdEmail() {
        EmailShell email = new EmailShell();
        email.subject = "Sharing your Easy Insight reports and dashboards with the rest of your team";
        email.name = "James";
        email.firstParagraph = "Once you have good reports and dashboards, you need to be able to share them with the rest of your team! Look at the guides below to make sure your team is able to use the work you've done. If you have any questions at all, please contact us at support@easy-insight.com or 1-(720)-316-8174.";
        email.image1 = "briefcase.png";
        email.image2 = "key.png";
        email.image3 = "monitor.png";
        email.url1 = "https://www.easy-insight.com/app/websiteDocs/Email_Scheduling_and_Exporting";
        email.url2 = "https://www.easy-insight.com/app/websiteDocs/Collaboration";
        email.url3 = "https://www.easy-insight.com/app/websiteDocs/Embedding_Reports_in_External_Systems";
        email.header1 = "Scheduling emails";
        email.header2 = "Managing permissions";
        email.header3 = "Embedding into external";
        email.headerParagraph1 = "Set up recurring emails of reports and dashboards to your team on the schedule you want and in the format you want.";
        email.headerParagraph2 = "Make it easy for your coworkers to view the reports and dashboards you've labored to build by setting the right permissions for them to easily see your work.";
        email.headerParagraph3 = "Learn how to embed your reports and dashboards into external systems to make things even more seamless.";
        return email;
    }

    private static EmailShell createFourthEmail() {
        EmailShell email = new EmailShell();
        email.subject = "Customizing the look and feel and locale of your Easy Insight reports";
        email.name = "James";
        email.firstParagraph = "Make sure you're able to customize the look and feel of your reports to match with your company's standards and make sure you have the right formats for your location by taking a look at the guides below. If you have any questions at all, please contact us at support@easy-insight.com or 1-(720)-316-8174.";
        email.image1 = "brushes.png";
        email.image2 = "technical_wrench.png";
        email.image3 = "world.png";
        email.url1 = "https://www.easy-insight.com/app/websiteDocs/Styling";
        email.url2 = "https://www.easy-insight.com/app/websiteDocs/Additional_Configuration";
        email.url3 = "https://www.easy-insight.com/app/websiteDocs/Account_Settings";
        email.header1 = "Color schemes";
        email.header2 = "Additional report properties";
        email.header3 = "Account settings for formatting";
        email.headerParagraph1 = "Set up color schemes to automatically apply your company's branding and colors to your reports and dashboards.";
        email.headerParagraph2 = "Learn how to apply specific formatting options to the various report types to enable more advanced settings and get exactly what you need.";
        email.headerParagraph3 = "Customize the formats used for currencies, numbers, and date formats to get the right formatting for your business and location.";
        return email;
    }

    private static EmailShell createFifthEmail() {
        EmailShell email = new EmailShell();
        email.subject = "Extending your Easy Insight reporting with more systems";
        email.name = "James";
        email.firstParagraph = "Even if you're just using Easy Insight for simple reporting on one SaaS system, you have full business intelligence functionality available to you to extend your reporting across multiple systems. Take a look at the guides below to see some of the more advanced functionality you can use. If you have any questions at all, please contact us at support@easy-insight.com or 1-(720)-316-8174.";
        email.image1 = "connect.png";
        email.image2 = "companies.png";
        email.image3 = "boxes.png";
        email.url1 = "https://www.easy-insight.com/app/websiteDocs/Combining_Data_Sources";
        email.url2 = "https://www.easy-insight.com/app/websiteDocs/Configuring_your_Data_Source";
        email.url3 = "https://www.easy-insight.com/app/websiteDocs/Report_Data_Sources";
        email.header1 = "Combining data sources";
        email.header2 = "Configuring your data sources";
        email.header3 = "Building data sources off of reports";
        email.headerParagraph1 = "Learn how to combine and join different data sources into a single source in order to extend reporting across your entire business.";
        email.headerParagraph2 = "Data source configuration allows you to automatically apply drilldowns and create reusable calculations for use across all reports.";
        email.headerParagraph3 = "You can even create additional data sources off of your reports to simplify report creation for additional users.";
        return email;
    }

    public static class EmailShell {
        private String name;
        private String firstParagraph;
        private String image1;
        private String image2;
        private String image3;
        private String url1;
        private String url2;
        private String url3;
        private String header1;
        private String header2;
        private String header3;
        private String headerParagraph1;
        private String headerParagraph2;
        private String headerParagraph3;
        private String subject;
    }

    private String viewAsWebpage = "<tr>\n" +
            "        <td {17} style=\"{18}\">\n" +
            "            <div align=\"center\">\n" +
            "                <table cellpadding=\"0\" width=\"600\" cellspacing=\"0\" border=\"0\">\n" +
            "                    <tbody><tr>\n" +
            "                        <td {17} style=\"{18}\">\n" +
            "                            <div style=\"padding-top:15px;font-family:Geneva,Verdana,Arial,Helvetica,sans-serif;text-align:right;font-size:9px;line-height:1.34em;color:#999999\">\n" +
            "                                Not rendering correctly? View this email as a web page <a href=\"{1}\" style=\"color:#999999;text-decoration:underline;white-space:nowrap\" target=\"_blank\">here</a>.\n" +
            "                            </div>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                    </tbody></table>\n" +
            "            </div>\n" +
            "        </td>\n" +
            "    </tr>\n";

    private String emailLine = "                                You are receiving this email because your organization has a Easy Insight account to which you have access. If you prefer not to receive Easy Insight emails like this, <a href=\"{1}\" target=\"_blank\">click here</a>.\n";

    private String string = "<html>\n" +
            "<body>\n" +
            "\n" +
            "<div {17} style=\"margin:0;padding:0;{18}\" marginheight=\"0\" marginwidth=\"0\">\n" +
            "\n" +
            "\n" +
            "<p style=\"margin-bottom:1em;display:none!important\"></p>\n" +
            "\n" +
            "\n" +
            "<table {17} style=\"{18}\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
            "\n" +
            "\n" +
            "    <tbody>{15}"+
            "\n" +
            "\n" +
            "    <tr>\n" +
            "        <td {17} style=\"padding:10px 20px;{18}\">\n" +
            "            <div align=\"center\">\n" +
            "                <table cellpadding=\"0\" width=\"600\" cellspacing=\"0\" border=\"0\">\n" +
            "                    <tbody><tr>\n" +
            "                        <td width=\"600\" bgcolor=\"#ffffff\" style=\"padding:30px;background-color:#ffffff;border:1px solid #cccccc;border-bottom:1px solid #acacac;border-radius:4px\">\n" +
            "                            <div align=\"center\">\n" +
            "                                <table cellpadding=\"0\" width=\"600\" cellspacing=\"0\" border=\"0\">\n" +
            "                                    <tbody><tr>\n" +
            "                                        <td style=\"text-align:right;vertical-align:top\">\n" +
            "                                            <a style=\"text-decoration:none;vertical-align:top;font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#373737\" href=\"https://www.easy-insight.com/app/login.jsp\" target=\"_blank\">Sign In |</a>\n" +
            "                                            <a style=\"text-decoration:none;vertical-align:top;font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#373737\" href=\"https://www.easy-insight.com/contactus.html\" target=\"_blank\">Support |</a>\n" +
            "                                            <a style=\"text-decoration:none;vertical-align:top;font-family:Arial,Helvetica,sans-serif;font-size:12px;color:#373737\" href=\"https://www.easy-insight.com/app/whatsnewweb\" target=\"_blank\">What's New</a>\n" +
            "                                        </td>\n" +
            "                                    </tr>\n" +
            "\n" +
            "\n" +
            "                                    <tr>\n" +
            "                                        <td valign=\"top\" align=\"left\" style=\"padding:0;background-color:#ffffff;padding-bottom:0px\">\n" +
            "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
            "                                                <tbody><tr>\n" +
            "                                                    <td>\n" +
            "                                                        <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"><a href=\"\" style=\"border-width:0px;border:0px\" target=\"_blank\"><img src=\"https://www.easy-insight.com/images/logo2.png\" style=\"border-width:0px;border:0px\" alt=\"\" title=\"\"></a></div>\n" +
            "                                                    </td>\n" +
            "                                                </tr>\n" +
            "                                                </tbody></table>\n" +
            "                                        </td>\n" +
            "                                    </tr>\n" +
            "\n" +
            "\n" +
            "                                    <tr>\n" +
            "                                        <td align=\"center\" style=\"padding:5px\">\n" +
            "                                            <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
            "                                                <tbody><tr>\n" +
            "                                                    <td>\n" +
            "                                                        <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"></div>\n" +
            "                                                    </td>\n" +
            "                                                </tr>\n" +
            "                                                </tbody></table>\n" +
            "                                        </td>\n" +
            "                                    </tr>\n" +
            "\n" +
            "                                    <tr>\n" +
            "                                        <td style=\"padding:0;background-color:#ffffff\">\n" +
            "                                            <div align=\"center\">\n" +
            "                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5em;color:#373737\">\n" +
            "                                                    <tbody><tr>\n" +
            "                                                        <td>\n" +
            "                                                            <div align=\"center\">\n" +
            "                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
            "                                                                    <tbody><tr>\n" +
            "                                                                        <td>\n" +
            "                                                                            <div align=\"center\">\n" +
            "                                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"600\">\n" +
            "                                                                                    <tbody><tr>\n" +
            "                                                                                        <td valign=\"top\" style=\"margin:10px 0 10px 0;text-align:left;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5em;color:#373737\">\n" +
            "                                                                                            <div>\n" +
            "                                                                                                <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"><p style=\"margin-bottom:1em\">Hi{0},</p>\n" +
            "                                                                                                    <p style=\"margin-bottom:1em\">Happy Reporting!</p>\n" +
            "                                                                                                    <p style=\"margin-bottom:1em\">The Easy Insight Team</p>\n" +
            "                                                                                                </div>\n" +
            "                                                                                            </div>\n" +
            "                                                                                        </td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    </tbody></table>\n" +
            "                                                                            </div>\n" +
            "                                                                        </td>\n" +
            "                                                                    </tr>\n" +
            "                                                                    <tr>\n" +
            "                                                                        <td>\n" +
            "                                                                            <div align=\"center\">\n" +
            "                                                                                <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"600\">\n" +
            "                                                                                    <tbody><tr>\n" +
            "                                                                                        <td style=\"margin-top:10px;width:100%;text-align:left;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5em;color:#373737;padding-top:10px;padding-bottom:10px;background-color:#f9f9f9;border:1px solid #eee;border-radius:4px\">\n" +
            "                                                                                            <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"><table style=\"width:600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\">\n" +
            "                                                                                                <tbody>\n" +
            "                                                                                                <tr>\n" +
            "                                                                                                    <td><a href=\"{5}\" target=\"_blank\"><img src=\"https://j8staging.easy-insight.com/images/maicons/{2}\" alt=\"\" order=\"0\"></a></td>\n" +
            "                                                                                                    <td style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px\">\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\"><span style=\"font-size:14px\"><a href=\"{5}\" target=\"_blank\"><strong>{3}</strong></a></span></p>\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\">{4}</p>\n" +
            "                                                                                                    </td>\n" +
            "                                                                                                </tr>\n" +
            "                                                                                                </tbody>\n" +
            "                                                                                            </table></div>\n" +
            "                                                                                        </td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    <tr>\n" +
            "                                                                                        <td style=\"height:20px\">&nbsp;</td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    <tr>\n" +
            "                                                                                        <td valign=\"top\" style=\"width:100%;text-align:left;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5em;color:#373737;padding-top:10px;padding-bottom:10px;background-color:#f9f9f9;border:1px solid #eee;border-radius:4px\">\n" +
            "                                                                                            <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"><table style=\"width:600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\">\n" +
            "                                                                                                <tbody>\n" +
            "                                                                                                <tr>\n" +
            "                                                                                                    <td><a href=\"{9}\" target=\"_blank\"><img src=\"https://j8staging.easy-insight.com/images/maicons/{6}\" alt=\"\"></a></td>\n" +
            "                                                                                                    <td style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px\">\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\"><span style=\"font-size:14px\"><a href=\"{9}\" target=\"_blank\"><strong>{7}</strong></a></span></p>\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\">{8}</p>\n" +
            "                                                                                                    </td>\n" +
            "                                                                                                </tr>\n" +
            "                                                                                                </tbody>\n" +
            "                                                                                            </table></div>\n" +
            "                                                                                        </td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    <tr>\n" +
            "                                                                                        <td style=\"height:20px\">&nbsp;</td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    <tr>\n" +
            "                                                                                        <td valign=\"top\" style=\"width:100%;text-align:left;font-family:Arial,Helvetica,sans-serif;font-size:12px;line-height:1.5em;color:#373737;padding-top:10px;padding-bottom:10px;background-color:#f9f9f9;border:1px solid #eee;border-radius:4px\">\n" +
            "                                                                                            <div style=\"color:inherit;font-size:inherit;line-height:inherit;margin:inherit;padding:inherit\"><table style=\"width:600px\" border=\"0\" cellspacing=\"0\" cellpadding=\"10\">\n" +
            "                                                                                                <tbody>\n" +
            "                                                                                                <tr>\n" +
            "                                                                                                    <td><a href=\"{13}\" target=\"_blank\"><img src=\"https://j8staging.easy-insight.com/images/maicons/{10}\" alt=\"\" border=\"0\"></a></td>\n" +
            "                                                                                                    <td style=\"font-family:Arial,Helvetica,sans-serif;font-size:12px\">\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\"><span style=\"font-size:14px\"><a href=\"{13}\" target=\"_blank\"><strong>{11}</strong></a></span></p>\n" +
            "                                                                                                        <p style=\"margin-bottom:1em\">{12}</p>\n" +
            "                                                                                                    </td>\n" +
            "                                                                                                </tr>\n" +
            "                                                                                                </tbody>\n" +
            "                                                                                            </table></div>\n" +
            "                                                                                        </td>\n" +
            "                                                                                    </tr>\n" +
            "                                                                                    </tbody></table>\n" +
            "                                                                            </div>\n" +
            "                                                                        </td>\n" +
            "                                                                    </tr>\n" +
            "                                                                    </tbody></table>\n" +
            "                                                            </div>\n" +
            "                                                        </td>\n" +
            "                                                    </tr>\n" +
            "                                                    </tbody></table>\n" +
            "                                            </div>\n" +
            "                                        </td>\n" +
            "                                    </tr>\n" +
            "\n" +
            "                                    </tbody></table>\n" +
            "                            </div>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                    </tbody></table>\n" +
            "            </div>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "\n" +
            "    <tr>\n" +
            "        <td {17} style=\"{18};padding:13px 30px\">\n" +
            "            <div align=\"center\">\n" +
            "                <table cellpadding=\"0\" width=\"600\" cellspacing=\"0\" border=\"0\">\n" +
            "                    <tbody><tr>\n" +
            "                        <td align=\"center\" {17} style=\"{18}\">\n" +
            "                            <p style=\"margin-bottom:1em;font-family:Geneva,Verdana,Arial,Helvetica,sans-serif;text-align:center;font-size:9px;line-height:1.34em;color:#999999;display:block\">\n" +
            "                                2014 Easy Insight LLC. All Rights Reserved.&nbsp;&nbsp;1401 Wewatta St Unit 606&nbsp;&nbsp;Denver&nbsp;CO&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\n" +
            "                                <br><br>\n" +
            "{14}"+
            "                                <br><br>\n" +
            "                                Copyright 2014 Easy Insight Inc. All rights reserved. Registration and Internet access required. Terms, conditions, pricing, features and service options subject to change.\n" +
            "                            </p>\n" +
            "                        </td>\n" +
            "                    </tr>\n" +
            "                    </tbody></table>\n" +
            "            </div>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "\n" +
            "    </tbody></table>\n" +
            "\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>";
}

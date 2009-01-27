package com.easyinsight.email;

import com.easyinsight.logging.LogClass;

import java.text.MessageFormat;

/**
 * User: James Boe
 * Date: Aug 17, 2008
 * Time: 10:00:56 PM
 */
public class NewAccountInvitation {
    private static String invitationText =
            "I''ve shared a data source with you called {0}\r\n"+
            "{1}\r\n\r\n"+
            "This data is not an attachment. It''s stored online as part of the Easy Insight database.\r\n"+
            "Click the link above to open up the data.";

    private static String listText =
            "I''ve shared a list with you called {0}\r\n"+
            "{1}\r\n\r\n"+
            "This list is not an attachment. It''s stored online as part of the Easy Insight database.\r\n"+
            "Click the link above to open up the list.";

    private static String chartText =
            "I''ve shared a chart with you called {0}\r\n"+
            "{1}\r\n\r\n"+
            "This chart is not an attachment. It''s stored online as part of the Easy Insight database.\r\n"+
            "Click the link above to open up the chart.";

    private static String crosstabText =
            "I''ve shared a crosstab with you called {0}\r\n"+
            "{1}\r\n\r\n"+
            "This crosstab is not an attachment. It''s stored online as part of the Easy Insight database.\r\n"+
            "Click the link above to open up the crosstab.";

    private static String mapText =
            "I''ve shared a map with you called {0}\r\n"+
            "{1}\r\n\r\n"+
            "This map is not an attachment. It''s stored online as part of the Easy Insight database.\r\n"+
            "Click the link above to open up the map.";

    private static String groupInviteText =
            "I''ve invited you to join a group named {0} on Easy Insight. Click the link below to join the group.";

    public void sendFeedInvitation(String to, String feedName, String feedID, String from) {
        String feedURL = "http://easy-insight.com/DMS/#feedID=" + feedID;
        String body = MessageFormat.format(invitationText, feedName, feedURL);
        String subject = feedName + " (Easy Insight)";
        try {
            new GoogleTest().sendSSLMessage(to, subject, body, from);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendListInvitation(String to, String analysisName, String analysisID, String from) {
        sendAnalysisInvitation(to, analysisName, analysisID, listText, from);
    }

    public void sendChartInvitation(String to, String analysisName, String analysisID, String from) {
        sendAnalysisInvitation(to, analysisName, analysisID, chartText, from);
    }

    public void sendCrosstabInvitation(String to, String analysisName, String analysisID, String from) {
        sendAnalysisInvitation(to, analysisName, analysisID, crosstabText, from);
    }

    public void sendMapInvitation(String to, String analysisName, String analysisID, String from) {
        sendAnalysisInvitation(to, analysisName, analysisID, mapText, from);
    }

    public void sendAnalysisInvitation(String to, String analysisName, String analysisID, String messageText, String from) {
        // so we generate an invite ID...
        // 
        String feedURL = "http://easy-insight.com/DMS/#analysisID=" + analysisID + "?inviteID=";
        String body = MessageFormat.format(messageText, analysisName, feedURL);
        String subject = analysisName + " (Easy Insight)";
        try {
            new GoogleTest().sendSSLMessage(to, subject, body, from);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void sendGroupInvitation(String to, String groupID) {
        
    }
}

package com.easyinsight.datafeeds.batchbook;

import com.easyinsight.analysis.ReportException;
import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/19/12
 * Time: 10:00 AM
 */
public class BatchbookCommunicationsCache extends BatchbookBaseSource {
    private List<Communication> communications;

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    public void populate(BatchbookCompositeSource batchbookCompositeSource) {
        communications = new ArrayList<Communication>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        HttpClient httpClient = getHttpClient(batchbookCompositeSource.getBbApiKey(), "");
        try {
            int count;
            int masterCount = 0;
            int zeroCount = 0;
            do {
                count = 0;
                Document deals;
                System.out.println("Master Count = " + masterCount);
                if (masterCount == 0) {
                    deals = runRestRequest("/service/communications.xml?limit=25&include_participants=true", httpClient, new Builder(), batchbookCompositeSource.getUrl(), batchbookCompositeSource);
                } else {
                    System.out.println("/service/communications.xml?offset="+masterCount+"&limit=25&include_participants=true");
                    deals = runRestRequest("/service/communications.xml?offset="+masterCount+"&limit=25&include_participants=true", httpClient, new Builder(), batchbookCompositeSource.getUrl(), batchbookCompositeSource);
                }
                Nodes dealNodes = deals.query("/communications/communication");
                System.out.println("size = " + dealNodes.size());
                for (int i = 0; i < dealNodes.size(); i++) {
                    Node dealNode = dealNodes.get(i);

                    String id = queryField(dealNode, "id/text()");
                    String subject = queryField(dealNode, "subject/text()");
                    String body = queryField(dealNode, "body/text()");
                    String type = queryField(dealNode, "ctype/text()");

                    Date date = dateFormat.parse(queryField(dealNode, "date/text()"));

                    Nodes tagNodes = dealNode.query("tags/tag/name/text()");
                    StringBuilder tagBuilder = new StringBuilder();
                    for (int j = 0; j < tagNodes.size(); j++) {
                        String tag = tagNodes.get(j).getValue();
                        tagBuilder.append(tag).append(",");
                    }
                    if (tagNodes.size() > 0) {
                        tagBuilder.deleteCharAt(tagBuilder.length() - 1);
                    }
                    String tags = tagBuilder.toString();
                    /*Document parties = runRestRequest("/service/communications/" + id + "/participants.xml", httpClient, new Builder(), batchbookCompositeSource.getUrl(),
                    batchbookCompositeSource);*/
                    Nodes partyNodes = dealNode.query("participants/participant");
                    List<CommunicationContact> communicationContacts = new ArrayList<CommunicationContact>();
                    for (int j = 0; j < partyNodes.size(); j++) {
                        Node partyNode = partyNodes.get(j);
                        String partyID = queryField(partyNode, "contact_id/text()");
                        String role = queryField(partyNode, "role/text()");
                        communicationContacts.add(new CommunicationContact(partyID, role));
                    }
                    Communication communication = new Communication(communicationContacts, subject, tags, type, date, id, body);
                    count++;
                    masterCount++;
                    communications.add(communication);
                }
            } while (count == 25);

            System.out.println("cache size = " + communications.size());
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Communication> getCommunications() {
        return communications;
    }
}

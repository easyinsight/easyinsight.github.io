package com.easyinsight.datafeeds.highrise;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 3/24/11
 * Time: 10:13 AM
 */
public class HighriseRecordingsCache extends HighRiseBaseSource {

    private List<Recording> caseNotes = new ArrayList<Recording>();
    private List<Recording> contactNotes = new ArrayList<Recording>();
    private List<Recording> dealNotes = new ArrayList<Recording>();
    private List<Recording> companyNotes = new ArrayList<Recording>();
    private List<HighriseEmail> emails = new ArrayList<HighriseEmail>();

    public List<HighriseEmail> getEmails() {
        return emails;
    }

    public List<Recording> getCaseNotes() {
        return caseNotes;
    }

    public List<Recording> getContactNotes() {
        return contactNotes;
    }

    public List<Recording> getDealNotes() {
        return dealNotes;
    }

    public List<Recording> getCompanyNotes() {
        return companyNotes;
    }

    public void populateCaches(HttpClient client, String url, FeedDefinition parentDefinition, Date lastTime, HighriseCache highriseCache, Set<String> companyIDs) throws HighRiseLoginException, ParsingException, ParseException {

        Builder builder = new Builder();
        //public static final String ALTDATEFORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);
        String dateString = dateFormat.format(lastTime);

        int page = 0;
        int count;
        do {
            String target;
            if (page == 0) {
                target = "/recordings.xml?since=" + dateString;
            } else {
                target = "/recordings.xml?since=" + dateString + "&n=" + (25 * page);
            }
            Document recordingsDoc = runRestRequest(target, client, builder, url, true, false, parentDefinition);
            Nodes recordingNodes = recordingsDoc.query("/recordings/recording");
            count = recordingNodes.size();
            for (int i = 0; i < recordingNodes.size(); i++) {
                Node recordingNode = recordingNodes.get(i);
                // type
                // subject-type
                String type = ((Element) recordingNode).getAttribute("type").getValue();
                if ("Note".equals(type)) {
                    String authorID = queryField(recordingNode, "author-id/text()");
                    String author = highriseCache.getUserName(authorID);
                    String body = queryField(recordingNode, "body/text()");
                    String createdAtString = queryField(recordingNode, "created-at/text()");
                    String updatedAtString = queryField(recordingNode, "updated-at/text()");
                    Date createdAt = deadlineFormat.parse(createdAtString);
                    Date updatedAt = deadlineFormat.parse(updatedAtString);
                    String id = queryField(recordingNode, "id/text()");
                    String subjectID = queryField(recordingNode, "subject-id/text()");
                    String subjectType = queryField(recordingNode, "subject-type/text()");
                    Recording recording = new Recording(body, createdAt, updatedAt, subjectID, id, author);
                    if ("Party".equals(subjectType)) {
                        if (companyIDs.contains(subjectID)) {
                            companyNotes.add(recording);
                        } else {
                            contactNotes.add(recording);
                        }
                    } else if ("Deal".equals(subjectType)) {
                        dealNotes.add(recording);
                    } else if ("Kase".equals(subjectType)) {
                        caseNotes.add(recording);
                    }
                } else if ("Email".equals(type)) {
                    String authorID = queryField(recordingNode, "author-id/text()");
                    String author = highriseCache.getUserName(authorID);
                    String createdAtString = queryField(recordingNode, "created-at/text()");
                    Date createdAt = deadlineFormat.parse(createdAtString);
                    String id = queryField(recordingNode, "id/text()");
                    String subjectID = queryField(recordingNode, "collection-id/text()");
                    String subjectType = queryField(recordingNode, "collection-type/text()");
                    HighriseEmail highriseEmail = new HighriseEmail(author, createdAt, id);
                    if ("Party".equals(subjectType)) {
                        if (companyIDs.contains(subjectID)) {
                            highriseEmail.setCompanyID(subjectID);
                        } else {
                            highriseEmail.setContactID(subjectID);
                        }
                    } else if ("Deal".equals(subjectType)) {
                        highriseEmail.setDealID(subjectID);
                    } else if ("Kase".equals(subjectType)) {
                        highriseEmail.setCaseID(subjectID);
                    }
                    emails.add(highriseEmail);
                }
            }
            page++;
        } while (count == 25);
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

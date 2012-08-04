package com.easyinsight.datafeeds.highrise;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 8/2/12
 * Time: 10:15 AM
 */
public class HighriseTaskCache extends HighRiseBaseSource {

    private List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    public void populate(HighRiseCompositeSource parentDefinition, EIConnection conn) throws HighRiseLoginException, ParsingException, ParseException {
        String url = parentDefinition.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        Map<String, String> peopleCache = new HashMap<String, String>();
        Map<String, String> categoryCache = new HashMap<String, String>();
        List<TaskInfo> tasks = new ArrayList<TaskInfo>();
        tasks.addAll(getTasks(token.getTokenValue(), "upcoming", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
        tasks.addAll(getTasks(token.getTokenValue(), "assigned", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
        tasks.addAll(getTasks(token.getTokenValue(), "completed", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));

        for (HighriseAdditionalToken additionalToken : parentDefinition.getAdditionalTokens()) {
            try {
                tasks.addAll(getTasks(additionalToken.getToken(), "upcoming", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
                tasks.addAll(getTasks(additionalToken.getToken(), "assigned", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
                tasks.addAll(getTasks(additionalToken.getToken(), "completed", url, parentDefinition, peopleCache, categoryCache, deadlineFormat));
            } catch (Exception e) {
                System.out.println("Failed to load tasks for token " + additionalToken.getToken());
            }
        }
        this.taskInfos = tasks;
    }

    public List<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    private List<TaskInfo> getTasks(String apiToken, String path, String url, FeedDefinition parentDefinition, Map<String, String> peopleCache,
                                    Map<String, String> categoryCache, DateFormat deadlineFormat) throws HighRiseLoginException, ParsingException, ParseException {
        List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
        HttpClient client = getHttpClient(apiToken, "");
        Builder builder = new Builder();
        Document companies = runRestRequest("/tasks/"+path+".xml", client, builder, url, true, false, parentDefinition);
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        HighriseCompanyCache companyCache = highRiseCompositeSource.getOrCreateCompanyCache(client, null);
        Nodes companyNodes = companies.query("/tasks/task");
        for (int i = 0; i < companyNodes.size(); i++) {
            Node taskNode = companyNodes.get(i);


            String id = queryField(taskNode, "id/text()");

            String categoryID = queryField(taskNode, "category-id/text()");
            String category = retrieveCategoryInfo(client, builder, categoryCache, categoryID, url, parentDefinition);
            String authorID = queryField(taskNode, "author-id/text()");
            String author = retrieveUserInfo(client, builder, peopleCache, authorID, url, parentDefinition);
            String ownerID = queryField(taskNode, "owner-id/text()");
            String owner = retrieveUserInfo(client, builder, peopleCache, ownerID, url, parentDefinition);
            String createdAtString = queryField(taskNode, "created-at/text()");
            Date createdAt = deadlineFormat.parse(createdAtString);
            String doneAtString = queryField(taskNode, "done-at/text()");
            Date doneAt = null;
            if (doneAtString != null) {
                doneAt = deadlineFormat.parse(doneAtString);

            }
            String dueAtString = queryField(taskNode, "due-at/text()");
            Date dueAt = null;
            if (dueAtString != null) {
                dueAt = deadlineFormat.parse(dueAtString);
                if (dueAt == null) {
                    Calendar cal = Calendar.getInstance();
                    String frame = queryField(taskNode, "frame/text()");
                    if ("next_week".equals(frame)) {
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        cal.set(Calendar.DAY_OF_WEEK, 0);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        dueAt = cal.getTime();
                    }
                }
            }

            String body = queryField(taskNode, "body/text()");

            String subjectType = queryField(taskNode, "subject-type/text()");
            String subjectID = queryField(taskNode, "subject-id/text()");
            String caseID = null;
            String companyID = null;
            String contactID = null;
            String dealID = null;
            if ("Kase".equals(subjectType)) {
                caseID = subjectID;
            } else if ("Party".equals(subjectType)) {
                if (companyCache.getCompanyIDs().contains(subjectID)) {
                    companyID = subjectID;
                } else {
                    contactID = subjectID;
                }
            } else if ("Deal".equals(subjectType)) {
                dealID = subjectID;
            }

            taskInfos.add(new TaskInfo(id, category, body, owner, author, caseID, companyID, dealID, createdAt, dueAt, doneAt, contactID));
        }
        return taskInfos;
    }

    private String retrieveCategoryInfo(HttpClient client, Builder builder, Map<String, String> categoryCache, String categoryID, String url, FeedDefinition parentDefinition) throws HighRiseLoginException, ParsingException {
        try {
            String contactName = null;
            if(categoryID != null) {
                contactName = categoryCache.get(categoryID);
                if(contactName == null) {
                    Document contactInfo = runRestRequest("/task_categories/" + categoryID + ".xml", client, builder, url, false, false, parentDefinition);
                    Nodes dealNodes = contactInfo.query("/task-category");
                    if (dealNodes.size() > 0) {
                        Node deal = dealNodes.get(0);
                        contactName = queryField(deal, "name/text()");
                    }

                    categoryCache.put(categoryID, contactName);
                }

            }
            return contactName;
        } catch (HighRiseLoginException e) {
            categoryCache.put(categoryID, "");
            return "";
        }
    }
}

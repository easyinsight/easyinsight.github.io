package com.easyinsight.datafeeds.highrise;

import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Jun 7, 2010
 * Time: 9:09:36 AM
 */
public class HighriseCache extends HighRiseBaseSource {
    Map<String, List<String>> contactTagMap = new HashMap<String, List<String>>();
    Map<String, List<String>> companyTagMap = new HashMap<String, List<String>>();
    Map<String, String> userCache = new HashMap<String, String>();

    public void populateCaches(HttpClient client, String url) throws HighRiseLoginException, ParsingException {
        Builder builder = new Builder();
        Document tagDoc = runRestRequest("/tags.xml", client, builder, url, true, false);
        Nodes tagNodes = tagDoc.query("/tags/tag");
        for (int i = 0; i < tagNodes.size(); i++) {
            loadingProgress(i, tagNodes.size(), "Synchronizing with tags...", true);
            Node tagNode = tagNodes.get(i);
            String tag = queryField(tagNode, "name/text()");            
            String id = queryField(tagNode, "id/text()");
            Document ppl = runRestRequest("/tags/" + id + ".xml", client, builder, url, false, false);
            Nodes pplNodes = ppl.query("/records/record");
            for (int j = 0; j < pplNodes.size(); j++) {
                Node person = pplNodes.get(j);
                boolean hasCompanyID = person.query("last-name/text()").size() > 0;
                String personID = queryField(person, "id/text()");
                if (hasCompanyID) {
                    List<String> peopleTags = contactTagMap.get(personID);
                    if (peopleTags == null) {
                        peopleTags = new ArrayList<String>();
                        contactTagMap.put(personID, peopleTags);
                    }
                    peopleTags.add(tag);
                } else {
                    List<String> companyTags = companyTagMap.get(personID);
                    if (companyTags == null) {
                        companyTags = new ArrayList<String>();
                        companyTagMap.put(personID, companyTags);
                    }
                    companyTags.add(tag);
                }
            }
            Nodes companyNodes = ppl.query("/companies/company");
            for (int j = 0; j < companyNodes.size(); j++) {
                Node person = companyNodes.get(j);
                String personID = queryField(person, "id/text()");
                List<String> tags = companyTagMap.get(personID);
                if (tags == null) {
                    tags = new ArrayList<String>();
                    companyTagMap.put(personID, tags);
                }
                tags.add(tag);
            }
            Nodes peopleNodes = ppl.query("/people/person");
            for (int j = 0; j < peopleNodes.size(); j++) {
                Node person = peopleNodes.get(j);
                String personID = queryField(person, "id/text()");
                List<String> tags = contactTagMap.get(personID);
                if (tags == null) {
                    tags = new ArrayList<String>();
                    contactTagMap.put(personID, tags);
                }
                tags.add(tag);
            }
        }
        Document userDoc = runRestRequest("/users.xml", client, builder, url, true, false);
        Nodes usersDoc = userDoc.query("/users/user");
        for (int i = 0; i < usersDoc.size(); i++) {
            Node userNode = usersDoc.get(i);
            String userName = queryField(userNode, "name/text()");
            String userID = queryField(userNode, "id/text()");
            userCache.put(userID, userName);
        }
    }

    public String getUserName(String userID) {
        String name = userCache.get(userID);
        if (name == null) {
            name = "";
        }
        return name;
    }

    public Map<String, List<String>> getContactTagMap() {
        return contactTagMap;
    }

    public Map<String, List<String>> getCompanyTagMap() {
        return companyTagMap;
    }

    public Map<String, String> getUserCache() {
        return userCache;
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

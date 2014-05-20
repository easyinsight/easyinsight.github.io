package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.logging.LogClass;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 1/4/12
 * Time: 3:24 PM
 */
public class ContactListCache extends ConstantContactBaseSource {
    private List<ContactList> contactLists;
    private Set<Contact> contacts;

    public List<ContactList> getOrCreateContactLists(ConstantContactCompositeSource ccSource) throws Exception, OAuthExpectationFailedException, ParsingException, OAuthMessageSignerException, OAuthCommunicationException {
        if (contactLists == null) {
            HttpClient client = new HttpClient();

            contactLists = new ArrayList<ContactList>();
            List campaigns = queryList("https://api.constantcontact.com/v2/lists?api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);


            for (Object obj : campaigns) {
                try {
                    Map node = (Map) obj;
                    String id = node.get("id").toString();
                    String name = node.get("name").toString();
                    ContactList contactList = new ContactList(id, name, name);
                    contactLists.add(contactList);
                    Map result = query("https://api.constantcontact.com/v2/lists/" + id + "/contacts?limit=500&api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
                    Map meta = (Map) result.get("meta");
                    String nextLink = null;
                    if (meta != null) {
                        Map pagination = (Map) meta.get("pagination");
                        if (pagination != null) {
                            Object nextLinkObject = pagination.get("next_link");
                            if (nextLinkObject != null) {
                                nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                            }
                        }
                    }
                    boolean hasMoreData;
                    do {
                        List results = (List) result.get("results");
                        hasMoreData = false;
                        for (Object contactObj : results) {
                            Map contactMap = (Map) contactObj;
                            String contactID = contactMap.get("id").toString();
                            contactList.getUsers().add(contactID);
                        }
                        if (nextLink != null) {
                            result = query(nextLink, ccSource, client);
                            meta = (Map) result.get("meta");
                            nextLink = null;
                            if (meta != null) {
                                Map pagination = (Map) meta.get("pagination");
                                if (pagination != null) {
                                    Object nextLinkObject = pagination.get("next_link");
                                    if (nextLinkObject != null) {
                                        nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                                    }
                                }
                            }
                            hasMoreData = true;
                        }
                    } while (hasMoreData);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }
        return contactLists;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }
}

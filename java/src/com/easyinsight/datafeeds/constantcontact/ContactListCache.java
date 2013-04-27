package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.datafeeds.FeedDefinition;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

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
                Map node = (Map) obj;
                String id = node.get("id").toString();
                String name = node.get("name").toString();
                contactLists.add(new ContactList(id, name, name));
            }

            //contacts = new ContactRetrieval().retrieve(ccSource, contactLists);
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

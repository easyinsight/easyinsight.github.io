package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.IRow;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.dataset.DataSet;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
            contactLists = new ArrayList<ContactList>();
            Document doc = query("https://api.constantcontact.com/ws/customers/"+ccSource.getCcUserName()+"/lists", ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource);
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
                        doc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), ccSource);
                        break;
                    }
                }
            } while (hasMoreData);
            contacts = new ContactRetrieval().retrieve(ccSource, contactLists);
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

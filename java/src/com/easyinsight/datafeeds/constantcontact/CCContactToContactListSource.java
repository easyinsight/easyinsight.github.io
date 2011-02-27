package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:57:05 AM
 */
public class CCContactToContactListSource extends ConstantContactBaseSource {

    public static final String CONTACT_ID = "Join - Contact ID";
    public static final String CONTACT_LIST_ID = "Join - Contact List ID";

    public CCContactToContactListSource() {
        setFeedName("Contacts to Contact Lists");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CONTACT_ID, CONTACT_LIST_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_LIST_ID), true));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CONTACT_TO_CONTACT_LIST;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            Document listDoc = query("https://api.constantcontact.com/ws/customers/" + ccSource.getCcUserName() + "/lists", ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
            boolean hasMoreData;
            do {
                hasMoreData = false;
                Nodes lists = listDoc.query("/feed/entry/id/text()");
                for (int i = 0; i < lists.size(); i++) {
                    Node listNode = lists.get(i);
                    String id = listNode.getValue().split("/")[7];
                    String url = "https://api.constantcontact.com/ws/customers/" + ccSource.getCcUserName() + "/lists/" + id + "/members";
                    Document doc = query(url, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                    boolean hasMoreContacts;
                    do {
                        hasMoreContacts = false;
                        Nodes subscribers = doc.query("/feed/entry/id/text()");
                        for (int j = 0; j < subscribers.size(); j++) {
                            IRow row = dataSet.createRow();
                            Node subscriber = subscribers.get(j);
                            String contactID = subscriber.getValue().split("/")[7];
                            row.addValue(CONTACT_ID, contactID);
                            row.addValue(CONTACT_LIST_ID, id);
                        }
                        Nodes links = doc.query("/feed/link");

                        for (int j = 0; j < links.size(); j++) {
                            Element link = (Element) links.get(j);
                            Attribute attribute = link.getAttribute("rel");
                            if (attribute != null && "next".equals(attribute.getValue())) {
                                String linkURL = link.getAttribute("href").getValue();
                                hasMoreContacts = true;
                                doc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                                break;
                            }
                        }
                    } while (hasMoreContacts);
                }
                Nodes links = listDoc.query("/feed/link");

                for (int i = 0; i < links.size(); i++) {
                    Element link = (Element) links.get(i);
                    Attribute attribute = link.getAttribute("rel");
                    if (attribute != null && "next".equals(attribute.getValue())) {
                        String linkURL = link.getAttribute("href").getValue();
                        hasMoreData = true;
                        listDoc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                        break;
                    }
                }
            } while (hasMoreData);
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

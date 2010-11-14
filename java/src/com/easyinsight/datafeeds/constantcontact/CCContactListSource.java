package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:56:52 AM
 */
public class CCContactListSource extends ConstantContactBaseSource {

    public static final String CONTACT_LIST_NAME = "Contact List Name";
    public static final String CONTACT_LIST_SHORT_NAME = "Contact List Short Name";
    public static final String CONTACT_LIST_URL = "Contact List URL";
    public static final String CONTACT_LIST_COUNT = "Contact List Count";
    public static final String CONTACT_LIST_ID = "Contact List ID";
    public static final String CONTACT_LIST_UPDATED_ON = "Contact List Updated On";

    public CCContactListSource() {
        setFeedName("Contact Lists");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CONTACT_LIST_NAME, CONTACT_LIST_SHORT_NAME, CONTACT_LIST_URL, CONTACT_LIST_COUNT, CONTACT_LIST_ID,
                CONTACT_LIST_UPDATED_ON);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CONTACT_LIST_NAME), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_LIST_SHORT_NAME), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_LIST_URL), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_LIST_ID), true));
        items.add(new AnalysisDateDimension(keys.get(CONTACT_LIST_UPDATED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisMeasure(keys.get(CONTACT_LIST_COUNT), AggregationTypes.SUM));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CONTACT_LISTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();
            Document doc = query("http://api.constantcontact.com/ws/customers/"+ccSource.getCcUserName()+"/lists", ccSource.getTokenKey(), ccSource.getTokenSecret());
            boolean hasMoreData;
            do {
                hasMoreData = false;
                Nodes nodes = doc.query("/feed/entry");
                for (int i = 0; i < nodes.size(); i++) {

                    IRow row = dataSet.createRow();
                    Node node = nodes.get(i);
                    String idString = node.query("id/text()").get(0).getValue();
                    String id = idString.split("/")[7];
                    String name = node.query("content/ContactList/Name/text()").get(0).getValue();
                    String shortName = node.query("content/ContactList/ShortName/text()").get(0).getValue();
                    row.addValue(CONTACT_LIST_ID, id);
                    row.addValue(CONTACT_LIST_NAME, name);
                    row.addValue(CONTACT_LIST_SHORT_NAME, shortName);
                    row.addValue(CONTACT_LIST_COUNT, 1);
                }

                Nodes links = doc.query("/feed/link");

                for (int i = 0; i < links.size(); i++) {
                    Element link = (Element) links.get(i);
                    Attribute attribute = link.getAttribute("rel");
                    if (attribute != null && "next".equals(attribute.getValue())) {
                        String linkURL = link.getAttribute("href").getValue();
                        hasMoreData = true;
                        doc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret());
                        break;
                    }
                }
            } while (hasMoreData);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ID, CONTACT_LIST_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            List<ContactList> contactLists = ccSource.getOrCreateContactListCache().getOrCreateContactLists(ccSource);
            System.out.println("Started retrieving contact list members...");
            DataSet dataSet = new DataSet();
            for (ContactList contactList : contactLists) {
                for (String userEmail : contactList.getUsers()) {
                    IRow row = dataSet.createRow();
                    row.addValue(CONTACT_ID, userEmail);
                    row.addValue(CONTACT_LIST_ID, contactList.getId());
                }
            }
            


            System.out.println("Finished retrieving contact list members");
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

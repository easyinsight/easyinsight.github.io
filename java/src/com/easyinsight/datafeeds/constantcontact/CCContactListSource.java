package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_LIST_NAME, CONTACT_LIST_SHORT_NAME, CONTACT_LIST_URL, CONTACT_LIST_COUNT, CONTACT_LIST_ID,
                CONTACT_LIST_UPDATED_ON);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            List<ContactList> contactLists = ccSource.getOrCreateContactListCache().getOrCreateContactLists(ccSource);
            DataSet dataSet = new DataSet();
            for (ContactList contactList : contactLists) {
                IRow row = dataSet.createRow();
                row.addValue(CONTACT_LIST_ID, contactList.getId());
                row.addValue(CONTACT_LIST_NAME, contactList.getName());
                row.addValue(CONTACT_LIST_SHORT_NAME, contactList.getShortName());
                row.addValue(CONTACT_LIST_COUNT, 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

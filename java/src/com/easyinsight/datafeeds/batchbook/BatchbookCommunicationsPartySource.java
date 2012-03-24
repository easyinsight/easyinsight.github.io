package com.easyinsight.datafeeds.batchbook;

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
 * Date: 1/17/11
 * Time: 10:13 PM
 */
public class BatchbookCommunicationsPartySource extends BatchbookBaseSource {
    public static final String COMMUNICATION_ID = "Source Communication ID";
    public static final String CONTACT_ID = "Communication Subject";
    public static final String ROLE = "Role";

    public BatchbookCommunicationsPartySource() {
        setFeedName("Communication Parties");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK_COMMUNICATION_PARTIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COMMUNICATION_ID, CONTACT_ID, ROLE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMMUNICATION_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(ROLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        DataSet dataSet = new DataSet();
        BatchbookCompositeSource batchbookCompositeSource = (BatchbookCompositeSource) parentDefinition;
        try {
            List<Communication> communications = batchbookCompositeSource.getOrCreateCache().getCommunications();
            for (Communication communication : communications) {
                for (CommunicationContact contact : communication.getContacts()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(COMMUNICATION_ID), communication.getId());
                    row.addValue(keys.get(ROLE), contact.getRole());
                    row.addValue(keys.get(CONTACT_ID), contact.getId());
                }
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dataSet;
    }
}

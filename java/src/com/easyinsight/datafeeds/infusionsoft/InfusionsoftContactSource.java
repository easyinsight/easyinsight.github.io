package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftContactSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String COMPANY_ID = "CompanyID";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String DATE_CREATED = "DateCreated";

    public InfusionsoftContactSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CONTACTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, COMPANY_ID, FIRST_NAME, LAST_NAME, DATE_CREATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(COMPANY_ID), "Contact Company ID"));
        analysisitems.add(new AnalysisDimension(keys.get(FIRST_NAME), "First Name"));
        analysisitems.add(new AnalysisDimension(keys.get(LAST_NAME), "Last Name"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Contact Creation Date", AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Contact", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

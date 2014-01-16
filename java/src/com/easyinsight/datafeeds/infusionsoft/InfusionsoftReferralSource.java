package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
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
 * Date: 12/30/13
 * Time: 5:08 PM
 */
public class InfusionsoftReferralSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String CONTACT_ID = "ContactId";
    public static final String AFFILIATE_ID = "AffiliateId";
    public static final String DATE_SET = "DateSet";
    public static final String DATE_EXPIRES = "DateExpires";
    public static final String IP_ADDRESS = "IPAddress";
    public static final String SOURCE = "Source";
    public static final String INFO = "Info";
    public static final String TYPE = "Type";

    public InfusionsoftReferralSource() {
        setFeedName("Referrals");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_REFERRAL;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CONTACT_ID, AFFILIATE_ID, DATE_EXPIRES, DATE_SET, IP_ADDRESS, SOURCE, INFO, TYPE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Referral ID"));
        analysisitems.add(new AnalysisDimension(keys.get(AFFILIATE_ID), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(IP_ADDRESS), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(SOURCE), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(INFO), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(TYPE), "Affiliate ID"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_SET), "Date Set", AnalysisDateDimension.DAY_LEVEL));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_EXPIRES), "Date Expires", AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Referral", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
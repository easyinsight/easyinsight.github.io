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
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftAffiliateSource extends InfusionsoftTableSource {

    public static final String AFFILIATE_ID = "Id";
    public static final String CONTACT_ID = "ContactId";
    public static final String LEAD_AMT = "LeadAmt";
    public static final String LEAD_PCT = "LeadPercent";
    public static final String SALE_AMT = "SaleAmt";
    public static final String SALE_PCT = "SalePercent";
    public static final String PAYOUT_TYPE = "PayoutType";
    public static final String DEF_COMMISSION_TYPE = "DefCommissionType";
    public static final String STATUS = "Status";
    public static final String AFF_NAME = "AffName";
    public static final String AFF_CODE = "AffCode";

    public InfusionsoftAffiliateSource() {
        setFeedName("Affiliates");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_AFFILIATES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(AFFILIATE_ID, CONTACT_ID, LEAD_AMT, LEAD_PCT, SALE_AMT, SALE_PCT, PAYOUT_TYPE, DEF_COMMISSION_TYPE,
                STATUS, AFF_CODE, AFF_CODE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(AFFILIATE_ID), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Affiliate Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PAYOUT_TYPE), "Payout Type"));
        analysisitems.add(new AnalysisDimension(keys.get(DEF_COMMISSION_TYPE), "Commission Type"));
        analysisitems.add(new AnalysisDimension(keys.get(STATUS), "Affiliate Status"));
        analysisitems.add(new AnalysisMeasure(keys.get(LEAD_AMT), "Lead Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(LEAD_PCT), "Lead Percent", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(SALE_AMT), "Sale Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(SALE_PCT), "Sale Percent", AggregationTypes.SUM));

        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Affiliate", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

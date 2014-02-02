package com.easyinsight.datafeeds.solve360;

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
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class Solve360CompanySource extends Solve360BaseSource {

    public static final String COMPANY_ID = "Company ID";
    private static final String NAME = "Name";
    private static final String RELATED_COMPANIES = "Related Companies";
    private static final String BILLING_ADDRESS = "Billing Address";
    private static final String PHONE = "Phone #";
    private static final String FAX = "Fax #";
    private static final String MAIN_ADDRESS = "Main Address";
    private static final String SHIPPING_ADDRESS = "Shipping Address";
    private static final String RESPONSIBLE_PARTY = "Responsible Party";
    private static final String WEBSITE = "Website";

    public Solve360CompanySource() {
        setFeedName("Companies");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_COMPANIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(COMPANY_ID, NAME, RELATED_COMPANIES, BILLING_ADDRESS, PHONE, FAX, MAIN_ADDRESS, SHIPPING_ADDRESS, RESPONSIBLE_PARTY, WEBSITE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(NAME)));
        analysisItems.add(new AnalysisDimension(keys.get(RELATED_COMPANIES)));
        analysisItems.add(new AnalysisDimension(keys.get(BILLING_ADDRESS)));
        analysisItems.add(new AnalysisDimension(keys.get(PHONE)));
        analysisItems.add(new AnalysisDimension(keys.get(FAX)));
        analysisItems.add(new AnalysisDimension(keys.get(MAIN_ADDRESS)));
        analysisItems.add(new AnalysisDimension(keys.get(SHIPPING_ADDRESS)));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLE_PARTY)));
        analysisItems.add(new AnalysisDimension(keys.get(WEBSITE)));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Map<Integer, Company> companies = ((Solve360CompositeSource) parentDefinition).getOrCreateCompanyCache();
        try {
            DataSet dataSet = new DataSet();
            for(Company c : companies.values()) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(COMPANY_ID), String.valueOf(c.getCompanyId()));
                row.addValue(keys.get(NAME), c.getName());
                row.addValue(keys.get(RELATED_COMPANIES), c.getCompany());
                row.addValue(keys.get(BILLING_ADDRESS), c.getBillingAddress());
                row.addValue(keys.get(PHONE), c.getCompanyPhone());
                row.addValue(keys.get(FAX), c.getFaxNumber());
                row.addValue(keys.get(MAIN_ADDRESS), c.getMainAddress());
                row.addValue(keys.get(SHIPPING_ADDRESS), c.getShippingAddress());
                row.addValue(keys.get(RESPONSIBLE_PARTY), c.getResponsibleParty());
                row.addValue(keys.get(WEBSITE), c.getWebsite());
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

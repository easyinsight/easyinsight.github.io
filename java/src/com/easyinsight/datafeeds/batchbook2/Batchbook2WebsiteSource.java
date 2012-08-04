package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/31/12
 * Time: 5:44 PM
 */
public class Batchbook2WebsiteSource extends Batchbook2BaseSource {
    public static final String ID = "Website Person ID";
    public static final String COMPANY_ID = "Website Company ID";
    public static final String WEBSITE_ADDRESS = "Website Address";
    public static final String WEBSITE_LABEL = "Website Label";

    public Batchbook2WebsiteSource() {
        setFeedName("Websites");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, WEBSITE_ADDRESS, WEBSITE_LABEL, COMPANY_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(ID)));
        fields.add(new AnalysisDimension(keys.get(WEBSITE_ADDRESS)));
        fields.add(new AnalysisDimension(keys.get(WEBSITE_LABEL)));
        fields.add(new AnalysisDimension(keys.get(COMPANY_ID)));
        return fields;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_WEBSITES;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            List<Person> people = batchbook2CompositeSource.getOrCreateCache(httpClient).getPeople();
            DataSet dataSet = new DataSet();
            for (Person person : people) {
                for (Stuff stuff : person.getWebsites()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(ID), person.getId());
                    row.addValue(keys.get(WEBSITE_ADDRESS), stuff.getPart1());
                    row.addValue(keys.get(WEBSITE_LABEL), stuff.getPart2());
                }
            }
            List<Company> companies = batchbook2CompositeSource.getOrCreateCache(httpClient).getCompanies();
            for (Company company : companies) {
                for (Stuff stuff : company.getEmails()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(COMPANY_ID), company.getId());
                    row.addValue(keys.get(WEBSITE_ADDRESS), stuff.getPart1());
                    row.addValue(keys.get(WEBSITE_LABEL), stuff.getPart2());
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

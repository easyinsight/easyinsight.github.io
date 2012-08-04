package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.*;
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
 * Time: 3:21 PM
 */
public class Batchbook2EmailSource extends Batchbook2BaseSource {
    public static final String ID = "Email Person ID";
    public static final String COMPANY_ID = "Email Company ID";
    public static final String EMAIL_ADDRESS = "Email Address";
    public static final String EMAIL_TYPE = "Email Label";

    public Batchbook2EmailSource() {
        setFeedName("Email Addresses");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, EMAIL_ADDRESS, EMAIL_TYPE, COMPANY_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(ID)));
        fields.add(new AnalysisDimension(keys.get(EMAIL_ADDRESS)));
        fields.add(new AnalysisDimension(keys.get(EMAIL_TYPE)));
        fields.add(new AnalysisDimension(keys.get(COMPANY_ID)));
        return fields;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_EMAILS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            List<Person> people = batchbook2CompositeSource.getOrCreateCache(httpClient).getPeople();
            DataSet dataSet = new DataSet();
            for (Person person : people) {
                for (Stuff stuff : person.getEmails()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(ID), person.getId());
                    row.addValue(keys.get(EMAIL_ADDRESS), stuff.getPart1());
                    row.addValue(keys.get(EMAIL_TYPE), stuff.getPart2());
                }
            }
            List<Company> companies = batchbook2CompositeSource.getOrCreateCache(httpClient).getCompanies();
            for (Company company : companies) {
                for (Stuff stuff : company.getEmails()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(COMPANY_ID), company.getId());
                    row.addValue(keys.get(EMAIL_ADDRESS), stuff.getPart1());
                    row.addValue(keys.get(EMAIL_TYPE), stuff.getPart2());
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

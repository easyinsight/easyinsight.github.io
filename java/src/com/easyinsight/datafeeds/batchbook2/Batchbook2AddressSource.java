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
 * Time: 3:21 PM
 */
public class Batchbook2AddressSource extends Batchbook2BaseSource {
    public static final String ID = "Address Person ID";
    public static final String COMPANY_ID = "Address Company ID";
    public static final String ADDRESS1 = "Address Line 1";
    public static final String ADDRESS2 = "Address Line 2";
    public static final String CITY = "Address City";
    public static final String STATE = "Address State";
    public static final String POSTAL = "Address Postal";
    public static final String COUNTRY = "Address Country";
    public static final String LABEL = "Address Label";

    public Batchbook2AddressSource() {
        setFeedName("Addresses");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, ADDRESS1, ADDRESS2, CITY, STATE, POSTAL, COUNTRY, LABEL, COMPANY_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.add(new AnalysisDimension(keys.get(ID)));
        fields.add(new AnalysisDimension(keys.get(ADDRESS1)));
        fields.add(new AnalysisDimension(keys.get(ADDRESS2)));
        fields.add(new AnalysisDimension(keys.get(CITY)));
        fields.add(new AnalysisDimension(keys.get(STATE)));
        fields.add(new AnalysisDimension(keys.get(POSTAL)));
        fields.add(new AnalysisDimension(keys.get(COUNTRY)));
        fields.add(new AnalysisDimension(keys.get(LABEL)));
        fields.add(new AnalysisDimension(keys.get(COMPANY_ID)));
        return fields;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_ADDRESSES;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            DataSet dataSet = new DataSet();
            List<Person> people = batchbook2CompositeSource.getOrCreateCache(httpClient).getPeople();
            for (Person person : people) {
                for (Address address : person.getAddresses()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(ID), person.getId());
                    row.addValue(keys.get(ADDRESS1), address.getAddress1());
                    row.addValue(keys.get(ADDRESS2), address.getAddress2());
                    row.addValue(keys.get(CITY), address.getCity());
                    row.addValue(keys.get(STATE), address.getState());
                    row.addValue(keys.get(POSTAL), address.getPostalCode());
                    row.addValue(keys.get(COUNTRY), address.getCountry());
                    row.addValue(keys.get(LABEL), address.getLabel());
                }
            }
            List<Company> companies = batchbook2CompositeSource.getOrCreateCache(httpClient).getCompanies();
            for (Company company : companies) {
                for (Address address : company.getAddresses()) {
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(COMPANY_ID), company.getId());
                    row.addValue(keys.get(ADDRESS1), address.getAddress1());
                    row.addValue(keys.get(ADDRESS2), address.getAddress2());
                    row.addValue(keys.get(CITY), address.getCity());
                    row.addValue(keys.get(STATE), address.getState());
                    row.addValue(keys.get(POSTAL), address.getPostalCode());
                    row.addValue(keys.get(COUNTRY), address.getCountry());
                    row.addValue(keys.get(LABEL), address.getLabel());
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

package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 7/16/12
 * Time: 10:12 AM
 */
public class Batchbook2CompanySource extends Batchbook2BaseSource {

    public static final String ID = "Company ID";
    public static final String ABOUT = "Company About";
    public static final String NAME = "Company Name";
    public static final String TAGS = "Company Tags";
    public static final String COUNT = "Company Count";
    public static final String URL = "Company URL";

    public Batchbook2CompanySource() {
        setFeedName("Companies");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(ABOUT, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(URL, new AnalysisDimension());
        fieldBuilder.addField(TAGS, new AnalysisList());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_COMPANIES;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            List<Company> companies = batchbook2CompositeSource.getOrCreateCache(httpClient).getCompanies();
            DataSet dataSet = new DataSet();
            for (Company company : companies) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), company.getId());
                row.addValue(keys.get(URL), batchbook2CompositeSource.getUrl() + "/contacts/" + company.getId());
                row.addValue(keys.get(ABOUT), company.getAbout());
                row.addValue(keys.get(NAME), company.getName());
                row.addValue(keys.get(COUNT), 1);
                StringBuilder sb = new StringBuilder();
                for (String tag : company.getTags()) {
                    sb.append(tag).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                row.addValue(keys.get(TAGS), sb.toString());
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

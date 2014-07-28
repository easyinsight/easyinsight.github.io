package com.easyinsight.datafeeds.batchbook2;

import com.easyinsight.PasswordStorage;
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
public class Batchbook2PeopleSource extends Batchbook2BaseSource {

    public static final String ID = "Person ID";
    public static final String ABOUT = "Person About";
    public static final String FIRST_NAME = "Person First Name";
    public static final String LAST_NAME = "Person Last Name";
    public static final String NAME = "Person Name";
    public static final String TAGS = "Person Tags";
    public static final String COMPANY_ID = "Person Company ID";
    public static final String COUNT = "Person Count";
    public static final String PERSON_URL = "Person URL";

    public Batchbook2PeopleSource() {
        setFeedName("People");
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(ABOUT, new AnalysisDimension());
        fieldBuilder.addField(FIRST_NAME, new AnalysisDimension());
        fieldBuilder.addField(LAST_NAME, new AnalysisDimension());
        fieldBuilder.addField(NAME, new AnalysisDimension());
        fieldBuilder.addField(TAGS, new AnalysisList());
        fieldBuilder.addField(COMPANY_ID, new AnalysisDimension());
        fieldBuilder.addField(PERSON_URL, new AnalysisDimension());
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.BATCHBOOK2_PEOPLE;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            Batchbook2CompositeSource batchbook2CompositeSource = (Batchbook2CompositeSource) parentDefinition;
            HttpClient httpClient = Batchbook2BaseSource.getHttpClient(batchbook2CompositeSource.getToken(), "");
            List<Person> people = batchbook2CompositeSource.getOrCreateCache(httpClient).getPeople();
            DataSet dataSet = new DataSet();
            for (Person person : people) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(ID), person.getId());
                row.addValue(keys.get(PERSON_URL), batchbook2CompositeSource.getUrl() + "/contacts/" + person.getId());
                for (Stuff stuff : person.getCompanies()) {
                    if ("true".equals(stuff.getPart2())) {
                        row.addValue(keys.get(COMPANY_ID), stuff.getPart1());
                    }
                }
                row.addValue(keys.get(FIRST_NAME), person.getFirstName());
                row.addValue(keys.get(LAST_NAME), person.getLastName());
                row.addValue(keys.get(ABOUT), person.getAbout());
                String name;
                if (person.getLastName() == null) {
                    name = person.getFirstName();
                } else if (person.getFirstName() == null) {
                    name = person.getLastName();
                } else {
                    name = person.getFirstName() + " " + person.getLastName();
                }
                row.addValue(keys.get(NAME), name);
                row.addValue(keys.get(COUNT), 1);
                StringBuilder sb = new StringBuilder();
                for (String tag : person.getTags()) {
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

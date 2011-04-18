package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 1:35:19 PM
 */
public class HighRiseCompanyNotesSource extends HighRiseBaseSource {
    public static final String BODY = "Company Note Body";
    public static final String NOTE_ID = "Company Note ID";
    public static final String NOTE_COMPANY_ID = "Note Company ID";
    public static final String NOTE_CREATED_AT = "Company Note Created At";
    public static final String NOTE_UPDATED_AT = "Company Note Updated At";
    public static final String NOTE_AUTHOR = "Company Note Author";
    public static final String COUNT = "Company Note Count";

    public HighRiseCompanyNotesSource() {
        setFeedName("Company Notes");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(BODY, NOTE_ID, NOTE_CREATED_AT, NOTE_UPDATED_AT, NOTE_AUTHOR, NOTE_COMPANY_ID, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(NOTE_ID), true));
        AnalysisDimension noteContactDim = new AnalysisDimension(keys.get(NOTE_COMPANY_ID), true);
        noteContactDim.setHidden(true);
        analysisItems.add(noteContactDim);
        analysisItems.add(new AnalysisDimension(keys.get(NOTE_AUTHOR), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(NOTE_CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(NOTE_UPDATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_COMPANY_NOTES;
    }

    protected boolean clearsData() {
        return false;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeCompanyNotes()) {
            return ds;
        }
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");

        try {
            Date date;
            if (lastRefreshDate == null) {
                date = new Date(0);
            } else {
                date = lastRefreshDate;
            }
            HighriseRecordingsCache highriseRecordingsCache = highRiseCompositeSource.getOrCreateRecordingsCache(client, date);

            List<Recording> recordings = highriseRecordingsCache.getCompanyNotes();

            Key noteKey = parentDefinition.getField(NOTE_ID).toBaseKey();

            if (lastRefreshDate == null) {
                for (Recording recording : recordings) {
                    IRow row = ds.createRow();
                    recordingToRow(recording, row);
                }
            } else {
                for (Recording recording : recordings) {
                    ds = new DataSet();
                    IRow row = ds.createRow();
                    recordingToRow(recording, row);
                    StringWhere userWhere = new StringWhere(noteKey, recording.getId());
                    dataStorage.updateData(ds, Arrays.asList((IWhere) userWhere));
                    ds = null;
                }
            }


        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    private void recordingToRow(Recording recording, IRow row) {
        row.addValue(BODY, recording.getBody());
        row.addValue(NOTE_ID, recording.getId());
        row.addValue(NOTE_AUTHOR, recording.getAuthor());
        row.addValue(NOTE_CREATED_AT, new DateValue(recording.getCreatedAt()));
        row.addValue(NOTE_UPDATED_AT, new DateValue(recording.getUpdatedAt()));
        row.addValue(NOTE_COMPANY_ID, recording.getSubjectID());
        row.addValue(COUNT, 1);
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new HighRiseCompanyNotes1To2(this));
    }
}
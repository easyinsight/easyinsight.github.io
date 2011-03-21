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
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 1:35:19 PM
 */
public class HighRiseDealNotesSource extends HighRiseBaseSource {
    public static final String BODY = "Deal Note Body";
    public static final String NOTE_ID = "Deal Note ID";
    public static final String NOTE_DEAL_ID = "Note Deal ID";
    public static final String NOTE_CREATED_AT = "Deal Note Created At";
    public static final String NOTE_UPDATED_AT = "Deal Note Updated At";
    public static final String NOTE_AUTHOR = "Deal Note Author";
    public static final String COUNT = "Deal Note Count";

    public HighRiseDealNotesSource() {
        setFeedName("Deal Notes");
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(BODY, NOTE_ID, NOTE_CREATED_AT, NOTE_UPDATED_AT, NOTE_AUTHOR, NOTE_DEAL_ID, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(BODY), true));
        analysisItems.add(new AnalysisDimension(keys.get(NOTE_ID), true));
        AnalysisDimension noteContactDim = new AnalysisDimension(keys.get(NOTE_DEAL_ID), true);
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
        return FeedType.HIGHRISE_DEAL_NOTES;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeDealNotes()) {
            return ds;
        }
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
            HighriseCache highriseCache = highRiseCompositeSource.getOrCreateCache(client);
            Document companies = runRestRequest("/deals.xml", client, builder, url, true, false, parentDefinition);
            Nodes caseNodes = companies.query("/deals/deal");
            loadingProgress(0, 1, "Synchronizing with deal notes...", callDataID);
            for (int i = 0; i < caseNodes.size(); i++) {
                Node caseNode = caseNodes.get(i);
                String id = queryField(caseNode, "id/text()");
                int notesOffset = 0;
                int notesCount;
                do {
                    Document notes;
                    if (notesOffset == 0) {
                        notes = runRestRequest("/deals/" + id + "/notes.xml", client, builder, highRiseCompositeSource.getUrl(), false, false, parentDefinition);
                    } else {
                        notes = runRestRequest("/deals/" + id + "/notes.xml?n=" + notesOffset, client, builder, highRiseCompositeSource.getUrl(), false, false, parentDefinition);
                    }
                    Nodes noteNodes = notes.query("/notes/note");
                    notesCount = 0;
                    for (int j = 0; j < noteNodes.size(); j++) {
                        Node noteNode = noteNodes.get(j);
                        String noteID = queryField(noteNode, "id/text()");
                        String body = queryField(noteNode, "body/text()");
                        String authorID = queryField(noteNode, "author-id/text()");
                        String createdAtString = queryField(noteNode, "created-at");
                        String updatedAtString = queryField(noteNode, "updated-at");
                        Date createdAt = deadlineFormat.parse(createdAtString);
                        Date updatedAt = null;
                        if (updatedAtString != null) {
                            updatedAt = deadlineFormat.parse(updatedAtString);
                        }
                        IRow row = ds.createRow();
                        row.addValue(BODY, body);
                        row.addValue(NOTE_ID, noteID);
                        row.addValue(NOTE_AUTHOR, highriseCache.getUserName(authorID));
                        row.addValue(NOTE_CREATED_AT, new DateValue(createdAt));
                        row.addValue(NOTE_UPDATED_AT, new DateValue(updatedAt));
                        row.addValue(NOTE_DEAL_ID, id);
                        row.addValue(COUNT, 1);

                        notesCount++;
                    }
                    notesOffset += 25;
                } while (notesCount == 25);
            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (parentDefinition.isAdjustDates()) {
            ds = adjustDates(ds);
        }
        return ds;
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new HighRiseDealNotes1To2(this));
    }
}
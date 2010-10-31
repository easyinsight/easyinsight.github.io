package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
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
    protected List<String> getKeys() {
        return Arrays.asList(BODY, NOTE_ID, NOTE_CREATED_AT, NOTE_UPDATED_AT, NOTE_AUTHOR, NOTE_COMPANY_ID, COUNT);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
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

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeCompanyNotes()) {
            return ds;
        }
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        if (token == null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.HIGHRISE_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        } else if (token != null && credentials != null && credentials.getUserName() != null && !"".equals(credentials.getUserName()) &&
                !credentials.getUserName().equals(token.getTokenValue())) {
            token.setTokenValue(credentials.getUserName());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID(), conn);
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        boolean writeDuring = dataStorage != null && !parentDefinition.isAdjustDates();
        Builder builder = new Builder();
        try {
            HighriseCache highriseCache = highRiseCompositeSource.getOrCreateCache(client);
            int offset = 0;
            int companyCount;
            do {
                companyCount = 0;
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/companies.xml?", client, builder, url, true, false);
                } else {
                    companies = runRestRequest("/companies.xml?n=" + offset, client, builder, url, true, false);
                }
                Nodes companyNodes = companies.query("/companies/company");
                loadingProgress(0, 1, "Synchronizing with company notes...", true);
                for (int i = 0; i < companyNodes.size(); i++) {
                    Node companyNode = companyNodes.get(i);
                    String id = queryField(companyNode, "id/text()");
                    int notesOffset = 0;
                    int notesCount;
                    do {
                        Document notes;
                        if (notesOffset == 0) {
                            notes = runRestRequest("/companies/" + id + "/notes.xml", client, builder, highRiseCompositeSource.getUrl(), false, false);
                        } else {
                            notes = runRestRequest("/companies/" + id + "/notes.xml?n=" + notesOffset, client, builder, highRiseCompositeSource.getUrl(), false, false);
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
                            IRow row = new Row();
                            row.addValue(BODY, body);
                            row.addValue(NOTE_ID, noteID);
                            row.addValue(NOTE_AUTHOR, highriseCache.getUserName(authorID));
                            row.addValue(NOTE_CREATED_AT, new DateValue(createdAt));
                            row.addValue(NOTE_UPDATED_AT, new DateValue(updatedAt));
                            row.addValue(NOTE_COMPANY_ID, id);
                            row.addValue(COUNT, 1);

                            notesCount++;
                        }
                        notesOffset += 25;
                    } while (notesCount == 25);
                }
                if (writeDuring) {
                    dataStorage.insertData(ds);
                    ds = new DataSet();
                }
                offset += 500;
            } while (companyCount == 500);

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if (!writeDuring) {
            if (parentDefinition.isAdjustDates()) {
                ds = adjustDates(ds);
            }
            return ds;
        } else {
            return null;
        }
    }
}
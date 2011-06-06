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
public class HighRiseEmailSource extends HighRiseBaseSource {
    public static final String EMAIL_AUTHOR = "Email Author";
    public static final String EMAIL_CONTACT_ID = "Email Contact ID";
    public static final String SENT_AT = "Email Sent At";
    public static final String EMAIL_ID = "Email ID";
    public static final String EMAIL_COMPANY_ID = "Email Company ID";
    public static final String EMAIL_DEAL_ID = "Email Deal ID";
    public static final String EMAIL_CASE_ID = "Email Case ID";
    public static final String COUNT = "Email Count";

    public HighRiseEmailSource() {
        setFeedName("Email");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(EMAIL_AUTHOR, EMAIL_CONTACT_ID, SENT_AT, COUNT, EMAIL_ID, EMAIL_COMPANY_ID, EMAIL_DEAL_ID, EMAIL_CASE_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_AUTHOR), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_CONTACT_ID), true));        
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_CASE_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_DEAL_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(EMAIL_ID), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(SENT_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_EMAILS;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return false;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;

        DataSet ds = new DataSet();
        if (!highRiseCompositeSource.isIncludeEmails()) {
            return ds;
        }
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        try {
            HighriseRecordingsCache cache = highRiseCompositeSource.getOrCreateRecordingsCache(client, lastRefreshDate);

            Key emailKey = parentDefinition.getField(EMAIL_ID).toBaseKey();

            System.out.println("...");
            if (lastRefreshDate == null) {
                for (HighriseEmail email : cache.getEmails()) {
                    IRow row = ds.createRow();
                    recordingToRow(email, row);
                }
            } else {
                for (HighriseEmail email : cache.getEmails()) {
                    ds = new DataSet();
                    IRow row = ds.createRow();
                    recordingToRow(email, row);
                    StringWhere userWhere = new StringWhere(emailKey, email.getId());
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

    private void recordingToRow(HighriseEmail recording, IRow row) {
        row.addValue(EMAIL_ID, recording.getId());
        row.addValue(EMAIL_AUTHOR, recording.getAuthorName());
        row.addValue(SENT_AT, new DateValue(recording.getSentAt()));
        row.addValue(EMAIL_CONTACT_ID, recording.getContactID());
        row.addValue(EMAIL_CASE_ID, recording.getCaseID());
        row.addValue(EMAIL_COMPANY_ID, recording.getCompanyID());
        row.addValue(EMAIL_DEAL_ID, recording.getDealID());
        row.addValue(COUNT, 1);
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new HighRiseEmail1To2(this));
    }
}

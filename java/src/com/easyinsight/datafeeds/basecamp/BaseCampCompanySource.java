package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
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
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 1, 2010
 * Time: 9:54:53 AM
 */
public class BaseCampCompanySource extends BaseCampBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static final String XMLDATETIMEFORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String COMPANY_ID = "Company ID";
    public static final String COMPANY_NAME = "Company Name";

    public BaseCampCompanySource() {
        setFeedName("Companies");
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_COMPANY;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        String url = source.getUrl();

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {

            Document projects = runRestRequest("/companies.xml", client, builder, url, null, false, parentDefinition, false);
            Nodes projectNodes = projects.query("/companies/company");
            for(int i = 0;i < projectNodes.size();i++) {
                Node companyNode = projectNodes.get(i);
                String companyName = queryField(companyNode, "name/text()");
                String companyID = queryField(companyNode, "id/text()");
                IRow row = ds.createRow();
                row.addValue(COMPANY_ID, companyID);
                row.addValue(COMPANY_NAME, companyName);
            }
        } catch (ReportException re) {
            throw re;
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }        
        return ds;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(COMPANY_ID, COMPANY_NAME);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension companyID = new AnalysisDimension(keys.get(COMPANY_ID), true);
        analysisItems.add(companyID);
        AnalysisDimension companyName = new AnalysisDimension(keys.get(COMPANY_NAME), true);
        analysisItems.add(companyName);
        return analysisItems;
    }
}

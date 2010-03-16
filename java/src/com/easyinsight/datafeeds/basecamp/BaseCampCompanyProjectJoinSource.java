package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Key;
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
 * Date: Mar 8, 2010
 * Time: 12:11:58 PM
 */
public class BaseCampCompanyProjectJoinSource extends BaseCampBaseSource {

    public static final String COMPANY_ID = "Company ID";
    public static final String PROJECT_ID = "Project ID";

    public BaseCampCompanyProjectJoinSource() {
        setFeedName("Company/Project Join");
    }

    public FeedType getFeedType() {
        return FeedType.BASECAMP_COMPANY_PROJECT_JOIN;
    }

    public DataSet getDataSet(com.easyinsight.users.Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage) {
        BaseCampCompositeSource source = (BaseCampCompositeSource) parentDefinition;
        String url = source.getUrl();

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.BASECAMP_TOKEN, parentDefinition.getDataFeedID(), false);
        if (token == null && credentials.getUserName() != null) {
            token = new Token();
            token.setTokenValue(credentials.getUserName());
            token.setTokenType(TokenStorage.BASECAMP_TOKEN);
            token.setUserID(SecurityUtil.getUserID());
            new TokenStorage().saveToken(token, parentDefinition.getDataFeedID());
        }
        if (token == null) {
            throw new RuntimeException();
        }
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
            Document projects = runRestRequest("/projects.xml", client, builder, url, null, false);
            Nodes projectNodes = projects.query("/projects/project");
            for(int i = 0;i < projectNodes.size();i++) {
                Node curProject = projectNodes.get(i);
                String projectID = queryField(curProject, "id/text()");
                Document companies = runRestRequest("/projects/" + projectID + "/companies.xml", client, builder, url, null, false);
                Nodes companyNodes = companies.query("/companies/company");
                for (int companyIndex = 0; companyIndex < companyNodes.size(); companyIndex++) {
                    Node companyNode = companyNodes.get(companyIndex);
                    String companyID = queryField(companyNode, "id/text()");
                    IRow row = ds.createRow();
                    row.addValue(COMPANY_ID, companyID);
                    row.addValue(PROJECT_ID, projectID);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return ds;
    }

    @NotNull
    protected List<String> getKeys() {
        return Arrays.asList(COMPANY_ID, PROJECT_ID);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, com.easyinsight.users.Credentials credentials, Connection conn) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisDimension companyID = new AnalysisDimension(keys.get(COMPANY_ID), true);
        companyID.setHidden(true);
        analysisItems.add(companyID);
        AnalysisDimension projectID = new AnalysisDimension(keys.get(PROJECT_ID), true);
        projectID.setHidden(true);
        analysisItems.add(projectID);
        return analysisItems;
    }
}

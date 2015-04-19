package com.easyinsight.datafeeds.google;

import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.analysis.*;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.*;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.authn.oauth.OAuthSigner;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ResourceNotFoundException;
import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.net.URL;

/**
 * User: jamesboe
 * Date: Jul 27, 2009
 * Time: 2:51:27 PM
 */
public class GoogleSpreadsheetFeed extends Feed {

    private String worksheetURL;
    private String tokenKey;
    private String tokenSecret;
    private String accessToken;
    private String refreshToken;

    private transient SpreadsheetService as;

    public GoogleSpreadsheetFeed(String worksheetURL, String tokenKey, String tokenSecret, String accessToken, String refreshToken) {
        this.worksheetURL = worksheetURL;
        this.tokenKey = tokenKey;
        this.tokenSecret = tokenSecret;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    private SpreadsheetService getService() throws OAuthException {

        if (as == null) {
            if (accessToken != null && !"".equals(accessToken)) {
                GoogleCredential credential = new GoogleCredential();
                credential.setAccessToken(accessToken);
                //credential.setRefreshToken(oauthTokenSecret);

                SpreadsheetService service = new SpreadsheetService("easyinsight-eidocs-1");
                service.useSsl();
                service.setOAuth2Credentials(credential);

                as = service;
            } else {
                SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
                spreadsheetService.useSsl();
                GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
                oauthParameters.setOAuthConsumerKey("www.easy-insight.com");
                oauthParameters.setOAuthConsumerSecret("OG0zlkZFPIe7JdHfLB8qXXYv");
                oauthParameters.setOAuthToken(tokenKey);
                oauthParameters.setOAuthTokenSecret(tokenSecret);
                oauthParameters.setScope("https://spreadsheets.google.com/feeds/");
                OAuthSigner signer = new OAuthHmacSha1Signer();
                spreadsheetService.setOAuthCredentials(oauthParameters, signer);
                as = spreadsheetService;
            }
        }
        return as;
    }

    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            return createDataSet(analysisItems);
        } catch (Exception gse) {
            try {
                OAuthClientRequest.TokenRequestBuilder tokenRequestBuilder = OAuthClientRequest.tokenLocation("https://www.googleapis.com/oauth2/v3/token").
                        setGrantType(GrantType.REFRESH_TOKEN).setClientId("196763839405.apps.googleusercontent.com").
                        setClientSecret("bRmYcsSJcp0CBehRRIcxl1hK").setRefreshToken(refreshToken).setRedirectURI("https://easy-insight.com/app/oauth");
                //tokenRequestBuilder.setParameter("type", "refresh_token");
                OAuthClient client = new OAuthClient(new URLConnectionClient());
                OAuthClientRequest request = tokenRequestBuilder.buildBodyMessage();
                OAuthJSONAccessTokenResponse response = client.accessToken(request);
                accessToken = response.getAccessToken();
                System.out.println("got new access token");

                try {
                    as = null;
                    DataSet dataSet = createDataSet(analysisItems);
                    PreparedStatement ps = conn.prepareStatement("SELECT GOOGLE_FEED.DATA_FEED_ID FROM GOOGLE_FEED, UPLOAD_POLICY_USERS, USER WHERE REFRESH_TOKEN = ? AND " +
                            "GOOGLE_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                            "USER.ACCOUNT_ID = ?");
                    PreparedStatement updateStmt = conn.prepareStatement("UPDATE GOOGLE_FEED SET ACCESS_TOKEN = ? WHERE data_feed_id = ?");

                    ps.setString(1, refreshToken);
                    ps.setLong(2, SecurityUtil.getAccountID());
                    ResultSet rs = ps.executeQuery();
                    System.out.println("We also need to update: ");
                    while (rs.next()) {
                        System.out.println("\t" + rs.getLong(1));
                        /*updateStmt.setString(1, accessToken);
                        updateStmt.setLong(2, rs.getLong(1));
                        updateStmt.executeUpdate();*/
                    }
                    ps.close();
                    updateStmt.close();
                    return dataSet;
                } catch (Exception e1) {
                    as = null;
                    throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Google data.", getDataSource()));
                }
            } catch (Exception e) {
                LogClass.error(e);
                throw new ReportException(new DataSourceConnectivityReportFault("You need to reauthorize Easy Insight to access your Google data.", getDataSource()));
            }
        }
    }

    protected DataSet createDataSet(Set<AnalysisItem> analysisItems) throws OAuthException, java.io.IOException, com.google.gdata.util.ServiceException {
        SpreadsheetService myService = getService();
        URL listFeedUrl = new URL(worksheetURL);
        ListFeed feed = myService.getFeed(listFeedUrl, ListFeed.class);
        DataSet dataSet = new DataSet();
        for (ListEntry listEntry : feed.getEntries()) {
            IRow row = dataSet.createRow();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.isDerived()) {
                    continue;
                }
                boolean matched = false;
                Key key = analysisItem.getKey();
                for (String tag : listEntry.getCustomElements().getTags()) {
                    if (key.toKeyString().equals(tag)) {
                        matched = true;
                        Value value;
                        String string = listEntry.getCustomElements().getValue(tag);
                        if (string == null) {
                            value = new EmptyValue();
                        } else {
                            string = string.trim();
                            if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                                value = new NumericValue(NumericValue.produceDoubleValue(string));
                            } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                                value = date.renameMeLater(new StringValue(string));
                            } else {
                                value = new StringValue(string);
                            }
                        }
                        row.addValue(analysisItem.createAggregateKey(), value);
                    }
                }
                if (!matched && "Count".equals(key.toKeyString())) {
                    row.addValue(analysisItem.createAggregateKey(), 1);
                }
            }
        }
        return dataSet;
    }
}

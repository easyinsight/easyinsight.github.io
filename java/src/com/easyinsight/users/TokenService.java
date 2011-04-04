package com.easyinsight.users;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.freshbooks.FreshbooksCompositeSource;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.config.ConfigLoader;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import flex.messaging.FlexContext;
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.PlainTextMessageSigner;

import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

/**
 * User: jamesboe
 * Date: Aug 24, 2009
 * Time: 2:25:19 PM
 */
public class TokenService {

    public static final int CONNECTION_SETUP = 1;
    public static final int NO_CONNECTION = 2;

    public static final String SALESFORCE_CONSUMER_KEY = "3MVG9VmVOCGHKYBQUAbz7d7kk6x2g29kEbyFhTBt7u..yutNvp7evoFyWTm2q4tZfWRdxekrK6fhhwf5BN4Tq";

    public OAuthResponse getOAuthURL(int type, boolean redirect, String host, FeedDefinition dataSource, int redirectType) {
        try {
            OAuthConsumer consumer;
            OAuthProvider provider;

            if (dataSource != null) {
                new FeedStorage().updateDataFeedConfiguration(dataSource);
            }

            if (type == FeedType.LINKEDIN.getType()) {
                consumer = new DefaultOAuthConsumer("pMAaMYgowzMITTDFzMoaIbHsCni3iBZKzz3bEvUYoIHlaSAEv78XoOsmpch9YkLq",
                        "leKpqRVV3M8CMup_x6dY8THBiKT-T4PXSs3cpSVXp0kaMS4AiZYW830yRvH6JU2O");
                provider = new DefaultOAuthProvider(
                        "https://api.linkedin.com/uas/oauth/requestToken", "https://api.linkedin.com/uas/oauth/accessToken",
                        "https://api.linkedin.com/uas/oauth/authorize");
            } else if (type == FeedType.TWITTER.getType()) {
                consumer = new DefaultOAuthConsumer("Kb9mqPL8TlaJB3lZHK8Fpw",
                        "q7W04Nth2vZYOvOfiiLfTZNdE83sPDpI2uGSAtJhKnM");
                provider = new DefaultOAuthProvider(
                        "http://twitter.com/oauth/request_token", "http://twitter.com/oauth/access_token",
                        "http://twitter.com/oauth/authorize");
            } else if (type == FeedType.FRESHBOOKS_COMPOSITE.getType()) {
                consumer = new CommonsHttpOAuthConsumer(FreshbooksCompositeSource.CONSUMER_KEY,
                        FreshbooksCompositeSource.CONSUMER_SECRET);
                consumer.setMessageSigner(new PlainTextMessageSigner());
                provider = new CommonsHttpOAuthProvider(
                        "https://"+host+".freshbooks.com/oauth/oauth_request.php", "https://"+host+".freshbooks.com/oauth/oauth_access.php",
                        "https://"+host+".freshbooks.com/oauth/oauth_authorize.php");
            } else if (type == FeedType.CONSTANT_CONTACT.getType()) {
                consumer = new DefaultOAuthConsumer("cec7e39c-25fc-43e6-a423-bf02de492d87", "ee72ddd074804402966863aad91b9687");
                provider = new DefaultOAuthProvider(
                        "https://oauth.constantcontact.com/ws/oauth/request_token", "https://oauth.constantcontact.com/ws/oauth/access_token",
                        "https://oauth.constantcontact.com/ws/oauth/confirm_access");
            } else if (type == FeedType.GOOGLE.getType()) {
                consumer = new DefaultOAuthConsumer("www.easy-insight.com", "OG0zlkZFPIe7JdHfLB8qXXYv");
                consumer.setMessageSigner(new HmacSha1MessageSigner());
                String scope = "https://spreadsheets.google.com/feeds/";
                provider = new DefaultOAuthProvider(
                        "https://www.google.com/accounts/OAuthGetRequestToken?scope=" + URLEncoder.encode(scope, "utf-8"), "https://www.google.com/accounts/OAuthGetAccessToken",
                        "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default");
            } else if (type == FeedType.GOOGLE_ANALYTICS.getType()) {
                consumer = new DefaultOAuthConsumer("www.easy-insight.com", "OG0zlkZFPIe7JdHfLB8qXXYv");
                consumer.setMessageSigner(new HmacSha1MessageSigner());
                String scope = "https://www.google.com/analytics/feeds/";
                provider = new DefaultOAuthProvider(
                        "https://www.google.com/accounts/OAuthGetRequestToken?scope=" + URLEncoder.encode(scope, "utf-8"), "https://www.google.com/accounts/OAuthGetAccessToken",
                        "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default");
            } else if (type == FeedType.SALESFORCE.getType()) {
                consumer = new DefaultOAuthConsumer(SALESFORCE_CONSUMER_KEY,
                        "5028271817562655674");
                provider = new DefaultOAuthProvider(
                        "https://login.salesforce.com" + "/_nc_external/system/security/oauth/RequestTokenHandler",
                        "https://login.salesforce.com" + "/_nc_external/system/security/oauth/AccessTokenHandler",
                        "https://login.salesforce.com" + "/setup/secur/RemoteAccessAuthorizationPage.apexp");
            } else {
                throw new RuntimeException();
            }
            FlexContext.getHttpRequest().getSession().setAttribute("oauthConsumer", consumer);
            FlexContext.getHttpRequest().getSession().setAttribute("oauthProvider", provider);

            String requestToken;
            if (redirect) {
                if (ConfigLoader.instance().isProduction()) {
                    requestToken = provider.retrieveRequestToken(consumer, "https://localhost/app/oauth?redirectTarget="+redirectType+"&dataSourceID=" + dataSource.getApiKey());
                    //requestToken = provider.retrieveRequestToken(consumer, "https://www.easy-insight.com/app/oauth?redirectTarget="+redirectType+"&dataSourceID=" + dataSource.getApiKey());
                } else {
                    requestToken = provider.retrieveRequestToken(consumer, "https://localhost/app/oauth?redirectTarget="+redirectType+"&dataSourceID=" + dataSource.getApiKey());
                    //requestToken = provider.retrieveRequestToken(consumer, "https://staging.easy-insight.com/app/oauth?redirectTarget="+redirectType+"&dataSourceID=" + dataSource.getApiKey());
                }
            } else {
                requestToken = provider.retrieveRequestToken(consumer, OAuth.OUT_OF_BAND);
            }

            // Salesforce requires that the request passes along your oauth_consumer_key
            if (type == FeedType.SALESFORCE.getType()) {
                requestToken = requestToken + "&oauth_consumer_key=" + SALESFORCE_CONSUMER_KEY;
            }
            return new OAuthResponse(requestToken, true);
        } catch (OAuthCommunicationException oauthException) {
            if (oauthException.getMessage().indexOf("302") != -1) {
                return new OAuthResponse(false, OAuthResponse.BAD_HOST);
            } else {
                return new OAuthResponse(false, OAuthResponse.OTHER_OAUTH_PROBLEM);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<TokenSpecification> getTokenSpecifications() {
        List<TokenSpecification> tokenSpecs = new ArrayList<TokenSpecification>();
        TokenStorage tokenStorage = new TokenStorage();
        TokenSpecification gDocsSpec = new TokenSpecification();
        gDocsSpec.setName("Google Spreadsheets");
        gDocsSpec.setType(TokenStorage.GOOGLE_DOCS_TOKEN);
        Token gDocsToken = tokenStorage.getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
        gDocsSpec.setDefined(gDocsToken != null);
        TokenSpecification gAnalyticsSpec = new TokenSpecification();
        gAnalyticsSpec.setName("Google Analytics");
        gAnalyticsSpec.setType(TokenStorage.GOOGLE_ANALYTICS_TOKEN);
        Token gAnalyticsToken = tokenStorage.getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_ANALYTICS_TOKEN);
        gAnalyticsSpec.setDefined(gAnalyticsToken != null);
        gDocsSpec.setUrlToConfigure(getAuthSubURL(TokenStorage.GOOGLE_DOCS_TOKEN, 0));
        gAnalyticsSpec.setUrlToConfigure(getAuthSubURL(TokenStorage.GOOGLE_ANALYTICS_TOKEN, 0));
        tokenSpecs.add(gDocsSpec);
        tokenSpecs.add(gAnalyticsSpec);
        return tokenSpecs;
    }

    public void deleteToken(int type) {
        try {
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type);
            if (token != null) {
                new TokenStorage().deleteToken(token);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String setToken(int type, String sessionToken) {
        /*System.out.println("got URL " + url);
        String queryString = url.substring(url.indexOf("?"));*/

        Token tokenObject = new Token();
        tokenObject.setUserID(SecurityUtil.getUserID());
        tokenObject.setTokenType(type);
        tokenObject.setTokenValue(sessionToken);
        new TokenStorage().saveToken(tokenObject, 0);
        return null;
    }

    public boolean isTokenEstablished(int type) {
        try {
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), type);
            return token != null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String getAuthSubURL(int type, long solution) {
        try {
            String nextURL;
            if (ConfigLoader.instance().isProduction()) {
                nextURL = "https://www.easy-insight.com/app/TokenRedirect?sourceType=" + type + "&refSolutionID=" + solution;
            } else {
                nextURL = "https://staging.easy-insight.com/app/TokenRedirect?sourceType=" + type + "&refSolutionID=" + solution;
            }
            FeedType feedType = new FeedType(type);
            String scope;
            if (feedType.equals(FeedType.GOOGLE_ANALYTICS)) {
                scope = "https://www.google.com/analytics/feeds/";
            } else if (feedType.equals(FeedType.GOOGLE)) {
                scope = "http://spreadsheets.google.com/feeds/";
            } else {
                throw new RuntimeException("Unknown type for authorization " + type);
            }
            return AuthSubUtil.getRequestUrl(nextURL, scope, true, true);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public String test() {
        try {
            SpreadsheetService spreadsheetService = new SpreadsheetService("easyinsight-eidocs-1");
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.GOOGLE_DOCS_TOKEN);
            if (token != null) {
                spreadsheetService.setAuthSubToken(token.getTokenValue(), Utility.getPrivateKey());
                SpreadsheetFeed spreadsheetFeed;
                try {
                    URL feedUrl = new URL("http://spreadsheets.google.com/feeds/spreadsheets/private/full");
                    spreadsheetFeed = spreadsheetService.getFeed(feedUrl, SpreadsheetFeed.class);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                int entrySize = spreadsheetFeed.getEntries().size();
                return String.valueOf(entrySize);
            } else {
                return "Couldn't get in";
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

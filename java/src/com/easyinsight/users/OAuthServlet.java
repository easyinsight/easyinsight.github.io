package com.easyinsight.users;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: May 7, 2010
 * Time: 12:27:16 AM
 */
public class OAuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            String code = req.getParameter("code");
            /*if(code != null) {

                String idUrl = response.getParam("id");
                //HttpGet httpRequest = new HttpGet(instanceUrl + "/services/data/v20.0/query/?q=SELECT+name+from+Account");
                HttpGet httpRequest = new HttpGet(idUrl);
                httpRequest.setHeader("Accept", "application/xml");
                httpRequest.setHeader("Content-Type", "application/xml");
                httpRequest.setHeader("Authorization", "OAuth " + token);


                org.apache.http.client.HttpClient cc = new DefaultHttpClient();
                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String string = cc.execute(httpRequest, responseHandler);
                System.out.println(string);
                    return;
                }
*/
            String verifier = req.getParameter("oauth_verifier");
            int redirectType;
            String dataSourceID = req.getParameter("dataSourceID");
            if (req.getParameter("redirectTarget") != null && Integer.parseInt(req.getParameter("redirectTarget")) == TokenService.USER_SOURCE) {
                redirectType = Integer.parseInt(req.getParameter("redirectTarget"));
            } else if (dataSourceID == null) {
                dataSourceID = (String) req.getSession().getAttribute("dataSourceID");
                redirectType = (Integer) req.getSession().getAttribute("redirectTarget");
            } else {
                redirectType = Integer.parseInt(req.getParameter("redirectTarget"));
            }

            if (redirectType == TokenService.USER_SOURCE) {
                OAuthConsumer consumer = (OAuthConsumer) req.getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) req.getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, verifier.trim());
                String secretKey = consumer.getTokenSecret();
                String token = consumer.getToken();

                Session session = Database.instance().createSession();
                Transaction t = session.beginTransaction();
                try {
                    long accountId = (Long) req.getSession().getAttribute("accountID");
                    Account account = (Account) session.get(Account.class, accountId);
                    account.setGoogleSecretToken(secretKey);
                    account.setGoogleToken(token);
                    t.commit();
                } catch (Exception e) {
                    t.rollback();
                    throw new RuntimeException(e);
                } finally {
                    session.close();
                }
                String redirectURL;
                if(ConfigLoader.instance().isProduction()) {
                    redirectURL = "https://www.easy-insight.com";
                } else {
                    redirectURL = "https://staging.easy-insight.com";
                }

                redirectURL = redirectURL + "/app/googleAppsUserList.jsp";

                resp.sendRedirect(redirectURL);
            } else {

                EIConnection conn = Database.instance().getConnection();
                try {
                    conn.setAutoCommit(false);
                    PreparedStatement idStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
                    idStmt.setString(1, dataSourceID);
                    ResultSet rs = idStmt.executeQuery();

                    if(!rs.next()) {
                        resp.sendError(404);
                        return;
                    }
                    long id = rs.getLong(1);
                    FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(id, conn);
                    IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;

                    dataSource.exchangeTokens(conn, req, verifier);
                    //feedDefinition.setVisible(true);
                    new FeedStorage().updateDataFeedConfiguration(feedDefinition, conn);
                    FeedRegistry.instance().flushCache(feedDefinition.getDataFeedID());
                    String redirectURL;
                    if (redirectType == TokenService.CONNECTION_SETUP) {
                        if (ConfigLoader.instance().isProduction()) {
                            redirectURL = "https://www.easy-insight.com/app/#connectionConfig=" + feedDefinition.getApiKey();
                        } else {
                            redirectURL = "https://staging.easy-insight.com/app/#connectionConfig=" + feedDefinition.getApiKey();
                        }
                    } else if (redirectType == TokenService.HTML_SETUP) {
                        redirectURL = RedirectUtil.getURL(req, "/app/html/dataSources/"+ feedDefinition.getApiKey() + "/createConnection");
                    } else {
                        redirectURL = "https://www.easy-insight.com/app/";
                    }
                    conn.commit();
                    resp.sendRedirect(redirectURL);
                } catch (Exception e) {
                    LogClass.error(e);
                    conn.rollback();
                } finally {
                    conn.setAutoCommit(true);
                    Database.closeConnection(conn);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            resp.sendError(500);
        }
    }
}

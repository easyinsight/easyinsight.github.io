package com.easyinsight.users;

import com.easyinsight.config.ConfigLoader;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.logging.LogClass;

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
            String verifier = req.getParameter("oauth_verifier");
            String dataSourceID = req.getParameter("dataSourceID");
            int redirectType = Integer.parseInt(req.getParameter("redirectTarget"));

            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);
                PreparedStatement idStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM DATA_FEED WHERE API_KEY = ?");
                idStmt.setString(1, dataSourceID);
                ResultSet rs = idStmt.executeQuery();
                rs.next();
                long id = rs.getLong(1);
                FeedDefinition feedDefinition = new FeedStorage().getFeedDefinitionData(id, conn);
                IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition) feedDefinition;

                dataSource.exchangeTokens(conn, req, verifier);
                feedDefinition.setVisible(true);
                new FeedStorage().updateDataFeedConfiguration(feedDefinition, conn);
                FeedRegistry.instance().flushCache(feedDefinition.getDataFeedID());
                String redirectURL;
                if (redirectType == TokenService.CONNECTION_SETUP) {
                    if (ConfigLoader.instance().isProduction()) {
                        redirectURL = "https://www.easy-insight.com/app/#connectionConfig="+feedDefinition.getApiKey();
                    } else {
                        redirectURL = "https://staging.easy-insight.com/app/#connectionConfig="+feedDefinition.getApiKey();
                    }
                } else {
                    if (ConfigLoader.instance().isProduction()) {
                        redirectURL = "https://www.easy-insight.com/app/";
                    } else {
                        redirectURL = "https://staging.easy-insight.com/app/";
                    }
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
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}

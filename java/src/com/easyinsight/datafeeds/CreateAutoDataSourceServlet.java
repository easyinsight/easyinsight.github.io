package com.easyinsight.datafeeds;

import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.util.RandomTextGenerator;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 9/23/14
 * Time: 2:10 PM
 */
public class CreateAutoDataSourceServlet extends HttpServlet {

    public static final String CREATE_COMPOSITE = "create_composite";
    public static final String CREATE_DASHBOARD = "create_dashboard";
    public static final String ADD_TO_COMPOSITE = "add_to_composite";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        int responseCode;
        String responseURL;

        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            AutoDataSourceAnalysis autoDataSourceAnalysis = new AutoDataSourceAnalysis(action).invoke();
            responseURL = autoDataSourceAnalysis.getResponseURL();
            responseCode = autoDataSourceAnalysis.getResponseCode();
            JSONObject jo = new JSONObject();
            if (responseURL != null) {
                jo.put("url", responseURL);
            }
            ResponseInfo responseInfo = new ResponseInfo(responseCode, jo.toString());
            resp.setContentType("application/json");
            resp.setStatus(responseInfo.getCode());
            resp.getOutputStream().write(responseInfo.getResponseBody().getBytes());
            resp.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }

    public static class AutoDataSourceAnalysis {
        private String action;
        private String responseURL;
        private int responseCode;
        private long responseDashboardID;

        public AutoDataSourceAnalysis(String action) {
            this.action = action;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getResponseURL() {
            return responseURL;
        }

        public long getResponseDashboardID() {
            return responseDashboardID;
        }

        public AutoDataSourceAnalysis invoke() throws Exception {
            List<DataSourceDescriptor> dataSources = new FeedService().searchForSubscribedFeeds();

            Set<Integer> types = new HashSet<>();
            Set<Long> existingIDs = new HashSet<>();
            List<DataSourceDescriptor> compositeSources = new ArrayList<>();
            DataSourceDescriptor autoDataSourceDescriptor = null;
            for (DataSourceDescriptor dataSource : dataSources) {
                if (new DataSourceTypeRegistry().billingInfoForType(FeedType.valueOf(dataSource.getDataSourceType())) == ConnectionBillingType.SMALL_BIZ && !types.contains(dataSource.getDataSourceType())) {
                    existingIDs.add(dataSource.getId());
                    types.add(dataSource.getType());
                } else if (dataSource.getDataSourceType() == FeedType.COMPOSITE.getType()) {
                    compositeSources.add(dataSource);
                    if (dataSource.isAutoCombined()) {
                        autoDataSourceDescriptor = dataSource;
                    }
                }
            }

            if (CREATE_COMPOSITE.equals(action)) {
                if (autoDataSourceDescriptor != null) {
                    responseCode = ResponseInfo.BAD_REQUEST;
                } else {
                    CompositeFeedDefinition compositeFeedDefinition = new CompositeFeedDefinition();
                    compositeFeedDefinition.setFeedName("Master Data Source");
                    compositeFeedDefinition.setAutoCombined(true);
                    List<CompositeFeedNode> nodes = new ArrayList<>();
                    for (Long existingID : existingIDs) {
                        CompositeFeedNode node = new CompositeFeedNode();
                        node.setDataFeedID(existingID);
                        nodes.add(node);
                    }
                    compositeFeedDefinition.setCompositeFeedNodes(nodes);
                    compositeFeedDefinition.setAccountVisible(true);

                    compositeFeedDefinition.setConnections(new ArrayList<>());
                    compositeFeedDefinition.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        compositeFeedDefinition.populateFields(conn);
                        compositeFeedDefinition.setApiKey(RandomTextGenerator.generateText(12));
                        long feedID = new FeedStorage().addFeedDefinitionData(compositeFeedDefinition, conn);
                        DataStorage.liveDataSource(feedID, conn, compositeFeedDefinition.getFeedType().getType());
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }

                    conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        Dashboard dashboard = new AutoComposite(compositeFeedDefinition.getDataFeedID(), conn).doSomething();
                        responseDashboardID = dashboard.getId();
                        responseURL = "/app/html/dashboard/" + dashboard.getUrlKey();
                        responseCode = ResponseInfo.ALL_GOOD;
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }
                }
            } else if (CREATE_DASHBOARD.equals(action)) {
                if (autoDataSourceDescriptor == null) {
                    // error
                    responseCode = ResponseInfo.BAD_REQUEST;
                } else {
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        Dashboard dashboard = new AutoComposite(autoDataSourceDescriptor.getId(), conn).doSomething();
                        responseURL = "/app/html/dashboard/" + dashboard.getUrlKey();
                        responseDashboardID = dashboard.getId();
                        responseCode = ResponseInfo.ALL_GOOD;
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }
                }
            } else if (ADD_TO_COMPOSITE.equals(action)) {
                if (autoDataSourceDescriptor == null) {
                    // error
                    responseCode = ResponseInfo.BAD_REQUEST;
                } else {
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        PreparedStatement ps = conn.prepareStatement("SELECT COMPOSITE_NODE.DATA_FEED_ID FROM COMPOSITE_FEED, COMPOSITE_NODE WHERE " +
                                "COMPOSITE_NODE.COMPOSITE_FEED_ID = COMPOSITE_FEED.COMPOSITE_FEED_ID AND COMPOSITE_FEED.DATA_FEED_ID = ?");
                        ps.setLong(1, autoDataSourceDescriptor.getId());
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            long existingID = rs.getLong(1);
                            existingIDs.remove(existingID);
                        }
                        if (existingIDs.size() == 0) {
                            // error
                            responseCode = ResponseInfo.BAD_REQUEST;
                        } else {
                            CompositeFeedDefinition dataSource = (CompositeFeedDefinition) new FeedStorage().getFeedDefinitionData(autoDataSourceDescriptor.getId(), conn);
                            for (Long existingID : existingIDs) {
                                CompositeFeedNode node = new CompositeFeedNode(existingID, 0, 0, "", 0, 0);
                                dataSource.getCompositeFeedNodes().add(node);
                            }
                            new DataSourceInternalService().updateFeedDefinition(dataSource, conn);
                            Dashboard dashboard = null;
                            for (Long id : existingIDs) {
                                CompositeFeedNode c = new CompositeFeedNode();
                                c.setDataFeedID(id);
                                dashboard = new AutoComposite(autoDataSourceDescriptor.getId(), conn).attach(c);
                            }
                            responseURL = "/app/html/dashboard/" + dashboard.getUrlKey();
                            responseDashboardID = dashboard.getId();
                            responseCode = ResponseInfo.ALL_GOOD;
                        }
                    } catch (Exception e) {
                        conn.rollback();
                        throw e;
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }
                }
            } else {
                responseCode = ResponseInfo.BAD_REQUEST;
            }
            return this;
        }
    }
}

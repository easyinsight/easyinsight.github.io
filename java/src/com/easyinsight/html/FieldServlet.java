package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 4/15/14
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class FieldServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportIDString = req.getParameter("dataSourceID");
        if (req.getSession().getAttribute("userID") != null) {
            SecurityUtil.populateThreadLocalFromSession(req);
        }
        FeedResponse insightResponse = new FeedService().openFeedIfPossible(reportIDString);
        long reportID;
        if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
            reportID = insightResponse.getFeedDescriptor().getId();
        } else {
            throw new com.easyinsight.security.SecurityException();
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            Feed f = FeedRegistry.instance().getFeed(reportID);
            List<FeedNode> fields = f.getFieldHierarchy();
            JSONObject jo = new JSONObject();
            JSONArray folders = new JSONArray();
            JSONArray analysisItems = new JSONArray();
            jo.put("folders", folders);
            jo.put("analysis_items", analysisItems);
            ExportMetadata md = ExportService.createExportMetadata(SecurityUtil.getAccountID(), conn, new InsightRequestMetadata());

            for(FeedNode ff : fields) {
                if(ff instanceof FolderNode) {
                    folders.put(((FolderNode) ff).getFolder().toJSON(md));
                } else if(ff instanceof AnalysisItemNode) {
                    analysisItems.put(((AnalysisItemNode) ff).getAnalysisItem().toJSON(md));
                }
            }
            resp.setContentType("application/json");
            resp.getOutputStream().write(jo.toString().getBytes());
            resp.getOutputStream().flush();
            resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
            resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
            resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

    }
}

package com.easyinsight.html;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.PostInstallSteps;
import com.easyinsight.solutions.SolutionKPIData;
import com.easyinsight.solutions.SolutionService;
import com.easyinsight.userupload.CredentialsResponse;
import com.easyinsight.userupload.UserUploadService;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 6/1/12
 * Time: 10:47 AM
 */
public class ConnectionInstalledServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            String dataSourceKey = req.getParameter("dataSourceID");
            long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
            FeedDefinition dataSource = new FeedService().getFeedDefinition(dataSourceID);
            String endURL;
            /*if (dataSource.rebuildFieldWindow()) {
                endURL = RedirectUtil.getURL(req, "/app/html/fieldSetup.jsp?dataSourceID=" + dataSourceKey);
            } else {*/
                SolutionKPIData solutionKPIData = new SolutionKPIData();
                solutionKPIData.setDataSourceID(dataSourceID);
                PostInstallSteps steps = new SolutionService().addKPIData(solutionKPIData);
                if (steps.getResult() != null) {
                    endURL = RedirectUtil.getURL(req, "/app/html/dashboard/" + steps.getResult().getUrlKey());
                } else {
                    endURL = RedirectUtil.getURL(req, "/a/data_sources/" + dataSource.getApiKey());
                }
            //}
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("url", endURL);
            response.setContentType("application/json");
            response.getOutputStream().write(jsonObject.toString().getBytes());
            response.getOutputStream().flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}

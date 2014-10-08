package com.easyinsight.html;

import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.export.DailyScheduleType;
import com.easyinsight.export.DataSourceRefreshActivity;
import com.easyinsight.logging.LogClass;
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
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 6/1/12
 * Time: 10:47 AM
 */
public class ConnectionInstalledServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String dataSourceKey = req.getParameter("dataSourceID");
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            int offset = Integer.parseInt(req.getParameter("utcOffset"));
            long dataSourceID = new FeedStorage().dataSourceIDForDataSource(dataSourceKey);
            FeedDefinition dataSource = new FeedService().getFeedDefinition(dataSourceID);
            String endURL;
            /*if (dataSource.rebuildFieldWindow()) {
                endURL = RedirectUtil.getURL(req, "/app/html/fieldSetup.jsp?dataSourceID=" + dataSourceKey);
            } else {*/
            SolutionKPIData solutionKPIData = new SolutionKPIData();
            solutionKPIData.setDataSourceID(dataSourceID);
                /*
                var activity:DataSourceRefreshActivity = new DataSourceRefreshActivity();
            activity.dataSourceID = _dataSourceDefinition.dataFeedID;
            activity.dataSourceName = _dataSourceDefinition.feedName;
            var schedule:DailyScheduleType = new DailyScheduleType();
            var morningOrEvening:int = int(Math.random() * 2);
            if (morningOrEvening == 0) {
                schedule.hour = int(Math.random() * 6);
            } else {
                schedule.hour = int(Math.random() * 6) + 18;
            }
            schedule.minute = int(Math.random() * 60);
            activity.scheduleType = schedule;
            kpiData.utcOffset = new Date().getTimezoneOffset();
            kpiData.activity = activity;
                 */
            DataSourceRefreshActivity dataSourceRefreshActivity = new DataSourceRefreshActivity();
            dataSourceRefreshActivity.setDataSourceID(dataSourceID);
            dataSourceRefreshActivity.setDataSourceName(dataSource.getFeedName());
            DailyScheduleType dailyScheduleType = new DailyScheduleType();
            dailyScheduleType.setTimeOffset(offset);
            dailyScheduleType.setHour((int) (Math.random() * 4 + 1));
            dailyScheduleType.setMinute((int) (Math.random() * 58 + 1));
            dataSourceRefreshActivity.setScheduleType(dailyScheduleType);
            solutionKPIData.setActivity(dataSourceRefreshActivity);
            solutionKPIData.setUtcOffset(offset);
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
            LogClass.error(e);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("url", "/a/data_sources/"  + dataSourceKey);
            } catch (JSONException e1) {
                throw new RuntimeException(e1);
            }
            response.setContentType("application/json");
            response.getOutputStream().write(jsonObject.toString().getBytes());
            response.getOutputStream().flush();
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}

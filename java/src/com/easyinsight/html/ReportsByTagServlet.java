package com.easyinsight.html;

import com.easyinsight.analysis.AnalysisService;
import com.easyinsight.analysis.ReportResults;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/25/13
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsByTagServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(request);
        try {
            String tag = request.getParameter("tagName");
            List<String> stringList = new ArrayList<String>();
//            stringList.add(tag);
            ReportResults results = new AnalysisService().getReportsWithTags(stringList);
            JSONObject jo = new JSONObject();
            JSONArray ja = new JSONArray();
            try {
                jo.put("reports", ja);
                for(InsightDescriptor id : results.getReports()) {
                    JSONObject reportObject = new JSONObject();
                    reportObject.put("name", id.getName());
                    reportObject.put("id", id.getUrlKey());
                    ja.put(reportObject);
                }
            } catch (JSONException e) {
                LogClass.error(e);
            }

            response.setContentType("application/json");
            response.getOutputStream().write(jo.toString().getBytes());
            response.getOutputStream().flush();
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}

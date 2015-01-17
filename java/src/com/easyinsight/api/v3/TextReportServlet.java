package com.easyinsight.api.v3;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSTextDefinition;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: jamesboe
 * Date: 11/28/11
 * Time: 3:27 PM
 */
@WebServlet(value = "/TextReport", asyncSupported = true)
public class TextReportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String reportName = request.getParameter("text");
        System.out.println(request.getParameter("token"));
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT analysis.analysis_id FROM analysis, user_to_analysis WHERE analysis.title = ? AND " +
                    "analysis.analysis_id = user_to_analysis.analysis_id AND user_to_analysis.user_id = ?");
            ps.setString(1, reportName);
            ps.setLong(2, 1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long reportID = rs.getLong(1);
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                EmbeddedTextDataResults results = new DataService().getEmbeddedTextResults(reportID, 0, new ArrayList<>(), insightRequestMetadata, new ArrayList<>());
                String text = results.getText();
                HttpClient client = new HttpClient();
                PostMethod postMethod = new PostMethod("https://hooks.slack.com/services/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");
                JSONObject jo = new JSONObject();
                jo.put("text", text);
                String content = jo.toString();
                StringRequestEntity entity = new StringRequestEntity(content, "application/json", "UTF-8");
                postMethod.setRequestEntity(entity);
                client.executeMethod(postMethod);
                String string = postMethod.getResponseBodyAsString();
                System.out.println(string);
                //resp.getOutputStream().write(text.getBytes());
                resp.getOutputStream().flush();
            } else {
                //resp.getOutputStream().write("Report not found.".getBytes());
                resp.getOutputStream().flush();
            }
        } catch (Exception e) {
            resp.getOutputStream().write("Server error.".getBytes());
            resp.getOutputStream().flush();
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void main(String[] args) throws IOException {
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod("https://hooks.slack.com/services/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");
        JSONObject jo = new JSONObject();
        jo.put("text", "Hrm.");
        String content = jo.toString();
        System.out.println(content);
        StringRequestEntity entity = new StringRequestEntity(content, "application/json", "UTF-8");
        postMethod.setRequestEntity(entity);
        client.executeMethod(postMethod);
        String string = postMethod.getResponseBodyAsString();
        System.out.println(string);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String reportName = request.getParameter("text");
        System.out.println(request.getParameter("token"));
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT analysis.analysis_id FROM analysis, user_to_analysis WHERE analysis.title = ? AND " +
                    "analysis.analysis_id = user_to_analysis.analysis_id AND user_to_analysis.user_id = ?");
            ps.setString(1, reportName);
            ps.setLong(2, 1);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                long reportID = rs.getLong(1);
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                EmbeddedTextDataResults results = new DataService().getEmbeddedTextResults(reportID, 0, new ArrayList<>(), insightRequestMetadata, new ArrayList<>());
                String text = results.getText();
                HttpClient client = new HttpClient();
                PostMethod postMethod = new PostMethod("https://hooks.slack.com/services/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");
                JSONObject jo = new JSONObject();
                jo.put("text", text);
                String content = jo.toString();
                StringRequestEntity entity = new StringRequestEntity(content, "application/json", "UTF-8");
                postMethod.setRequestEntity(entity);
                client.executeMethod(postMethod);
                String string = postMethod.getResponseBodyAsString();
                System.out.println(string);
                resp.getOutputStream().flush();
            } else {
                resp.getOutputStream().write("Report not found.".getBytes());
                resp.getOutputStream().flush();
            }
        } catch (Exception e) {
            resp.getOutputStream().write("Server error.".getBytes());
            resp.getOutputStream().flush();
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void populateFiltersFromRequest(HttpServletRequest request, WSAnalysisDefinition report) throws ParseException {
        for (FilterDefinition filter : report.getFilterDefinitions()) {
            if (filter.getFilterName() != null && !"".equals(filter.getFilterName())) {
                if (filter instanceof FilterValueDefinition) {
                    FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                    String param = request.getParameter(filter.getFilterName());
                    if (param != null) {
                        filterValueDefinition.setFilteredValues(Arrays.asList((Object) param));
                    }
                } else if (filter instanceof RollingFilterDefinition) {

                } else if (filter instanceof FilterDateRangeDefinition) {
                    FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                    String startParam = request.getParameter(filter.getFilterName() + "_start");
                    String endParam = request.getParameter(filter.getFilterName() + "_end");
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    if (startParam != null) {
                        filterDateRangeDefinition.setStartDate(dateFormat.parse(startParam));
                    }
                    if (endParam != null) {
                        filterDateRangeDefinition.setEndDate(dateFormat.parse(endParam));
                    }
                } else if (filter instanceof FilterPatternDefinition) {
                    FilterPatternDefinition filterPatternDefinition = (FilterPatternDefinition) filter;
                    String param = request.getParameter(filter.getFilterName());
                    if (param != null) {
                        filterPatternDefinition.setPattern(param);
                    }
                }
            }
        }
    }
}

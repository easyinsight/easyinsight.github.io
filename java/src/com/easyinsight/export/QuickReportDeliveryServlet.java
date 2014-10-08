package com.easyinsight.export;

import com.easyinsight.api.v3.JSONServlet;
import com.easyinsight.api.v3.ResponseInfo;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.UserStub;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/24/14
 * Time: 3:32 PM
 */
public class QuickReportDeliveryServlet extends JSONServlet {
    @Override
    protected ResponseInfo processJSON(JSONObject jsonObject, EIConnection conn, HttpServletRequest request) throws Exception {
        int utc = Integer.parseInt(request.getParameter("utc"));
        QuickReportResult quickReportResult = new QuickReportResult(conn, utc).invoke();
        List<InsightDescriptor> reports = quickReportResult.getReports();
        String name = quickReportResult.getName();


        org.json.JSONObject jo = new org.json.JSONObject();
        if (reports.size() > 0) {
            request.getSession().setAttribute("autoScheduleName", name);
            jo.put("url", RedirectUtil.getURL(request, "/app/html/scheduleManagement"));
        }
        return new ResponseInfo(ResponseInfo.ALL_GOOD, jo.toString());
    }

    public static class QuickReportResult {
        private EIConnection conn;
        private int utc;
        private List<InsightDescriptor> reports;
        private String name;
        private String dataSourceName;
        private long dataSourceID;

        public QuickReportResult(EIConnection conn, int utc) {
            this.conn = conn;
            this.utc = utc;
        }

        public QuickReportResult(EIConnection conn, int utc, long dataSourceID, String dataSourceName) {
            this.conn = conn;
            this.utc = utc;
            this.dataSourceID = dataSourceID;
            this.dataSourceName = dataSourceName;
        }

        public List<InsightDescriptor> getReports() {
            return reports;
        }

        public String getName() {
            return name;
        }

        public QuickReportResult invoke() throws Exception {
            PreparedStatement ps;
            if (dataSourceID == 0) {
                ps = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE FROM ANALYSIS, USER_TO_ANALYSIS WHERE ANALYSIS.AUTO_SETUP_DELIVERY = ? AND " +
                        "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.USER_ID = ? AND " +
                        "ANALYSIS.ACCOUNT_VISIBLE = ?");
                ps.setBoolean(1, true);
                ps.setLong(2, SecurityUtil.getUserID());
                ps.setBoolean(3, true);
            } else {
                ps = conn.prepareStatement("SELECT ANALYSIS.ANALYSIS_ID, TITLE FROM ANALYSIS, USER_TO_ANALYSIS WHERE ANALYSIS.AUTO_SETUP_DELIVERY = ? AND " +
                        "ANALYSIS.ANALYSIS_ID = USER_TO_ANALYSIS.ANALYSIS_ID AND USER_TO_ANALYSIS.USER_ID = ? AND " +
                        "ANALYSIS.ACCOUNT_VISIBLE = ? AND ANALYSIS.DATA_FEED_ID = ?");
                ps.setBoolean(1, true);
                ps.setLong(2, SecurityUtil.getUserID());
                ps.setBoolean(3, true);
                ps.setLong(4, dataSourceID);
            }
            ResultSet rs = ps.executeQuery();

            reports = new ArrayList<>();

            while (rs.next()) {
                long reportID = rs.getLong(1);

                String title = rs.getString(2);
                System.out.println("Auto setup of " + reportID + " - " + title);
                InsightDescriptor report = new InsightDescriptor();
                report.setId(reportID);
                report.setName(title);
                reports.add(report);
            }

            ps.close();

            name = null;

            if (reports.size() == 1) {
                name = reports.get(0).getName();
                ReportDelivery reportDelivery = new ReportDelivery();
                reportDelivery.setReportID(reports.get(0).getId());
                reportDelivery.setSubject("Weekly Delivery of " + reports.get(0).getName());
                reportDelivery.setUserID(SecurityUtil.getUserID());
                String body = "This report has been generated for weekly delivery by Easy Insight. You can configure this report's format, delivery, and recipients through the <a href=\"https://www.easy-insight.com/app/html/scheduleManagement\">Scheduling</a> page on your Easy Insight interface.";
                reportDelivery.setBody(body);
                reportDelivery.setHtmlEmail(true);
                reportDelivery.setReportFormat(ReportDelivery.HTML_TABLE);
                reportDelivery.setSendIfNoData(false);
                UserStub myself = new UserStub();
                myself.setUserID(SecurityUtil.getUserID());
                reportDelivery.setUsers(Arrays.asList(myself));
                WeeklyScheduleType weeklyScheduleType = new WeeklyScheduleType();
                weeklyScheduleType.setDayOfWeek(Calendar.MONDAY);
                weeklyScheduleType.setHour(6);
                weeklyScheduleType.setMinute(0);
                reportDelivery.setScheduleType(weeklyScheduleType);
                reportDelivery.setCustomFilters(new ArrayList<>());
                new ExportService().addOrUpdateSchedule(reportDelivery, utc, conn);
                reportDelivery.taskNow(conn);
            } else if (reports.size() > 1) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < reports.size(); i++) {
                    InsightDescriptor report = reports.get(i);
                    sb.append(report.getName());
                    if (i == (reports.size() - 2)) {
                        sb.append(",");
                    } else if (i == (reports.size() - 1)) {
                        sb.append(" and ");
                    }
                }
                name = sb.toString();
                GeneralDelivery generalDelivery = new GeneralDelivery();
                if (dataSourceName == null) {
                    generalDelivery.setDeliveryLabel("Weekly Delivery of Reports");
                    generalDelivery.setSubject("Weekly Delivery of Reports");
                } else {
                    generalDelivery.setDeliveryLabel("Weekly Delivery of Reports for " + dataSourceName);
                    generalDelivery.setSubject("Weekly Delivery of Reports for " + dataSourceName);
                }
                generalDelivery.setSenderID(0);
                String body = "This report has been generated for weekly delivery by Easy Insight. You can configure this report's format, delivery, and recipients through the <a href=\"https://www.easy-insight.com/app/html/scheduleManagement\">Scheduling</a> page on your Easy Insight interface.";
                generalDelivery.setBody(body);
                generalDelivery.setHtmlEmail(true);
                UserStub myself = new UserStub();
                myself.setUserID(SecurityUtil.getUserID());
                generalDelivery.setUsers(Arrays.asList(myself));
                WeeklyScheduleType weeklyScheduleType = new WeeklyScheduleType();
                weeklyScheduleType.setDayOfWeek(Calendar.MONDAY);
                weeklyScheduleType.setHour(6);
                weeklyScheduleType.setMinute(0);
                generalDelivery.setScheduleType(weeklyScheduleType);

                List<DeliveryInfo> deliveryInfos = new ArrayList<>();
                for (InsightDescriptor report : reports) {
                    DeliveryInfo deliveryInfo = new DeliveryInfo();
                    deliveryInfo.setId(report.getId());
                    deliveryInfo.setSendIfNoData(false);
                    deliveryInfo.setFormat(ReportDelivery.HTML_TABLE);
                    deliveryInfo.setType(DeliveryInfo.REPORT);
                    deliveryInfo.setFilters(new ArrayList<>());
                    deliveryInfos.add(deliveryInfo);
                }

                generalDelivery.setDeliveryInfos(deliveryInfos);

                new ExportService().addOrUpdateSchedule(generalDelivery, utc, conn);
                generalDelivery.taskNow(conn);
            }



            return this;
        }
    }
}

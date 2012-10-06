package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * User: jamesboe
 * Date: 5/31/12
 * Time: 11:03 AM
 */
public class HtmlServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportIDString = req.getParameter("reportID");
        if (req.getSession().getAttribute("userID") != null) {
            SecurityUtil.populateThreadLocalFromSession(req);
        }
        try {
            InsightResponse insightResponse = new AnalysisService().openAnalysisIfPossible(reportIDString);
            long reportID;
            if (insightResponse.getStatus() == InsightResponse.SUCCESS) {
                reportID = insightResponse.getInsightDescriptor().getId();
            } else {
                throw new com.easyinsight.security.SecurityException();
            }
            EIConnection conn = Database.instance().getConnection();
            try {
                WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(reportID);

                // we aren't *just* iterating the report's filters, we're also iterating the dashboard containing the report
                // and retrieving information thereof

                List<FilterDefinition> filters = report.getFilterDefinitions();

                List<FilterDefinition> drillthroughFilters = new ArrayList<FilterDefinition>();

                String drillThroughKey = req.getParameter("drillThroughKey");
                if (drillThroughKey != null) {
                    PreparedStatement queryStmt = conn.prepareStatement("SELECT drillthrough_save_id FROM drillthrough_save WHERE url_key = ?");
                    queryStmt.setString(1, drillThroughKey);
                    ResultSet rs = queryStmt.executeQuery();
                    rs.next();
                    long drillthroughSaveID = rs.getLong(1);
                    PreparedStatement filterStmt = conn.prepareStatement("SELECT filter_id from drillthrough_report_save_filter WHERE drillthrough_save_id = ?");
                    filterStmt.setLong(1, drillthroughSaveID);
                    ResultSet filterRS = filterStmt.executeQuery();
                    while (filterRS.next()) {
                        Session hibernateSession = Database.instance().createSession(conn);
                        FilterDefinition filter = (FilterDefinition) hibernateSession.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterRS.getLong(1)).list().get(0);
                        filter.afterLoad();
                        drillthroughFilters.add(filter);
                        hibernateSession.close();
                    }
                }

                filters.addAll(drillthroughFilters);

                String dashboardIDString = req.getParameter("dashboardID");
                if (dashboardIDString != null) {
                    long dashboardID = Long.parseLong(dashboardIDString);
                    Dashboard dashboard = new DashboardService().getDashboard(dashboardID);
                    filters.addAll(dashboard.filtersForReport(reportID));
                }

                for (FilterDefinition filter : filters) {
                    if (filter instanceof FilterValueDefinition) {
                        FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                        String value = req.getParameter("filter" + filter.getFilterID());
                        if (value != null) {
                            if (filterValueDefinition.isSingleValue()) {
                                filterValueDefinition.setFilteredValues(Arrays.asList((Object) value));
                            } else {
                                String[] values = value.split(",");
                                List<Object> valueList = new ArrayList<Object>();
                                Collections.addAll(valueList, values);
                                filterValueDefinition.setFilteredValues(valueList);
                            }
                        }
                    } else if (filter instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                        String value = req.getParameter("filter"+filter.getFilterID());
                        if (value != null) {
                            int filterValue = Integer.parseInt(value);
                            rollingFilterDefinition.setInterval(filterValue);
                            if (filterValue == MaterializedRollingFilterDefinition.CUSTOM) {
                                int direction = Integer.parseInt(req.getParameter("filter" + filter.getFilterID() + "direction"));
                                int customValue = Integer.parseInt(req.getParameter("filter" + filter.getFilterID() + "value"));
                                int interval = Integer.parseInt(req.getParameter("filter" + filter.getFilterID() + "interval"));
                                rollingFilterDefinition.setCustomBeforeOrAfter(direction);
                                rollingFilterDefinition.setCustomIntervalAmount(customValue);
                                rollingFilterDefinition.setCustomIntervalType(interval);
                            }
                        }
                    } else if (filter instanceof AnalysisItemFilterDefinition) {
                        AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                        String value = req.getParameter("filter"+filter.getFilterID());
                        if (value != null) {
                            long fieldID = Long.parseLong(value);
                            for (AnalysisItem item : analysisItemFilterDefinition.getAvailableItems()) {
                                if (item.getAnalysisItemID() == fieldID) {
                                    analysisItemFilterDefinition.setTargetItem(item);
                                    break;
                                }
                            }
                        }
                    } else if (filter instanceof FlatDateFilter) {
                        FlatDateFilter flatDateFilter = (FlatDateFilter) filter;
                        String value = req.getParameter("filter"+filter.getFilterID());
                        if (value != null) {
                            flatDateFilter.setValue(Integer.parseInt(value));
                        }
                    } else if (filter instanceof MultiFlatDateFilter) {
                        MultiFlatDateFilter multiFlatDateFilter = (MultiFlatDateFilter) filter;
                        String startMonthString = req.getParameter("filter" + filter.getFilterID() + "start");
                        String endMonthString = req.getParameter("filter" + filter.getFilterID() + "end");
                        List<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();
                        if (startMonthString != null && endMonthString != null) {
                            int startMonth = Integer.parseInt(startMonthString);
                            int endMonth = Integer.parseInt(endMonthString);
                            for (int i = startMonth; i <= endMonth; i++) {
                                DateLevelWrapper wrapper = new DateLevelWrapper();
                                wrapper.setDateLevel(i);
                                levels.add(wrapper);
                            }
                            multiFlatDateFilter.setLevels(levels);
                        }

                    } else if (filter instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                        String startDate = req.getParameter("filter" + filter.getFilterID() + "start");
                        String endDate = req.getParameter("filter" + filter.getFilterID() + "end");
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        if (startDate != null) {
                            filterDateRangeDefinition.setStartDate(dateFormat.parse(startDate));
                        }
                        if (endDate != null) {
                            filterDateRangeDefinition.setEndDate(dateFormat.parse(endDate));
                        }
                    }
                    String enabledParam = req.getParameter("filter" + filter.getFilterID() + "enabled");
                    if (enabledParam != null) {
                        if ("true".equals(enabledParam)) {
                            filter.setEnabled(true);
                        } else {
                            filter.setEnabled(false);
                        }
                    }
                }
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                if (req.getParameter("timezoneOffset") != null) {
                    int timezoneOffset = Integer.parseInt(req.getParameter("timezoneOffset"));
                    insightRequestMetadata.setUtcOffset(timezoneOffset);
                }
                doStuff(req, resp, insightRequestMetadata, conn, report);
                resp.setHeader("Cache-Control","no-cache"); //HTTP 1.1
                resp.setHeader("Pragma","no-cache"); //HTTP 1.0
                resp.setDateHeader ("Expires", 0); //prevents caching at the proxy server
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } finally {
            if (req.getSession().getAttribute("userID") != null) {
                SecurityUtil.clearThreadLocal();
            }
        }
    }

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception { }
}

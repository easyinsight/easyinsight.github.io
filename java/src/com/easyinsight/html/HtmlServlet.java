package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
        long reportID = Long.parseLong(req.getParameter("reportID"));
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            SecurityUtil.authorizeInsight(reportID);
            EIConnection conn = Database.instance().getConnection();
            try {
                WSAnalysisDefinition report = new AnalysisService().openAnalysisDefinition(reportID);
                for (FilterDefinition filter : report.getFilterDefinitions()) {
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
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
            SecurityUtil.clearThreadLocal();
        }
    }

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception { }
}

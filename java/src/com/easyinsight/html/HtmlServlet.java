package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 5/31/12
 * Time: 11:03 AM
 */
public class HtmlServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reportIDString = req.getParameter("reportID");
        if (req.getSession().getAttribute("userID") != null) {
            SecurityUtil.populateThreadLocalFromSession(req);
        }
        try {

            InputStream is = req.getInputStream();
            JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
            JSONObject filterObject = (JSONObject) parser.parse(is);
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

                boolean logReport = report.isLogReport();

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
                    PreparedStatement filterStmt = conn.prepareStatement("SELECT filter_id FROM drillthrough_report_save_filter WHERE drillthrough_save_id = ?");
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

                for (FilterDefinition filter : flattenFilters(filters)) {
                    JSONObject curFilter = (JSONObject) filterObject.get(String.valueOf(filter.getFilterID()));
                    if (filter instanceof FilterValueDefinition) {
                        FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                        Object value = curFilter != null ? curFilter.get("selected") : null;
                        if (value != null) {
                            if (filterValueDefinition.isSingleValue()) {
                                filterValueDefinition.setFilteredValues(Arrays.asList((Object) value));
                            } else {
                                JSONObject arr = (JSONObject) value;
                                List<Object> valueList = new ArrayList<Object>();
                                for (Object o : arr.keySet()) {
                                    if((Boolean) arr.get(o))
                                        valueList.add(o);
                                }
                                filterValueDefinition.setFilteredValues(valueList);
                            }
                        }
                    } else if (filter instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                        Object obj = curFilter != null ? curFilter.get("interval_type") : null;
                        Integer filterValue = null;
                        if (obj != null && obj instanceof String) {
                            String string = (String) obj;
                            try {
                                filterValue = Integer.parseInt(string);
                            } catch (NumberFormatException e) {
                                // ignore
                            }
                        } else if (obj != null && obj instanceof Integer) {
                            filterValue = (Integer) obj;
                        }

                        if (filterValue != null) {
                            rollingFilterDefinition.setInterval(filterValue);
                            if (filterValue == MaterializedRollingFilterDefinition.CUSTOM) {
                                int direction = getIntFromJSON(curFilter, "direction");
                                int customValue = getIntFromJSON(curFilter, "value");
                                int interval = getIntFromJSON(curFilter, "interval");
                                rollingFilterDefinition.setCustomBeforeOrAfter(direction);
                                rollingFilterDefinition.setCustomIntervalAmount(customValue);
                                rollingFilterDefinition.setCustomIntervalType(interval);
                            }
                        }
                    } else if (filter instanceof AnalysisItemFilterDefinition) {
                        AnalysisItemFilterDefinition analysisItemFilterDefinition = (AnalysisItemFilterDefinition) filter;
                        String value = curFilter != null ? (String) curFilter.get("selected") : null;
                        if (value != null) {
                            long fieldID = Long.valueOf(value);
                            for (AnalysisItem item : analysisItemFilterDefinition.getAvailableItems()) {
                                if (item.getAnalysisItemID() == fieldID) {
                                    analysisItemFilterDefinition.setTargetItem(item);
                                    break;
                                }
                            }
                        }
                    } else if (filter instanceof FlatDateFilter) {
                        FlatDateFilter flatDateFilter = (FlatDateFilter) filter;
                        String value = curFilter != null ? (String) curFilter.get("selected") : null;
                        if (value != null) {
                            flatDateFilter.setValue(Integer.parseInt(value));
                        }
                    } else if (filter instanceof MultiFlatDateFilter) {
                        MultiFlatDateFilter multiFlatDateFilter = (MultiFlatDateFilter) filter;
                        Integer startMonthString = curFilter != null ? (Integer) curFilter.get("start") : null;
                        Integer endMonthString = curFilter != null ? (Integer) curFilter.get("end") : null;
                        List<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();
                        if (startMonthString != null && endMonthString != null) {
                            int startMonth = startMonthString;
                            int endMonth = endMonthString;
                            for (int i = startMonth; i <= endMonth; i++) {
                                DateLevelWrapper wrapper = new DateLevelWrapper();
                                wrapper.setDateLevel(i);
                                levels.add(wrapper);
                            }
                            multiFlatDateFilter.setLevels(levels);
                        }

                    } else if (filter instanceof FilterDateRangeDefinition) {
                        FilterDateRangeDefinition filterDateRangeDefinition = (FilterDateRangeDefinition) filter;
                        String startDate = curFilter != null ? (String) curFilter.get("start") : null;
                        String endDate = curFilter != null ? (String) curFilter.get("end") : null;
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                        if (startDate != null) {
                            filterDateRangeDefinition.setStartDate(dateFormat.parse(startDate));
                        }
                        if (endDate != null) {
                            filterDateRangeDefinition.setEndDate(dateFormat.parse(endDate));
                        }
                    } else if (filter instanceof FilterPatternDefinition) {
                        FilterPatternDefinition filterPatternDefinition = (FilterPatternDefinition) filter;
                        String pattern = curFilter != null ? (String) curFilter.get("pattern") : null;
                        if (pattern != null)
                            filterPatternDefinition.setPattern(pattern);
                    } else if (filter instanceof MultiFieldFilterDefinition) {
                        MultiFieldFilterDefinition multiFieldFilterDefinition = (MultiFieldFilterDefinition) filter;
                        Object value = curFilter != null ? curFilter.get("selected") : null;
                        if (value != null) {
                            JSONObject arr = (JSONObject) value;
                            //List<AnalysisItemSelection> items = new DataService().possibleFields(multiFieldFilterDefinition, report);
                            /*Map<String, AnalysisItemSelection> map = new HashMap<String, AnalysisItemSelection>();
                            for (AnalysisItemSelection handle : items) {
                                map.put(handle.getAnalysisItem().toDisplay(), handle);
                            }*/
                            List<AnalysisItemHandle> selections = new ArrayList<AnalysisItemHandle>();
                            for (Object o : arr.keySet()) {
                                if ("All".equals(o)) {
                                    if ((Boolean) arr.get(o)) {
                                        multiFieldFilterDefinition.setAll(true);
                                    } else {
                                        multiFieldFilterDefinition.setAll(false);
                                    }
                                } else {
                                    AnalysisItemHandle h = new AnalysisItemHandle();
                                    h.setSelected((Boolean) arr.get(o));
                                    h.setName((String) o);
                                    selections.add(h);
                                }
                            }
                            multiFieldFilterDefinition.setSelectedItems(selections);
                        }
                    }


                        Boolean enabledParam = curFilter != null ? (Boolean) curFilter.get("enabled") : null;
                    if (enabledParam != null) {
                        filter.setEnabled(enabledParam);
                    }
                    if (logReport) {
                        LogClass.info("For report " + report.getName() + ", filter " + filter.getFilterID() + " has enabled = " + filter.isEnabled());
                    }
                }
                InsightRequestMetadata insightRequestMetadata = new InsightRequestMetadata();
                if (req.getParameter("timezoneOffset") != null) {
                    int timezoneOffset = Integer.parseInt(req.getParameter("timezoneOffset"));
                    insightRequestMetadata.setUtcOffset(timezoneOffset);
                }
                doStuff(req, resp, insightRequestMetadata, conn, report);
                resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
                resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
                resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (req.getSession().getAttribute("userID") != null) {
                SecurityUtil.clearThreadLocal();
            }
        }
    }

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
                    PreparedStatement filterStmt = conn.prepareStatement("SELECT filter_id FROM drillthrough_report_save_filter WHERE drillthrough_save_id = ?");
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

                for (FilterDefinition filter : flattenFilters(filters)) {
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
                        String value = req.getParameter("filter" + filter.getFilterID());
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
                        String value = req.getParameter("filter" + filter.getFilterID());
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
                        String value = req.getParameter("filter" + filter.getFilterID());
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
                resp.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
                resp.setHeader("Pragma", "no-cache"); //HTTP 1.0
                resp.setDateHeader("Expires", 0); //prevents caching at the proxy server
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

    protected int getIntFromJSON(JSONObject jsonObject, String property) {
        Object result = jsonObject.get(property);
        if (result == null) {
            return 0;
        }
        if (result instanceof String) {
            String string = (String) result;
            try {
                return Integer.parseInt(string);
            } catch (NumberFormatException e) {
                return 0;
            }
        } else if (result instanceof Number) {
            Number number = (Number) result;
            return number.intValue();
        }
        return 0;
    }

    private List<FilterDefinition> flattenFilters(List<FilterDefinition> filters) {
        List<FilterDefinition> newList = new ArrayList<FilterDefinition>();
        for (FilterDefinition f : filters) {
            if (f instanceof OrFilter) {
                newList.addAll(((OrFilter) f).getFilters());
            } else {
                newList.add(f);
            }
        }
        return newList;
    }

    protected void doStuff(HttpServletRequest request, HttpServletResponse response, InsightRequestMetadata insightRequestMetadata,
                           EIConnection conn, WSAnalysisDefinition report) throws Exception {
    }
}

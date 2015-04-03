package com.easyinsight.html;

import com.easyinsight.analysis.*;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dashboard.DashboardInfo;
import com.easyinsight.dashboard.FilterPositionKey;
import com.easyinsight.dashboard.SavedConfiguration;
import com.easyinsight.logging.LogClass;
import net.minidev.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 11/14/13
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class FilterUtils {
    public static void adjustFilters(Collection<FilterDefinition> filters, JSONObject filterObject, String reportName, boolean logReport) throws ParseException {
        for (FilterDefinition filter : flattenFilters(filters)) {
            JSONObject curFilter = (JSONObject) filterObject.get(String.valueOf(filter.getFilterID()));
            adjustFilter(reportName, logReport, filter, curFilter);
        }
    }

    public static void adjustFilter(String reportName, boolean logReport, FilterDefinition filter, JSONObject curFilter) throws ParseException {
        if (filter instanceof FilterValueDefinition) {
            FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
            Object value = curFilter != null ? curFilter.get("selected") : null;
            if (value != null) {
                filterValueDefinition.setPersistedValues(null);
                if (filterValueDefinition.isSingleValue()) {
                    if ("[ No Value ]".equals(value)) {
                        filterValueDefinition.setFilteredValues(Arrays.asList((Object) new EmptyValue()));
                    } else {
                        filterValueDefinition.setFilteredValues(Arrays.asList((Object) value));
                    }
                } else {
                    JSONObject arr = (JSONObject) value;
                    List<Object> valueList = new ArrayList<Object>();
                    for (Object o : arr.keySet()) {
                        if ((Boolean) arr.get(o)) {
                            if ("[ No Value ]".equals(o)) {
                                valueList.add(new EmptyValue());
                            } else {
                                valueList.add(o);
                            }
                        }
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
                //long fieldID = Long.parseLong(value);
                int level = 0;
                if (value.contains("|")) {
                    String[] tokens = value.split("\\|");
                    value = tokens[0];
                    level = Integer.parseInt(tokens[1]);
                }
                List<AnalysisItemSelection> possibles = new DataService().possibleFields(analysisItemFilterDefinition, null, null, null);
                for (AnalysisItemSelection possible : possibles) {
                    if (possible.getAnalysisItem().toDisplay().equals(value) && level == possible.getCustomDateLevel()) {
                        analysisItemFilterDefinition.setTargetItem(possible.getAnalysisItem());
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
            List<DateLevelWrapper> wrappers = new DataService().getMultiDateOptions(multiFlatDateFilter);
            Integer startMonthString = curFilter != null ? (Integer) curFilter.get("start") : null;
            Integer endMonthString = curFilter != null ? (Integer) curFilter.get("end") : null;
            List<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();
            if (startMonthString != null && endMonthString != null) {
                int startMonth = startMonthString;
                int endMonth = endMonthString;
                for (DateLevelWrapper wrapper : wrappers) {
                    if (wrapper.getDateLevel() >= startMonth && wrapper.getDateLevel() <= endMonth) {
                        levels.add(wrapper);
                    }
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
                System.out.println("Using all = " + multiFieldFilterDefinition.isAll());
                multiFieldFilterDefinition.setSelectedItems(selections);
            }
        }


        Boolean enabledParam = curFilter != null ? (Boolean) curFilter.get("enabled") : null;
        if (enabledParam != null) {
            filter.setEnabled(enabledParam);
        }
        if (logReport) {
            LogClass.info("For report " + reportName + ", filter " + filter.getFilterID() + " has enabled = " + filter.isEnabled());
        }
    }

    public static List<FilterDefinition> flattenFilters(Collection<FilterDefinition> filters) {
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

    protected static int getIntFromJSON(JSONObject jsonObject, String property) {
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

    public static void adjustReport(WSAnalysisDefinition report, DashboardInfo positions) throws ReportNotFoundException {
        if(positions == null)
            return;
        if(positions.getReport() != null && positions.getReport().getUrlKey().equals(report.getUrlKey())) {
            Map<String, FilterDefinition> map = positions.getSavedConfiguration().getDashboardStackPositions().getFilterMap();
            for(FilterDefinition f : report.getFilterDefinitions()) {
                FilterPositionKey k = new FilterPositionKey(FilterPositionKey.REPORT, f.getFilterID(), null);
                if(map.containsKey(k.createURLKey())) {
                    f.override(map.get(k.createURLKey()));
                }
            }
        } else {
            throw new ReportNotFoundException("Report not found.");
        }
    }

    public static String toFilterString(Value value) {
        String valueString;
        if (value.type() == Value.NUMBER) {
            int intValue = value.toDouble().intValue();
            double doubleValue = value.toDouble().doubleValue();
            if (intValue == doubleValue) {
                valueString = String.valueOf(intValue);
            } else {
                valueString = value.toHTMLString();
            }
        } else {
            valueString = value.toHTMLString();
        }
        return valueString;
    }
}

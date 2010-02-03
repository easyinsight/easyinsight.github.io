package com.easyinsight.kpi;

import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.analysis.RollingFilterDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.security.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 30, 2010
 * Time: 12:55:30 PM
 */
public class KPIUtil {

    public static KPIUser defaultUser() {
        KPIUser kpiUser = new KPIUser();
        UserStub userStub = new UserStub(SecurityUtil.getUserID(), SecurityUtil.getUserName(), "", "");
        kpiUser.setOwner(true);
        kpiUser.setResponsible(true);
        kpiUser.setFeedConsumer(userStub);
        return kpiUser;
    }

    public static KPI createKPIForDateFilter(String name, String icon, AnalysisMeasure measure, AnalysisDimension date, int dateType,
                                      List<FilterDefinition> otherFilters, int highIsGood) {
        KPI kpi = new KPI();
        kpi.setName(name);
        kpi.setIconImage(icon);
        kpi.setAnalysisMeasure(measure);
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        rollingFilterDefinition.setField(date);
        rollingFilterDefinition.setInterval(dateType);
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        filters.add(rollingFilterDefinition);
        if (otherFilters != null) {
            filters.addAll(otherFilters);
        }
        kpi.setFilters(filters);
        kpi.setHighIsGood(highIsGood);
        return kpi;
    }

    public static KPI createKPIWithFilters(String name, String icon, AnalysisMeasure measure, List<FilterDefinition> filters, int highIsGood) {
        KPI kpi = new KPI();
        kpi.setName(name);
        kpi.setIconImage(icon);
        kpi.setAnalysisMeasure(measure);
        kpi.setFilters(filters);
        kpi.setHighIsGood(highIsGood);
        return kpi;
    }
}

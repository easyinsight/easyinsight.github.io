package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.TrendColumn;
import com.easyinsight.analysis.definitions.WSTrendDefinition;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="trend_report")
public class TrendDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="trend_report_id")
    private long trendReportID;

    @Column(name="filter_name")
    private String filterName;

    @Column (name="day_window")
    private String dayWindow;

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSTrendDefinition trend = new WSTrendDefinition();
        trend.setTrendReportID(trendReportID);
        trend.setFilterName(filterName);
        trend.setDayWindow(Integer.parseInt(dayWindow));
        return trend;
    }

    public String getDayWindow() {
        return dayWindow;
    }

    public void setDayWindow(String dayWindow) {
        this.dayWindow = dayWindow;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public long getTrendReportID() {
        return trendReportID;
    }

    public void setTrendReportID(long trendReportID) {
        this.trendReportID = trendReportID;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        TrendDefinitionState trendState = (TrendDefinitionState) super.clone(allFields);
        trendState.setTrendReportID(0);
        return trendState;
    }
}

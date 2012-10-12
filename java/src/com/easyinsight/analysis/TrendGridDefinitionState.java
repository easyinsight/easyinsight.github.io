package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSTrendDefinition;
import com.easyinsight.analysis.definitions.WSTrendGridDefinition;
import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="trend_grid_report")
public class TrendGridDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="trend_grid_report_id")
    private long trendReportID;

    @Column(name="filter_name")
    private String filterName;

    @Column (name="day_window")
    private String dayWindow;

    @Column (name="sort_index")
    private int sortIndex;

    @Column (name="sort_direction")
    private boolean sortDirection;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("filterName", String.valueOf(filterName)));
        element.addAttribute(new Attribute("dayWindow", String.valueOf(dayWindow)));
        element.addAttribute(new Attribute("sortIndex", String.valueOf(sortIndex)));
        element.addAttribute(new Attribute("sortDirection", String.valueOf(sortDirection)));
        return element;
    }

    public void subclassFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        filterName = element.getAttribute("filterName").getValue();
        dayWindow = element.getAttribute("dayWindow").getValue();
        sortIndex = Integer.parseInt(element.getAttribute("sortIndex").getValue());
        sortDirection = Boolean.parseBoolean(element.getAttribute("sortDirection").getValue());
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSTrendGridDefinition trend = new WSTrendGridDefinition();
        trend.setTrendReportID(trendReportID);
        trend.setFilterName(filterName);
        trend.setDayWindow(Integer.parseInt(dayWindow));
        trend.setSortAscending(sortDirection);
        trend.setSortIndex(sortIndex);
        return trend;
    }

    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public boolean isSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(boolean sortDirection) {
        this.sortDirection = sortDirection;
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
        TrendGridDefinitionState trendState = (TrendGridDefinitionState) super.clone(allFields);
        trendState.setTrendReportID(0);
        return trendState;
    }
}

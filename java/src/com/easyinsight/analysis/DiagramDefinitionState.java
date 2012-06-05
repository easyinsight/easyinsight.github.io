package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSDiagramDefinition;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 10/5/11
 * Time: 2:03 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="diagram_report")
public class DiagramDefinitionState extends AnalysisDefinitionState {
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="diagram_report_id")
    private long diagramReportID;

    @Column(name="filter_name")
    private String filterName;

    @Column (name="day_window")
    private String dayWindow;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("diagramReport");
        element.addAttribute(new Attribute("filterName", String.valueOf(filterName)));
        element.addAttribute(new Attribute("dayWindow", String.valueOf(dayWindow)));
        return element;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "diagram_report_to_diagram_report_link",
            joinColumns = @JoinColumn(name = "diagram_report_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "diagram_report_link_id", nullable = false))
    private List<DiagramLink> links;

    public List<DiagramLink> getLinks() {
        return links;
    }

    public void setLinks(List<DiagramLink> links) {
        this.links = links;
    }

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSDiagramDefinition trend = new WSDiagramDefinition();
        trend.setDiagramReportID(diagramReportID);
        trend.setFilterName(filterName);
        trend.setDayWindow(Integer.parseInt(dayWindow));
        trend.setLinks(links);
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

    public long getDiagramReportID() {
        return diagramReportID;
    }

    public void setDiagramReportID(long diagramReportID) {
        this.diagramReportID = diagramReportID;
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        List<DiagramLink> links = new ArrayList<DiagramLink>();
        for (DiagramLink link : this.links) {
            link.afterLoad();
            links.add(link);
        }
        setLinks(links);
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        for (DiagramLink link : this.links) {
            link.beforeSave(session);
        }
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        DiagramDefinitionState trendState = (DiagramDefinitionState) super.clone(allFields);
        trendState.setDiagramReportID(0);
        List<DiagramLink> targetLinks = new ArrayList<DiagramLink>();
        for (DiagramLink link : this.links) {
            DiagramLink clone = link.clone();
            targetLinks.add(clone);
        }
        trendState.setLinks(targetLinks);
        return trendState;
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) throws CloneNotSupportedException {
        super.updateIDs(replacementMap);
        for (DiagramLink diagramLink : links) {
            diagramLink.updateIDs(replacementMap);
        }
    }
}

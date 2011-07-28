package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSCombinedVerticalListDefinition;
import com.easyinsight.core.PersistableValue;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 27, 2010
 * Time: 4:58:50 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="combined_vertical_list")
public class CombinedVerticalListDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="combined_vertical_list_id")
    private long combinedVerticalListID;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinTable(name = "combined_vertical_list_to_report",
            joinColumns = @JoinColumn(name = "parent_report_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "child_report_id", nullable = false))
    private List<AnalysisDefinition> childReports = new ArrayList<AnalysisDefinition>();

    @Override
    public WSAnalysisDefinition createWSDefinition() {
        WSCombinedVerticalListDefinition gantt = new WSCombinedVerticalListDefinition();
        gantt.setCombinedVerticalListDefinitionID(combinedVerticalListID);
        List<WSAnalysisDefinition> children = new ArrayList<WSAnalysisDefinition>();
        for (AnalysisDefinition child : childReports) {
            children.add(child.createBlazeDefinition());
        }
        gantt.setReports(children);
        return gantt;
    }

    public long getCombinedVerticalListID() {
        return combinedVerticalListID;
    }

    public void setCombinedVerticalListID(long combinedVerticalListID) {
        this.combinedVerticalListID = combinedVerticalListID;
    }

    public List<AnalysisDefinition> getChildReports() {
        return childReports;
    }

    public void setChildReports(List<AnalysisDefinition> childReports) {
        this.childReports = childReports;
    }

    @Override
    public AnalysisDefinitionState clone(List<AnalysisItem> allFields) throws CloneNotSupportedException {
        CombinedVerticalListDefinitionState verticalListDefinitionState = (CombinedVerticalListDefinitionState) super.clone(allFields);
        verticalListDefinitionState.setCombinedVerticalListID(0);
        return verticalListDefinitionState;
    }
}

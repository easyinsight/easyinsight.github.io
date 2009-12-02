package com.easyinsight.analysis;

import com.easyinsight.core.Key;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Mar 31, 2009
 * Time: 10:06:02 AM
 */
@Entity
@Table(name="tree_report")
public class TreeDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="tree_report_id")
    private long treeDefinitionID;

    public long getTreeDefinitionID() {
        return treeDefinitionID;
    }

    public void setTreeDefinitionID(long treeDefinitionID) {
        this.treeDefinitionID = treeDefinitionID;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSTreeDefinition wsTreeDefinition = new WSTreeDefinition();
        wsTreeDefinition.setTreeDefinitionID(treeDefinitionID);
        return wsTreeDefinition;
    }

    @Override
    public AnalysisDefinitionState clone(Map<Key, Key> keyMap, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        TreeDefinitionState state = (TreeDefinitionState) super.clone(keyMap, allFields);
        state.setTreeDefinitionID(0);
        return state;
    }
}
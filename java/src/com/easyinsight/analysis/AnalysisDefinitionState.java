package com.easyinsight.analysis;

import com.easyinsight.core.Key;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * User: James Boe
 * Date: Mar 28, 2009
 * Time: 2:13:33 PM
 */
@Entity
@Table(name="report_state")
@Inheritance(strategy=InheritanceType.JOINED)
public abstract class AnalysisDefinitionState implements Cloneable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="report_state_id")
    private long id;

    public abstract WSAnalysisDefinition createWSDefinition();

    public AnalysisDefinitionState clone(Map<Key, Key> keyMap, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        AnalysisDefinitionState state = (AnalysisDefinitionState) super.clone();
        state.setId(0);
        return state;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void updateIDs(Map<Long, AnalysisItem> replacementMap, Map<Key, Key> keyMap) throws CloneNotSupportedException {

    }

    public Collection<? extends AnalysisDefinition> containedReports(Session session) {
        return new ArrayList<AnalysisDefinition>();
    }

    public void beforeSave(Session session) {

    }

    public void updateReportIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        
    }

    public void afterLoad() {
        
    }
}

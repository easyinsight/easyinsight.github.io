package com.easyinsight.analysis;

import javax.persistence.*;

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

    @Override
    public AnalysisDefinitionState clone() throws CloneNotSupportedException {
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
}

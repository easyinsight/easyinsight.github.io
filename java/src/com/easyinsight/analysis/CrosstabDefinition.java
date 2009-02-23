package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItemFactory;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.*;

import org.hibernate.annotations.Where;
import org.hibernate.annotations.Cascade;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:47:59 PM
 */
@Entity
@Table(name="crosstab_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public class CrosstabDefinition extends AnalysisDefinition {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="crosstab_definition_id")
    private Long crosstabDefinitionID;

    @OneToMany
    @Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="analysis_id", nullable = false)
    @Where(clause = "field_type = 'Column'")
    private Collection<CrosstabColumnField> columns;
    @OneToMany
    @Cascade ({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="analysis_id", nullable = false)
    @Where(clause = "field_type = 'Row'")
    private Collection<CrosstabRowField> rows;
    @OneToMany
    @Cascade ({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="analysis_id", nullable = false)
    @Where(clause = "field_type = 'Measure'")
    private Collection<CrosstabMeasureField> measures;

    public Long getCrosstabDefinitionID() {
        return crosstabDefinitionID;
    }

    public void setCrosstabDefinitionID(Long crosstabDefinitionID) {
        this.crosstabDefinitionID = crosstabDefinitionID;
    }

    public Collection<CrosstabColumnField> getColumns() {
        return columns;
    }

    public void setColumns(Collection<CrosstabColumnField> columns) {
        this.columns = columns;
    }

    public Collection<CrosstabRowField> getRows() {
        return rows;
    }

    public void setRows(Collection<CrosstabRowField> rows) {
        this.rows = rows;
    }

    public Collection<CrosstabMeasureField> getMeasures() {
        return measures;
    }

    public void setMeasures(Collection<CrosstabMeasureField> measures) {
        this.measures = measures;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSCrosstabDefinition wsCrosstabDefinition = new WSCrosstabDefinition();
        wsCrosstabDefinition.setColumns(AnalysisItemFactory.fromAnalysisFields(columns));
        wsCrosstabDefinition.setMeasures(AnalysisItemFactory.fromAnalysisFields(measures));
        wsCrosstabDefinition.setRows(AnalysisItemFactory.fromAnalysisFields(rows));
        wsCrosstabDefinition.setCrosstabDefinitionID(crosstabDefinitionID);
        return wsCrosstabDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        CrosstabDefinition crosstabDefinition = (CrosstabDefinition) super.clone();
        crosstabDefinition.setCrosstabDefinitionID(null);
        List<CrosstabColumnField> newColumns = new ArrayList<CrosstabColumnField>();
        for (CrosstabColumnField crosstabField : columns) {
            newColumns.add(new CrosstabColumnField(crosstabField.getAnalysisItem().clone()));
        }
        crosstabDefinition.setColumns(newColumns);
        List<CrosstabRowField> newRows = new ArrayList<CrosstabRowField>();
        for (CrosstabRowField crosstabField : rows) {
            newRows.add(new CrosstabRowField(crosstabField.getAnalysisItem().clone()));
        }
        crosstabDefinition.setRows(newRows);
        List<CrosstabMeasureField> newMeasures = new ArrayList<CrosstabMeasureField>();
        for (CrosstabMeasureField crosstabField : measures) {
            newMeasures.add(new CrosstabMeasureField(crosstabField.getAnalysisItem().clone()));
        }
        crosstabDefinition.setMeasures(newMeasures);
        return crosstabDefinition;
    }
}

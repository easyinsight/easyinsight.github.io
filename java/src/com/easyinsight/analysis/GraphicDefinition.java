package com.easyinsight.analysis;

import org.hibernate.annotations.Where;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jul 17, 2008
 * Time: 7:32:36 PM
 */
@Entity
@Table (name="graphic_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public abstract class GraphicDefinition extends AnalysisDefinition {

    /*@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="graphic_definition_id")
    private Long graphicDefinitionID;*/

    @OneToMany
    @Cascade ({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="analysis_id", nullable=false)
    @Where(clause = "field_type = 'Dimension'")
    private List<ChartDimensionField> dimensions;
    @OneToMany
    @Cascade ({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    @JoinColumn(name="analysis_id", nullable=false)
    @Where(clause = "field_type = 'Measure'")
    private List<ChartMeasureField> measures;

    /*public Long getGraphicDefinitionID() {
        return graphicDefinitionID;
    }

    public void setGraphicDefinitionID(Long graphicDefinitionID) {
        this.graphicDefinitionID = graphicDefinitionID;
    }*/

    public List<ChartDimensionField> getDimensions() {
        return dimensions;
    }

    public void setDimensions(List<ChartDimensionField> dimensions) {
        this.dimensions = dimensions;
    }

    public List<ChartMeasureField> getMeasures() {
        return measures;
    }

    public void setMeasures(List<ChartMeasureField> measures) {
        this.measures = measures;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        GraphicDefinition graphicDefinition = (GraphicDefinition) super.clone();
        List<ChartDimensionField> newDimensions = new ArrayList<ChartDimensionField>();
        for (ChartDimensionField chartDimensionField : dimensions) {
            newDimensions.add(new ChartDimensionField(chartDimensionField.getAnalysisItem()));
        }
        graphicDefinition.setDimensions(newDimensions);
        List<ChartMeasureField> newMeasures = new ArrayList<ChartMeasureField>();
        for (ChartMeasureField chartMeasureField : measures) {
            newMeasures.add(new ChartMeasureField(chartMeasureField.getAnalysisItem()));
        }
        graphicDefinition.setMeasures(newMeasures);
        return graphicDefinition;
    }
}

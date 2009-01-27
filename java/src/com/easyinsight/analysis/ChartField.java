package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 11:38:21 PM
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@Table(name="chart_field")
@DiscriminatorColumn(
    name="field_type",
    discriminatorType=DiscriminatorType.STRING
)
public class ChartField extends AnalysisField {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="chart_field_id")
    private long chartFieldID;

    public ChartField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public ChartField() {
    }

    public long getChartFieldID() {
        return chartFieldID;
    }

    public void setChartFieldID(long chartFieldID) {
        this.chartFieldID = chartFieldID;
    }
}

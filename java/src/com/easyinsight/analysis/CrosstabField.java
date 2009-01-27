package com.easyinsight.analysis;

import com.easyinsight.AnalysisItem;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Apr 15, 2008
 * Time: 1:12:47 PM
 */
@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="crosstab_field")
@DiscriminatorColumn(
    name="field_type",
    discriminatorType=DiscriminatorType.STRING
)
public class CrosstabField extends AnalysisField {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="crosstab_field_id")
    private long crosstabFieldID;

    public CrosstabField(AnalysisItem analysisItem) {
        super(analysisItem);
    }

    public CrosstabField() {
    }

    public long getCrosstabFieldID() {
        return crosstabFieldID;
    }

    public void setCrosstabFieldID(long crosstabFieldID) {
        this.crosstabFieldID = crosstabFieldID;
    }
}

package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Mar 28, 2009
 * Time: 5:23:55 PM
 */
@Entity
@PrimaryKeyJoinColumn(name="report_state_id")
@Table(name="list_report")
public class ListDefinitionState extends AnalysisDefinitionState {

    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="list_report_id")
    private long definitionID;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name="list_limits_metadata_id")
    private ListLimitsMetadata listLimitsMetadata;

    @Column(name="show_row_numbers")
    private boolean showRowNumbers = false;

    public long getDefinitionID() {
        return definitionID;
    }

    public void setDefinitionID(long definitionID) {
        this.definitionID = definitionID;
    }

    public ListLimitsMetadata getListLimitsMetadata() {
        return listLimitsMetadata;
    }

    public void setListLimitsMetadata(ListLimitsMetadata listLimitsMetadata) {
        this.listLimitsMetadata = listLimitsMetadata;
    }

    public boolean isShowRowNumbers() {
        return showRowNumbers;
    }

    public void setShowRowNumbers(boolean showRowNumbers) {
        this.showRowNumbers = showRowNumbers;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setListLimitsMetadata(listLimitsMetadata);
        listDefinition.setShowLineNumbers(showRowNumbers);
        listDefinition.setReportType(WSAnalysisDefinition.LIST);
        return listDefinition;
    }

    public ListDefinitionState clone() throws CloneNotSupportedException {
        ListDefinitionState listDefinition = (ListDefinitionState) super.clone();
        listDefinition.setDefinitionID(0);
        if (listLimitsMetadata != null) {
            listDefinition.getListLimitsMetadata().setLimitsMetadataID(0);
        }
        return listDefinition;
    }
}

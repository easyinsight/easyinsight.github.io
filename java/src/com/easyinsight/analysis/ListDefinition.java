package com.easyinsight.analysis;

import javax.persistence.*;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Jan 11, 2008
 * Time: 9:47:07 AM
 */
@Entity
@Table(name="list_definition")
@PrimaryKeyJoinColumn(name="analysis_id")
public class ListDefinition extends AnalysisDefinition {

    @GeneratedValue(strategy=GenerationType.IDENTITY)    
    @Column(name="list_definition_id")
    private Long listDefinitionID;

    /*@OneToMany(cascade=CascadeType.ALL)
    //@JoinColumn(name="analysis_id", nullable = false)
    //private List<ListField> columns;
    @JoinTable(name="analysis_to_analysis_item",
        joinColumns = @JoinColumn(name="analysis_id"),
        inverseJoinColumns = @JoinColumn(name="analysis_item_id"))
    private List<AnalysisItem> columns;    */

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="list_limits_metadata_id")
    private ListLimitsMetadata listLimitsMetadata;

    @Column(name="show_row_numbers")
    private boolean showRowNumbers = false;

    public Long getListDefinitionID() {
        return listDefinitionID;
    }

    public void setListDefinitionID(Long listDefinitionID) {
        this.listDefinitionID = listDefinitionID;
    }

    public ListLimitsMetadata getListLimitsMetadata() {
        return listLimitsMetadata;
    }

    public void setListLimitsMetadata(ListLimitsMetadata listLimitsMetadata) {
        this.listLimitsMetadata = listLimitsMetadata;
    }

    /*public List<AnalysisItem> getColumns() {
        return columns;
    }

    public void setColumns(List<AnalysisItem> columns) {
        if (columns == null) {
            columns = new ArrayList<AnalysisItem>();
        }
        this.columns = new ArrayList<AnalysisItem>(columns);
    }*/

    public boolean isShowRowNumbers() {
        return showRowNumbers;
    }

    public void setShowRowNumbers(boolean showRowNumbers) {
        this.showRowNumbers = showRowNumbers;
    }

    public WSAnalysisDefinition createWSDefinition() {
        WSListDefinition listDefinition = new WSListDefinition();
        listDefinition.setListDefinitionID(listDefinitionID);
        listDefinition.setListLimitsMetadata(listLimitsMetadata);
        listDefinition.setShowLineNumbers(showRowNumbers);
        return listDefinition;
    }

    public AnalysisDefinition clone() throws CloneNotSupportedException {
        ListDefinition listDefinition = (ListDefinition) super.clone();
        listDefinition.setListDefinitionID(null);
        if (listLimitsMetadata != null) {
            listDefinition.getListLimitsMetadata().setLimitsMetadataID(0);
        }
        return listDefinition;
    }
}

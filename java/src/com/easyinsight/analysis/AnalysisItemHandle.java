package com.easyinsight.analysis;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: 11/8/13
 * Time: 2:20 PM
 */
@Entity
@Table(name="analysis_item_handle")
public class AnalysisItemHandle implements Cloneable, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_item_handle_id")
    private Long handleID;

    @Column(name="analysis_item_id")
    private Long analysisItemID;

    @Column(name="name")
    private String name;

    @Column(name="position")
    private int position = -1;

    @Column(name="selected")
    private boolean selected;

    public AnalysisItemHandle clone() throws CloneNotSupportedException {
        AnalysisItemHandle handle = (AnalysisItemHandle) super.clone();
        handle.setHandleID(null);
        handle.setAnalysisItemID(null);
        return handle;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHandleID() {
        return handleID;
    }

    public void setHandleID(Long handleID) {
        this.handleID = handleID;
    }

    public Long getAnalysisItemID() {
        return analysisItemID;
    }

    public void setAnalysisItemID(Long analysisItemID) {
        this.analysisItemID = analysisItemID;
    }

    public void save() {
        if (handleID != null && handleID == 0) {
            handleID = null;
        }
        if (analysisItemID != null && analysisItemID == 0) {
            analysisItemID = null;
        }
    }

    public AnalysisItem reconcileToAnalysisItem(long dataSourceID) throws SQLException {
        // get data source
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID);
        List<AnalysisItem> fields = dataSource.getFields();
        for (AnalysisItem field : fields) {
            if ((getAnalysisItemID() != null && getAnalysisItemID() > 0 && field.getAnalysisItemID() == getAnalysisItemID()) ||
                    name.equals(field.toDisplay())) {
                return field;
            }
        }
        return null;
    }
}

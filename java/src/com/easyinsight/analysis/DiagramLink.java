package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/5/11
 * Time: 3:49 PM
 */
@Entity
@Table(name="diagram_report_link")
public class DiagramLink implements Cloneable, Serializable {

    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="diagram_report_link_id")
    private long diagramLinkID;

    @OneToOne (cascade = CascadeType.MERGE)
    @JoinColumn(name="source_id")
    private AnalysisItem startItem;
    @OneToOne (cascade = CascadeType.MERGE)
    @JoinColumn(name="target_id")
    private AnalysisItem endItem;

    @Column(name="link_name")
    private String label;

    public DiagramLink clone() throws CloneNotSupportedException {
        DiagramLink clone = (DiagramLink) super.clone();
        clone.setDiagramLinkID(0);
        return clone;
    }

    public long getDiagramLinkID() {
        return diagramLinkID;
    }

    public void setDiagramLinkID(long diagramLinkID) {
        this.diagramLinkID = diagramLinkID;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AnalysisItem getStartItem() {
        return startItem;
    }

    public void setStartItem(AnalysisItem startItem) {
        this.startItem = startItem;
    }

    public AnalysisItem getEndItem() {
        return endItem;
    }

    public void setEndItem(AnalysisItem endItem) {
        this.endItem = endItem;
    }

    public void afterLoad() {
        startItem = (AnalysisItem) Database.deproxy(startItem);
        startItem.afterLoad();
        endItem = (AnalysisItem) Database.deproxy(endItem);
        endItem.afterLoad();
    }

    public void beforeSave(Session session) {
        startItem.reportSave(session);
        if (startItem.getAnalysisItemID() == 0) {
            session.save(startItem);
        } else {
            session.update(startItem);
        }
        endItem.reportSave(session);
        if (endItem.getAnalysisItemID() == 0) {
            session.save(endItem);
        } else {
            session.update(endItem);
        }
    }

    public void updateIDs(ReplacementMap replacementMap) throws CloneNotSupportedException {
        setStartItem(replacementMap.getField(startItem));
        setEndItem(replacementMap.getField(endItem));
    }
}

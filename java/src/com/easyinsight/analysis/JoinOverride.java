package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedStorage;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 2:24 PM
 */
@Entity
@Table(name="join_override")
public class JoinOverride implements Cloneable, Serializable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="join_override_id")
    private long joinOverrideID;
    @Transient
    private String sourceName;
    @Transient
    private String targetName;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="source_analysis_item_id")
    private AnalysisItem sourceItem;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="target_analysis_item_id")
    private AnalysisItem targetItem;
    @Column(name="left_join")
    private boolean sourceOuterJoin;
    @Column(name="right_join")
    private boolean targetOuterJoin;
    @Column(name="left_join_on_original")
    private boolean sourceJoinOriginal;
    @Column(name="right_join_on_original")
    private boolean targetJoinOriginal;
    @Column(name="data_source_id")
    private Long dataSourceID;

    @Column(name="marmot_script")
    private String marmotScript;

    public String toXML(XMLMetadata xmlMetadata) {
        Element joinOverride = new Element("joinOverride");
        Element sourceItem = new Element("sourceItem");
        joinOverride.appendChild(sourceItem);
        sourceItem.appendChild(sourceItem.toXML());
        Element targetItem = new Element("targetItem");
        joinOverride.appendChild(targetItem);
        targetItem.appendChild(targetItem.toXML());
        joinOverride.addAttribute(new Attribute("dataSourceID", xmlMetadata.urlKeyForDataSourceID(dataSourceID)));
        Element marmotScript = new Element("marmotScript");
        joinOverride.appendChild(marmotScript);
        marmotScript.appendChild(this.marmotScript != null ? this.marmotScript : "");
        return joinOverride.toXML();
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public boolean isSourceJoinOriginal() {
        return sourceJoinOriginal;
    }

    public void setSourceJoinOriginal(boolean sourceJoinOriginal) {
        this.sourceJoinOriginal = sourceJoinOriginal;
    }

    public boolean isTargetJoinOriginal() {
        return targetJoinOriginal;
    }

    public void setTargetJoinOriginal(boolean targetJoinOriginal) {
        this.targetJoinOriginal = targetJoinOriginal;
    }

    public boolean isSourceOuterJoin() {
        return sourceOuterJoin;
    }

    public void setSourceOuterJoin(boolean sourceOuterJoin) {
        this.sourceOuterJoin = sourceOuterJoin;
    }

    public boolean isTargetOuterJoin() {
        return targetOuterJoin;
    }

    public void setTargetOuterJoin(boolean targetOuterJoin) {
        this.targetOuterJoin = targetOuterJoin;
    }

    public JoinOverride clone() throws CloneNotSupportedException {
        JoinOverride joinOverride = (JoinOverride) super.clone();
        joinOverride.setJoinOverrideID(0);
        return joinOverride;
    }

    public Long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(Long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public void reportSave(Session session) {
        if (dataSourceID == 0) {
            dataSourceID = null;
        }
        sourceItem.reportSave(session);
        targetItem.reportSave(session);
    }

    public void afterLoad() {
        if (sourceItem != null) {
            sourceItem = (AnalysisItem) Database.deproxy(sourceItem);
            sourceItem.afterLoad();
        }
        if (targetItem != null) {
            targetItem = (AnalysisItem) Database.deproxy(targetItem);
            targetItem.afterLoad();
        }
    }

    public void updateIDs(ReplacementMap replacementMap) {
        sourceItem = replacementMap.getField(sourceItem);
        targetItem = replacementMap.getField(targetItem);
    }

    public long getJoinOverrideID() {
        return joinOverrideID;
    }

    public void setJoinOverrideID(long joinOverrideID) {
        this.joinOverrideID = joinOverrideID;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public AnalysisItem getSourceItem() {
        return sourceItem;
    }

    public void setSourceItem(AnalysisItem sourceItem) {
        this.sourceItem = sourceItem;
    }

    public AnalysisItem getTargetItem() {
        return targetItem;
    }

    public void setTargetItem(AnalysisItem targetItem) {
        this.targetItem = targetItem;
    }
}

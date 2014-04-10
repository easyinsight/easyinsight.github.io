package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.CompositeFeedConnection;
import com.easyinsight.datafeeds.FeedStorage;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 3/1/11
 * Time: 2:24 PM
 */
@Entity
@Table(name="join_override")
public class JoinOverride implements Cloneable, Serializable {

    public static final int NORMAL = 1;
    public static final int COMPOSITE = 2;

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
    @Column(name="join_type")
    private int joinType = JoinOverride.NORMAL;

    @Column(name="source_cardinality")
    private int sourceCardinality;
    @Column(name="target_cardinality")
    private int targetCardinality;
    @Column(name="force_outer_join")
    private int forceOuterJoin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "join_override_to_source_fields",
            joinColumns = @JoinColumn(name = "join_override_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id", nullable = false))
    private List<AnalysisItem> sourceItems;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "join_override_to_target_fields",
            joinColumns = @JoinColumn(name = "join_override_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "analysis_item_id", nullable = false))
    private List<AnalysisItem> targetItems;

    @Column(name="marmot_script")
    private String marmotScript;

    public int getSourceCardinality() {
        return sourceCardinality;
    }

    public void setSourceCardinality(int sourceCardinality) {
        this.sourceCardinality = sourceCardinality;
    }

    public int getTargetCardinality() {
        return targetCardinality;
    }

    public void setTargetCardinality(int targetCardinality) {
        this.targetCardinality = targetCardinality;
    }

    public int getForceOuterJoin() {
        return forceOuterJoin;
    }

    public void setForceOuterJoin(int forceOuterJoin) {
        this.forceOuterJoin = forceOuterJoin;
    }

    public void fromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        Element sourceItemXML = (Element) (element.query("sourceItem").get(0)).getChild(0);
        sourceItem = AnalysisItem.fromXML(sourceItemXML, xmlImportMetadata);
        Element targetItemXML = (Element) (element.query("targetItem").get(0)).getChild(0);
        targetItem = AnalysisItem.fromXML(targetItemXML, xmlImportMetadata);
        dataSourceID = xmlImportMetadata.dataSourceForURLKey(element.getAttribute("dataSourceID").getValue()).getDataFeedID();
        marmotScript = element.query("marmotScript/text()").get(0).getValue();
    }

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

    public int getJoinType() {
        return joinType;
    }

    public void setJoinType(int joinType) {
        this.joinType = joinType;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public List<AnalysisItem> getSourceItems() {
        return sourceItems;
    }

    public void setSourceItems(List<AnalysisItem> sourceItems) {
        this.sourceItems = sourceItems;
    }

    public List<AnalysisItem> getTargetItems() {
        return targetItems;
    }

    public void setTargetItems(List<AnalysisItem> targetItems) {
        this.targetItems = targetItems;
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
        if (sourceItems != null) {
            joinOverride.setSourceItems(new ArrayList<AnalysisItem>(sourceItems));
        }
        if (targetItems != null) {
            joinOverride.setTargetItems(new ArrayList<AnalysisItem>(targetItems));
        }
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
        if (sourceItem != null) {
            sourceItem.reportSave(session);
        }
        if (targetItem != null) {
            targetItem.reportSave(session);
        }
        if (sourceItems != null) {
            for (AnalysisItem item : sourceItems) {
                item.reportSave(session);
            }
        }
        if (targetItems != null) {
            for (AnalysisItem item : targetItems) {
                item.reportSave(session);
            }
        }
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
        if (sourceItems != null) {
            for (AnalysisItem item : sourceItems) {
                item.afterLoad();
            }
            sourceItems = new ArrayList<AnalysisItem>(sourceItems);
        }
        if (targetItems != null) {
            for (AnalysisItem item : targetItems) {
                item.afterLoad();
            }
            targetItems = new ArrayList<AnalysisItem>(targetItems);
        }
    }

    public void updateIDs(ReplacementMap replacementMap) {
        System.out.println("Updating " + sourceItem.toDisplay() + "...");
        sourceItem = replacementMap.getField(sourceItem);
        System.out.println("\tUpdated with " + sourceItem.toDisplay());
        System.out.println("Updating " + targetItem.toDisplay() + "...");
        targetItem = replacementMap.getField(targetItem);
        System.out.println("\tUpdated with " + targetItem.toDisplay());
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

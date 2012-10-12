package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.*;

/**
 * User: James Boe
 * Date: Jan 25, 2009
 * Time: 2:32:26 PM
 */
@Entity
@Table(name="analysis_hierarchy_item")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisHierarchyItem extends AnalysisDimension {
    @OneToOne (cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="hierarchy_level_id")
    private HierarchyLevel hierarchyLevel;
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="analysis_hierarchy_item_to_hierarchy_level",
        joinColumns = @JoinColumn(name="analysis_item_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="hierarchy_level_id", nullable = false))
    private List<HierarchyLevel> hierarchyLevels;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        Element levels = new Element("levels");
        element.appendChild(levels);
        for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
            Element hierarchyLevelElement = new Element("hierarchyLevel");
            levels.appendChild(hierarchyLevelElement);
            if (hierarchyLevel == this.hierarchyLevel) {
                hierarchyLevelElement.addAttribute(new Attribute("currentLevel", "true"));
            } else {
                hierarchyLevelElement.addAttribute(new Attribute("currentLevel", "false"));
            }
            hierarchyLevelElement.appendChild(hierarchyLevel.getAnalysisItem().toXML(xmlMetadata));
        }
        return element;
    }

    @Override
    protected void subclassFromXML(Element fieldNode, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(fieldNode, xmlImportMetadata);
        List<HierarchyLevel> hierarchyLevelList = new ArrayList<HierarchyLevel>();
        Nodes levels = fieldNode.query("/levels/hierarchyLevel");
        for (int i = 0; i < levels.size(); i++) {
            Element level = (Element) levels.get(i);
            boolean currentLevel = Boolean.parseBoolean(level.getAttribute("currentLevel").getValue());
            AnalysisItem analysisItem = AnalysisItem.fromXML((Element) level.getChild(0), xmlImportMetadata);
            HierarchyLevel hierarchyLevel = new HierarchyLevel();
            hierarchyLevel.setAnalysisItem(analysisItem);
            hierarchyLevelList.add(hierarchyLevel);
            if (currentLevel) {
                this.hierarchyLevel = hierarchyLevel;
            }
        }
        setHierarchyLevels(hierarchyLevelList);
    }

    public void beforeSave() {
        super.beforeSave();
        int i = 0;
        for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
            hierarchyLevel.setPosition(i++);
        }
    }

    public void afterLoad(boolean optimized) {
        super.afterLoad(optimized);
        if (hierarchyLevels != null) {
            if (getHierarchyLevel() != null) {
                setHierarchyLevel((HierarchyLevel) Database.deproxy(getHierarchyLevel()));
            }
            List<HierarchyLevel> hierarchyLevelList = new ArrayList<HierarchyLevel>();
            for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
                hierarchyLevelList.add((HierarchyLevel) Database.deproxy(hierarchyLevel));
            }
            Collections.sort(hierarchyLevelList, new Comparator<HierarchyLevel>() {

                public int compare(HierarchyLevel o1, HierarchyLevel o2) {
                    return new Integer(o1.getPosition()).compareTo(o2.getPosition());
                }
            });
            hierarchyLevels = hierarchyLevelList;
            for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
                hierarchyLevel.setAnalysisItem((AnalysisItem) Database.deproxy(hierarchyLevel.getAnalysisItem()));
                hierarchyLevel.getAnalysisItem().afterLoad();
            }
        }
    }

    public List<HierarchyLevel> getHierarchyLevels() {
        return hierarchyLevels;
    }

    public void setHierarchyLevels(List<HierarchyLevel> hierarchyLevels) {
        this.hierarchyLevels = hierarchyLevels;
    }

    public HierarchyLevel getHierarchyLevel() {
        return hierarchyLevel;
    }

    public void setHierarchyLevel(HierarchyLevel hierarchyLevel) {
        this.hierarchyLevel = hierarchyLevel;
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        //analysisItems.add(this);
        if (getEverything) {
            for (HierarchyLevel hierarchyLevel : getHierarchyLevels()) {
                analysisItems.add(hierarchyLevel.getAnalysisItem());
            }
        } else {
            analysisItems.add(hierarchyLevel.getAnalysisItem());
        }
        return analysisItems;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.HIERARCHY;
    }

    @Override
    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public AnalysisItemResultMetadata createResultMetadata() {
        return hierarchyLevel.getAnalysisItem().createResultMetadata();
    }

    @Override
    public AnalysisDimension clone() throws CloneNotSupportedException {
        AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) super.clone();
        List<HierarchyLevel> levels = new ArrayList<HierarchyLevel>();
        for (HierarchyLevel hierarchyLevel : analysisHierarchyItem.hierarchyLevels) {
            levels.add(hierarchyLevel.clone());
        }
        analysisHierarchyItem.setHierarchyLevel(levels.get(0));
        analysisHierarchyItem.setHierarchyLevels(levels);
        return analysisHierarchyItem;
    }

    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
            AnalysisItem replacementItem = replacementMap.getField(hierarchyLevel.getAnalysisItem());
            hierarchyLevel.setAnalysisItem(replacementItem);
        }
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    public void reportSave(Session session) {
        super.reportSave(session);

        // clear out any bad rows from the hierarchy
        
        Iterator<HierarchyLevel> levelIter = hierarchyLevels.iterator();
        while (levelIter.hasNext()) {
            HierarchyLevel hierarchyLevel = levelIter.next();
            if (hierarchyLevel == null || hierarchyLevel.getAnalysisItem() == null) {
                levelIter.remove();
            }
        }
        for (HierarchyLevel hierarchyLevel : hierarchyLevels) {
            if (hierarchyLevel.getAnalysisItem().getAnalysisItemID() == 0) {
                AnalysisItem analysisItem = hierarchyLevel.getAnalysisItem();
                analysisItem.reportSave(session);
                session.save(analysisItem);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AnalysisHierarchyItem that = (AnalysisHierarchyItem) o;

        if (!hierarchyLevel.equals(that.hierarchyLevel)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + hierarchyLevel.hashCode();
        return result;
    }

    /*@Override
    public String toXML() {
        String xml = "<hierarchy>" + super.toXML();
        for (HierarchyLevel level : hierarchyLevels) {
            xml += "<level>" + level.getAnalysisItem().toXML() + "</level>";
        }
        xml += "</hierarchy>";
        return xml;
    }*/
}

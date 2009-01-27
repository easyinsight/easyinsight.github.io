package com.easyinsight.analysis;

import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.util.List;
import java.io.Serializable;

/**
 * User: James Boe
 * Date: Jan 25, 2009
 * Time: 2:32:31 PM
 */
@Entity
@Table(name="hierarchy")
public class Hierarchy implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="hierarchy_id")
    private long hierarchyID;
    @OneToMany (cascade = CascadeType.ALL)
    @JoinColumn(name="hierarchy_id")
    @IndexColumn(name="level", base=0)
    private List<HierarchyLevel> hierarchyLevels;

    public long getHierarchyID() {
        return hierarchyID;
    }

    public void setHierarchyID(long hierarchyID) {
        this.hierarchyID = hierarchyID;
    }

    public List<HierarchyLevel> getHierarchyLevels() {
        return hierarchyLevels;
    }

    public void setHierarchyLevels(List<HierarchyLevel> hierarchyLevels) {
        this.hierarchyLevels = hierarchyLevels;
    }
}

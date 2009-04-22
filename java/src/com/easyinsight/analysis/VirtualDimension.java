package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.PersistableValue;
import com.easyinsight.dataset.DataSet;

import javax.persistence.*;
import java.util.*;

/**
 * User: James Boe
 * Date: Apr 16, 2009
 * Time: 6:41:47 PM
 */
@Entity
@Table(name="virtual_dimension")
public class VirtualDimension {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="virtual_dimension_id")
    private long virtualDimensionID;

    @OneToOne (cascade = CascadeType.MERGE)
    @JoinColumn(name="analysis_dimension_id")
    private AnalysisDimension baseDimension;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="virtual_dimension_to_virtual_transform",
        joinColumns = @JoinColumn(name="virtual_dimension_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="virtual_transform_id", nullable = false))
    private List<VirtualTransform> virtualTransforms;

    @Column(name="name")
    private String name;

    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn (name="default_dimension_id")
    private VirtualTransform defaultTransform;

    public void toRemote() {
        for (VirtualTransform transform : virtualTransforms) {
            transform.toRemote();
        }
        defaultTransform.toRemote();
    }

    public void fromRemote() {
        for (VirtualTransform transform : virtualTransforms) {
            transform.fromRemote();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getVirtualDimensionID() {
        return virtualDimensionID;
    }

    public void setVirtualDimensionID(long virtualDimensionID) {
        this.virtualDimensionID = virtualDimensionID;
    }

    public AnalysisDimension getBaseDimension() {
        return baseDimension;
    }

    public void setBaseDimension(AnalysisDimension baseDimension) {
        this.baseDimension = baseDimension;
    }

    public List<VirtualTransform> getVirtualTransforms() {
        return virtualTransforms;
    }

    public void setVirtualTransforms(List<VirtualTransform> virtualTransforms) {
        this.virtualTransforms = virtualTransforms;
    }

    public VirtualTransform getDefaultTransform() {
        return defaultTransform;
    }

    public void setDefaultTransform(VirtualTransform defaultTransform) {
        this.defaultTransform = defaultTransform;
    }

    public List<IRow> createDimensions(List<IRow> rows, Collection<AnalysisItem> items) {
        List<IRow> results = new ArrayList<IRow>();
        Map<Value, AnalysisDimension> dimensionMap = new HashMap<Value, AnalysisDimension>();
        for (VirtualTransform virtualTransform : virtualTransforms) {
            for (PersistableValue value : virtualTransform.getAssignedValues()) {
                dimensionMap.put(value.toValue(), virtualTransform.getTransformDimension());                
            }
        }
        boolean defaultContained = items.contains(getDefaultTransform().getTransformDimension());
        for (IRow row : rows) {
            IRow newRow = new Row();
            newRow.addValues(row.getValues());
            Value startDimension = row.getValue(baseDimension.getKey());
            AnalysisDimension splitDimension = dimensionMap.get(startDimension);
            if (splitDimension == null && defaultContained) {
                splitDimension = getDefaultTransform().getTransformDimension();
            }
            if (splitDimension != null && startDimension != null && startDimension.type() != Value.EMPTY) {
                newRow.addValue(splitDimension.getKey(), startDimension);
                results.add(newRow);
            }
        }
        return results;
    }
}

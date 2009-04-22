package com.easyinsight.analysis;

import com.easyinsight.core.PersistableValue;
import com.easyinsight.core.PersistableStringValue;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
* Date: Apr 20, 2009
* Time: 10:56:54 AM
*/
@Entity
@Table(name="virtual_transform")
public class VirtualTransform {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="virtual_transform_id")
    private long virtualTransformID;
    @OneToOne (cascade = CascadeType.ALL)
    @JoinColumn(name="transform_dimension_id")
    private AnalysisDimension transformDimension;
    @OneToMany(cascade=CascadeType.ALL)
    @JoinTable(name="virtual_transform_to_value",
        joinColumns = @JoinColumn(name="virtual_transform_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name="value_id", nullable = false))
    private List<PersistableValue> assignedValues;

    @Transient
    private List<String> stringValues;

    public void toRemote() {
        assignedValues = new ArrayList<PersistableValue>(assignedValues);
        stringValues = new ArrayList<String>();
        for (PersistableValue persistableValue : assignedValues) {
            stringValues.add(persistableValue.toValue().toString());
        }
    }

    public void fromRemote() {
        assignedValues = new ArrayList<PersistableValue>();
        for (String value : stringValues) {
            assignedValues.add(new PersistableStringValue(value));
        }
    }

    public List<String> getStringValues() {
        return stringValues;
    }

    public void setStringValues(List<String> stringValues) {
        this.stringValues = stringValues;
    }

    public long getVirtualTransformID() {
        return virtualTransformID;
    }

    public void setVirtualTransformID(long virtualTransformID) {
        this.virtualTransformID = virtualTransformID;
    }

    public AnalysisDimension getTransformDimension() {
        return transformDimension;
    }

    public void setTransformDimension(AnalysisDimension transformDimension) {
        this.transformDimension = transformDimension;
    }

    public List<PersistableValue> getAssignedValues() {
        return assignedValues;
    }

    public void setAssignedValues(List<PersistableValue> assignedValues) {
        this.assignedValues = assignedValues;
    }
}

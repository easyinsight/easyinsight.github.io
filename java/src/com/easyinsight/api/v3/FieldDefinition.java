package com.easyinsight.api.v3;

/**
 * User: jamesboe
 * Date: 12/20/10
 * Time: 11:29 AM
 */
public class FieldDefinition {
    private String internalName;
    private String displayName;
    private String keyGrouping;
    private boolean defaultGroupBy;
    private MeasureAggregationType measureAggregationType;
    private MeasureFormattingType measureFormattingType;
    private FieldType fieldType;

    public String getKeyGrouping() {
        return keyGrouping;
    }

    public void setKeyGrouping(String keyGrouping) {
        this.keyGrouping = keyGrouping;
    }

    public boolean isDefaultGroupBy() {
        return defaultGroupBy;
    }

    public void setDefaultGroupBy(boolean defaultGroupBy) {
        this.defaultGroupBy = defaultGroupBy;
    }

    public MeasureAggregationType getMeasureAggregationType() {
        return measureAggregationType;
    }

    public void setMeasureAggregationType(MeasureAggregationType measureAggregationType) {
        this.measureAggregationType = measureAggregationType;
    }

    public MeasureFormattingType getMeasureFormattingType() {
        return measureFormattingType;
    }

    public void setMeasureFormattingType(MeasureFormattingType measureFormattingType) {
        this.measureFormattingType = measureFormattingType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

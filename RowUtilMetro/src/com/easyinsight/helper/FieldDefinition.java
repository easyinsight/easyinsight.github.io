package com.easyinsight.helper;

/**
 * User: jamesboe
 * Date: 1/6/11
 * Time: 10:46 AM
 */
public class FieldDefinition {
    private String internalName;
    private String displayName;
    private FieldType fieldType;

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

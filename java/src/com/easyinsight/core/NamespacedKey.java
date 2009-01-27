package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Jun 12, 2008
 * Time: 10:36:56 AM
 */
public class NamespacedKey extends NamedKey {
    private String namespaceField;
    private String field;

    public NamespacedKey() {
    }

    public NamespacedKey(String namespaceField, String name, String field) {
        super(name);
        this.namespaceField = namespaceField;
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getNamespaceField() {
        return namespaceField;
    }

    public void setNamespaceField(String namespaceField) {
        this.namespaceField = namespaceField;
    }
}

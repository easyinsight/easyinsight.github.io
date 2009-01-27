package com.easyinsight.core;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: James Boe
 * Date: Jul 16, 2008
 * Time: 12:44:12 AM
 */
@Entity
@Table(name="string_value")
@PrimaryKeyJoinColumn(name="value_id")
public class PersistableStringValue extends PersistableValue {
    @Column (name="string_content")
    private String value;

    public PersistableStringValue() {
    }

    public PersistableStringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

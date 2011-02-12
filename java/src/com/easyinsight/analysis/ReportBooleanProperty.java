package com.easyinsight.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Oct 31, 2010
 * Time: 4:29:34 PM
 */
@Entity
@Table(name="report_boolean_property")
public class ReportBooleanProperty extends ReportProperty {
    @Column(name="property_value")
    private boolean value;

    public ReportBooleanProperty(String propertyName, boolean value) {
        super(propertyName);
        setEnabled(true);
        this.value = value;
    }

    public ReportBooleanProperty() {
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}

package com.easyinsight.analysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:40:42 AM
 */
@Entity
@Table(name="report_numeric_property")
public class ReportNumericProperty extends ReportProperty {
    @Column(name="property_value")
    private double value;

    public ReportNumericProperty(String propertyName, double value) {
        super(propertyName);
        this.value = value;
    }

    public ReportNumericProperty() {
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}

package com.easyinsight.analysis;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: Jun 2, 2010
 * Time: 9:40:27 AM
 */
@Entity
@Table(name="report_property")
@Inheritance(strategy= InheritanceType.JOINED)
public class ReportProperty implements Cloneable {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="report_property_id")
    private Long reportPropertyID;
    @Column(name="property_name")
    private String propertyName;

    public ReportProperty(String propertyName) {
        this.propertyName = propertyName;
    }

    public ReportProperty() {
    }

    @Override
    public ReportProperty clone() throws CloneNotSupportedException {
        ReportProperty reportProperty = (ReportProperty) super.clone();
        reportProperty.setReportPropertyID(null);
        return reportProperty;
    }

    public Long getReportPropertyID() {
        return reportPropertyID;
    }

    public void setReportPropertyID(Long reportPropertyID) {
        this.reportPropertyID = reportPropertyID;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public void cleanup() {

    }
}

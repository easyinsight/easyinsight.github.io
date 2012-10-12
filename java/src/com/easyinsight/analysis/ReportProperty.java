package com.easyinsight.analysis;

import nu.xom.Element;

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

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public Element toXML() {
        return null;
    }

    public static ReportProperty fromXML(Element element) {
        ReportProperty reportProperty;
        if ("reportStringProperty".equals(element.getLocalName())) {
            reportProperty = new ReportStringProperty();
        } else if ("reportNumericProperty".equals(element.getLocalName())) {
            reportProperty = new ReportNumericProperty();
        } else if ("reportBooleanProperty".equals(element.getLocalName())) {
            reportProperty = new ReportBooleanProperty();
        } else if ("reportImageProperty".equals(element.getLocalName())) {
            reportProperty = new ReportImageProperty();
        } else {
            throw new RuntimeException();
        }
        reportProperty.customFromXML(element);
        return reportProperty;
    }

    protected void customFromXML(Element element) {

    }
}

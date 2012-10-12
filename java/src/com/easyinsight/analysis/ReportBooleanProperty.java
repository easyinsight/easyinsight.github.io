package com.easyinsight.analysis;

import nu.xom.Attribute;
import nu.xom.Element;

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

    public ReportBooleanProperty(String propertyName, boolean value, boolean enabled) {
        super(propertyName);
        setEnabled(enabled);
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

    @Override
    public Element toXML() {
        Element element = new Element("reportBooleanProperty");
        element.addAttribute(new Attribute("propertyName", getPropertyName()));
        element.appendChild(String.valueOf(value));
        return element;
    }

    protected void customFromXML(Element element) {
        setPropertyName(element.getAttribute("propertyName").getValue());
        value = Boolean.parseBoolean(element.getChild(0).getValue());
    }
}

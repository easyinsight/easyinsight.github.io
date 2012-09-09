package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: 10/25/11
 * Time: 11:47 AM
 */
@Entity
@Table(name="vertical_list_field_extension")
public class VerticalListReportExtension extends ReportFieldExtension {
    @Column(name="line_above")
    private boolean lineAbove;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("verticalListReportFieldExtension");
        element.addAttribute(new Attribute("lineAbove", String.valueOf(lineAbove)));
        return element;
    }

    public boolean isLineAbove() {
        return lineAbove;
    }

    public void setLineAbove(boolean lineAbove) {
        this.lineAbove = lineAbove;
    }
}

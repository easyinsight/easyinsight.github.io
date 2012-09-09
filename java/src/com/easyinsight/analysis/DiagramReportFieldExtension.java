package com.easyinsight.analysis;


import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;

import javax.persistence.*;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 10:02 AM
 */
@Entity
@Table(name="diagram_report_field_extension")
@PrimaryKeyJoinColumn(name="report_field_extension_id")
public class DiagramReportFieldExtension extends TrendReportFieldExtension {

    @Column(name="x")
    private int x;
    @Column(name="y")
    private int y;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.getAttribute("extensionType").setValue("diagram");
        element.addAttribute(new Attribute("x", String.valueOf(x)));
        element.addAttribute(new Attribute("y", String.valueOf(y)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element extensionElement, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(extensionElement, xmlImportMetadata);
        setX(Integer.parseInt(extensionElement.getAttribute("x").getValue()));
        setY(Integer.parseInt(extensionElement.getAttribute("y").getValue()));
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

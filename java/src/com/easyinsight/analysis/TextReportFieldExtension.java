package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: jamesboe
 * Date: 10/5/11
 * Time: 11:54 AM
 */
@Entity
@Table(name="text_report_field_extension")
public class TextReportFieldExtension extends ReportFieldExtension {
    @Column(name="align")
    private String align;
    @Column(name="font_size")
    private int size;

    @Column(name="fixed_width")
    private int fixedWidth;

    @Column(name="word_wrap")
    private boolean wordWrap;

    @Column(name="sortable")
    private boolean sortable = true;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("fieldExtension");
        element.addAttribute(new Attribute("extensionType", "text"));
        element.addAttribute(new Attribute("align", align));
        element.addAttribute(new Attribute("size", String.valueOf(size)));
        element.addAttribute(new Attribute("fixedWidth", String.valueOf(fixedWidth)));
        element.addAttribute(new Attribute("wordWrap", String.valueOf(wordWrap)));
        element.addAttribute(new Attribute("sortable", String.valueOf(sortable)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element extensionElement, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(extensionElement, xmlImportMetadata);
        setAlign(extensionElement.getAttribute("align").getValue());
        setSize(Integer.parseInt(extensionElement.getAttribute("size").getValue()));
        setFixedWidth(Integer.parseInt(extensionElement.getAttribute("fixedWidth").getValue()));
        setWordWrap(Boolean.parseBoolean(extensionElement.getAttribute("wordWrap").getValue()));
        setSortable(Boolean.parseBoolean(extensionElement.getAttribute("sortable").getValue()));
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public int getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

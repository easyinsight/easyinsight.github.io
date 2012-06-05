package com.easyinsight.analysis;

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
    public String toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("textReportFieldExtension");
        element.addAttribute(new Attribute("align", align));
        element.addAttribute(new Attribute("size", String.valueOf(size)));
        element.addAttribute(new Attribute("size", String.valueOf(fixedWidth)));
        element.addAttribute(new Attribute("size", String.valueOf(wordWrap)));
        element.addAttribute(new Attribute("size", String.valueOf(sortable)));
        return element.toXML();
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

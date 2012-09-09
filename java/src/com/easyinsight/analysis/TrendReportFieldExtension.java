package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/26/11
 * Time: 10:02 AM
 */
@Entity
@Table(name="trend_report_field_extension")
@PrimaryKeyJoinColumn(name="report_field_extension_id")
public class TrendReportFieldExtension extends ReportFieldExtension {
    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="trend_date_id")
    private AnalysisItem date;

    @Column(name="icon_image")
    private String iconImage;

    @Column(name="high_low")
    private int highLow;

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("fieldExtension");
        element.addAttribute(new Attribute("extensionType", "trend"));
        Element date = new Element("date");
        date.appendChild(this.date.toXML(xmlMetadata));
        element.appendChild(date);
        element.addAttribute(new Attribute("iconImage", "iconImage"));
        element.addAttribute(new Attribute("highLow", String.valueOf(highLow)));
        return element;
    }

    @Override
    protected void subclassFromXML(Element extensionElement, XMLImportMetadata xmlImportMetadata) {
        super.subclassFromXML(extensionElement, xmlImportMetadata);
        setIconImage(extensionElement.getAttribute("iconImage").getValue());
        setHighLow(Integer.parseInt(extensionElement.getAttribute("highLow").getValue()));
        Nodes dates = extensionElement.query("date");
        if (dates.size() == 1) {
            this.date = AnalysisItem.fromXML((Element) dates.get(0), xmlImportMetadata);
        }
    }

    public int getHighLow() {
        return highLow;
    }

    public void setHighLow(int highLow) {
        this.highLow = highLow;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public AnalysisItem getDate() {
        return date;
    }

    public void setDate(AnalysisItem date) {
        this.date = date;
    }

    public void updateIDs(ReplacementMap replacementMap) {
        if (date != null) {
            date = replacementMap.getField(date);
        }
    }

    public List<AnalysisItem> getAnalysisItems(boolean getEverything) {
        List<AnalysisItem> items = super.getAnalysisItems(getEverything);
        if (getEverything && date != null) {
            items.add(date);
        }
        return items;
    }

    @Override
    public void reportSave(Session session) {
        super.reportSave(session);
        if (date != null) {
            date.reportSave(session);
            session.saveOrUpdate(date);
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (date != null) {
            setDate((AnalysisItem) Database.deproxy(getDate()));
            date.afterLoad();
        }
    }
}

package com.easyinsight.analysis;

import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Attribute;
import nu.xom.Element;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/5/11
 * Time: 11:54 AM
 */
@Entity
@Table(name="ytd_field_extension")
public class YTDReportFieldExtension extends ReportFieldExtension {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="benchmark_id")
    private AnalysisItem benchmark;

    /*@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="date_field_id")
    private AnalysisItem dateField;*/

    @Column(name="line_above")
    private boolean lineAbove;

    @Column(name="always_show")
    private boolean alwaysShow;

    @Column(name="section")
    private String section;

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public boolean isAlwaysShow() {
        return alwaysShow;
    }

    public void setAlwaysShow(boolean alwaysShow) {
        this.alwaysShow = alwaysShow;
    }

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2012);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_YEAR, 364);
        System.out.println(cal.getTimeInMillis());
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = new Element("ytdReportFieldExtension");
        element.addAttribute(new Attribute("lineAbove", String.valueOf(lineAbove)));
        if (benchmark != null) {
            Element benchmark = new Element("benchmark");
            benchmark.appendChild(benchmark.toXML());
        }
        return element;
    }

    /*public AnalysisItem getDateField() {
        return dateField;
    }

    public void setDateField(AnalysisItem dateField) {
        this.dateField = dateField;
    }*/

    public boolean isLineAbove() {
        return lineAbove;
    }

    public void setLineAbove(boolean lineAbove) {
        this.lineAbove = lineAbove;
    }

    public AnalysisItem getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(AnalysisItem benchmark) {
        this.benchmark = benchmark;
    }

    public void upbenchmarkIDs(ReplacementMap replacementMap) {
        benchmark = replacementMap.getField(benchmark);
        //dateField = replacementMap.getField(dateField);
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = super.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        if (getEverything) {
            items.add(benchmark);
            //items.add(dateField);
        }
        return items;
    }

    @Override
    public void reportSave(Session session) {
        super.reportSave(session);
        if (benchmark != null) {
            benchmark.reportSave(session);
            session.saveOrUpdate(benchmark);
        }
        /*if (dateField != null) {
            dateField.reportSave(session);
            session.saveOrUpdate(dateField);
        }*/
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (benchmark != null) {
            setBenchmark((AnalysisItem) Database.deproxy(getBenchmark()));
            benchmark.afterLoad();
        }
        /*if (dateField != null) {
            setDateField((AnalysisItem) Database.deproxy(getDateField()));
            dateField.afterLoad();
        }*/
    }

    public int extensionType() {
        return ReportFieldExtension.YTD;
    }

    public void validate(Set<Long> sourceIDs) {
        if (benchmark != null) {
            benchmark.validate(sourceIDs);
        }
    }
}

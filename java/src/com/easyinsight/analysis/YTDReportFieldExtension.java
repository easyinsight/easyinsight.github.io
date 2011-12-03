package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

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

    public AnalysisItem getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(AnalysisItem benchmark) {
        this.benchmark = benchmark;
    }

    public void upbenchmarkIDs(ReplacementMap replacementMap) {
        benchmark = replacementMap.getField(benchmark);
    }

    public List<AnalysisItem> getAnalysisItems(boolean getEverything) {
        List<AnalysisItem> items = super.getAnalysisItems(getEverything);
        if (getEverything) {
            items.add(benchmark);
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
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        if (benchmark != null) {
            setBenchmark((AnalysisItem) Database.deproxy(getBenchmark()));
            benchmark.afterLoad();
        }
    }
}

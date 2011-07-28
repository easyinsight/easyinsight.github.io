package com.easyinsight.analysis;

import java.util.List;

public class EmbeddedVerticalResults extends EmbeddedResults {
    private List<EmbeddedResults> list;
    private WSAnalysisDefinition report;

    public EmbeddedVerticalResults() {
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public List<EmbeddedResults> getList() {
        return list;
    }

    public void setList(List<EmbeddedResults> list) {
        this.list = list;
    }
}
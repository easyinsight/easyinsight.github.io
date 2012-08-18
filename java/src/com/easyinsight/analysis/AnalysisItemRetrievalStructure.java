package com.easyinsight.analysis;

import com.easyinsight.pipeline.Pipeline;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/11/12
 * Time: 10:02 AM
 */
public class AnalysisItemRetrievalStructure {
    private boolean onStorage;
    private WSAnalysisDefinition report;
    private AnalysisDefinition baseReport;
    private List<String> sections = Arrays.asList(Pipeline.BEFORE, Pipeline.AFTER, Pipeline.LAST);
    private String currentSection;

    public AnalysisItemRetrievalStructure(@Nullable String currentSection, AnalysisItemRetrievalStructure structure) {
        setReport(structure.getReport());
        setOnStorage(structure.isOnStorage());
        setBaseReport(structure.baseReport);
        setSections(structure.sections);
        this.currentSection = currentSection;
    }

    public AnalysisItemRetrievalStructure(@Nullable String currentSection) {
        this.currentSection = currentSection;
    }

    public boolean onOrAfter(String sectionName) {
        if (currentSection == null) {
            return true;
        }
        int position = sections.indexOf(currentSection);
        int index = sections.indexOf(sectionName);
        return index >= position;
    }

    public void setSections(List<String> sections) {
        this.sections = sections;
    }

    public void setBaseReport(AnalysisDefinition baseReport) {
        this.baseReport = baseReport;
    }

    public WSAnalysisDefinition getReport() {
        return report;
    }

    public void setReport(WSAnalysisDefinition report) {
        this.report = report;
    }

    public List<FilterDefinition> getFilters() {
        if (report != null && report.getFilterDefinitions() != null) {
            return report.getFilterDefinitions();
        }
        if (baseReport != null && baseReport.getFilterDefinitions() != null) {
            return baseReport.getFilterDefinitions();
        }
        return new ArrayList<FilterDefinition>();
    }

    public boolean isOnStorage() {
        return onStorage;
    }

    public void setOnStorage(boolean onStorage) {
        this.onStorage = onStorage;
    }
}

package com.easyinsight.analysis;

import com.easyinsight.datafeeds.Feed;
import com.easyinsight.pipeline.IComponent;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: jamesboe
 * Date: 4/4/11
 * Time: 4:37 PM
 */
@Entity
@Table(name="named_filter_reference")
@PrimaryKeyJoinColumn(name="filter_id")
public class NamedFilterReference extends FilterDefinition {
    @Column(name="filter_name")
    private String referenceName = "";

    @Transient
    private FilterDefinition filter;

    public String getReferenceName() {
        return referenceName;
    }

    public void setReferenceName(String filterName) {
        this.referenceName = filterName;
    }
    @Override
    public int type() {
        return FilterDefinition.NAMED_REF;
    }


    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        if (filter != null) {
            return filter.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return filter.materialize(insightRequestMetadata);
    }

    @Override
    public String toQuerySQL(String tableName) {
        return filter.toQuerySQL(tableName);
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return filter.populatePreparedStatement(preparedStatement, start, type, insightRequestMetadata);
    }

    @Override
    public boolean validForQuery() {
        return filter.validForQuery();
    }

    @Override
    public List<IComponent> createComponents(String pipelineName, IFilterProcessor filterProcessor, AnalysisItem sourceItem, boolean columnLevel) {
        return filter.createComponents(pipelineName, filterProcessor, sourceItem, columnLevel);
    }

    @Override
    public void timeshift(Feed dataSource, Collection<FilterDefinition> filters) {
        for (FilterDefinition filter : filters) {
            if (referenceName.equals(filter.getFilterName())) {
                this.filter = filter;
                break;
            }
        }
        if (this.filter == null) {
            throw new ReportException(new GenericReportFault("Could not find a filter named " + referenceName + " as referenced in a named filter reference you defined."));
        }
        filter.timeshift(dataSource, filters);
    }
}

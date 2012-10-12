package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import com.easyinsight.database.Database;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 2/17/11
 * Time: 9:47 AM
 */
@Entity
@Table(name="or_filter")
public class OrFilter extends FilterDefinition {

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinTable(name = "or_filter_to_filter",
            joinColumns = @JoinColumn(name = "or_filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false))
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    @Override
    public int type() {
        return FilterDefinition.OR;
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        for (FilterDefinition filter : filters) {
            filter.beforeSave(session);
            if (filter.getFilterID() == 0) {
                session.save(filter);
            } else {
                session.update(filter);
            }
        }
    }

    @Override
    public FilterDefinition clone() throws CloneNotSupportedException {
        OrFilter orFilter = (OrFilter) super.clone();
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filter : this.filters) {
            FilterDefinition clonedFilter = filter.clone();
            filters.add(clonedFilter);
            if (clonedFilter.getField() != null) {
                clonedFilter.setField(clonedFilter.getField().clone());
            }
        }
        orFilter.setFilters(filters);
        return orFilter;
    }

    @Override
    public void updateIDs(ReplacementMap replacementMap) {
        super.updateIDs(replacementMap);
        for (FilterDefinition filter : filters) {
            filter.updateIDs(replacementMap);
        }
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filter : this.filters) {
            filter = ((FilterDefinition) Database.deproxy(filter));
            filter.afterLoad();
            filters.add(filter);
        }
        this.filters = filters;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        MaterializedOrFilter materializedOrFilter = new MaterializedOrFilter(getField());
        List<MaterializedFilterDefinition> materializedFilters = new ArrayList<MaterializedFilterDefinition>();
        for (FilterDefinition filter : filters) {
            if (filter.isEnabled()) {
                materializedFilters.add(filter.materialize(insightRequestMetadata));
            }
        }
        materializedOrFilter.setFilters(materializedFilters);
        return materializedOrFilter;
    }

    @Override
    public String toQuerySQL(String tableName) {
        StringBuilder sb = new StringBuilder();
        for (FilterDefinition filter : filters) {
            if (filter.isEnabled() && filter.validForQuery()) {
                sb.append(filter.toQuerySQL(tableName));
                sb.append(" OR ");
            }
        }
        sb.delete(sb.length() - 5, sb.length() - 1);
        return sb.toString();
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        int counter = start;
        for (FilterDefinition filter : filters) {
            counter = filter.populatePreparedStatement(preparedStatement, counter, type, insightRequestMetadata);
        }
        return counter;
    }

    public boolean isSingleSource() {
        return false;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        Element filters = new Element("filters");
        element.appendChild(filters);
        for (FilterDefinition filter : this.filters) {
            filters.appendChild(filter.toXML(xmlMetadata));
        }
        return element;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        Nodes nodes = element.query("/filters/filter");
        for (int i = 0; i < nodes.size(); i++) {
            Element filterElement = (Element) nodes.get(i);
            this.filters.add(FilterDefinition.fromXML(filterElement, xmlImportMetadata));
        }
    }

    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything, boolean includeFilters, Collection<AnalysisItem> analysisItemSet, AnalysisItemRetrievalStructure structure) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        for (FilterDefinition filterDefinition : filters) {
            items.addAll(filterDefinition.getAnalysisItems(allItems, insightItems, getEverything, includeFilters, analysisItemSet, structure));
        }
        return items;
    }

    @Override
    public String toHTML(FilterHTMLMetadata filterHTMLMetadata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"orFilterBox\">");
        for (FilterDefinition filter : filters) {
            sb.append(filter.toHTML(filterHTMLMetadata));
        }
        sb.append("</div>");
        return sb.toString();
    }
}

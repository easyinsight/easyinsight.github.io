package com.easyinsight.analysis;

import org.hibernate.Session;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * User: jamesboe
 * Date: Sep 12, 2010
 * Time: 5:40:15 PM
 */
@Entity
@Table(name="ordered_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class OrderedFilterDefinition extends FilterDefinition {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "ordered_filter_to_filter",
            joinColumns = @JoinColumn(name = "ordered_filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "filter_id", nullable = false))
    private List<FilterDefinition> filters;

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    @Override
    public void beforeSave(Session session) {
        for (FilterDefinition filter : filters) {
            filter.beforeSave(session);
        }
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String toQuerySQL(String tableName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}

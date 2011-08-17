package com.easyinsight.analysis;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jamesboe
 * Date: 7/15/11
 * Time: 11:03 PM
 */
@Entity
@Table(name="multi_date_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class MultiFlatDateFilter extends FilterDefinition {

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "multi_date_filter_to_date_level_wrapper",
            joinColumns = @JoinColumn(name = "filter_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "date_level_wrapper_id", nullable = false))
    private Collection<DateLevelWrapper> levels = new ArrayList<DateLevelWrapper>();

    public Collection<DateLevelWrapper> getLevels() {
        return levels;
    }

    public void setLevels(Collection<DateLevelWrapper> dateLevels) {
        this.levels = dateLevels;
    }

    @Override
    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedMultiFlatDateFilter(getField(), levels);
    }

    @Override
    public void afterLoad() {
        super.afterLoad();
        Collection<DateLevelWrapper> wrappers = new ArrayList<DateLevelWrapper>();
        for (DateLevelWrapper wrapper : getLevels()) {
            wrappers.add(wrapper);
        }
        setLevels(wrappers);
    }

    @Override
    public String toQuerySQL(String tableName) {
        /*return "year(" + getField().toKeySQL() + ") = ?";*/
        return null;
    }

    @Override
    public boolean validForQuery() {
        return false;
    }

    @Override
    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        /*preparedStatement.setInt(start++, value);
        return start;*/
        return start;
    }
}

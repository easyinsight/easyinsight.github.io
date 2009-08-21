package com.easyinsight.analysis;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Aug 7, 2009
 * Time: 10:59:53 AM
 */
public class FilterPatternDefinition extends FilterDefinition {

    private String pattern;
    private boolean regex;
    private boolean caseSensitive;

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableFilterPatternDefinition pattern = new PersistableFilterPatternDefinition();
        pattern.setEnabled(isEnabled());
        pattern.setField(getField());
        pattern.setCaseSensitive(isCaseSensitive());
        pattern.setRegex(isRegex());
        pattern.setPattern(getPattern());
        pattern.setApplyBeforeAggregation(isApplyBeforeAggregation());
        pattern.setFilterId(getFilterID());
        pattern.setIntrinsic(isIntrinsic());
        return pattern;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterPatternDefinition(getField(), pattern, caseSensitive, regex);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().getKeyID();
        queryBuilder.append(columnName);
        queryBuilder.append(" LIKE ?");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        String likePattern = pattern.replaceAll("\\*", "%");
        preparedStatement.setString(start, likePattern);
        return start + 1;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isRegex() {
        return regex;
    }

    public void setRegex(boolean regex) {
        this.regex = regex;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}

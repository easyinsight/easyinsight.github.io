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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
}

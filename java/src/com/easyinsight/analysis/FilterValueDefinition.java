package com.easyinsight.analysis;

import com.easyinsight.core.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:57:56 PM
 */
public class FilterValueDefinition extends FilterDefinition {
    private boolean inclusive;
    private List<Object> filteredValues;

    public FilterValueDefinition() {        
    }

    public FilterValueDefinition(AnalysisItem field, boolean inclusive, List<Object> filteredValues) {
        super(field);
        this.inclusive = inclusive;
        this.filteredValues = filteredValues;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

     public List<Object> getFilteredValues() {
        return filteredValues;
    }

    public void setFilteredValues(List<Object> filteredValues) {
        this.filteredValues = filteredValues;
    }

    public PersistableFilterDefinition toPersistableFilterDefinition() {
        PersistableValueFilterDefinition persistableFilterDefinition = new PersistableValueFilterDefinition();
        persistableFilterDefinition.setIntrinsic(isIntrinsic());
        persistableFilterDefinition.setFilterId(getFilterID());
        persistableFilterDefinition.setInclusive(inclusive);
        persistableFilterDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        persistableFilterDefinition.setField(getField());
        Set<Value> valueSet = new HashSet<Value>();
        for (Object valueObject : filteredValues) {
            Value value;
            if (valueObject instanceof Value) {
                value = (Value) valueObject;
            } else if (valueObject instanceof String) {
                value = new StringValue((String) valueObject);
            } else if (valueObject instanceof Number) {
                value = new NumericValue((Number) valueObject);
            } else if (valueObject instanceof Date) {
                value = new DateValue((Date) valueObject);
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            valueSet.add(value);
        }
        Set<PersistableValue> filterDefinitionValues = PersistableValueFactory.fromValue(valueSet);
        persistableFilterDefinition.setFilterValues(filterDefinitionValues);
        return persistableFilterDefinition;
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = getField().toKeySQL();
        queryBuilder.append(columnName);
        if (inclusive) {
            queryBuilder.append(" in (");
        } else {
            queryBuilder.append(" not in (");
        }
        for (int i = 0; i < getFilteredValues().size(); i++) {
            queryBuilder.append("?,");
        }
        queryBuilder = queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(")");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        Set<Value> valueSet = new HashSet<Value>();
        for (Object valueObject : filteredValues) {
            Value value;
            if (valueObject instanceof Value) {
                value = (Value) valueObject;
            } else if (valueObject instanceof String) {
                value = new StringValue((String) valueObject);
            } else if (valueObject instanceof Number) {
                value = new NumericValue((Number) valueObject);
            } else if (valueObject instanceof Date) {
                value = new DateValue((Date) valueObject);
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            valueSet.add(value);
        }
        if (type == Value.NUMBER) {
            for (Value value : valueSet) {
                preparedStatement.setDouble(start++, value.toDouble());
            }
        } else if (type == Value.DATE) {
            // TODO: ???
        } else if (type == Value.STRING) {
            for (Value value : valueSet) {
                preparedStatement.setString(start++, value.toString());
            }
        } else if (type == Value.EMPTY) {
            for (Value value : valueSet) {
                preparedStatement.setString(start++, value.toString());
            }
        }
        return start;
    }

    @Override
    public boolean validForQuery() {
        if (filteredValues.size() == 1) {
            Object value = filteredValues.get(0);
            if (value instanceof String) {
                String string = (String) value;
                return !"".equals(string);
            }
        }
        return filteredValues.size() > 0;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        Set<Value> valueSet = new HashSet<Value>();
        for (Object valueObject : filteredValues) {
            Value value;
            if (valueObject instanceof String) {
                value = new StringValue((String) valueObject);
            } else if (valueObject instanceof Number) {
                value = new NumericValue((Number) valueObject);
            } else if (valueObject instanceof Date) {
                value = new DateValue((Date) valueObject);
            } else if (valueObject instanceof Value) {
                value = (Value) valueObject;
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            valueSet.add(value);
        }
        return new MaterializedValueFilterDefinition(getField(), valueSet, inclusive);
    }
}

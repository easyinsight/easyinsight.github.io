package com.easyinsight.analysis;

import com.easyinsight.core.*;
import org.hibernate.Session;

import javax.persistence.*;
import java.sql.Types;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: James Boe
 * Date: Jul 8, 2008
 * Time: 2:57:56 PM
 */
@Entity
@Table(name="value_based_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FilterValueDefinition extends FilterDefinition {
    @Column(name="inclusive")
    private boolean inclusive = true;
    @Transient
    private List<Object> filteredValues;
    @OneToMany(cascade= CascadeType.ALL)
    @JoinTable(name="filter_to_value",
               joinColumns=@JoinColumn(name="filter_id"),
               inverseJoinColumns=@JoinColumn(name="value_id"))
    private Set<PersistableValue> persistedValues;
    @Column(name="single_value")
    private boolean singleValue;

    @Column(name="auto_complete")
    private boolean autoComplete;

    public FilterValueDefinition() {        
    }

    public FilterValueDefinition(AnalysisItem field, boolean inclusive, List<Object> filteredValues) {
        super(field);
        this.inclusive = inclusive;
        this.filteredValues = filteredValues;
    }

    public boolean isAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public Set<PersistableValue> getPersistedValues() {
        return persistedValues;
    }

    public void setPersistedValues(Set<PersistableValue> persistedValues) {
        this.persistedValues = persistedValues;
    }

    public boolean isSingleValue() {
        return singleValue;
    }

    public void setSingleValue(boolean singleValue) {
        this.singleValue = singleValue;
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

    public FilterDefinition clone() throws CloneNotSupportedException {
        FilterValueDefinition filter = (FilterValueDefinition) super.clone();
        Set<PersistableValue> values = new HashSet<PersistableValue>();
        if (persistedValues == null) {
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
            filter.setPersistedValues(filterDefinitionValues);
        } else {
            for (PersistableValue value : persistedValues) {
                values.add(value.clone());
            }
            filter.setPersistedValues(values);
        }
        List<Object> transferValues = new ArrayList<Object>();        
        for (PersistableValue filterDefinitionValue : filter.getPersistedValues()) {
            transferValues.add(filterDefinitionValue.toValue());
        }
        filter.setFilteredValues(transferValues);
        return filter;
    }

    public void beforeSave(Session session) {
        super.beforeSave(session);
        Set<Value> valueSet = new HashSet<Value>();
        if (filteredValues != null) {
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
        }
        Set<PersistableValue> filterDefinitionValues = PersistableValueFactory.fromValue(valueSet);
        setPersistedValues(filterDefinitionValues);
    }

    public void afterLoad() {
        super.afterLoad();
        List<Object> values = new ArrayList<Object>();
        if (getPersistedValues() != null) {
            for (PersistableValue filterDefinitionValue : getPersistedValues()) {
                values.add(filterDefinitionValue.toValue());
            }
        }
        setFilteredValues(values);
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
        List<Value> valueSet = new ArrayList<Value>();
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
            } else if (valueObject == null) {
                continue;
            } else {
                throw new RuntimeException("Unexpected value class " + valueObject.getClass().getName());
            }
            if (value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                if ("(No Value)".equals(stringValue.getValue())) {
                    value = new EmptyValue();
                }
            }
            valueSet.add(value);
        }
        if (type == Value.NUMBER) {
            for (Value value : valueSet) {
                preparedStatement.setDouble(start++, value.toDouble());
            }
        } else if (type == Value.DATE) {
            for (Value value : valueSet) {
                if (value.type() == Value.DATE) {
                    DateValue dateValue = (DateValue) value;
                    preparedStatement.setTimestamp(start++, new java.sql.Timestamp(dateValue.getDate().getTime()));
                } else {
                    preparedStatement.setNull(start++, Types.TIMESTAMP);
                }
            }
        } else if (type == Value.STRING) {
            for (Value value : valueSet) {
                preparedStatement.setString(start++, value.toString());
            }
        } else if (type == Value.EMPTY) {
            for (Value value : valueSet) {
                preparedStatement.setString(start++, value.toString());
            }
        } else {
            for (Value value : valueSet) {
                preparedStatement.setString(start++, value.toString());
            }
        }
        return start;
    }

    @Override
    public boolean validForQuery() {
        if (getField().isMultipleTransform()) {
            return false;
        }
        if (super.validForQuery() && filteredValues.size() == 1) {
            Object value = filteredValues.get(0);
            if (value instanceof String) {
                String string = (String) value;
                return !"".equals(string);
            } else if (value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                return !"".equals(stringValue.toString());
            } else if (value instanceof EmptyValue) {
                return false;
            }
        }
        return super.validForQuery() && filteredValues.size() > 0;
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
            if (value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                if ("(No Value)".equals(stringValue.getValue())) {
                    value = new EmptyValue();
                }
            }
            valueSet.add(value);
        }
        return new MaterializedValueFilterDefinition(getField(), valueSet, inclusive);
    }

    @Override
    public String toXML() {
        String xml = "<valueFilter>";
        xml += getField().toXML();
        xml += "</valueFilter>";
        return xml;
    }
}

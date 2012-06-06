package com.easyinsight.analysis;

import com.easyinsight.core.*;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
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

    @Column(name="exclude_empty")
    private boolean excludeEmpty;

    @Column(name="all_option")
    private boolean allOption;

    @Transient
    private AnalysisItemResultMetadata cachedValues;

    public FilterValueDefinition() {        
    }

    public FilterValueDefinition(AnalysisItem field, boolean inclusive, List<Object> filteredValues) {
        super(field);
        this.inclusive = inclusive;
        this.filteredValues = filteredValues;
    }

    public AnalysisItemResultMetadata getCachedValues() {
        return cachedValues;
    }

    public void setCachedValues(AnalysisItemResultMetadata cachedValues) {
        this.cachedValues = cachedValues;
    }

    public boolean isExcludeEmpty() {
        return excludeEmpty;
    }

    public void setExcludeEmpty(boolean excludeEmpty) {
        this.excludeEmpty = excludeEmpty;
    }

    public boolean isAllOption() {
        return allOption;
    }

    public void setAllOption(boolean allOption) {
        this.allOption = allOption;
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
        for (PersistableValue persistableValue : getPersistedValues()) {
            persistableValue.truncate();
            session.saveOrUpdate(persistableValue);
        }
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
                return !"".equals(string) && !"All".equals(string);
            } else if (value instanceof StringValue) {
                StringValue stringValue = (StringValue) value;
                return !"".equals(stringValue.toString()) && !"All".equals(stringValue.toString());
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
    public int type() {
        return FilterDefinition.VALUE;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        setInclusive(Boolean.parseBoolean(element.getAttribute("inclusive").getValue()));
        setSingleValue(Boolean.parseBoolean(element.getAttribute("singleValue").getValue()));
        setAutoComplete(Boolean.parseBoolean(element.getAttribute("autoComplete").getValue()));
        setAllOption(Boolean.parseBoolean(element.getAttribute("allOption").getValue()));
        setExcludeEmpty(Boolean.parseBoolean(element.getAttribute("excludeEmpty").getValue()));
        Nodes valueNodes = element.query("values/value/text()");
        filteredValues = new ArrayList<Object>();
        for (int i = 0; i < valueNodes.size(); i++) {
            filteredValues.add(valueNodes.get(i).getValue());
        }
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("inclusive", String.valueOf(inclusive)));
        element.addAttribute(new Attribute("singleValue", String.valueOf(singleValue)));
        element.addAttribute(new Attribute("autoComplete", String.valueOf(autoComplete)));
        element.addAttribute(new Attribute("allOption", String.valueOf(allOption)));
        element.addAttribute(new Attribute("excludeEmpty", String.valueOf(excludeEmpty)));
        Element values = new Element("values");
        element.appendChild(values);
        for (Object valueObject : filteredValues) {
            Element valueElement = new Element("value");
            valueElement.appendChild(valueObject.toString());
        }
        // if@ualberta.com, 780-492-9120
        return element;
    }

    @Override
    public String toHTML(WSAnalysisDefinition report) {
        StringBuilder sb = new StringBuilder();
        AnalysisItemResultMetadata metadata = new DataService().getAnalysisItemMetadata(report.getDataFeedID(), getField(), 0, 0, 0, report);
        if (metadata.getReportFault() != null) {
            return "";
        }
        AnalysisDimensionResultMetadata dimensionMetadata = (AnalysisDimensionResultMetadata) metadata;
        String filterName = "filter"+getFilterID();
        if (singleValue) {

            String onChange = "updateFilter('filter" + getFilterID() + "')";
            if (!isToggleEnabled()) {
                sb.append(checkboxHTML());
            }
            sb.append(label());
            sb.append("<select style=\"margin-left:5px;margin-top:5px;margin-right:5px\" id=\""+filterName+"\" onchange=\""+onChange+"\">");


            List<String> stringList = new ArrayList<String>();
            for (Value value : dimensionMetadata.getValues()) {
                stringList.add(value.toString());
            }
            Collections.sort(stringList);
            if (isAllOption()) {
                stringList.add(0, "All");
            }
            if (isExcludeEmpty()) {
                stringList.remove("");
            }
            String existingChoice = getFilteredValues().get(0).toString();
            for (String value : stringList) {
                if (value.equals(existingChoice)) {
                    sb.append("<option selected=\"selected\">").append(value).append("</option>");
                } else {
                    sb.append("<option>").append(value).append("</option>");
                }
            }
            sb.append("</select>");

        } else {
            String divID = "filter" + getFilterID() + "div";
            sb.append("<div id=\"").append(divID).append("\" class=\"modal hide\">");
            sb.append("<div class=\"modal-body\">");
            sb.append("<div class=\"control-group\">");
            sb.append("<label class=\"control-label\" for=\""+filterName+"\">Available Values</label>");
            sb.append("<div class=\"controls\">");
            int size = Math.min(15, dimensionMetadata.getValues().size());
            sb.append("<select multiple=\"multiple\" id=\""+filterName+"\" size=\""+size+"\" style=\"width:400px\"");
            for (Value value : dimensionMetadata.getValues()) {
                if (filteredValues.contains(value)) {
                    sb.append("<option selected=\"selected\">").append(value).append("</option>");
                } else {
                    sb.append("<option>").append(value).append("</option>");
                }
            }
            sb.append("</select>");
            sb.append("</div>");
            sb.append("</div>");
            sb.append("</div>");
            sb.append("<div class=\"modal-footer\">\n" +
                    "        <button class=\"btn\" data-dismiss=\"modal\" onclick=\"updateMultiFilter('"+filterName+"')\">Send</button>\n" +
                    "        <button class=\"btn\" data-dismiss=\"modal\" type=\"button\">Cancel</button>\n" +
                    "    </div>");
            sb.append("</div>");
            sb.append("<div style=\"margin-left:5px;margin-top:8px;margin-right:5px\">");
            if (!isToggleEnabled()) {
                sb.append(checkboxHTML());
            }
            sb.append("<a href=\"#"+divID+"\" data-toggle=\"modal\">").append(label()).append("</a></div>");
        }
        return sb.toString();
    }
}

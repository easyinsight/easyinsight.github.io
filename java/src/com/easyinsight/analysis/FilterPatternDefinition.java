package com.easyinsight.analysis;

import com.easyinsight.core.XMLImportMetadata;
import com.easyinsight.core.XMLMetadata;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Nodes;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Aug 7, 2009
 * Time: 10:59:53 AM
 */
@Entity
@Table(name="pattern_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class FilterPatternDefinition extends FilterDefinition {

    @Column(name="pattern")
    private String pattern;
    @Column(name="regex")
    private boolean regex;
    @Column(name="case_sensitive")
    private boolean caseSensitive;

    @Override
    public int type() {
        return FilterDefinition.PATTERN;
    }

    public MaterializedFilterDefinition materialize(InsightRequestMetadata insightRequestMetadata) {
        return new MaterializedFilterPatternDefinition(getField(), pattern, caseSensitive, regex);
    }

    public String toQuerySQL(String tableName) {
        StringBuilder queryBuilder = new StringBuilder();
        String columnName = "k" + getField().getKey().toBaseKey().getKeyID();
        queryBuilder.append(columnName);
        queryBuilder.append(" LIKE ?");
        return queryBuilder.toString();
    }

    public int populatePreparedStatement(PreparedStatement preparedStatement, int start, int type, InsightRequestMetadata insightRequestMetadata) throws SQLException {
        String likePattern = pattern.replaceAll("\\*", "%");
        preparedStatement.setString(start, likePattern);
        return start + 1;
    }

    @Override
    public boolean validForQuery() {
        return super.validForQuery() && pattern != null && !regex;
    }

    @Override
    public void beforeSave(Session session) {
        super.beforeSave(session);
        if (pattern == null) {
            pattern = "";
        }
    }

    @Override
    public Element toXML(XMLMetadata xmlMetadata) {
        Element element = super.toXML(xmlMetadata);
        element.addAttribute(new Attribute("regularExpression", String.valueOf(regex)));
        element.addAttribute(new Attribute("caseSensitive", String.valueOf(caseSensitive)));
        element.appendChild(pattern);
        return element;
    }

    public void customFromXML(Element element, XMLImportMetadata xmlImportMetadata) {
        setRegex(Boolean.parseBoolean(element.getAttribute("regularExpression").getValue()));
        setCaseSensitive(Boolean.parseBoolean(element.getAttribute("caseSensitive").getValue()));
        pattern = element.getValue();
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

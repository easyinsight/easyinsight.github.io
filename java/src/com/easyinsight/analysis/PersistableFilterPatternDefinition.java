package com.easyinsight.analysis;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Column;

/**
 * User: jamesboe
 * Date: Aug 7, 2009
 * Time: 11:04:27 AM
 */
@Entity
@Table(name="pattern_filter")
@PrimaryKeyJoinColumn(name="filter_id")
public class PersistableFilterPatternDefinition extends PersistableFilterDefinition {

    @Column(name="pattern")
    private String pattern;
    @Column(name="regex")
    private boolean regex;
    @Column(name="case_sensitive")
    private boolean caseSensitive;

    public FilterDefinition toFilterDefinition() {
        FilterPatternDefinition filterPatternDefinition = new FilterPatternDefinition();
        filterPatternDefinition.setFilterID(getFilterId());
        filterPatternDefinition.setApplyBeforeAggregation(isApplyBeforeAggregation());
        filterPatternDefinition.setCaseSensitive(isCaseSensitive());
        filterPatternDefinition.setField(getField());
        filterPatternDefinition.setIntrinsic(isIntrinsic());
        filterPatternDefinition.setPattern(getPattern());
        filterPatternDefinition.setRegex(isRegex());
        return filterPatternDefinition;
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

package com.easyinsight.scrubbing;

import com.easyinsight.analysis.AnalysisItem;

import javax.persistence.*;

/**
 * User: James Boe
 * Date: Jul 30, 2008
 * Time: 11:39:52 AM
 */
@Entity
@Table(name="text_replace_scrub")
@PrimaryKeyJoinColumn(name="data_scrub_id")
public class TextReplaceScrub extends DataScrub {
    @Column(name="source_text")
    private String sourceText;
    @Column(name="target_text")
    private String replaceText;
    @Column(name="regex")
    private boolean regex;
    @Column(name="case_sensitive")
    private boolean caseSensitive;

    @OneToOne
    @JoinColumn(name = "analysis_item_id")
    private AnalysisItem analysisItem;

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public String getReplaceText() {
        return replaceText;
    }

    public void setReplaceText(String replaceText) {
        this.replaceText = replaceText;
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

    public IScrubber createScrubber() {
        return new TextReplaceScrubber(this);
    }
}

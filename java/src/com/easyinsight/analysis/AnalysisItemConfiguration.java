package com.easyinsight.analysis;

import com.easyinsight.tag.Tag;

import java.util.List;

/**
 * User: jamesboe
 * Date: 11/19/13
 * Time: 8:48 AM
 */
public class AnalysisItemConfiguration {
    private AnalysisItem analysisItem;
    private List<Tag> tags;
    public TextReportFieldExtension textExtension;
    public YTDReportFieldExtension ytdExtension;
    public VerticalListReportExtension verticalListExtension;

    public TextReportFieldExtension getTextExtension() {
        return textExtension;
    }

    public void setTextExtension(TextReportFieldExtension textExtension) {
        this.textExtension = textExtension;
    }

    public YTDReportFieldExtension getYtdExtension() {
        return ytdExtension;
    }

    public void setYtdExtension(YTDReportFieldExtension ytdExtension) {
        this.ytdExtension = ytdExtension;
    }

    public VerticalListReportExtension getVerticalListExtension() {
        return verticalListExtension;
    }

    public void setVerticalListExtension(VerticalListReportExtension verticalListExtension) {
        this.verticalListExtension = verticalListExtension;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

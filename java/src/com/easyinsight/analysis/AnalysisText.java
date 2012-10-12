package com.easyinsight.analysis;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.core.Key;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: James Boe
 * Date: Aug 27, 2008
 * Time: 4:52:21 PM
 */
@Entity
@Table(name="analysis_text")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class AnalysisText extends AnalysisDimension {

    @Column(name="html")
    private boolean html;

    public AnalysisText() {
    }

    public AnalysisText(String key, boolean group) {
        super(key, group);
    }

    public AnalysisText(Key key) {
        super(key, true);
    }

    public AnalysisText(Key key, String displayName) {
        super(key, displayName);
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public int getType() {
        return super.getType() | AnalysisItemTypes.TEXT;
    }

    @Override
    public int actualType() {
        return AnalysisItemTypes.TEXT;
    }
}

package com.easyinsight.storage;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;

/**
* User: jamesboe
* Date: 9/9/11
* Time: 10:29 AM
*/
class KeyMetadata {
    private Key key;
    private int type;
    private AnalysisItem analysisItem;

    KeyMetadata(Key key, int type, AnalysisItem analysisItem) {
        this.key = key;
        this.type = type;
        this.analysisItem = analysisItem;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AnalysisItem getAnalysisItem() {
        return analysisItem;
    }

    public void setAnalysisItem(AnalysisItem analysisItem) {
        this.analysisItem = analysisItem;
    }

    public String createInsertClause() {
        if (type == Value.DATE) {
            return key.toSQL() + ", datedim_" + key.getKeyID() + "_id";
        } else {
            return key.toSQL();
        }
    }

    public String createUpdateClause() {
        if (type == Value.DATE) {
            return key.toSQL() + " = ?, datedim_" + key.getKeyID() + "_id = ?";
        } else {
            return key.toSQL() + " = ?";
        }
    }

    public String createInsertQuestionMarks() {
        if (type == Value.DATE) {
            return "?, ?";
        } else {
            return "?";
        }
    }
}

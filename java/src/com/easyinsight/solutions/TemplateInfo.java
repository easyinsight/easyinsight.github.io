package com.easyinsight.solutions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.tag.Tag;

import java.util.List;

/**
 * User: jamesboe
 * Date: 7/8/14
 * Time: 9:45 AM
 */
public class TemplateInfo {
    private List<FieldAssignment> fieldAssignmentList;
    private List<Tag> tags;
    private List<AnalysisItem> items;

    public TemplateInfo(List<FieldAssignment> fieldAssignmentList, List<Tag> tags) {
        this.fieldAssignmentList = fieldAssignmentList;
        this.tags = tags;
    }

    public TemplateInfo() {
    }

    public List<AnalysisItem> getItems() {
        return items;
    }

    public void setItems(List<AnalysisItem> items) {
        this.items = items;
    }

    public List<FieldAssignment> getFieldAssignmentList() {
        return fieldAssignmentList;
    }

    public void setFieldAssignmentList(List<FieldAssignment> fieldAssignmentList) {
        this.fieldAssignmentList = fieldAssignmentList;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

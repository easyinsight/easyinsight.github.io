package com.easyinsight.analysis;

import java.util.List;

/**
 * User: jamesboe
 * Date: 12/2/13
 * Time: 11:37 AM
 */
public interface IFieldChoiceFilter {
    List<AnalysisItemHandle> getFieldOrdering();

    List<WeNeedToReplaceHibernateTag> getAvailableTags();

    List<AnalysisItemHandle> getAvailableHandles();

    long getFilterID();

    boolean excludeReportFields();

    List<AnalysisItemHandle> selectedItems();

    int getExpandDates();
}

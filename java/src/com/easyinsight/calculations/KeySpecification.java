package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Feb 8, 2010
 * Time: 11:04:38 AM
 */
public interface KeySpecification {

    @Nullable
    public AnalysisItem findAnalysisItem(Map<String, List<AnalysisItem>> keyMap, Map<String, List<AnalysisItem>> displayMap) throws CloneNotSupportedException;
}

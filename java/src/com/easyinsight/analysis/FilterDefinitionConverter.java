package com.easyinsight.analysis;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Apr 16, 2008
 * Time: 1:52:43 PM
 */
public class FilterDefinitionConverter {
    public static List<FilterDefinition> fromPersistableFilters(List<FilterDefinition> persistableFilterDefinitions) {
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
        if (persistableFilterDefinitions != null) {
            for (FilterDefinition persistableFilterDefinition : persistableFilterDefinitions) {
                persistableFilterDefinition.afterLoad();
                filterDefinitions.add(persistableFilterDefinition);
            }
        }
        return filterDefinitions;
    }

    public static List<FilterDefinition> fromFilters(List<FilterDefinition> filterDefinitions) {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        for (FilterDefinition filter : filterDefinitions) {
            if (filter.getFromFilterSet() == 0) {
                filters.add(filter);
            }
        }
        return filters;
    }
}

package com.easyinsight.analysis;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Apr 16, 2008
 * Time: 1:52:43 PM
 */
public class FilterDefinitionConverter {
    public static List<FilterDefinition> fromPersistableFilters(List<PersistableFilterDefinition> persistableFilterDefinitions) {
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();
        if (persistableFilterDefinitions != null) {
            for (PersistableFilterDefinition persistableFilterDefinition : persistableFilterDefinitions) {
                persistableFilterDefinition.getField().afterLoad();
                filterDefinitions.add(persistableFilterDefinition.toFilterDefinition());
            }
        }
        return filterDefinitions;
    }

    public static List<PersistableFilterDefinition> fromFilters(List<FilterDefinition> filterDefinitions) {
        List<PersistableFilterDefinition> persistableFilterDefinitions = new ArrayList<PersistableFilterDefinition>();
        if (filterDefinitions != null) {
            for (FilterDefinition filterDefinition : filterDefinitions) {
                persistableFilterDefinitions.add(filterDefinition.toPersistableFilterDefinition());
            }
        }
        return persistableFilterDefinitions;
    }
}

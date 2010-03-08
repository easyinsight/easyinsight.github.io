package com.easyinsight.datafeeds.basecamp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 1, 2010
 * Time: 9:54:53 AM
 */
public class BaseCampCommentSource extends BaseCampBaseSource {
    public static final String TODO_ITEM_ID = "Todo Item";
    public static final String MILESTONE_ID = "Milestone ID";
    

    // in the end, we do need to refactor out milestones into a separate data source
    // and for that matter, people should be as well...

    // 

    @NotNull
    @Override
    protected List<String> getKeys() {
        return null;        
    }
}

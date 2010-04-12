package com.easyinsight.datafeeds.basecamp;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;

import java.util.Arrays;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Dec 21, 2009
 * Time: 3:05:54 PM
 */
public class BaseCamp1To2 extends DataSourceMigration {
     public BaseCamp1To2(FeedDefinition dataSource) {
        super(dataSource);
    }

    public void migrate(Map<String, Key> keys, EIConnection conn) {
        AnalysisDimension startTodoDim = (AnalysisDimension) findAnalysisItem(BaseCampTodoSource.TODOLISTNAME);
        if (startTodoDim != null) {
            AnalysisDimension todoDimension = new AnalysisDimension(startTodoDim.getKey(), BaseCampTodoSource.TODOLISTNAME);
            URLLink todoLink = new URLLink();
            todoLink.setLabel("View Todo List in Basecamp");
            todoLink.setUrl("[basecamp.url]/projects/["+BaseCampTodoSource.PROJECTID+"]/todo_lists/["+BaseCampTodoSource.TODOLISTID+"]");
            todoDimension.setLinks(Arrays.asList((Link) todoLink));
            migrateAnalysisItemByDisplay(BaseCampTodoSource.TODOLISTNAME, todoDimension);
        }
        AnalysisDimension startProjectDim = (AnalysisDimension) findAnalysisItem("Todo - " + BaseCampTodoSource.PROJECTNAME);
        if (startProjectDim != null) {
            AnalysisDimension projectDimension = new AnalysisDimension(startProjectDim.getKey(), "Todo - " + BaseCampTodoSource.PROJECTNAME);
            URLLink projectLink = new URLLink();
            projectLink.setLabel("View Project in Basecamp");
            projectLink.setUrl("[basecamp.url]/projects/["+BaseCampTodoSource.PROJECTID+"]");
            projectDimension.setLinks(Arrays.asList((Link) projectLink));
            migrateAnalysisItemByDisplay("Todo - " + BaseCampTodoSource.PROJECTNAME, projectDimension);
        }
        /*AnalysisDimension contentDimension = new AnalysisDimension("Todo Item", true);
        migrateAnalysisItem(BaseCampDataSource.CONTENT, contentDimension);*/
    }

    public int fromVersion() {
        return 1;
    }

    public int toVersion() {
        return 2;
    }
}

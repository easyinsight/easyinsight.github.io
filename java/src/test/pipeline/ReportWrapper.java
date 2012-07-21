package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;

/**
* User: jamesboe
* Date: 7/19/12
* Time: 1:29 PM
*/
public class ReportWrapper {

    private WSListDefinition listDefinition;
    private FeedMetadata feedMetadata;

    ReportWrapper(WSListDefinition listDefinition, FeedMetadata feedMetadata) {
        this.listDefinition = listDefinition;
        this.feedMetadata = feedMetadata;
    }

    public WSListDefinition getListDefinition() {
        return listDefinition;
    }

    public FeedMetadata getFeedMetadata() {
        return feedMetadata;
    }

    public FieldWrapper copyField(String name) {
        AnalysisItem baseItem = null;
        for (AnalysisItem analysisItem : feedMetadata.getFields()) {
            if (name.equals(analysisItem.toDisplay())) {
                baseItem = analysisItem;
                break;
            }
        }
        if (baseItem == null) {
            throw new RuntimeException("Could not find " + name);
        }
        AnalysisItem clone = new AnalysisService().cloneItem(baseItem);
        listDefinition.getAddedItems().add(clone);
        return new FieldWrapper(clone);
    }

    public Results runReport(EIConnection conn) {
        return new Results(DataService.listDataSet(getListDefinition(), new InsightRequestMetadata(), conn), getListDefinition());
    }

    public FieldWrapper addField(String name) {
        for (AnalysisItem analysisItem : feedMetadata.getFields()) {
            if (name.equals(analysisItem.toDisplay())) {
                listDefinition.getColumns().add(analysisItem);
                return new FieldWrapper(analysisItem);
            }
        }
        for (AnalysisItem analysisItem : listDefinition.getAddedItems()) {
            if (name.equals(analysisItem.toDisplay())) {
                listDefinition.getColumns().add(analysisItem);
                return new FieldWrapper(analysisItem);
            }
        }
        throw new RuntimeException("Could not find field " + name);
    }

    public void addFilter(FilterDefinition filterDefinition) {
        listDefinition.getFilterDefinitions().add(filterDefinition);
    }

    public FieldWrapper getField(String name) {
        for (AnalysisItem analysisItem : feedMetadata.getFields()) {
            if (name.equals(analysisItem.toDisplay())) {
                return new FieldWrapper(analysisItem);
            }
        }
        for (AnalysisItem analysisItem : listDefinition.getAddedItems()) {
            if (name.equals(analysisItem.toDisplay())) {
                return new FieldWrapper(analysisItem);
            }
        }
        throw new RuntimeException("Could not find field " + name);
    }

    public FieldWrapper addCalculation(String calculationName, String calculationString) {
        AnalysisCalculation calculation = new AnalysisCalculation();
        calculation.setKey(new NamedKey(calculationName));
        calculation.setCalculationString(calculationString);
        listDefinition.getAddedItems().add(calculation);
        return new FieldWrapper(calculation);
    }
}

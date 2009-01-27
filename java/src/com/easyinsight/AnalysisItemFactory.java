package com.easyinsight;

import com.easyinsight.analysis.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: James Boe
 * Date: Apr 13, 2008
 * Time: 11:51:13 PM
 */
public class AnalysisItemFactory {
    public static List<AnalysisItem> fromAnalysisFields(Collection analysisFields) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        for (Object analysisField : analysisFields) {
            analysisItems.add(((AnalysisField) analysisField).getAnalysisItem());
        }
        processItems(analysisItems);
        return analysisItems;
    }

    private static void processItems(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
                AnalysisHierarchyItem analysisHierarchyItem = (AnalysisHierarchyItem) analysisItem;
                analysisHierarchyItem.setHierarchyLevels(new ArrayList<HierarchyLevel>(analysisHierarchyItem.getHierarchyLevels()));
            }
        }
    }

    public static List<ListField> fromAnalysisItems(Collection<AnalysisItem> analysisItems) {
        List<ListField> analysisFields = new ArrayList<ListField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new ListField(analysisItem));
        }
        return analysisFields;
    }

     public static List<CrosstabRowField> createCrosstabRows(Collection<AnalysisItem> analysisItems) {
        List<CrosstabRowField> analysisFields = new ArrayList<CrosstabRowField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new CrosstabRowField(analysisItem));
            analysisItem.setAnalysisItemID(0);
            analysisItem.getFormattingConfiguration().setFormattingConfigurationID(0);
            if (analysisItem instanceof AnalysisDimension) {
                AnalysisDimension analysisDimension = (AnalysisDimension) analysisItem;
                analysisDimension.setAnalysisDimensionID(0);
            } else if (analysisItem instanceof AnalysisMeasure){
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                if (analysisMeasure.getMeasureConditionRange() != null) {
                    analysisMeasure.getMeasureConditionRange().setMeasureConditionRangeID(0);
                    analysisMeasure.getMeasureConditionRange().getLowCondition().setMeasureConditionID(0);
                    analysisMeasure.getMeasureConditionRange().getHighCondition().setMeasureConditionID(0);
                }
            }
        }
        return analysisFields;
    }

    public static List<CrosstabColumnField> createCrosstabColumns(Collection<AnalysisItem> analysisItems) {
        List<CrosstabColumnField> analysisFields = new ArrayList<CrosstabColumnField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new CrosstabColumnField(analysisItem));
            analysisItem.setAnalysisItemID(0);
            analysisItem.getFormattingConfiguration().setFormattingConfigurationID(0);
            if (analysisItem instanceof AnalysisDimension) {
                AnalysisDimension analysisDimension = (AnalysisDimension) analysisItem;
                analysisDimension.setAnalysisDimensionID(0);
            } else if (analysisItem instanceof AnalysisMeasure){
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                if (analysisMeasure.getMeasureConditionRange() != null) {
                    analysisMeasure.getMeasureConditionRange().setMeasureConditionRangeID(0);
                    analysisMeasure.getMeasureConditionRange().getLowCondition().setMeasureConditionID(0);
                    analysisMeasure.getMeasureConditionRange().getHighCondition().setMeasureConditionID(0);
                }
            }
        }
        return analysisFields;
    }

    public static List<CrosstabMeasureField> createCrosstabMeasures(Collection<AnalysisItem> analysisItems) {
        List<CrosstabMeasureField> analysisFields = new ArrayList<CrosstabMeasureField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new CrosstabMeasureField(analysisItem));
            analysisItem.setAnalysisItemID(0);
            analysisItem.getFormattingConfiguration().setFormattingConfigurationID(0);
            if (analysisItem instanceof AnalysisDimension) {
                AnalysisDimension analysisDimension = (AnalysisDimension) analysisItem;
                analysisDimension.setAnalysisDimensionID(0);
            } else if (analysisItem instanceof AnalysisMeasure){
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                if (analysisMeasure.getMeasureConditionRange() != null) {
                    analysisMeasure.getMeasureConditionRange().setMeasureConditionRangeID(0);
                    analysisMeasure.getMeasureConditionRange().getLowCondition().setMeasureConditionID(0);
                    analysisMeasure.getMeasureConditionRange().getHighCondition().setMeasureConditionID(0);
                }
            }
        }
        return analysisFields;
    }

    public static List<ChartDimensionField> createChartDimensions(Collection<AnalysisItem> analysisItems) {
        List<ChartDimensionField> analysisFields = new ArrayList<ChartDimensionField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new ChartDimensionField(analysisItem));
            analysisItem.setAnalysisItemID(0);
            analysisItem.getFormattingConfiguration().setFormattingConfigurationID(0);
            if (analysisItem instanceof AnalysisDimension) {
                AnalysisDimension analysisDimension = (AnalysisDimension) analysisItem;
                analysisDimension.setAnalysisDimensionID(0);
            } else if (analysisItem instanceof AnalysisMeasure){
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;
                if (analysisMeasure.getMeasureConditionRange() != null) {
                    analysisMeasure.getMeasureConditionRange().setMeasureConditionRangeID(0);
                    analysisMeasure.getMeasureConditionRange().getLowCondition().setMeasureConditionID(0);
                    analysisMeasure.getMeasureConditionRange().getHighCondition().setMeasureConditionID(0);
                }
            }
        }
        return analysisFields;
    }

    public static List<ChartMeasureField> createChartMeasures(Collection<AnalysisItem> analysisItems) {
        List<ChartMeasureField> analysisFields = new ArrayList<ChartMeasureField>();
        for (AnalysisItem analysisItem : analysisItems) {
            analysisFields.add(new ChartMeasureField(analysisItem));
            analysisItem.setAnalysisItemID(0);
            analysisItem.getFormattingConfiguration().setFormattingConfigurationID(0);
            if (analysisItem instanceof AnalysisDimension) {
                AnalysisDimension analysisDimension = (AnalysisDimension) analysisItem;
                analysisDimension.setAnalysisDimensionID(0);
            } else if (analysisItem instanceof AnalysisMeasure){
                AnalysisMeasure analysisMeasure = (AnalysisMeasure) analysisItem;                
                if (analysisMeasure.getMeasureConditionRange() != null) {
                    analysisMeasure.getMeasureConditionRange().setMeasureConditionRangeID(0);
                    analysisMeasure.getMeasureConditionRange().getLowCondition().setMeasureConditionID(0);
                    analysisMeasure.getMeasureConditionRange().getHighCondition().setMeasureConditionID(0);
                }
            }
        }
        return analysisFields;
    }
}

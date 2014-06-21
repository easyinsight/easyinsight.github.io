package com.easyinsight.analysis;

import com.easyinsight.core.DateValue;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.Value;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.PipelineData;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * User: jamesboe
 * Date: 6/18/14
 * Time: 3:07 PM
 */
public class PipelinePointInTime implements IComponent {

    private AnalysisItem instanceID;
    private AnalysisItem stageItem;
    private AnalysisDateDimension stageDate;
    private AnalysisItem target;
    private List<String> lastStages;

    private DataSet resultSet;

    public PipelinePointInTime(AnalysisItem instanceID, AnalysisItem stageItem, AnalysisDateDimension stageDate, AnalysisItem target, List<String> lastStages) {
        this.instanceID = instanceID;
        this.stageItem = stageItem;
        try {
            AnalysisDateDimension stage = (AnalysisDateDimension) stageDate.clone();
            stage.setDateLevel(((AnalysisDateDimension) target).getDateLevel());
            this.stageDate = stage;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        this.target = target;
        this.lastStages = lastStages;
    }

    public AnalysisItem getInstanceID() {
        return instanceID;
    }

    public AnalysisItem getStageItem() {
        return stageItem;
    }

    public AnalysisDateDimension getStageDate() {
        return stageDate;
    }

    // fromPipeline([Pipeline Field], [Source Field])

    public void blah(WSAnalysisDefinition baseReport, DataSet originalSet, InsightRequestMetadata insightRequestMetadata, EIConnection conn) {

        if (resultSet == null) {

            Map<Value, IRow> originalKeyed = new HashMap<>();
            for (IRow row : originalSet.getRows()) {
                Value instanceIDValue = row.getValue(instanceID);
                originalKeyed.put(instanceIDValue, row);
            }

            List<AnalysisItem> initialSet = new ArrayList<>();

            initialSet.add(instanceID);
            initialSet.add(stageItem);

            initialSet.add(stageDate);



            WSListDefinition report = new WSListDefinition();
            report.setDataFeedID(baseReport.getDataFeedID());
            report.setAddedItems(baseReport.getAddedItems());
            report.setAddonReports(baseReport.getAddonReports());
            report.setFilterDefinitions(new ArrayList<>());

            report.setColumns(initialSet);

            DataSet dataSet = DataService.listDataSet(report, insightRequestMetadata, conn);

            Map<Value, List<IRow>> instanceToRow = new HashMap<>();

            for (IRow row : dataSet.getRows()) {
                Value instance = row.getValue(instanceID);
                List<IRow> rows = instanceToRow.get(instance);
                if (rows == null) {
                    rows = new LinkedList<>();
                    instanceToRow.put(instance, rows);
                }
                rows.add(row);
            }

            DataSet endSet = new DataSet();
            for (Map.Entry<Value, List<IRow>> entry : instanceToRow.entrySet()) {
                Value instanceIDValue = entry.getKey();

                // for the instance ID, pull back the base detail info

                List<StagePoint> stagePoints = new ArrayList<>();

                for (IRow row : entry.getValue()) {
                    Value stageValue = row.getValue(stageItem);
                    String stageName = stageValue.toString();
                    Value stageDateValue = row.getValue(stageDate);
                    if (stageDateValue.type() == Value.DATE) {
                        Date date = ((DateValue) stageDateValue).getDate();
                        Instant instant = Instant.ofEpochMilli(date.getTime());
                        LocalDate res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
                        stagePoints.add(new StagePoint(stageName, row, res));
                    }
                }

                if (stagePoints.size() == 0) {
                    continue;
                }

                Collections.sort(stagePoints, (o1, o2) -> o1.stageDate.compareTo(o2.stageDate));

                // we're going to generate rows at this point based on intervals between stage points

                int i = 0;
                boolean moreData = true;

                // have to key on stuff to make this work...

                do {
                    StagePoint point = stagePoints.get(i);

                    LocalDate start = point.stageDate;

                    if (i < (stagePoints.size() - 1)) {
                        StagePoint nextPoint = stagePoints.get(i + 1);
                        LocalDate end = nextPoint.stageDate;
                        LocalDate date = start;
                        boolean reachedEnd;
                        do {
                            Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                            Date res = Date.from(instant);

                            IRow originalRow = originalKeyed.get(instanceIDValue);
                            if (originalRow != null) {
                                IRow newRow = endSet.createRow();
                                newRow.addValues(originalRow);

                                newRow.addValue(stageDate.createAggregateKey(), res);
                                newRow.addValue(stageItem.createAggregateKey(), point.stage);

                                newRow.addValue(new NamedKey("count"), 1);
                            }
                            if (stageDate.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                                date = date.plusDays(1);
                            } else if (stageDate.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL) {
                                date = date.plusWeeks(1);
                            } else if (stageDate.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                                date = date.plusMonths(1);
                            } else if (stageDate.getDateLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                                date = date.plusMonths(3);
                            } else if (stageDate.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                                date = date.plusYears(1);
                            } else {
                                throw new RuntimeException();
                            }
                            reachedEnd = !date.isBefore(end);
                        } while (!reachedEnd);
                    } else {
                        if (lastStages.contains(point.stage)) {
                            // we're done
                        } else {
                            boolean reachedEnd;
                            LocalDate date = start;
                            LocalDate end = LocalDate.now();
                            do {
                                Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
                                Date res = Date.from(instant);

                                IRow originalRow = originalKeyed.get(instanceIDValue);
                                if (originalRow != null) {
                                    IRow newRow = endSet.createRow();
                                    newRow.addValues(originalRow);

                                    newRow.addValue(stageDate.createAggregateKey(), res);
                                    newRow.addValue(stageItem.createAggregateKey(), point.stage);
                                }
                                if (stageDate.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                                    date = date.plusDays(1);
                                } else if (stageDate.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL) {
                                    date = date.plusWeeks(1);
                                } else if (stageDate.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                                    date = date.plusMonths(1);
                                } else if (stageDate.getDateLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                                    date = date.plusMonths(3);
                                } else if (stageDate.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                                    date = date.plusYears(1);
                                } else {
                                    throw new RuntimeException();
                                }
                                reachedEnd = !date.isBefore(end);
                            } while (!reachedEnd);
                        }
                        moreData = false;
                    }
                    i++;
                } while (moreData);
            }
            System.out.println("here's our final set");
            resultSet = endSet;
        }
    }

    @Override
    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {

        // have to combine our filled in set with the remaining set, with the necessary cache info for everything else to pull from
        // and create the appropriate other filters to get what you need

        Calendar calendar = Calendar.getInstance();
        for (IRow row : resultSet.getRows()) {
            row.addValue(target.createAggregateKey(), target.transformValue(row.getValue(stageDate.createAggregateKey()), pipelineData.getInsightRequestMetadata(), false, calendar));
        }
        return resultSet;
    }

    @Override
    public void decorate(DataResults listDataResults) {

    }

    private class StagePoint {
        private String stage;
        private IRow row;
        private LocalDate stageDate;

        private StagePoint(String stage, IRow row, LocalDate stageDate) {
            this.stage = stage;
            this.row = row;
            this.stageDate = stageDate;
        }
    }
}

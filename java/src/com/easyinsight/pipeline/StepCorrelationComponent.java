package com.easyinsight.pipeline;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.core.Value;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.*;

import java.util.*;

/**
 * User: James Boe
 * Date: May 18, 2009
 * Time: 1:55:39 PM
 */
public class StepCorrelationComponent implements IComponent {

    private AnalysisStep analysisStep;
    private AnalysisItem target;

    public StepCorrelationComponent(AnalysisStep analysisStep, AnalysisItem target) {
        this.analysisStep = analysisStep;
        this.target = target;
    }

    public DataSet apply(DataSet dataSet, PipelineData pipelineData) {
        Map<Value, Correlation> correlationMap = new HashMap<Value, Correlation>();
        for (IRow row : dataSet.getRows()) {
            Value correlationValue = row.getValue(analysisStep.getCorrelationDimension());
            Correlation correlation = correlationMap.get(correlationValue);
            if (correlation == null) {
                correlation = new Correlation(analysisStep.getStartDate(), analysisStep.getEndDate());
                correlationMap.put(correlationValue, correlation);
            }
            correlation.addRow(row);
        }
        List<IRow> rows = new ArrayList<IRow>();
        for (Correlation correlation : correlationMap.values()) {
            rows.addAll(correlation.generateRows().getRows());
        }
        return new DataSet(rows);
    }

    public void decorate(DataResults listDataResults) {
    }

    private class Correlation {
        private IRow row;
        private Date startDate;
        private Date endDate;
        private AnalysisDateDimension start;
        private AnalysisDateDimension end;

        private Correlation(AnalysisDateDimension start, AnalysisDateDimension end) {
            this.start = start;
            this.end = end;
        }

        public void addRow(IRow row) {
            this.row = row;
            Value startValue = row.getValue(start);
            if (startValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) startValue;
                startDate = dateValue.getDate();
            }
            Value endValue = row.getValue(end);
            if (endValue.type() == Value.DATE) {
                DateValue dateValue = (DateValue) endValue;
                endDate = dateValue.getDate();
            }
        }

        private boolean evaluate(AnalysisDateDimension date, Calendar cal, Object endObject, int year) {
            if (analysisStep.getDateLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                int currentYear = cal.get(Calendar.YEAR);
                if (currentYear > year) {
                    return false;
                } else if (currentYear < year) {
                    return true;
                }
                int currentQuarter = cal.get(Calendar.MONTH) / 3;
                int endQuarter = (Integer) endObject;
                return currentQuarter <= endQuarter;
            } else if (analysisStep.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                int currentYear = cal.get(Calendar.YEAR);
                if (currentYear > year) {
                    return false;
                } else if (currentYear < year) {
                    return true;
                }
                int currentQuarter = cal.get(Calendar.MONTH);
                int endQuarter = (Integer) endObject;
                return currentQuarter <= endQuarter;
            } else if (analysisStep.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL) {
                int currentYear = cal.get(Calendar.YEAR);
                if (currentYear > year) {
                    return false;
                } else if (currentYear < year) {
                    return true;
                }
                int currentQuarter = cal.get(Calendar.WEEK_OF_YEAR);
                int endQuarter = (Integer) endObject;
                return currentQuarter <= endQuarter;
            } else if (analysisStep.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                int currentYear = cal.get(Calendar.YEAR);
                if (currentYear > year) {
                    return false;
                } else if (currentYear < year) {
                    return true;
                }
                int currentQuarter = cal.get(Calendar.DAY_OF_YEAR);
                int endQuarter = (Integer) endObject;
                return currentQuarter <= endQuarter;
            } else if (analysisStep.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                int currentYear = cal.get(Calendar.YEAR);
                if (currentYear > year) {
                    return false;
                }
                return true;
            }
            return false;
        }

        public DataSet generateRows() {
            // loop from startDate to endDate, create entry for each field
            // create startDate, create endDate, create the interval between
            DataSet dataSet = new DataSet();
            if (startDate != null) {
                long startTime = startDate.getTime();
                long endTime = endDate == null ? System.currentTimeMillis() : endDate.getTime();

                Object endObject = null;
                int endYear = 0;
                if (analysisStep.getDateLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(endTime);
                    endObject = cal.get(Calendar.MONTH) / 3;
                    endYear = cal.get(Calendar.YEAR);
                } else if (analysisStep.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(endTime);
                    endObject = cal.get(Calendar.MONTH);
                    endYear = cal.get(Calendar.YEAR);
                } else if (analysisStep.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(endTime);
                    endObject = cal.get(Calendar.WEEK_OF_YEAR);
                    endYear = cal.get(Calendar.YEAR);
                } else if (analysisStep.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(endTime);
                    endYear = cal.get(Calendar.YEAR);
                } else if (analysisStep.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(endTime);
                    endObject = cal.get(Calendar.DAY_OF_YEAR);
                    endYear = cal.get(Calendar.YEAR);
                } else {
                    throw new RuntimeException();
                }
                // can't use end time, need to calculate end object

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(startTime);
                //long interval = 1000 * 60 * 60 * 24;


                while (evaluate(analysisStep, cal, endObject, endYear)) {
                    IRow row = dataSet.createRow();
                    row.addValues(this.row);
                    if (target == null) {
                        row.addValue(analysisStep.createAggregateKey(), new DateValue(cal.getTime()));
                    } else {
                        row.addValue(target.createAggregateKey(), new DateValue(cal.getTime()));
                    }
                    row.removeValue(start.createAggregateKey());
                    row.removeValue(end.createAggregateKey());
                    row.removeValue(analysisStep.getCorrelationDimension().createAggregateKey());
                    if (analysisStep.getDateLevel() == AnalysisDateDimension.DAY_LEVEL) {
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                    } else if (analysisStep.getDateLevel() == AnalysisDateDimension.WEEK_LEVEL) {
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                    } else if (analysisStep.getDateLevel() == AnalysisDateDimension.MONTH_LEVEL) {
                        cal.add(Calendar.MONTH, 1);
                    } else if (analysisStep.getDateLevel() == AnalysisDateDimension.YEAR_LEVEL) {
                        cal.add(Calendar.YEAR, 1);
                    } else if (analysisStep.getDateLevel() == AnalysisDateDimension.QUARTER_OF_YEAR_LEVEL) {
                        cal.add(Calendar.MONTH, 3);
                    } else {
                        throw new RuntimeException();
                    }

                }
            }
            return dataSet;
        }
    }
}

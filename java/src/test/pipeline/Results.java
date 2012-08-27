package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import junit.framework.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 7/19/12
 * Time: 1:30 PM
 */
public class Results {
    private DataSet dataSet;
    private WSListDefinition report;

    Results(DataSet dataSet, WSListDefinition report) {
        this.dataSet = dataSet;
        this.report = report;
    }

    public DataSet getDataSet() {
        return dataSet;
    }

    public WSListDefinition getReport() {
        return report;
    }

    public void verifyRowCount(int count) {
        String logName = null;
        if (count != dataSet.getRows().size()) {
            logName = new ReportLog().log(dataSet.getReportLog());
        }
        Assert.assertEquals("Counts did not match. Pipeline logged to " + logName + ". Data set contents were: " + dataSet.getRows(), count, dataSet.getRows().size());
    }

    public void verifyRow(Object... values) throws Exception {
        boolean found = false;
        for (IRow row : dataSet.getRows()) {
            boolean validRow = true;
            for (int i = 0; i < values.length; i++) {
                Object target = values[i];
                AnalysisItem analysisItem = report.getColumns().get(i);
                Value value = row.getValue(analysisItem);
                if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                    Number number = (Number) target;
                    validRow = validRow && number.intValue() == value.toDouble().intValue();
                } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    Date targetDate;
                    if (target instanceof Date) {
                        targetDate = (Date) target;
                        Calendar c = Calendar.getInstance();
                        c.setTime(targetDate);
                        Calendar d = Calendar.getInstance();
                        d.setTime(((DateValue) value).getDate());

                        validRow = validRow && d.get(Calendar.YEAR) == c.get(Calendar.YEAR) && d.get(Calendar.DAY_OF_YEAR) == c.get(Calendar.DAY_OF_YEAR);
                    } else if (target instanceof String) {
                        AnalysisDateDimension dateDim = (AnalysisDateDimension) analysisItem;
                        SimpleDateFormat sdf = new SimpleDateFormat(dateDim.getCustomDateFormat());
                        targetDate = sdf.parse((String) target);
                        validRow = validRow && ((DateValue) value).getDate().equals(targetDate);
                    } else if (target instanceof Number) {
                        validRow = validRow && ((NumericValue) value).getValue() == ((Number) target).doubleValue();
                    } else {
                        throw new RuntimeException();
                    }


                } else {
                    String string = (String) target;
                    validRow = validRow && value.toString().equals(string);
                }
            }
            if (validRow) {
                found = true;
                break;
            }
        }
        String logName = null;
        if (!found) {
            logName = new ReportLog().log(dataSet.getReportLog());
        }
        Assert.assertTrue("We could not find a row matching the specifications. Pipeline logged to " + logName + ". Data set contents were: " + dataSet.getRows(), found);
    }
}

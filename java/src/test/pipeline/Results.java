package test.pipeline;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Value;
import com.easyinsight.dataset.DataSet;
import junit.framework.Assert;

import java.text.SimpleDateFormat;
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
        Assert.assertEquals("Counts did not match. Data set contents were: " + dataSet.getRows(), count, dataSet.getRows().size());
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
                    } else if (target instanceof String) {
                        AnalysisDateDimension dateDim = (AnalysisDateDimension) analysisItem;
                        SimpleDateFormat sdf = new SimpleDateFormat(dateDim.getCustomDateFormat());
                        targetDate = sdf.parse((String) target);
                    } else {
                        throw new RuntimeException();
                    }
                    validRow = validRow && ((DateValue) value).getDate().equals(targetDate);
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
        Assert.assertTrue("We could not find a row matching the specifications. Data set contents were: " + dataSet.getRows(), found);
    }
}

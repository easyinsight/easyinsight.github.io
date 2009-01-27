package test;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.IRow;

/**
 * User: James Boe
 * Date: Feb 8, 2008
 * Time: 5:31:49 PM
 */
public class TestDataSetCreator {

    private int size = 10;

    public TestDataSetCreator() {
    }

    public TestDataSetCreator(int size) {
        this.size = size;
    }

    public DataSet createDataSet() {
        DataSet dataSet = new DataSet();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < 2; j++) {
                IRow row = dataSet.createRow();
                row.addValue("Customer", "C" + String.valueOf(i));
                row.addValue("NumericDim", String.valueOf(i * j));
                row.addValue("Product", "P" + String.valueOf(i) + "S" + String.valueOf(j));
                row.addValue("Revenue", String.valueOf(10));
                row.addValue("OrderDate", "2006-" + (i + 1) + "-0" + (j + 1));
            }
        }
        return dataSet;
    }
}

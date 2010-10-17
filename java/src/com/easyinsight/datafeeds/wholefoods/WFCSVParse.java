package com.easyinsight.datafeeds.wholefoods;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.IRow;
import com.easyinsight.core.Key;
import com.easyinsight.core.NumericValue;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * User: jamesboe
 * Date: Oct 16, 2010
 * Time: 7:27:18 PM
 */
public class WFCSVParse {
    public DataSet createData(DataStorage dataStorage, Map<String, Key> keyMap, byte[] bytes) throws IOException, SQLException {
        DataSet dataSet = new DataSet();
        System.out.println(bytes.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        BufferedInputStream bis = new BufferedInputStream(bais);
        CsvReader r= new CsvReader(bis, Charset.forName("UTF-16LE"));


        int i = 0;

        boolean headersRead = false;

        while (r.readRecord()) {
            if (r.getColumnCount() < 5) {
                continue;
            }
            if (!headersRead) {
                headersRead = true;
                continue;
            }
            IRow row = dataSet.createRow();
            row.addValue(keyMap.get(WholeFoodsSource.REGION), cleanup(r.get(0)));
            row.addValue(keyMap.get(WholeFoodsSource.STORE), cleanup(r.get(1)));
            row.addValue(keyMap.get(WholeFoodsSource.STORE_ID), cleanup(r.get(2)));
            row.addValue(keyMap.get(WholeFoodsSource.ITEM_SKU), cleanup(r.get(3)));
            row.addValue(keyMap.get(WholeFoodsSource.ITEM), cleanup(r.get(4)));
            row.addValue(keyMap.get(WholeFoodsSource.QUANTITY), cleanup(r.get(5)));
            row.addValue(keyMap.get(WholeFoodsSource.QUANTITY_UOM), cleanup(r.get(6)));
            row.addValue(keyMap.get(WholeFoodsSource.DATE), convertDate(cleanup(r.get(7))));
            row.addValue(keyMap.get(WholeFoodsSource.DOLLAR_SALES), NumericValue.produceDoubleValue(cleanup(r.get(9))));
            row.addValue(keyMap.get(WholeFoodsSource.LAST_YEAR_SALES), NumericValue.produceDoubleValue(cleanup(r.get(10))));
            row.addValue(keyMap.get(WholeFoodsSource.ARP), NumericValue.produceDoubleValue(cleanup(r.get(11))));
            row.addValue(keyMap.get(WholeFoodsSource.UNITS), NumericValue.produceDoubleValue(cleanup(r.get(12))));
            row.addValue(keyMap.get(WholeFoodsSource.LAST_YEAR_UNITS_SOLD), NumericValue.produceDoubleValue(cleanup(r.get(13))));
            i++;

            if (i % 250 == 0) {
                if (dataStorage != null) {
                    dataStorage.insertData(dataSet);
                    dataSet = new DataSet();
                } else {
                    break;
                }
            }
        }
        if (dataStorage != null) {
            dataStorage.insertData(dataSet);
        }
        r.close();
        return dataSet;
    }

    private Date convertDate(String finalString) {
        String[] tokens = finalString.split(" ");
        int year = Integer.parseInt(tokens[0]);
        int weekOfYear = Integer.parseInt(tokens[2]);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.WEEK_OF_YEAR, -13);
        Calendar startCal = Calendar.getInstance();
        startCal.set(Calendar.YEAR, year);
        startCal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
        return cal.getTime();
    }

    private static String cleanup(String value) throws UnsupportedEncodingException {
        return value;
        /*value = value.trim();
        if (value.length() > 1) {
            value = value.substring(1, value.length() - 2);
        }
        return value;*/
    }
}

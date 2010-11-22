package com.easyinsight.selenium;

import com.easyinsight.rowutil.RowMethod;
import com.easyinsight.rowutil.RowUtil;
import com.easyinsight.rowutil.TransactionResults;
import com.easyinsight.rowutil.TransactionalLoad;

import java.rmi.RemoteException;
import java.util.Calendar;

/**
 * User: jamesboe
 * Date: Oct 9, 2010
 * Time: 4:20:53 PM
 */
public class WholeFoodsXMLParse implements IWholeFoodsParse {

    private RowUtil rowUtil;

    private boolean updatedWhere = false;

    public WholeFoodsXMLParse(String apiKey, String apiSecretKey, String dataSourceName) throws RemoteException {
        rowUtil = new RowUtil(RowMethod.UPDATE, apiKey, apiSecretKey,  dataSourceName, true,
                "Region", "Store", "Store ID", "Item SKU", "Item Name", "Quantity", "Quantity UOM", "Date", "$ Sales", "$ Sales - Last Year",
                "Unit Sales", "Unit Sales - Last Year", "Average Retail Price");
    }

    public void addPage(String str) throws RemoteException {
        boolean haveData = true;
        int i = 2;
        while (haveData) {
            int index = str.indexOf("<tr o=\""+i+"\">");
            if (index == -1) {
                break;
            }
            Object[] vals = new Object[12];
            for (int j = 0; j < 12; j++) {
                int closeIndex = str.indexOf("</td>", index);
                int endIndex = closeIndex - 4;
                String substring = str.substring(index, endIndex);
                //System.out.println("substring = " + substring);
                int lastIdx = substring.lastIndexOf(">") + 1;
                String finalString = substring.substring(lastIdx);
                if (j >= 8) {
                    vals[j] = produceDoubleValue(finalString.substring(1));
                } else if (finalString.indexOf("Week") != -1) {
                    String[] tokens = finalString.split(" ");
                    int year = Integer.parseInt(tokens[0]);
                    int weekOfYear = Integer.parseInt(tokens[2]);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                    cal.set(Calendar.DAY_OF_WEEK, 3);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    cal.add(Calendar.WEEK_OF_YEAR, -13);
                    Calendar startCal = Calendar.getInstance();
                    startCal.set(Calendar.YEAR, year);
                    startCal.set(Calendar.WEEK_OF_YEAR, weekOfYear);
                    if (!updatedWhere) {
                        rowUtil.where().and("Date", cal.get(Calendar.DAY_OF_YEAR), cal.get(Calendar.YEAR));
                        updatedWhere = true;
                    }
                    vals[j] = cal.getTime();
                } else {
                    vals[j] = finalString;
                }
                index = endIndex + 5;
            }
            rowUtil.newRow(vals);
            i++;
        }
    }

    public static double produceDoubleValue(String valueObj) {
        Double value;
        if (valueObj == null || "".equals(valueObj)) {
            value = 0.;
        } else {
            try {
                value = Double.parseDouble(valueObj);
            } catch (NumberFormatException e) {
                // see if we can find a # in there somewhere...
                char[] transferArray = new char[valueObj.length()];
                int i = 0;
                for (char character : valueObj.toCharArray()) {
                    if (Character.isDigit(character) || character == '.') {
                        transferArray[i++] = character;
                    }
                }
                if (transferArray.length > 0) {
                    try {
                        value = Double.parseDouble(new String(transferArray));
                    } catch (NumberFormatException e1) {
                        value = 0.;
                    }
                } else {
                    value = 0.;
                }
            }
        }
        return value;
    }

    public void done() throws RemoteException {
        rowUtil.flush();        
    }
}

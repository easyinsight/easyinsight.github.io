package com.easyinsight.scheduler;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceInternalService;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.TempStorage;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 11, 2009
 * Time: 4:27:39 PM
 */
public class RedshiftFileUpdate {

    private long feedID;
    private boolean update;
    private long uploadID;
    private long userID;
    private long accountID;

    private List<AnalysisItem> newFields;

    public void setNewFields(List<AnalysisItem> newFields) {
        this.newFields = newFields;
    }

    public long getFeedID() {
        return feedID;
    }

    public boolean isUpdate() {
        return update;
    }

    public long getUploadID() {
        return uploadID;
    }

    public void updateData(long feedID, boolean update, EIConnection conn, byte[] bytes) throws Exception {
        DataStorage tableDef = null;
        try {
            FileBasedFeedDefinition feedDefinition = (FileBasedFeedDefinition) new FeedStorage().getFeedDefinitionData(feedID, conn);
            Key countKey = null;
            for (AnalysisItem field : feedDefinition.getFields()) {
                if (field.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure measure = (AnalysisMeasure) field;
                    if (measure.isRowCountField()) {
                        countKey = measure.getKey();
                    }
                }
            }

            if (newFields != null && newFields.size() > 0) {
                feedDefinition.getFields().addAll(newFields);
            }
            feedDefinition.setLastRefreshStart(new Date());
            new DataSourceInternalService().updateFeedDefinition(feedDefinition, conn);
            tableDef = DataStorage.writeConnection(feedDefinition, conn, accountID);
            if (update) {
                tableDef.truncate();
            }

            tableDef.commit();
            tableDef.closeConnection();
            TempStorage tempStorage = DataStorage.tempConnection(feedDefinition, conn);
            tempStorage.createTable(tempStorage.defineTempInsertTable(), true);

            CharsetDetector charsetDetector = new CharsetDetector();
            charsetDetector.setText(bytes);
            CharsetMatch[] charsetMatches = charsetDetector.detectAll();
            CsvReader r = null;
            for (CharsetMatch charsetMatch : charsetMatches) {
                try {
                    String charsetName = charsetMatch.getName();
                    ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                    r = new CsvReader(stream, Charset.forName(charsetName));
                    break;
                } catch (UnsupportedCharsetException e) {
                    // continue
                }
            }
            Map<String, AnalysisItem> analysisItems = new HashMap<String, AnalysisItem>();
            for (AnalysisItem field : feedDefinition.getFields()) {
                analysisItems.put(field.getKey().toKeyString(), field);
            }
            String[] headerColumns = null;
            boolean foundHeaders = false;
            boolean foundRecord = true;

            while (!foundHeaders && foundRecord) {
                foundRecord = r.readRecord();
                headerColumns = r.getValues();
                if (r.getColumnCount() >= 1 && r.getColumnCount() == headerColumns.length) {
                    foundHeaders = true;
                }
            }
            if (!foundHeaders) {
                throw new RuntimeException("We were unable to find headers in the file.");
            }
            DataSet dataSet = new DataSet();
            long start = System.currentTimeMillis();
            int i = 0;
            while (r.readRecord()) {
                IRow row = dataSet.createRow();
                for(int j = 0;j < r.getColumnCount();j++) {
                    if (j >= headerColumns.length) {
                        continue;
                    }
                    String key = headerColumns[j];
                    if (key.length() > 50) {
                        key = key.substring(0, 50);
                    }
                    AnalysisItem analysisItem = analysisItems.get(key);
                    if (analysisItem == null) {
                        continue;
                    }
                    String string = r.get(j);
                    Value value = transformValue(string, analysisItem);
                    row.addValue(analysisItem.getKey(), value);
                }
                row.addValue(countKey, 1);
                i++;
                if (i == 1000) {
                    tempStorage.insertData(dataSet);
                    dataSet = new DataSet();
                    i = 0;
                }
            }
            tempStorage.insertData(dataSet);
            tempStorage.commit();
            System.out.println("Inserting into temp, elapsed time = " + (System.currentTimeMillis() - start));
            tableDef = DataStorage.writeConnection(feedDefinition, conn);
            tableDef.insertFromSelect(tempStorage.getTableName());
            tableDef.commit();
            conn.commit();
            r.close();
        }
        catch(Exception se) {
            if (tableDef != null) tableDef.rollback();
            throw se;
        } finally {
            if (tableDef != null) {
                tableDef.closeConnection();
            }
        }
    }

    private Value transformValue(String string, AnalysisItem analysisItem) {
        Value value;
        if (string == null) {
            value = new EmptyValue();
        } else {
            string = string.trim();
            if (analysisItem == null) {
                value = new EmptyValue();
            } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
                value = new NumericValue(NumericValue.produceDoubleValue(string));
            } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension date = (AnalysisDateDimension) analysisItem;
                value = date.renameMeLater(new StringValue(string));
                if (value instanceof DateValue) {
                    DateValue dateValue = (DateValue) value;
                    dateValue.setFormat(date.getCustomDateFormat());
                }
            } else {
                value = new StringValue(string);
            }
        }
        return value;
    }


    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public void setUploadID(long uploadID) {
        this.uploadID = uploadID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }
}

package com.easyinsight.scheduler;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.dataset.ColumnSegment;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.TempStorage;
import com.easyinsight.users.User;
import com.easyinsight.userupload.DefaultFormatMapper;
import com.easyinsight.userupload.UploadFormat;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.userupload.UserUploadService;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import javax.persistence.Transient;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jun 10, 2009
 * Time: 9:10:02 PM
 */
public class FileProcessOptimizedCreateScheduledTask {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    private long userID;

    public long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    private long accountID;

    @Transient
    private long feedID;

    public void createFeed(Connection conn, byte[] bytes, UploadFormat uploadFormat, List<AnalysisItem> fields, boolean accountVisible) throws Exception {
        DataStorage tableDef = null;
        try {
            //PersistableDataSetForm dataSet = uploadFormat.createDataSet(bytes, fields, new DefaultFormatMapper(fields));

            /*for (AnalysisItem field : fields) {
                dataSet.refreshKey(field.getKey());
            }*/

            boolean hasCount = false;
            Key countKey = null;
            for (AnalysisItem field : fields) {
                if ("count".equals(field.toDisplay().toLowerCase())) {
                    hasCount = true;
                    countKey = field.getKey();
                }
            }

            AnalysisMeasure countMeasure;
            if (!hasCount) {
                countKey = new NamedKey("Count");
                countMeasure = new AnalysisMeasure(countKey, AggregationTypes.SUM);
                countMeasure.setRowCountField(true);
                fields.add(countMeasure);
            }

            FileBasedFeedDefinition feedDefinition = new FileBasedFeedDefinition();
            feedDefinition.setAccountVisible(accountVisible);
            feedDefinition.setUploadFormat(uploadFormat);
            feedDefinition.setFeedName(name);
            User user = UserUploadService.retrieveUser(conn, userID);
            feedDefinition.setOwnerName(user.getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(user.getUserID(), user.getAccount().getAccountID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(fields);
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
            tableDef = result.getTableDefinitionMetadata();
            /*tableDef.commit();
            tableDef.closeConnection();*/
            /*TempStorage tempStorage = DataStorage.tempConnection(feedDefinition, (EIConnection) conn);
            tempStorage.createTable(tempStorage.defineTempInsertTable());*/
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
            for (AnalysisItem field : fields) {
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
                    tableDef.insertData(dataSet);
                    dataSet = new DataSet();
                    i = 0;
                }
            }
            tableDef.insertData(dataSet);
            System.out.println("Inserting into temp, elapsed time = " + (System.currentTimeMillis() - start));
            /*tableDef = DataStorage.writeConnection(feedDefinition, conn);
            tableDef.insertFromSelect(tempStorage.getTableName());*/
            feedID = result.getFeedID();
            tableDef.commit();
            conn.commit();
            r.close();
        }
        catch(Exception e) {
            if(tableDef != null) {
                tableDef.rollback();
            }
            throw e;
        }
        finally {
            if(tableDef != null)
                tableDef.closeConnection();
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

    public long getFeedID() {
        return feedID;
    }

    public void setFeedID(long feedID) {
        this.feedID = feedID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }
}

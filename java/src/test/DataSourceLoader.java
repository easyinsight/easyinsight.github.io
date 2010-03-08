package test;

import com.easyinsight.database.Database;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedCreationResult;
import com.easyinsight.datafeeds.FeedCreation;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.core.*;
import com.easyinsight.analysis.*;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

import test.util.TestUtil;

/**
 * User: James Boe
 * Date: Jun 8, 2009
 * Time: 12:52:20 PM
 */
public class DataSourceLoader {

    public static final Key DIM1_KEY = new NamedKey("Dim1");
    public static final Key DIM2_KEY = new NamedKey("Dim2");
    public static final Key MEASURE = new NamedKey("Measure");
    public static final Key DATE = new NamedKey("Date");

    public static void main(String[] args) throws SQLException, IOException {
        Database.initialize();
        TestUtil.getProUser();
        Connection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);
        FeedDefinition feedDefinition = new FeedDefinition();
        feedDefinition.setFeedName("Test God Knows How Many Million Rows");
        feedDefinition.setOwnerName("Test User");
        feedDefinition.setPubliclyVisible(true);
        feedDefinition.setMarketplaceVisible(true);
        UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
        uploadPolicy.setPubliclyVisible(true);
        uploadPolicy.setMarketplaceVisible(true);
        feedDefinition.setUploadPolicy(uploadPolicy);
        feedDefinition.setFields(Arrays.asList(new AnalysisDimension(DIM1_KEY, true), new AnalysisDimension(DIM2_KEY, true), new AnalysisMeasure(MEASURE, AggregationTypes.SUM),
                new AnalysisDateDimension(DATE, true, AnalysisDateDimension.DAY_LEVEL)));
        FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
        result.getTableDefinitionMetadata().commit();
        result.getTableDefinitionMetadata().closeConnection();
        conn.commit();
        Database.instance().closeConnection(conn);
        feedDefinition = new FeedStorage().getFeedDefinitionData(result.getFeedID());
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 28; i++) {
            cal.add(Calendar.DAY_OF_YEAR, -1);
            addDataSet(feedDefinition, 0, cal.getTime());
        }
    }

    private static void addDataSet(FeedDefinition feedDefinition, int startPoint, Date day) throws SQLException, IOException {
        System.out.println(new Date());
        System.out.println(Runtime.getRuntime().freeMemory() + " / " + Runtime.getRuntime().totalMemory());
        DataSet dataSet = new DataSet();
        for (int i = startPoint; i < (startPoint + 10000); i++) {
            for (int j = 0; j < 10; j++) {
                IRow row = dataSet.createRow();
                row.addValue(DIM1_KEY, new StringValue("a" + i));
                row.addValue(DIM2_KEY, new StringValue("b" + i));
                row.addValue(MEASURE, new NumericValue(1));
                row.addValue(DATE, new DateValue(day));
            }
        }
        FileWriter fw = new FileWriter("c:\\export.txt");
        BufferedWriter bw = new BufferedWriter(fw, 512);
        StringBuilder lineBuilder = new StringBuilder();
        for (IRow row : dataSet.getRows()) {
            Iterator<Key> iter = row.getKeys().iterator();
            while (iter.hasNext()) {
                Value value = row.getValue(iter.next());
                lineBuilder.append(value.toString());
                if (iter.hasNext()) {
                    lineBuilder.append(",");
                }
            }
            lineBuilder.append("\r\n");
        }
        bw.write(lineBuilder.toString());
        bw.close();
        Connection conn = Database.instance().getConnection();
        conn.setAutoCommit(false);

        DataStorage dataStorage = DataStorage.writeConnection(feedDefinition, conn);
        dataStorage.insertData(dataSet);
        dataStorage.commit();
        Statement statement = dataStorage.getStorageConn().createStatement();
        statement.execute("FLUSH TABLES");
        dataStorage.getStorageConn().commit();
        conn.commit();
        dataStorage.closeConnection();
        Database.instance().closeConnection(conn);
    }
}

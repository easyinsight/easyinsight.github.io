package com.easyinsight.datafeeds.mailchimp;

import com.csvreader.CsvReader;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.IRow;
import com.easyinsight.analysis.ReportException;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/15/14
 * Time: 9:03 AM
 */
public class MailchimpListResultSource extends ServerDataSourceDefinition {

    public static final String LIST_ENTRY_COUNT = "List Entry Count";

    private String listID;

    @Override
    public FeedType getFeedType() {
        return FeedType.MAILCHIMP_LIST;
    }

    private transient List<String> keyList = new ArrayList<>();

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM MAILCHIMP_LIST WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO MAILCHIMP_LIST (DATA_SOURCE_ID, LIST_ID) VALUES (?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, listID);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT LIST_ID FROM MAILCHIMP_LIST WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setListID(rs.getString(1));
        }
        queryStmt.close();
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        try {
            MailchimpCompositeSource mailchimpCompositeSource = (MailchimpCompositeSource) parentDefinition;
            String apiKey = mailchimpCompositeSource.getMailchimpApiKey();
            String dataCenter = apiKey.split("-")[1];
            URL url = new URL("https://" + dataCenter + ".api.mailchimp.com/export/1.0/list/?apikey="+apiKey+"&id="+listID);

            InputStream is = url.openStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String headerLine = in.readLine();
            headerLine = headerLine.substring(headerLine.indexOf("[") + 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(headerLine.getBytes());
            CsvReader csvReader = new CsvReader(bais, Charset.forName("UTF-8"));
            csvReader.readRecord();

            keyList = new ArrayList<>();

            for (int i = 0; i < csvReader.getColumnCount(); i++) {
                String header = csvReader.get(i);
                System.out.println(header);
                keyList.add(header);
                fieldBuilder.addField(header, new AnalysisDimension());
            }

            /*String[] tokens = headerLine.split(",");
            for (String token : tokens) {
                System.out.println(token);
                int start;
                start = token.indexOf("\"") + 1;
                int end;
                if (token.endsWith("\"")) {
                    end = token.lastIndexOf("\"") - 1;
                } else {
                    end = token.length();
                }

                String substringToken = token.substring(start, end);

            }*/
            fieldBuilder.addField(LIST_ENTRY_COUNT, new AnalysisMeasure());
            in.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            MailchimpCompositeSource mailchimpCompositeSource = (MailchimpCompositeSource) parentDefinition;
            String apiKey = mailchimpCompositeSource.getMailchimpApiKey();
            String dataCenter = apiKey.split("-")[1];
            URL url = new URL("https://" + dataCenter + ".api.mailchimp.com/export/1.0/list/?apikey="+apiKey+"&id="+listID);

            InputStream is = url.openStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            in.readLine();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line).append("\r\n");
            }

            sb.deleteCharAt(sb.length() - 1);

            in.close();

            DataSet dataSet = new DataSet();
            ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes());
            CsvReader csvReader = new CsvReader(bais, Charset.forName("UTF-8"));
            while (csvReader.readRecord()) {
                IRow row = dataSet.createRow();
                for (int i = 0; i < csvReader.getColumnCount(); i++) {
                    String value = csvReader.get(i);
                    String key = keyList.get(i);
                    row.addValue(keys.get(key), value);
                }
                row.addValue(keys.get(LIST_ENTRY_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }
}

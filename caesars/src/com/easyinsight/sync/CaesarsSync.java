package com.easyinsight.sync;

import com.easyinsight.helper.*;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * User: jamesboe
 * Date: 4/1/12
 * Time: 11:58 AM
 */
public class CaesarsSync extends HttpServlet {

    private String username;
    private String password;

    @Override
    public void init(ServletConfig config) throws ServletException {

        super.init(config);    //To change body of overridden methods use File | Settings | File Templates.
        URL url = getClass().getClassLoader().getResource("password.properties");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(url.getFile())));
            username = properties.get("caesars.username").toString();
            password = properties.get("caesars.password").toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SyncRunnable syncRunnable = new SyncRunnable();
        syncRunnable.setDaemon(false);
        syncRunnable.start();
        resp.setContentType("text/xml");
        resp.setStatus(200);
        resp.getOutputStream().write("<response>Started.</response>".getBytes());
        resp.getOutputStream().flush();
    }
    
    private class SyncRunnable extends Thread {
        @Override
        public void run() {
            try {
                HttpClient client = new HttpClient();
                client.getParams().setAuthenticationPreemptive(true);
                Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/xml/reports/34019");
                client.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());
                Nodes nodes = doc.query("/response/rows/row");
                List<Record> fields = new ArrayList<Record>();
                DateFormat df = new SimpleDateFormat("MMM-dd-yy hh:mm aa");
                for (int i = 0; i < nodes.size(); i++) {
                    Node rowNode = nodes.get(i);
                    Nodes values = rowNode.query("value");
                    Element value1 = (Element) values.get(0);
                    Element value2 = (Element) values.get(1);
                    String recordID;
                    String value;
                    if (value1.getAttribute("field").getValue().equals("Rounds - Record ID#")) {
                        recordID = value1.getValue();
                        value = value2.getValue();
                    } else {
                        recordID = value2.getValue();
                        value = value1.getValue();
                    }
                    try {
                        String[] lines = value.split("\\n");
                        for (String line : lines) {
                            String date = line.substring(1, 19);
                            Date dateObj = df.parse(date);
                            String person = line.substring(21, line.indexOf("]"));
                            String status = line.substring(line.indexOf("]") + 2);
                            fields.add(new Record(person, dateObj, status, recordID));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Rounds Worked", username, password);

                dataSourceFactory.addGrouping("Person");
                dataSourceFactory.addDate("Date");
                dataSourceFactory.addGrouping("Action");
                dataSourceFactory.addGrouping("RoundRecordID");
                dataSourceFactory.addMeasure("Count");

                DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

                DataSourceTarget dataSourceTarget = dataSourceOperationFactory.replaceRowsOperation();

                dataSourceTarget.flush();

                dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

                for (int i = 0; i < fields.size(); i++) {
                    Record record = fields.get(i);
                    DataRow dataRow = dataSourceTarget.newRow();
                    dataRow.addValue("Person", record.getPerson());
                    dataRow.addValue("Date", record.getDate());
                    dataRow.addValue("Action", record.getAction());
                    dataRow.addValue("Count", 1);
                    dataRow.addValue("RoundRecordID", record.getRecordID());
                    if ((i % 5000) == 0) {
                        dataSourceTarget.flush();
                    }
                }


                dataSourceTarget.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Record {
        private String person;
        private Date date;
        private String action;
        private String recordID;

        private Record(String person, Date date, String action, String recordID) {
            this.person = person;
            this.date = date;
            this.action = action;
            this.recordID = recordID;
        }

        public String getRecordID() {
            return recordID;
        }

        public String getPerson() {
            return person;
        }

        public Date getDate() {
            return date;
        }

        public String getAction() {
            return action;
        }
    }
}

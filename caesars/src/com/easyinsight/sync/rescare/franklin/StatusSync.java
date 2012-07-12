package com.easyinsight.sync.rescare.franklin;

import com.easyinsight.helper.*;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

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
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 7/6/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class StatusSync extends HttpServlet {

    private String username;
    private String password;

    @Override
    public void init() throws ServletException {
        super.init();    //To change body of overridden methods use File | Settings | File Templates.
        URL url = getClass().getClassLoader().getResource("passwords.properties");
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(url.getFile())));
            username = properties.get("rescare.username").toString();
            password = properties.get("rescare.password").toString();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("getting...");
        try {
        SyncRunnable syncRunnable = new SyncRunnable();
        syncRunnable.setDaemon(false);
        syncRunnable.start();
        } catch(Exception e) { e.printStackTrace(); }
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
                System.out.println(username + ":" + password);
                Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
                GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/xml/reports/rQMdCPYQhNzOHBSrOmHE");
                client.executeMethod(getMethod);
                Document doc = new Builder().build(getMethod.getResponseBodyAsStream());

                Nodes nodes = doc.query("/response/rows/row");
                List<Record> fields = new ArrayList<Record>();
                for (int i = 0; i < nodes.size(); i++) {
                    Node rowNode = nodes.get(i);
                    String participantID = rowNode.query("value[@field='Participant ID']").get(0).getValue();
                    String status = rowNode.query("value[@field='Status']").get(0).getValue();
                    fields.add(new Record(participantID, status));

                }

                DataSourceFactory dataSourceFactory = APIUtil.defineDataSource("Participant Statuses", username, password);

                dataSourceFactory.addGrouping("ParticipantID");
                dataSourceFactory.addGrouping("Status");

                DataSourceOperationFactory dataSourceOperationFactory = dataSourceFactory.defineDataSource();

                DataSourceTarget dataSourceTarget = dataSourceOperationFactory.replaceRowsOperation();

                dataSourceTarget.flush();

                dataSourceTarget = dataSourceOperationFactory.addRowsOperation();

                for (int i = 0; i < fields.size(); i++) {
                    Record record = fields.get(i);
                    DataRow dataRow = dataSourceTarget.newRow();
                    dataRow.addValue("ParticipantID", record.getParticipantID());
                    dataRow.addValue("Status", record.getStatus());
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
        private String participantID;
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getParticipantID() {
            return participantID;
        }

        public void setParticipantID(String participantID) {
            this.participantID = participantID;
        }

        private Record(String participantID, String status) {
            this.participantID = participantID;
            this.status = status;
        }

        private Record() {

        }
    }
}

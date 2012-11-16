package com.easyinsight.sync.rescare.franklin;

import com.easyinsight.helper.*;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 10/8/12
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSyncThread extends Thread {
    public static final DateFormat eiDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    protected String username;
    protected String password;
    protected String reportId;
    protected String sourceName;

    public BaseSyncThread(String username, String password) {

        this.password = password;
        this.username = username;

    }

    abstract protected void setWhereClauses(Update u, Map<String, Object> rec);
    abstract protected DataSourceOperationFactory defineFields(DataSourceFactory dataSourceFactory);
    abstract protected void processIn(Node data, List<Map<String, Object>> records) throws Exception;

    @Override
    public void run() {
        try {

            HttpClient client = new HttpClient();
            client.getParams().setAuthenticationPreemptive(true);
            System.out.println(username + ":" + password);
            Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            GetMethod getMethod = new GetMethod("https://www.easy-insight.com/app/xml/reports/" + reportId);
            client.executeMethod(getMethod);
            Document doc = new Builder().build(getMethod.getResponseBodyAsStream());

            Nodes nodes = doc.query("/response/rows/row");
            List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < nodes.size(); i++) {
                Node rowNode = nodes.get(i);
                processIn(rowNode, records);
            }

            DataSourceFactory dataSourceFactory = APIUtil.defineDataSource(sourceName, username, password);
            DataSourceOperationFactory operationFactory = defineFields(dataSourceFactory);
            BulkUpdateDataSourceTarget ops = operationFactory.bulkUpdateRowsOperation();
            int count = 0;
            for(Map<String, Object> rec : records) {
                Update u = ops.createUpdate();
                DataRow r = u.newRow();
                for(String s : rec.keySet()) {
                    if(rec.get(s) instanceof String) {
                        r.addValue(s, (String) rec.get(s));
                    } else if (rec.get(s) instanceof Number) {
                        r.addValue(s, (Number) rec.get(s));
                    } else if (rec.get(s) instanceof Date) {
                        r.addValue(s, (Date) rec.get(s));
                    }
                }
                setWhereClauses(u, rec);

                if((count++) % 5000 == 0)
                    ops.flush();
            }
            ops.flush();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String getField(Node rec, String field) {
        return rec.query("value[@field='" + field +"']").get(0).getValue();
    }
}

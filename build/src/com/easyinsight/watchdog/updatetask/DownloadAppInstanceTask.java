package com.easyinsight.watchdog.updatetask;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tools.ant.BuildException;


/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 1:42:37 AM
 */
public class DownloadAppInstanceTask extends AppInstanceTask {
    

    public void execute() throws BuildException {
        try {
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setAuthenticationPreemptive(true);
            Credentials defaultcreds = new UsernamePasswordCredentials(getUserName(), getPassword());
            httpClient.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            for (String instance : getInstances()) {
                HttpMethod updateMethod = new GetMethod("http://" + instance + ":4000/?operation=download");
                httpClient.executeMethod(updateMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }


    
}

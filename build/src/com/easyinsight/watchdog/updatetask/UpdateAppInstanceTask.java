package com.easyinsight.watchdog.updatetask;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import java.security.SignatureException;
import java.text.MessageFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;


/**
 * User: James Boe
 * Date: Jan 27, 2009
 * Time: 1:42:37 AM
 */
public class UpdateAppInstanceTask extends AppInstanceTask {
    

    public void execute() throws BuildException {
        try {
            HttpClient httpClient = new HttpClient();            
            for (String instance : getInstances()) {
                HttpMethod updateMethod = new GetMethod("http://" + instance + ":4000/update?operation=update");
                httpClient.executeMethod(updateMethod);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }


    
}

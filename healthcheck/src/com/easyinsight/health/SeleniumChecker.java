package com.easyinsight.health;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 9/6/12
 * Time: 8:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class SeleniumChecker extends TimerTask {

    private String location;


    private static SeleniumChecker instance;

    public static void initialize() {
        instance = new SeleniumChecker();
        try {
            URL url = instance.getClass().getClassLoader().getResource("selenium.properties");
            Properties properties = new Properties();

            properties.load(new FileInputStream(new File(url.getFile())));

            instance.location = (String) properties.get("selenium.url");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static SeleniumChecker instance() {
        return instance;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(location);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setReadTimeout(15 * 1000);
            connection.connect();

            BufferedReader in = null;
            try {
                in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
            } catch (IOException e) {
                in = new BufferedReader(
                        new InputStreamReader(
                                connection.getErrorStream()));
            }

            StringBuilder result = new StringBuilder();

            String line;

            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();

            if(result.indexOf("All ok!") < 0) {
                HealthChecker.alert();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

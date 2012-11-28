package com.easyinsight.config;

import com.easyinsight.logging.LogClass;

import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * User: James Boe
 * Date: Sep 7, 2008
 * Time: 10:53:19 AM
 */
public class ConfigLoader {

    private String databaseHost;
    private String databasePort;
    private String databaseName;
    private String databaseUserName;
    private String databasePassword;

    private String billingUsername;
    private String billingPassword;
    private String billingKeyID;
    private String billingKey;

    private String merchantID;
    private String billingPublicKey;
    private String billingPrivateKey;

    private String googleUserName;
    private String googlePassword;
    private String outputLogPath;

    private boolean taskRunner;

    public String getOutputLogPath() {
        return outputLogPath;
    }

    public void setOutputLogPath(String outputLogPath) {
        this.outputLogPath = outputLogPath;
    }

    public boolean isTaskRunner() {
        return taskRunner;
    }

    public void setTaskRunner(boolean taskRunner) {
        this.taskRunner = taskRunner;
    }

    public String getGoogleUserName() {
        return googleUserName;
    }

    public void setGoogleUserName(String googleUserName) {
        this.googleUserName = googleUserName;
    }

    public String getGooglePassword() {
        return googlePassword;
    }

    public void setGooglePassword(String googlePassword) {
        this.googlePassword = googlePassword;
    }

    public String getRedirectLocation() {
        return redirectLocation;
    }

    public void setRedirectLocation(String redirectLocation) {
        this.redirectLocation = redirectLocation;
    }

    private String redirectLocation;

    public String getBillingKeyID() {
        return billingKeyID;
    }

    public void setBillingKeyID(String billingKeyID) {
        this.billingKeyID = billingKeyID;
    }

    public String getBillingPassword() {
        return billingPassword;
    }

    public void setBillingPassword(String billingPassword) {
        this.billingPassword = billingPassword;
    }

    public String getBillingUsername() {
        return billingUsername;
    }

    public void setBillingUsername(String billingUsername) {
        this.billingUsername = billingUsername;
    }

    public String getBillingKey() {
        return billingKey;
    }

    public void setBillingKey(String billingKey) {
        this.billingKey = billingKey;
    }

    public Boolean isProduction() {
        return production;
    }

    public void setProduction(Boolean production) {
        this.production = production;
    }

    private Boolean production;

    private static ConfigLoader instance;

    public static ConfigLoader instance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    private String localURL;

    public String getLocalURL() {
        return localURL;
    }

    public void setLocalURL(String localURL) {
        this.localURL = localURL;
    }

    public String getBillingPrivateKey() {
        return billingPrivateKey;
    }

    public void setBillingPrivateKey(String billingPrivateKey) {
        this.billingPrivateKey = billingPrivateKey;
    }

    public String getBillingPublicKey() {
        return billingPublicKey;
    }

    public void setBillingPublicKey(String billingPublicKey) {
        this.billingPublicKey = billingPublicKey;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    private ConfigLoader() {
        try {
            URL url = getClass().getClassLoader().getResource("eiconfig.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(url.getFile())));
            databaseHost = (String) properties.get("database.host");
            databasePort = (String) properties.get("database.port");
            databaseName = (String) properties.get("database.name");
            databaseUserName = (String) properties.get("database.username");
            databasePassword = (String) properties.get("database.password");

            billingUsername = (String) properties.get("billing.username");
            billingPassword = (String) properties.get("billing.password");
            billingKeyID = (String) properties.get("billing.keyid");
            billingKey = (String) properties.get("billing.key");
            redirectLocation = (String) properties.get("billing.redirectLocation");
            outputLogPath = (String) properties.get("report.log.path");

            googleUserName = (String) properties.get("google.username");
            googlePassword = (String) properties.get("google.password");

            billingPublicKey = (String) properties.get("billing.blue.key");
            billingPrivateKey = (String) properties.get("billing.blue.secretKey");
            merchantID = (String) properties.get("billing.blue.merchantKey");

            taskRunner = Boolean.valueOf((String) properties.get("taskrunner"));

            localURL = (String) properties.get("localurl");

            production = Boolean.valueOf((String) properties.get("production"));
            
        } catch (IOException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}

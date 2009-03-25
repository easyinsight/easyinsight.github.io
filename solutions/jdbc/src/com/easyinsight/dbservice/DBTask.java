package com.easyinsight.dbservice;

import com.easyinsight.dbservice.validated.*;

import java.util.*;
import java.sql.*;
import java.sql.Date;
import java.net.URL;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * User: James Boe
 * Date: Jan 16, 2009
 * Time: 10:24:35 PM
 */
public class DBTask extends TimerTask {

    private String eiHost = System.getProperty("ei.target", "www.easy-insight.com");
    private IStorage storage;

    public DBTask() {
        String storageMechanism = System.getProperty("ei.storage", "xml");
        if ("database".equals(storageMechanism)) {
            System.out.println("using jdbc storage...");
            storage = new DerbyBackedStorage();
        } else {
            System.out.println("using xml storage...");
            storage = new XMLBackedStorage();
        }
        run();
    }

    public void run() {
        try {
            List<QueryConfiguration> queryConfigs = storage.getQueryConfigurations();
            EIConfiguration eiConfiguration = storage.getEIConfiguration();
            DBConfiguration dbConfiguration = storage.getDBConfiguration();
            if (eiConfiguration != null && dbConfiguration != null) {
                URL url = new URL("http://" + this.eiHost + "/app/services/ValidatedPublishBasic");
                BasicAuthValidatedPublish service = new BasicAuthValidatingPublishServiceServiceLocator().getBasicAuthValidatingPublishServicePort(url);
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setUsername(eiConfiguration.getUserName());
                ((BasicAuthValidatingPublishServiceServiceSoapBindingStub)service).setPassword(eiConfiguration.getPassword());
                for (QueryConfiguration queryConfiguration : queryConfigs) {
                    QueryValidatedPublish publish = new QueryValidatedPublish(queryConfiguration, service);
                    System.out.println("Running " + queryConfiguration.getName());
                    publish.execute(dbConfiguration);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}

package com.easyinsight.dbservice;

/**
 * User: James Boe
 * Date: Feb 1, 2009
 * Time: 10:52:22 AM
 */
public class SingletonState {
    private static SingletonState instance;

    private DBConfiguration dbConfiguration;
    private EIConfiguration eiConfiguration;

    public static void initialize() {
        instance = new SingletonState();
    }

    public static SingletonState getInstance() {
        return instance;
    }

    
}

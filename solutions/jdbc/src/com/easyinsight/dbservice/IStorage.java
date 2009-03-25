package com.easyinsight.dbservice;

import java.util.List;

/**
 * User: James Boe
 * Date: Mar 23, 2009
 * Time: 11:56:30 AM
 */
public interface IStorage {
    public List<QueryConfiguration> getQueryConfigurations();
    public EIConfiguration getEIConfiguration();
    public DBConfiguration getDBConfiguration();
}

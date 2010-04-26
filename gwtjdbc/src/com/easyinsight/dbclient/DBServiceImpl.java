package com.easyinsight.dbclient;

import com.easyinsight.client.DBService;
import com.easyinsight.client.GWTDataSource;
import com.easyinsight.client.GWTQuery;
import com.easyinsight.client.GWTUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 1:40:17 PM
 */
public class DBServiceImpl extends RemoteServiceServlet implements DBService {

    private static final String USER_SESSION = "GWTAppUser";

    public GWTUser checkLogin(String userName, String password) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean anyUserExists() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GWTDataSource[] getDataSources() {
        return new GWTDataSource[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public GWTQuery[] getQueriesForDataSource(GWTDataSource dataSource) {
        return new GWTQuery[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String createUser(GWTUser user, String password, String eiPassword) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createDataSource(GWTDataSource dataSource) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void saveQuery(GWTQuery query) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteQuery(GWTQuery query) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteDataSource(GWTDataSource dataSource) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void testQuery(GWTQuery query) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

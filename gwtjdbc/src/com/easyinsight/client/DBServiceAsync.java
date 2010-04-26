package com.easyinsight.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 1:40:09 PM
 */
public interface DBServiceAsync {

    void anyUserExists(AsyncCallback<Boolean> async);

    void getDataSources(AsyncCallback<GWTDataSource[]> async);

    // Sample interface method of remote interface
    void checkLogin(String userName, String password, AsyncCallback<GWTUser> async);

    void getQueriesForDataSource(GWTDataSource dataSource, AsyncCallback<GWTQuery[]> async);

    void createDataSource(GWTDataSource dataSource, AsyncCallback<Void> async);

    void saveQuery(GWTQuery query, AsyncCallback<Void> async);

    void deleteQuery(GWTQuery query, AsyncCallback<Void> async);

    void deleteDataSource(GWTDataSource dataSource, AsyncCallback<Void> async);

    void testQuery(GWTQuery query, AsyncCallback<Void> async);

    void createUser(GWTUser user, String password, String eiPassword, AsyncCallback<String> async);
}

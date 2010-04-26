package com.easyinsight.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 1:38:51 PM
 */
@RemoteServiceRelativePath("DBService")
public interface DBService extends RemoteService {
    // Sample interface method of remote interface
    GWTUser checkLogin(String userName, String password);
    boolean anyUserExists();
    GWTDataSource[] getDataSources();
    GWTQuery[] getQueriesForDataSource(GWTDataSource dataSource);
    String createUser(GWTUser user, String password, String eiPassword);
    void createDataSource(GWTDataSource dataSource);
    void saveQuery(GWTQuery query);
    void deleteQuery(GWTQuery query);
    void deleteDataSource(GWTDataSource dataSource);
    void testQuery(GWTQuery query);

    /**
     * Utility/Convenience class.
     * Use BlahService.App.getInstance () to access static instance of BlahServiceAsync
     */
    public static class App {
        private static DBServiceAsync ourInstance = GWT.create(DBService.class);

        public static synchronized DBServiceAsync getInstance() {
            return ourInstance;
        }
    }
}

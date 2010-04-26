package com.easyinsight.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * User: jamesboe
 * Date: Apr 23, 2010
 * Time: 10:55:36 AM
 */
public class DBCore implements EntryPoint {

    private static DBCore core;

    public void onModuleLoad() {
        core = this;
        AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {

            public void onFailure(Throwable caught) {

            }

            public void onSuccess(Boolean result) {
                if (result) {
                    RootPanel.get("core").add(new HomeScreen());
                } else {
                    RootPanel.get("core").add(new FirstUserScreen());
                }
            }
        };
        getService().anyUserExists(callback);    
    }

    public static DBCore get() {
        return core;
    }

    protected DBServiceAsync getService() {
        DBServiceAsync svc = (DBServiceAsync) GWT.create(DBService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) svc;
        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "DBService");
        return svc;
    }

    public void toHomeScreen() {
        RootPanel.get("core").clear();
        RootPanel.get("core").add(new HomeScreen());
    }

    public void newQuery() {
        RootPanel.get("core").clear();
        RootPanel.get("core").add(new QueryScreen());
    }

    public void newDataSource() {
        RootPanel.get("core").clear();
        //RootPanel.get("core").add(new DataSourceScreen());
    }

    public void openQuery() {

    }

    public void openDataSource() {
        
    }

    public void toLogin() {

    }

    public void logout() {
        
    }
}

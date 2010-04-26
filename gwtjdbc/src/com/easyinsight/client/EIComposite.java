package com.easyinsight.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Composite;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 2:12:30 PM
 */
public class EIComposite extends Composite {
    protected DBServiceAsync getService() {
        DBServiceAsync svc = (DBServiceAsync) GWT.create(DBService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) svc;
        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "DBService");
        return svc;
    }
}

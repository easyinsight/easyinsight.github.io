package com.easyinsight.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 2:20:26 PM
 */
public class HomeScreen extends EIComposite {
    public HomeScreen() {
        VerticalPanel verticalPanel = new VerticalPanel();
        Button addDataSourceButton = new Button();
        addDataSourceButton.setText("Add Data Source...");
        addDataSourceButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                DBCore.get().newDataSource();    
            }
        });
        verticalPanel.add(addDataSourceButton);

    }
}

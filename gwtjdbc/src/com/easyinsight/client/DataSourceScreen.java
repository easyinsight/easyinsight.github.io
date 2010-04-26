package com.easyinsight.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 2:20:59 PM
 */
public class DataSourceScreen extends EIComposite {
    private RadioButton mysqlButton = new RadioButton("queryGroup", "MySQL");
    private RadioButton jdbcButton = new RadioButton("queryGroup", "JDBC");

    private GWTDataSource gwtDataSource;

    private VerticalPanel editorPanel;

    public DataSourceScreen(GWTDataSource gwtDataSource) {
        if (gwtDataSource != null) {

        }
        VerticalPanel verticalPanel = new VerticalPanel();
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(mysqlButton);
        horizontalPanel.add(jdbcButton);
        verticalPanel.add(horizontalPanel);
        editorPanel = new VerticalPanel();
        verticalPanel.add(editorPanel);
        mysqlButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

            }
        });
        jdbcButton.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {

            }
        });
    }
}

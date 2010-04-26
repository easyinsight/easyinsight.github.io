package com.easyinsight.client;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 5:00:10 PM
 */
public class MySQLSourceScreen extends EIComposite {

    private TextBox hostBox = new TextBox();
    private TextBox portBox = new TextBox();
    private TextBox databaseNameBox = new TextBox();
    private TextBox userNameBox = new TextBox();
    private TextBox passwordBox = new PasswordTextBox();

    public MySQLSourceScreen() {
        Grid grid = new Grid(5, 2);
        grid.setWidget(0, 0, new Label("Host:"));
        grid.setWidget(0, 1, hostBox);
        grid.setWidget(1, 0, new Label("Port:"));
        grid.setWidget(1, 1, portBox);
        grid.setWidget(2, 0, new Label("Database Name:"));
        grid.setWidget(2, 1, databaseNameBox);
        grid.setWidget(3, 0, new Label("Database User Name:"));
        grid.setWidget(3, 1, userNameBox);
        grid.setWidget(4, 0, new Label("Database Password:"));        
        grid.setWidget(4, 1, passwordBox);
        initWidget(grid);
    }
}

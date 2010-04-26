package com.easyinsight.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User: jamesboe
 * Date: Apr 24, 2010
 * Time: 2:11:18 PM
 */
public class FirstUserScreen extends EIComposite {
    private TextBox userNameText = new TextBox();
    private TextBox eiUserName = new TextBox();
    private PasswordTextBox eiPassword = new PasswordTextBox();
    private PasswordTextBox passwordText = new PasswordTextBox();
    private Label lblError=new Label();

    public FirstUserScreen() {
        // Lets add a grid to hold all our widgets
        Grid grid = new Grid(6, 2);
        //Set the error label
        grid.setWidget(4, 1, lblError);
        //Add the Label for the username
        grid.setWidget(0, 0, new Label("Username"));
        //Add the UserName textBox
        grid.setWidget(0, 1, userNameText);
        //Add the label for password
        grid.setWidget(1, 0, new Label("Password"));
        //Add the password widget
        grid.setWidget(1, 1, passwordText);
        //Add the label for password
        grid.setWidget(2, 0, new Label("EI User Name"));
        //Add the password widget
        grid.setWidget(2, 1, eiUserName);
        //Add the label for password
        grid.setWidget(3, 0, new Label("EI Password"));
        //Add the password widget
        grid.setWidget(3, 1, eiPassword);
        //Create a button
        Button btnLogin = new Button("login");
        //Add the Login button to the form
        grid.setWidget(5, 1, btnLogin);

        /** Add a click listener which is called
         when the button is clicked */
        btnLogin.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                checkLogin(userNameText.getText(), passwordText.getText(), eiUserName.getText(), eiPassword.getText());
            }
        });
        initWidget(grid);

    }

    private void checkLogin(String userName, String password, String eiUserName, String eiPassword) {
        AsyncCallback<String> callback = new AsyncCallback<String>() {

            public void onFailure(Throwable ex) {
                setErrorText("Error " + ex.getMessage());
            }

            public void onSuccess(String result) {
                if (result == null) {
                    
                    // The user is authenticated, Set the home screen
                    //Blah.get().setHomeScreen(user);
                } else {
                    setErrorText(result);
                }
            }
        };
        GWTUser gwtUser = new GWTUser();
        gwtUser.setUserName(userName);
        gwtUser.setEiName(eiUserName);
        getService().createUser(gwtUser, password, eiPassword, callback);

    }

    private void setErrorText(String errorMessage) {
        lblError.setText(errorMessage);
    }
}

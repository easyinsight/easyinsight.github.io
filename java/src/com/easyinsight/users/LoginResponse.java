package com.easyinsight.users;

/**
 * User: jamesboe
 * Date: 3/25/12
 * Time: 6:45 PM
 */
public class LoginResponse {
    private UserServiceResponse userServiceResponse;
    private String token;
    private String userIDString;

    public LoginResponse() {
    }

    public LoginResponse(UserServiceResponse userServiceResponse) {
        this.userServiceResponse = userServiceResponse;
    }

    public LoginResponse(String token, String userIDString, UserServiceResponse userServiceResponse) {
        this.token = token;
        this.userIDString = userIDString;
        this.userServiceResponse = userServiceResponse;
    }

    public UserServiceResponse getUserServiceResponse() {
        return userServiceResponse;
    }

    public void setUserServiceResponse(UserServiceResponse userServiceResponse) {
        this.userServiceResponse = userServiceResponse;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserIDString() {
        return userIDString;
    }

    public void setUserIDString(String userIDString) {
        this.userIDString = userIDString;
    }
}

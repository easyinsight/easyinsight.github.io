package com.easyinsight.export;

/**
* User: jamesboe
* Date: 2/28/13
* Time: 1:26 PM
*/
class UserInfo {
    String email;
    String firstName;
    String lastName;
    String userName;
    long userID;
    boolean accountAdmin;
    long personaID;

    UserInfo(String email, String firstName, String lastName, String userName, long userID, boolean accountAdmin, long personaID) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.userID = userID;
        this.accountAdmin = accountAdmin;
        this.personaID = personaID;
    }
}

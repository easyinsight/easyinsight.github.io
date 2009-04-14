package com.easyinsight.users;

import org.jetbrains.annotations.NotNull;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 8:11:37 PM
 */
public interface IUserService {
    //@NotNull
    //public UserServiceResponse createUser(User user);

    @NotNull
    UserServiceResponse authenticate(String userName, String password);

    //public void updateUser(String userName, String fullName, String email);

    //void createAccount(Account testAccount);

    //void removeUser(Account testAccount, User newUser);
}

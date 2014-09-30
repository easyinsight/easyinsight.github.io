package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.html.RedirectUtil;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: 9/15/14
 * Time: 11:51 AM
 */
public class ReactivationAccount {

    private class UserInfo {
        private String email;
        private long userID;
        private String firstName;
        private String lastName;

        private UserInfo(String email, long userID, String firstName, String lastName) {
            this.email = email;
            this.userID = userID;
            this.firstName = firstName;
            this.lastName = lastName;
        }
    }

    public void generate(long accountID, EIConnection conn, String bucket) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT EMAIL, USER_ID, FIRST_NAME, USER.NAME, ACCOUNT.NAME FROM USER, ACCOUNT WHERE ACCOUNT.ACCOUNT_ID = ? AND ACCOUNT_ADMIN = ? AND ACCOUNT.ACCOUNT_ID = USER.ACCOUNT_ID");
        stmt.setLong(1, accountID);
        stmt.setBoolean(2, true);
        List<UserInfo> userInfos = new ArrayList<>();
        ResultSet rs = stmt.executeQuery();
        String accountName = "";
        while (rs.next()) {
            String email = rs.getString(1);
            long userID = rs.getLong(2);
            String firstName = rs.getString(3);
            String lastName = rs.getString(4);
            accountName = rs.getString(5);
            UserInfo userInfo = new UserInfo(email, userID, firstName, lastName);
            userInfos.add(userInfo);
        }
        PreparedStatement aliasStmt = conn.prepareStatement("UPDATE USER SET EMAIL = ?, USERNAME = ?, OPT_IN_EMAIL = ? WHERE USER_ID = ?");
        PreparedStatement aliasAccountStmt = conn.prepareStatement("UPDATE ACCOUNT SET NAME = ? WHERE ACCOUNT_ID = ?");
        aliasAccountStmt.setString(1, accountName + "reactivation");
        aliasAccountStmt.setLong(2, accountID);
        aliasAccountStmt.executeUpdate();
        aliasAccountStmt.close();
        PreparedStatement createReactivationStmt = conn.prepareStatement("INSERT INTO reactivation_data (email_address, user_id, " +
                "activation_key, date_generated, became_user_id, first_name, last_name, bucket) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)");
        for (UserInfo userInfo : userInfos) {
            aliasStmt.setString(1, userInfo.email + "reactivation");
            aliasStmt.setString(2, userInfo.email + "reactivation");
            aliasStmt.setBoolean(3, false);
            aliasStmt.setLong(4, userInfo.userID);
            aliasStmt.executeUpdate();

            createReactivationStmt.setString(1, userInfo.email);
            createReactivationStmt.setLong(2, userInfo.userID);
            String activationKey = RandomTextGenerator.generateText(25);
            createReactivationStmt.setString(3, activationKey);
            createReactivationStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            createReactivationStmt.setLong(5, 0);
            createReactivationStmt.setString(6, userInfo.firstName);
            createReactivationStmt.setString(7, userInfo.lastName);
            createReactivationStmt.setString(8, bucket);
            createReactivationStmt.execute();
            System.out.println("https://localhost:4443/app/reactivation?activationKey="+activationKey);
        }
        aliasStmt.close();
        createReactivationStmt.close();
        stmt.close();
    }

    public void setupAccount(String reactivationKey, HttpServletRequest request, HttpServletResponse response) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT email_address, date_generated, became_user_id, " +
                    "first_name, last_name FROM reactivation_data WHERE activation_key = ?");
            stmt.setString(1, reactivationKey);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String email = rs.getString(1);
                Date dateGenerated = rs.getDate(2);
                long becameUserID = rs.getLong(3);
                String firstName = rs.getString(4);
                String lastName = rs.getString(5);
                if (becameUserID != 0) {
                    response.sendRedirect(RedirectUtil.getURL(request, "/app/invalidActivation.jsp"));
                } else {
                    com.easyinsight.users.UserTransferObject user = new com.easyinsight.users.UserTransferObject();
                    user.setUserName(email);
                    user.setFirstName(firstName);
                    user.setName(lastName);
                    user.setEmail(email);
                    user.setAccountAdmin(true);
                    user.setOptInEmail(true);
                    user.setInitialSetupDone(false);
                    user.setInvoiceRecipient(true);
                    user.setAnalyst(true);
                    user.setTestAccountVisible(true);
                    com.easyinsight.users.AccountTransferObject account = new com.easyinsight.users.AccountTransferObject();
                    account.setAccountType(Account.PROFESSIONAL);
                    account.setAccountState(Account.TRIAL);
                    account.setDefaultHTML(true);
                    account.setName(email);
                    String exists = new com.easyinsight.users.UserService().doesUserExist(user.getUserName(), user.getEmail(), account.getName());
                    if (exists == null) {
                        String url = RedirectUtil.getURL(request, "/app/user/reactivation.jsp");

                        String password = RandomTextGenerator.generateText(15);

                        final long accountID = new com.easyinsight.users.UserService().createAccount(user, account, password, url);



                        UserServiceResponse userServiceResponse = new UserService().authenticate(email, password, false);

                        PreparedStatement updateStmt = conn.prepareStatement("UPDATE reactivation_data SET became_user_id = ? WHERE email_address = ?");
                        updateStmt.setLong(1, userServiceResponse.getUserID());
                        updateStmt.setString(2, email);
                        updateStmt.executeUpdate();

                        HttpSession session = request.getSession();
                        session.invalidate();
                        session = request.getSession(true);
                        SecurityUtil.populateSession(session, userServiceResponse);
                        final long userID = userServiceResponse.getUserID();
                        final String curEmail = email;
                        final String curFirstName = firstName;

                        new Thread(new Runnable() {

                            public void run() {
                                EIConnection conn = Database.instance().getConnection();
                                try {
                                    new AccountMemberInvitation().sendWelcomeEmail(curEmail, conn, userID, curFirstName, accountID);
                                } catch (Exception e) {
                                    LogClass.error(e);
                                } finally {
                                    Database.closeConnection(conn);
                                }
                            }
                        }).start();
                        response.sendRedirect(url);
                        //UserService.checkAccountStateOnLogin(session, userServiceResponse, request, response, url);
                    } else {
                        response.sendRedirect(RedirectUtil.getURL(request, "/app/invalidActivation.jsp"));
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}

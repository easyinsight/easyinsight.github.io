package com.easyinsight.users;

import com.easyinsight.database.EIConnection;

import com.easyinsight.email.SendGridEmail;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.PasswordService;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.email.AccountMemberInvitation;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 3:14:53 PM
 */
public class EIAccountManagementService {

    public void applyReactivation(int accountID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_ID = ?");
            updateStmt.setInt(1, Account.REACTIVATION_POSSIBLE);
            updateStmt.setLong(2, accountID);
            updateStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void specialOffer() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);

            PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.USER_ID FROM USER, ACCOUNT WHERE USER.OPT_IN_EMAIL = ? AND " +
                    "USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND USER.ACCOUNT_ADMIN = ? AND ACCOUNT_STATE = ? AND ACCOUNT.CREATION_DATE < ?");
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_STATE = ?");
            queryStmt.setBoolean(1, true);
            queryStmt.setBoolean(2, true);
            queryStmt.setLong(3, Account.DELINQUENT);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 2011);
            cal.set(Calendar.MONTH, Calendar.MAY);
            cal.set(Calendar.DAY_OF_YEAR, 30);
            queryStmt.setTimestamp(4, new Timestamp(cal.getTimeInMillis()));
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long accountID = rs.getLong(1);
                updateStmt.setInt(1, Account.REACTIVATION_POSSIBLE);
                updateStmt.setLong(2, accountID);
                updateStmt.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void comeBackEmail(String htmlText) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        SendGridEmail sendGridEmail = new SendGridEmail();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT EMAIL, USER.USER_ID FROM USER, ACCOUNT WHERE USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND " +
                    "ACCOUNT.account_state = ?");
            queryStmt.setInt(1, Account.REACTIVATION_POSSIBLE);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                String email = rs.getString(1);
                long userID = rs.getLong(2);
                PreparedStatement queryUnsubscribeStmt = conn.prepareStatement("SELECT unsubscribe_key from user_unsubscribe_key WHERE USER_ID = ?");
                PreparedStatement insertKeyStmt = conn.prepareStatement("INSERT INTO USER_UNSUBSCRIBE_KEY (USER_ID, UNSUBSCRIBE_KEY) VALUES (?, ?)");
                queryUnsubscribeStmt.setLong(1, SecurityUtil.getUserID());
                ResultSet unsubscribeRS = queryUnsubscribeStmt.executeQuery();
                String unsubscribeKey;
                if (unsubscribeRS.next()) {
                    unsubscribeKey = unsubscribeRS.getString(1);
                } else {
                    unsubscribeKey = RandomTextGenerator.generateText(12);
                    insertKeyStmt.setLong(1, SecurityUtil.getUserID());
                    insertKeyStmt.setString(2, unsubscribeKey);
                    insertKeyStmt.execute();
                }
                String emailBody = htmlText.replace("{0}", "https://www.easy-insight.com/app/unsubscribe?user=" + unsubscribeKey);
                try {
                    sendGridEmail.sendEmail(userID, email, "ComeBack", "Take another try with Easy Insight with a renewed 15 day free trial!", emailBody, conn);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void testDeliver(String htmlText, String subject) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT unsubscribe_key from user_unsubscribe_key WHERE USER_ID = ?");
            PreparedStatement insertKeyStmt = conn.prepareStatement("INSERT INTO USER_UNSUBSCRIBE_KEY (USER_ID, UNSUBSCRIBE_KEY) VALUES (?, ?)");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet rs = queryStmt.executeQuery();
            String unsubscribeKey;
            if (rs.next()) {
                unsubscribeKey = rs.getString(1);
            } else {
                unsubscribeKey = RandomTextGenerator.generateText(12);
                insertKeyStmt.setLong(1, SecurityUtil.getUserID());
                insertKeyStmt.setString(2, unsubscribeKey);
                insertKeyStmt.execute();
            }
            String emailBody = htmlText.replace("{0}", "https://www.easy-insight.com/app/unsubscribe?user=" + unsubscribeKey);
            PreparedStatement userQueryStmt = conn.prepareStatement("SELECT email from USER where USER_ID = ?");
            userQueryStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet userRS = userQueryStmt.executeQuery();
            userRS.next();
            String emailAddress = userRS.getString(1);
            SendGridEmail sendGridEmail = new SendGridEmail();
            sendGridEmail.sendEmail(SecurityUtil.getUserID(), emailAddress, "Newsletter Test", subject, emailBody, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void sendNewsletter(String htmlText, String subject) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        SendGridEmail sendGridEmail = new SendGridEmail();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT EMAIL, USER.USER_ID FROM USER, ACCOUNT WHERE USER.opt_in_email = ? AND " +
                    "USER.ACCOUNT_ID = ACCOUNT.ACCOUNT_ID AND ACCOUNT.account_state != ? AND ACCOUNT.account_state != ?");
            queryStmt.setBoolean(1, true);
            queryStmt.setInt(2, Account.CLOSING);
            queryStmt.setInt(3, Account.CLOSED);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                String email = rs.getString(1);
                long userID = rs.getLong(2);
                PreparedStatement queryUnsubscribeStmt = conn.prepareStatement("SELECT unsubscribe_key from user_unsubscribe_key WHERE USER_ID = ?");
                PreparedStatement insertKeyStmt = conn.prepareStatement("INSERT INTO USER_UNSUBSCRIBE_KEY (USER_ID, UNSUBSCRIBE_KEY) VALUES (?, ?)");
                queryUnsubscribeStmt.setLong(1, SecurityUtil.getUserID());
                ResultSet unsubscribeRS = queryUnsubscribeStmt.executeQuery();
                String unsubscribeKey;
                if (unsubscribeRS.next()) {
                    unsubscribeKey = unsubscribeRS.getString(1);
                } else {
                    unsubscribeKey = RandomTextGenerator.generateText(12);
                    insertKeyStmt.setLong(1, SecurityUtil.getUserID());
                    insertKeyStmt.setString(2, unsubscribeKey);
                    insertKeyStmt.execute();
                }
                String emailBody = htmlText.replace("{0}", "https://www.easy-insight.com/app/unsubscribe?user=" + unsubscribeKey);
                try {
                    sendGridEmail.sendEmail(userID, email, "Newsletter", subject, emailBody, conn);
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateTrial(long accountID, Date newTrialDate) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new AccountActivityStorage().updateTrialTime(accountID, conn, newTrialDate);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    @NotNull
    public UserServiceResponse authenticateAdmin(String userName, String password) {
        UserServiceResponse userServiceResponse;
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        List results;
        try {
            conn.setAutoCommit(false);
            results = session.createQuery("from User where userName = ?").setString(0, userName).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                String actualPassword = user.getPassword();
                String encryptedPassword = PasswordService.getInstance().encrypt(password, user.getHashSalt(), user.getHashType());
                if (encryptedPassword.equals(actualPassword)) {
                    List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                    Account account = (Account) accountResults.get(0);
                    if (account.getAccountType() != Account.ADMINISTRATOR) {
                        throw new SecurityException();
                    }
                    userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
                }
            } else {
                userServiceResponse = new UserServiceResponse(false, "Incorrect user name, please try again.");
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public void adminUpdate(AccountAdminTO accountTO) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, accountTO.getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            Account updatedAccount = accountTO.toAccount(account);
            if (accountTO.getTrialDate() != null) {
                new AccountActivityStorage().updateTrialTime(accountTO.getAccountID(), conn, accountTO.getTrialDate());
            }
            session.update(updatedAccount);
            session.flush();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account) {
        User user = userTransferObject.toUser();
        user.setAccount(account);        
        user.setPassword(PasswordService.getInstance().encrypt(password, user.getHashSalt(), "SHA-256"));
        user.setHashType("SHA-256");
        return user;
    }

    public void eiActivateBizAccount(long accountID, UserTransferObject adminUser, boolean preserveConsultants) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            String password = RandomTextGenerator.generateText(12);
            User user = createInitialUser(adminUser, password, account);
            user.setAccount(account);
            account.addUser(user);
            session.save(user);
            account.setAccountState(Account.ACTIVE);
            session.update(account);
            session.getTransaction().commit();
            new AccountMemberInvitation().newProAccount(user.getEmail(), user.getUserName(), password);
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public List<AccountAdminTO> getAccounts() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        List<AccountAdminTO> accounts = new ArrayList<AccountAdminTO>();
        AccountActivityStorage storage = new AccountActivityStorage();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            List results = session.createQuery("from Account").list();
            for (Object obj : results) {
                Account account = (Account) obj;
                AccountAdminTO accountTO = account.toAdminTO();
                if (account.getAccountState() == Account.TRIAL) {
                    Date trialDate = storage.getTrialTime(account.getAccountID(), conn);
                    accountTO.setTrialDate(trialDate);
                }
                accounts.add(accountTO);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
        return accounts;
    }

}

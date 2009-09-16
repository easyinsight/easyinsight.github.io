package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.PasswordService;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.groups.Group;
import com.easyinsight.groups.GroupStorage;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 3:14:53 PM
 */
public class EIAccountManagementService {

    @NotNull
    public UserServiceResponse authenticateAdmin(String userName, String password) {
        try {
            UserServiceResponse userServiceResponse;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.getTransaction().begin();
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                if (results.size() > 0) {
                    User user = (User) results.get(0);
                    String actualPassword = user.getPassword();
                    String encryptedPassword = PasswordService.getInstance().encrypt(password);
                    if (encryptedPassword.equals(actualPassword)) {
                        List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, user.getAccount().getAccountID()).list();
                        Account account = (Account) accountResults.get(0);
                        if (account.getAccountType() != Account.ADMINISTRATOR) {
                            throw new SecurityException();
                        }
                        userServiceResponse = new UserServiceResponse(true, user.getUserID(), user.getAccount().getAccountID(), user.getName(),
                                user.getAccount().getAccountType(), account.getMaxSize(), user.getEmail(), user.getUserName(), encryptedPassword, user.isAccountAdmin(), user.isDataSourceCreator(), user.isInsightCreator(), (user.getAccount().isBillingInformationGiven() != null && user.getAccount().isBillingInformationGiven()), user.getAccount().getAccountState());
                        // FlexContext.getFlexSession().getRemoteCredentials();
                    } else {
                        userServiceResponse = new UserServiceResponse(false, "Incorrect password, please try again.");
                    }
                } else {
                    userServiceResponse = new UserServiceResponse(false, "Incorrect user name, please try again.");
                }
                session.getTransaction().commit();
            } finally {
                session.close();
            }
            return userServiceResponse;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void eiApproveConsultant(long consultantID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where guestUserID = ?").setLong(0, consultantID).list();
            Consultant consultant = (Consultant) results.get(0);
            consultant.setState(Consultant.ACTIVE);
            session.update(consultant);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public void adminUpdate(AccountAdminTO accountTO) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List accountResults = session.createQuery("from Account where accountID = ?").setLong(0, accountTO.getAccountID()).list();
            Account account = (Account) accountResults.get(0);
            Account updatedAccount = accountTO.toAccount();
            updatedAccount.setUsers(account.getUsers());
            updatedAccount.setLicenses(account.getLicenses());
            session.update(updatedAccount);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private void configureNewAccount(Account account) {
        if (account.getAccountType() == Account.ENTERPRISE) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(5);
            account.setMaxSize(1000000000);
        } else if (account.getAccountType() == Account.PROFESSIONAL) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(1);
            account.setMaxSize(200000000);
        } else if (account.getAccountType() == Account.INDIVIDUAL) {
            account.setAccountState(Account.BETA);
            account.setMaxUsers(1);
            account.setMaxSize(20000000);
        } else if (account.getAccountType() == Account.FREE) {
            account.setAccountState(Account.ACTIVE);
            account.setMaxUsers(1);
            account.setMaxSize(1000000);
        }
    }

    public void eiNewBizAccount(AccountTransferObject accountTransferObject, UserTransferObject initialUser) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        Account account;
        User user;
        try {
            session.getTransaction().begin();
            account = accountTransferObject.toAccount();
            configureNewAccount(account);
            account.setAccountState(Account.ACTIVE);
            initialUser.setAccountAdmin(true);
            initialUser.setDataSourceCreator(true);
            initialUser.setInsightCreator(true);
            String password = RandomTextGenerator.generateText(12);
            user = createInitialUser(initialUser, password, account);
            account.addUser(user);
            session.save(account);
            user.setAccount(account);
            session.update(user);
            session.getTransaction().commit();
            new AccountMemberInvitation().newProAccount(user.getEmail(), user.getUserName(), password);
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        long groupID;
        Connection conn = Database.instance().getConnection();
        try {
            Group group = new Group();
            group.setName(account.getName());
            group.setPubliclyVisible(false);
            group.setPubliclyJoinable(false);
            group.setDescription("This group was automatically created to act as a location for exposing data to all users in the account.");
            groupID = new GroupStorage().addGroup(group, user.getUserID(), conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            account.setGroupID(groupID);
            session.update(account);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    private User createInitialUser(UserTransferObject userTransferObject, String password, Account account) {
        User user = new User();
        user.setAccountAdmin(userTransferObject.isAccountAdmin());
        user.setAccount(account);
        user.setDataSourceCreator(userTransferObject.isDataSourceCreator());
        user.setEmail(userTransferObject.getEmail());
        user.setInsightCreator(userTransferObject.isInsightCreator());
        user.setName(userTransferObject.getName());
        user.setUserName(userTransferObject.getUserName());
        user.setPassword(PasswordService.getInstance().encrypt(password));
        return user;
    }

    public void eiActivateBizAccount(long accountID, UserTransferObject adminUser, boolean preserveConsultants) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Account where accountID = ?").setLong(0, accountID).list();
            Account account = (Account) results.get(0);
            if (preserveConsultants) {
                for (Consultant consultant : account.getGuestUsers()) {
                    consultant.getUser().setAccountAdmin(false);
                    consultant.getUser().setDataSourceCreator(false);
                    consultant.getUser().setInsightCreator(false);
                }
            } else {
                for (Consultant consultant : account.getGuestUsers()) {
                    session.delete(consultant);
                }
                account.setGuestUsers(new ArrayList<Consultant>());
            }
            String password = RandomTextGenerator.generateText(12);
            User user = createInitialUser(adminUser, password, account);
            user.setAccount(account);
            account.addUser(user);
            session.save(user);
            account.setAccountState(Account.ACTIVE);
            session.update(account);
            session.getTransaction().commit();
            if (preserveConsultants && account.getGuestUsers().size() > 0) {
                Consultant consultant = account.getGuestUsers().get(0);
                new AccountMemberInvitation().newProAccountWithConsultant(user.getEmail(), user.getUserName(), password, consultant.getUser().getName(),
                        consultant.getUser().getEmail());
            } else {
                new AccountMemberInvitation().newProAccount(user.getEmail(), user.getUserName(), password);
            }
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
        Session session = Database.instance().createSession();
        try {
            session.beginTransaction();
            List results = session.createQuery("from Account").list();
            for (Object obj : results) {
                Account account = (Account) obj;
                accounts.add(account.toAdminTO());
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return accounts;
    }


    public List<EIConsultant> getPendingConsultants() {
        SecurityUtil.authorizeAccountAdmin();
        List<EIConsultant> consultants = new ArrayList<EIConsultant>();
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from Consultant where state = ?").setInteger(0, Consultant.PENDING_EI_APPROVAL).list();
            for (Object obj : results) {
                Consultant consultant = (Consultant) obj;
                EIConsultant eiConsultant = consultant.toEIConsultant();
                Account account = consultant.getUser().getAccount();
                eiConsultant.setAccountName(account.getName());
                eiConsultant.setAccountID(account.getAccountID());
                consultants.add(eiConsultant);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return consultants;
    }

}

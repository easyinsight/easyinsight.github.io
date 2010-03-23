package com.easyinsight.users;

import com.easyinsight.preferences.UISettings;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * User: jboe
 * Date: Jan 2, 2008
 * Time: 5:33:36 PM
 */

@Entity
@Table(name="user")
public class User {
    @Column(name="username")
    private String userName;
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="user_id")
    private long userID;
    @Column(name="password")
    private String password;
    @Column(name="email")
    private String email;
    @Column(name="name")
    private String name;

    @Transient
    private UISettings uiSettings;

    @Column(name="first_name")
    private String firstName;

    @Column(name="title")
    private String title;

    @Column(name="persona_id")
    private Long personaID;

    @Column(name="permissions")
    private int permissions;

    @Column(name="account_admin")
    private boolean accountAdmin;
    @Column(name="data_source_creator")
    private boolean dataSourceCreator;
    @Column(name="insight_creator")
    private boolean insightCreator;

    @Column(name="user_key")
    private String userKey;
    @Column(name="user_secret_key")
    private String userSecretKey;

    @Column(name="last_login_date")
    private Date lastLoginDate;

    @ManyToOne
    @JoinColumn (name="account_id")
    private Account account;

    public User() {
    }

    public User(String userName, String password, String name, String email) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public UserTransferObject toUserTransferObject() {
        UserTransferObject userTransferObject = new UserTransferObject();
        userTransferObject.setUserID(userID);
        userTransferObject.setEmail(email);
        userTransferObject.setUserName(userName);
        userTransferObject.setName(name);
        userTransferObject.setAccountAdmin(accountAdmin);
        userTransferObject.setTitle(title);
        userTransferObject.setFirstName(firstName);
        userTransferObject.setPersonaID(personaID != null ? personaID : 0);
        return userTransferObject;
    }

    public UISettings getUiSettings() {
        return uiSettings;
    }

    public void setUiSettings(UISettings uiSettings) {
        this.uiSettings = uiSettings;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getPersonaID() {
        return personaID;
    }

    public void setPersonaID(Long personaID) {
        this.personaID = personaID;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAccountAdmin() {
        return accountAdmin;
    }

    public void setAccountAdmin(boolean accountAdmin) {
        this.accountAdmin = accountAdmin;
    }

    public boolean isDataSourceCreator() {
        return dataSourceCreator;
    }

    public void setDataSourceCreator(boolean dataSourceCreator) {
        this.dataSourceCreator = dataSourceCreator;
    }

    public boolean isInsightCreator() {
        return insightCreator;
    }

    public void setInsightCreator(boolean insightCreator) {
        this.insightCreator = insightCreator;
    }

    public void update(UserTransferObject transferObject) {
        setUserName(transferObject.getUserName());
        setAccountAdmin(transferObject.isAccountAdmin());
        setEmail(transferObject.getEmail());
        setFirstName(transferObject.getFirstName());
        setLastLoginDate(transferObject.getLastLoginDate());
        setPersonaID(transferObject.getPersonaID() > 0 ? transferObject.getPersonaID() : null);
        setName(transferObject.getName());
        setTitle(transferObject.getTitle());        
    }
}

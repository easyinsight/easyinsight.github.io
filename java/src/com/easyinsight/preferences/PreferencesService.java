package com.easyinsight.preferences;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import org.hibernate.Session;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 11:50:50 AM
 */
public class PreferencesService {

    public ApplicationSkin getUserSkin() {
        ApplicationSkin applicationSkin = null;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from ApplicationSkinSettings where userID = ?").setLong(0, SecurityUtil.getUserID()).list();
            if (results.size() > 0) {
                ApplicationSkinSettings settings = (ApplicationSkinSettings) results.get(0);
                applicationSkin = settings.toSkin();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
        return applicationSkin;
    }

    public long addImage(String imageName, byte[] bytes, boolean publicImage) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_IMAGE (image_bytes, image_name, user_id, public_visibility) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            BufferedInputStream bis = new BufferedInputStream(bais, 1024);
            insertStmt.setBinaryStream(1, bis, bytes.length);
            insertStmt.setString(2, imageName);
            insertStmt.setLong(3, userID);
            insertStmt.setBoolean(4, publicImage);
            insertStmt.execute();
            long id = Database.instance().getAutoGenKey(insertStmt);
            conn.commit();
            return id;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public byte[] getImage(long imageID) {
        byte[] bytes;
        long accountID;
        try {
            accountID = SecurityUtil.getAccountID();
        } catch (Exception e) {
            accountID = 0;
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM USER_IMAGE, USER WHERE USER_IMAGE_ID = ? AND ((USER.ACCOUNT_ID = ? AND USER_IMAGE.USER_ID = USER.USER_ID) OR (USER_IMAGE.public_visibility = ?))");
            queryStmt.setLong(1, imageID);
            queryStmt.setLong(2, accountID);
            queryStmt.setBoolean(3, true);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            bytes = rs.getBytes(1);
            conn.commit();
            return bytes;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public byte[] getImage(long imageID, EIConnection conn)  {
        byte[] bytes;
        long accountID;
        try {
            accountID = SecurityUtil.getAccountID();
        } catch (Exception e) {
            accountID = 0;
        }

        try {

            PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM USER_IMAGE WHERE USER_IMAGE_ID = ?");
            queryStmt.setLong(1, imageID);
            ResultSet rs = queryStmt.executeQuery();
            rs.next();
            bytes = rs.getBytes(1);

            return bytes;
        } catch (Exception e) {
            LogClass.error(e);

            throw new RuntimeException(e);
        }
    }

    public void deleteImage(long imageID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<ImageDescriptor> getImages() {
        List<ImageDescriptor> images = new ArrayList<ImageDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement("SELECT USER_IMAGE.image_name, USER_IMAGE.user_image_id FROM USER_IMAGE, USER WHERE USER.ACCOUNT_ID = ? AND USER_IMAGE.USER_ID = USER.USER_ID");
            stmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ImageDescriptor imageDescriptor = new ImageDescriptor();
                imageDescriptor.setName(rs.getString(1));
                imageDescriptor.setId(rs.getLong(2));
                images.add(imageDescriptor);
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return images;
    }

    public ApplicationSkin getGlobalSkin() {
        ApplicationSkin applicationSkin = null;
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from ApplicationSkinSettings where globalSkin = ?").setBoolean(0, true).list();
            if (results.size() > 0) {
                ApplicationSkinSettings settings = (ApplicationSkinSettings) results.get(0);
                applicationSkin = settings.toSkin();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
        return applicationSkin;
    }

    public ApplicationSkin getAccountSkin() {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from ApplicationSkinSettings where accountID = ?").setLong(0, SecurityUtil.getAccountID()).list();
            if (results.size() > 0) {
                ApplicationSkinSettings settings = (ApplicationSkinSettings) results.get(0);
                return settings.toSkin();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException();
        } finally {
            session.close();
        }
        return null;
    }

    public void saveGlobalSkin(ApplicationSkin skin) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Session session = Database.instance().createSession();
        try {
            ApplicationSkinSettings settings = skin.toSettings(ApplicationSkin.APPLICATION);
            settings.setGlobalSkin(true);
            session.getTransaction().begin();
            session.saveOrUpdate(settings);
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public ApplicationSkin saveUserSkin(ApplicationSkin skin) {
        ApplicationSkin result;
        Session session = Database.instance().createSession();
        try {
            ApplicationSkinSettings settings = skin.toSettings(ApplicationSkin.USER);
            settings.setUserID(SecurityUtil.getUserID());
            session.getTransaction().begin();
            session.saveOrUpdate(settings);
            result = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return result;
    }

    public ApplicationSkin saveAccountSkin(ApplicationSkin skin) {
        ApplicationSkin result;
        Session session = Database.instance().createSession();
        try {
            ApplicationSkinSettings settings = skin.toSettings(ApplicationSkin.ACCOUNT);
            settings.setAccountID(SecurityUtil.getAccountID());
            session.getTransaction().begin();
            session.saveOrUpdate(settings);
            result = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
            session.getTransaction().commit();
        } catch (Exception e) {
            LogClass.error(e);
            session.getTransaction().rollback();
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
        return result;
    }

    public List<Persona> getPersonas() {
        SecurityUtil.authorizeAccountAdmin();
        long accountID = SecurityUtil.getAccountID();
        List<Persona> personas = new ArrayList<Persona>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT PERSONA_ID, PERSONA_NAME FROM PERSONA WHERE ACCOUNT_ID = ?");
            queryStmt.setLong(1, accountID);
            ResultSet personaRS = queryStmt.executeQuery();
            while (personaRS.next()) {
                long personaID = personaRS.getLong(1);
                String personaName = personaRS.getString(2);
                Persona persona = new Persona();
                persona.setPersonaID(personaID);
                persona.setName(personaName);
                PreparedStatement visibilitySettingsStmt = conn.prepareStatement("SELECT CONFIG_ELEMENT, VISIBLE FROM " +
                        "ui_visibility_setting where persona_id = ?");
                visibilitySettingsStmt.setLong(1, personaID);
                List<UIVisibilitySetting> settings = new ArrayList<UIVisibilitySetting>();
                ResultSet visibilityRS = visibilitySettingsStmt.executeQuery();
                while (visibilityRS.next()) {
                    String key = visibilityRS.getString(1);
                    boolean visible = visibilityRS.getBoolean(2);
                    UIVisibilitySetting setting = new UIVisibilitySetting();
                    setting.setKey(key);
                    setting.setSelected(visible);
                    settings.add(setting);
                }
                UISettings uiSettings = new UISettings();
                uiSettings.setVisibilitySettings(settings);
                persona.setUiSettings(uiSettings);
                personas.add(persona);
                visibilitySettingsStmt.close();
                PreparedStatement dlsStmt = conn.prepareStatement("SELECT DLS_ID, DATA_SOURCE_ID, FEED_NAME FROM DLS, data_feed WHERE PERSONA_ID = ? AND " +
                        "DATA_FEED.DATA_FEED_ID = dls.data_source_id");
                PreparedStatement getFilterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dls_to_filter where dls_id = ?");
                dlsStmt.setLong(1, personaID);
                List<DataSourceDLS> dlsList = new ArrayList<DataSourceDLS>();
                ResultSet dlsRS = dlsStmt.executeQuery();
                while (dlsRS.next()) {
                    DataSourceDLS dls = new DataSourceDLS();
                    long dlsID = dlsRS.getLong(1);
                    long dataSourceID = dlsRS.getLong(2);
                    dls.setDataSourceID(dataSourceID);
                    dls.setDataSourceName(dlsRS.getString(3));
                    dls.setDataSourceDLSID(dlsID);
                    getFilterStmt.setLong(1, dlsID);
                    List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                    ResultSet filterRS = getFilterStmt.executeQuery();
                    while (filterRS.next()) {
                        long filterID = filterRS.getLong(1);
                        Session session = Database.instance().createSession(conn);
                        try {
                            List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                            if (results.size() > 0) {
                                FilterDefinition filter = (FilterDefinition) results.get(0);
                                filter.getField().afterLoad();
                                filter.afterLoad();
                                filters.add(filter);
                            }
                        } finally {
                            session.close();
                        }
                    }
                    dls.setFilters(filters);
                    dlsList.add(dls);
                }
                getFilterStmt.close();
                persona.setDataSourceDLS(dlsList);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return personas;
    }

    public List<UserDLS> getUserDLS(long userID, long personaID) {
        List<UserDLS> dls = new ArrayList<UserDLS>();
        EIConnection conn = Database.instance().getConnection();
        try {
            boolean personaChanged = false;
            if (userID != 0) {
                PreparedStatement getStmt = conn.prepareStatement("SELECT PERSONA_ID FROM USER WHERE USER_ID = ?");
                getStmt.setLong(1, userID);
                ResultSet personaRS = getStmt.executeQuery();
                personaRS.next();
                long existingPersonaID = personaRS.getLong(1);
                if (existingPersonaID != personaID) {
                    personaChanged = true;
                }
            }
            Map<Long, UserDLS> map = new HashMap<Long, UserDLS>();

            if (!personaChanged) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT USER_DLS_ID, DLS_ID FROM USER_DLS WHERE USER_ID = ?");
                PreparedStatement queryUserFilterStmt = conn.prepareStatement("SELECT FILTER_ID, ORIGINAL_FILTER_ID FROM user_dls_to_filter where user_dls_id = ?");
                queryStmt.setLong(1, userID);
                ResultSet rs = queryStmt.executeQuery();

                while (rs.next()) {
                    long userDLSID = rs.getLong(1);
                    long dlsID = rs.getLong(2);
                    queryUserFilterStmt.setLong(1, userDLSID);
                    ResultSet uFilterRS = queryUserFilterStmt.executeQuery();
                    UserDLS userDLS = new UserDLS();
                    userDLS.setDlsID(dlsID);
                    List<UserDLSFilter> userDLSFilterList = new ArrayList<UserDLSFilter>();
                    userDLS.setUserDLSFilterList(userDLSFilterList);
                    dls.add(userDLS);
                    map.put(dlsID, userDLS);
                    while (uFilterRS.next()) {
                        long filterID = uFilterRS.getLong(1);
                        long originalFilterID = uFilterRS.getLong(2);
                        UserDLSFilter userDLSFilter = new UserDLSFilter();
                        userDLSFilter.setOriginalFilterID(originalFilterID);
                        FilterDefinition filter;
                        Session session = Database.instance().createSession(conn);
                        try {
                            List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                            if (results.size() > 0) {
                                filter = (FilterDefinition) results.get(0);
                                filter.getField().afterLoad();
                                filter.afterLoad();
                            } else {
                                throw new RuntimeException("Could not find filter " + originalFilterID);
                            }
                        } finally {
                            session.close();
                        }
                        userDLSFilter.setFilterDefinition(filter);
                        userDLSFilterList.add(userDLSFilter);
                    }
                }
            }
            List<DataSourceDLS> dataSourceDLSList = getDataSourceDLS(personaID, conn);
            for (DataSourceDLS dataSourceDLS : dataSourceDLSList) {
                UserDLS userDLS = map.get(dataSourceDLS.getDataSourceDLSID());
                if (userDLS == null) {
                    userDLS = new UserDLS();
                    dls.add(userDLS);
                }
                userDLS.setDataSourceName(dataSourceDLS.getDataSourceName());
                userDLS.setDataSourceID(dataSourceDLS.getDataSourceID());
                userDLS.setDlsID(dataSourceDLS.getDataSourceDLSID());
                List<UserDLSFilter> userDLSFilterList = userDLS.getUserDLSFilterList();
                for (FilterDefinition filterDefinition : dataSourceDLS.getFilters()) {
                    boolean matched = false;
                    for (UserDLSFilter userDLSFilter : userDLSFilterList) {
                        if (userDLSFilter.getOriginalFilterID() == filterDefinition.getFilterID()) {
                            matched = true;
                            break;
                        }
                    }
                    if (!matched) {
                        FilterDefinition clonedFilter = filterDefinition.clone();
                        UserDLSFilter userDLSFilter = new UserDLSFilter();
                        userDLSFilter.setOriginalFilterID(filterDefinition.getFilterID());
                        userDLSFilter.setFilterDefinition(clonedFilter);
                        userDLSFilterList.add(userDLSFilter);
                    }
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return dls;
    }

    private List<DataSourceDLS> getDataSourceDLS(long personaID, EIConnection conn) throws SQLException {
        List<DataSourceDLS> dlsList = new ArrayList<DataSourceDLS>();
        PreparedStatement dlsStmt = conn.prepareStatement("SELECT DLS_ID, DATA_SOURCE_ID, FEED_NAME FROM DLS, data_feed WHERE PERSONA_ID = ? AND " +
                "DATA_FEED.DATA_FEED_ID = dls.data_source_id");
        PreparedStatement getFilterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dls_to_filter where dls_id = ?");
        dlsStmt.setLong(1, personaID);
        ResultSet dlsRS = dlsStmt.executeQuery();
        while (dlsRS.next()) {
            DataSourceDLS dls = new DataSourceDLS();
            long dlsID = dlsRS.getLong(1);
            long dataSourceID = dlsRS.getLong(2);
            dls.setDataSourceDLSID(dlsID);
            dls.setDataSourceID(dataSourceID);
            dls.setDataSourceName(dlsRS.getString(3));
            getFilterStmt.setLong(1, dlsID);
            List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
            ResultSet filterRS = getFilterStmt.executeQuery();
            while (filterRS.next()) {
                long filterID = filterRS.getLong(1);
                Session session = Database.instance().createSession(conn);
                try {
                    List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterID).list();
                    if (results.size() > 0) {
                        FilterDefinition filter = (FilterDefinition) results.get(0);
                        filter.getField().afterLoad();
                        filter.afterLoad();
                        filters.add(filter);
                    }
                } finally {
                    session.close();
                }
            }
            dls.setFilters(filters);
            dlsList.add(dls);
        }
        getFilterStmt.close();
        return dlsList;
    }

    public long savePersona(Persona persona) {
        SecurityUtil.authorizeAccountAdmin();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            savePersona(persona, conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return persona.getPersonaID();
    }

    public long savePersona(Persona persona, EIConnection conn) throws SQLException {
        if (persona.getPersonaID() == 0) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO PERSONA (PERSONA_NAME, ACCOUNT_ID) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, persona.getName());
            insertStmt.setLong(2, SecurityUtil.getAccountID());
            insertStmt.execute();
            persona.setPersonaID(Database.instance().getAutoGenKey(insertStmt));
        } else {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE PERSONA SET PERSONA_NAME = ? WHERE " +
                    "PERSONA_ID = ? AND ACCOUNT_ID = ?");
            updateStmt.setString(1, persona.getName());
            updateStmt.setLong(2, persona.getPersonaID());
            updateStmt.setLong(3, SecurityUtil.getAccountID());
            updateStmt.executeUpdate();
        }
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM UI_VISIBILITY_SETTING WHERE PERSONA_ID = ?");
        clearStmt.setLong(1, persona.getPersonaID());
        clearStmt.executeUpdate();
        PreparedStatement insertSettingStmt = conn.prepareStatement("INSERT INTO UI_VISIBILITY_SETTING (CONFIG_ELEMENT, VISIBLE, PERSONA_ID) " +
                "VALUES (?, ?, ?)");
        for (UIVisibilitySetting setting : persona.getUiSettings().getVisibilitySettings()) {
            insertSettingStmt.setString(1, setting.getKey());
            insertSettingStmt.setBoolean(2, setting.isSelected());
            insertSettingStmt.setLong(3, persona.getPersonaID());
            insertSettingStmt.execute();
        }
        if (SecurityUtil.getAccountTier() == Account.PERSONAL) {
            PreparedStatement updateUserStmt = conn.prepareStatement("UPDATE USER SET PERSONA_ID = ? WHERE USER_ID = ?");
            updateUserStmt.setLong(1, persona.getPersonaID());
            updateUserStmt.setLong(2, SecurityUtil.getUserID());
            updateUserStmt.executeUpdate();
        }
        /*PreparedStatement dlsClearStmt = conn.prepareStatement("DELETE FROM DLS WHERE PERSONA_ID = ?");
        dlsClearStmt.setLong(1, persona.getPersonaID());
        dlsClearStmt.executeUpdate();*/
        PreparedStatement getDlsStmt = conn.prepareStatement("SELECT DLS_ID FROM DLS WHERE PERSONA_ID = ?");
        getDlsStmt.setLong(1, persona.getPersonaID());
        ResultSet existingDLSRS = getDlsStmt.executeQuery();
        Set<Long> existingDLIDs = new HashSet<Long>();
        while (existingDLSRS.next()) {
            existingDLIDs.add(existingDLSRS.getLong(1));
        }
        for (DataSourceDLS dataSourceDLS : persona.getDataSourceDLS()) {
            existingDLIDs.remove(dataSourceDLS.getDataSourceDLSID());
        }
        PreparedStatement deleteDLSStmt = conn.prepareStatement("DELETE FROM DLS WHERE DLS_ID = ?");
        for (Long dlsID : existingDLIDs) {
            deleteDLSStmt.setLong(1, dlsID);
            deleteDLSStmt.executeUpdate();
        }
        PreparedStatement dlsAddStmt = conn.prepareStatement("INSERT INTO DLS (DATA_SOURCE_ID, PERSONA_ID) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        PreparedStatement getExistingFilterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dls_to_filter WHERE DLS_ID = ?");
        PreparedStatement deleteFilterStmt = conn.prepareStatement("DELETE FROM DLS_TO_FILTER WHERE filter_id = ?");
        PreparedStatement saveFiltersStmt = conn.prepareStatement("INSERT INTO dls_to_filter (dls_id, filter_id) VALUES (?, ?)");
        for (DataSourceDLS dls : persona.getDataSourceDLS()) {
            long dlsID;
            if (dls.getDataSourceDLSID() == 0) {
                dlsAddStmt.setLong(1, dls.getDataSourceID());
                dlsAddStmt.setLong(2, persona.getPersonaID());
                dlsAddStmt.execute();
                dlsID = Database.instance().getAutoGenKey(dlsAddStmt);
            } else {
                dlsID = dls.getDataSourceDLSID();
            }
            getExistingFilterStmt.setLong(1, dlsID);
            Set<Long> existingFilterIDs = new HashSet<Long>();
            ResultSet filterRS = getExistingFilterStmt.executeQuery();
            while (filterRS.next()) {
                existingFilterIDs.add(filterRS.getLong(1));
            }
            for (FilterDefinition filterDefinition : dls.getFilters()) {
                existingFilterIDs.remove(filterDefinition.getFilterID());
            }
            for (Long filterID : existingFilterIDs) {
                deleteFilterStmt.setLong(1, filterID);
                deleteFilterStmt.executeUpdate();
            }
            for (FilterDefinition filterDefinition : dls.getFilters()) {
                Session session = Database.instance().createSession(conn);
                try {
                    boolean newFilter = filterDefinition.getFilterID() == 0;
                    filterDefinition.beforeSave(session);
                    session.saveOrUpdate(filterDefinition);
                    session.flush();
                    if (newFilter) {
                        saveFiltersStmt.setLong(1, dlsID);
                        saveFiltersStmt.setLong(2, filterDefinition.getFilterID());
                        saveFiltersStmt.execute();
                    }
                } finally {
                    session.close();
                }
            }
        }
        getExistingFilterStmt.close();
        return persona.getPersonaID();
    }


}

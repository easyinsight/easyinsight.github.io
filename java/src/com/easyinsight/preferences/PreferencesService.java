package com.easyinsight.preferences;

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
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 11:50:50 AM
 */
public class PreferencesService {

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
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT IMAGE_BYTES FROM USER_IMAGE, USER WHERE (USER_IMAGE_ID = ? AND USER.ACCOUNT_ID = ? AND USER_IMAGE.USER_ID = USER.USER_ID) OR " +
                    "USER_IMAGE.PUBLIC_VISIBILITY = ?");
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

    public ImageDescriptor getImages() {
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
        return null;
    }

    public ApplicationSkin getGlobalSkin() {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from ApplicationSkinSettings where globalSkin = ?").setBoolean(0, true).list();
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

    public ApplicationSkin getUserSkin() {
        Session session = Database.instance().createSession();
        try {
            session.getTransaction().begin();
            List results = session.createQuery("from ApplicationSkinSettings where userID = ?").setLong(0, SecurityUtil.getUserID()).list();
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
            ApplicationSkinSettings settings = skin.toSettings();
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
            ApplicationSkinSettings settings = skin.toSettings();
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
            ApplicationSkinSettings settings = skin.toSettings();
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
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return personas;
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
        return persona.getPersonaID();
    }


}

package com.easyinsight.preferences;

import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 22, 2010
 * Time: 2:46:54 PM
 */
public class UISettingRetrieval {
    public static UISettings getUISettings(long personaID, EIConnection conn, Account account) throws SQLException {
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
        visibilitySettingsStmt.close();
        UISettings uiSettings = new UISettings();
        uiSettings.setVisibilitySettings(settings);
        uiSettings.setMarketplace(account.isMarketplaceEnabled());
        uiSettings.setPublicSharing(account.isPublicDataEnabled());
        uiSettings.setReportSharing(account.isReportSharingEnabled());
        return uiSettings;
    }
}

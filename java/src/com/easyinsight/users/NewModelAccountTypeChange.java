package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 9/6/12
 * Time: 2:18 PM
 */
public class NewModelAccountTypeChange implements Serializable {
    private int addonDesigners;

    private int addonStorage;

    private int addonConnections;
    private boolean yearly;
    private boolean chargeNow;

    public boolean isChargeNow() {
        return chargeNow;
    }

    public void setChargeNow(boolean chargeNow) {
        this.chargeNow = chargeNow;
    }

    public int getAddonDesigners() {
        return addonDesigners;
    }

    public void setAddonDesigners(int addonDesigners) {
        this.addonDesigners = addonDesigners;
    }

    public int getAddonStorage() {
        return addonStorage;
    }

    public void setAddonStorage(int addonStorage) {
        this.addonStorage = addonStorage;
    }

    public int getAddonConnections() {
        return addonConnections;
    }

    public void setAddonConnections(int addonConnections) {
        this.addonConnections = addonConnections;
    }

    public boolean isYearly() {
        return yearly;
    }

    public void setYearly(boolean yearly) {
        this.yearly = yearly;
    }

    public String storageString(long baseStorage) {

        long storageAmount = baseStorage + ((long) addonStorage * 250000000L);

        return Account.humanReadableByteCount(storageAmount, true);
    }

    public void apply(Account account, Session session) {
        account.setAddonDesigners(addonDesigners);
        account.setAddonSmallBizConnections(addonConnections);
        account.setAddonStorageUnits(addonStorage);

        session.update(account);
        session.flush();
        try {
            SecurityUtil.changeAccountType(account.getAccountType());
        } catch (Exception e) {

        }
    }
}

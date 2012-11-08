package com.easyinsight.users;

import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: 9/6/12
 * Time: 2:18 PM
 */
public class AccountTypeChange implements Serializable {
    private int designers;
    private int accountType;
    private int storage;
    private boolean yearly;
    private boolean chargeNow;

    public boolean isChargeNow() {
        return chargeNow;
    }

    public void setChargeNow(boolean chargeNow) {
        this.chargeNow = chargeNow;
    }

    public int getDesigners() {
        return designers;
    }

    public void setDesigners(int designers) {
        this.designers = designers;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

    public boolean isYearly() {
        return yearly;
    }

    public void setYearly(boolean yearly) {
        this.yearly = yearly;
    }

    public String storageString() {

        long storageAmount = 0;
        if (accountType == Account.PROFESSIONAL) {
            if (storage == 1) {
                storageAmount = Account.PROFESSIONAL_MAX;
            } else if (storage == 2) {
                storageAmount = Account.PROFESSIONAL_MAX_2;
            } else if (storage == 3) {
                storageAmount = Account.PROFESSIONAL_MAX_3;
            } else if (storage == 4) {
                storageAmount = Account.PROFESSIONAL_MAX_4;
            }
        } else if (accountType == Account.PLUS) {
            if (storage == 1) {
                storageAmount = Account.PLUS_MAX;
            } else if (storage == 2) {
                storageAmount = Account.PLUS_MAX2;
            } else if (storage == 3) {
                storageAmount = Account.PLUS_MAX3;
            }
        } else if (accountType == Account.BASIC) {
            if (storage == 1) {
                storageAmount = Account.BASIC_MAX;
            } else if (storage == 2) {
                storageAmount = Account.BASIC_MAX2;
            } else if (storage == 3) {
                storageAmount = Account.BASIC_MAX3;
            }
        }
        return Account.humanReadableByteCount(storageAmount, true);
    }

    public void apply(Account account, Session session) {
        account.setMaxUsers(designers);

        account.setAccountType(accountType);

        long storageAmount = 0;
        if (accountType == Account.PROFESSIONAL) {
            if (storage == 1) {
                storageAmount = Account.PROFESSIONAL_MAX;
            } else if (storage == 2) {
                storageAmount = Account.PROFESSIONAL_MAX_2;
            } else if (storage == 3) {
                storageAmount = Account.PROFESSIONAL_MAX_3;
            } else if (storage == 4) {
                storageAmount = Account.PROFESSIONAL_MAX_4;
            }
        } else if (accountType == Account.PLUS) {
            if (storage == 1) {
                storageAmount = Account.PLUS_MAX;
            } else if (storage == 2) {
                storageAmount = Account.PLUS_MAX2;
            } else if (storage == 3) {
                storageAmount = Account.PLUS_MAX3;
            }
        } else if (accountType == Account.BASIC) {
            if (storage == 1) {
                storageAmount = Account.BASIC_MAX;
            } else if (storage == 2) {
                storageAmount = Account.BASIC_MAX2;
            } else if (storage == 3) {
                storageAmount = Account.BASIC_MAX3;
            }
        }
        account.setMaxSize(storageAmount);
        session.update(account);
        session.flush();
        SecurityUtil.changeAccountType(account.getAccountType());
    }
}

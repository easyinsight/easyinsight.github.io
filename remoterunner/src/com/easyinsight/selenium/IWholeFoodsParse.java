package com.easyinsight.selenium;

import java.rmi.RemoteException;

/**
 * User: jamesboe
 * Date: Oct 14, 2010
 * Time: 11:17:22 AM
 */
public interface IWholeFoodsParse {
    void addPage(String str) throws RemoteException;

    void done() throws RemoteException;
}

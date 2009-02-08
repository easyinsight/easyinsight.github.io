/**
 * BasicAuthUncheckedPublish.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.solutions.dbservice.webservice;

public interface BasicAuthUncheckedPublish extends java.rmi.Remote {
    public java.lang.String updateRow(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Row row, com.easyinsight.solutions.dbservice.webservice.Where where) throws java.rmi.RemoteException;
    public java.lang.String addRows(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Row[] rows) throws java.rmi.RemoteException;
    public java.lang.String replaceRows(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Row[] rows) throws java.rmi.RemoteException;
    public boolean validateCredentials() throws java.rmi.RemoteException;
    public void deleteRows(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Where where) throws java.rmi.RemoteException;
    public java.lang.String updateRows(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Row[] rows, com.easyinsight.solutions.dbservice.webservice.Where where) throws java.rmi.RemoteException;
    public java.lang.String addRow(java.lang.String dataSourceName, com.easyinsight.solutions.dbservice.webservice.Row arg1) throws java.rmi.RemoteException;
    public void disableUnchecked(java.lang.String dataSourceKey) throws java.rmi.RemoteException;
}

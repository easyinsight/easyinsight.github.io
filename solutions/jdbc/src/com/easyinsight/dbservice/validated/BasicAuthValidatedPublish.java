/**
 * BasicAuthValidatedPublish.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.dbservice.validated;

public interface BasicAuthValidatedPublish extends java.rmi.Remote {
    public void updateRow(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Row row, com.easyinsight.dbservice.validated.Where where) throws java.rmi.RemoteException;
    public void addRows(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Row[] rows) throws java.rmi.RemoteException;
    public void replaceRows(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Row[] rows) throws java.rmi.RemoteException;
    public boolean validateCredentials() throws java.rmi.RemoteException;
    public void deleteRows(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Where where) throws java.rmi.RemoteException;
    public void updateRows(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Row[] rows, com.easyinsight.dbservice.validated.Where where) throws java.rmi.RemoteException;
    public void addRow(java.lang.String dataSourceKey, com.easyinsight.dbservice.validated.Row row) throws java.rmi.RemoteException;
}

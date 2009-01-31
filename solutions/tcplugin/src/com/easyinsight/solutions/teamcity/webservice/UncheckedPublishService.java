/**
 * UncheckedPublishService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.solutions.teamcity.webservice;

public interface UncheckedPublishService extends java.rmi.Remote {
    public void updateRow(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Row row, com.easyinsight.solutions.teamcity.webservice.Where where) throws java.rmi.RemoteException;
    public void addRows(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Row[] rows) throws java.rmi.RemoteException;
    public void replaceRows(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Row[] rows) throws java.rmi.RemoteException;
    public void deleteRows(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Where where) throws java.rmi.RemoteException;
    public void updateRows(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Row[] rows, com.easyinsight.solutions.teamcity.webservice.Where where) throws java.rmi.RemoteException;
    public void addRow(java.lang.String dataSourceName, com.easyinsight.solutions.teamcity.webservice.Row arg1) throws java.rmi.RemoteException;
}

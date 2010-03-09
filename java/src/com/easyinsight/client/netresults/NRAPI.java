/**
 * NRAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public interface NRAPI extends javax.xml.rpc.Service {

/**
 * The Net-Results API
 */
    public java.lang.String getNRAPIPortAddress();

    public com.easyinsight.client.netresults.NRAPIPortType getNRAPIPort() throws javax.xml.rpc.ServiceException;

    public com.easyinsight.client.netresults.NRAPIPortType getNRAPIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}

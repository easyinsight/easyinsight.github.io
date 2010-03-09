/**
 * NRAPILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.client.netresults;

public class NRAPILocator extends org.apache.axis.client.Service implements com.easyinsight.client.netresults.NRAPI {

/**
 * The Net-Results API
 */

    public NRAPILocator() {
    }


    public NRAPILocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public NRAPILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for NRAPIPort
    private java.lang.String NRAPIPort_address = "https://apps.net-results.com/soap/v1/NRAPI.php";

    public java.lang.String getNRAPIPortAddress() {
        return NRAPIPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String NRAPIPortWSDDServiceName = "NRAPIPort";

    public java.lang.String getNRAPIPortWSDDServiceName() {
        return NRAPIPortWSDDServiceName;
    }

    public void setNRAPIPortWSDDServiceName(java.lang.String name) {
        NRAPIPortWSDDServiceName = name;
    }

    public com.easyinsight.client.netresults.NRAPIPortType getNRAPIPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(NRAPIPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getNRAPIPort(endpoint);
    }

    public com.easyinsight.client.netresults.NRAPIPortType getNRAPIPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.easyinsight.client.netresults.NRAPIBindingStub _stub = new com.easyinsight.client.netresults.NRAPIBindingStub(portAddress, this);
            _stub.setPortName(getNRAPIPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setNRAPIPortEndpointAddress(java.lang.String address) {
        NRAPIPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.easyinsight.client.netresults.NRAPIPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.easyinsight.client.netresults.NRAPIBindingStub _stub = new com.easyinsight.client.netresults.NRAPIBindingStub(new java.net.URL(NRAPIPort_address), this);
                _stub.setPortName(getNRAPIPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("NRAPIPort".equals(inputPortName)) {
            return getNRAPIPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1", "NRAPI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://apps.net-results.com/soap/v1", "NRAPIPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("NRAPIPort".equals(portName)) {
            setNRAPIPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}

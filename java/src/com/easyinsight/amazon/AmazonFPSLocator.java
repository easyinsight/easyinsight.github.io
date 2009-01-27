/**
 * AmazonFPSLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class AmazonFPSLocator extends org.apache.axis.client.Service implements com.easyinsight.amazon.AmazonFPS {

    public AmazonFPSLocator() {
    }


    public AmazonFPSLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public AmazonFPSLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for AmazonFPSPort
    private java.lang.String AmazonFPSPort_address = "https://fps.amazonaws.com";

    public java.lang.String getAmazonFPSPortAddress() {
        return AmazonFPSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String AmazonFPSPortWSDDServiceName = "AmazonFPSPort";

    public java.lang.String getAmazonFPSPortWSDDServiceName() {
        return AmazonFPSPortWSDDServiceName;
    }

    public void setAmazonFPSPortWSDDServiceName(java.lang.String name) {
        AmazonFPSPortWSDDServiceName = name;
    }

    public com.easyinsight.amazon.AmazonFPSPortType getAmazonFPSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(AmazonFPSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getAmazonFPSPort(endpoint);
    }

    public com.easyinsight.amazon.AmazonFPSPortType getAmazonFPSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.easyinsight.amazon.AmazonFPSBindingStub _stub = new com.easyinsight.amazon.AmazonFPSBindingStub(portAddress, this);
            _stub.setPortName(getAmazonFPSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setAmazonFPSPortEndpointAddress(java.lang.String address) {
        AmazonFPSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.easyinsight.amazon.AmazonFPSPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.easyinsight.amazon.AmazonFPSBindingStub _stub = new com.easyinsight.amazon.AmazonFPSBindingStub(new java.net.URL(AmazonFPSPort_address), this);
                _stub.setPortName(getAmazonFPSPortWSDDServiceName());
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
        if ("AmazonFPSPort".equals(inputPortName)) {
            return getAmazonFPSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AmazonFPS");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "AmazonFPSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("AmazonFPSPort".equals(portName)) {
            setAmazonFPSPortEndpointAddress(address);
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

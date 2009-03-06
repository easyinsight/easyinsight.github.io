/**
 * BasicAuthUncheckedPublishServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.solutions.teamcity.webservice;

public class BasicAuthUncheckedPublishServiceServiceLocator extends org.apache.axis.client.Service implements com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublishServiceService {

    public BasicAuthUncheckedPublishServiceServiceLocator() {
    }


    public BasicAuthUncheckedPublishServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BasicAuthUncheckedPublishServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicAuthUncheckedPublishServicePort
    private java.lang.String BasicAuthUncheckedPublishServicePort_address = "http://localhost:8080/app/services/UncheckedPublishBasic";

    public java.lang.String getBasicAuthUncheckedPublishServicePortAddress() {
        return BasicAuthUncheckedPublishServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicAuthUncheckedPublishServicePortWSDDServiceName = "BasicAuthUncheckedPublishServicePort";

    public java.lang.String getBasicAuthUncheckedPublishServicePortWSDDServiceName() {
        return BasicAuthUncheckedPublishServicePortWSDDServiceName;
    }

    public void setBasicAuthUncheckedPublishServicePortWSDDServiceName(java.lang.String name) {
        BasicAuthUncheckedPublishServicePortWSDDServiceName = name;
    }

    public com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublish getBasicAuthUncheckedPublishServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicAuthUncheckedPublishServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicAuthUncheckedPublishServicePort(endpoint);
    }

    public com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublish getBasicAuthUncheckedPublishServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublishServiceServiceSoapBindingStub _stub = new com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublishServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getBasicAuthUncheckedPublishServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicAuthUncheckedPublishServicePortEndpointAddress(java.lang.String address) {
        BasicAuthUncheckedPublishServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublish.class.isAssignableFrom(serviceEndpointInterface)) {
                com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublishServiceServiceSoapBindingStub _stub = new com.easyinsight.solutions.teamcity.webservice.BasicAuthUncheckedPublishServiceServiceSoapBindingStub(new java.net.URL(BasicAuthUncheckedPublishServicePort_address), this);
                _stub.setPortName(getBasicAuthUncheckedPublishServicePortWSDDServiceName());
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
        if ("BasicAuthUncheckedPublishServicePort".equals(inputPortName)) {
            return getBasicAuthUncheckedPublishServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "BasicAuthUncheckedPublishServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "BasicAuthUncheckedPublishServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicAuthUncheckedPublishServicePort".equals(portName)) {
            setBasicAuthUncheckedPublishServicePortEndpointAddress(address);
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

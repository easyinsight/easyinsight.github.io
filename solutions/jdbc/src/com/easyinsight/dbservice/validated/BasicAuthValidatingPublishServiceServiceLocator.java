/**
 * BasicAuthValidatingPublishServiceServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.dbservice.validated;

public class BasicAuthValidatingPublishServiceServiceLocator extends org.apache.axis.client.Service implements com.easyinsight.dbservice.validated.BasicAuthValidatingPublishServiceService {

    public BasicAuthValidatingPublishServiceServiceLocator() {
    }


    public BasicAuthValidatingPublishServiceServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public BasicAuthValidatingPublishServiceServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicAuthValidatingPublishServicePort
    private java.lang.String BasicAuthValidatingPublishServicePort_address = "http://localhost:8080/app/services/ValidatedPublishBasic";

    public java.lang.String getBasicAuthValidatingPublishServicePortAddress() {
        return BasicAuthValidatingPublishServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicAuthValidatingPublishServicePortWSDDServiceName = "BasicAuthValidatingPublishServicePort";

    public java.lang.String getBasicAuthValidatingPublishServicePortWSDDServiceName() {
        return BasicAuthValidatingPublishServicePortWSDDServiceName;
    }

    public void setBasicAuthValidatingPublishServicePortWSDDServiceName(java.lang.String name) {
        BasicAuthValidatingPublishServicePortWSDDServiceName = name;
    }

    public com.easyinsight.dbservice.validated.BasicAuthValidatedPublish getBasicAuthValidatingPublishServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicAuthValidatingPublishServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicAuthValidatingPublishServicePort(endpoint);
    }

    public com.easyinsight.dbservice.validated.BasicAuthValidatedPublish getBasicAuthValidatingPublishServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.easyinsight.dbservice.validated.BasicAuthValidatingPublishServiceServiceSoapBindingStub _stub = new com.easyinsight.dbservice.validated.BasicAuthValidatingPublishServiceServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getBasicAuthValidatingPublishServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicAuthValidatingPublishServicePortEndpointAddress(java.lang.String address) {
        BasicAuthValidatingPublishServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.easyinsight.dbservice.validated.BasicAuthValidatedPublish.class.isAssignableFrom(serviceEndpointInterface)) {
                com.easyinsight.dbservice.validated.BasicAuthValidatingPublishServiceServiceSoapBindingStub _stub = new com.easyinsight.dbservice.validated.BasicAuthValidatingPublishServiceServiceSoapBindingStub(new java.net.URL(BasicAuthValidatingPublishServicePort_address), this);
                _stub.setPortName(getBasicAuthValidatingPublishServicePortWSDDServiceName());
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
        if ("BasicAuthValidatingPublishServicePort".equals(inputPortName)) {
            return getBasicAuthValidatingPublishServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "BasicAuthValidatingPublishServiceService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://basicauth.api.easyinsight.com/", "BasicAuthValidatingPublishServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicAuthValidatingPublishServicePort".equals(portName)) {
            setBasicAuthValidatingPublishServicePortEndpointAddress(address);
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

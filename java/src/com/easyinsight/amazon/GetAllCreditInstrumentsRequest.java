/**
 * GetAllCreditInstrumentsRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.easyinsight.amazon;

public class GetAllCreditInstrumentsRequest  implements java.io.Serializable {
    private com.easyinsight.amazon.InstrumentStatus instrumentStatus;

    public GetAllCreditInstrumentsRequest() {
    }

    public GetAllCreditInstrumentsRequest(
           com.easyinsight.amazon.InstrumentStatus instrumentStatus) {
           this.instrumentStatus = instrumentStatus;
    }


    /**
     * Gets the instrumentStatus value for this GetAllCreditInstrumentsRequest.
     * 
     * @return instrumentStatus
     */
    public com.easyinsight.amazon.InstrumentStatus getInstrumentStatus() {
        return instrumentStatus;
    }


    /**
     * Sets the instrumentStatus value for this GetAllCreditInstrumentsRequest.
     * 
     * @param instrumentStatus
     */
    public void setInstrumentStatus(com.easyinsight.amazon.InstrumentStatus instrumentStatus) {
        this.instrumentStatus = instrumentStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetAllCreditInstrumentsRequest)) return false;
        GetAllCreditInstrumentsRequest other = (GetAllCreditInstrumentsRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.instrumentStatus==null && other.getInstrumentStatus()==null) || 
             (this.instrumentStatus!=null &&
              this.instrumentStatus.equals(other.getInstrumentStatus())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getInstrumentStatus() != null) {
            _hashCode += getInstrumentStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetAllCreditInstrumentsRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", ">GetAllCreditInstrumentsRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instrumentStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("", "InstrumentStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://fps.amazonaws.com/doc/2007-01-08/", "InstrumentStatus"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
